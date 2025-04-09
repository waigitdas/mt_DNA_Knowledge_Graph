/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.dnode;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class haplopath_creator {
    @UserFunction
    @Description("cretes dnode nodes and related elments from the phylotree.")

    public String create_haplopaths(

  )
   
         { 
             
        create_hps();
         return "";
            }

    
    public static void main(String args[]) {

        create_hps();
    }
    
     public static String create_hps() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String rsrs ="rsrs";
        
       mt_var.neo4jlib.neo4j_qry.CreateIndex(" variant","name");
       mt_var.neo4jlib.neo4j_qry.CreateIndex(" dnode","name");
       mt_var.neo4jlib.neo4j_qry.CreateIndex(" dnode","op");
       mt_var.neo4jlib.neo4j_qry.CreateIndex(" dnode","path");
       mt_var.neo4jlib.neo4j_qry.CreateIndex(" dnode","lvl");

       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h: dnode)-[r]-() delete r");
       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=()-[r: block_dnode]->() delete r");
       mt_var.neo4jlib.neo4j_qry.qry_write("match (h: dnode) delete h");
        
       //set end of line property
       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b1: block) with b1 optional match (b1)-[r: block_child]->(b2: block) with b1 where b2 is null set b1.end_of_path=1");
       
        //create   dnode nodes. Different root nodes
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b1: block{name:'RSRS'})-[r: block_child*0..50]->(b2: block)  with b2,[x in nodes(p)|x.name] as path,[y in nodes(p) |id(y)] as op with distinct b2,path,size(op)-1 as lvl,mt_var.graph.get_ordpath(op) as op merge(hp: dnode{path:path,name:last(path),op:op, branch_variants:case when b2.branch_variants is null then '' else b2.branch_variants end, lvl:lvl})");
        
        //create block_haplogroup relationship
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (b: block) with b match (h: dnode) where h.name=b.name  merge (b)-[r: block_dnode]->(h)");
        
        //create   dnode_variant relationship
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (b)-[r: block_dnode]->(h) with h,b match (v: variant) where v.name in b.path_variants  merge (h)-[rh: dnode_variant]->(v)");
     
        //set dnode end of path
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b)-[r: block_dnode]->(h) where b.end_of_path is not null set h.end_of_path=b.end_of_path");
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //HARMONIZE DATA TO OPTIMIZE HAPLOGROUP ASSIGNMENT
        //at this point the dnode terminal variants may include back mutations.
        //get filtered list of variants and create   dnode property path_variants
        //the collection excludes tansversions and converts back mutations to their ancestral form using the UDF 
        //this query runs amazingly fast: 666 msec to create 6019 properties
        //fixes lower case variants used by FTDNA to denote transversions.
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(h)-[r: dnode_variant]->(v) with h,v with h,v order by v.pos with h,v, v.name as vn with h, vn  with h, vn as vnu with h,collect(vnu) as vs set h.path_variants=vs");
                
    
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b)-[r: block_dnode]->(h) with h,b unwind b.branch_variants as x call { with x with x as z with z  return z } with h,collect(z) as branch_variants set h.branch_variants=branch_variants");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b1)-[r: block_child]->(b2) with b1,b2 match (h1: dnode{name:b1.name}) with b1,b2,h1 match (h2: dnode{name:b2.name}) merge (h1)-[rh: dnode_child]->(h2)");
        
//        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b)-[r: block_dnode]->(h) with h,b unwind b.variants as x call { with x with x as z with z where right(z,1) in ['A','C','G','T','a','c','g','t', 'd']  with case when right(trim(z),1)='d' then z else toUpper(z) end as z return z } with h,collect(z) as branch_variants set h.branch_variants=branch_variants");
//        
//        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b1)-[r: block_child]->(b2) with b1,b2 match (h1: dnode{name:b1.name}) with b1,b2,h1 match (h2: dnode{name:b2.name}) merge (h1)-[rh: dnode_child]->(h2)");
 
        mt_var.back_mutations.haplopath_back_mutations bmut = new mt_var.back_mutations.haplopath_back_mutations();
        bmut.process_back_mutations();
        
      
        /////////////////////////////////////////////////////////
        // create anchor variant: variants which appear only once in the haplotree and thereby are anchored to a specific branch
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h: dnode) with h unwind h.branch_variants as x call {with x return toUpper(x) as y } with y, count(*) as ct with y where ct=1 match (h2: dnode) where size(h2.branch_variants)>0 with y, h2 where apoc.coll.contains(h2.branch_variants, y)  with collect(y) as variants,h2 set h2.anchor_variants= variants ");
        
        //variants downstream from anchors
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path1=(h1)-[r: dnode_child*0..99]->(h2) where h1.anchor_variants is not null and h2.anchor_variants is null with h2 match path2=(h3: dnode{name:h2.name})-[r2: dnode_child*0..30]->(h4) where h3.name<>h4.name with h2,h3,h4,apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten([x in nodes(path2) where h3.name<>h4.name|x.branch_variants]))) as dsv set h4.downstream_var=dsv");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(h)-[r: dnode_variant]->(v) where h.anchor_variants is not null and v.name in h.anchor_variants set r.var_type='anchor'");
        
        //probe_dnode created from parsing Genbank sequence and its assigned_hg
        mt_var.neo4jlib.neo4j_qry.qry_write("match(s:mt_seq) where s.assigned_hg is not null with s match (h: dnode{name:s.assigned_hg}) where h.anchor_variants is null with s, h unwind h.branch_variants as x call { with x,s match(p:probe)-[r: probe_variant]-(v: variant{name:x}) where s.fullSeq<>replace(s.fullSeq,p.probe,'') and x is not null return p, v } with p, h, size(collect(s.name)) as size_seq, v.name as variant merge (p)-[rph: probe_dnode{variant:variant, var_type:'non_anchor'}]->(h)");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s)-[r:seq_probe]->(p) where s.assigned_hg is not null with s,p match (h: dnode{name:s.assigned_hg}) with p, h, count(*) as ct merge (p)-[rph:probe_dnode{seq_ct:ct, var_type:'seq_probe'}]->(h)");
        
        //this query filters path_variants to remove any depreciated_back_mutations and saves it as the filtered_path_variant
        //this uses the double relationship of the dnode_variant relationship to both the variant and back_mutation
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(h: dnode)-[r: dnode_variant]->(v: variant) with h,v.pos as pos,collect(v.name) as vn with h,vn where size(vn)>1 with h, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(apoc.coll.flatten(collect(vn))))) as vnn with h,apoc.coll.subtract(h.path_variants,vnn) as vns match (vv: variant) where vv.name in vns with h,vv order by vv.pos with h,collect(vv.name) as vns2 set h.filtered_path_variants=vns2");
        
        //sort variant lists for easier viewing
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h: dnode) where size(h.branch_variants)>1 set h.branch_variants=mt_var.sorter.sort_variant_list_by_pos_toList(h.branch_variants) ");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h: dnode) where size(h.path_variants)>1 set h.path_variants=mt_var.sorter.sort_variant_list_by_pos_toList(h.path_variants) ");
        
        
        return "";
    }
}

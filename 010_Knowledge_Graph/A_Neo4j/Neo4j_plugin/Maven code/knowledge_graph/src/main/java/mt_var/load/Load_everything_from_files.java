/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class Load_everything_from_files {
    @UserFunction
    @Description("Creates knowledge graph by loading initial data and sequentially augmenting with analytics.")

    public String load_all_data_sequentially(
  
  )
   
         { 
             
        load_graph();
         return "";
            }

    
    
    public static void main(String args[]) {
        load_graph();
    }
    
     public static String load_graph() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        
        String cq = "";

      String fnt = "traker_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        File ft = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fnt);
        FileWriter fwt = null;
        
        try
        {
            mt_var.neo4jlib.neo4j_info.fwtracker = new FileWriter(ft);
            mt_var.neo4jlib.neo4j_info.fwtracker.write("date_time,elapsed_time,item\n");
        }
       catch(IOException e){}
        mt_var.neo4jlib.neo4j_info.start_time = System.nanoTime();
        mt_var.genlib.tracker.writeTracker("start time");
 
        
        mt_var.neo4jlib.neo4j_qry.CreateIndex("mt_seq", "name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe_pos");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "ref_probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "seq_probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex( "probe", "probe_name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "var_alert");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "method");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "parts_pattern");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe_id");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "source");
 
        

        ///////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////

       //load lookup probes
       String luf = "rsrs_look_up_probes_240531.csv";
       mt_var.neo4jlib.file_lib.copyFileToImportDirectory(luf);
       mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + luf + "' as line FIELDTERMINATOR ','  create (l:lookup_probe{probe:toString(line.probe), pos:toInteger(line.pos), refseq:toString(line.refseq)})");

       mt_var.load.load_heteroplasmy_codes_hg lhc = new mt_var.load.load_heteroplasmy_codes_hg();
       lhc.load_codes_for_heteroplasmy();
       
        mt_var.load.load_seq ls = new mt_var.load.load_seq();
        ls.loads_GenBank_sequences();
        mt_var.genlib.tracker.writeTracker("sequences loaded");
        
        mt_var.load.create_pos_probes lpp = new mt_var.load.create_pos_probes();
        lpp.loads_pos_probes_from_file();
        mt_var.genlib.tracker.writeTracker("position probes loaded");
        
        mt_var.load.load_probes_variants_relationahips lv = new mt_var.load.load_probes_variants_relationahips();
        lv.loads_variants_from_file();
        mt_var.genlib.tracker.writeTracker("variants loaded");
        
        

        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s)-[r:seq_probe]->(p) with s,collect(p.probe) as ps set s.probe_ct= size(ps)");
        

        //////////////////////////////////////////////////////////////////////////

        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s)-[r:seq_probe]->(p) with s,collect(p.probe) as ps set s.probe_ct= size(ps)");
         mt_var.genlib.tracker.writeTracker("seq_probe relationship created");
        
    
        mt_var.load.upload_haplotree ursrs = new mt_var.load.upload_haplotree();
        ursrs.upload_FTDNA_rsrs_haplotree();
        mt_var.genlib.tracker.writeTracker("FTDNA mt-haplotree loaded");     
        
        mt_var.dnode.haplopath_creator hpc = new mt_var.dnode.haplopath_creator();
        hpc.create_haplopaths();
        mt_var.genlib.tracker.writeTracker("haplopaths created created");
       
         //////////////////////////////////////////////////////////////////////////
       //set up probe_patterns
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq)-[r:seq_probe]->(p) where p.subsumed_by is null with s,p order by p.ref_start_pos, p.probe with s.assigned_hg as hg, collect(distinct p.probe) as probes, collect(distinct s.name) as seqs  with hg, size(probes) as probe_ct, size(seqs) as seq_ct, probes as probe_pattern match(b:block{name:hg})  merge (b)-[r:block_probe_pattern]-(pp:probe_pattern{pattern:probe_pattern})");

        //fix sort of patterns so they are aligned across all patterns
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe_pattern) set p.pattern=apoc.coll.sort(p.pattern)");

       //remove 5 patterns with single probe. This affects 6 mt_seq and 5 haplogroups. These seem to be mt_seq with anomalous sequence assembly
//        mt_pangenome.neo4jlib.neo4j_qry.qry_write("MATCH ()-[r:block_probe_pattern]->(p) where size(p.pattern)=1 delete r,p");
//        mt_pangenome.genlib.tracker.writeTracker("probe pattern nodes create.");

       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (b)-[r:block_probe_pattern]->(p)  set p.hg=b.name");
 

         
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq)-[r:seq_probe]->(p:probe) with distinct s, p with s, collect(p) as ps set s.probe_ct= size(ps) ");

        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:probe_pattern) set n.ppid=id(n)");
        
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe_pattern", "ppid");
         mt_var.genlib.tracker.writeTracker("probe patterns created");
        
        /////////////////////////////////////////////////////////////////////////////////////////
  
        
               mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq)-[r:seq_probe]->(p) where p.sumsumed_by is null with s,p order by p.ref_start_pos, p.probe with s.assigned_hg as hg, collect(distinct p.probe) as probes, collect(distinct s.name) as seqs  with hg, size(probes) as probe_ct, size(seqs) as seq_ct, probes as probe_pattern match(h:dnode{name:hg})  merge (h)-[r:dnode_probe_pattern]-(pp:probe_pattern{pattern:probe_pattern})");

  
               mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe)-[r:probe_variant]->(v) where p.subsumed_by is null with v,p order by p.pid with v, collect(distinct p.pid) as pids set v.probe_ct=size(pids), v.pids= pids");
               
             mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe_pattern) set p.probe_ct=size(p.pattern) ");
   
       
            //set dnode sequence ciunt
            mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h:dnode) with h match(s:mt_seq{assigned_hg:h.name}) with h, size(collect( s.name)) as seq_ct set h.seq_ct=seq_ct");

            //crete probe hierarchy
            mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (pp:probe) where pp.subsumed_by is not null and pp.ref_start_pos>15 with pp\n" +
    "match(pc:probe) where pc.pid in pp.subsumed_by merge (pp)-[r:probe_child]->(pc)");

            //create probe hierarchy
            mt_var.probes.depreciate_never_completes_probe_hierarchy ph = new mt_var.probes.depreciate_never_completes_probe_hierarchy();
            ph.create_probe_hierarchy();
         mt_var.genlib.tracker.writeTracker("probe hierarchy created");
           
            /////////////////////////////////
            //create dnode probe proberties
            mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p)-[r:probe_dnode]->(h) where p.subsumed_by is null with p,collect(h.name) as hgs, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct h.path_variants)))) as pv with p, hgs, pv where size(hgs)=1 with hgs, collect(distinct p.pid) as pids, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct pv)))) as ppv match(hx:dnode{name:hgs[0]}) set hx.probe_variant_ct=size(ppv), hx.probes=pids,hx.probe_variants=ppv");
            
           
           
         mt_var.genlib.tracker.writeTracker("dnode and relationships created");
             
         mt_var.load.upload_mt_yfull_tmrca uyf = new mt_var.load.upload_mt_yfull_tmrca();
         uyf.oad_yfull_mt_times();
        mt_var.genlib.tracker.writeTracker("dnode timelines added");
         
         
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq)-[r:seq_probe]-(p) with s, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(p.variants)))) as variants set s.all_variants=variants ");
         

         mt_var.neo4jlib.transaction_query tq = new mt_var.neo4jlib.transaction_query();
          tq.execute_cypher_query("MATCH (s:mt_seq) where s.all_variants is not null with s, mt_var.sorter.sort_variant_list_by_pos_toList(s.all_variants) as var set s.all_variants = var");
         
                  mt_var.genlib.tracker.writeTracker("seq_all_variantsloaded!");

         
         mt_var.genlib.tracker.writeTracker("successfully completed all load steps!  Enhancement not underway");

          
    
//         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe) where p.haploprobe is not null match (d:dnode{name:p.haploprobe}) set p.op = d.op, p.lvl=d.lvl");
         
              
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (d:dnode) where d.path_variants is not null set d.path_variants=mt_var.sorter.sort_variant_list_by_pos_toList(d.path_variants) ");
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (d:dnode) where d.probe_variants is not null set d.probe_variants=mt_var.sorter.sort_variant_list_by_pos_toList(d.probe_variants)");
         
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq) where s.all_variants is not null set s.all_variants=mt_var.sorter.sort_variant_list_by_pos_toList(s.all_variants) ");
         
         
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq)-[r:seq_probe]-(t:probe) WHERE t.subsumed_by IS NULL AND t.ref_start_pos > 15 MERGE (s)-[:seq_probe_filtered]->(t);");

         mt_var.genlib.tracker.writeTracker("variant sets and filtered seq_probes completed successfully!");

         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq) with s match (d:dnode) where s.assigned_hg=d.name with s, d, apoc.coll.subtract(d.path_variants,s.all_variants) as missing  with s, d, size(missing) as missed_ct, mt_var.sorter.sort_variant_list_by_pos(missing) as missing with s, mt_var.sorter.sort_variant_list_by_pos(apoc.convert.fromJsonList(missing)) as missing, missed_ct set s.hg_missed_var=missing, s.missed_var_ct=missed_ct");
         
//         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq) with s match (d:dnode) where s.assigned_hg=d.name with s, d, apoc.coll.subtract(d.path_variants,s.all_variants) as missing  with s, d, size(missing) as missed_ct, mt_var.sorter.sort_variant_list_by_pos(missing) as missing set s.hg_missed_var=missing, s.missed_var_ct=missed_ct");
//         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq) set s.hg_missed_var= apoc.convert.fromJsonList(s.hg_missed_var) ");
//         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq) where s.hg_missed_var is not null set s.hg_missed_var= mt_var.sorter.sort_variant_list_by_pos(s.hg_missed_var) ");
         
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq) where s.hg_missed_var is not null  set s.hg_missed_var= apoc.convert.fromJsonList(s.hg_missed_var) ");
         
         mt_var.load.load_haplogrep3_rsrs_phylotree lhgt = new mt_var.load.load_haplogrep3_rsrs_phylotree();
         lhgt.load_tree();
         
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:variant) where size(v.name)-size(replace(v.name,'!','')) in [1,3,5,7 ] with v match (p:probe) where v.pos-25<p.ref_start_pos<v.pos+25 and p.subsumed_by is null with p, p.pid as pid, mt_var.sorter.sort_variant_list_by_pos_toList(collect(distinct v.name)) as vars set p.back_mutations=vars");
         mt_var.neo4jlib.neo4j_qry.qry_write("CREATE INDEX probe_ref_start_pos_index FOR (p:probe) ON (p.ref_start_pos)");
         
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq)  with s match (d:dnode{name:s.assigned_hg})merge (s)-[r:seq_dnode]->(d)")
                 ;
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(dp)-[r:dnode_child]->(dc) merge (dc)-[rp:dnode_parent]->(dp)");
         
         mt_var.neo4jlib.neo4j_qry.qry_write("match (v:variant) where size(v.name)-size(replace(v.name,'!','')) in [1,3,5,7] with  v, replace(v.name,'!','') as anc with v, anc, apoc.text.regexGroups(v.name,'\\d+')[0][0] as pos with v,pos,split(v.name,pos) as ss set v.anc_name = ss[1] + pos + ss[0], v.anc=v.der, v.der=v.anc");
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (d:dnode) where d.start_back_mutation is not null  with d unwind d.start_back_mutation as x call{with x with x, replace(x,'!','') as anc with anc, apoc.text.regexGroups(anc,'\\d+')[0][0] as pos with anc,pos,split(anc,pos) as ss return ss[1]+ pos + ss[0] as anc_name} with d, collect(anc_name) as anc_bm set d.anc_back_mutation_names=anc_bm")
                 ;
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(s)-[r:seq_dnode]->(d) with d, collect(distinct s.name) as seqs set d.seq_ct =  size(seqs) ");
                 
         mt_var.seq_pairs.create_hg_part_child_relationship chpc = new mt_var.seq_pairs.create_hg_part_child_relationship();
         chpc.dispositive_triad();

         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:variant) WHERE v.region IS NULL WITH v,      CASE  WHEN v.pos >= 10 AND v.pos <= 574 THEN 'HVR2'   WHEN v.pos >= 575 AND v.pos <= 16000 THEN 'CR' WHEN v.pos >= 16017 AND v.pos <= 16545 THEN 'HVR1' ELSE NULL  END AS newRegion WHERE newRegion IS NOT NULL SET v.region = newRegion ");
                 
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe)  WITH p, CASE  WHEN p.ref_start_pos >= 10 AND p.ref_start_pos <= 574 THEN 'HVR2'   WHEN p.ref_start_pos >= 575 AND p.ref_start_pos <= 16000 THEN 'CR'  WHEN p.ref_start_pos >= 16017 AND p.ref_start_pos <= 16545 THEN 'HVR1' ELSE NULL  END AS newRegion with p, newRegion WHERE newRegion IS NOT NULL SET p.region = newRegion ");
         
         ;mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe) WHERE size(p.variants) > 1 SET p.variants = apoc.coll.dropDuplicateNeighbors(p.variants)");
         
         
         mt_var.load.load_fused_probes lfp = new mt_var.load.load_fused_probes();
         lfp.fused_probe_load();
         
         
        return "";
    }
}

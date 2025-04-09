/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports.dispositive_triad;

import java.util.regex.Pattern;
import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class hg_missed_variants {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String missed_hg_var(
  )
   
         { 
             
        get_missed();
         return "";
            }

    
    
    public static void main(String args[]) {
       get_missed();
    }
    
     public static String get_missed()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String fn = "hg_missed_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) RETURN s.name as seq, s.assigned_hg as assigned_hg, s.missed_var_ct as missed_var_ct,case when s.missed_var_ct=0 then 1 else 0 end as no_missed_var,  s.hg_missed_var as hg_missed_var order by s.name", fn, "sequence_details", "2;3", "", true, "UDF:\nreturn mt_var.reports.missed_hg_var()\n\nThe Cypher query returns the missed variants (a property of the mt_seq nodes) for each sequence with an assignd haplogroup.\nThis property was a memorialization of the analysis comparing the sequence variants to those require in the definition of the haplogroup.\nThe Cypher query leverages the List and Path datatypes of the graph database\nThe Neo4j apoc.coll.subtract function subtracts the List (set) of sequence variants from the List of Path variants defining the dnode.", false);
 
        mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) where s.missed_var_ct is not null RETURN s.missed_var_ct as missed_ct, count(*) as seq_ct, s.missed_var_ct*count(*) as missed_total order by missed_ct", fn, "missed_counts", "1;2", "", false, "This query aggregates the data by the number ofmissed variants (column C in the prior worksheet).\nA small number assigned haplogroups are not in the dnode tree and are excluded.\nNote that the total in column C agrees with thathe total in the prior worksheet\n\nNearly all of the sequences are missing 1 or more assigned haplogroup variants.", false);
        
        mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) with s.assigned_hg as hg,collect(distinct s.hg_missed_var) as mv, sum(case when s.missed_var_ct>0 then 1 else 0 end) as seq_with_missed match (d:dnode{name:hg}) with hg, mv, d, seq_with_missed, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(apoc.coll.flatten(mv)))) as all_var return   hg, size(d.path_variants) as defining_variant_ct, d.lvl as hierarchy_lvl,d.seq_ct as seq_ct,  seq_with_missed, round(toFloat(seq_with_missed)/toFloat(d.seq_ct),3)  as missed_rate,size(all_var) as missed_var_ct, round(toFloat(size(all_var)) /toFloat(size(d.path_variants)),3) as portion_of_required_variants,case when size(all_var) > 0 then 1 else 0 end as has_missed, all_var as missed_var, mv as var_groups order by d.op", fn, "missed_by_haplogroup", "3;5;6;8", "", false, "The Cypher query aggregates the data by haplogroup.\nMost haplogroups and the sequences assigned to them had at least 1 missing haplogroup-defining variant.", false);
 
        String cquvct = "MATCH (s:mt_seq) with s.assigned_hg as hg, s.hg_missed_var as mv unwind mv as x with x, collect(distinct hg) as hgs  optional match (d:dnode) where size(d.branch_variants)>0  and apoc.coll.contains(d.branch_variants, x) with x as variant, size(hgs) as hg_ct,collect(distinct d.name) as defines_branch,  apoc.coll.sort(hgs) as hgs return size(apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(hgs))))) as unique_hg_ct ";
        
        
        String uvct = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cquvct).split("\n")[0].split(Pattern.quote("|"))[0];
        
         mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) with s.assigned_hg as hg, s.hg_missed_var as mv unwind mv as x with x, collect(distinct hg) as hgs  optional match (d:dnode) where size(d.branch_variants)>0  and apoc.coll.contains(d.branch_variants, x) with x as variant, size(hgs) as hg_ct,collect(distinct d.name) as defines_branch,  apoc.coll.sort(hgs) as hgs return variant,size(defines_branch) as branches_defined, hg_ct,defines_branch, hgs  order by hg_ct desc", fn, "traversal_fail_points", "1;1", "", false, "The Cypher query evaluates each missed variant, returning the haplogroups affected.\nColumns B and D are the branches where the missed variant first appears and therefore where a traversal would be interupted.\nThe traversal targetted destination dnode nodes (assigned haplogroup of the sequence)are in Column C and E.\n\nThe total number of haplogroups misattributed is " +  uvct + ", which is the same as the total in column I of the prior worksheet.\nThis value is computed by this Cypher query which uses several set algebra functions in the Neo4j apoc library:\n" + cquvct, true);
//         mt_var.excelLib.excelRept.createExcel(cq, fn, "missed_counts", "1;2", "", false, "message", false);
//         mt_var.excelLib.excelRept.createExcel(cq, fn, "missed_counts", "1;1", "", false, "message", false);
//         mt_var.excelLib.excelRept.createExcel(cq, fn, "missed_counts", "1;1", "", false, "message", false);
//        
        return "";
    }
}

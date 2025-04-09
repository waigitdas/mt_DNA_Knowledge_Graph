/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports.dispositive_triad;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
//import mt_var.excelLib.*;

public class proof_diprositive_triad_dnode_tree_sequence_concordance {
    @UserFunction
    @Description("reports on dnode paths (traversable descending haplotree).")

    public String dispositive_triad_proof(
 
  )
   
         { 
             
        get_haplotrees();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_haplotrees();
    }
    
     public static String get_haplotrees()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String fn = "dispositive_proof_dnode_tree_computes_and_assigned_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        mt_var.excelLib.excelRept.createExcel("MATCH path1=(dp:dnode)-[rd:dnode_child]->(dc:dnode) with path1, dp, dc match path2=(sp:mt_seq{assigned_hg:dp.name})-[rs:hg_parent_child]->(sc:mt_seq{assigned_hg:dc.name})  with path1, path2, dp, dc, sp, sc, rs, apoc.coll.subtract(dp.branch_variants,sc.all_variants) as missed_branch, apoc.coll.subtract(dc.path_variants, sc.all_variants) as missed_path,sum(case when size(apoc.coll.subtract(dc.path_variants, sc.all_variants))>0 then 1 else 0 end) as missed_path_ct with dc, rs.hg_pair as hg_pair, dp.name as parent_hg, dc.name as child_hg, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(missed_branch)))) as list_missed_branch, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(missed_path)))) as list_missed_path, sum(case when size(missed_branch)>0 then 1 else 0 end) as missed_branch_var, sum(case when size(missed_path)>0 then 1 else 0 end) as missed_path_var, missed_path_ct return apoc.text.repeat('___',dc.lvl) + child_hg as haplogroup, dc.lvl as lvl, dc.seq_ct as seq_c, case when missed_branch_var>0 then 1 else 0 end as branch_discordance, case when missed_path_var>0 then 1 else 0 end as  path_discordance,     size(list_missed_branch) as missed_list_ct, size(list_missed_path) as path_list_ct,  list_missed_branch, list_missed_path order by dc.op ", fn, "dnode_tree_missed_variants", "2;3;4;5;6", "1:####,###;2:###.###;4:###,###;5:###,###;6:###,###", true, "UDF:\nreturn mt_var.reports.dispositive_triad.dispositive_triad_proof()\n\nThis report compared the expected dnode variants with the observed sequence variants using the dipositive triad.\nThe dnode tree branches are sorted in hierarchical order (ordpath) with hierarchical level shown in column B. Haplogroups without Genbank sequences are omitted; thus, there are gaps in the levels.\n\nThe missed variant are computed using set algebra and subtracting the sequence set from the assigned dnode set of variants.\nColumns D and E reporton whether the variants of the sequence are aligned (0) or discordant (1) from thr dnode variant list. These are summed at thebottom of the columns\nThe discordanr counts are compared to the totalnumber of haplogroups (column A)\nSequence branch defining variants are missing in 17%, with the cumulative path variants are missing in 95%", true);
       
        
 
      return "";
    }
}

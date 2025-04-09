/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class fused_probe_specificity_for_haplogroups1 {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String fused_probe_specificity(
 
  )
   
         { 
             
        probe_summary();
         return "";
            }

    
    
    public static void main(String args[]) {
        probe_summary();
    }
    
     public static String probe_summary() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "fused_probe_specificity_for_haplogroups_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
                mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe) RETURN p.hg_ct as hg_ct, count(*) as occurrences order by hg_ct", fn, "fused_probe_dnodes_ct", "1;1;", "", true, "\"UDF:\\nreturn mt_var.reports.fused_probe_specificity\n\nThis Cypher summarizes the fused probe property hg_ct and its number of occurrences.", false);
        
                mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe)-[:fused_probe_dnode]->(d:dnode)  WHERE p.variants is not null\n" +
"WITH p, collect(distinct  d) AS branches with p, branches,  size(branches) as branch_ct RETURN  branch_ct,  count(*) as fused_probe_ct order by branch_ct", fn, "fused_probe_haplogroup_branch_counts", "1;1", "1:###,###;1:###,###", false, "The Cypher shows that most fused_probes align with just one haplogroup.\n\n", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe)-[:fused_probe_dnode]->(d:dnode) WHERE  p.variants is not null  WITH p, collect(distinct  d) AS branches with p, branches WHERE size(branches) = 1 RETURN p.pid AS fused_probe, branches[0].name as haplogroup, p.seq_ct as seq_ct, size(branches) as branch_ct,size(p.variants) as variant_ct, p.variants as variants order by haplogroup, variants, seq_ct desc ", fn, "haplogroup_specicity_fused_probes", "", "", false, "The Cypher extracts only fused_probe aligned with one haplogroup.\nThe row count is the same as the top row in the prior worksheet.\nHere we see the fused_probe level detail.\nWhile some fused_probes have the same variants, their flanking regions differ.", false);
               
               mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe)-[:fused_probe_dnode]->(d:dnode) WHERE  p.variants is not null  WITH p, collect(distinct  d) AS branches with p, branches WHERE size(branches) = 1 with branches[0].name as haplogroup, size(collect(distinct(p.pid))) as fused_probe_ct, sum(p.seq_ct) as seq_ct RETURN haplogroup, seq_ct, fused_probe_ct, toFloat(seq_ct)/toFloat(fused_probe_ct) as seq_per_probe order by seq_ct desc", fn, "haplogroup_summary", "1;2", "1:###,###;2:####,###;3:##.#", false, "The Cypher also extracts fused_probes aligned with a single haplogroup\nThe sorting in by sequence count in descending order.\nThe diversity of fused_probes (with different flanking regions and variants) is shown in columns C and D\nIn some groups (e.g., H3u1) a few fused_probes align with most sequences. In others(e.g., B@) here are numerous fused_probes and a low seq/probe ratio.", false);
               
               mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe)-[:fused_probe_dnode]->(d:dnode) WHERE  p.variants is not null WITH p, collect(distinct  d.name) AS branches with p, branches WHERE size(branches) > 1 with p, branches,mt_var.dnode.mrca_from_cypher_list(branches) as mrca RETURN p.pid AS fused_probe, p.seq_ct as seq_ct,case when size(branches)=1 then 1 else 0 end as branch_specific,  size(branches) as branch_ct, mrca,  case when apoc.coll.contains(branches,mrca) then 1 else 0 end as is_clade, case when apoc.coll.contains(branches,mrca) then size(branches) else 0 end as clade, size(p.variants) as variant_ct, p.variants ORDER BY branch_ct  DESC ", fn, "fused_probe_mapped_to_many_haplogroups", "5;5", "", false, "The Cypher extracts fused_probes and determines whether they aligned with a single clade\nThe common ancestors is in column E and if it is in the list of haplogroups (not shown), then it is mapped to a single clade (col E = 1) and column G will contain the number of sequence aligned with the common ancestor and descendant nodes aligned with the fused_probe.\nThe fused_probe may not align with all descendant nodes of the common ancestor (see next worksheet). ", false);
               
               mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe)-[:fused_probe_dnode]->(d:dnode) WHERE p.variants is not null  WITH p, collect(distinct  d.name) AS branches with p, branches WHERE size(branches) > 1 with p, branches,mt_var.dnode.mrca_from_cypher_list(branches) as mrca  with p.pid AS pid, p.seq_ct as seq_ct,case when size(branches)=1 then 1 else 0 end as branch_specific,branches, mrca,  case when apoc.coll.contains(branches,mrca) then size(branches) else 0 end as clade, size(p.variants) as variant_ct, p.variants as variants where clade>0 with pid, seq_ct,mrca,variant_ct, variants,  size(branches) as ct_clade_desc MATCH (dp:dnode{name:mrca})-[r:dnode_child*0..25]->(dc)  with pid, seq_ct, mrca,dp.lvl as mrca_tree_lvl, variant_ct, variants, ct_clade_desc, collect(distinct dc.name) as dcc RETURN pid,seq_ct, mrca, ct_clade_desc, size(dcc) as ct_all_clade_desc, toFloat(ct_clade_desc)/toFloat(size(dcc)) as ratio , variant_ct, variants ORDER BY mrca, variants", fn, "clade_specific_fused_probes", "", "4:###,###;5:##.#####", false, "This Cypher extracts just those fused_probes aligned with a clade\nIt shows multiple fused_probe may align with a common ancestor\nThe number of descendant branches aligned with the fused_probe is in column D\nThe total number of descendant branches from the common ancestor is in column E.\nThe portion of fused_probe aligned branches to the all descendant branches is in column F\nWhile some fused_probes have the same variants, their flanking regions differ.", true);
        //       
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false "message.", true;
        

        return "";
    }
}

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


public class proof_back_mutation_hg_parent_child_discordance {
    @UserFunction
    @Description("Dispositive triad back mutations that are discordant")

    public String dt_back_mutation(
  )
   
         { 
             
        discorant_bm();
         return "";
            }

    
    
    public static void main(String args[]) {
        discorant_bm();
    }
    
     public static String discorant_bm()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "dispositive_evidence_back_mutation_retention_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
        String ct = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str("MATCH (dp:dnode)-[rc:dnode_child]->(target:dnode) where target.anc_back_mutation_names is not null match (sp:mt_seq{assigned_hg:dp.name})-[r:hg_parent_child]->(sc:mt_seq{assigned_hg:target.name})  with dp, target, r, sp, sc, apoc.coll.intersection(sp.all_variants, target.anc_back_mutation_names) as bm_in_parent, apoc.coll.intersection(sc.all_variants, target.anc_back_mutation_names) as bm_in_target with dp, target, r, sp,sc, bm_in_parent, bm_in_target where size(bm_in_target)>0 return  sum(case when size(target.start_back_mutation)-size(bm_in_parent)>0 then 1 else 0 end)  as expected_not_in_parent").split("\n")[0].split(Pattern.quote("|"))[0];
        
        
          mt_var.excelLib.excelRept.createExcel("MATCH (dp:dnode)-[rc:dnode_child]->(target:dnode) where target.anc_back_mutation_names is not null match (sp:mt_seq{assigned_hg:dp.name})-[r:hg_parent_child]->(sc:mt_seq{assigned_hg:target.name})  with dp, target, r, sp, sc, apoc.coll.intersection(sp.all_variants, target.anc_back_mutation_names) as bm_in_parent, apoc.coll.intersection(sc.all_variants, target.anc_back_mutation_names) as bm_in_target with dp, target, r, sp,sc, bm_in_parent, bm_in_target where size(bm_in_target)>0 return distinct r.hg_pair, sp.name as target_parent, sc.name as target, size(target.start_back_mutation) as expected_bm_ct, size(bm_in_parent) as parent_var_ct,  size(target.start_back_mutation)-size(bm_in_parent) as expected_not_in_parent,  size(bm_in_target) as retained_in_target, target.start_back_mutation as parent_expected, bm_in_parent as parent_observed,  apoc.coll.subtract(bm_in_parent,target.anc_back_mutation_names) as target_expected, bm_in_target as target_observed order by retained_in_target desc, r.hg_pair", fn, "discorant_back_matation_detail", "", "", true, "UDF:\nreturn mt_var.reports.dispositive_triad.dt_back_mutatio()\n\nThis Cypher uses the dispostive triad to find expected back mutations that did not actually back mutate.\nThe back mutation in the targetted child haplotree node are in columns D and H\nColumn H is thent the variants expected in the parent that are expected to disappear in the child where the back mutations occur.\n\nThere are two anomalies observer\n   1. The parent may not have all the variants expected to be removed in the child (count in column F)\n   2. The child does not drop all the expected variants; that is, they do not revert to the ancestral state (columns G and K).\n\nThere are " + ct + " instances where the parent node did not have all the variants expected to back mutate.", false);
               
               mt_var.excelLib.excelRept.createExcel("MATCH (dp:dnode)-[rc:dnode_child]->(target:dnode) where target.anc_back_mutation_names is not null match (sp:mt_seq{assigned_hg:dp.name})-[r:hg_parent_child]->(sc:mt_seq{assigned_hg:target.name})  with dp, target, r, sp, sc, apoc.coll.intersection(sp.all_variants, target.anc_back_mutation_names) as bm_in_parent, apoc.coll.intersection(sc.all_variants, target.anc_back_mutation_names) as bm_in_target with dp, target, r, sp,sc, bm_in_parent, bm_in_target where size(bm_in_target)>0 with r.hg_pair as hg_pair,target.start_back_mutation as expected_bm, size(target.start_back_mutation) as expected_bm_ct, size(bm_in_parent) as parent_var_ct,  size(target.start_back_mutation)-size(bm_in_parent) as expected_not_in_parent, size(bm_in_target) as retained_in_target, target.start_back_mutation as parent_expected, bm_in_parent as parent_observed,  apoc.coll.subtract(bm_in_parent,target.anc_back_mutation_names) as target_expected, bm_in_target as target_observed , count(*) as occurrences return hg_pair, expected_bm, parent_var_ct,expected_not_in_parent,retained_in_target, parent_expected,	parent_observed, target_expected, target_observed, occurrences order by  hg_pair,retained_in_target desc", fn, "hp_pairs", "9;9", "", false, "This Cypher aggregates the retained back mutations by the hg_pair in the dispositive triad", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH (dp:dnode)-[rc:dnode_child]->(target:dnode) where target.anc_back_mutation_names is not null match (sp:mt_seq{assigned_hg:dp.name})-[r:hg_parent_child]->(sc:mt_seq{assigned_hg:target.name})  with dp, target, r, sp, sc, apoc.coll.intersection(sp.all_variants, target.anc_back_mutation_names) as bm_in_parent, apoc.coll.intersection(sc.all_variants, target.anc_back_mutation_names) as bm_in_target with dp, target, r, sp,sc, bm_in_parent, bm_in_target where size(bm_in_target)>0 with r.hg_pair as hg_pair,target.start_back_mutation as expected_bm, size(target.start_back_mutation) as expected_bm_ct, size(bm_in_parent) as parent_var_ct,  size(target.start_back_mutation)-size(bm_in_parent) as expected_not_in_parent, size(bm_in_target) as retained_in_target, target.start_back_mutation as parent_expected, bm_in_parent as parent_observed,  apoc.coll.subtract(bm_in_parent,target.anc_back_mutation_names) as target_expected, bm_in_target as target_observed return target_observed as retained, count(*) as occurrence_ct,size(collect(distinct hg_pair)) as  hg_pair_ct, collect(distinct hg_pair) as hg_pairs order by  occurrence_ct desc", fn, "retained_variannt", "1;2", "", false, "This Cypher summarized by the retained variant.", true);
        
        return "";
    }
}

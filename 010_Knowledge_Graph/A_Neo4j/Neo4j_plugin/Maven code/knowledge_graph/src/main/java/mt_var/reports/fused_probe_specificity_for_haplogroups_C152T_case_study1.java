/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class fused_probe_specificity_for_haplogroups_C152T_case_study1 {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String fused_probe_specificit_C152T(
  )
   
         { 
             
        show_fused_probes();
         return "";
            }

    
    
    public static void main(String args[]) {
        show_fused_probes();
    }
    
     public static String show_fused_probes()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "C152T_fused_probe_specificity_for_haplogroups_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
                 mt_var.excelLib.excelRept.createExcel("MATCH path1=(p)-[r:fused_probe_variant]->(v:variant{name:'C152T'}) with p,v,path1 match path2=(p)-[fused_probe_dnode]-(d:dnode) where apoc.coll.contains(d.path_variants, 'C152T') and  size(p.variants)>0 with v, p,  collect(distinct d.name) as haplogroups  with v, p, haplogroups, mt_var.dnode.mrca_from_cypher_list(haplogroups) as hgs_common_ancestor return  v.name as variant,size(haplogroups) as hg_ct,case when size(haplogroups)=1 then 1 else 0 end as hg_specific,  size(p.variants) as var_ct,  p.pid as fused_probe, p.seq_ct as fused_probe_seq_ct,hgs_common_ancestor, case when apoc.coll.contains(haplogroups,hgs_common_ancestor) then 1 else 0 end as one_path, haplogroups,  p.variants as fused_probe_variants  order by fused_probe_seq_ct desc", fn, "C152T_case_study", "1;2;5;7", "", true, "UDF:\nreturn mt_var.reports.fused_probe_specificity()\n\nThis Cypher identifies numerous unique fused_probes which subsume the C152T variant.\nThes can be see by setting the filter in colum C to 1 (true)\nThe uniqueness is because there is a distinct set of annotated variantsin for each fused_probe sequence,\n", false);
        
   
               mt_var.excelLib.excelRept.createExcel("MATCH path1=(p)-[r:fused_probe_variant]->(v:variant{name:'C152T'}) with p,v,path1 match path2=(p)-[fused_probe_dnode]-(d:dnode) where apoc.coll.contains(d.path_variants, 'C152T') and  size(p.variants)>0 with v, p,  collect(distinct d.name) as haplogroups  with v, p, haplogroups, mt_var.dnode.mrca_from_cypher_list(haplogroups) as hgs_common_ancestor where size(haplogroups)>1 with v,p, haplogroups,hgs_common_ancestor, case when apoc.coll.contains(haplogroups,hgs_common_ancestor) then 1 else 0 end as one_path with v, p, haplogroups,hgs_common_ancestor, one_path where one_path=0 return  v.name as variant,size(haplogroups) as hg_ct,case when size(haplogroups)=1 then 1 else 0 end as hg_specific,  size(p.variants) as var_ct,  p.pid as fused_probe, p.seq_ct as fused_probe_seq_ct,hgs_common_ancestor, one_path, haplogroups,  p.variants as fused_probe_variants  order by fused_probe_seq_ct desc", fn, "C152T_multiple_hgs", "1;2;5;7", "", false, "This Cypher shows only probes that are not specific for a haplogroup or clade.\nThese probes identify intersections that are not consistent with the haplotree model.\n", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH path1=(p)-[r:fused_probe_variant]->(v:variant{name:'C152T'}) with p,v,path1 match path2=(p)-[:fused_probe_dnode]-(d:dnode) where apoc.coll.contains(d.path_variants, 'C152T') and  size(p.variants)>0 with v, p,  collect(distinct d.name) as haplogroups  with v, p, haplogroups, mt_var.dnode.mrca_from_cypher_list(haplogroups) as hgs_common_ancestor where size(haplogroups)>1 with v,p, haplogroups,hgs_common_ancestor, case when apoc.coll.contains(haplogroups,hgs_common_ancestor) then 1 else 0 end as one_path with v, p, haplogroups,hgs_common_ancestor, one_path where one_path=0 with  collect(distinct p.pid) as pids MATCH (s:mt_seq)-[r:seq_fused_probe]->(ps:fused_probe) where ps.pid in pids with s,  collect(ps.pid) as pids, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(ps.haplogroups)))) as hgs return s.name, s.assigned_hg as assigned_hg,case when apoc.coll.contains(hgs,s.assigned_hg) then 1 else 0 end as hgs_contains_assigned, size(hgs) as hg_ct,hgs[0..10] as haplogroups_10, size(pids) as pid_ct, pids order by hg_ct desc, pid_ct desc, pids[0], s.assigned_hg, s.name", fn, "seqs_C152T_multiple_hgs", "2;5;7", "", false, "This Cypher shows only probes that are not specific for a haplogroup or clade.\nThese probes identify intersections that are not consistent with the haplotree model.\n", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH path1=(p)-[r:fused_probe_variant]->(v:variant{name:'C152T'})  with p,v,path1 match path2=(p)-[fused_probe_dnode]-(d:dnode) where apoc.coll.contains(d.path_variants, 'C152T') and  size(p.variants)>0 with v, p, size(p.variants) as var_ct,  collect(distinct d.name) as haplogroups  return  size(haplogroups) as hg_ct, count(*) as occurrences order by hg_ct", fn, "haplogroups_per_fused_probe", "1;1", "", false, "This Cyper shows the number of haologroups linked to a fused_probe\nThis agrees with the prior worksheet with its 77% rate of fused_probes map to one haplogroup.\nThis worksheet show the other rates, but does not account for fused_probes mapping to a single clade (see prior worksheet).", false);

               mt_var.excelLib.excelRept.createExcel("MATCH (p)-[:fused_probe_variant]->(v:variant {name: 'C152T'}) MATCH (p)-[:fused_probe_dnode]-(d:dnode) WHERE  apoc.coll.contains(d.path_variants, 'C152T')  AND size(p.variants) > 0 WITH apoc.coll.dropDuplicateNeighbors( apoc.coll.sort(              apoc.coll.flatten(collect(p.variants)) )) AS vars, d.name AS haplogroup UNWIND vars AS x RETURN x AS variant, count(DISTINCT haplogroup) AS haplogroup_count ORDER BY haplogroup_count DESC, toInteger(apoc.text.regexGroups(variant,'\\d+')[0][0])", fn, "variants", "1;1", "", false, "This Cypher shows the numerous variants adjacent to C152T in fused_probes. \nWhile C152T is a path variant in 2884 haplogroups, its adjacent variants in fused_probes makes it specific of 77% of these haplogroups..", false);

               mt_var.excelLib.excelRept.createExcel("MATCH (d:dnode)<-[r:fused_probe_dnode]-(p:fused_probe) where apoc.coll.contains(d.path_variants,'C152T') with d, d.name as hg, size(collect(distinct p.pid)) as fused_probe_ct, collect(distinct p.pid) as pids return hg, fused_probe_ct, apoc.coll.sort(pids) as pids order by d.op", fn, "haplogroups", "", "", false, "This Cypher rollup up the fused_probes subsuming C152T by hapolgroup. ", true);

        

        return "";
    }
}

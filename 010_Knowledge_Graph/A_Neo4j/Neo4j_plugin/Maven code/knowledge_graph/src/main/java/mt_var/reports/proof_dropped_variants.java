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


public class proof_dropped_variants {
    @UserFunction
    @Description("reports and case study of the dropped variants revealed by the hg_parent_child relationship.")

    public String dropped_variants (
        
  )
   
         { 
             
        dropped_variant_rept();
         return "";
            }

    
    
    public static void main(String args[]) {
        dropped_variant_rept();
    }
    
     public static String dropped_variant_rept()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "proof_hg_parent_child_dropped_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
               mt_var.excelLib.excelRept.createExcel("MATCH p=()-[r:hg_parent_child]->() RETURN count(*) as total, sum(case when r.dropped_count>0 then 0 else 1 end) as no_dropped_defining_path_var,sum(case when r.dropped_count>0 then 1 else 0 end) as has_dropped_defining_path_var, sum(case when r.all_dropped_count>0 then 0 else 1 end) as no_dropped_any_var, sum(case when r.added_count>0 then 1 else 0 end) as added_var", fn, "summary", "", "", true, "UDF:\nreturn mt_var.reports.dropped_variants()\n\nThis Cypher extracts the properties of the hg_parent_child relationship to summarize the frequencies of paren->child transition variant changes\nColum  A has the total number of hg_parent_child rrelationships which link 2 mt_seq nodes\nColumns B and C report the path defining variant changes in parent->child transition; almost all have dropped variants.\nThere are many sequence variants not used in defining haplotree branches. Column D shows an even greater number of seqence pairs have dropped variants when these are considersd\nColumn E is the number of added variants in the parent->child transition which are not among the branch defining varints. This number needs to be interpreted cautiously because there are duplicate formats of the same variant in some cases.\n\nThe percentages can be added manually.\n\nThis summary highlights the extent of dropped and added variants, reinforcing the complexity of haplogroup transitions beyond what is captured in the traditional haplotree. .", false);
               
               mt_var.excelLib.excelRept.createExcel("match path1=(sp:mt_seq{assigned_hg:'H5'})-[r:hg_parent_child]->(sc:mt_seq)  with path1, sp,sc match path2=(dp:dnode{name:sp.assigned_hg})-[rdsv:dnode_shared_variants]->(dc:dnode{name:'H5a'}) with path1, path2, sp,sc,dp,dc match path3=(sx:mt_seq)-[rsd:seq_dnode]->(dx) where sx in [sp,sc] and dx in [dp,dc] with [x in nodes(path3)|x.name] as xpath return xpath[1], count(*)", fn, "h5_H5a_transition_", "", "", false, "This Cypher initially finds all sequences with assigned haplogroups H5 or H5a (where H5a is the child haplogroup in the haplotree).\nFor these two sets, it then identifies sequences that share variants and also have H5 or H5a as their assigned haplogroup.\nThe results show that not all H5 children are H5a.\nThe count of H5a sequences matches the sum of the various patterns of dropped variants found on the next worksheet.", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq{assigned_hg:'H5'})-[r:hg_parent_child]->(sc:mt_seq{assigned_hg:'H5a'}) RETURN r.dropped_path_variants as dropped_variants, r.dropped_count as droppe_size, count(*) as seq_pair_ct order by seq_pair_ct desc ", fn, "H5_H5a_detaile", "2;2", "2:###,###;3:###,###", false, "This Cypher provides details of the dropped variants for the H5 → H5a haplogroup transition.\nThe diversity of these patterns reveals complexity that is not inherent in the existing haplotree.\n\nThis query, using the hg_pair property of the hg_parent_child relationship gives the same result\nMATCH p=(sp:mt_seq)-[r:hg_parent_child{hg_pair:'H5:H5a'}]->(sc:mt_seq) RETURN r.hg_pair, r.dropped_path_variants as dropped_variants, r.dropped_count as droppe_size, count(*) as seq_pair_ct order by seq_pair_ct desc \n\nThe results confirm that multiple distinct dropout patterns exist, suggesting a more complex evolutionary history than the current haplotree structure implies.\"", false);

               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) with r.hg_pair as hg_pair, collect(distinct r.dropped_path_variants) as pattern RETURN hg_pair, size(pattern) as pattern_ct,size(apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(pattern)))) as unique_variant_ct, pattern order by pattern_ct desc", fn, "all_hg_pairs", "", "", false, "This Cypher query leverages the hg_parent_child relationship and its hg_pair property to extract and aggregate results by haplogroup pairs (hg_pair).\nOut of 3,498 haplogroup pairs, only 11 do not have any dropped variants.\nThe query uses APOC collection functions to identify unique dropped variants for each hg_pair.\nThe COLLECT function captures the diversity of dropped variant patterns.\nThe results for the H5 → H5a pair are consistent with those in the previous worksheet, reinforcing the findings.\nThe pattern field consists of sets formatted as Neo4j List data types. ", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) with r.hg_pair as hg_pair, r.dropped_path_variants as pattern, count(*) as ct with pattern, sum(ct) as rel_ct,collect(distinct hg_pair) as hg_pairs RETURN pattern,size(pattern) as patter_size,rel_ct as hp_parent_child_rel_ct,  size(hg_pairs) as hg_pair_ct, hg_pairs  order by hg_pair_ct desc, toInteger(apoc.text.regexGroups(pattern[0],'\\d+')[0][0])", fn, "pattern_aggregration", "2;2", "2:###,###,###;4:##", false, "This Cypher aggregates haplogroup transitions by dropped variant patterns.\\nAll hg_parent_child relationships are considered, with the total count displayed at the bottom of column C.\\nThe results reveal 25,691 unique variant patterns, many shared across multiple haplogroup pairs, demonstrating the reticular nature of the topology.\\nThis suggests that the evolutionary structure is more complex than the traditional haplotree model.", true);

        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false "message.", true;
        

        return "";
    }
}

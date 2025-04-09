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


public class compare_trees {
    @UserFunction
    @Description("Repoert comparisons between FTDNA and Haplogrep3 rsrs haplotree")

    public String compare_ftdna_haplogrep3(
  )
   
         { 
             
        tree_compare();
         return "";
            }

    
    
    public static void main(String args[]) {
        tree_compare();
    }
    
     public static String tree_compare()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "ftdna_phylotree_compare_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
               mt_var.excelLib.excelRept.createExcel("MATCH (v:variant)  with v optional match (vg:hg_variant{name:v.name}) RETURN v.pos as pos,v.name as ftdna_name, vg.name as haplogrep3_name, case when vg.name is not null then 1 else 0 end as same order by pos", fn, "all_var", "3;3", "", true, "UDF:\nreturn mt_var.reports.compare_ftdna_haplogrep3()\n\nThis Cypher compares FDNA to Haplogrep3 variants using the optional match which will retun a null when Haplogrep3 does NOT have a matching variant\nThe concondance is 27.3%, indicating that the knowledge graph had far more variants that required for the Haplogrep3 phylotree. ]nThe misses can be seen by filtering on column D.\nContrast this with the last worksheet where knowledge graph variant list used in analytics had 99.8% of the variants in the Hapogrep3 variant list.\n\nTherefor, There will be a trivial effect resulting from the haplogroup assignments based on the Haplogrep3 tree and the analytics using variants from FTDNA and sequence annotations.", false);
               
               mt_var.excelLib.excelRept.createExcel("MATCH (v:variant) where right(v.name,1)='d'  with v optional match (vg:hg_variant{name:v.name}) RETURN v.pos as pos,v.name as ftdna_name, vg.name as haplogrep3_name, case when vg.name is not null then 1 else 0 end as same order by pos", fn, "deletions", "3;3", "", false, "This Cypher query delivers the same results as the first worksheet, but filtered to show only deletions.", false);
               
               mt_var.excelLib.excelRept.createExcel("MATCH (v:variant) where  v.name<>replace(v.name,'.','')  with v optional match (vg:hg_variant{name:v.name}) RETURN v.pos as pos,v.name as ftdna_name, vg.name as haplogrep3_name, case when vg.name is not null then 1 else 0 end as same order by pos", fn, "insertions", "3;3", "", false, "This Cypher query delivers the same results as the first worksheet, but filtered to show only insertions.", false);
              
               mt_var.excelLib.excelRept.createExcel("MATCH (v:variant) where right(v.name,1)='!'   with v optional match (vg:variant) where right(vg.name,1)='!' and right(v.name,1)='!' and (v.pos=vg.pos and size(v.name)-size(replace(v.name,'!','')) in [1,3,5] ) RETURN v.pos as pos,v.name as ftdna_name, vg.name as haplogrep3_name, case when vg.name is not null then 1 else 0 end as same order by pos", fn, "back_mutations", "3;", "", false, "This Cypher query delivers the same results as the first worksheet, but filtered to show only back mutations.", false);
               
              
               mt_var.excelLib.excelRept.createExcel("MATCH (v:variant) where right(v.name,1)<>'d' and v.name=replace(v.name,'.','')  with v optional match (vg:hg_variant{name:v.name}) RETURN v.pos as pos,v.name as ftdna_name, vg.name as haplogrep3_name, case when vg.name is not null then 1 else 0 end as same order by pos", fn, "no_indels", "3;3", "", false, "This Cypher query delivers the same results as the first worksheet, but filtered to exclude indels", false);

               mt_var.excelLib.excelRept.createExcel("MATCH (d:dnode) with d optional match (h:haplogroup{name:d.name}) RETURN   apoc.text.repeat('___', d.lvl) + d.name as dnode_name, d.lvl as level, h.name as haplogrep3_name, d.seq_ct as seq_ct, case when d.name=h.name then 1 else 0 end as same order by d.op", fn, "ftdna3 compare_tree", "3;4", "", false, "The FTDNA haplotree has 94& of the same named branches as Haplogrep3", false);

                mt_var.excelLib.excelRept.createExcel("MATCH (h:haplogroup) with h optional match (d:dnode{name:h.name}) RETURN   h.name as haplogrep3_name, d.lvl as level, d.name as dnode3_name, d.seq_ct as seq_ct, case when d.name=h.name then 1 else 0 end as same order by d.op", fn, "haplogrep3 compare_tree", "3;4", "", false, "The Haplogrep3 haplotree shars 95% of the same named branches with the FTDNA haplotree. ", false);

 

               mt_var.excelLib.excelRept.createExcel("MATCH (v:hg_variant)  with v optional match (vg:variant) where vg.name=v.name or (vg.pos=v.pos and size(vg.name)-size(replace(vg.name,'!','')) in [1,3,5] ) RETURN v.pos as pos,vg.name as ftdna_name, v.name as haplogrep3_name, case when vg.name is not null then 1 else 0 end as same order by pos", fn, "haplogrep_ftdna", "3;3", "", false, "This Cypher shows that 99.8% of Haplogrep3 variants are found in the knowledge graph variant list used in analytics.", true);

        

        return "";
    }
}

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


public class base_sets_for_haplotree_proof {
    @UserFunction
    @Description("Sets used in proof analytics.")

    public String proof_setsg(
        
  )
   
         { 
             
        get_sets();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_sets();
    }
    
     public static String get_sets() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "base_sets_for_haplotree_proof_analytics_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
    
               mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) RETURN s.name as sequence, s.assigned_hg as assigned_haplogroup, size(s.all_variants) as variant_ct, s.all_variants as variants order by sequence", fn, "sequences_with_variants", "", "", true, "Genbank sequeces (column A) include the RSRS reference sequence (Genbank NC_012920) whose name iis changed to assure it sorts at the top of the list\nThe haplogroup assigned by others (column B) was computed by them using the Halplogrep3 tools. The exceptions are a few sequences in the L& clade identified by the research team at FTDNA.\n\\nThe variant lists have redunancies so that variations in formsating can be matched. For example, 106-111d will also be represented at a set of vaiiants in the formats 106d and G106d or GGAG106d\\nWhen there is heteroplasmy the variant with the heteroplasmy code included in the list is supplemented by any variants (except the ancestral) represented by the heteroplasmy code. N is expanded to include the 4 possibilities: A, C, G, and T, excluding the ancestral nucleotide at the position.\\nThis design maximizes the opportunity to reconcile a variant in various lists.", false);
    
               mt_var.excelLib.excelRept.createExcel("MATCH (d:dnode) RETURN apoc.text.repeat('__', d.lvl) + d.name as haplotree_branch,d.lvl as level,size(d.branch_variants) as branch_var_ct, size(d.path_variants) as path_var_ct, d.branch_variants as branch_defining_variants, d.path_variants as path_variants, d.start_back_mutation as start_back_mutation, d.op as hexordpath  order by d.op", fn, "shaplotree_with Variants", "", "", false, "The haplotree branches (column A) in hierarchical order and the level (depth) in the haplotre. The variants computed with tools developed in this project are in column C and E.\nPath variants (columns D and F) were aggregaated using the Neo4j collect function during traversals from the root node to the dnode represesenting the haplogroup. They were then sorted by position of the variant.\nBranch defining variants are in column C sorted in position order.\nBack mutation are shown in Column G at the point in the haplotree where the reservion the the ancestral state occurred,\nThe hexadecimal ORDPATH (column H) sorts the rows in haplotree hierarchical order.", false);
               
              
               mt_var.excelLib.excelRept.createExcel("MATCH (v:variant) where v.pos>15  RETURN v.name as variant, v.pos as position, v.anc as ancestral, v.der as derived order by position, variant", fn, "variants", "", "", false, "Variants (column A) are sorted by position (column B) and shown with ther anc and der nucleotides.", true);
       

        return "";
    }
}

/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports.dispositive_triad;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class missed_child_seq_var {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String missed_variants_childseq(
     
  )
   
         { 
             
        missed_rept() ;
         return "";
            }

    
    
    public static void main(String args[]) {
       missed_rept() ;
    }
    
     public static String missed_rept() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "seq_child_missed_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) with  sc.assigned_hg as chg, r.dropped_count as dropped_ct, r.dropped_path_variants as missed, size(collect(distinct sc.name)) as seq_ct match (d:dnode{name:chg}) with  d,seq_ct, dropped_ct, missed return  apoc.text.repeat('__', d.lvl-3) +  d.name as child_hg, d.lvl as lvl,  dropped_ct,missed as dropped order by  d.op, dropped_ct desc", fn, "haplogroups_with_dropped", "2;2", "2:###,###;3:###,###",    true, "UDF:\nreturn mt_var.reports.dispositive_triad.issed_variants_childseq{}\n\bhaplogroups sorted by ORDPATH in tree hierarchical order\nThe dropped variants are aggregated by haplogroup and may be different in differing sequence-pairs.\nNot all haplogroups are shown because some do not have sequences on adjacent branches to form sequence-pairs.\n\nThe analyses presented in this workbook address conformance to the haplotree\nWhen a variant isdropped, it does NOT imply that n ancestral state is restores.\nThat is, these dropped variants are not equivalent to back mutations.", false);
               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) with  sc.assigned_hg as chg  match (d:dnode{name:chg}) with  d, count(*) as ct return  d.name as child_hg, d.lvl as lvl,  ct order by  ct desc", fn, "haplogroups", "2;2;", "2:###,###,###;3:###.###,###", false, "haplogroups sorted by sequence-pair counts\nThe total sums to the total number of relationships.\nTherefore, there are no exceptions; all available comparisons have at least one dropped variant.", false);
               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) with r.dropped_path_variants as missed_pattern, count(*) as ct return  missed_pattern, ct order by  ct desc", fn, "variant_pattern", "1;1", "1:###,###,###;2:###,###,###", false, "Aggregation is by the dropped variant patter\nThe sets of variants are sorted by the variant position to assure uniqueness of any set.\nThe total is equal to the number of relationships.", false);
               mt_var.excelLib.excelRept.createExcel("MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) with r unwind r.dropped_path_variants as x with x,count(*) as ct return  x as variant, ct order by  ct desc", fn, "variants", "1;1", "1:###,###,###;2:###,###,###", false, "Summation by individual dropped variants regardless of the haplogroup or pattern.", true);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false "message.", true;
        

        return "";
    }
}

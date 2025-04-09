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


public class napolbranches_no_variants1 {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String no_variant_haplobranches(
        @Name("rn1") 
            Long rn1,
        @Name("rn2") 
            Long rn2
  )
   
         { 
             
        get_rept();
         return "";
            }

    
    
    public static void main(String args[]) {
       get_rept();
    }
    
     public static String get_rept()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "haplogranch_without_variant_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
               mt_var.excelLib.excelRept.createExcel("MATCH (d:dnode) where size(d.branch_variants)=0  RETURN d.name, d.lvl,d.seq_ct as seq_ct, case when d.seq_ct>0 then 1 else 0 end as has_seq,case when size(d.name)-size(replace(d.name,'!','')) in[1,3,5,7] then 1 else 0 end as has_bm, case when d.start_back_mutation is not null then 1 else 0 end as has_bm2, case when right(d.name,2)='-x' then 1 else 0 end as has_x,d.branch_variants,d.path, size(d.path_variants) as path_var_ct order by d.op", fn, "hg_branches", "2;3;4;5;6", "", true, "UDF:\nreturn mt_var.reports.dispositive_triad.no_variant_haplobranches()'n'nThe Cypher identifies dnode tree branches with no defining variants. There are two scenarios for this:\n1. The branch is defined ONLY by a back mutation.\n2. The branch name end with -x which is a placeholder in the tree\n\n818 sequnces were assigned one of these haplogroups. \nHowever, some of these haplogroups are unique to the FTDNA haplotree and may therefor not be assigned by theHaplogrep3 tool.\n\nNontheless these haplotree assignments were made in the absence of a branch defining variant in the sequence data.", false);
    
               mt_var.excelLib.excelRept.createExcel("MATCH (d:dnode) where size(d.branch_variants)=0 MATCH p=(sp:mt_seq)-[r:hg_parent_child]->(sc:mt_seq) where sc.assigned_hg=d.name  RETURN r.hg_pair, sp.name, sp.assigned_hg, sc.name, sc.assigned_hg, case when r.dropped_path_variants is not null then 1 else 0 end as dropped_ct, case when r.added_variants is not null then 1 else 0 end as added_var_ct, r.dropped_path_variants as dropped_path_variantss, r.added_variants as added_variants, sp.start_back_mutation, sc.start_back_mutation", fn, "dispositive_triad", "5;6", "", false, "There are 19,866 hg_pairs where the child sequence has an assigned hapologroup with no defining branch variants.\nThe dispositive triad shows that 95% of the child sequences have added and 26% dropped variants compared to their haplogroup parent sequence. . ", true);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
 

        return "";
    }
}

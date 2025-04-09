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


public class proof_dropped_path_variants {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String dropped_path_variants(
    
  )
   
         { 
             
        get_dropped();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_dropped();
    }
    
     public static String get_dropped() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "dnode_tree_no_dropped_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
               mt_var.excelLib.excelRept.createExcel("MATCH p=(dp:dnode)-[r:dnode_child]->(dc:dnode) with dc, dp, apoc.coll.intersection(dp.path_variants, dc.path_variants)  as shared_var, size(dp.path_variants) as parent_path_var_ct, size(dc.path_variants) as child_path_var_ct with dp,dc, shared_var, parent_path_var_ct, child_path_var_ct, parent_path_var_ct- size(shared_var) as diff_ct,dc.start_back_mutation as bm return apoc.text.repeat('___', dp.lvl) + dp.name as hg1, dc.name as hg2, dp.lvl as parent_lvl, dc.lvl as child_lvl,dp.seq_ct as parent_seq_ct, dc.seq_ct as child_seq_ct, size(shared_var) as shared_var_ct, parent_path_var_ct, child_path_var_ct, size(bm) as back_mutation_ct,  diff_ct-case when  bm is null then 0 else size(bm) end as  diff_ct, size(dc.branch_variants) as child_branch_variant_ct, shared_var, dp.path_variants as parent_path_variants, dc.path_variants as child_path_variants, dc.branch_variants as child_branch_variants, dc.start_back_mutation as back_mutations, dc.start_restore_mutation as restored_mutation   order by dc.op ", fn, "dnode_tree_varint_accounting", "5;10", "", true, "This query is designed to check the quality of the dnode tree.\nIt verifies that in the haplotree itself there are no tdropped variants\nThe path_variants in the child dnode are the same as those in the parent path_variants after subtracting the child's new branch-defining variants and back mutations.\nThere are 9 exceptions to this statement (set filter on Column K to exclude zeros and nulls); 6 are attributable to a deletion or double back mutation. However, since the proof is looking for dropped mutations, this will not affect the analysis.\n\nNotice that the path variant count is increments from its parent by its own branch variant count because these new variants are added to the path variant list. ", true);
     

        return "";
    }
}

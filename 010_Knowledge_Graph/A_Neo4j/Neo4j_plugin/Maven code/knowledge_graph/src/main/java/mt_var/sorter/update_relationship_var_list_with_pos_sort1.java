/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.sorter;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class update_relationship_var_list_with_pos_sort1 {
    @UserFunction
    @Description("sorts list of variants by position.")

    public String update_rel_var_listg(
        @Name("rel") 
            String rel,
        @Name("rely") 
            String property
  )
   
         { 
             
        update_property(rel, property);
         return "";
            }

    
    
    public static void main(String args[]) {
        // TODO code application logic here
    }
    
     public static String update_property(String rel, String prop) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String cq = "MATCH ()-[r:" + rel + "]->() WHERE r." + prop + " IS NOT NULL WITH r, [variant IN r." + prop + " | { original: variant, position: CASE WHEN size(apoc.text.regexGroups(variant, \"\\\\d+\")) > 0 THEN toInteger(apoc.text.regexGroups(variant, \"\\\\d+\")[0][0]) ELSE 0 END }] AS variants_with_positions UNWIND variants_with_positions AS sorted_data WITH r, sorted_data ORDER BY sorted_data.position ASC WITH r, collect(sorted_data.original) AS sorted_variants SET r." + prop + " = sorted_variants";

        String q = "\"";

//         this did not fix crash from memory issues
//        mt_var.neo4jlib.neo4j_qry.qry_write("CALL apoc.periodic.iterate(" + q  + cq + q + ", {batchSize: 50, parallel: false, retries: 10})");
        try
        {
            mt_var.neo4jlib.neo4j_qry.qry_write(cq);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "\n" + rel + "." + prop + "\n========================================]n");
            
        }
        return "";
    }
}

/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.genlib;

import java.util.regex.Pattern;
import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class id_for_node {
    @UserFunction
    @Description("creates d property for node.")

    public String set_node_ids(
        @Name("node") 
            String node,
        @Name("indx_name") 
            String indx_name,
        @Name("start")
            Long start,
        @Name("sort_item")
            String sort_item
  )
   
         { 
             
        create_id(node, indx_name, start, sort_item);
         return "";
            }

    
    
    public static void main(String args[]) {
              create_id("anode", "ag_id", 100000L, "commop");
    }
    
     public static String create_id(String n, String v, Long ct, String s) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String cq = "match (x:" + n + ") return id(x) as id order by x." + s ;
       
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n");
        
        
        for (int i=0; i<c.length; i++)
        {
            int ccct = ct.intValue() + i;
            String xs[] = c[i].split(Pattern.quote("|"));
            mt_var.neo4jlib.neo4j_qry.qry_write("match(x:" + n + ") where id(x)=" + xs[0] + " set x." + v + "=" + ccct );
 
            
        }
        
        return "";
    }
}

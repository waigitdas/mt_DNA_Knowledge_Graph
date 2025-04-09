/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.back_mutations;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class variant_deletion_path {
    @UserFunction
    @Description("Prunes dnode variants.")

    public String haplopath_to_delete(
        @Name("path_start") 
            String path_start,
        @Name("variant")
            String variant
  )
   
         { 
             
        String p = get_path(path_start,variant);
         return p;
            }

    
    
    public static void main(String args[]) {
        String d = get_path("L0","A263G");
        System.out.println(d);
    }
    
     public static String get_path(String strt, String var) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String q = "\"";  //some block names contain ' ... thus, need "" in the cypher query
        
         
        //dnode nodes do not have back mutations!
        String cq = "MATCH path=(b1: dnode{name:" + q + strt + q + "})-[r: dnode_child*0..99]->(b2) with path,b1,r,b2,[q in relationships(path)|q.revert_to_anc] as rr with path, b1,r,b2 where not apoc.coll.contains(rr,'" + var + "') with collect(b2.name) as haplopaths return haplopaths";  

        
        String c = "[]";
        try
        {
            c = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0];
        }
        catch(Exception e){}
        
        return c.replace("\"","");
    }
}

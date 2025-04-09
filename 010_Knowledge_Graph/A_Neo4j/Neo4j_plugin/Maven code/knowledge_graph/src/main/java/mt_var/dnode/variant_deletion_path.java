/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.dnode;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class variant_deletion_path {
    @UserFunction
    @Description("Identifies path with specific variant which is then deleted byother UDF.")

    public String haplopath_to_delete(
        @Name("path_start") 
            String path_start,
        @Name("variant")
            String variant,
        @Name("refseq") 
            String refseq
  )
   
         { 
             
        String p = get_path(path_start,variant, refseq);
         return p;
            }

    
    
    public static void main(String args[]) {
        String d = get_path("L0","A263G", "rcrs");
        System.out.println(d);
    }
    
     public static String get_path(String strt, String var, String refseqUsed) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String q = "\"";  //some block names contain ' ... thus, need "" in the cypher query
        
        //dnode nodes do not have back mutations!
        String cq = "MATCH path=(b1:" + refseqUsed + "_haplopath{name:" + q + strt + q + "})-[r:" + refseqUsed + "_haplopath_child*0..99]->(b2) with path,b1,r,b2,[q in relationships(path)|q.revert_to_anc] as rr with path, b1,r,b2 where not apoc.coll.contains(rr,'" + var + "') with collect(b2.name) as haplopaths return haplopaths";  
                //"match path=(b1:" + refseqUsed + "_block{name:" + q + strt + q + "})-[r:" + refseqUsed + "_block_child*0..99]->(b2:" + refseqUsed + "_block) with [x in nodes(path)|x.name] as p return p order by size(p) desc";
        
        String c = "[]";
        try
        {
            c = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0];
        }
        catch(Exception e){}
        
        return c.replace("\"","");
    }
}

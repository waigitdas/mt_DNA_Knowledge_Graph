/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class never_completes_probe_hierarchy {
    @UserFunction
    @Description("identifies overlap probes, expands then, creates hierarchy.")

    public String create_probe_hierarchy(
  )
   
         { 
             
        create_hierarchy();
         return "";
            }

    
    
    public static void main(String args[]) {
        create_hierarchy();
    }
    
     public static String create_hierarchy() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        //probe hierarchy
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (po:probe{type:'overlap'}) with po match (pp:probe{type:'position'}) where po.original_probe=pp.probe set pp.subsumed_by=po.pid");

         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (po:probe{type:'overlap'}) with po match (pp:probe{type:'position'}) where po.original_probe=pp.probe with pp,apoc.coll.sort(collect(po.pid)) as popids set pp.subsumed_by=popids, pp.subsumed_ct=size(popids)");

        
        //created parent_child relationships
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (pp:probe) where pp.subsumed_by is not null and pp.ref_start_pos>15 with pp match(pc:probe) where pc.pid in pp.subsumed_by with distinct pp, pc merge (pp)-[r:probe_child]->(pc)");
        
        //add root node and create paent-child relationship to it and the probes with subsumed by value
        mt_var.neo4jlib.neo4j_qry.qry_write("create(p:probe{pid:0,name:'root', ref_start_pos:-1})");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("match(p:probe{pid:0}) with p match (pp:probe) where pp.subsumed_by is not null merge (p)-[r:probe_child]->(pp)");
    
        //create ordpath enabling hierarchical sorting
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path=(pp:probe{pid:0})-[r:probe_child*0..3]->(pc) with path,pc order by pc.ref_start_pos with pc, [x in nodes(path)|x.pid] as ord with pc, ord,mt_var.graph.get_ordpath(ord) as op set pc.op=op, pc.lvl=size(ord)");
            
        //create ordpath enabling hierarchical sorting
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path=(pp:probe{pid:0})-[r:probe_child*0..3]->(pc) with path,pc order by pc.ref_start_pos with pc, [x in nodes(path)|x.pid] as ord with pc, ord,mt_var.graph.get_ordpath(ord) as op set pc.op=op, pc.lvl=size(ord)");
        
        
        
       
        
        return "";
    }
}

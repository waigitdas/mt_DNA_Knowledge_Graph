/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class load_fused_probes {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String fused_probe_load(
          )
   
         { 
             
        load_probes();
         return "";
            }

    
    
    public static void main(String args[]) {
        load_probes();
    }
    
     public static String load_probes()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String fn = "fused_probes_20250322_143857.csv";
        mt_var.neo4jlib.file_lib.copyFileToImportDirectory(fn);
        
        try
        {
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n)-[r]-(p:fused_probe) DELETE r");
        mt_var.neo4jlib.neo4j_qry.qry_write("match (p:fused_probe) delete p");
        }
        catch(Exception e){}
        
        mt_var.neo4jlib.neo4j_qry.CreateIndex("fused_probe","probe");
        
        
//        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fn + "' as line fieldterminator '|' with line with toInteger(line.start) as start, toInteger(line.end) as end, count(*) as ct, line.fusedProbe as fusedProbe, line.pids as source_probes  create(fp:fused_probe{start_pos:start, end_pos:end, seq_ct:ct, probe:fusedProbe, source_probes:source_probes})");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + fn + "' AS line FIELDTERMINATOR '|' WITH line WITH toInteger(line.start) AS start, toInteger(line.end) AS end, count(*) AS ct, line.fusedProbe AS fusedProbe, line.pids AS source_probes CREATE (fp:fused_probe {start_pos: start, end_pos: end, seq_ct: ct, probe: fusedProbe, source_probes: [x IN split(source_probes, ',') | toInteger(x)]})");;

         mt_var.neo4jlib.neo4j_qry.addRowNumbertoNode("pid", "match (n:fused_probe) order by n.start_pos");
         mt_var.neo4jlib.neo4j_qry.CreateIndex("fused_probe", "pid");
         
         
          String lc = "load csv with headers from 'file:///" + fn + "' as line fieldterminator '|' return line ";
          String cq = "match (s:mt_seq{name:line.seq}) with s, line match (p:fused_probe{probe:line.fusedProbe}) merge (s)-[r:seq_fused_probe]->(p)";
           mt_var.neo4jlib.neo4j_qry.APOCPeriodicIterateCSV(lc, cq, 1000);
                 
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:fused_probe) with p unwind p.source_probes as x call { with x match(px:probe{pid:x}) return px.variants as vars} with p, collect(vars) as vars set p.variants=apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(apoc.coll.flatten(vars))))");
        
      mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s)-[r:seq_fused_probe]->(p) WITH s, p MATCH (d:dnode{name:s.assigned_hg})  WITH s, p, d, apoc.coll.intersection(d.path_variants, p.variants) AS cv WHERE size(cv) > 0 MERGE (p)-[rf:fused_probe_dnode]->(d) SET rf.variants = [v IN cv | toString(v)]");
      
                mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path=(p:fused_probe)-[r:fused_probe_dnode]->(d:dnode) with d, apoc.coll.sort(collect(p.pid)) as pids set d.fused_probes=pids  ")   ;
                
                
               mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:fused_probe)  WITH p, CASE  WHEN p.start_pos >= 10 AND p.start_pos <= 574 THEN 'HVR2'   WHEN p.start_pos >= 575 AND p.start_pos <= 16000 THEN 'CR'  WHEN p.start_pos >= 16017 AND p.start_pos <= 16545 THEN 'HVR1' ELSE NULL  END AS newRegion with p, newRegion WHERE newRegion IS NOT NULL SET p.region = newRegion")
                 ;

        mt_var.neo4jlib.neo4j_qry.qry_write("Started streaming 46 records after 9 ms and completed after 1241 ms.");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:fused_probe) with p unwind p.variants as x call{ with x match (v:variant{name:x}) return v } merge (p)-[r:fused_probe_variant{pos:v.pos}]->(v)");
        
        mt_var.neo4jlib.neo4j_qry.qry_write(("MATCH path=(s)-[r:seq_fused_probe]->(p) where s.assigned_hg is not null with p, apoc.coll.sort(collect(distinct s.assigned_hg)) as sn with p,sn set p.hg_ct=size(sn), p.haplogroups=sn"));
        
        return "";
    }
}

/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class load_seq_pos_probe_relationships {
    @UserFunction
    @Description("creates seq_probe relationship from previously created pipe-delimited file.")

    public String loads_sequences_pos_probe_rels(
  )
   
         { 
             
        load_rels();
         return "";
            }

    
    
    public static void main(String args[]) {
        load_rels();
    }
    
     public static String load_rels() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

         String seqrelFile = "seq_position_probes_20240717_105103.csv";
         mt_var.neo4jlib.file_lib.copyFileToImportDirectory(seqrelFile);
     
       String cq =  "load csv with headers from 'file:///" + seqrelFile + "' as line fieldterminator '|' with line match(s:mt_seq{seq_id:toInteger(line.seq_id), name:line.seq}) match(p:probe{pid:toInteger(line.pid)}) merge (s)-[r:seq_probe]->(p)";  
       mt_var.neo4jlib.neo4j_qry.qry_write(cq);
        
         
        return "";
    }
}

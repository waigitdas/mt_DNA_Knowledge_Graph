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


public class load_seq {
    @UserFunction
    @Description("loads sequences from previously created pipe-delimited file.")

    public String loads_GenBank_sequences(
  )
   
         { 
             
        load_seqs();
         return "";
            }

    
    
    public static void main(String args[]) {
        load_seqs();
    }
    
     public static String load_seqs() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

         String seqFile = "GenBank_full_sequences_20241006_071511.csv";
         mt_var.neo4jlib.file_lib.copyFileToImportDirectory(seqFile);
     
       mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + seqFile + "' as line fieldterminator '|' with line create(s:mt_seq{seq_id:toInteger(line.seq_id), name:line.name, source:line.source, assigned_hg:line.assigned_hg,fullSeq:line.fullSeq})");
        
         
        return "";
    }
}

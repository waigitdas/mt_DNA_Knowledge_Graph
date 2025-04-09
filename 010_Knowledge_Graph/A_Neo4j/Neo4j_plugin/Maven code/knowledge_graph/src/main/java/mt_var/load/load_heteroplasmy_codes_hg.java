/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
import mt_var.genlib.tracker;

public class load_heteroplasmy_codes_hg {
    @UserFunction
    @Description("Loads referenc and test sequences from GenBank and elsewheree.")

    public String load_codes_for_heteroplasmy(
  )
   
         { 
             
        load_codes();
         return "";
            }

    
    
    public static void main(String args[]) {
        load_codes();
    }
    
     public static String load_codes() 
    {
       mt_var.neo4jlib.neo4j_info.neo4j_var();
       mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
 
        //load heteroplasmy codes
        mt_var.neo4jlib.file_lib.copyFileToImportDirectory("mt_heteroplasmy_codes.csv");
      mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///mt_heteroplasmy_codes.csv' AS line FIELDTERMINATOR ',' with line.code as c, apoc.coll.sort(collect(line.nucleotide)) as n with c,n where size(n)>1  create (h:mt_heteroplasmy_codes{code:toString(c), nucleotides:n})");
       
  
        
        return "";
    }
}

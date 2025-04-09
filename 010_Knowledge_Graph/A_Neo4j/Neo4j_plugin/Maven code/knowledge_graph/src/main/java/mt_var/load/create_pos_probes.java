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


public class create_pos_probes {
    @UserFunction
    @Description("loads position probes from previously create pipe-delimited file..")

    public String loads_pos_probes_from_file(
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

         String fnpos = "pos_probes_for_import_20240717_093122.csv";
         mt_var.neo4jlib.file_lib.copyFileToImportDirectory(fnpos);
     
       String cq = "LOAD CSV WITH HEADERS FROM 'file:///" + fnpos + "' as line FIELDTERMINATOR '|' create (p:probe{pid:toInteger(line.pid), ref_start_pos:toInteger(line.start_pos), ref_probe:toString(line.ref_probe), probe:toString(line.test_probe), seq_ct:toInteger(line.ct), start_pos:toInteger(line.test_start), end_pos:toInteger(line.test_end),length:toInteger(line.len),frameshift:toInteger(line.sz_diff),left_flank_probe:line.left_flank_probe, right_flank_probe:line.right_flank_probe,ref_len:toInteger(line.ref_len), test_len:toInteger(line.test_len),len_diff:toInteger(line.len_diff), type:'position' })";
        mt_var.neo4jlib.neo4j_qry.qry_write(cq);

        
         
        return "";
    }
}

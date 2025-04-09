/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class create_loadable_pos_probes {
    @UserFunction
    @Description("Loads pos probes.")

    public String create_csv_pos_probes(

  )
   
         { 
             
        String s = load_to_position_probe_nodes();
         return s;
            }

    
    
    public static void main(String args[]) {
        String s = load_to_position_probe_nodes();
        System.out.println(s);
    }
    
     public static String load_to_position_probe_nodes() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
//        mt_var.neo4jlib.neo4j_qry.qry_write("match(l:lookup_probe) delete l");
                
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe_pos");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "ref_probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "seq_probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex( "probe", "probe_name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "var_alert");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "method");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "parts_pattern");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe_id");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe");
 
        
        
       String fn = " pangenome_pos_probe_20240630_103207.csv"; 
     
        try {
            Files.copy(Paths.get(mt_var.neo4jlib.neo4j_info.ref_data_dir + fn), Paths.get(mt_var.neo4jlib.neo4j_info.Import_Dir + fn), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        }
        String fn2 = "pos_probes_for_import.csv;";
                
        mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited("load csv with headers from 'file:///" + fn + "' as line fieldterminator '|'  with line where toInteger(line.test_probe_length)<14000 and abs(size(line.ref_probe)-size(line.test_probe))<12  return count(*) as ct, toInteger(line.ref_probe_start_pos) as start_pos,toInteger(size(line.ref_probe)) as ref_len, toInteger(line.test_probe_length) as test_len, size(line.ref_probe)-size(line.test_probe) as len_diff, line.test_probe as test_probe, line.ref_probe as ref_probe, line.left_flank_prove as left_flank_probe, line.right_flank_probe as right_flank_probe order by start_pos ", fn2 );
//         mt_var.genlib.tracker.writeTracker("aggregated and filtered probe csv created.");

         String rw[] = mt_var.neo4jlib.file_lib.ReadFileByLineWithEncoding(mt_var.neo4jlib.neo4j_info.Import_Dir + fn2).split("\n");
         
        String fn3 = "pos_probes_for_import_" + mt_var.genlib.current_date_time.getDateTime() + ".csv;";
         File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fn3);
         FileWriter fw = null;
         try
         {
             fw = new FileWriter(f);
             fw.write("pid|ct|start_pos|ref_len|test_len|len_diff|test_probe|ref_probe|left_flank_probe|right_flank_probe\n");
         }
         catch(IOException e)
         {}
         
         int pid = 0;
         for(int i=0; i<rw.length; i++)
         {
             pid = 1000000 + i + 1;
             try
             {
                 fw.write(pid + "|" + rw[i] + "\n");
                 fw.flush();
             }
             catch(IOException ex){}
         }
         
         try
         {
             fw.flush();
             fw.close();
         }
         catch(IOException e){}
         
         
        //create probes
        String cq = "LOAD CSV WITH HEADERS FROM 'file:///" + fn3 + "' as line FIELDTERMINATOR '|' create (p:probe{pid:toInteger(line.pid), ref_start_pos:toInteger(line.start_pos), ref_probe:toString(line.ref_probe), probe:toString(line.test_probe), seq_ct:toInteger(line.ct), start_pos:toInteger(line.test_start), end_pos:toInteger(line.test_end),length:toInteger(line.len),frameshift:toInteger(line.sz_diff),left_flank_probe:line.left_flank_probe, right_flank_probe:line.right_flank_probe,ref_len:toInteger(line.ref_len), test_len:toInteger(line.test_len),len_diff:toInteger(line.len_diff), type:'position' })";
        mt_var.neo4jlib.neo4j_qry.qry_write(cq);
   
        
        return "completed";        
        
    }
}

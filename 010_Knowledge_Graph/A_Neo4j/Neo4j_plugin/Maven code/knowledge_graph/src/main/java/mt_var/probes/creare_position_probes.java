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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class creare_position_probes {
    /**
     * This function receives n parameters. It iterates through the mt_seq nodes, creating in silico probes for each.
     * The output is a pipe-delimiited file which is archived for future use, which involves loading the knowledge graph with this file andthe LOAD CSV function available in Neo4j.
     * @parm none
     * @return pipe-delimited file
     * @author: David A Stumpf, MD, PhD
     */
    private static int pos;
    private static int left_flank;

    private static int[] counts;
    private static FileWriter fw;
    private static int ct = 0;
    // static Object[] parts_pattern_prefix;
 
    @UserFunction
    @Description("")

    public String create_probes_all_seq_positions(
        @Name("refseq") 
            String refseq
  )
   
         { 
             
        create_probes(refseq);
         return "";
            }

    
    
    public static void main(String args[]) {
        create_probes("rsrs");
    }
    
    
     public static  String create_probes(String refseqUsed) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

      //load lookup probes
       String luf = "rsrs_look_up_probes_240531.csv";
       mt_var.neo4jlib.file_lib.copyFileToImportDirectory(mt_var.neo4jlib.neo4j_info.ref_data_dir + luf);
       mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + luf + "' as line FIELDTERMINATOR ','  create (l:lookup_probe{probe:toString(line.probe), pos:toInteger(line.pos), refseq:toString(line.refseq)})");
       

       String ref_seq_name;
        if (refseqUsed.compareTo("rcrs")==0)
        {
            ref_seq_name="NC_012920";
        }
        else
        {
            ref_seq_name = "RSRS";
        }
        
        
        String cq = "";
        left_flank = 20;

       String delimiter = "|";
        ct = 0;
            
        String fn = refseqUsed + "_pos_probe_reference_file_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fn);
        String fnerr = refseqUsed + "_all_pos_probe_errors_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        File ferr = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fnerr);
        FileWriter fwerr = null;
  
        try
        {
            fw = new FileWriter(f);
            fw.write("seq|ref_probe_start_pos|test_probe_start_pos|test_probe_end_pos|test_probe_length|frameshift|ref_probe|test_probe|left_flank_probe|right_flank_probe|diff|error\n");
            fw.flush();
            
            fwerr = new FileWriter(ferr);
            fwerr = new FileWriter(ferr);
            fwerr.write("");
        }
        catch (IOException e){}
        

       //////////////////////////////////////////////////////////////////////////
       //////////////////////////////////////////////////////////////////////////
        
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str("match (s:mt_seq{source:'GenBank'}) return s.name as name order by name").split("\n");
        String cc[]=null;
        mt_var.probes.indiv_seq_position_probes ip = new mt_var.probes.indiv_seq_position_probes();
        for (int i=0; i<c.length; i++)
        {
            try
            {
                String r = ip.indiv_probes_all_positions(c[i]);
                if(r.compareTo("")!=0)
                {
                    fw.write(r) ;
                     fw.flush();
                } 
           }
            catch(IOException e){}
        }
     
        
        try
        {
            fw.flush();
            fw.close();
        }
        catch(IOException e){}
        
        //flag seq_ct of probesl
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fn + " as line fieldterminator '|' with  line.seq as seq, count(*) as ct  match (s:mt_seq{name:seq}) with s, ct set s.probe_ct=ct");
        
        
 return "completed";
}  //end function
     
}  //end class

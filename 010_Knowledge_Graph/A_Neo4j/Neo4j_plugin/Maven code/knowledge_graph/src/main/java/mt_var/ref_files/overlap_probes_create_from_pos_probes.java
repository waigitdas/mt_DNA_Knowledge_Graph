/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.ref_files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Objects.nonNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
//import mt_var.load.load_initial_knowledge_graph_from_csv;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class overlap_probes_create_from_pos_probes {
    @UserFunction
    @Description("finds and manages overlap probes")

    public String manage_overap_probes(
  )
   
         { 
             
        overlaps();
         return "";
            }

    
    
    public static void main(String args[]) {
        String s = overlaps();
        System.out.println(s);
    }
    
     public static String overlaps()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
//        
                /////////////////////////////////////////////////////////////
       String fnt = "traker.csv";
        File ft = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fnt);
        FileWriter fwt = null;
        
        try
        {
            mt_var.neo4jlib.neo4j_info.fwtracker = new FileWriter(ft);
            mt_var.neo4jlib.neo4j_info.fwtracker.write("date_time,elapsed_time,item\n");
        }
       catch(IOException e){}

        String cq = "";
 
        mt_var.neo4jlib.neo4j_info.start_time = System.nanoTime();
        mt_var.genlib.tracker.writeTracker("start time");
        /////////////////////////////////////////////////////////////
  
         
        
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe_pos");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "ref_probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "seq_probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex( "probe", "probe_name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "var_alert");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "method");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "parts_pattern");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe_id");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "probe");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "source");
        mt_var.neo4jlib.neo4j_qry.CreateIndex(" variant","name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex(" variant","pos");

        ///////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////

        mt_var.load.load_seq ls = new mt_var.load.load_seq();
        ls.loads_GenBank_sequences();
        mt_var.genlib.tracker.writeTracker("sequences loaded");
        
        mt_var.load.create_pos_probes lpp = new mt_var.load.create_pos_probes();
        lpp.loads_pos_probes_from_file();
        mt_var.genlib.tracker.writeTracker("position probes loaded");
        
        mt_var.load.load_probes_variants_relationahips lv = new mt_var.load.load_probes_variants_relationahips();
        lv.loads_variants_from_file();
        mt_var.genlib.tracker.writeTracker("variants loaded");
        
        mt_var.load.load_seq_pos_probe_relationships lsr = new mt_var.load.load_seq_pos_probe_relationships();
        lsr.loads_sequences_pos_probe_rels();
        mt_var.genlib.tracker.writeTracker("seq_pos relationships loaded");
        
        cq = "MATCH (p1:probe) with p1 MATCH (p2:probe) where p1.probe<>p2.probe and p1.probe<>replace(p1.probe,p2.probe,'') and p1.ref_start_pos>25  with p1, p2 unwind p2.variants as x call { with x, p1 with case when right(x,1)='d' and apoc.coll.contains(p1.variants,x)=false then 1 else 0 end as del return del } with p1,p2,sum(del) as dels with p1,p2,dels  where size(apoc.coll.subtract(p2.variants,p1.variants))>0 with dels,p1,p2, mt_var.probes.dmp_two_probes(p1.probe,p2.probe) as dmp with dels, p1, p2, dmp where dmp<>replace(dmp,'EQUAL','') RETURN dels, p1.pid as id1, p2.pid as id2,p1.seq_ct as seq_ct1, p2.seq_ct as seq_ct2, p1.ref_len as ref_len1, p2.ref_len as ref_len2, p1.ref_start_pos as ref_start_pos1, p1.ref_start_pos + p1.ref_len-1 as ref_end1, p2.ref_start_pos as ref_start_pos2, p2.ref_start_pos+p2.ref_len-1 as ref_end2,  p1.variants as variants1, p2.variants as variants2, p1.probe as probe1, p2.probe as probe2,dmp,mt_var.variants.find_similar_variants(p1.variants, p2.variants) as similar_variants "; 
        String fn1 = "overlapping_probes_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited(cq, fn1);
        mt_var.genlib.tracker.writeTracker("overlap probes identified");
        
        //String fn1 = "overlapping_probes_20240626_085241.csv";
        String co[] = mt_var.neo4jlib.file_lib.readFileByLine(mt_var.neo4jlib.neo4j_info.Import_Dir +  fn1).split("\n");
    
//        ArrayList<mt_var.probes.overlap_probe_pairs> lop = new ArrayList<mt_var.probes.overlap_probe_pairs>();
        mt_var.ref_files.overlap_probe_pairs op = null;
        String cs[] = null;
        
        for (int i=1; i<co.length; i++)
        {
            try
            {
            cs = co[i].split(Pattern.quote("|"));
            op = new mt_var.ref_files.overlap_probe_pairs();
            op.dels = Integer.parseInt(cs[0]);
            op.id1 = Integer.parseInt(cs[1]);
            op.id2 = Integer.parseInt(cs[2]);
            op.seq_ct1 = Integer.parseInt(cs[3]);
            op.seq_ct2 = Integer.parseInt(cs[4]);
            op.ref_len1 =Integer.parseInt(cs[5]);
            op.ref_len2 = Integer.parseInt(cs[6]);
            op.ref_start_pos1 =Integer.parseInt(cs[7]);
            op.ref_start_pos2 =Integer.parseInt(cs[9]);
            op.ref_end1 =Integer.parseInt(cs[8]);
            op.ref_end2 =Integer.parseInt(cs[10]);
            if (cs[11].compareTo("NULL")==0)
            {
                  op.variants1 =  Collections.<String>emptyList();  ;
            }
             else
             {
                op.variants1 = new ArrayList<>(Arrays.asList(cs[11].replace("[","").replace("]","")));
             }
            op.variants2 = new ArrayList<>(Arrays.asList(cs[12].replace("[","").replace("]","")));
            op.probe1 = cs[13].replace("\"","");
            op.probe2 = cs[14].replace("\"","");
            op.dmp = cs[15];
            if(cs.length==17)
            {
            op.similar_variants = cs[16];
            }
            else
            {
                op.similar_variants = "";
            }
             
                        
            
            cq = "create (n:overlap_probe_pairs{dels:" + op.dels + ",id1:" + op.id1 +", id2:" + op.id2 + ", seq_ct1:" + op.seq_ct1 + ", seq_ct2:" + op.seq_ct2 + ", ref_len1:" + op.ref_len1 + ", ref_len2:" + op.ref_len2 + ", ref_start_pos1:" + op.ref_start_pos1 + ", ref_start_pos2:"+ op.ref_start_pos2 + ", ref_end1:" + op.ref_end1 + ", ref_end2:" + op.ref_end2 + ",  variants1:" + op.variants1 + ", variants2:" + op.variants2 + ", probe1:'" + op.probe1 + "', probe2:'" + op.probe2 + "', dmp:'" + op.dmp + "', similar_variants:'" + op.similar_variants + "', type:'overlap'})";
            mt_var.neo4jlib.neo4j_qry.qry_write(cq); 
            int d4=0;
            }
            catch(Exception e)
            {
                System.out.println("error type 1: " + i + "\t" + op.id1 + "\t" + op.id2 + "\t" + e.getMessage() + "\t" + cq);
            }
//            lop.add(op);
            
            
        }
        
              mt_var.genlib.tracker.writeTracker("overlapping probe pairs nodes created");
  
       ////////////////////////////////////////////////////////////////////////////////////////////
       ////////////////////////////////////////////////////////////////////////////////////////////
       
        String fn_new_probes = "new_probes_from_overlaps.csv";
        File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fn_new_probes);
        FileWriter fw = null;
       
        try
        {
            fw = new FileWriter(f);
            fw.write("pos|new_start|new_end|new_len|new_probe|orig_probe|new_seq_ct|variants|subsumed_probe_id|large_probe_id|left_flank|right_flank|seqs\n");
        }
        catch(IOException e){}
 
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str("MATCH (o:overlap_probe_pairs) with o.id2 as id2, o.ref_len2 as len2,o.ref_start_pos2 as start2, o.ref_end2 as end2, o.probe2 as probe2, apoc.coll.sort(collect(o.id1)) as id1s, max(o.ref_len1) as mx,o.variants2 as var2 return id2, start2, end2, len2, mx, mx - len2 as append_len,size(id1s) as size, id1s, probe2,var2 order by id2 ").split("\n");
        
        String probe ="";
        int left_flank = 0;
        int right_flank =0;
        String new_probe = "";
        int start = 0;
        int ct =0;
        int probe_id =0;
        String variants = "";
        String overlaps="";
        
        for (int i=0; i<c.length; i++)
        {
            cs = c[i].split(Pattern.quote("|"));
            probe = cs[8];
            start = Integer.parseInt(cs[1]);
            left_flank = Integer.parseInt(cs[5]); //left & right flanks the same unless left too close to start pos
            if(left_flank-1>start) {left_flank = 2;}
            
            right_flank = Integer.parseInt(cs[5]);
            overlaps = cs[7];
            variants = cs[9];
            probe_id = Integer.parseInt(cs[0]);
            
            cq = "with " + left_flank + " as left_padding, " + right_flank + " as right_padding," + probe +  " as probe match(s:mt_seq)  with s, left_padding, right_padding, probe, split(s.fullSeq,probe) as ss with s,ss,size(ss[0])+1 as new_start, left_padding, right_padding, probe where size(ss)=2 with apoc.coll.sort(collect(distinct size(ss[0])+1)) as pos, new_start, probe, right(ss[0],left_padding-1) as lf,  substring(ss[1],0,right_padding) as rf, count(*) as ct,collect(s.name) as sn  with pos,new_start - size(lf) -1 as new_start,size(lf)+size(probe)+size(rf) as new_len, lf + probe + rf as new_probe,probe, ct, sn return pos,new_start,new_start+new_len-1 as new_end, size(new_probe) as new_len, new_probe,probe, ct, " + variants + " as var, " + probe_id + " as pids, " + overlaps  + " as overlaps, " + left_flank + " as left_flank, " + right_flank + " as right_flank, sn as seqs  order by ct desc";
          
            String css[] =null;
            try
            {
                css = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n");
                 for(int j=0; j<css.length;j++)
                {
                    //System.out.println(i + "\t" + ct + "\t" + css[j].replace("\"","") );
                    try
                    {
                        fw.write(css[j].replace("\\","").replace("\"","").replace("[","").replace("]","") + "\n");  //.replace("\"","").replace("[","").replace("]","") 
                        fw.flush();
                    }
                    catch(IOException e){}
                    ct = ct + 1;
                }
                
            }
            catch(Exception e)
            {
                    System.out.println("error type 2: " + i + "\t" + ct + "\terror: " + cq);
                    ct = ct + 1;
            }

        } //next i
        
        
        try
        {
            fw.flush();
            fw.close();
        }
        catch(IOException e){}
        
        
        
        String newFile = "new_probes_from_overlaps_2" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        String npf[] = mt_var.neo4jlib.file_lib.ReadFileByLineWithEncoding(mt_var.neo4jlib.neo4j_info.Import_Dir + fn_new_probes).split("\n");
        File fnew = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + newFile);
        FileWriter fwn = null;
        try
        {
            fwn = new FileWriter(fnew);
        }
        catch(IOException e){}

       int mxid = Integer.parseInt(mt_var.neo4jlib.neo4j_qry.qry_to_csv("match (p:probe) return max(p.pid) as mx").split("\n")[0].split(",")[0]);  

       try
       {
           fwn.write("pid|" + npf[0] + "\n");
       }
       catch(IOException e){}
       
        for (int i=1; i<npf.length; i++)
        {
            int newId = mxid + 1 + i;
            try
            {
            fwn.write(newId +"|" + npf[i] + "\n");
            }
            catch(IOException e){}
        }
      
        try
        {
            fwn.flush();
            fwn.close();
        }
        catch(IOException e){}
        
        
        
        
        mt_var.genlib.tracker.writeTracker("new probes from overlaps file written to import directoty");
  
        return "completed";
    }
}

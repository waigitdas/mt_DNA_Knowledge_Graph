/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class indiv_seq_position_probes {
    private static ArrayList<mt_var.probes.wseq_probe> sp;
    private static ArrayList<mt_var.probes.wseq_probe> pfinal;
    private static mt_var.probes.wseq_probe ptemp;
    private static int last_ref_end_pos;
    private static int test_start_pos;
//    private static FileWriter fw;
    private static String left_flank;
    private static String right_flank;
    private static Boolean flank_found;
    private static int concat_start_row;
    private static String concat_ref_probe;
    private static int iref_probe;
    @UserFunction
    @Description("Compute probes between those of known flanking regions.")

    public String indiv_probes_all_positions(
        @Name("seq_name")
            String seq_name
      )
   
         { 
             
        String r = create_probes(seq_name);
         return r;
            }

    
    
    public static void main(String args[]) {
        String r = create_probes("'AY195780'");
                //"'DQ112735'");
        
        
        System.out.println(r);
       
    }
    
    
     public static  String create_probes(String seq) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        int iref_pos = 1;
        int itest_pos = 2;
        iref_probe = 3;
        int itest_probe = 4;
        
        String cq = "";
        test_start_pos = 0; 
        last_ref_end_pos = 0; 
        flank_found = false;
        left_flank = "";
        right_flank = "";
        concat_start_row = 0;
        concat_ref_probe = "";

       //////////////////////////////////////////////////////////////////////////
       //////////////////////////////////////////////////////////////////////////
       String s = "";
       cq = "match (lo:lookup_probe) with lo  MATCH (s:mt_seq{name:" + seq + "})  where lo.pos%40=0   with lo, split(s.fullSeq,lo.probe) as ss  with size(ss)=2 as bool, lo.pos as ref_pos, case when size(ss)=2 then size(ss[0]) + case when lo.pos = 0 then 0 else 1 end else 0 end as test_pos, lo.probe as ref_probe, case when size(ss)=2 then lo.probe else '' end as test_probe  return bool,ref_pos, test_pos, ref_probe,test_probe order by ref_pos";
               //"match (lo:lookup_probe{refseq:'rsrs'}) with lo  MATCH (s:mt_seq{name:" + seq + "})  where lo.pos%40=0   with lo, s.fullSeq<>replace(s.fullSeq,lo.probe,'') as diff  return lo.pos-40 as pos, lo.probe, case when diff=false then '' else lo.probe end as test_seq, diff order by pos";
       String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_csv(cq).split("\n");
       if(c.length==1){return "";}
       
       //all rows from the query.
       //those rows with same ref and test probes will be droped from final result
       sp = new ArrayList<mt_var.probes.wseq_probe>();
       
       //retained rows with computed test pprobe
        pfinal = new ArrayList<mt_var.probes.wseq_probe>();
        
        int ct = 0;
        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////
        //popuate initial array leave blank values for test_proves where their sequence differs from RSRS
       for (int i=0; i<c.length; i++)
           x:
       {
           String cs[] = c[i].split(",");
           mt_var.probes.wseq_probe p = new mt_var.probes.wseq_probe();
           p.ref_start_pos = Integer.parseInt(cs[iref_pos].replace("\"",""));

           p.start_pos = Integer.parseInt(cs[itest_pos].replace("\"",""));
           p.ref_probe = cs[iref_probe].replace("\"","").strip();
           p.test_probe = cs[itest_probe].replace("\"","").strip(); 
           
           if (p.get_test_probe().compareTo("")!=0)
           {
               //initial iterations of i will find the 1st test value that is the same as the ref value )e.g., 1st flanking probe)
               p.end_pos=p.start_pos + 39;
               p.length = 40;
               if(flank_found.compareTo(true)==0)
                       {
                           sp.get(concat_start_row).right_flank= p.get_ref_probe();
                           flank_found = false;
                           cq = "MATCH (s:mt_seq{name:" + seq + "}) with s, split(split(s.fullSeq,'" + sp.get(concat_start_row).get_left_flank() + "')[1],'" + sp.get(concat_start_row).get_right_flank() + "')[0] as seq with seq,  size(split(s.fullSeq,seq)[0]) +1 as pos return  pos, seq";
                          String y[] = mt_var.neo4jlib.neo4j_qry.qry_to_csv(cq).split("\n")[0].split(",");
                          try
                          {  //this will fail is the 1st row has no test probe equal to ref probe. The catch processes the 1st row
                          sp.get(concat_start_row).start_pos = Integer.parseInt(y[0].replace("\"",""));
                          }
                          catch(Exception e)
                          {
                              fix_pos_zero(seq, i, c, cs, e.getMessage());
                              break x;
                          }
                          sp.get(concat_start_row).test_probe = y[1].replace("\"","");
                          sp.get(concat_start_row).cq = cq;
                          sp.get(concat_start_row).length = sp.get(concat_start_row).get_test_probe().length();
                          
                          ptemp.test_probe = sp.get(concat_start_row).get_test_probe();
                          ptemp.length  = sp.get(concat_start_row).get_length();
                          concat_ref_probe = concat(concat_start_row, i-1);;
                          ptemp.ref_probe = concat_ref_probe;
                          ptemp.cq  = sp.get(concat_start_row).cq;
                          ptemp.start_pos = sp.get(concat_start_row).get_start_pos();
                          ptemp.right_flank = sp.get(concat_start_row).get_right_flank();
                          ptemp.end_pos = ptemp.get_start_pos() + ptemp.get_length() -1;
                          ptemp.frameshift = ptemp.get_start_pos() - ptemp.get_ref_start_pos(); 
                          
                        DiffMatchPatch dmp = new DiffMatchPatch();
                        LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(ptemp.get_ref_probe(),ptemp.get_test_probe());
//                        dmp.diffCleanupSemantic(diffs);
                        String ds = "";
                        for (int d=0; d<diffs.size(); d++)
                        {
                            ds = ds + diffs.get(d).operation + ": " + diffs.get(d).text;
                            if (d<diffs.size())
                            {
                                ds = ds + "; ";
                            }
                            }
                        ptemp.diff = ds;
                                
                          
                          pfinal.add(ptemp);
                          
                           concat_start_row = 0;
                           ct = 0;
                       }            
           }
           else
           { //blank test_probe; find flanking ref_probes
               if(flank_found.compareTo(false)==0)
               {
                   if(p.get_ref_start_pos()==0)
                   {
                       fix_pos_zero(seq,i, c, cs, "");
                   }
                   else
                   {
                   ct = ct + 1;
                   try
                   {
                   ptemp = new mt_var.probes.wseq_probe();
                   p.left_flank = sp.get(i-1).get_ref_probe();
                   concat_ref_probe = p.get_ref_probe();
                   concat_start_row = i;
                   flank_found = true;
                   ptemp.left_flank = p.left_flank;
                   ptemp.ref_start_pos = p.ref_start_pos;
                   }
                   catch(Exception e){
                       int fgg=0;
                   }
                   }
               }
               
           }
           sp.add(p);
       }  //next probe

        //if the last rows  have no no test probe, must use function to create them
        if(sp.get(sp.size()-1).get_test_probe().compareTo("")==0)
        {
            fix_last_probe(seq, c);
        }
     
      String delimiter = "|";  //;/\n";
      String retn = "";
       for(int i=0; i<pfinal.size(); i++)
      {
          retn = retn + seq.replace("\"","") + delimiter + pfinal.get(i).get_ref_start_pos() + delimiter +  pfinal.get(i).get_start_pos() + delimiter +  pfinal.get(i).get_end_pos() + delimiter + pfinal.get(i).get_length() + delimiter + pfinal.get(i).get_frameshift() + delimiter +  pfinal.get(i).get_ref_probe() + delimiter +  pfinal.get(i).get_test_probe() + delimiter +  pfinal.get(i).get_left_flank() + delimiter + pfinal.get(i).get_right_flank() + delimiter +  pfinal.get(i).get_diff() + "\n"; 
       }
       
 return retn;
}  //end function
     
     
     private static String concat(int s, int e)
     {
        String r = "";
        for (int i=s; i<e+1; i++)
        {
            r = r + sp.get(i).get_ref_probe();
        }
         return r;
     }
     
     private static void fix_pos_zero(String seq, int i, String[] c,  String[] cs, String error)
     {
                try
                      {
                      ptemp.left_flank="";
                      }
                      catch (Exception e)
                      {
//                          return "";
                      }
                      String c2[] = c[i+1].split(",");
                      ptemp.right_flank  = c2[3].replace("\"","");
                      ptemp.ref_probe = cs[iref_probe].replace("\"","").strip();
                      ptemp.ref_start_pos = 0;
                      ptemp.start_pos = 0;
                      String cq = "MATCH (s:mt_seq{name:" + seq + "})  with split(s.fullSeq,'" + ptemp.get_right_flank() + "')[0] as ss return size(ss) as sz, ss";
                      String x[] = mt_var.neo4jlib.neo4j_qry.qry_to_csv(cq).split("\n")[0].split(",");
                      ptemp.length = Integer.parseInt(x[0].replace("\"",""));
                      ptemp.test_probe = x[1].replace("\"","");
                      ptemp.end_pos = ptemp.length -1;
                      ptemp.frameshift = ptemp.get_start_pos() - ptemp.get_ref_start_pos(); 
                      ptemp.error = error;
                      pfinal.add(ptemp);
     }  // end fix_pos_zero function
     
     private static void fix_last_probe(String seq, String[] c)
             {
                 mt_var.probes.wseq_probe p2 = new mt_var.probes.wseq_probe();
 
                 int first_blank_test_row = 0;
                 for (int i=c.length-1; i>0; i--)
                 {
                    String x[] = c[i].split(",");
                    if(x[0].compareTo("TRUE")==0)
                    { 
                        //found left flank and there is no right flank.
                        p2.left_flank = x[3].replace("\"","");
                         
                        String y[] = mt_var.neo4jlib.neo4j_qry.qry_to_csv("MATCH (s:mt_seq{name:" + seq + "}) with split(s.fullSeq,'" + p2.get_left_flank() + "') as ss with size(ss[0])+40 as pos, ss[1] as probe   return pos, probe").replace("\n","").split(",");
                        p2.start_pos = Integer.parseInt(y[0].replace("\"","").strip()) +1;
                        p2.test_probe = y[1].replace("\"","");
                        p2.length = p2.test_probe.length();
                        p2.end_pos = p2.start_pos + p2.length-1;
                        p2.right_flank ="";

                        String z[] = mt_var.neo4jlib.neo4j_qry.qry_to_csv("MATCH (s:mt_seq{name:'AAAA_RSRS'}) with split(s.fullSeq,'" + p2.get_left_flank() + "') as ss with size(ss[0])+40 as pos, ss[1] as probe   return pos, probe").replace("\n","").split(",");
                        p2.ref_start_pos = Integer.parseInt(z[0].replace("\"","").strip()) +1;
                        p2.ref_probe = z[1].replace("\"","");
                        first_blank_test_row = i + 1;
                         break;
                    }
                 }
                 
                       DiffMatchPatch dmp = new DiffMatchPatch();
                        LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(p2.get_ref_probe(),p2.get_test_probe());
//                        dmp.diffCleanupSemantic(diffs);
                        String ds = "";
                        for (int d=0; d<diffs.size(); d++)
                        {
                            ds = ds + diffs.get(d).operation + ": " + diffs.get(d).text;
                            if (d<diffs.size())
                            {
                                ds = ds + "; ";
                            }
                            }
                        p2.diff = ds.replace("\n","");
                        p2.frameshift =  p2.get_start_pos() - p2.get_ref_start_pos(); 
                    
                 pfinal.add(p2);
                 
}  //end function
             
}  //end class

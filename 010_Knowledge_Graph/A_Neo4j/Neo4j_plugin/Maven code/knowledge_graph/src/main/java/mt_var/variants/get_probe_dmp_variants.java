/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class get_probe_dmp_variants {
    @UserFunction
    @Description("Variants in a specific probe computed by aligning the probe first with loook up probes.")

    public Map<String, Object> compute_probe_variants(
        @Name("probe_id") 
            Long probe_id
  )
   
         { 
             
        //returns a pipe-delimited string with variant parameters and the diff-match-patch intermediary result.     
        //probes may have multiple variants and caoncatenate probes will have at least one per 40-base region.
        //aligned reference and tesr sequences are compared using diff-match-patch
        //results are accumulated in the gvars object (list of gfgVar class instatiations)
//        calling function must process results based on its requirements
        Map<String, Object> r = get_variants(probe_id);
         return r;
            }

    
    
    public static void main(String args[]) {
//         get_variants(81678L);   //length=20
        Map<String, Object> r = null;
    
//        r = get_variants(1014038L); // 8281-8208d + 8210d
//        r = get_variants(1013896L);
//        r = get_variants(1000783L);  //1014068L);
//        r = get_variants(1000065L);
//        r = get_variants(1037316L);
        r = get_variants(1000049L);
        System.out.println("==================================================\n\n");
        r = get_variants(1035920L);
//         System.out.println(r);
    }
    
     public static Map<String, Object> get_variants(Long pid) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
     
     String cq = "MATCH (p:probe) WHERE p.pid = " + pid + 
                    " RETURN p.ref_start_pos, p.ref_probe, p.probe, " +
                    "p.orig_ref_start_pos, p.original_probe, p.ref_len, " +
                    "p.stitched_ref_probe, p.stitched_ref_start_pos, p.stitched_probe_length";
        
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0].split(Pattern.quote("|"));;
                    
        DiffMatchPatch dmp = new DiffMatchPatch();
         LinkedList<DiffMatchPatch.Diff> diffs = null;
         int ct = 0;
         Long curr_pos = 0L;
         ArrayList<mt_var.variants.gfgVar> gvars= new ArrayList<mt_var.variants.gfgVar>();
         int increment = 0;
         mt_var.variants.gfgVar v = null;

         int ins_correction = 0;
         int ref_start = 0;
         int test_len = c[2].length();
         String ref_probe ="";
         String test_probe = null;

         try
         {
         //different setup for position and overlap probes
//         if(c[3].compareTo("NULL")!=0)
//         {
 
             
             
//        // Overlap probe setup
//        int orig_ref_start = Integer.parseInt(c[3]) - 1; // ✅ Adjust 1-based to 0-based
//        Long long_orig_ref_start = Long.parseLong(c[3]) - 1; // ✅ Adjust 1-based to 0-based
//        int ref_len = Integer.parseInt(c[5]);
//        String original_probe = c[4].replace("\"", ""); // ✅ Use original_probe directly

        
        
//        int ref_seq_len = mt_var.neo4jlib.neo4j_info.ref_seq.length();

        
        
        
        
  
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        
        
//        // overlap probes
//        if (orig_ref_start >= 0 && orig_ref_start + ref_len <= ref_seq_len) {
//            
//         // ✅ Stitched properties
//        String stitched_ref_probe = c[6].replace("\"", "");
//        Long stitched_probe_start = Long.parseLong(c[7]);
//        int stitched_probe_length = Integer.parseInt(c[8]);

        if (c[6].compareTo("NULL")!=0) {
           //overlap probes
            test_probe = c[4].replace("\"", "");  
            ref_probe = c[6].replace("\"", ""); ;
            curr_pos = Long.parseLong(c[7].replace("\"",""));
            ins_correction = 1;
            System.out.println("Using Stitched Ref Probe:\n" + ref_probe);
        }        
         
          // position probes   
         else
             {
                 //position; no adjustments for position
                test_probe = c[2].replace("\"", "");    //probe           
                 ref_probe = c[1].replace("\"","");  //ref_probe
                 curr_pos = Long.parseLong(c[0]);   //ref_start_pos
             }
         
            System.out.println(ref_probe + "\n\n" + test_probe);
            diffs = dmp.diffMain(ref_probe, test_probe);

         }
         catch(Exception e)
         {
             System.out.println("Error: 12: " + e.getMessage());
         }

         String sDiffs = "";
         
         for (int i=0; i<diffs.size()-1; i++)
         {
             sDiffs = sDiffs + diffs.get(i).operation + ": " + diffs.get(i).text.length() + ": " + diffs.get(i).text + " *** ";
             increment = 0;
            
             switch (diffs.get(i).operation)
             {
                 case EQUAL:
                    increment = diffs.get(i).text.length();
                     break;
                 
                 case INSERT:
                     try
                     {
                     if (diffs.get(i-1).operation.compareTo(DiffMatchPatch.Operation.DELETE)==0)
                     {
                         //alreay processed because when there are both INSERT and DELETE, then DELETE always precceeds the INSERT.
                         break;
                     }
                     }
                     catch(Exception e){break;}
                     
                     //insertion
                    v = new mt_var.variants.gfgVar();
                    v.pos = curr_pos -2 + ins_correction;
                    v.anc_nuc ="";
                    Long ins_pos = v.pos;
                    v.der= diffs.get(i).text;
                    v.name = ins_pos + ".1" + v.der;
                    v.classification = "ins";
                    v.method = 1;
                     gvars.add(v);
                     break;
                     
                 
                 case DELETE:
                     ct = 0;
                    v = new mt_var.variants.gfgVar();
                    v.der = "";
                     v.pos = curr_pos;
                      v.anc = diffs.get(i).text;  
                      
                      /////////////////////////////////////////////////////////////
                      if (diffs.get(i+1).operation.compareTo(DiffMatchPatch.Operation.EQUAL)==0)
                       { // simple deletion where there is no INSERT following the DELETE
                           //represent this in three possible ways
                           //1st: with all deleted nucleotides
                           v.pos = curr_pos;
                           v.name = v.anc + v.pos + "d";
                           v.classification =  "del";
                           v.method =15;
                           gvars.add(v);
                       }
                      
                      /////////////////////////////////////////////////////////////
                      //delete followed by insert is ether
                      //df = 0: simple substitution
                      //df = 1: der longer than anc -- insertional after substitutions
                      //df =-1: anc longer than der -- substituions followed by deletions
                      if (diffs.get(i+1).operation.compareTo(DiffMatchPatch.Operation.INSERT)==0)
                       { 
                           v.der = diffs.get(i+1).text;
                       }

                    int df = v.der.length() - v.anc.length();

                    switch(Integer.signum(df))
                       {
                        case 0:  //der and anc same length
                            if (diffs.get(i+1).operation.compareTo(DiffMatchPatch.Operation.INSERT)==0)
                            { 
                                int md = 2;  //single mutation in the region
                                if(v.anc.length()>1){
                                    md=9;   // multiple substitutions in this region
                                }
                                 for (int j=0;j<v.der.length(); j++)
                                {
                                    mt_var.variants.gfgVar nv = new mt_var.variants.gfgVar();
                                    nv.pos = curr_pos + j;
                                     nv.anc = v.anc.substring(j,j+1);
                                     nv.der = v.der.substring(j,j+1);
                                     nv.name = nv.anc + nv.pos  + nv.der;
                                     nv.classification = "sub";
                                     nv.method = md;
                                     if (nv.anc.compareTo(nv.der)!=0)
                                     {
                                         gvars.add(nv);
                                     }
                                }
                                break;
                            }
                            else
                          {     //deletion
                               v = new mt_var.variants.gfgVar();
                               v.pos = curr_pos;
                               v.der ="";
                               v.anc= diffs.get(i).text;
                               v.name = v.pos + v.der + "d";
                               v.classification = "del";
                               v.method = 3;
                               gvars.add(v);

                                }

                            break;
                        case 1:  //der longer than anc
                            //create substityions untl anc list ends
                            v.der = diffs.get(i+1).text;
                            for(int j=0; j < v.anc.length(); j++)
                            {
                               mt_var.variants.gfgVar nv = new mt_var.variants.gfgVar();
                               nv.anc = v.anc.substring(j,j+1);
                               nv.der = v.der.substring(j,j+1);
                               nv.pos = curr_pos + ct;
                               nv.name = nv.anc + nv.pos + nv.der;
                               nv.classification = "sub";
                               nv.method = 4;
                               if(nv.anc.compareTo(nv.der)!=0)
                               {
                                   gvars.add(nv);
                               }
                                ct = ct +1; 
                            }
                            //do not break
                            //add remaining v.anc as insertions
                            for (int j = v.anc.length(); j< v.der.length(); j++)
                            {
                               mt_var.variants.gfgVar nv = new mt_var.variants.gfgVar();
                             
 
                               nv.der = v.der.substring(j,j+1);
                               nv.anc = "";
                               nv.pos = curr_pos + ct;
                               nv.name = nv.pos + ".1" + nv.der;
                               nv.classification = "ins";
                               nv.method = 5;
                               gvars.add(nv);
                              ct = ct + 1;  
                                
                            }
                            
                            break;
                            
                        case -1:   //anc longer than del
                            for(int j=0; j < v.der.length(); j++)
                            {
                               mt_var.variants.gfgVar nv = new mt_var.variants.gfgVar();
                             
                               nv.anc = v.anc.substring(j,j+1);
                               nv.der = v.der.substring(j,j+1);
                               nv.pos = curr_pos + ct;
                               nv.name = nv.anc + nv.pos + nv.der;
                               nv.classification = "ins";
                               nv.method = 6;
                               if(nv.anc.compareTo(nv.der)!=0)
                               {
                                   gvars.add(nv);
                               }
                              ct = ct + 1;
   //do not break; continue to deletions
                            }
                            //add remaining v.anc as deletions
                            
//creeate arraylist to hold one iteration which is used to find boundaries of a deletion
                            ArrayList<mt_var.variants.gfgVar> ngg= new ArrayList<mt_var.variants.gfgVar>();
                            
                            for (int j = v.der.length(); j< v.anc.length(); j++)
                            {
                               mt_var.variants.gfgVar nv = new mt_var.variants.gfgVar();
                               nv.anc = v.anc.substring(j,j+1);
                               nv.der = "";
                               nv.pos = curr_pos + ct;
                               nv.name =  nv.anc + nv.pos + "d";
                               nv.classification = "del";
                               nv.method = 7;
                               gvars.add(nv);
                               ngg.add(nv);
                              ct = ct + 1; 
                                
                            }
                            
                           //String sd = specific_deletion(diffs.get(i).text, v.pos, pid, ngg);
                            String sd = deletion_pattern(diffs.get(i).text, v.pos, pid, ngg);
                           if (sd.compareTo("")!=0)
                           {
                                   mt_var.variants.gfgVar nv = new mt_var.variants.gfgVar();
                                   nv.pos = curr_pos;
                                   nv.der ="";
                                   nv.anc= v.anc;
                                   nv.name = sd;
                                   nv.classification = "del";
                                   if(sd.contains("*"))
                                   {
                                       nv.method=180;
                                   }
                                   else{
                                   nv.method=18;
                                   }
                                   gvars.add(nv);
                                
                           }
                            
                            break;
                        }  // end of deletion switch
                    increment = diffs.get(i).text.length();
             }  //end of top level switch
             
             //increment reference position        
             curr_pos = curr_pos +  increment;
             increment = 0;

         }   // end of diffs interation
    
         //prepare and return results
        String r = "";
        String vx = "";
        
        
    Map<String, Object> result = new HashMap<>();
    List<String> variantNames = new ArrayList<>();
    for (int i=0; i< gvars.size(); i++){
      variantNames.add(gvars.get(i).name );
    }

    result.put("dmp", sDiffs);
    result.put("variants", variantNames);

    
    System.out.println(result.get("variants"));
    return result;

        
        
    }
     
     
     
     private static String deletion_pattern(String del, Long pos, Long pid, ArrayList<mt_var.variants.gfgVar> ngg )
{
    if (ngg.size() > 2 && (ngg.get(ngg.size() - 1).pos - ngg.get(0).pos) > 2)
    {
        return ngg.get(0).pos + "-" + ngg.get(ngg.size() - 1).pos + "d";
    }
    return "";
}

   
}

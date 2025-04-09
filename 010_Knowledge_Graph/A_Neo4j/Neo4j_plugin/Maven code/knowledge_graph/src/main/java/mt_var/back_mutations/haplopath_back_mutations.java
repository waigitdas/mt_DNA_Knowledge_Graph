/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.back_mutations;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class haplopath_back_mutations {
    @UserFunction
    @Description("Optimize back mutations for graph traversals used in analytics")

    public String process_back_mutations(
        
  )
   
         { 
             
        fix_back_mutations();
         return "";
            }

    
    
    public static void main(String args[]) {
        fix_back_mutations();
    }
    
     public static String fix_back_mutations() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String cq="";
        
        
        //back mutation management involves several steps
        //A: annotate back mutation and their restoration
        //B: Remove back mutations at point they appear and, for path_variants, in all downstream path nodes
 
        ///////////////////////////////////////////////
        //Step A
        //1/ identify terminal branches of the haplotree; marks a end of line (this was done previously in the workflow).
        //2.  identify the haplotree start positions of each back mutation first appears and produces the ancestral state.  
        //3.  identify any haplotree position before the end of line where the mutation reappears.
        
            //A-2 create start_back_mutation property at the point in the path where it first appears
            //FTDNA represents them with an odd number of !
            cq = "MATCH p=(h: dnode) unwind h.branch_variants as x call { with x with x as z with z where size(z)-size(replace(z,'!','')) in [1,3,5,7] with z as wz return wz } with h, collect(wz) as ww set h.start_back_mutation=ww";
            mt_var.neo4jlib.neo4j_qry.qry_write(cq);
            //End Step A-2
            ///////////////////////////////////////////////////////////////
            
            //A-3: return point in path where mutation returns
            //FTDNA represents them with an even number of !
            mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(h: dnode) unwind h.branch_variants as x call { with x with x as z with z where size(z)-size(replace(z,'!','')) in [2,4,6] with z return z as wz } with h, collect(wz) as ww set h.start_restore_mutation=ww");
            //////////////////////////////////////////////////////////////
            //end of Step A-2
            ///////////////////////////////////////////////////////////////
            

            //////////////////////////////////////////////////////////////
            //Step B: remove back mutations from branch_variants and path_variants lists at pos in path where the back mutation appears
            //this does NOT remove the back mutation from the path_variants list in the downstream path
            //How FTDNA represents handles back mutations .... 
            // C16311T is represented as T16311C! 
            //Use regex to get pos and to split the variant name so start and  end nucleotides can be reversed
             //unwind the variant list, filer to continue only if they are NOT backmutation (!, !!!, or !!!!!), and collect those not filtered 
            mt_var.neo4jlib.neo4j_qry.qry_write("match(h: dnode) where h.start_back_mutation is not null with h unwind h.start_back_mutation as x call { with x with replace(trim(x), '!','') as y with y, apoc.text.regexGroups(y,'\\d+')[0][0] as pos with pos, split(y,pos) as ss with ss[1] + pos + ss[0] as w return w } with h,collect(w) as ww with h,ww, apoc.coll.subtract(h.branch_variants,ww) as tv, apoc.coll.subtract(h.path_variants,ww) as pv set h.branch_variants=tv, h.path_variants=pv");
            mt_var.neo4jlib.neo4j_qry.qry_write("match(h: dnode) where h.start_back_mutation is not null with h unwind h.start_back_mutation as x call { with x return trim(x) as w } with h,collect(w) as ww with h,ww, apoc.coll.subtract(h.branch_variants,ww) as tv, apoc.coll.subtract(h.path_variants,ww) as pv set h.branch_variants=tv, h.path_variants=pv");
            //End StepB
            
       
            
        //Step C:  iterate to remove back mutations from downstream paths    
        //Queue up data on back mutations
        cq = "MATCH (h: dnode) where h.start_back_mutation is not null with h unwind h.start_back_mutation as x call { with x,h return mt_var.back_mutations.haplopath_to_delete(h.name,x) as del_path } return h.name as hp,x,size(del_path)-size(replace(del_path,',',''))+1 as path_lenght,del_path order by hp "; 
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n");
        String cs[] = new String[4];
        
        //Remove the back mutations (cs[6]) from the block (cs[2]) where it appears
        //remove the  original mutations (cs[7]) from the back_mutation block (cs[2]) and any downstream blocks (cs[11]) 
        
        for (int i=0; i<c.length; i++)
                {
                    cs = c[i].split(Pattern.quote("|"));
                    String p[] = cs[3].replace("[","").replace("]","").replace("\"","").split(",");
                    
                    for (String p1 : p) {

                        remove_variant(cs[1].replace("\"",""),p1);
                    }
                }
//        End Step C

            ////////////////////////////////////////////////////////////
//            Step D:  fix double !! which represent return of a mutation
//            simply change by removing !! in branch_variants
//            this involves 2 step: inseting the variant and removing the variant notation with !!
     
            String cr[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str("MATCH (h: dnode) where h.start_restore_mutation is not null with h unwind h.start_restore_mutation as x call { with x,h return mt_var.back_mutations.haplopath_to_delete(h.name,x) as fix_path } return h.name as hp,x,size(fix_path)-size(replace(fix_path,',',''))+1 as path_lenght,fix_path order by h.op").split("\n");

  
//            ////////////////////////////////
//               //path_variants fix
               String q = "\'"; 
               String crs[] = cr[0].strip().split(Pattern.quote("|"));
               String hp = crs[0].replace("\"","").strip();
               String variant = crs[1].replace("\"","").strip();  //original value in terminal variant list to be removed
        
               Pattern pattern = Pattern.compile("([A-Z])(\\d+)([A-Za-z]*)");
               Matcher matcher2 = pattern.matcher(variant.replace("!",""));
               if (matcher2.matches()) {
                String der = matcher2.group(1).strip();        
                String pos = matcher2.group(2).strip();
                String anc = matcher2.group(3).strip();

             String vreplace = anc + pos + der;  //variant to replace the original
              cq= "match (h: dnode{name:" + q + hp + q + "}) with h, apoc.coll.subtract(h.path_variants,[" + q + variant.strip() + q +"]) as new_var set h.path_variants=new_var";
                mt_var.neo4jlib.neo4j_qry.qry_write(cq);
              cq= "match (h: dnode{name:" + q +hp + q + "}) with h, apoc.coll.insert(h.path_variants, 0, " + q + vreplace.strip() + q +") as new_var set h.path_variants=new_var";
                mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                              
//                       
               ///////////////////////////////////
               //query to fix restored variants. It adds the variant and removed the annotated variant with !!
               mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(h1: dnode{lvl:0})-[r: dnode_child*0..99]->(h2: dnode) where h2.start_restore_mutation is not null WITH h2, h2.name as dnode,  h2.start_restore_mutation as start_restore_mutation, h2.branch_variants as branch_variants, apoc.coll.subtract(h2.branch_variants,h2.start_restore_mutation) as new with h2, dnode, start_restore_mutation, branch_variants,new unwind h2.start_restore_mutation as x call { with x, new with apoc.coll.insert(new,0,replace(x,'!!','')) as new2 return new2 } set h2.branch_variants=new2");
                             //the path variant list needs to be update in the row in which it appears and all downstream rows
               
                            
                for (int i=0; i<cr.length; i++)
                {
                    crs = cr[i].split(Pattern.quote("|"));
                    String p[] = crs[3].strip().replace("[","").replace("]","").replace("\"","").split(",");
                   
                    for (String p1 : p) {
                        fix_restored_mutations(crs[1].strip().replace("\"",""),p1);
                    }
                }       
  
                }       
          //  end of Step D
            
     
        
//        Step E: re-sort lists so they are more readable to humans
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h: dnode) where size(h.branch_variants)>1 set h.branch_variants=mt_var.sorter.sort_variant_list_by_pos_toList(h.branch_variants)");
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (h: dnode) where size(h.path_variants)>1 set h.path_variants=mt_var.sorter.sort_variant_list_by_pos_toList(h.path_variants)");
        
       return "";
    }
     
     public static void remove_variant(String variant, String haplobranch)
     {
            String q = "\"";  //need "" because some dnode names have single quote
            String cq = ""; 
            Pattern pattern = Pattern.compile("([A-Z])(\\d+)([A-Za-z]*)");
            Matcher matcher2 = pattern.matcher(variant.replace("!",""));
            if (matcher2.matches()) {
                String der = matcher2.group(1).strip();        
                String pos = matcher2.group(2).strip();
                String anc = matcher2.group(3).strip();
             String v = anc + pos + der;  //variant.replace("[","").replace("]","");
                haplobranch = haplobranch.replace(q,"").strip();
                cq= "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.subtract(h.path_variants,[" + q + v.strip() + q + "]) as new_var set h.path_variants=new_var";
                mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                cq= "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.subtract(h.path_variants,[" + q + variant.strip() + q + "]) as new_var set h.path_variants=new_var";
                mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                try{  //catche error if a string=""
                    cq = "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.subtract(h.branch_variants,[" + q + v.strip() + q + "]) as new_var set h.branch_variants=new_var";
                    mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                }
                catch(Exception e){}
            
               try{  //catche error if a string=""
                    cq = "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.subtract(h.branch_variants,[" + q + variant.strip() + q + "]) as new_var set h.branch_variants=new_var";
                    mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                }
                catch(Exception e){}
            }            
     }
     
     
          public static void fix_restored_mutations(String variant, String haplobranch)
     {
            String q = "\"";  //need "" because some dnode names have single quote
            String cq = ""; 
               String v = variant.replace("!","").strip(); 
                haplobranch = haplobranch.replace(q,"").strip();
                cq= "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.subtract(h.path_variants,[" + q + variant.strip() + q +"]) as new_var set h.path_variants=new_var";
                mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                cq= "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.insert(h.path_variants,0, " + q + v.strip() + q + ") as new_var set h.path_variants=new_var";
                mt_var.neo4jlib.neo4j_qry.qry_write(cq);
     
                //process terminal variant only for 1st row containing the values to address
                try
                {
                cq = "MATCH (h: dnode {name:" + q + haplobranch + q + "}) WHERE apoc.coll.containsAll(h.branch_variants, [" + q + variant.strip() + q +"]) RETURN h.name as hp, h.branch_variants as var";
                String chk[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n");
                if (chk[0].length()>0)
                {
                    cq= "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.subtract(h.branch_variants,[" + q + variant.strip() + q +"]) as new_var set h.path_variants=new_var";
                    mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                    cq= "match (h: dnode{name:" + q + haplobranch + q + "}) with h, apoc.coll.insert(h.branch_variants,0, " + q + v.strip() + q + ") as new_var set h.path_variants=new_var";
                    mt_var.neo4jlib.neo4j_qry.qry_write(cq);
                }
     }
          catch(Exception e){}
 
     }
}

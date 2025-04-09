/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports.dispositive_triad;

import java.util.regex.Pattern;
import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class incongruent_variant_haplogroup_alignment {
    @UserFunction
    @Description("Finds anomalous haplogroup assignments.")

    public String incongruity_parent_child_variants(
      
  )
   
         { 
             
        show_anomalies();
         return "";
            }

    
    
    public static void main(String args[]) {
        show_anomalies();
    }
    
     public static String show_anomalies() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "incongruity_affecting_haplogroup_assignment_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        //get counts of sequences involved
        String cq1 = "MATCH p=()-[r:hg_parent_child]->(sc) with collect(distinct sc.name) as seqs RETURN size(seqs) as uniq_seq_ct";
        String uniq_child_seq = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq1).split("\n")[0].split(Pattern.quote("|"))[0];
        
        String cq2 = " MATCH path1=(sp:mt_seq)-[rhpc:hg_parent_child]->(sc:mt_seq) with path1,sp,rhpc, sc  match path2=(dp:dnode)-[r:dnode_child]->(dc:dnode) where dp.name=sp.assigned_hg and dc.name=sc.assigned_hg with path1,path2, sp,sc, dp, dc,  rhpc,apoc.coll.intersection(sp.all_variants,dc.branch_variants) as iv with path1,path2, sp,sc, dp, dc, rhpc,  iv where size(iv)>0 with apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct sc.name)))) as seqs return size(seqs) as unique_seq_ct";
        String uniq_misaligned_seqqs = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq2).split("\n")[0].split(Pattern.quote("|"))[0];
         
         // report detailed results
        mt_var.excelLib.excelRept.createExcel("MATCH path1=(sp:mt_seq)-[rhpc:hg_parent_child]->(sc:mt_seq) with path1,sp,rhpc, sc  match path2=(dp:dnode)-[r:dnode_child]->(dc:dnode) where dp.name=sp.assigned_hg and dc.name=sc.assigned_hg with path1,path2, sp,sc, dp, dc,  rhpc,apoc.coll.intersection(sp.all_variants,dc.branch_variants) as iv with path1,path2, sp,sc, dp, dc, rhpc,  iv where size(iv)>0 RETURN rhpc.hg_pair, sp.name, sp.assigned_hg, sc.name, sc.assigned_hg, size(iv),iv as incongruent_variant, rhpc.added_variants ", fn, "inconguent_variants", "", "", true, "UDF:\nmt_var.reports.dispositive_prooof.incongruity_parent_child_variants{}\n\nThe Cypher create the Dispositive Triad and the find cases where the sequence parent and child share a haplotree branch defining variant (column G) of the hapolgroup child.\nThat is, the branch defining variant is not a new variant for the chid sequence. as would be expected if thehaplotreewas aligned with the sequence data.\nColumnH shows new variants in th child sequence which are not used  as branch defining variants.", false);
               
               
               mt_var.excelLib.excelRept.createExcel("MATCH path1=(sp:mt_seq)-[rhpc:hg_parent_child]->(sc:mt_seq) with path1,sp,rhpc, sc  match path2=(dp:dnode)-[r:dnode_child]->(dc:dnode) where dp.name=sp.assigned_hg and dc.name=sc.assigned_hg with path1,path2, sp,sc, dp, dc,  rhpc,apoc.coll.intersection(sp.all_variants,dc.branch_variants) as iv with path1,path2, sp,sc, dp, dc, rhpc,  iv where size(iv)>0 with  rhpc.hg_pair as hg_pair, count(*) as seq_pair_ct,  size(iv) as var_ct, iv as incongruent_variants  return hg_pair, seq_pair_ct, var_ct, incongruent_variants", fn, "seq_hg_pairs", "1;1;", "1:###,###;2:###,###", false, "This Cypher aggregate the prior worksheet data by the sequence pairs sharing the same haplogroup parent child relationship.\nThe total number of these pairs (sum at bottom of Column B) is the same as the number of rows in the prior worksheet\n\nTwo additional Cypher queries return the unique child sequences in the Dispositive Triads and the number who show the misalignment.\n\nThe total number of child sequences in the Dispositive Triads is \n" + uniq_child_seq  + " as shown wit this Cypher:\n" + cq1 + "\n\nThe number of child sequences who share adefining varient with theparent sequence is:\n " +  uniq_misaligned_seqqs + "\nwhich is the value returned by this Cypher:\n" + cq2, true);
            
        return "";
    }
}

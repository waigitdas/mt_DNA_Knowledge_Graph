/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports;

import java.util.regex.Pattern;
import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class traversal_variants_for_sequence {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String seq_traversal(
        @Name("seq") 
            String seq
  )
   
         { 
             
        get_traversal(seq);
        return "";
            }

    
    
    public static void main(String args[]) {
//        get_traversal("OP682672");
//        get_traversal("MF621126");
//        get_traversal("EF184583");
        get_traversal("ON597788");
//        get_traversal("MZ920270");
    }
    
     public static String get_traversal(String seq) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String fn = seq + "_traversal_path_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cqx = "MATCH (s:mt_seq{name:'" + seq + "'}) RETURN s.name as seq, s.assigned_hg as assigned_hg, s.missed_var_ct as missed_var_ct, s.hg_missed_var as hg_missed_var order by s.name";
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cqx).split("\n")[0].split(Pattern.quote("|"));
       
        
              mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq{name:'" + seq + "'}) with s match path=(dp:dnode{name:'RSRS'})-[r:dnode_child*0..25]->(dc:dnode{name:s.assigned_hg})  with s, [x in nodes(path)|x.name] as path_dnode unwind path_dnode as y call { with y, s match (dy:dnode{name:y}) with dy, case when size(dy.branch_variants)>0 then apoc.coll.subtract(dy.branch_variants,s.all_variants) else '**' end  as missed_var , case when size(dy.path_variants)>0 then apoc.coll.subtract(dy.path_variants,s.all_variants) else '**' end  as missed_path_var  return dy, missed_var, missed_path_var }  RETURN s.name, dy.name, dy.start_back_mutation, missed_var as missed_branch_var , missed_path_var", fn, "traversal", "", "", true, "UDF:\nreturn mt_val.reports.seq_traversal('" + seq + "')\n\nKnowledge graph traversal for sequence " + seq + " from the root RSRS (Eve) to the assigned haplogroup (" + c[1] + ").\n\nThere are " + c[2] + " missed variants in this traversal: " + c[3] + "\n\nThis Cypher query gives the final results:\n" + cqx + "\n\nThis report illustrates the ability of the knowledge graph to bring together many facets of the graph:\nthe sequence and its assigned haplogroup, the dnode tree traversal, the set algebra using two variant lists, and \nhow back mutation provenance hlps tracks variant chanes along the traversal path.\n\nThe variant sets used are properties of the dnode and the sequence nodes. Their values were computed during construction of the knwledge graph.", true);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false "message.", true;
        
        
        return "";
    }
}

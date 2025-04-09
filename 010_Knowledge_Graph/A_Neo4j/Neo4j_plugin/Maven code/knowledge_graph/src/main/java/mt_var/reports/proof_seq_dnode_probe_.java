/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class proof_seq_dnode_probe_ {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String Do_probe_seq_hg_align(
  )
   
         { 
             
        get_associations();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_associations();
    }
    
     public static String get_associations() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "specificity_ambiguity_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        
        String cq = "match path1=(s:mt_seq)-[sp:seq_probe]-(p:probe) where p.subsumed_by is null and p.variants is not null with path1, s,sp, p match path2=(p)-[pd:probe_dnode]->(d:dnode) with path1, path2, s, p,pd, sp,d, apoc.coll.intersection(s.all_variants,d.path_variants) as iv where size(iv)>0 return s.name as seq, s.assigned_hg as assigned_hg, p.pid as pid, d.name as dnode,case when s.assigned_hg=d.name then 1 else 0 end as same_hgs, size(p.variants), apoc.coll.intersection(p.variants, s.all_variants) as shared_in_seq, apoc.coll.intersection(p.variants, d.path_variants) as pshared_in_probe, apoc.coll.subtract(apoc.coll.intersection(p.variants, s.all_variants), p.path_variants) as availables_for_hg, size(iv) as shared_seq_probe_var_ct, id(sp), id(pd)  order by s.name, same_hgs";
        
        mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited(cq,  fn);
        
               //mt_var.excelLib.excelRept.createExcel("match path1=(s:mt_seq)-[sp:seq_probe]-(p:probe) where p.subsumed_by is null and p.variants is not null with path1, s,sp, p match path2=(p)-[pd:probe_dnode]->(d:dnode) with path1, path2, s, p,pd, sp,d, apoc.coll.intersection(s.all_variants,d.path_variants) as iv where size(iv)>0 return s.name as seq, s.assigned_hg as assigned_hg, p.pid as pid, d.name as dnode,case when s.assigned_hg=d.name then 1 else 0 end as same_hgs, size(p.variants), apoc.coll.intersection(p.variants, s.all_variants) as shared_in_seq, apoc.coll.intersection(p.variants, d.path_variants) as pshared_in_probe, apoc.coll.subtract(apoc.coll.intersection(p.variants, s.all_variants), p.path_variants) as availables_for_hg, size(iv) as shared_seq_probe_var_ct, id(sp), id(pd)  order by s.name, same_hgs", fn, "probe_var_hg_detail", "", "", true, "message.", true);
        
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
       
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false "message.", true;
        

        return "";
    }
}

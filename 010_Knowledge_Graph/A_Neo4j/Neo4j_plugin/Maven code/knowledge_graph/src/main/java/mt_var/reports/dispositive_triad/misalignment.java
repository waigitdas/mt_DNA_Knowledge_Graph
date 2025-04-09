/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports.dispositive_triad;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class misalignment {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String misaligned_haplogroup_varints(
       
  )
   
         { 
             
        get_misaligned();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_misaligned();
    }
    
     public static String get_misaligned() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "misaligned_haplogroup_required_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
               mt_var.excelLib.excelRept.createExcel("MATCH path1=(s:mt_seq)-[sp:seq_probe]-(p:probe) WHERE p.subsumed_by IS NULL   AND p.variants IS NOT NULL   AND 'G103A' IN p.variants   WITH path1, s, sp, p MATCH path2=(p)-[pd:probe_dnode]->(d:dnode) WITH path1, path2, s, p, pd, sp, d, apoc.coll.intersection(s.all_variants, d.path_variants) AS iv WHERE size(iv) > 0 RETURN s.name AS seq, s.assigned_hg AS assigned_hg, p.pid AS pid, d.name AS dnode, CASE WHEN s.assigned_hg = d.name THEN 1 ELSE 0 END AS same_hgs, size(p.variants) AS probe_variant_ct, apoc.coll.intersection(p.variants, s.all_variants) AS shared_in_seq,  apoc.coll.intersection(p.variants, d.path_variants) AS shared_in_probe, apoc.coll.subtract(apoc.coll.intersection(p.variants, s.all_variants), p.path_variants) AS available_for_hg, size(iv) AS shared_seq_probe_var_ct, id(sp) AS sp_id, id(pd) AS pd_id ORDER BY s.name, same_hgs", fn, "G103A", "", "", true, "message.", true);
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

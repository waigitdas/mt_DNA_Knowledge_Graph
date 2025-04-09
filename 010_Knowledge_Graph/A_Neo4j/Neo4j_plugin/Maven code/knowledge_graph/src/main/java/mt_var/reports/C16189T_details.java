/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.reports;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
//import mt_var.excelLib.*;

public class C16189T_details {
    @UserFunction
    @Description("Template used in creating new reports.")

    public String C16189T_report(
 
  )
   
         { 
             
        get_rept();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_rept();
    }
    
     public static String get_rept() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String fn = "C16189T_report_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        String cq_alt = "MATCH (p:probe)  where apoc.coll.containsAll(p.variants,['T16187d','16188.1T']) with p,p.variants as variants, mt_var.probes.dmp_two_probes(p.ref_probe, p.probe) as dmp  return p.pid as probe_id, p.ref_start_pos as ref_start_pos,  p.seq_ct as seq_ct, case when dmp<>replace(dmp,'DELETE: T  *  EQUAL: CC  *  INSERT: T', '') then 1 else 0 end as dmp_with_expected, size(variants) as var_ct, variants, dmp order by p.ref_start_pos"; 
        
       mt_var.excelLib.excelRept.createExcel("MATCH (p:probe)  where apoc.coll.containsAll(p.variants,['T16187d','16188.1T']) with p,p.variants as variants, mt_var.variants.compute_probe_variants(p.pid) AS dmp   return p.pid as probe_id, p.ref_start_pos as ref_start_pos, p.type,   p.seq_ct as seq_ct, case when size(apoc.coll.intersection(dmp.variants, ['T16187d','16188.1T']))=2 then 1 else 0 end as dmp_with_expected, size(variants) as var_ct, variants, dmp.dmp as dmp order by p.ref_start_pos", fn, "dmp_detail", "3;4;5", "", true, "A major difference in this project and current methods is the annotation of T16187d and 16188.1T  as C16189T.\nThe query finds over 6,000 probes with T16187d and 16188.1T and then checks the diff-match-patch pattern and verified that the annotation is ccorrect (column G).\nThe characteristic pattern in the DMP is used to determine whether it is present (Col H).\nThe fidelity of the assignments also indicated the precisin of the variant annotation which is very consistent in many different combinations of adjacent varients.\n\nNotice that all the probes are position probes. This indicates they were specific in the first probe creation step and did not need to be striched into overlap probes to achieve specificity.\n\nBecause they are all position probes, this alternative Cypher will produce the same results\n " + cq_alt, true);
         
        return "";
    }
}

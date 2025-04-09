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

public class nucleotide_repeats {
    @UserFunction
    @Description("report on sequences repeated in all study sequences..")

    public String repeat_mutations_at_many_locations(
 
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
        
        String fn = "repeat_sequence_locations_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) with s.name as seq, s.assigned_hg as hg, mt_var.probes.find_seq_positions(s.name,'CCCCCTCTA') as repeat_positions WHERE repeat_positions <> '' return seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 as repeat_ct,  repeat_positions  order by repeat_ct desc, hg, seq", fn, "9-base", "2", "2:###,###", true, "The 9-base sequence generally reported as deleted a positions 8281-8289 is found at other positions in mt-DNA sequebes.\nThe positions reported are those in the test sequence, not adjusted for frameshifts.", false);
        
       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) WITH s.name AS seq, s.assigned_hg AS hg, mt_var.probes.find_seq_positions(s.name,'ATCTCCCCC') AS repeat_positions WHERE repeat_positions <> '' RETURN seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 AS repeat_ct, repeat_positions ORDER BY repeat_ct DESC, hg, seq", fn, "9-base inversion", "2", "2:###,###", false, "", false);
        
       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) WITH s.name AS seq, s.assigned_hg AS hg, mt_var.probes.find_seq_positions(s.name,'GGGGGAGAT') AS repeat_positions WHERE repeat_positions <> '' RETURN seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 AS repeat_ct, repeat_positions ORDER BY repeat_ct DESC, hg, seq", fn, "9-base_complement", "2", "2:###,###", false, "", false);
        
//       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) with s.name as seq, s.assigned_hg as hg, mt_var.probes.find_seq_positions(s.name,'CCCCCTCTA') as repeat_positions  return seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 as repeat_ct,  repeat_positions  order by repeat_ct desc, hg, seq", fn, "9-base", "2", "2:###,###", false, "The 9-base sequence generally reported as deleted a positions 8281-8289 is found at other positions in mt-DNA sequebes.\nThe positions reported are those in the test sequence, not adjusted for frameshifts.", false);
//        
//       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) with s.name as seq, s.assigned_hg as hg, mt_var.probes.find_seq_positions(s.name,'CCCCCTCTA') as repeat_positions  return seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 as repeat_ct,  repeat_positions  order by repeat_ct desc, hg, seq", fn, "9-base", "2", "2:###,###", false, "The 9-base sequence generally reported as deleted a positions 8281-8289 is found at other positions in mt-DNA sequebes.\nThe positions reported are those in the test sequence, not adjusted for frameshifts.", false);
//        


       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) WITH s.name AS seq, s.assigned_hg AS hg, mt_var.probes.find_seq_positions(s.name,'ACGAGG') AS repeat_positions WHERE repeat_positions <> '' RETURN seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 AS repeat_ct, repeat_positions ORDER BY repeat_ct DESC, hg, seq", fn, "ACGAGG", "", "", false, "The 9-base sequence generally reported as deleted a positions 8281-8289 has a inversion also found at several positions in mt-DNA sequebes.\nThe positions reported are those in the test sequence, not adjusted for frameshifts.", false);
        
       mt_var.excelLib.excelRept.createExcel("MATCH (s:mt_seq) WITH s.name AS seq, s.assigned_hg AS hg, mt_var.probes.find_seq_positions(s.name,'GGAGCA') AS repeat_positions WHERE repeat_positions <> '' RETURN seq, hg, size(repeat_positions)-size(replace(repeat_positions,',',''))+1 AS repeat_ct, repeat_positions ORDER BY repeat_ct DESC, hg, seq", fn, "105-111d sequence", "", "", false, "The 6-base sequence generally reported as deleted a positions 106-1111 is found at other positions in mt-DNA sequebes.\nThe positions reported are those in the test sequence, not adjusted for frameshifts.", true);
        
//       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", true, "message.", false);
//        
//       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", true, "message.", false);
//        
//       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", true, "message.", false);
//        mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", true, "message.", false);
//        
//       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", true, "message.", true);
//        
        
        return "";
    }
}

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

public class dnode_probe_statistics {
    @UserFunction
    @Description("report on probe subgroup: haplopath probes (haploprobes).")

    public String dnode_probes_report(
 
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
        
        String fn = "dnode_probe_statistics_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
       mt_var.excelLib.excelRept.createExcel("match (hm:dnode) with hm optional match (p:probe)-[r:probe_dnode]->(h:dnode{name:hm.name}) where  p.subsumed_by is null with hm, p,h with hm, collect(p.pid) as pids  with hm, pids with  hm, size(pids) as ct with hm, ct,  sum(case when ct=0 then 0 else 1 end) as has_probe with  hm.name as dnode, hm.lvl as lvl,  has_probe, ct as probe_ct, hm return apoc.text.repeat('...', hm.lvl) + dnode, lvl, hm.seq_ct as seq_ct,  has_probe,  probe_ct  order by hm.op", fn, "Hapolpath_probe_counts", "2;3;4;5;6", "2:0;3:#0;4:0;5:###,###;6:###,###;7:##.##%", true, "Most haplogroups have probes (column E). \nThe dataset used does not include Genbank sequences for every haplogroup. Thus, probes could not be mapped to these haplotree  branches.\nThe many be multiple haploprobes  (column F) and probes (Column G) for a haplotree branch", true);
        
      
        
    
        return "";
    }
}

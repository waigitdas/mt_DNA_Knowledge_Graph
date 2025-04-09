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

public class haplopath_probes_and_variants {
    @UserFunction
    @Description("report about dnode variants.")

    public String haplopath_probes(
 
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
        
        String fn = "haplopath_probes_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
       mt_var.excelLib.excelRept.createExcel("MATCH (p)-[r:probe_dnode]->(h) where p.subsumed_by is null and p.ref_start_pos>15 with p,collect(h.name) as hgs, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct h.branch_variants)))) as pv, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct p.variants)))) as gv with p, hgs, pv, gv where size(hgs)=1 with hgs, collect(distinct p.pid) as pids, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct pv)))) as ppv, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect(distinct gv)))) as ggv with hgs, pids, ppv ,ggv, apoc.coll.intersection(ppv,ggv) as both match(hx: dnode{name:hgs[0]}) RETURN apoc.text.repeat('...',hx.lvl) + hx.name as haplopathp, hx.lvl as lvl, hx.seq_ct as seq_ct, size(pids) as probe_ct, size(ppv) as hp_var_ct, size(ggv) as graph_var_ct, size(both) as var_both_ct, pids as block_probe_ids, ppv as dnode_variants, ggv as graph_variants, both as variants_in_both_lists order by hx.op", fn, "dnode with probes variants", "2;3;4;5;6", "2:###,###;3:###,###;4:###,###;5:###,###;6:0", true, "This query find dnode defining probes that are specific for the haplogroup, mapping to only one haplogroup.\nVariants in column I are those defining a branch in the current rendering of the haplotree\nVariants in column J are those aggregated from all the probe variants and they are NOT unique to a dnode nor are they the same as variants in column H.\nThe graph intersection shown in column K is the variaiats shared by the two lists (cols I and J)\n\nThese observations demonstrate that the probes are more specific than the variants because they distinguish the patterns of adjacent mutations.", true);
        
        
        return "";
    }
}

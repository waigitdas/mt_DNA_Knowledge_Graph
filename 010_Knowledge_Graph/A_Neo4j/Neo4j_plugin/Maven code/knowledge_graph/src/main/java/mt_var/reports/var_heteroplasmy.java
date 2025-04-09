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


public class var_heteroplasmy {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String variant_heterplasmy(
     
  )
   
         { 
             
        get_heteroplasmy();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_heteroplasmy();
    }
    
     public static String get_heteroplasmy() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String fn = "heteroplasmy_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        mt_var.excelLib.excelRept.createExcel("MATCH (v:variant) where not v.der in ['A','C','G','T', ''] and v.name=replace(v.name,'.','') RETURN v.name as variant, v.pos as pos, v.der as der,  v.probe_ct as probe_ct order by v.pos", fn, "variants", "", "", true, "message   ",   false);
         
         mt_var.excelLib.excelRept.createExcel("MATCH (p:probe)-[r:probe_variant]->(v:variant) where not v.der in ['A','C','G','T', ''] and v.name=replace(v.name,'.','') and p.subsumed_by is null and p.ref_start_pos>15 RETURN v.name as variant, v.pos as pos,p.pid as probe_id, p.ref_start_pos as ref_start_pos, v.der as var_der, p.variants as probe_variants,  v.probe_ct as var_probe_ct order by v.pos", fn, "probes", "", "", false, "message", false);
         mt_var.excelLib.excelRept.createExcel("MATCH (d:dnode)-[r:dnode_variant]->(v:variant) where not v.der in ['A','C','G','T', ''] and v.name=replace(v.name,'.','') RETURN v.name as variant, d.name as haplogroup, v.pos as pos, v.der as der,  v.probe_ct as probe_ct order by v.pos", fn, "haplogroups", "", "", false, "message", false);
         mt_var.excelLib.excelRept.createExcel("MATCH (p)-[r:probe_variant]->(v) RETURN p.type, case when p.subsumed_by is not null then 1 else 0 end as sb, v.source, r.source, count(*)", fn, "vprobe_type_variant_source", "", "", false, "message", true);
//         mt_var.excelLib.excelRept.createExcel(fn, fn, "variants", "", "", true, "message", false);
//         mt_var.excelLib.excelRept.createExcel(fn, fn, "variants", "", "", true, "message", false);
//         mt_var.excelLib.excelRept.createExcel(fn, fn, "variants", "", "", true, "message", false);
//         mt_var.excelLib.excelRept.createExcel(fn, fn, "variants", "", "", true, "message", false);
 
        
        
        return "";
    }
}

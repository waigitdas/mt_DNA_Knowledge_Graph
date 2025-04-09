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


public class region_stability_fused_probes {
    @UserFunction
    @Description("Template used in creating new functions.")

      public String assess_fused_probe_persistence(
  )
   
         { 
        get_rates();
         return "";
            }

    
    
    public static void main(String args[]) {
        get_rates();
    }
    
     public static String get_rates() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String fn = "region_mutation_rates_fused_probes_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String cq = "";
        
        
       mt_var.excelLib.excelRept.createExcel("UNWIND [  {region: 'HVR2', min_pos: 10, max_pos: 574}, {region: 'CR', min_pos: 575, max_pos: 16000},   {region: 'HVR1', min_pos: 16017, max_pos: 16545} ] AS r WITH r.region AS region, r.min_pos AS min_pos, r.max_pos AS max_pos,      (r.max_pos - r.min_pos + 1) AS region_length OPTIONAL MATCH (v:variant) WHERE v.pos >= min_pos AND v.pos <= max_pos WITH region, min_pos, max_pos, region_length, count(v) AS ct RETURN region, min_pos, max_pos, region_length, ct,        round(1.0 * ct / region_length, 6) AS rate_per_base ORDER BY min_pos", fn, "variants", "4;4", "4:###,###;4:###,###", true, "This worksheet shows mutation rates based on detected variants across mtDNA regions. Mutation rates are higher in hypervariable regions (HVR1, HVR2) and lower in coding regions, indicating functional constraint.", false);

               mt_var.excelLib.excelRept.createExcel("UNWIND [  {region: 'HVR2', min_pos: 10, max_pos: 574},   {region: 'CR', min_pos: 575, max_pos: 16000},   {region: 'HVR1', min_pos: 16017, max_pos: 16545} ] AS r WITH r.region AS region, r.min_pos AS min_pos, r.max_pos AS max_pos, (r.max_pos - r.min_pos + 1) AS region_length OPTIONAL MATCH (p:fused_probe) WHERE p.end_pos >= min_pos AND p.start_pos <= max_pos WITH region, min_pos, max_pos, region_length, count(p) AS ct RETURN region, min_pos, max_pos, region_length, ct as fused_probe_ct,  round(1.0 * ct / region_length, 6) AS probes_per_base ORDER BY min_pos", fn, "fused_probes", "4;4", "4:###,###;4:###,###", false, "This worksheet shows mutation rates based on detected fused_probes. Higher fused_probe mutation rates, especially in hypervariable regions, reflect increased sensitivity and relaxed selection..", false);
        
               mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe) WHERE p.region IS NOT NULL AND p.subsumed_by IS NULL AND size(p.variants) > 0 WITH p.region AS region, toFloat(size(p.variants)) AS variants_per_fused_probe WITH region, count(*) AS probe_count, round(avg(variants_per_fused_probe), 3) AS mean_variants_per_fused_probe, round(stdev(variants_per_fused_probe), 3) AS sd_variants_per_fused_probe RETURN region, probe_count, mean_variants_per_fused_probe, sd_variants_per_fused_probe ORDER BY region", fn, "variants_per_fused_probe", "", "", false, "This worksheet summarizes variants per fused_probe by region. Higher variance in hypervariable regions reflects relaxed selection, while lower variance in coding regions reflects functional constraint.", false);

               mt_var.excelLib.excelRept.createExcel("MATCH (p:fused_probe) WHERE p.region IS NOT NULL AND p.subsumed_by IS NULL AND size(p.variants) > 0 WITH p.region AS region, toFloat(size(p.variants)) AS variants_per_fused_probe WITH region, count(*) AS N, avg(variants_per_fused_probe) AS mean_variants_per_fused_probe, stdev(variants_per_fused_probe) AS sd_variants_per_fused_probe WITH collect({ region: region, N: N, mean: mean_variants_per_fused_probe, variance: sd_variants_per_fused_probe * sd_variants_per_fused_probe }) AS region_stats UNWIND region_stats AS r WITH sum(r.N) AS total_N, sum(r.mean * toFloat(r.N)) / sum(r.N) AS overall_mean, collect(r) AS region_stats UNWIND region_stats AS r WITH total_N, overall_mean, sum((r.mean - overall_mean) * (r.mean - overall_mean) * r.N) AS SSB, sum(r.N * r.variance) AS SSW, size(region_stats) AS k WITH CASE WHEN k > 1 THEN SSB / toFloat(k - 1) ELSE null END AS MSB, CASE WHEN total_N - k > 0 THEN SSW / toFloat(total_N - k) ELSE null END AS MSW, CASE WHEN SSB > 0 AND SSW > 0 THEN (SSB / toFloat(k - 1)) / (SSW / toFloat(total_N - k)) ELSE null END AS F_statistic, total_N, k RETURN MSB, MSW, F_statistic, total_N AS N, k - 1 AS df_between, total_N - k AS df_within", fn, "ANOVA", "", "", false, "This worksheet shows ANOVA results for mutation rate differences between regions. High F-statistic and low MSW confirm that the differences are statistically significant.", true);

        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false, "message.", false);
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", false "message.", true;
        

        return "";
    }
}

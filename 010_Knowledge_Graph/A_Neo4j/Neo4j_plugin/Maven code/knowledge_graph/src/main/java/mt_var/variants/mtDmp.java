/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class mtDmp {
    private List<gfgVar> variants;
    private String dmpSummary;

    public mtDmp() {
        variants = new ArrayList<>();
    }

    // Add a variant to the list
    public void addVariant(gfgVar variant) {
        variants.add(variant);
    }

    // Get the list of variants
    public List<gfgVar> getVariants() {
        return variants;
    }

    // Get the DMP summary
    public String getDmpSummary() {
        return dmpSummary;
    }

    // Set the DMP summary
    public void setDmpSummary(String dmpSummary) {
        this.dmpSummary = dmpSummary;
    }

    // Convert mtDmp to a map for Neo4j compatibility
    public Map<String, Object> toMap() {
        List<Map<String, Object>> variantMaps = new ArrayList<>();
        
        for (gfgVar variant : variants) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", variant.get_name());
            map.put("pos", variant.get_pos());
            map.put("anc", variant.get_anc());
            map.put("der", variant.get_der());
            map.put("classification", variant.get_classification());
            map.put("method", variant.get_method());
            map.put("dmp", variant.get_dmp());
            variantMaps.add(map);
        }

        // Create the map structure for Neo4j return
        Map<String, Object> result = new HashMap<>();
        result.put("variants", variantMaps);
        result.put("dmp_summary", dmpSummary);

        // Debugging info (optional)
        System.out.println("Returning " + variantMaps.size() + " variants");

        return result;
    }
}

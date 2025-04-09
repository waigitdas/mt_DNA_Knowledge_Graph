/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.seq_pairs;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class seq_pair_variant_lists {
    @UserFunction
    @Description("Creates hg_parent_child relationship with properties comparing sequences in haplotree parent->child relationship.")

    public String dropped_variants_during_traversals() { 
        get_new_bm();
        return "";
    }

    public static void main(String args[]) {
        get_new_bm();
    }

    public static String get_new_bm() {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
///////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////   Write pipe-delimited file  ////////////////////////////////////////////
        // Write file with sequence pair data for subsequent loading
///////////////////////////////////////////////////////////////////////////////////////////////

String fnw = "variants_dropped_in_traversal_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        mt_var.neo4jlib.neo4j_qry.qry_write("CALL apoc.export.csv.query(\"WITH ['child_hg', 'parent_hg', 'child_seq', 'parent_seq', 'dropped_count', 'dropped_path_variants'] AS header RETURN apoc.text.join(header, '|') AS row UNION ALL MATCH (child:mt_seq)-[:seq_dnode]->(dc:dnode) MATCH (parent:mt_seq)-[:seq_dnode]->(dp:dnode)<-[:dnode_child]-(dc) WITH dp, dc, parent, child, apoc.coll.intersection(parent.all_variants, dp.path_variants) AS parent_relevant_variants, apoc.coll.intersection(child.all_variants, dc.path_variants) AS child_relevant_variants WITH dp, dc, parent, child, apoc.coll.subtract(parent_relevant_variants, child_relevant_variants) AS dropped_path_variants WITH dp, dc, parent, child, apoc.coll.subtract(dropped_path_variants, parent.start_back_mutation) AS corrected_dropped_path_variants WHERE size(corrected_dropped_path_variants) > 0 RETURN apoc.text.join([dp.name, dc.name, parent.name, child.name, toString(size(corrected_dropped_path_variants)), apoc.text.join(corrected_dropped_path_variants, ',')], '|') AS row\", 'file:///" + fnw + "', {batchSize: 1000, retries: 25, quotes: false})");

        
        //dot limited to path_variants
        String fnall= "variants_all_dropped_in_traversal_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        mt_var.neo4jlib.neo4j_qry.qry_write("CALL apoc.export.csv.query(\"WITH ['child_hg', 'parent_hg', 'child_seq', 'parent_seq', 'all_dropped_count', 'alll_dropped_path_variants'] AS header RETURN apoc.text.join(header, '|') AS row UNION ALL MATCH (child:mt_seq)-[:seq_dnode]->(dc:dnode) MATCH (parent:mt_seq)-[:seq_dnode]->(dp:dnode)<-[:dnode_child]-(dc) WITH dp, dc, parent, child,parent.all_variants AS parent_relevant_variants, child.all_variants AS child_relevant_variants WITH dp, dc, parent, child, apoc.coll.subtract(parent_relevant_variants, child_relevant_variants) AS all_dropped_path_variants WITH dp, dc, parent, child, apoc.coll.subtract(all_dropped_path_variants, parent.start_back_mutation) AS corrected_all_dropped_path_variants WHERE size(corrected_all_dropped_path_variants) > 0 RETURN apoc.text.join([dp.name, dc.name, parent.name, child.name, toString(size(corrected_all_dropped_path_variants)), apoc.text.join(corrected_all_dropped_path_variants, ',')], '|') AS row\", 'file:///" + fnall + "', {batchSize: 1000, retries: 25, quotes: false})");
       
        
               // Write file with sequence pair data for subsequent loading
        String fnwadd = "variants_added_in_traversal_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        mt_var.neo4jlib.neo4j_qry.qry_write("CALL apoc.export.csv.query(\"WITH ['child_hg', 'parent_hg', 'child_seq', 'parent_seq', 'missed_added_count', 'missed_added_path_variants'] AS header RETURN apoc.text.join(header, '|') AS row UNION ALL MATCH (child:mt_seq)-[:seq_dnode]->(dc:dnode) MATCH (parent:mt_seq)-[:seq_dnode]->(dp:dnode)<-[:dnode_child]-(dc) WITH dp, dc, parent, child, apoc.coll.intersection(parent.all_variants, dp.path_variants) AS parent_relevant_variants, apoc.coll.intersection(child.all_variants, dc.path_variants) AS child_relevant_variants WITH dp, dc, parent, child, apoc.coll.subtract(child_relevant_variants, parent_relevant_variants) AS added_path_variants WITH dp, dc, parent, child, apoc.coll.subtract(added_path_variants, parent.start_back_mutation) AS filtered_added_path_variants WITH dp, dc, parent, child, apoc.coll.subtract(filtered_added_path_variants, child.branch_variants) AS corrected_missed_added_path_variants WHERE size(corrected_missed_added_path_variants) > 0 RETURN apoc.text.join([dp.name, dc.name, parent.name, child.name, toString(size(corrected_missed_added_path_variants)), apoc.text.join(corrected_missed_added_path_variants, ',')], '|') AS row\", 'file:///" + fnwadd + "', {batchSize: 1000, retries: 25, quotes: false})");
//
        
//        
//        // Write file with sequence pair data for subsequent loading
//        String fnwd = "dmp_parent_child_seq_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
//        mt_var.neo4jlib.neo4j_qry.qry_write("CALL apoc.export.csv.query(\"WITH ['child_hg', 'parent_hg', 'child_seq', 'parent_seq', 'dmp_diff'] AS header RETURN apoc.text.join(header, '|') AS row UNION ALL MATCH (child:mt_seq)-[:seq_dnode]->(dc:dnode) MATCH (parent:mt_seq)-[:seq_dnode]->(dp:dnode)<-[:dnode_child]-(dc) WITH dp, dc, parent, child, mt_var.probes.dmp_two_probes(parent.fullSeq, child.fullSeq) AS dmp_diff RETURN apoc.text.join([dp.name, dc.name, parent.name, child.name, dmp_diff], '|') AS row\", 'file:///" + fnwd + "',  {batchSize: 1000, retries: 25, quotes: false})");


// Add `hg_pair` property for easier querying
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (parent)-[r:hg_parent_child]->(child) WITH r, parent.assigned_hg + '_' + child.assigned_hg AS hg_pair SET r.hg_pair = hg_pair");
        mt_var.neo4jlib.neo4j_qry.CreateRelationshipIndex("hg_parent_child","hg_pair");
        
///////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////   Load pipe-delimited files  ////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////


  mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV FROM 'file:///" + fnw + "' AS raw_line FIELDTERMINATOR '|' WITH raw_line SKIP 1 WITH raw_line[2] AS child_seq,      raw_line[3] AS parent_seq,      toInteger(raw_line[4]) AS dropped_count, split(raw_line[5], ',') AS dropped_path_variants_list WITH child_seq, parent_seq, dropped_count, [variant IN dropped_path_variants_list | {original: variant, position: CASE WHEN variant =~ \"^(\\\\d+).*\"   THEN toInteger(apoc.text.regreplace(variant, \"^(\\\\d+).*\", \"$1\")) ELSE 0  END }] AS variants_with_positions WITH child_seq, parent_seq, dropped_count, apoc.coll.sortMaps(variants_with_positions, 'position') AS sorted_variants_with_positions WITH child_seq, parent_seq, dropped_count, [x IN sorted_variants_with_positions | x.original] AS sorted_variants MATCH (parent:mt_seq {name: parent_seq}) MATCH (child:mt_seq {name: child_seq}) MERGE (parent)-[r:hg_parent_child]->(child) SET r.dropped_count = dropped_count, r.dropped_path_variants = sorted_variants");
             

        //initial ADDED  not accounted for 
        mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV FROM 'file:///" + fnwadd + "' AS raw_line FIELDTERMINATOR '|' WITH raw_line SKIP 1 WITH raw_line[2] AS child_seq, raw_line[3] AS parent_seq,  toInteger(raw_line[4]) AS added_count, raw_line[5] AS added_variants WITH child_seq, parent_seq, added_count,added_variants MATCH (parent:mt_seq {name: parent_seq}) MATCH (child:mt_seq {name: child_seq}) MERGE (parent)-[r:hg_parent_child]->(child) SET r.added_count = added_count, r.added_variants = added_variants");
        
        
        // add ALL DROPPED list to previously created relationship
        mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV FROM 'file:///" + fnall + "' AS raw_line FIELDTERMINATOR '|' WITH raw_line SKIP 1 WITH raw_line[2] AS child_seq,      raw_line[3] AS parent_seq,      toInteger(raw_line[4]) AS dropped_count, split(raw_line[5], ',') AS dropped_path_variants_list WITH child_seq, parent_seq, dropped_count, [variant IN dropped_path_variants_list | {original: variant, position: CASE WHEN variant =~ \"^(\\\\d+).*\"   THEN toInteger(apoc.text.regreplace(variant, \"^(\\\\d+).*\", \"$1\")) ELSE 0  END }] AS variants_with_positions WITH child_seq, parent_seq, dropped_count, apoc.coll.sortMaps(variants_with_positions, 'position') AS sorted_variants_with_positions WITH child_seq, parent_seq, dropped_count, [x IN sorted_variants_with_positions | x.original] AS sorted_variants MATCH (parent:mt_seq {name: parent_seq}) MATCH (child:mt_seq {name: child_seq}) MERGE (parent)-[r:hg_parent_child]->(child) SET r.all_dropped_count = dropped_count, r.all_dropped_path_variants = sorted_variants");
        
        
        mt_var.sorter.update_relationship_var_list_with_pos_sort1 urel = new mt_var.sorter.update_relationship_var_list_with_pos_sort1();
        urel.update_rel_var_listg("hg_parent_child","dropped_path_variants");
        urel.update_rel_var_listg("hg_parent_child","added_variants");
        urel.update_rel_var_listg("hg_parent_child","all_dropped_path_variants");
 
        
//        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (parent:mt_seq)-[r:hg_parent_child]->(child) WHERE r.dropped_path_variants IS NOT NULL WITH r, [variant IN r.dropped_path_variants | { original: variant,position: CASE  WHEN size(apoc.text.regexGroups(variant, \"\\\\d+\")) > 0 THEN toInteger(apoc.text.regexGroups(variant, \"\\\\d+\")[0][0]) ELSE 0 END }] AS variants_with_positions UNWIND variants_with_positions AS sorted_data WITH r, sorted_data ORDER BY sorted_data.position ASC WITH r, collect(sorted_data.original) AS sorted_variants SET r.dropped_path_variants = sorted_variants");
        
        
        
        return "";
    }
}

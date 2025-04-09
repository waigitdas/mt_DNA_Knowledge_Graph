/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class create_shared_path_variants_rel {
    @UserFunction
    @Description("Template used in creating new functions.")

    public String create_shared_path_var(

  )
   
         { 
             
        create_relationship();
         return "";
            }

    
    
    public static void main(String args[]) {
        create_relationship();
    }
    
     public static String create_relationship()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

     String fn = "shared_path_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";

     String cq = "MATCH (d1:dnode) MATCH (d2:dnode) WHERE d1.name < d2.name WITH d1, d2, apoc.coll.intersection(d1.path_variants, d2.path_variants) AS shared_path_variants WHERE size(shared_path_variants) > 0 RETURN d1.name AS hg1, d2.name AS hg2, shared_path_variants";

    mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited(cq, fn);
    
//     fn = "shared_path_variants_20250226_122727.csv";
     String lc = "load csv with headers from 'file:///" + fn + "' AS line FIELDTERMINATOR '|' return line ";
    cq = " MATCH (d1:dnode {name: line.hg1}) MATCH (d2:dnode {name: line.hg2}) MERGE (d1)-[r:dnode_shared_variants{shared_path_variants: line.shared_path_variants, shared_path_variant_ct:  size(split(line.shared_path_variants, ','))}]->(d2)";

    mt_var.neo4jlib.neo4j_qry.APOCPeriodicIterateCSV(lc, cq, 10000);

       mt_var.neo4jlib.neo4j_qry.qry_write("CALL apoc.periodic.iterate('MATCH ()-[r:dnode_shared_variants]->() WHERE r.shared_path_variants IS NOT NULL RETURN r', 'SET r.shared_path_variants = apoc.convert.fromJsonList(r.shared_path_variants)', {batchSize: 100000, parallel: false, iterateList: true, retries: 25})");

               
        
        mt_var.sorter.update_relationship_var_list_with_pos_sort su = new mt_var.sorter.update_relationship_var_list_with_pos_sort();
            su.update_relationship_var_listg("dnode_shared_variants","shared_path_variants");
            
            
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (dp)-[r:dnode_shared_variants]->(dc) SET r.hp_pair = dp.name + ':' + dc.name");
        mt_var.neo4jlib.neo4j_qry.qry_write("CREATE INDEX hp_pair_index FOR ()-[r:dnode_shared_variants]-() ON (r.hp_pair);;");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (dp)-[r:dnode_shared_variants]->(dc) OPTIONAL MATCH path = (dp)-[:dnode_child*1]->(dc)  SET r.parent_child = CASE WHEN path IS NOT NULL THEN 1 ELSE 0 END");
        mt_var.neo4jlib.neo4j_qry.qry_write("CREATE INDEX hp_parent_child FOR ()-[r:dnode_shared_variants]-() ON (r.parent_child);");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (dp)-[r:dnode_shared_variants]->(dc) OPTIONAL MATCH path = (dp)-[:haplotree*1..]->(dc)  WITH dp, dc, r, path,      CASE WHEN path IS NOT NULL THEN length(path) ELSE 0 END AS hops SET r.haplo_distance = hops");
        
        
        //       mt_var.excelLib.excelRept.createExcel("query here", fn, "sheet name", "", "", true, "message.", false);
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

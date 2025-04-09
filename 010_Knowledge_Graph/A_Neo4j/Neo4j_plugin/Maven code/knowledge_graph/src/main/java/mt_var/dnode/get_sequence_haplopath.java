/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.dnode;

import java.util.regex.Pattern;
import static mt_var.genlib.create_mt_seq_csv_file.fasta_seq;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
//import mt_var.excelLib.*;

public class get_sequence_haplopath {
    @UserFunction
    @Description("External sequence processed to estimate placement of dnode tree (haplopath).")

    public String get_seq_info(
         @Name("seq_path")
             String seq_path
  )
   
         { 
             
        get_rept(seq_path);
         return "";
            }

    
    
    public static void main(String args[]) {
        String path = "E:/DAS_Coded_BU_2020/Genealogy/DNA/mt_haplotree_project/mt_DNA_sequences/entrez/";
        get_rept(path + "AB055387.1.fasta");
    }
    
     public static String get_rept(String seq_path) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String seq = mt_var.neo4jlib.file_lib.getFileNameFromPath(seq_path);
        
        String fn = "seq_rept_" + seq + "_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        String s[] = mt_var.neo4jlib.file_lib.readFileByLine(seq_path).split("\n");
    
          seq = "";
           
           // concatenate the rows into a single string with the full sequence
          // String header = s[0].replaceAll(" [^a-zA-Z_-]+ ", "");
           for (int j=1; j<s.length; j++)
           {
               seq = seq + s[j];
               }

       
       String cq = "with '" + seq + "' as sfs match (p:probe) where p.subsumed_by is null and p.ref_start_pos>15 and sfs<>replace(sfs,p.probe,'') with p match (h: dnode) where p.pid in h.unique_probes  with   h.name as computed_hg, collect(p.pid) as probe_ids, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten(collect (p.variants)))) as vars, h.path_variants as pv return  computed_hg, size(probe_ids) as probe_ct,size(vars) as probe_var_ct, size(pv) as path_var_ct, apoc.coll.sort(probe_ids), mt_var.sorter.sort_variant_list_by_pos_toList(vars) as uniq_probe_variants, pv as path_variants";
       mt_var.excelLib.excelRept.createExcel(cq, fn, "haplopath_details", "", "", true, "message.", true);
        
        
        return "";
    }
}

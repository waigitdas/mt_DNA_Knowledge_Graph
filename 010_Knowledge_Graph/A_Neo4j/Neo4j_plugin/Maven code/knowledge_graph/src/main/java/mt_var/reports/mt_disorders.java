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

public class mt_disorders {
    @UserFunction
    @Description("summary of mt-disease mutations and frequency in study Genbank sequences.")

    public String show_disorders(
 
  )
   
         { 
             
        rept_disorders();
         return "";
            }

    
    
    public static void main(String args[]) {
        rept_disorders();
    }
    
     public static String rept_disorders()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String fn = "mt_disease_mutations_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
        
        mt_var.excelLib.excelRept.createExcel("MATCH path=(p)-[r:probe_variant]->(v) where v.disease is not null with collect(p.pid) as pids, v.name as variant, v.pos as pos, sum(p.seq_ct) as seq_ct , v.disease as mt_disorder, v.locus as locus, v.locus_type as locus_type return variant, pos, locus, locus_type, seq_ct, size(pids) as probe_ct,mt_disorder,  pids as probe_ids  order by seq_ct desc,pos", fn, "study_sample_disease_mutations", "4;5", "4:#######;5:####", true, "Mitochondrial disorders from mt-DNA mutations were found in the GenBank population.\nThe incidence approximates that found in population surveys.\nThe mutations were provided by MitoMap and linked to the probes created in this study.", false);
        
        String cq = "with ['T3394C','T14502C','T12811C','G11696A','G3316A','C3497T','C3571T', 'T12338C', 'A14693G','C4216T','A1595G'] as sec match (s:mt_seq) where apoc.coll.contains(s.all_variants,'G11778A') and size(sec)<>size(apoc.coll.subtract(sec,s.all_variants)) with sec, s, apoc.coll.intersection(sec, s.all_variants) as seq_sec return s.name as seq, seq_sec as seconary_mutation,toInteger(apoc.text.regexGroups(seq_sec[0],'\\d+')[0][0])  as pos, s.all_variants as seq_variants order by pos  ";
         mt_var.excelLib.excelRept.createExcel(cq, fn, "secondary_mutations", "", "", false, "seconary mutations increase the penatrence of the phnotype\nThese sequences have the primary mutation G11778A and the secondry mutation(s) lited.\nThe apoc.coll.* functions are set functions with enble rapid quiery performance.\n\nreference: \n]L.Caporali et al., “Peculiar combinations of individually non-pathogenic missense mitochondrial DNA variants cause low penetrance Leber’s hereditary optic neuropathy,” PLoS Genetics, vol. 14, no. 2, p. e1007210, Feb. 2018, doi: 10.1371/journal.pgen.1007210.\nY. Dai et al., “Mutation analysis of Leber’s hereditary optic neuropathy using a multi-gene panel,” Biomedical Reports, vol. 8, no. 1, p. 51, Nov. 2017, doi: 10.3892/br.2017.1014.", true);
  
        return "";
    }
}

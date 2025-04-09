/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.ref_files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class hg_all_sequences {
    @UserFunction
    @Description("Haplogroup assignments for all sequences.")

    public String get_haplogroup(
        @Name("refseq") 
            String refseq
  )
   
         { 
             
        get_hg(refseq);
         return " ";
            }

    
    
    public static void main(String args[]) {
        get_hg("rsrs");
    }
    
     public static String get_hg(String refseqUsed) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String cq= "MATCH p=(k:mt_seq) where k.ancient_hg is null return k.name as name order by k.name";
               
        String seq[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n");
 
        String fn = refseqUsed + "_hg_using_haplopath_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        File f = new File( mt_var.neo4jlib.neo4j_info.Import_Dir + fn);
        FileWriter fw = null;
                try
                {
                    fw = new FileWriter(f);
                    fw.write("seq_name|computed_hg|asgn_hg|missing_ct|missing|filtered_path_variants|hg_lvl|branch_variants|dnode\n");
                }
                catch(IOException e){}
        
    for (int i=0; i<seq.length;i++)
        {
        cq = "MATCH (k:mt_seq{name:" + seq[i] + "}) with k match (p:probe)-[r:probe_variant]->(v: variant) with k,v where k.fullSeq<>replace(k.fullSeq,p.probe,'') and v.name is not null with k,v order by v.pos with k,collect(v.name) as vs match (h: dnode) with k,h, apoc.coll.subtract(h.filtered_path_variants,vs) as missing RETURN k.name as sequence,h.name as hg,case when k.assigned_hg is null then '' else k.assigned_hg end as assigned_hg,size(missing) as missing_ct, missing,h.filtered_path_variants as path_variants, h.lvl as ht_lvl, h.branch_variants as branch_variants,h.path as dnode order by missing_ct, h.lvl desc limit 1";
        String r[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0].split(Pattern.quote("|"));
        try
        {
            for (int j=0;j<r.length;j++)
            {
                fw.write(r[j].replace("\"","") + "|");
            }
            fw.write("\n");
            fw.flush();
        }
        catch(IOException e){}
       

    }  // next sequence

    try
    {
        fw.flush();
        fw.close();
    }
    catch (IOException e){}
    
    //load hg to knowledge graph
    mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + fn + "' as line FIELDTERMINATOR '|' match(k:mt_seq{name:toString(line.seq_name)}) set k.gfg_hg=toString(line.computed_hg)  ");
    
    mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + fn + "' as line FIELDTERMINATOR '|' match(k:mt_seq{name:toString(line.seq_name)}) match (h:" + refseqUsed + "_haplopath{name:toString(line.computed_hg)}) merge (k)-[r:" + refseqUsed + "_seq_dnode]->(h)");
    
    mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + fn + "' as line FIELDTERMINATOR '|' with line, line.seq_name as seq,line.seq_var_ct as var_ct,line.seq_var as vs match (s:mt_seq{name:seq}) set s.seq_variant_ct=var_ct, s.seq_variants=vs");
    
    mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///" + fn + "' as line FIELDTERMINATOR '|' with line, line.seq_name as seq,line.seq_probe_ct as probe_ct,line.seq_probes as ps match (s:mt_seq{name:seq}) set s.seq_probe_ct=probe_ct, s.seq_probes=ps");
    
    
        return "";
    }
    
    
}

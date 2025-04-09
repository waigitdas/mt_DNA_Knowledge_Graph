/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class load_probes_variants_relationahips {
    @UserFunction
    @Description("loads probe variants from previousl created pipe-delimited file..")

    public String loads_variants_from_file(
  )
   
         { 
             
        load_vars();
         return "";
            }

    
    
    public static void main(String args[]) {
        load_vars();
    }
    
     public static String load_vars() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        mt_var.neo4jlib.neo4j_qry.CreateIndex(" variant", "name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("probe", "pid");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("mt_seq", "name");
        
         String fn = "probe_variants_position_probes_only_20240806_213625.csv"; 
                 //"probe_variants_all_probes_subsumbed_by_null_20240806_223707.csv"; //probe_variants_20240726_135613.csv";
         mt_var.neo4jlib.file_lib.copyFileToImportDirectory(fn);
         
         String fnload = "probe_pos_var.csv";  //received file with distinct, needed fields
        
         //transform probe_variant file into new file filtered and condensed containing data relevant to next steps
         mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited("LOAD CSV WITH HEADERS FROM 'file:///" + fn + "' AS line fieldterminator '|' with line where toInteger(line.pos)>15 with toInteger(line.pos) as pos, toInteger(line.pid) as pid, trim(line.var) as var, line.anc as anc, line.der as der, line.classification as classification, toInteger(line.method) as method return distinct pos, pid, var, anc, der, classification, method ", fnload);
         
         //////////////////////////////////////////////////////////////
         //position probes
         //create position variant nodes
         String lc = "load csv with headers from 'file:///" + fnload + "' as line fieldterminator '|' return line ";
         String cq = " with line where toInteger(line.pos)>15 merge (v: variant{name:trim(line.var), pos:toInteger(line.pos), anc:case when line.anc is null  or line.anc='null' then '' else line.anc end, der:case when line.der is null or line.der='null' then '' else line.der end, source:'position'})";
         
         mt_var.neo4jlib.neo4j_qry.APOCPeriodicIterateCSV(lc, cq, 200000);
         mt_var.genlib.tracker.writeTracker("variants loaded from large probe_variant file");
         
         
         /////////////////////////////////////////////////////////////
         //load mt disorder probes
         mt_var.load.load_disease_mutations ldm = new mt_var.load.load_disease_mutations();
         ldm.mitomap_mutations("rsrs");
         mt_var.genlib.tracker.writeTracker("mitochondrial disease mutations loaded);");
         
         //add heteroplasmy new variant nodes
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:variant) where (not v.der in ['A','C','G','T', ''] and v.name=replace(v.name,'.','') ) or (v.name=replace(v.name,'.','') or apoc.text.regexGroups(v.name,'\\\\d+\\.\\d+([^ACGT]+)')) with v, mt_var.heteroplasmy.get_vars(v.name,v.anc,v.der) as nv unwind nv as x with v,nv, x optional match(vx:variant{name:x}) with v,x,vx where vx.name is null and x > ' ' merge (vnew:variant{name:x, pos:v.pos,anc:v.anc,der:right(x,1)}) \n" +
"set vnew.source='heteroplasmy', vnew.probe_ct = case when v.probe_ct is null then 0 else v.probe_ct end");
          mt_var.genlib.tracker.writeTracker("new variants from heteroplasmy pvariants;");
         
         ///////////////////////////////////////////////////////////////////////
         //load overlap probes
        String fnnewprobes = "new_probes_from_overlaps_220240725_113930.csv";
         mt_var.neo4jlib.file_lib.copyFileToImportDirectory(fnnewprobes);

           mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fnnewprobes + "' as line fieldterminator '|'   with line   merge (p:probe{probe:line.new_probe}) set p.ref_start_pos=toInteger(line.new_start),p.ref_len=toInteger(line.new_len), p.seq_ct=toInteger(line.new_seq_ct), p.variants=replace(line.variants,' ',''), p.source=toInteger(line.subsumed_probe_id),p.orig_ref_start_pos=toInteger(line.pos), p.original_probe=line.orig_probe, p.left_flank=toInteger(line.left_flank), p.right_flank=toInteger(line.right_flank), p.type='overlap'");
           
           mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fnnewprobes + "' as line fieldterminator '|'  with line.new_probe as probe, min(toInteger(line.pid)) as pid match (p:probe{probe:probe}) set p.pid=pid");
           
           
           mt_var.probes.never_completes_probe_hierarchy ph = new mt_var.probes.never_completes_probe_hierarchy();
           ph.create_probe_hierarchy();
           
         mt_var.genlib.tracker.writeTracker("new/overlap probes added and subsumed by and source prode properties set");
  
        //changes string to list of strings
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:probe{type:'overlap'}) with n, split(n.variants,',') as ss  set n.variants= ss");
        
         
         ///////////////////////////////////////////////////////////////////////
         //usse this to load large file with links between probes and variants
        lc = "LOAD CSV WITH HEADERS FROM 'file:///" + fnload + "' AS line FIELDTERMINATOR '|' return line  ";
        cq = " WITH line where toInteger(line.pos)>15 MATCH (v: variant {name:trim(line.var)}) with line, v MATCH (p:probe) WHERE p.pid = toInteger(line.pid) with line, p, v MERGE (p)-[r:probe_variant {classification:line.classification, method:toInteger(line.method), pos:toInteger(line.pos), load_method:1}]->(v)";
        mt_var.neo4jlib.neo4j_qry.APOCPeriodicIterateCSV(lc, cq, 200000);
        mt_var.genlib.tracker.writeTracker("probes variant relationships created from large probe_varient csv file");
        
               
        //////////////////////////////////////////////////////////////////////////
        //seq_probe relationships
        String seqrelFile = "seq_position_probes_20240717_105103.csv";
         mt_var.neo4jlib.file_lib.copyFileToImportDirectory(seqrelFile);
         
       cq =  "load csv with headers from 'file:///" + seqrelFile + "' as line fieldterminator '|' with line match(s:mt_seq{seq_id:toInteger(line.seq_id), name:line.seq}) match(p:probe{pid:toInteger(line.pid)}) merge (s)-[r:seq_probe]->(p)";  
       mt_var.neo4jlib.neo4j_qry.qry_write(cq);
         mt_var.genlib.tracker.writeTracker("seq pos probe relationships loaded");
    
            String ovf = "seq_overlap_probea_20240718_115801.csv";
            mt_var.neo4jlib.file_lib.copyFileToImportDirectory(ovf);

         mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + ovf + "' as line fieldterminator '|' with line match(s:mt_seq{name:line.seq}) with line, s match (p:probe{probe:line.probe}) merge (s)-[r:seq_probe{start_pos:line.new_start, end_pos:line.new_end, type:'overlap'}]->(p)");
        mt_var.genlib.tracker.writeTracker("seq overlap probe relationships loaded");
 
        ///////////////////////////////////////////////////////////////
        
        //add heteroplasms variant probe_variant relationship
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p:probe)-[r:probe_variant]->(v:variant) where (not v.der in ['A','C','G','T', ''] and v.name=replace(v.name,'.','') ) or (v.name=replace(v.name,'.','') or apoc.text.regexGroups(v.name,'\\\\d+\\.\\d+([^ACGT]+)')) with  distinct v,p with v, p, mt_var.heteroplasmy.get_vars(v.name,v.anc,v.der) as nv unwind nv as x call { with x match (vx:variant{name:x, source:'heteroplasmy'}) return x as y, vx} with p,v, y, vx merge (p)-[rnew:probe_variant{source:'heteroplasmy', pos:v.pos}]->(vx)");
                

                
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p)-[r:probe_variant]->(v) with p, v order by v.pos with p, collect(v.name) as variants set p.variants= variants");
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path=(p:probe)-[r:probe_variant]->(v:variant{source:'heteroplasmy'}) set p.heteroplasmy=1");
        
        
        mt_var.genlib.tracker.writeTracker("probe variants set");
        return "";
    }
}

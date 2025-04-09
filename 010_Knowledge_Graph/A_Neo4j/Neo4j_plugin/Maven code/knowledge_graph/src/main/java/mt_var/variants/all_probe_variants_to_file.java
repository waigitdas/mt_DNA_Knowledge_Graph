/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class all_probe_variants_to_file {
    @UserFunction
    @Description("computess all probe variants using DMP.")

    public String probe_variants_to_csv_and_load(
  )
   
         { 
             
        get_variants();
         return "completed";
            }

    
    
    public static void main(String args[]) {
        get_variants();
    }
    
     public static String get_variants() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
 
        mt_var.neo4jlib.neo4j_qry.CreateIndex(" variant","name");
        mt_var.neo4jlib.neo4j_qry.CreateIndex(" variant","pos");

        String fn = "probe_variants_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fn);
        FileWriter fw = null;
                try
                {
                    fw = new FileWriter(f);
                    fw.write("var|pid|pos|anc|der|classification|method|diff\n");
                }
                catch(IOException e){}
                
        
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str("match(p:probe) where p.subsumed_by is null return p.pid as id order by id").split("\n");
        //{type:'position'}
        mt_var.variants.get_probe_variants v = new mt_var.variants.get_probe_variants();
        
        for (int i=0; i<c.length; i++)
        {
            String r = v.compute_probe_variants(Long.parseLong(c[i]));

            try
            {
                fw.write(r);
                fw.flush();
            }
            catch(IOException e)
            {
            System.out.println(c[i] + "\t" + e.getMessage());
            }
            
        }
        
        try
        {
            fw.flush();
            fw.close();
        }
        catch(IOException e){}
        
        try
        {
            mt_var.genlib.tracker.writeTracker("probe variants computed and exported to csv file.");
        }
        catch(Exception e){}
        
 
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fn + "' as line fieldterminator '|' with line where line.der=replace(line.der,'N','') with toInteger(line.pos) as pos, line.var as var ,line.anc as anc, line.der as der, collect(distinct toInteger(line.method)) as m, count(*) as ct with pos,var,m, ct,anc,der optional match (v: variant{name:var}) with pos,var,v,m,ct,anc,der where v.name is null create (new_var: variant{name:var, pos:pos, anc:anc, der:der,source:'probe', probe:1 })");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fn + "' as line fieldterminator '|' with line where line.der=replace(line.der,'N','') with line, toInteger(line.pos) as pos, line.var as var match (v: variant{name:var, pos:pos}) with line, v match (p:probe) where id(p)=toInteger(line.pid) merge (p)-[r:probe_variant{classification:line.classification, method:toInteger(line.method)}]->(v)");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fn + "' as line fieldterminator '|' with line where line.der=replace(line.der,'N','') and line.var in ['8281-8289d', '106-111d', '954-961d'] with line, toInteger(line.pos) as pos, line.var as var match (v: variant{name:var}) with line, pos, v match (p:probe) where id(p)=toInteger(line.pid) merge (p)-[r:probe_variant{classification:line.classification, method:toInteger(line.method), pos:pos}]->(v)");
        
        try
        {
        mt_var.genlib.tracker.writeTracker("new variant appended to  variant node set and probe_variant relationship created");
        }
        catch(Exception e){}
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (p)-[r:probe_variant]->(v) with p, v order by v.pos with p, collect(v.name) as variants set p.variants= variants");
        
        return "";
    }
}

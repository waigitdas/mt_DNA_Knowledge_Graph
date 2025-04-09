/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import mt_var.load.web_file_to_import_folder;
import mt_var.neo4jlib.neo4j_info;
import mt_var.neo4jlib.neo4j_qry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.UserFunction;


public class upload_haplotree {
       @UserFunction
       @Description("Loads the entire mt-haplotree directly from the current FTDNA mt-DNA json refernce file into Neo4j. This json is updated frequently as new variants and haplotree branches are discovered. Source: https://www.familytreedna.com/public/mt-dna-haplotree/get")

    public String create_rsrs_haplotree()
    {
        upload_FTDNA_rsrs_haplotree();  
        return "completed";
    }
    
  public static void main(String args[]) {
        //upload_FTDNA_rsrs_haplotree();  
  }       
       
public String upload_FTDNA_rsrs_haplotree() {
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String FileNm = mt_var.neo4jlib.neo4j_info.Import_Dir + "rsrs_haplotree.json";
        
        //retrieve online FTDNA Y-haplotree json and place in import directory.
        web_file_to_import_folder.url_file_to_import_dir("https://www.familytreedna.com/public/mt-dna-haplotree/get","rsrs_haplotree.json");
          
        neo4j_qry.CreateIndex("block", "haplogroupId");
        neo4j_qry.CreateIndex("block", "name");
        neo4j_qry.CreateIndex("variant", "name");
        try{
       
        }
        catch (Exception e){}

        try{
            neo4j_qry.CreateCompositeIndex("block","haplogroupId,name,parentId,IsRoot");
            neo4j_qry.CreateCompositeIndex("variant","name,pos,anc,der,region");
        
        }
       catch (Exception e){}
        
        //read, parse  and load json into Neo4j
        File file = new File(FileNm);
        String fileContents="";
 
        try (FileInputStream inputStream = new FileInputStream(file))
        {
            fileContents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
            
        //read json
        JSONObject jo = new JSONObject(fileContents).getJSONObject("allNodes");
        int jl = jo.length();
        String fny =neo4j_info.Import_Dir + "rsrs_HT.csv";
        String fnv =neo4j_info.Import_Dir + "rsrs_HT_variants.csv";
        File fy = new File(fny);
        File fv = new File(fnv);

       try{

        FileWriter fwy = new FileWriter(fy);
        fwy.write("haplogroupId|name|parentId|IsRoot\n");
        FileWriter fwv = new FileWriter(fv);
        fwv.write("variant_name|haplogroupId|pos|anc|der|region\n");
 
        String var = "";
        String rg = "";
        String hid = "";
        String pos = "";
        String anc = "";
        String der = "";

        JSONArray keys = jo.names();
        
        for (int i=0; i< keys.length(); i++) {
            String key = keys.getString (i); 
            JSONObject jo2 = jo.getJSONObject(key);
            try{
                fwy.write(jo2.get("haplogroupId") + "|" + jo2.get("name") + "|" + jo2.get("parentId") + "|" + jo2.get("isRoot") + "\n");
             }
            catch (Exception e)  //no parentId
            {
          }
            
            JSONArray ja3 = jo2.getJSONArray("variants");
            
            for (int k=0; k<ja3.length() ; k++) {
                //fourth level parse
                JSONObject ja4 = ja3.getJSONObject(k);
                try { var = ja4.get("variant") + "|";}
                catch (Exception e) {var = "|";}
                try { hid = jo2.get("haplogroupId") + "|";}
                catch (Exception e) {hid = "0|";}
                try { pos = ja4.get("position") + "|";}
                catch (Exception e) {pos = "0|";}
                try { anc = ja4.get("ancestral") + "|";}
                catch (Exception e) {anc = "|";}
                try { der = ja4.get("derived") + "|";}
                catch (Exception e) {der = "|";}
                 try { rg = ja4.get("region") + "|";}
                catch (Exception e) {rg = "0|";}
                try {
                    fwv.write(var + hid + pos + anc + der + rg +"\n");
                } 
                catch (IOException ex) {
                    //Logger.getLogger(upload_Y_DNA_Haplotree.class.getName()).log(Level.SEVERE, null, ex);
                }
              }  
         
       
        }  
            fwy.flush();
            fwy.close();
            fwv.flush();
            fwv.close();
      
            }
         catch (Exception e) {System.out.println(e.getMessage());}
    
       //Load csv to Neo4j
       String lc = "LOAD CSV WITH HEADERS FROM 'file:///rsrs_HT.csv' as line FIELDTERMINATOR '|'  ";
 
       String cq = "merge (b:block{haplogroupId:toInteger(line.haplogroupId),name:toString(line.name),parentId:toInteger(line.parentId),IsRoot:toBoolean(line.IsRoot)})";
       neo4j_qry.qry_write(lc + cq);
       
      neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///rsrs_HT_variants.csv' as line FIELDTERMINATOR '|' with line where line.variant_name is not null merge (v:variant{name:toString(case when line.der<>toUpper(line.der) then toUpper(line.variant_name) else line.variant_name end), pos:toInteger(line.pos),anc:toString(case when line.anc is null then '' else line.anc end),der:toString(case when line.der is null then '' else toUpper(line.der) end)}) set v.region=toString(case when line.region is null then '' else line.region end), v.ftdna=1");
       
       //delete 3 duplicate variant nodes
       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:variant) with v.name as variant,count(*) as ct with variant, ct where ct>1 match (vx:variant) where vx.name=variant and vx.ftdna=1 delete vx");
       
      neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///rsrs_HT_variants.csv' as line FIELDTERMINATOR '|' with line match (b:block{haplogroupId:toInteger(line.haplogroupId)}) match (v:variant{name:toString(case when line.der<>toUpper(line.der) then toUpper(line.variant_name) else line.variant_name end)}) merge (b)-[r:block_variant]->(v)");  
     
       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (b1:block) with b1 match (b2:block) where b2.haplogroupId=b1.parentId merge (b2)-[r:block_child]-(b1)");
  
       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (b1:block) with b1 match (b2:block) where b2.haplogroupId=b1.parentId merge (b2)-[r:block_child]-(b1)");
       

       //set lvl and ordpath
       mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path=(b1:block{name:'RSRS'})-[r:block_child*0..99]->(b2:block) with b1,b2,[x in nodes(path)|id(x)] as opp with b1,b2,size(opp)-1 as lvl, mt_var.graph.get_ordpath(opp) as op set b2.lvl=lvl,b2.op=op");
       
      //add variants to block
      mt_var.neo4jlib.neo4j_qry.qry_write("MATCH p=(b:block)-[r:block_variant]->(v:variant) with b,v order by v.name with b,collect(v.name) as vs set b.branch_variants=vs");
      
      //add path_variants property to block
      mt_var.neo4jlib.neo4j_qry.qry_write("MATCH path=(b1:block{name:'RSRS'})-[r:block_child*0..999]->(b2:block) with b2, [x in nodes(path)|x.name] as blocks, [y in nodes(path)|id(y)] as op, apoc.coll.dropDuplicateNeighbors(apoc.coll.sort(apoc.coll.flatten([z in nodes(path) where z.branch_variants is not null|z.branch_variants]))) as variants with b2,blocks,variants,size(op) as lvl, mt_var.graph.get_ordpath(op) as op set b2.path_variants = variants,b2.path_variant_ct=size(variants)");
       
//      mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:variant) set n.source='ftdna'");
      

        mt_var.load.upload_mt_yfull_tmrca utm = new mt_var.load.upload_mt_yfull_tmrca();
        utm.oad_yfull_mt_times();
        
       return "Completed";
    }
        
}
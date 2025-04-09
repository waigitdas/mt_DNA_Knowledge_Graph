/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import mt_var.load.web_file_to_import_folder;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.UserFunction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class upload_mt_yfull_tmrca {
       @UserFunction
       @Description("Loads the entire mt-haplotree directly from the current YFull mt-DNA json refernce file into Neo4j. This json is updated frequently as new snps and haplotree branches are discovered. Source: https://raw.githubusercontent.com/YFullTeam/MTree/master/mtree/current_mtree.json")

     public String oad_yfull_mt_times() {
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        String s = load_mt_yfull();
        return s;
        }
        
    
     
     
        public class haplogroup
        {
            String id;
            String parent_id;
            String snps;
            String formed;
            String formedlowage;
            String formedhighage;
            String tmrca;
            String tmrcahighage;
            String tmrcalowage;
            JsonArray children;
         
        
        }
 
               public static void main(String args[]) {
            load_mt_yfull();
         }
 
     public static String load_mt_yfull()
     {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
       
        //delete prior haplotree data
//        gen.neo4jlib.neo4j_qry.qry_write("match ()-[r:mt_block_child]-() delete r");
//        gen.neo4jlib.neo4j_qry.qry_write("match ()-[r:mt_block_snp]-() delete r");
//        gen.neo4jlib.neo4j_qry.qry_write("match (b:mt_yfull_block)-[r]-() delete r");
//        gen.neo4jlib.neo4j_qry.qry_write("match (b:mt_yfull_block) delete b");
//        gen.neo4jlib.neo4j_qry.qry_write("match (v:mt_yfull_variant)-[r]-() delete r");
//        gen.neo4jlib.neo4j_qry.qry_write("match (v:mt_yfull_variant) delete v");
//        
        String FileNm = mt_var.neo4jlib.neo4j_info.Import_Dir + "yfull_mt_haplotree.json";
        ArrayList<haplogroup> MasterList = new ArrayList<haplogroup>();
        
        //retrieve online FTDNA Y-haplotree json and place in import directory.
        web_file_to_import_folder.url_file_to_import_dir("https://raw.githubusercontent.com/YFullTeam/MTree/master/mtree/current_mtree.json","yfull_mt_haplotree.json");
         
//        neo4j_qry.CreateIndex("mt_block", "haplogroupId");


        try
        {
        //read json
        String data = new String(Files.readAllBytes(Paths.get(FileNm))); 
        JsonElement parent = JsonParser.parseString(data);
 
        //initiate iteration
        haplogroup hg = new Gson().fromJson(data, haplogroup.class);
        ArrayList<haplogroup> temp = new ArrayList<haplogroup>();
        
        //MasterList will hold all the blocks in an array
        MasterList.add(hg);
        
        for (int h=0; h<50000; h++)
        {
            try{
                //get hchild haplogroups
                temp = parseBlock(MasterList.get(h));
            }
            catch(Exception e){
                break;}  //break at end of list
    
            //add returned child blocks to the master list
            try
            {
            for (int j= 0; j<temp.size(); j++)
            {
                MasterList.add(temp.get(j));
            }
            }
            catch(Exception e){}  //null values not processed
        } // next iteration until break
}  //end try
        catch(Exception e){System.out.println(e.getMessage());}  //not caught in development
        
        
        //use MasterList to export csv for import into Neo4j
        String s = "hg|snp\n";
        String c = "hg|parent|tmrca|tmlow|tmhigh|formed|formedlow|formedhigh\n";
        for (int i=0;i<MasterList.size(); i++)
        {
            haplogroup hp = MasterList.get(i);
          
            if (hp.snps!=null)
            {
                

            String ss[] = hp.snps.split(",");
            try{
            c = c + hp.id + "|" + hp.parent_id + "|" + hp.tmrca + "|" + hp.tmrcalowage + "|" + hp.tmrcahighage +   "|" + hp.formed + "|" + hp.formedlowage + "|" + hp.formedhighage +  "\n";
            
           
            }
            catch(Exception e){}

            for (int j=0; j<ss.length; j++)
            {
                s = s + hp.id + "|" + ss[j].strip() + "\n";

            }
            }
                
        }
        
        mt_var.neo4jlib.file_lib.writeFile(s, mt_var.neo4jlib.neo4j_info.Import_Dir + "YFull_mt_hg_snps.csv");
        mt_var.neo4jlib.file_lib.writeFile(c, mt_var.neo4jlib.neo4j_info.Import_Dir + "YFull_mt_hg_parent.csv");
        
        //mt_yfull_block

        mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///YFull_mt_hg_parent.csv' as line FIELDTERMINATOR '|' match (m:dnode{name:line.hg}) set m.tmrca=toInteger(line.tmrca), m.tmlow=toInteger(line.tmlow), m.tmhigh=toInteger(line.tmhigh), m.formed=toInteger(line.formed), m.formedlow=ToInteger(line.formedlow), m.formedhigh=toInteger(line.formedhigh) ");
        
     
       return "Completed";
    }
     
     
    public static ArrayList<haplogroup> parseBlock(haplogroup parent)
    {
            
            ArrayList<haplogroup> chhg = new ArrayList<haplogroup>();  // new Gson().fromJson(ch, haplogroup.class);
            try
            {
            for (int i=0; i<parent.children.size();i++)
            {
                JsonObject ch = parent.children.get(i).getAsJsonObject();
                haplogroup chx = new Gson().fromJson(ch, haplogroup.class);
                chx.parent_id = parent.id;
                chhg.add(chx);
            }
            }
            catch(Exception e){}
          
         return chhg;
     }
         
}
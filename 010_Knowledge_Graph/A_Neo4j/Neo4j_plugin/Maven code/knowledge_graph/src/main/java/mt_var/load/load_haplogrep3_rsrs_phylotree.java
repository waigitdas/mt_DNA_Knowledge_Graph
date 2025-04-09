/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class load_haplogrep3_rsrs_phylotree {
    @UserFunction
    @Description("loads Haplogrep3 phylotree from downloded file")

    public String load_tree(
 
  )
   
         { 
             
        phylotree();
         return "";
            }

    
    
    public static void main(String args[]) {
        phylotree();
    }
    
     public static String phylotree() 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        cleanup();
        
        String fnh = "haplogrep_haplogroups_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fnh);
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(f);
            fw.write("haplogroup|parent|variant\n");
        }
        catch(IOException e){}
                
        
         
        String cq = "";
        String fn = "phylotree_17_rsrs.xml";
        String filePath = "E:/Genealogy/Haplogrep3/software/trees/phylotree-rsrs/17.0/" + fn;
       
       mt_var.neo4jlib.neo4j_qry.CreateIndex("haplogroup", "name");;
        mt_var.neo4jlib.neo4j_qry.CreateIndex("hg_variant", "name");;
       
        Document xmlDoc  = null;
try
{
            // Initialize XML Parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            // Parse XML File into a Document object
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.out.println("Error: File not found - " + filePath);
                return null;
            }
            xmlDoc= builder.parse(xmlFile);
            xmlDoc.getDocumentElement().normalize(); // Normalize for consistency
            int fgf=0;
            System.out.println
        ("XML Loaded Successfully: " + xmlDoc.getDocumentElement().getNodeName());
            System.out.println(xmlDoc.getElementsByTagName("haplogroup").getLength());
  
        } 
        catch (Exception e) {
            System.out.println("Error loading XML: " + e.getMessage());
        }

     List<Haplogroup> haplogroups = new ArrayList<>();
        NodeList haplogroupNodes = xmlDoc.getElementsByTagName("haplogroup");

        for (int i = 0; i < haplogroupNodes.getLength(); i++) {
            Element haplogroupElement = (Element) haplogroupNodes.item(i);
            String haplogroupName = haplogroupElement.getAttribute("name").trim();
            List<String> variants = new ArrayList<>();

            NodeList detailsList = haplogroupElement.getElementsByTagName("details");
            for (int j = 0; j < detailsList.getLength(); j++) {
                Element detailsElement = (Element) detailsList.item(j);
                NodeList polyList = detailsElement.getElementsByTagName("poly");
                for (int k = 0; k < polyList.getLength(); k++) {
                    Element polyElement = (Element) polyList.item(k);
                    String variant = polyElement.getTextContent().trim();
                    if (!variant.isEmpty()) {
                        variants.add(variant);
   
                        
              // Retrieve the parent haplogroup name
                Node parentNode = haplogroupElement.getParentNode();
                String parentHaplogroupName = "";
                if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element parentElement = (Element) parentNode;
                    if (parentElement.getTagName().equals("haplogroup")) {
                        parentHaplogroupName = parentElement.getAttribute("name").trim();
                    }
                }
                        
                        
                        
                        
                        try
                        {
                            fw.write(haplogroupName + "|" +  parentHaplogroupName + "|" +variant +"\n");
                            fw.flush();
                        }
                        catch(IOException e){}
                    }
                }
            }

            haplogroups.add(new Haplogroup(haplogroupName, variants));
        }
 
  
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fnh + "' as line fieldterminator '|' with line with  line.haplogroup as hg, count(*) as ct, collect(distinct line.variant) as var create (h:haplogroup{name:hg, varianr_ct: ct, variants:var})");
        
mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fnh + "' as line fieldterminator '|' with line with  apoc.coll.sort(collect(distinct line.haplogroup ) ) as hgs, count(*) as ct, line.variant as var create(v:hg_variant{name:var, hg_ct:ct, hgs:hgs})");

        
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fnh + "' as line fieldterminator '|' with line match  (h:haplogroup{name:line.haplogroup}) with h, line match(v:hg_variant{name:line.variant}) merge (h)-[r:haplogroup_variant]->(v)");

        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///" + fnh + "' as line fieldterminator '|'  with line where line.parent>' '   match (hp:haplogroup{name:toString(line.parent)}) with hp, line match (hc:haplogroup{name:toString(line.haplogroup)}) merge (hp)-[r:haplogroup_child]->(hc)");
        
        
        /////////////////////////////////////////////////////////////////////////
        //harmonize names to knowledge graph schema
        mt_var.neo4jlib.neo4j_qry.qry_write(("MATCH (v:hg_variant) with v where v.name=replace(v.name,'.','') and v.name=replace(v.name,'!','') with v, right(v.name,1) as der with v, der where der<>'d'  set v.der =der, v.pos=toInteger(apoc.text.regexGroups(v.name,'\\d+')[0][0])"));
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:hg_variant) with v where v.name=replace(v.name,'.','') and right(v.name,1)='d' set v.der ='', v.pos=toInteger(apoc.text.regexGroups(v.name,'\\\\d+')[0][0])");

        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:hg_variant) WHERE (v.name CONTAINS '.'  or v.name<>replace(v.name,'!',''))  SET v.pos = toInteger(apoc.text.regexGroups(v.name, '^\\d+')[0][0])");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (v:hg_variant) where v.der is not null  and v.name=replace(v.name,'!','') with v MATCH (n:lookup_probe{pos:v.pos})  with  n, v,  substring(n.probe,0,1) as nuc  set v.anc=nuc, v.name = nuc + v.name");
        

        return "";
    }
     
     
     public static void cleanup()
     {
         mt_var.neo4jlib.neo4j_qry.qry_write("match ()-[r:haplogroup_variant]->() delete r");
         mt_var.neo4jlib.neo4j_qry.qry_write("match ()-[r:haplogroup_child]->() delete r");
         mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:haplogroup) delete n");
         mt_var.neo4jlib.neo4j_qry.qry_write("match (n:hg_variant) delete n");
         
     }
     
     //class for Haplogroup object that is instantiated for processing xml
        private static class Haplogroup {
            private final String name;
            private final List<String> variants;

            public Haplogroup(String name, List<String> variants) {
                this.name = name;
                this.variants = variants;
            }

            public String getName() {
                return name;
            }
        }
       
}

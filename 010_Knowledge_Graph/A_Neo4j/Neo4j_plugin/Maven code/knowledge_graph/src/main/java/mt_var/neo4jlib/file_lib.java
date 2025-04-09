/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.neo4jlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.*;
import org.apache.commons.io.IOUtils;


public class file_lib {
    public static String currExcelFile;
    public static Boolean openExcelFile;

    public static String[] ReadGEDCOM(String filePath) {
        String delimiter = "%^&*"; 
        String c = mt_var.neo4jlib.file_lib.ReadFileByLineWithEncoding(filePath);
        String[] s = c.replace("|","^").split("0 @");
        String g = "";

        //pull out only the desired elements
        for (int i=0; i<s.length; i++){
                if (s[i].substring(0,1).equals("P") || s[i].substring(0,1).equals("F")) {
                   g = g + delimiter +  s[i];
                    }     
                }
                
        String ged[] = g.split(Pattern.quote(delimiter));
        System.out.println(ged.length );

        return ged;
    }
    
    
    public static String ReadFileByLineWithEncoding(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
 
        try {
          Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.ISO_8859_1);
                        
        {
            try{stream.forEach(s -> contentBuilder.append(s.replace("\"", "")).append("\n"));}
            catch(Exception eee){System.out.println(eee.getMessage() + "^^^");}
        }
        }
        catch (IOException ee) 
        {
            ee.printStackTrace();
        }
    
        return contentBuilder.toString();
 
    }
    
    public static String copyFileToImportDirectory(String filePath)
    {
        try {
            Files.copy(Paths.get(mt_var.neo4jlib.neo4j_info.ref_data_dir + filePath), Paths.get(mt_var.neo4jlib.neo4j_info.Import_Dir + filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            //Logger.getLogger(load_initial_knowledge_graph_from_csv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        StringBuilder contentBuilder = new StringBuilder();
// 
//        try {
//            //read unicode
//          Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8);
//                        
//        {
//            stream.forEach(s -> contentBuilder.append(s).append("\n"));
//        }
//        }
//        catch (IOException e) 
//        {
//            e.printStackTrace();
//        }
// 
//        String s =  contentBuilder.toString();  
//        String fn = mt_var.neo4jlib.file_lib.getFileNameFromPath(filePath);
//        
//        File fni = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fn);
//        try{
//            FileWriter fw = new FileWriter(fni);
//            fw.write(s);
//            fw.flush();    
//            fw.close();
//    }
//        catch (IOException e){}
        
        return "";
    }
    
    public static String readFileByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
 
        try {
            //read unicode
          Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8);
                        
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
    
    
    public static void writeFile(String s, String filePath) {
        File fn = new File(filePath);
        try{
            FileWriter fw = new FileWriter(fn);
            fw.write(s);
            fw.flush();    
            fw.close();
    }
        catch (IOException e){}
        
    }
    
    
public static void get_file_transform_put_in_import_dir(String filePathRead, String filePathSave){

try{        
   Reader fileReader = new FileReader(filePathRead);
   //Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(fileReader);
   Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(fileReader);
   File fn = new File(neo4j_info.Import_Dir +  filePathSave);
   FileWriter fw = new FileWriter(fn, Charset.forName("UTF8"));
            
        
   int ct = 0;
   for (CSVRecord record : records) {
       String s = "";
       for (int i=0; i < record.size(); i++) {
        if (ct==0) {
            s = s + record.get(i).replace(" ","_").replace("-","_") + "|";
        }
        else {
            s = s + record.get(i).replace("\"","")  + "|";
        }
    
   }
       
   fw.write(s + "\n");
   fw.flush();;
   ct = ct +1;
   }

       fw.flush();    
       fw.close();
       fileReader.close();
}
     catch (Exception e) {System.out.println(e.getMessage());};
    }
   
public static void get_file_transform_selected_put_in_import_dir(String filePathRead, String filePathSave, String selectText, String kit){

try{        
   Reader fileReader = new FileReader(filePathRead);
   //Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(fileReader);
   Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(fileReader);
   File fn = new File(neo4j_info.Import_Dir +  filePathSave);
   FileWriter fw = new FileWriter(fn);
            
        
   int ct = 0;
   for (CSVRecord record : records) {
          if(ct>0 && record.get(0).contentEquals("Known SNP")==false){break;}
          String s="";
          if (ct==0) {s = "kit" + "|";}
          else{s = kit + "|";}
       for (int i=0; i < record.size(); i++) {

        if (ct==0) {s = s + record.get(i).replace(" ","_").replace("-","_") + "|";}
        else {
            
            s = s + record.get(i).replace("\"","")  + "|";
        }
    
   } //end record
       
   fw.write(s + "\n");
   ct = ct +1;

   } //end all records

       fw.flush();    
       fw.close();
       fileReader.close();
}
     catch (Exception e) {System.out.println(e.getMessage());};
    }
   
public static String getFileNameFromPath(String FileName) {
    String[] s = FileName.split("/");
    return s[s.length-1];
}

public static void parse_chr_containing_csv_save_to_import_folder(String FileName,int ChrColNumber){
    FileWriter fw = null;
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        String s ="";
        try {
            String c = file_lib.ReadFileByLineWithEncoding(FileName);
            c = c.replace("|"," ").replace(",","|").replace("\"", "");
            //System.out.println("\n" + neo4j_info.Import_Dir);
            String[] cc = c.split("\n");
            String header = cc[0].replace(" ","_").replace("-","_");
            c = c.replace(cc[0], header);
            String[] ccc = c.split("\n");
            String SaveFileName = getFileNameFromPath(FileName);
            File fn = new File(neo4j_info.Import_Dir + SaveFileName);
            fw = new FileWriter(fn);
            fw.write(header.replace("ï»¿", "") + "\n");
            for (int ii=1; ii<ccc.length; ii++){
                String[] xxx = ccc[ii].split(Pattern.quote("|"));
                
                s = "";
                for(int j=0; j<xxx.length; j++) {
                    if (j==ChrColNumber) {  //chr
                        if (xxx[j].strip().length()==1) {
                            xxx[j] = "0" + xxx[j].strip();
                        }
                        else {xxx[j]=xxx[j].strip();}
                    }
                    s = s + xxx[j] + "|";
                }
                s = s +  "\n";
               if (s !="") {fw.write(s);}  //ignore blank lines
            }
            if (s !="") {fw.write(s);}  //ignore blank lines
            fw.flush();
            fw.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
//Logger.getLogger(file_lib.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                    System.out.println(ex.getMessage());

                //Logger.getLogger(file_lib.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
}
 


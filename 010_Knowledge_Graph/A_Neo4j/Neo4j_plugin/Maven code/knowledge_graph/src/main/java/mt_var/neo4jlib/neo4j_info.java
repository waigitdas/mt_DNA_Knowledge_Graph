/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.neo4jlib;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//reads text file with user name, password, folders relevant to the functions. 
//this strategy mitigates the need for users to input this info into the function calls

public class neo4j_info {
    public static String project;
    public static String Import_Dir=null;
    public static String Database_Dir;
    public static String neo4j_username;
    public static String neo4j_password;
    public static String tg_logic ;
    public static String tg_logic_overlap ;
    public static String user_database ;
    public static String root_directory;
    public static String gedcom_file ;
    public static String Curated_rn_gedcom_file;
    public static String tg_file;
    public static String alt_left_bracket ;
    public static String alt_right_bracket ;
// public static String tg_file;
    public static String probe_not_matched = "X"; // 0x29BB;
//   public static String alt_left_bracket ;
//   public static String alt_right_bracket ;
     
    //windows
    public static String activeProjectData = "C:/Genealogy/gfg/wai/active_neo4j.wai";
    //Linux
    public static String neo4j_home_dir;
    public static String wrt;    
    public static String cq;
    public static int method;
    public static String ref_data_dir; 
    public static String probe_discovery_file;
    public static FileWriter fwtracker;
    public static double start_time;
    public static String seq_probes_file;
    public static String anc_seq;
    public static String refseqUsedPublic;
    public static ArrayList<String>  mvars;
//    public static mt_var.variants.gfgVar gv;
//    public static List<mt_var.variants.heteroplasmy> heteroplasmy_list;

    //public static Workbook wb;
    
public static Boolean neo4j_var() {
    Boolean WasFilled =false;
    if (Import_Dir == null) {  //variable not available
 
    String c = file_lib.readFileByLine(activeProjectData);
    String[] s = c.split("\n");
    int ct = 0;
    for (int i=1; i < s.length; i++) {  //first row is comment
        String[] ss = s[i].split(Pattern.quote("|"));
        switch (ss[0]){
            case "project": project = ss[1].strip() ; 
            break;
            case "neo4j_import_directory": Import_Dir =  ss[1].strip() ; //s[ct+1].replace("neo4j_import_directory: ", "");
           break; 
            case "neo4j_username": neo4j_username = ss[1].strip();
            break;
            case "neo4j_password": neo4j_password = ss[1].strip();
            break;
            case "user_database": user_database = ss[1].strip();    
            break;
            case "ftdna_data_root_dir": root_directory = ss[1].strip();    
            break;
            case "gedcom_file": gedcom_file = ss[1].strip();    
            break;
            case "curated_rn_gedcom_file": Curated_rn_gedcom_file = ss[1].strip();    
            break;
            case "tg_file": try{tg_file = ss[1].strip();    } catch(Exception e){tg_file ="";}
       }
        ct = ct + 1;
    }
    tg_logic = "s.chr=t.chr and t.strt_pos<=s.strt_pos and t.end_pos>=s.end_pos";
    tg_logic_overlap = "s.chr=t.chr and t.end_pos>=s.strt_pos and t.strt_pos<=s.end_pos";
    alt_left_bracket = "\u298B";
    alt_right_bracket = "\u298C";
    Database_Dir =  Import_Dir.replace("import/", "");
    ref_data_dir = "E://DAS_Coded_BU_2020/Genealogy/Publishing/convergent Evolution/reference_files/";
    
 }
    else {
        WasFilled = true;
    }
    
    return WasFilled;

  } 


public static Boolean neo4j_var_reload() {
    
     //neo4j_home_dir = gen.neo4jlib.neo4j_qry.qry_str("Call dbms.listConfig() YIELD name, value where name='dbms.directories.neo4j_home' RETURN replace(value,'\\\\','/') as path");
    Boolean WasFilled =true;  
    String c = file_lib.readFileByLine(activeProjectData);
    String[] s = c.split("\n");
    int ct = 0;
    for (int i=1; i < s.length; i++) {  //first row is comment
        String[] ss = s[i].split(Pattern.quote("|"));
        switch (ss[0]){
            case "project": project =  ss[1].strip() ; //s[ct+1].replace("neo4j_import_directory: ", "");
           break; 
           case "neo4j_import_directory": Import_Dir =  ss[1].strip() ; //s[ct+1].replace("neo4j_import_directory: ", "");
           break; 
            case "neo4j_username": neo4j_username = ss[1].strip();
            break;
            case "neo4j_password": neo4j_password = ss[1].strip();
            break;
            case "user_database": user_database = ss[1].strip();    
            break;
            case "ftdna_data_root_dir": root_directory = ss[1].strip();    
            break;
            case "gedcom_file": gedcom_file = ss[1].strip();    
            break;
            case "curated_rn_gedcom_file": Curated_rn_gedcom_file = ss[1].strip();    
            break;
            case "tg_file": try{tg_file = ss[1].strip();}catch(Exception e){tg_file ="";}    
       }
        ct = ct + 1;
    }
    tg_logic = "s.chr=t.chr and t.strt_pos<=s.strt_pos and t.end_pos>=s.end_pos";
    tg_logic_overlap = "s.chr=t.chr and t.end_pos>=s.strt_pos and t.strt_pos<=s.end_pos";
    alt_left_bracket = "\u298B";
    alt_right_bracket = "\u298C";
   
    return WasFilled;

  } 

  
public static void main(String args[]){
    neo4j_var();
}

        
}
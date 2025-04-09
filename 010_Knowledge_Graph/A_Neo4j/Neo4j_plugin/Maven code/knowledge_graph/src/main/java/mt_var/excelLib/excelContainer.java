/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.excelLib;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelContainer {
    public static Workbook wb;
    @UserFunction
    @Description("TCreates nd hold Excel Workbook.")

    public String newWorkbook(
       
  )
   
         { 
             
        String s = createWorkbook();
        return s;
       
            }

    
    
    public static void main(String args[]) {
        createWorkbook();
    }
    
     public static String createWorkbook() 
    {
        wb = new XSSFWorkbook();
        return "completed";
    }
}

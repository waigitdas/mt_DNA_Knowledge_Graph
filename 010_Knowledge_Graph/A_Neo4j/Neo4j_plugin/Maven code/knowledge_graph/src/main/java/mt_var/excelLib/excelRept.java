/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.excelLib;

//import mt_var.neo4jlib.neo4j_qry;
import java.awt.Desktop;
import java.io.File;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelRept {
    @UserFunction
    @Description("createsExcel file from cypher query.")

    public  String cypher_to_excel(
        @Name("cq") 
            String cq,
        @Name("fileName")
            String fileName,
        @Name("sheetName")
            String sheetName,
        @Name("colSum")
            String colSum,
       @Name("colNumberFormat")
            String colNumberFormat,
        @Name("createNewExcelFile")
            Boolean createNewExcelFile,
          @Name("Message")
            String Message,
          @Name("OpenFile")
            Boolean OpenFile

            
            
        
  )
   
         { 
             
        String s =createExcel(cq, fileName, sheetName, colSum, colNumberFormat, createNewExcelFile, Message, OpenFile);
         return s;
            }

    
    
    public static void main(String args[]) {
        String fn = "Example_" + mt_var.genlib.current_date_time.getDateTime() + ".xlsx";
//        createExcel("Match(s:mt_seq) return s.seq_id as seq_id, s.name as seq, s.assigned_hg as hg limit 25", fn, "sequences", "0:######",  true, "", false);
//        createExcel("match (n) return labels(n) as node, count(*) as ct", fn, "nodes","1:#######", true, "message", false);
//        createExcel("match ()-[r]->() return type(r) as relationship, count(*) as ct", fn, "relationhips","1:#######", false, "message", true);
//        createExcel("MATCH p=(b)-[r:dnode_probe_pattern]->(pp)  with b, pp.pattern as pattern match (p:probe)-[rv:probe_variant]->(v) where p.probe in pattern with b, collect(distinct v.name) as vn match (h: dnode{name:b.name}) with h,  h.branch_variants  as bvn, vn with h, bvn,vn, apoc.coll.intersection(bvn,vn) as both, apoc.coll.subtract(bvn,vn) as missed return apoc.text.repeat('...',h.lvl) + h.name as dnode, h.lvl, size(bvn) as block_var_ct,size(vn) as gfg_var_ct, size (both) as var_in_both, size(missed) as gfg_missed_var, mt_var.sorter.sort_variant_list_by_pos(missed) as missed, mt_var.sorter.sort_variant_list_by_pos(vn) as gfg_var, mt_var.sorter.sort_variant_list_by_pos(bvn) as haplpath_var order by h.op, h.name", fn, "haplopaths","1:#######;2:###;3:###;4:###;5:###", false, "", true);
    }
    
     public static String createExcel(String cq, String fn, String sheetName, String colSum, String numFormat,Boolean newExcel, String message, Boolean OpenFile) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited(cq,"file:" + mt_var.neo4jlib.neo4j_info.Import_Dir +  fn);
        String c[] = mt_var.neo4jlib.file_lib.ReadFileByLineWithEncoding(mt_var.neo4jlib.neo4j_info.Import_Dir + fn).split("\n");
        
        
        //mt_var.
        if (newExcel.compareTo(true)==0)
        {
            mt_var.excelLib.excelContainer.createWorkbook();
        }
        
        
        //Workbook workbook = new XSSFWorkbook();
        Sheet sheet = mt_var.excelLib.excelContainer.wb.createSheet(sheetName);
        sheet.createFreezePane(0, 1);
        
        
        ///////////////////////////////////////////////////////
        //header
        Row headerRow = sheet.createRow(0);
        String ch[] = c[0].split(Pattern.quote("|"));
        int cols = ch.length;
        int cellId = 0;
        for (String ci : ch)
        {
            
            Cell hc = headerRow.createCell(cellId++);
            hc.setCellValue(ci);
        }
                
        ///////////////////////////////////////////////////////
        // rows
       
        for (int i=1; i<c.length; i++)
        {
            Row dataRow = sheet.createRow(i);
            int rowCellId = 0;
            String cr[] = c[i].split(Pattern.quote("|"));
            for (String cj :cr)
            {
               Cell rc = dataRow.createCell(rowCellId++);

                try {
                    double numericValue = Double.parseDouble(cj.replace(",", ""));
                    rc.setCellValue(numericValue);
                } catch (NumberFormatException e) {
                    if(cj.length()>5000)
                    {
                        cj = cj.substring(0,5000) + "... truncated";
                    }
                        
                    rc.setCellValue(cj);
                }
            }
        }
        /////////////////////////////////////////////////
        //column sum
        if(colSum.compareTo("")!=0)
        {
            String ss[] = colSum.split(Pattern.quote(";"));
            int nrw = sheet.getLastRowNum() +2;
            Row sumRow = sheet.createRow(nrw);
            for (int j=0; j<ss.length; j++)
            {
                int col = Integer.parseInt(ss[j]);
                sumColumn(mt_var.excelLib.excelContainer.wb, sheet, col, sumRow);            
            }
        }

       
        
        /////////////////////////////////////////////////
        //column numeric format
        if(numFormat.compareTo("")!=0)
        {
            String ns[] = numFormat.split(Pattern.quote(";"));
            for (int j=0; j<ns.length; j++)
            {
                String nss[] = ns[j].split(Pattern.quote(":"));
                int col = Integer.parseInt(nss[0]);
                formatColumnAsNumber(sheet,col,nss[1]);              
            }
        }
        
        sheet.setAutoFilter(new CellRangeAddress(0,c.length,0, cols-1));
        

        //autofit column width after filters and formats applied but before messages.
        int colId=0;
        for (int i=0; i<cols; i++)
        {   
            int newWidth  = 0;
            try
            {
            sheet.autoSizeColumn(i);
            //increment width to accomadate the filter space
            //In Apache POI, the column width is measured in units of 1/256th of a character width.
            newWidth = sheet.getColumnWidth(i) + 750 ;
            sheet.setColumnWidth(i,newWidth);
            }
            catch(Exception e)
            {
                //Apache POI causes error if the column width wwould be >255
                newWidth = 255 *256;
                sheet.setColumnWidth(i, newWidth);
            }
        }

        /////////////////////////////////////////////////////
        //add message
        addMessage(sheet,"cypher query: \n" + cq + "\n" + message);
        
        
        ////////////////////////////////////////////////////
        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(mt_var.neo4jlib.neo4j_info.Import_Dir +  fn)) {
            mt_var.excelLib.excelContainer.wb.write(fileOut);
        } catch (IOException e) {}

        // Closing the workbook
        try {
            if (OpenFile.compareTo(true)==0)
                {
                    mt_var.excelLib.excelContainer.wb.close();
                    Desktop.getDesktop().open(new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fn));
                }
        } 
        catch (IOException e) {}
   
        return "Excel file created successfully!";
    }
     
     private static String formatColumnAsNumber(Sheet sheet, int columnIndex, String numFormat) {
        // Create a numeric cell style
        CellStyle numericStyle = mt_var.excelLib.excelContainer.wb.createCellStyle();
        DataFormat format = mt_var.excelLib.excelContainer.wb.createDataFormat();
        numericStyle.setDataFormat(format.getFormat(numFormat));

        // Iterate over all rows in the sheet
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    cell.setCellStyle(numericStyle);
                }
            }
        }
        return "";
    }
     
     private static String addMessage(Sheet s, String msg)
     {
            int ct = 0;
            int lr = 0;
            String ms[] = msg.split("\n");
            for (int i = 0; i<ms.length; i++)
            {
                switch (ct)  //different spacing for difference lines in the messahe
                {
                    case 0:   //first row is the cypher query
                        ct = ct + 1;
                        lr = s.getLastRowNum() + 3;
                        break;
                    case 1:
                        ct = ct + 1;
                        lr = s.getLastRowNum() +1;
                        break;
                     case 2:
                        ct = ct + 1;
                        lr = s.getLastRowNum() +2;
                        break;
                    default:
                        lr = s.getLastRowNum() + 1;
                        break;
                            
                }
                        
                Row m = s.createRow(lr);
                Cell mc = m.createCell(0);
                mc.setCellValue(ms[i]);
            }
            return "";
    }
     
 public static void sumColumn(Workbook workbook, Sheet sheet, int columnIndex, Row sumRow) {
    // Create a bold font and style
    Font boldFont = workbook.createFont();
    boldFont.setBold(true);
    CellStyle boldStyle = workbook.createCellStyle();
    boldStyle.setFont(boldFont);

    // Create a new cell two rows below the last item in the column
    Cell sumCell = sumRow.createCell(columnIndex);

    // Determine the range of cells to sum
    int firstRow = 0;
    int lastRow = sheet.getLastRowNum() - 1;
    String columnLetter = CellReference.convertNumToColString(columnIndex);
    String formula = String.format("SUM(%s%d:%s%d)", columnLetter, firstRow + 2, columnLetter, lastRow + 1);

    // Set the formula
    sumCell.setCellFormula(formula);

    // Apply the bold style to the sum cell
    sumCell.setCellStyle(boldStyle);
}
 
}

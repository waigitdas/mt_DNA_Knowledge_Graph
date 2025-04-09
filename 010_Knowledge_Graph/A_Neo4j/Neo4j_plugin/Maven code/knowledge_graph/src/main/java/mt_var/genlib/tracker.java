/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mt_var.genlib;

import java.io.IOException;

/**
 *
 * @author david
 */
public class tracker {

     public static void writeTracker(String item)
     {
         try
         {
             double endtm = (System.nanoTime() - mt_var.neo4jlib.neo4j_info.start_time)/60000000000L;
             mt_var.neo4jlib.neo4j_info.fwtracker.write(mt_var.genlib.current_date_time.getDateTime() + "," + endtm + "," + item + "\n");
             System.out.println(mt_var.genlib.current_date_time.getDateTime() + "\t" + endtm + " min \t" +   item);
             mt_var.neo4jlib.neo4j_info.fwtracker.flush();
             }
    catch(IOException e){}
         
         
     }
    
}


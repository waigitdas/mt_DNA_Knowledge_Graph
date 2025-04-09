/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mt_var.genlib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author david
 */
public class current_date_time {

    public static String getDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now);
    }
    
    public static String getGEDCOMdate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now);
    }
    
    public static void main(String args[]) {
        System.out.println(getDateTime());
        System.out.println(getGEDCOMdate());
    }
}

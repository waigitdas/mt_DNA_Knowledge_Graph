/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.util.ArrayList;


public class gfgVar {
    String name;
    Long pos;
    String anc;
    String anc_nuc;
    String der;
    String der_nuc;
    String classification;
    String newElement;
    ArrayList<String> variant_list;
    String submission; 
    int method;
    String dmp;
    
    
    public String get_name()
    {
        return name;
    }
    
    public String get_anc()
    {
        return anc;
    }
    
    public String get_der()
    {
        return der;
    }
    
    public String get_anc_nuc()
    {
        return anc_nuc;
    }
    
    public String get_der_nuc()
    {
        return der_nuc;
    }
    
    public Long get_pos()
    {
       return pos;
     }
    
    public String get_classification()
            {
                return classification;
            }
    
    public String get_newEmement()
    {
        return newElement;
    }
    
    public ArrayList<String> get_variant_list()
    {
        return variant_list;
    }     
    
    public int get_method()
    {
        return this.method;
    }

    public String get_dmp()
    {
        return this.dmp;
    }
    
}


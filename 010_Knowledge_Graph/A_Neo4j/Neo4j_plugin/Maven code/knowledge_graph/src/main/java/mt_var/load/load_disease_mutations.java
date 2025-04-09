/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.load;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class load_disease_mutations {
    @UserFunction
    @Description("Loads mito map disease mutations as mt_variant nodes")

    public String mitomap_mutations(
            @Name("refseq")
                String refseq
         )
   
         { 
             
        load_mitomap(refseq);
         return "";
            }

    
    
    public static void main(String args[]) {
        load_mitomap("rsrs");
    }
    
     public static String load_mitomap(String refseqUsed) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
  
       String fn ="ConfirmedMutations_MITOMAP_Foswiki.csv";
       
       mt_var.neo4jlib.file_lib.copyFileToImportDirectory(fn);
  
        String delimiter = "|";
        
        String fno = "mitomap_import.csv";
        File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fno);
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(f);
            fw.write("mm_notation|name|anc|der|pos|type|locus|locus_type|aa_rna|disease\n");
        }
        catch(Exception e){}
        
        String mm[] = mt_var.neo4jlib.file_lib.ReadFileByLineWithEncoding(mt_var.neo4jlib.neo4j_info.Import_Dir + fn).split("\n");
        String v[] = new String[5];
        
        for (int i=1; i<mm.length; i++)
        {
            String m[] = mm[i].split(Pattern.quote(","));
            v = get_mutation(m);
            if(v[0]!=null)
            {
                String sout = m[4] + delimiter + v[0] + delimiter + v[1] + delimiter + v[2] + delimiter + v[3] + delimiter + v[4] + delimiter + m[2] + delimiter + m[1]  + delimiter + m[6]  + delimiter + m[3];
//                System.out.println(sout.replace(delimiter,"\t"));
                try
                {
                    fw.write(sout + "\n");
                }   
                 catch(IOException e){}

                int hh=999;
            }
        }
        
            try
            {
                fw.flush();
                fw.close();
            }
            catch(IOException e){}
        
            //add load_disease_mutations variants
            mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///mitomap_import.csv' as line fieldterminator '|' MATCH(v: variant{name:line.name}) set v.disease=line.disease, v.locus=line.locus, v.locus_type=line.locus_type, v.mm_notation=line.mm_notation, v.aa_rna=line.aa_rna");
            
            mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///mitomap_import.csv' as line fieldterminator '|' optional MATCH(v: variant{name:line.name}) with line where v.name is null create(v: variant{name:line.name, pos:toInteger(line.pos), anc:line.anc, der:line.der, disease:line.disease, locus:line.locus, mm_notation:toString(line.mm_notation), locus_type:line.locus_type, aa_rna:line.aa_rna, source:'mitomap'})");
            
        return "";
    }
     
     public static String[] get_mutation(String[] m)
     {
         String v[] = new String[6];
         //v[0] = null;
         String m1 = m[4].replace("m.","");
         String m2[] = new String[2];
         
         if (m1.contains(">")==true)
         {
            m2 = m1.split(Pattern.quote(">"));
            v[0] = m2[0].substring(m2[0].length()-1,m2[0].length()) + m2[0].substring(0,m2[0].length()-1) + m2[1];
            v[1] = m2[0].substring(m2[0].length()-1,m2[0].length()) ;  //anc
            v[2] = m2[1];
            v[3] = m[5];
            v[4] = "transition";
         }
         
         if (m1.contains("ins")==true)
         {
             m2 = m1.split(Pattern.quote("_"));
             String m3[] = m2[1].split(Pattern.quote("ins"));
             v[0] = m2[0] + "." + m3[1].length() + m3[1];
             v[1] = "";
             v[2] = m3[1];
             v[3] = m[5];
             v[4] = "insertion";
         }
         
            if (m1.contains("del")==true)
         {
             m2 = m1.split(Pattern.quote("_"));
             String m4[] = m1.split(Pattern.quote("del"));
             String m3[] = m2[0].split(Pattern.quote("del"));
             v[0] = m4[0] + "d" ;
             v[1] = "";
             v[2] = ""; // m3[1];
             v[3] = m[5];
             v[4] = "deletion";
         }

         
         return v;
     }


}

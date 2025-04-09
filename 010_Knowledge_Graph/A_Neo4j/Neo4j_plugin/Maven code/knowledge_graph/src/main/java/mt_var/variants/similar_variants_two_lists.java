/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class similar_variants_two_lists {
    @UserFunction
    @Description("TCompares array of variants and report back those with similar pos and nucleotide.")

    public String find_similar_variants(
        @Name("list1") List<String> list1,
        @Name("list2") List<String> list2 
  )
   
         { 
             
        String r = findSimilarPairs(list1, list2);
         return r;
            }

    
    
    public static void main(String args[]) {
       List<String> list1 = Arrays.asList("C41T", "T89d", "89d", "90d", "92.1CA", "C152T","A345G", "C195T");
        List<String> list2 = Arrays.asList("T89C", "G92A", "C152T","A345d", "C195T");
        String r = findSimilarPairs(list1, list2);
        System.out.println(r);
    }
    
  private static String findSimilarPairs(List<String> list1, List<String> list2) {
        List<Map<String, String>> similarPairs = new ArrayList<>();
        
        Pattern pattern = Pattern.compile("([A-Z])(\\d+)([A-Za-z]*)");
        String r = "";
        
        try
        {
        for (String str1 : list1) {
            Matcher matcher1 = pattern.matcher(str1);
            if (matcher1.matches()) {
                String base1 = matcher1.group(1);
                String position1 = matcher1.group(2);
                String variant1 = matcher1.group(3);
             

                for (String str2 : list2) {
                    Matcher matcher2 = pattern.matcher(str2);
                    if (matcher2.matches()) {
                        String base2 = matcher2.group(1);
                        String position2 = matcher2.group(2);
                        String variant2 = matcher2.group(3);

                        // Match the base and position but not the exact variant
                        if (base1.equals(base2) && position1.equals(position2) && !variant1.equals(variant2)) {
                            r = r + str1 + " : " + str2 + "  ~  ";
                        }
                    }
                }
            }
        }
        }
        catch(Exception e)
        {
            r = "error in variant similarity"; 
        }

        
        return r;
    }

    
    }

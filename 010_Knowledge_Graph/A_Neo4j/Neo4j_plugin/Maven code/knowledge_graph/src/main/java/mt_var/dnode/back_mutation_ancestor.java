/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.dnode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class back_mutation_ancestor {
    @UserFunction
    @Description("Determine back mutation source ... where it first appears in the path.")

    public String get_backmutation_ancestor(
        @Name("back_mutation") 
            String back_mutation
  )
   
         { 
             
        String m =get_anc_mutation(back_mutation);
         return m;
            }

    
    
    public static void main(String args[]) {
//        String x = get_anc_mutation("T195C!") + "\n";
//        x = x + get_anc_mutation("W123456Y!") + "\n";
//        x = x + get_anc_mutation("W123456Y!!!!") + "\n";

//        System.out.println(x);
    }
    
     public static String get_anc_mutation(String mut) 
    {
   
         
        //if not a back mutation, immediately return the submitted value
        if(mut.contains("!")==false)
                {
                    return mut;
                }
        
        
        //avoid convertibg double backmutations
        String x = mut.replace("!","");
        int sub = mut.length()-x.length();
        if((mut.length()-x.length())%2==0)
        {
            return x;
        }
        
        //regex patter to create 3 matches
        Pattern p = Pattern.compile("[A-Z]|[0-9]+|[A-Z]");
   
        //get matcher and put 3 matches into array
        Matcher m = p.matcher(mut);

        String c[] = new String[3];
        int i=0;
        try
        {
        while (m.find())
        {
            c[i] = m.group();
            i = i + 1;
            }  
        
            return c[0] + c[i] + c[2];
        }
        catch(Exception e)
        {
            return mut + "_error";
        }
    }
}

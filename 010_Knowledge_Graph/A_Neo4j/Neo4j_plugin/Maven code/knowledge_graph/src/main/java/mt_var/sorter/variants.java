/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.sorter;

import java.util.Arrays;
import java.util.List;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class variants {
    @UserFunction
    @Description("sorts variants in List<String> by their position")

    public String sort_variant_list_by_pos(
        @Name("variants") 
            List<String> variants
  )
   
         { 
             
        String r = sort_vars(variants);
         return r;
            }

    
    
    public static void main(String args[]) {
        //List<String> s2 = Arrays.asList()
        List<String> s2 = Arrays.asList("A1018G", "A10688G", "A11467G", "A11914G", "A12308G", "A13528G", "A16129G", "A1811G", "A247G", "A2758G", "A6047G", "A7521G", "A769G", "A825T", "C10810T", "C10873T", "C10915T", "C13565T", "C14620T", "C16311T", "C2885T", "C9540T", "G10398A", "G12372A", "G13105A", "G13276A", "G4104A", "G499A", "G7146A", "G8701A", "T10664C", "T12705C", "T13506C", "T13650C", "T15693C", "T16278C", "T16356C", "T3594C", "T4312C", "T4646C", "T5999C", "T7256C", "T7705C", "T8468C", "T8655C");
       sort_vars(s2);
    }
    
     public static String sort_vars(List<String> variant_list) 
     {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
       
        String ar ="";
        String q = "'";
       for (int i=0; i<variant_list.size(); i++)
        {
            ar = ar + q + variant_list.get(i) + q;
            if(i<variant_list.size()-1)
            {
                ar=ar +",";
            }
        }
        
       String cq ="with [" + ar + "] as vn unwind vn as x call {     with x     with x,toInteger(apoc.text.regexGroups(x,'\\\\d+')[0][0]) as pos return x as y,pos } with y,pos order by pos return collect(y) as sorted_var";
       
        String r = mt_var.neo4jlib.neo4j_qry.qry_to_csv(cq).split("\n")[0];
        //System.out.println(r);
        return r;
    }
}

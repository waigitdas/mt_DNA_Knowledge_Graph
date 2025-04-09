/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import mt_var.templates.*;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class adjacent_seq_variants {
    @UserFunction
    @Description("Template used in creating new functions.")

    public List<String> adjacent_variants_all_seq(
        @Name("var") 
            String var
        )
   
         { 
             
        List<String> ls = get_adjacent(var);
         return ls;
            }

    
    
    public static void main(String args[]) {
        get_adjacent("C16189T");
    }
    
     public static List<String> get_adjacent(String vx) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        List<String> ls = new ArrayList<>();
        
        String c[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str("with '" + vx + "'as vx MATCH (s:mt_seq)  where apoc.coll.contains(s.hg_missed_var,vx) with distinct  vx, toInteger(apoc.text.regexGroups(vx,'\\\\d+')[0][0]) as px match (v:variant) where px+10>v.pos>px-10  return   v.name as adjacent_var order by v.pos ").split("\n");
        
        for (int i=0; i<c.length; i++)
        {
            String cs[] = c[i].split(Pattern.quote("}"));
            for (int j=0; j<cs.length; j++)
            {
            ls.add(cs[j].replace("\"","")); 
        }
        }
        
        
        return ls;
    }
}

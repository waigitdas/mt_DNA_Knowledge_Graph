/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.heteroplasmy;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class new_heteroplasmy_var {
    @UserFunction
    @Description("returns list of probe from submitted variant with heteroplasmy code.")

    public List<String> get_vars(
        @Name("heteroplasmy_var") 
            String heteroplasmy_var,
        @Name("anc")
            String anc,
        @Name("der")
            String der
  )
   
         { 
             
        List<String> ls = get_new_var(heteroplasmy_var, anc, der);
         return ls;
            }

    
    
    public static void main(String args[]) {
        List<String> rls = get_new_var("G125B", "G", "B");
        String s = "";
        for (int i=0; i < rls.size(); i++)
        {
            System.out.println(rls.get(i));
            
        }
    }
    
     public static List<String> get_new_var(String hv, String anc, String der) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        String cq = "with '" + hv + "' as ov, '" + anc + "' as anc, '" + der + "' as der match (h:mt_heteroplasmy_codes{code:der}) with h, ov, der, anc unwind h.nucleotides as x call { with x, ov, der, anc with x, der, anc, replace(ov,der,x) as nv with nv as nv where x<>anc return nv } return nv";
        String r[] = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n");
        List<String> ls = new ArrayList<>();
        for (int i=0; i<r.length; i++)
        {
            ls.add(r[i].replace("\"",""));
        }
        
        return ls;
    }
}

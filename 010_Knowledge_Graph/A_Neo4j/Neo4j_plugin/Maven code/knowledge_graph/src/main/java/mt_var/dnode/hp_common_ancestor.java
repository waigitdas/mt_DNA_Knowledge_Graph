/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.dnode;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.regex.Pattern;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

/**
 *
 * @author david
 */
public class hp_common_ancestor {
    @UserFunction
    @Description("Returns string with number of mrcas and paths lengths to common ancestor. This can be used to look up the relationship and expected shared centimorgans.")

    public String mrca_path_len(
        @Name("hg1") 
            String hg1,
        @Name("hg2") 
            String hg2
  )
   
         { 
             
        String s = get_mrca_path_len(hg1, hg2);
         return s;
            }

//       public static void main(String args[]) {
//         System.out.println (get_mrca_path_len("L3e3b2","G3b1"));
//         System.out.println (get_mrca_path_len("G3b1","L3e3b2"));
//    }
       
        public String get_mrca_path_len(String hg1, String hg2)
        {
            String rnmin;
            String rnmax;
            String cq = "";
            String q = "\"";
            String s[] = null;
            if (hg1.compareTo(hg2)<0)
            {
                rnmin=hg1;
                rnmax=hg2;
            }
            else {
                rnmin=hg2;
                rnmax=hg1;
            }
                String r = "";
                try
                {
                cq= "match path = (h1:dnode{name:" + q + rnmin.replace("\"","") + q + "})-[r1:dnode_parent*0..29]->(mrca:dnode)<-[r2:dnode_parent*0..20]-(h2:dnode{name:" + q + rnmax.replace("\"","") + q + "}) where h1.name<h2.name return mrca.name as mrca,h1.name as h1name, h2.name as h2name,size(r1) as path1, size(r2) as path2";
                s = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0].split(Pattern.quote("|"));
                
                 if(hg1.compareTo(rnmin)==0)
                         {
                             //no switch
                             r = s[0].replace("\"","") + "|" + s[3] + "|" + s[4];
                         }
                 else
                 {
                     //switch back
                     r = s[0].replace("\"","") + "|" + s[4] + "|" + s[3];
                 }
                }
                catch(Exception e)
                {
                    r = "";  //Error: "  + s.length  + "\t" + cq;
                }
                return r; 
        }
        
}

/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.dnode;
    import mt_var.neo4jlib.neo4j_qry;

    import org.neo4j.procedure.Name;
    import org.neo4j.procedure.UserFunction;
    import org.neo4j.procedure.Description;
     import mt_var.conn.connTest;
import java.util.Collections;
 
    import java.util.List;        
import java.util.regex.Pattern;
      

public class mrca_from_cypher_List {          
    @UserFunction
    @Description("Returns most recent common dnode ancestor shared by multiple dnode nodes. ")
        
    public String mrca_from_cypher_list(
        @Name("hg_list") 
            List<String> hg_list
    )

        {
            String x = "[";
            String q = "\"";
        for (int i=0; i<hg_list.size(); i++)
        {
            x = x + q + hg_list.get(i) + q;
            if (i<hg_list.size()-1)
            {
                x = x + ",";
            }
         }
            x = x + "]"
;
            String cq= "match (c:dnode) where c.name in " + x + " With c order by c.name With collect(distinct c.name) As cc match (c2:dnode)-[:dnode_parent*0..25]->(MRCA:dnode)<-[:dnode_parent*0..25]-(c3:dnode) where c2.name in cc And c3.name in cc  and c2.name<>c3.name with MRCA,cc,c2 order by c2.name with MRCA,cc,collect(distinct c2.name) as cc2  with distinct cc,cc2,MRCA.name as CommonAncestor where cc2=cc return CommonAncestor";
        String r = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0].split(Pattern.quote("|"))[0];
        
        return r.replace("\"","");
        }
}
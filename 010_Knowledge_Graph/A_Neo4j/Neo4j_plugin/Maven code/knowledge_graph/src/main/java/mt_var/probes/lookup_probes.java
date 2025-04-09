/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class lookup_probes {
    @UserFunction
    @Description("create lookup probes")

    public String create_probes(
            @Name("refseq")
                String refseq
  )
   
         { 
             
        get_probes(refseq);
         return "";
            }

    
    
    public static void main(String args[]) {
        get_probes("rsrs");
    }
    
     public static String get_probes(String refSeqUsed) 
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        mt_var.neo4jlib.neo4j_qry.CreateIndex("lookup_probe", "pos");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("lookup_probe", "refseq");
        mt_var.neo4jlib.neo4j_qry.CreateIndex("lookup_probe", "probe");
        
        String refseq = "";
        
        if (refSeqUsed.compareTo("rcrs")==0)
        {
            refseq = mt_var.neo4jlib.neo4j_qry.qry_to_csv("MATCH p=(k:mt_seq{name:'NC_012920'}) return k.fullSeq as  rCRS").split("\n")[0];
        }

        if (refSeqUsed.compareTo("rsrs")==0)
        {
            refseq = mt_var.neo4jlib.neo4j_qry.qry_to_csv("MATCH p=(k:mt_seq{name:'AAAA_RSRS'})return k.fullSeq as rCRS").split("\n")[0];
        }
        
        String probe = "";
//        String nuc = "";
         
        //mt_var.neo4jlib.neo4j_qry.qry_write("match (l:lookup_probe) delete l");
        
        
        for (int i=0;i<refseq.length()-40;i++)
        {
            probe = refseq.substring(i,i+40);
//            nuc = refseq.substring(i,i+1);
            mt_var.neo4jlib.neo4j_qry.qry_write("merge(l:lookup_probe{refseq:'" + refSeqUsed + "', pos:" + i + ", probe:'" + probe.replace("\"","") +"'})" );
            
        }
        return "";
    }
}

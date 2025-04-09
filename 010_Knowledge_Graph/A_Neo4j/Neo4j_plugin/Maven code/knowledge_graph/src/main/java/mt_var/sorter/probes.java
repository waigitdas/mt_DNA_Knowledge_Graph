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


public class probes {
    @UserFunction
    @Description("sorts list of probe sequnces  by their start position")

    public String sort_probe_list_by_pos(
        @Name("probe_list") 
            List<String> probe_list
  )
   
         { 
             
        String r = sort_probes(probe_list);
         return r;
            }

    
  
    public static void main(String args[]) {
       List<String> s2 = Arrays.asList("TAGGCCCCAAGAATTTTGGTG", "AATCCATTTCACTATCATATT", "CCCTAACCCTAACTTCCCTAA", "CAAGCACGCAGCAATGCAGCT", "AATAACAATTGAATGTCTGCA", "ACACAAAATAGACTACGAAAG", "AATGCAAACAGTACCTAACAA", "ATAATAGGAGCTTAAACCCCC", "AGGACTCATAATAGTTACAAT", "ATCTACTCATCTTCCTAATTA", "CATCATCGAAGCCGCAAACAT", "AATATTGTACGGTACCATAAA", "CAGGTTTCTACTCCAAAGACC", "CAGCAGTGATTAACCTTTAGC", "CCATCAAGTACTTCCTCACGC", "CAGTACATAGTACATAAAGCC", "TGAGCCCTATTTATTACTCTC", "CCATACTAGTCTTTGCCGCCT", "ACAAATGATAACCATACACAA", "GCATCATCCCTCTACTATTTT", "TACAACCCCCACCCATCCTAC", "ATTAGACTGAACCGAATTGGT", "TACCCCCCAATTAGGAGGGCA", "TAGTCCTGTACGCCCTTTTCC", "TATTTAGCTGTTCCCCAACCT", "AAACTACCACCTACCTCCCTC", "CCCTACTTCTAACCTCCCTGT", "TAGTAACCACGTTCTCCTGAT", "CACTAGGATACCAACAAACCT", "CTCGCTTCCCCACCCTTACTA", "GCAACCTTCTGGGTAACGACC", "CCACATGAAACATCCTATCAT", "TGTAGCAGGAATCTTCTTACT", "TTTCAAAAAGGTATTAGAAAA", "TAGAAGAAAATCCCACAAACC", "AGCGAACTACTATACTCAATT", "AAGCAGCGGTGGGCCTAGCCC", "TGACATGACTTTCCAAAAAAC", "CAGTACTCTTGAAACTAGGCG", "GTCAAATCCCCTCTCGTCCCC", "ACCCCCTGGTCAACCTCAACC", "CAAAGCATAACATTTCGCCCA", "TAGGCACAGCCCTAAGCCTCC", "ACCGACTAATCACCACCCAAC", "AAAAATTATAGCCAAGCATAA");
       sort_probes(s2);
    }
    
     public static String sort_probes(List<String> probe_list) 
     {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
       
        String ar ="";
        String q = "'";
       for (int i=0; i<probe_list.size(); i++)
        {
            ar = ar + q + probe_list.get(i) + q;
            if(i<probe_list.size()-1)
            {
                ar=ar +",";
            }
        }
        
       String cq ="with [" + ar + "] as p unwind p as x call { with x match (pp: probe{probe:x}) with x, pp.variants[0] as var with x,var,toInteger(apoc.text.regexGroups(var,'\\\\d+')[0][0]) as pos return x as y,pos, var } with y,pos,var order by pos return collect(y) as sorted_probes";
       //,collect(pos) as pos,collect(var) as vars
       
        String r = mt_var.neo4jlib.neo4j_qry.qry_to_csv(cq).split("\n")[0];
        System.out.println(r);
        return r;
    }
}

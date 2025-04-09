/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class pos_multiple_pos_in_seq {
    @UserFunction
    @Description("gets to position of probe's homologous region in specified sequence")

    public String find_seq_positions(
        @Name("se_name") 
            String seq_name,
        @Name("probe") 
            String probe
    ) { 
        String s = get_positions(seq_name, probe);
        return s;
    }

    public static void main(String args[]) {
        // get_positions("poz235","CCCCCTCTA");
        get_positions("EU092880", "CCCCCTCTA");
    }

    public static String get_positions(String seq_name, String probe) {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        String cq = "MATCH (k:mt_seq{name:'" + seq_name + "'}) " +
                    "WHERE k.fullSeq <> replace(k.fullSeq,'" + probe + "','') " +
                    "WITH k, split(k.fullSeq,'" + probe + "') AS ss " +
                    "WITH k, ss, 0 AS ct UNWIND ss AS x " +
                    "CALL { WITH x, ct WITH size(x) AS sz RETURN sz } " +
                    "RETURN k.name AS seq, size(ss) AS repeatCt, collect(sz) " +
                    "ORDER BY repeatCt DESC, seq";

        String[] c = null;
        String[] p = null;

        try {
            c = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq).split("\n")[0].split(Pattern.quote("|"));
        } catch(Exception e) {
            return "";
        }

        try {
            p = c[2].replace("\"", "").replace("[", "").replace("]", "").split(",");
            if (p.length <= 1) {
                return ""; // no split occurred — no valid positions
            }
        } catch(Exception e) {
            return "";
        }

        int[] pos = new int[p.length];

        pos[0] = Integer.parseInt(p[0].strip());
        String s = String.valueOf(pos[0]);
        if (p.length > 2) {
            s = s + ", ";
        }

        for (int i = 1; i < p.length - 1; i++) {
            try {
                pos[i] = pos[i - 1] + probe.length() + Integer.parseInt(p[i].strip());
            } catch(Exception e) {
                return "";
            }

            s = s + pos[i];
            if (i < p.length - 2) {
                s = s + ", ";
            }
        }

        return s;
    }
}

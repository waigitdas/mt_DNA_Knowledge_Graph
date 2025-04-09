/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.variants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.HashMap;

import mt_var.neo4jlib.neo4j_qry;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class get_gvar {

    @UserFunction
    @Description("Variants in a specific probe computed by aligning the probe first with lookup probes.")
    public Map<String, Object> gvar_probe_variants(
        @Name("probe_id") Long probe_id
    ) {
        mtDmp mtDmp = get_variants(probe_id);
        if (mtDmp != null) {
            return mtDmp.toMap();
        }
        return null;
    }

    public static mtDmp get_variants(Long pid) {
        mtDmp mtDmp = new mtDmp();

        // Load Neo4j environment (custom method)
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();

        // Query to get the reference and test probe sequences
        String cq = "MATCH (p:probe) WHERE p.pid=" + pid + " RETURN p.ref_start_pos, p.ref_probe, p.probe";
        String[] c = mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited_str(cq)
                        .split("\n")[0]
                        .split(Pattern.quote("|"));

        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diffs = null;
        Long curr_pos = 0L;

        try {
            diffs = dmp.diffMain(c[1].replace("\"", ""), c[2].replace("\"", ""));
            curr_pos = Long.parseLong(c[0]);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

        StringBuilder sDiffs = new StringBuilder();

        for (int i = 0; i < diffs.size(); i++) {
            sDiffs.append(diffs.get(i).operation)
                    .append(": ")
                    .append(diffs.get(i).text.length())
                    .append(": ")
                    .append(diffs.get(i).text)
                    .append(" *** ");

            gfgVar v = new gfgVar();

            switch (diffs.get(i).operation) {
                case EQUAL:
                    curr_pos += diffs.get(i).text.length();
                    break;

                case INSERT:
                    if (i > 0 && diffs.get(i - 1).operation == DiffMatchPatch.Operation.DELETE) {
                        break;
                    }

                    v.pos = curr_pos - 2;
                    v.anc = "";
                    v.der = diffs.get(i).text;
                    v.name = v.pos + ".1" + v.der;
                    v.classification = "ins";
                    v.method = 1;

                    mtDmp.addVariant(v);
                    break;

                case DELETE:
                    v.pos = curr_pos;
                    v.anc = diffs.get(i).text;
                    v.der = "";

                    if (i + 1 < diffs.size() && diffs.get(i + 1).operation == DiffMatchPatch.Operation.EQUAL) {
                        v.name = v.anc + v.pos + "d";
                        v.classification = "del";
                        v.method = 15;
                        mtDmp.addVariant(v);
                    }

                    if (i + 1 < diffs.size() && diffs.get(i + 1).operation == DiffMatchPatch.Operation.INSERT) {
                        v.der = diffs.get(i + 1).text;
                    }

                    int df = v.der.length() - v.anc.length();

                    switch (Integer.signum(df)) {
                        case 0:
                            for (int j = 0; j < v.der.length(); j++) {
                                gfgVar nv = new gfgVar();
                                nv.pos = curr_pos + j;
                                nv.anc = v.anc.substring(j, j + 1);
                                nv.der = v.der.substring(j, j + 1);
                                nv.name = nv.anc + nv.pos + nv.der;
                                nv.classification = "sub";
                                nv.method = 2;
                                mtDmp.addVariant(nv);
                            }
                            break;

                        case 1:
                            for (int j = 0; j < v.der.length(); j++) {
                                gfgVar nv = new gfgVar();
                                nv.pos = curr_pos + j;
                                nv.der = v.der.substring(j, j + 1);
                                nv.anc = "";
                                nv.name = nv.pos + ".1" + nv.der;
                                nv.classification = "ins";
                                nv.method = 5;
                                mtDmp.addVariant(nv);
                            }
                            break;
                    }
                    break;
            }
        }

        mtDmp.setDmpSummary(sDiffs.toString());
        return mtDmp;
    }
}

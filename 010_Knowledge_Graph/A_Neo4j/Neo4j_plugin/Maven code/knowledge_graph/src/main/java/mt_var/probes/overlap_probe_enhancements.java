/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.probes;

import mt_var.neo4jlib.neo4j_qry;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.UserFunction;
import java.util.List;
import java.util.LinkedList;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

public class overlap_probe_enhancements {

    @UserFunction
    @Description("Enhance overlap probes with stitched reference and corrected alignment.")

    public String enhance_overlap_probes() {
        try {
            enhanceProbes();
            return "Overlap probes enhanced with stitched reference and alignment correction.";
        } catch (Exception e) {
            return "Error during enhancement: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        mt_var.neo4jlib.neo4j_info.neo4j_var(); // Ensure connection is established
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload(); // Refresh connection if needed
        
        overlap_probe_enhancements enhancer = new overlap_probe_enhancements();
        String result = enhancer.enhance_overlap_probes();
        System.out.println(result);
    }
    
    private static void enhanceProbes() {
        DiffMatchPatch dmp = new DiffMatchPatch();

        // Step 1: Retrieve all overlap probes
        String query = "MATCH (p:probe) WHERE p.type = 'overlap' " +
                       "RETURN p.pid, p.probe, p.orig_ref_start_pos, p.source";
        List<List<Object>> probes = mt_var.neo4jlib.neo4j_qry.qry_to_list(query);

        for (List<Object> probe : probes) {
            long pid = (long) probe.get(0);
            String testProbe = (String) probe.get(1);
            int origRefStartPos = ((Number) probe.get(2)).intValue();
            long sourceId = (long) probe.get(3);

            if (sourceId > 0) {
                // Step 2: Get the reference probe from the source position probe
                String refQuery = "MATCH (p:probe) WHERE p.pid = " + sourceId + " RETURN p.ref_probe";
                List<List<Object>> refResult = mt_var.neo4jlib.neo4j_qry.qry_to_list(refQuery);

                StringBuilder stitchedRef = new StringBuilder();

                if (!refResult.isEmpty()) {
                    stitchedRef.append((String) refResult.get(0).get(0));
                }

                if (stitchedRef.length() > 0) {
                    // ✅ Step 3: Get the reference sequence from the reference node
                    String refSeqQuery = "MATCH (ref:mt_seq{name:'AAAA_RSRS'}) RETURN ref.fullSeq";
                    List<List<Object>> refSeqResult = mt_var.neo4jlib.neo4j_qry.qry_to_list(refSeqQuery);
                    String refFullSeq = refSeqResult.get(0).get(0).toString();

                    // ✅ Step 4: Compute the stitched reference start position directly
                    int stitchedRefStartPos = refFullSeq.indexOf(stitchedRef.toString()) + 1;

                    // ✅ Step 5: Compute alignment shift using DMP (optional for checking)
                    LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(stitchedRef.toString(), testProbe);

                    int frameShift = 0;
                    for (DiffMatchPatch.Diff diff : diffs) {
                        if (diff.operation == DiffMatchPatch.Operation.INSERT) {
                            frameShift += diff.text.length();
                        } else if (diff.operation == DiffMatchPatch.Operation.DELETE) {
                            frameShift -= diff.text.length();
                        }
                    }

                    // ✅ Step 6: Store the corrected values in the overlap probe node
                    String updateQuery = String.format(
                        "MATCH (p:probe) WHERE p.pid = %d " +
                        "SET p.stitched_ref_probe = '%s', " +
                        "    p.stitched_ref_start_pos = %d, " +
                        "    p.left_flank_shift = %d",
                        pid,
                        stitchedRef.toString().replace("'", "\\'"), // Escape single quotes
                        stitchedRefStartPos,
                        frameShift
                    );

                    mt_var.neo4jlib.neo4j_qry.qry_write(updateQuery);

                    System.out.println("Updated probe: " + pid + ", adjusted start: " + stitchedRefStartPos);
                }
            }
        }
    }
}

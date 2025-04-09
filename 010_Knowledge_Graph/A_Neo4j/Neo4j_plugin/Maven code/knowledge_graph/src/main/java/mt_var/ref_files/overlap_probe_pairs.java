/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */

package mt_var.ref_files;

import java.util.List;

public class overlap_probe_pairs {
    int dels;
    int id1;
    int id2;
    int seq_ct1;
    int seq_ct2;
    int overlap_seq_ct;
    List<String> overlap_seqs;
    int ref_len1;
    int ref_len2;
    int ref_start_pos1;
    int ref_start_pos2;
    int ref_end1;
    int ref_end2;
    List<String> variants1;
    List<String> variants2;
    String probe1;
    String probe2;
    String dmp;
    String similar_variants;
    
     // getters and setters
    public int getDels() { return dels; }
    public int getId1() { return id1; }
    public int getId2() { return id2; }
    public int getSeq_ct1() { return seq_ct1; }
    public int getSeq_ct2() { return seq_ct2; }
    public int getRef_len1() { return ref_len1; }
    public int getRef_len2() { return ref_len2; }
    public int getRef_start_pos1() { return ref_start_pos1; }
    public int getRef_start_pos2() { return ref_start_pos2; }
    public int getRef_end1() { return ref_end1; }
    public int getRef_end2() { return ref_end2; }
    public List<String> getVariants1() { return variants1; }
    public List<String> getVariants2() { return variants2; }
    public String getProbe1() { return probe1; }
    public String getProbe2() { return probe2; }
    public String getDmp() { return dmp; }
    public String getSimilar_variants() { return similar_variants; }
    
    
}

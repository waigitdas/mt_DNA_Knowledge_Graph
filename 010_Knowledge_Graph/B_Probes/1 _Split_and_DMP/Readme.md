# Creating Probes

## Lookup Pobes

Lookup probes were created for each position in the reference sequence (Genbann NC_012920). This ws done using User Defined Function (UDF) mt_var.ref_files.lookup_probes. Lookup probes are 40-base sequence with a start position property enumerated from position 15 t0 16539, the latter’s end position is 16559.

⬇   <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/Z_Reference_files/rsrs_look_up_probes_240531.csv">view lookup file</a>



## Workflow creating <i>in silico</i> probes<

<ol type="A">
<li>Reference sequence 40-base probes splits a test sequence at homologous positions, leaving gap region with defined boundaries; 
<li>The gap sequence is extracted using a double split with the adjacent sequences; 
<li>The DMP algorithm computed variants; 
<li>The variants are memorialized in the graph individually and with the probe. In the example, concatenation of two rows is required, each having variants
</ol>

The image is created from screenshots of Excel worksheets. The latter has the Neo4j cypher queries generating the output
<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/Probe_workflow.jpg" width="200%" height="200%">

The UDF mt_var.ref_files.create_position_probes iterates each full GenBank sequence (test sequence), using the lookup probes and the split function to identify the boundaries of matching sequences which flank non-matching regions. The flanking boundaries enable extracting variant probes from the test sequence. The resulting variant “position probes” and the homologous reference region are exported to a pipe-delimited file. The file is further processed by the UDF mt_var.ref_files.create_loadable_pos_probes to aggregate rows and export a pipe-delimited file which is loaded into the knowledge graph to create an initial set of probe nodes. The UDF mt_var.ref_files. all_probe_variants_to_file which uses the diff­match­patch (dmp) algorithm and function logic to identify variants from the RSRS reference sequence within the variant probes. The variant probe and variant data are written to a pipe-delimited files.

## Probe Refinement

The position probes from different full sequences may have overlaps which confound downstream analytics. To address this, overlap probes are created using the UDF mt_var.ref_files.overlap_probes_create_from_pos_probes. This UDF loads the position probe pipe-delimited file into probe nodes with their type property set to position. The position probes overlapping others are identified and 40-base left and right flanking regions added. Because these flanking regions may differ between test sequences there can be multiple overlapping overlap probes created for each position probe. The overlap probes inherit the variants from their source but do not add any additional variants that might exist in the flanking regions. The overlap probes are written to a pipe-delimited file and then imported into probe nodes with the type property set to overlap. Both the position and overlap probe pipe-delimited files have mt_seq names on rows with the probe data. These files are used to create pipe-delimited files with sequence probe relationships for both position and overlap probes. The hierarchy inherent in this processing is memorialized by adding a probe id (pid) as a subsumed_by property to the smaller source probes which contain the full sequence of the larger overlap probe. The source probe id (pid) is added as a property of the overlap probe.  A probe_child relationship memorializes this hierarchy in the graph. A seq_probe_filtered relationship linked sequence nodes to their probe nodes with a null subsumed_by property and ref_start_pos>15.

⬇ The files create are available in the <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/Z_Reference_files">reference file folder</a>. They are utilized in loading the knowledge graph.


## The Probe UDF

For a more detailed technical discussion of the UDF accepts a probe identifier (pid), extracts it, aligns it using lookup probes and the processes it using diff0match-patch, please see this <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/B_Probes/1%20-Split_and_DMP/Probe_variant_udf.md">document</a> or refer directly to the <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/A_Neo4j/Neo4j_plugin/Maven%20code/knowledge_graph/src/main/java/mt_var/variants/get_probe_variants.java">UDF</a> 


## Fused Probes

For greater precision and sensitivity, probes are further process by fusing probes of individual sequences, as described <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence/3%20-%20Misalignments_Revealed_by_Probes">here</a>

<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/2_Probe_precision">NEXT ➡️</a>
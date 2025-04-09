# Mitochondrial DNA Knowledge Graph


## The Knowledge Graph
 
A mitochondrial DNA (mtDNA) knowledge graph (KG) was constructed in sequential steps in a Neo4j native graph database. GenBank full sequences were loaded into mt_seq nodes with their assigned haplogroup recorded as a property. The sequences were parsed using 40-base reference probes, producing in silico probes. Overlapping probes were stitched together and flanking regions from the full sequences added to ensure unique targeting to homologous positions in the reference sequence. The alignment of probes with the homologous reference sequence enabled comparisons using the diff-match-patch (DMP) algorithm, facilitating the annotation of probes with variants. Relationships between mt_seq, probe, and variant nodes formed the core of the initial graph.

The mtDNA haplotree was loaded into the KG and used to generate a derivative dependency graph, which replicated the haplotree’s branching structure while harmonizing haplogroup naming and pruning variants that were absent from the full sequence data. The knowledge graph was enhanced by memorialized analytical results within the KG by addition of new nodes, relationships, or properties.

<hr>


## Key Advantages
### 1. Alignment

Aligning reference and test sequences uses lookup probes to identify regions homologous in the test and reference sequences. Gaps where there is not homology with the test sequence have flanking regions of homology. Using a double split, the test sequence non-homologous region creates a set of in silico probes used throughout this project. <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/1%20_Split_and_DMP">Read More</a>



<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/Probe_workflow.jpg" width="150%" height="150%">


### 2 Precision

Precise alignment methods creates probes which uniquely identify the targeted position. The small deviations are due to frameshift from insertions and deletions. <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/2_Probe_precision">Read More</a>



<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/probe_precision.png" width="50%" height="50%">

### 3. Specificity
The legacy haplotree uses variants to define multiple haplogroups. In this image there is onr tan variant node (C152T) in the center related to numerous dnode (haplogroup) nodes. The specificity is very low.:

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/C152T_legacy_method.png"  width="30%" height="30%">

The Knowledge Graph  incorporates probes (red). This image is a subset of the lager knowledge graph.  The C152T is in numerous probes, each with different adjacent variants giving them specificity for single haplogroups or, as shown on the right, a clade with a parent haplogroup and descendant haplogroups: 

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/C152T_probe_haplogroups.png"  width="50%" height="50%">

<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/3_Probe%20specificity">Read More</a>


### 4. The Dispositive Triad

The dispositive triad (DT) is a set of3 knowledge graph relationships which align the haplotree model with sequences. Analytics of the parent-child sequences are discordant with the haplotree model. These observations definitively proves that the haplotree does not align with the real world of Genbank mitochondrial full sequences. <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence">Read More</a>

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/hg_parent_child_example.png"  width="40%" height="40%">



### 5. Structural Alignment and Timeline Distortion  

The relationship between path variants and probes suggests that these two signals are independent, with no significant correlation between them (Pearson r = 0.009, p = 0.969). This lack of alignment indicates that the divergence model based on path variants does not reflect the underlying structural reality captured by probes. Probes reveal the stability of hypervariable regions and elevates their utility.  <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/D_Graph_Characteristics/d_Haplotree_Timelines">Read More</a>


| **Variant vs. Probe Mutation Rates** | **Relative Inferred Time to Divergence** |
|--------------------------------------|------------------------------------------|
| <img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/probes_and_variants_by_level.png" width="70%" height="70%"> | <img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/inferred_timeline_probes_vs_variants.png" width="70%" height="70%"> |



### 6. The Method Makes the Model

The current haplotree model is a human construct used over several decades. The emergence of better data, new  analytic tools and new insights about mitochondrial biology compel the development of a new model. The knowledge graph and graph methods provide important clues and can contribute to the development of a network model.  

 <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/030_MitoNet_Topology">Coming Soon: Graph Topology</a>


<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes">NEXT ➡️</a>
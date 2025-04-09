# Dispositive Triad (DT)

## Overview  
The **Dispositive Triad (DT)** is a paradigm-shifting structural analysis that exposes the fundamental misalignment between traditional haplogroup-based phylogenetics and real sequence data. This triad demonstrates, with **dispositive evidence**, that the haplotree framework cannot be reconciled with empirical genetic relationships.  

 


 ## Key takeaways
<ol>
 <li>Probes are much more septicity than individual variants.</li>
 <li>Ony <b><span style="color:red">3% of sequencesT</span></b> have all the variants required for their haplogroup 
 <li><b><span style="color:red">1% of sequencesT</span></b> have the same number of path variants as sequences in their parent haplogroup</li>
 <li>Traversals for paths from Eve to nodes for the assigned haplogroup fail for lack of required variants</li>
 <li>In 1,559 instances, sequences retained mutations when haplotree back mutation predicted an ancestral state.</li>
 <li>The haplotree model is seriously flawed.</li>
</ol>

 <hr>

##  Why DT?  
For decades, the haplotree has been treated as the foundation of mitochondrial lineage analysis. Evaluating individual haplogroup assignments against the haplotree requirements is typically done and, while missing variants are seen, they are easy to ignore because they are relatively limited. The knowledge graph enables population level analytics where cumulative effects are more notable. In a population, sequences can be ordered against each other based on their haplogroup assignments and their parent-child relationships in the haplotree. The dispositive triad does this.   


## The Three Components of DT  

DT consists of three key relationship structures that, when analyzed together, reveal the inconsistencies of the haplotree:  

1. **Haplogroup Parent-Child Path (`path1`)**  
   - The conventional haplogroup tree structure.  
   - Represents how haplogroups are traditionally assigned.  

2. **Sequence Lineage Path (`path2`)**  
   - Direct sequence relationships based on assigned haplogroups.  
   - Shows how sequences actually relate through shared variants.  

3. **Sequence-to-Haplogroup Mapping (`path3`)**  
   - The reality check—mapping sequences to their haplogroup placements.  
   - Bridges the gap between sequence data and traditional classification.  

Together, these components **form an undeniable case** against the validity of the haplotree model.  

<hr>

### **DT Visualization Example**  

The following diagram represents an example of how DT structures expose inconsistencies:  

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/hg_parent_child_exampl2e.png" width="50%" height="5%">  

This triad shows how traditional haplogroup assignments fail to reflect the actual genetic relationships between sequences. **The Dispositive Triad (DT) forces a fundamental reevaluation of lineage classification.**  

<hr>

### **Cypher Query for DT Analysis**  

The following Cypher query constructs the **Dispositive Triad** in Neo4j, revealing the misalignment between sequence-based relationships and haplogroup-based classification:  

```cypher
MATCH path1=(dp:dnode)-[r:dnode_child]->(dc:dnode)
WHERE dp.name = 'H5' AND dc.name = 'H5a'  
WITH path1, dp, dc  

MATCH path2=(sp:mt_seq{name:'GQ983102', assigned_hg:'H5'})-[rpc:hg_parent_child]->(sc:mt_seq{name:'FJ460545', assigned_hg:'H5a'})  
WITH path1, path2, sp, sc, dp, dc  

MATCH path3 = (sx)-[rhg:seq_dnode]->(dx)
WHERE sx IN [sp, sc] AND dx IN [dp, dc]  
RETURN path1, path2, path3 LIMIT 25  

```

### Creating the hg_parent_child relationship 

The UDF mt_var.seq_pairs.create_hg_part_child_relationship creates pipe-delimited file with over 2M rows which is available as a <a hre="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/2_The_Dispositive_Triad/variants_dropped_in_traversal_20250220_235254.zip">zip file</a>. This UDF then loads from the  file to create the hg_parent_child relationship. The file data is also used to add properties to the hg_parent_child relationship for the dropped and added variants between the parent and child sequences. Placing these data in the relationship documents its provenance.   The dropped and added variants can then be analyzed without recreating them for each downstream analysis.   

To determine whether dropped and added transition changes were significant and given the large dataset and non-normal distribution, the Mann-Whitney U non-parametric method was used to compute highly significant differences. The U-statistics of -2,189,774 for dropped variants and -774,216 for added variants had corresponding Z-scores of -287,575.79 and -119,501.46. The probability of these differences occurring by chance is effectively zero.

| Variant Transition | N        | U Mean ± SD  | Z-score      |
|-------------------|----------|--------------|-------------|
| Dropped          | 2,189,774 | 338 ± 7.62   | -287575.79  |
| Added            | 774,216   | 242 ± 6.48   | -119501.46  |



## Analytics Enabled


## 1. Haplogroups with no defining variants

⬇ There are 228 haplogroups with no branch-defining variant and 818 sequences are assigned to these haplogroups.  Importantly, this is one scenario where differences between the phylotrees of Haplogrep3 and FTDNA contribute to missed haplogroup assignments. The dispositive triad shows added variants which might be used as defining variants.    
 <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/2_The_Dispositive_Triad/haplogranch_without_variant_20250322_091105.xlsx">Excel file</a>

## 2. Sequences haplogroup assignments without defining variants

Missing variants are identified by subtracting the **sequence variants** from the **path variants**:  

```cypher
apoc.coll.subtract(path_variants, sequence_variants)
```


### ⬇  Key Observations  

- **Only 3%** of GenBank sequences contain all the defining variants required for their assigned haplogroup. <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/2_The_Dispositive_Triad/hg_missed_variants_20250224_162048.xlsx">Excel file</a>   (Tab 2)
- **Nearly all haplogroups (99%)** have assigned sequences missing some required variants. (Tab 3)  
- There are 1530 distinct variants involved. (Tab 4)   
- **C16189T is the second most commonly missed variant** (Tab 4) and is also discussed in detail <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence/1_Graph_traversal_failures">here</a>  
- **"Missing" variant regions often contain mutations**—just not the ones expected to be there. See the discussion on **back mutations** <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/D_Graph_Characteristics/c_Back_mutations">here</a>.  


## 3. Incongruent variant patterns

The full triad using three relationships creates sequences in a haplogroup parent-child relationship. These sequence pairs enable comparisons between haplotree and sequence variant sets. Haplotree child branches have all the variants of their parent and are defined by new variants, the branch_variants property in the knowledge graph. Likewise, child sequences should follow this pattern.     
 
 Sequence pairs are connected by 221,4992 hg_parent_child relationships, which involve 35,805 unique child sequences. There are 13,542 hg_parent_child relationships with 4,807 child sequences which shared a child haplogroup defining variant with the parent sequence. That is, a parent sequence variant was not novel in the child sequence.  T

⬇  This Excel file shows the <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/2_The_Dispositive_Triad/incongruity_affecting_haplogroup_assignment_20250321_092229.xlsx">incongruent patterns</a>. There are 415 unique haplogroup pairs involving many haplotree clades.

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/hg_parent_childanomalous_variante2.png" width="50%" height="50%">


Back mutations should restore the ancestral state. In a hg_pair, back mutations in the parent sequence should not be present in the child sequence. There are 144,280 instances where a parent dnode has a back mutation and , of these 13,559 child sequences retained the mutation. 

⬇  This Excel file shows the <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/2_The_Dispositive_Triad/dispositive_evidence_back_mutation_retention_20250321_150533.xlsx">DT analyses of retained back mutations</a>. 


<!--
## 4. Triad descendant 
 
 There is a progressive loss of variants in descend paths.
-->

<hr>

## What DT Proves  
- The haplotree structure is not aligned with the data of sequences assigned to its haplogroups.branches
- The tree **fails as a predictive model** when compared to real sequence data.  
- A new framework is needed—one that embraces sequence-driven lineage reconstruction.  

---

**DT is not just an analysis—it is the evidence that demands a new approach.**  


<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence/3%20-%20Misalignments_Revealed_by_Probes">NEXT ➡️</a>

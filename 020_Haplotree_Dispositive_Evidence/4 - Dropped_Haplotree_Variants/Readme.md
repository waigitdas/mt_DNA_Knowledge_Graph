# Dropped Variants
<h3><strong><em>Only 1% of sequences inherit all variants from haplogroup parent sequences</em></strong></h3>


This folder has files evaluating missing variants from the perspective of parent-child haplogroups in the haplotree. Sequences have assigned haplogroups. This assignment was done using the variant lists for the sequence. The dnode tree is derived from the haplotree, with back mutations removed. Each sequence is linked to the haplotree by a seq_dnode relationship. The haplotree hierarchy is rendered with the dnode_child relationship. We can then create a hg_parent_child relationship which links together sequences which have haplogroups that are in a parent-child relationship in the haplotree. 

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/hg_parent_child_example.png" width="50%" height="50%">

We can now compare the parent and child sequences using set algebra. By subtracting  the child path variants from  the parent path variants we see any variants that were dropped ... that is, not passed down as would be expected. There are 2,215,069 possibe hg_parent_child relationship. The comparison reveals 2,190,125 sequence pairs with dropped variants. Thus, only 1.12% of sequence pair have the expected outcome. The dropped variants are detailed in the Excel in this folder.  In 35% of child sequences there were more variants than the parent sequence after excluding the path defining variants; that is, there are available variants that are not in the logic of defining branches.

<a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/4%20-%20Dropped_Haplotree_Variants/seq_child_missed_20250402_061845.xlsx">Excel File</a>

The haplotree itself is robust, with inconsequential dropped variants. <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/4%20-%20Dropped_Haplotree_Variants/dnode_tree_no_dropped_variants_20250402_054738.xlsx">Excel File</a>


<hr>

# Set Algebra of Dropped Path Variants 

## Overview
This document explains the set algebra used to analyze dropped variants between parent and child mitochondrial DNA (mtDNA) sequences. The analysis is based on set operations performed in a Neo4j knowledge graph.

## **Set Definitions**
- **P**: Parent sequence variants (`parent.all_variants`)
- **C**: Child sequence variants (`child.all_variants`)
- **P_path**: Path-specific variants in the parent (`dp.path_variants`)
- **C_path**: Path-specific variants in the child (`dc.path_variants`)
- **P_rel**: Parent’s relevant variants (i.e., mutations found in the parent’s path)
  
  \[ P_{rel} = P \cap P_{path} \]
  
- **C_rel**: Child’s relevant variants (i.e., mutations found in the child’s path)
  
  \[ C_{rel} = C \cap C_{path} \]
  
- **D_raw**: Dropped path variants (mutations present in the parent but absent in the child)
  
  \[ D_{raw} = P_{rel} \setminus C_{rel} \]
  
- **S**: Start-back mutations (known reversions that should not be considered lost)
- **D_corr**: Corrected dropped path variants (excluding expected reversions)
  
  \[ D_{corr} = D_{raw} \setminus S \]
  
## **Filtering Criteria**
The final dataset includes only cases where the corrected dropped path variant set is non-empty:

\[ |D_{corr}| > 0 \]

## **Visualization**
A Venn diagram is used to represent these sets:

- **Parent Relevant Variants**: Variants found in both the parent’s sequence and path.
- **Child Relevant Variants**: Variants found in both the child’s sequence and path.
- **Path Variants**: All path-associated variants.
- **Dropped Path Variants**: Variants lost in the child.
- **Corrected Dropped Path Variants**: True losses after excluding start-back mutations.

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/dropped_variants.png" width="70%" height="70%">

## **Conclusion**
This set-theoretic approach provides a precise way to analyze dropped variants in mtDNA sequences, ensuring that only meaningful mutation losses are considered. The methodology helps clarify evolutionary changes and refine haplogroup assignment processes.



<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/030_MitoNet_Topology">NEXT ➡️</a>

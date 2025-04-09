# Graph Traveral Failures
 
Haplogroup assignments were provided by the <a href="https://www.mitomap.org/MITOMAP">Center for Mitochondrial
& Epigenomic Medicine</a> at the Children's Hospital of PhiladelphiaMitomap. They used the <a href="https://github.com/genepi/haplogrep3">Haplogrep3 tools</a>. The assignments were added as a property to the Genbank sequence nodes. 

This project intended to replicate these assignments using graph methods. That effort failed. The Excel workbooks in this folder Used traversals of the dnode (haplotree) paths from Eve to the assigned haplogroup node. show:

<ol>
 <li>Traversals to the assigned haplogroup node were successful foe only 34% of the Genbank sequences.</li>
 <li>The C16189T was a common "missed variant" and ignoring this would increase the success rate to ~60%</li>
 <li>Sequences whose haplogroup required C169185 often did not have it, but was frequently annotated with the closely adjacent T16187d and 16188.1T variants </li>

</ol>

The traversal for Genbank ON597788, whose haplogroup is H30a, is interrupted at L2'3'4'5'6'7. This is because it does not have the variants T16189T, T16187C
 and T8200C (shown in red). 

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/haplotree_traversal_with%20back_and_missing_variants.jpeg" width="50%" height="50%">
 
 <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/1_Graph_traversal_failures/ON597788_traversal_path_variants_20250402_234250.xlsx">Excel File</a>

The C16189T variant has long been recognized as a problematic marker in mitochondrial DNA studies due to its high recurrence and frequent misidentification in automated haplogroup classification tools. In the knowledge graph analysis, discrepancies in Haplogrep3’s variant calls flagged this issue, as the tool identified C16189T where the raw sequence data instead revealed 5,914 probes with the variants <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/1_Graph_traversal_failures/C16189T_report_20250312_123411.xlsx">T16187d and 16188.1T</a>. This suggests that the widely accepted presence of C16189T in haplogroup definitions may be, at least in some cases, an artifact of alignment errors rather than a true defining mutation. The implications extend beyond this single variant—if a fundamental marker can be repeatedly misclassified, it raises broader concerns about the reliability of haplogroup assignments and the validity of the haplotree itself. Rather than an isolated error, this case serves as further evidence that the haplotree and its underlying classification tools are built upon assumptions that do not always hold up under closer scrutiny.

.


| Variants Combination               | Count  |
|-------------------------------------|--------|
| T16187d                            | 8059   |
| T16187d, 16188.1T                   | 35486  |
| C16189T                            | 206    |
| T16187d, C16189T                    | 77     |
| 16188.1T                           | 94     |
| T16187d, C16189T, 16188.1T          | 6      |


<a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/1_Graph_traversal_failures/C16189T_advacent_seq_variants.xlsx">Excel File</a>


<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/C16189T_Veen_diagram.png" width="50%" height="50%">


⬇ The <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/1_Graph_traversal_failures/'C16189T'_T16187d'_'16188.1T_case%20study.xlsx">case study</a> of 6 sequences with 3 variants in the 16189 region shows sequence which have C16189T and two closely adjacent and many other variants. These complex matching challenges were handled correctly by the knowledge graph <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/1%20_Split_and_DMP">split and DMP methods</a>. 


<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence/2_The_Dispositive_Triad">NEXT ➡️</a>

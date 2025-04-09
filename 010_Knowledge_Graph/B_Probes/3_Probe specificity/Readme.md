## Probe Specificity




The tree model defines a single branch using individual variants: Is there a better way?
`
- When analyzed as **isolated** mutations, they appear in multiple haplogroups.
- When analyzed **as a group**, they provide better specificity.
- This suggests that the reliance on single variants is an **oversimplification**, obscuring actual evolutionary relationships.


### C152T Case Study

This intuitive Cypher query use a set algebra function to find C152T ... 

MATCH (p:dnode) where apoc.coll.contains(p.path_variants,'C152T') RETURN count(*) 

It counts 4,237 haplotree branches with C152T as a path variant. The individual C162T variant has very little specificity. 

Probes have much more specificity, which can be appreciated in this image with a sample of probes: (red) containing the C152T variant (tan) and connected to dnode nodes (haplogroups) in one haplogroup or in a clade with multiple descending branches from a common ancestral branch:


<div style="border: 1px solid #ccc; padding: 10px; text-align: center; width: 80%; margin: 10px auto;">
    <p style="margin-top: 5px; font-style: italic; color: #555;"><b>Figure 1:</b> The variant C152T (tan node) is used to define many haplotree branches (purple). Probes (red) subsume adjacent variants, giving them much greater specificity. Here the probes connect to a single haplogroup or, on the right, to a set of haplogroups in a single clade.</p>  <img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/C152T_probe_haplogroups.png"  width="50%" height="50%">
  
</div>

This chart shows a few rows from the Excel file, each with a different probe and set of adjacent variants including C152T.

| variant | probe | haplogroup | probe variants |
|--------|--------|---------|------------|
| C152T  | 1000121 | H8b   | [G73A, C114T, C152T] |
| C152T  | 1000729 | Q1d   | [T89C, C152T, T199C] |
| C152T  | 1001238 | H14   | [C152T, G185A, C195T] |
| C152T  | 1003523 | F1a1a | [C146T, C152T, C195T] |


⬇ *Download the complete file: <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/B_Probes/3_Probe%20specificity/C152T_fused_probe_specificity_for_haplogroups_20250401_085923.xlsx">C152T case study</a>



<hr>


### C152T Probe specificity and haplogroup assignments

A C152T case study looked at the 2,019 probes which subsumed the C152T variant. This number and thee specificity results from numerous patterns of other adjacent variants in the probes.  

<div style="border: 1px solid #ccc; padding: 10px; text-align: center; width: 80%; margin: 10px auto;">
    <p style="margin-top: 5px; font-style: italic; color: #555;"><b>Figure 2:</b> The probes specificity is shown in the Venn diagram and table. Most (77%) probes are specific for one haplogroup and another 5.5% are specific for a single clade.</p>  <img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/C152T_probe_specificity.png"  width="50%" height="50%">
  
</div>



| Category                                             | Count | %       |
|------------------------------------------------------|-------|---------|
| Probes Specific for 1 Haplogroup                     | 1559  | 77.22%  |
| Probes Specific for 1 Path with Multiple Haplogroups | 111   | 5.50%   |
| Other Probes (Non-Specific)                          | 349   | 17.29%  |
| Total Probes                                         | 2019  | 100.00% |

For further discussion -see <a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/5_Probes_Challenge_Haplotree_Assumption">Probes Challenge Haplotree Assumptions</a>



<hr>





<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/B_Probes/4_Sequence_Alignment">NEXT ➡️</a>

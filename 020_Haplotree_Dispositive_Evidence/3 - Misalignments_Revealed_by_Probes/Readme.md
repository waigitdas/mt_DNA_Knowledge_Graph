## Fused Probe

### Construction of Fused Probes

Fused probes are created  by  combining overlapping probes for a single sequence. The stitching is done by the UDF mt_var.load.load_fused_probes.  The fusion process is designed to increase specificity and reduce confounding homologies with other sequences which are less specific. 

Fusion is done at the individual sequence level irrespective of its variants, assigned haplogroups or other sequences.  The same fused probe may be independently created using more than one sequence.

### Incremental Value of Fused Probes

Fused probes offer greater specificity than the source probes from which they are constructed. Earlier generations of probes (e.g., position or overlap probes) often lacked sufficient resolution. They generated too many hits across unrelated sequences and failed to uniquely identify true evolutionary changes.

In contrast, fused probes are longer and context-aware. They reduce redundancy by collapsing overlapping signals and offer more reliable markers of shared ancestry.


### Fused Probe Specificity

Specificity requires clear definitions. The following example is instructive.

The Cypher returns the results for a single fused probe:

> MATCH (p:fused_probe)-[:fused_probe_dnode]->(d:dnode{name:'A10'}) WHERE  'C10094T' in p.variants  
 WITH p, collect(distinct  d) AS branches with p, branches WHERE size(branches) = 1 with p.pid AS fused_probe 
 MATCH path=(s:mt_seq)-[r:seq_fused_probe]->(p:fused_probe{pid:fused_probe})  
 with p, s, apoc.coll.intersection(p.variants, s.all_variants)  as ssp with s,p,ssp 
 match (d:dnode{name:s.assigned_hg}) with s, p, d, ssp, apoc.coll.intersection(p.variants, d.path_variants) as pdv  
 return p.pid, s.name, s.assigned_hg, ssp , pdv,  p.variants  order by s.assigned_hg 


where ssp is the intersection of probe and sequence variants and pdv is the intersection of the probe and dnode (haplogroup) variants. The set intersection is the shared variants. The probe variants are in the last column.


| p.pid | s.name   | s.assigned_hg | ssp        | pdv        | p.variants |
|-------|----------|---------------|------------|------------|------------|
|1240946 | GU122995 | A10           | [C10094T]   | [C10094T]   | [C10094T]   |
|1240946 | HM569228 | A10           | [C10094T]   | [C10094T]   | [C10094T]   |
|1240946 | KM101896 | A10           | [C10094T]   | [C10094T]   | [C10094T]   |
|1240946 | MG660641 | A10           | [C10094T]   | [C10094T]   | [C10094T]   |
|1240946 | MN628133 | A10           | [C10094T]   | [C10094T]   | [C10094T]   |
|1240946 | MF588858 | C             | [C10094T]   | []          | [C10094T]   |
|1240946 | MT079034 | H1            | [C10094T]   | []          | [C10094T]   |
|1240946 | MT079110 | H1            | [C10094T]   | []          | [C10094T]   |
|1240946 | MT079111 | H1            | [C10094T]   | []          | [C10094T]   |
|1240946 | JN214441 | L1b1a8        | [C10094T]   | []          | [C10094T]   |
|1240946 | MN595883 | U2b2          | [C10094T]   | []          | [C10094T]   |



Probe1240946 has a homologous region in 11 sequences with several assigned haplogroups However, the probe variants match the path variants of only one of these haplogroups. In this sense, for 5 sequences, the probe is specific for A10. The variant C10094T is not a defining variant of the haplogroups assigned to the other 6 sequences. The haplotree is naive about the knowledge graph association of all these 11 sequences with the same fused probe. 

 <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/3%20-%20Misalignments_Revealed_by_Probes/single_fused_probe_misaligned_with_hg.xlsx">Excel File</a>


For clarity we can illustrate this with a Cypher query using provenance and degree to limit the display to the relevant nodes and relationships. Sequences are yellow nodes, the probe red and the dnodes (haplogroups) in purple.

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/fused_probe2.png"  width="50%" height="50%">>

The haplotree has a  gap in its logic illustrated by [] in table column 5, which is an empty set because there is no intersection of the probe and haplogroup variant sets. The probe has a relationship that is not represented in the haplotree because it is an oversimplification of the reality provided by sequence data.  

### Exposed New Relationships

Another Cypher query removes the degree filter which reveals all probes linked to the 11 sequences including the dense central cluster and also all probes linked to only one of these sequences. The knowledge graph exposes additional relationships, not shown here, to the peripheral probes to additional sequences. These will be explored further the the next topology phase of this project. The reticulated pattern of descent from parent nodes is very different from the phylotree model. 


MATCH path=(s)-[r:seq_fused_probe]->() where s.name in ["GU122995","HM569228", "JN214441", "KM101896", "MF588858", "MG660641", "MN595883","MN628133", "MT079034", "MT079110", "MT079111"]   RETURN path 


<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/fused_probe3.png" width="50%" height="50%">


## Scope of Misalignment

The misalignment analytics were expanded to the entire sample. There are 37,877 fused probes or 77.7% of the total specific a single haplogroup and another 4.4% with a single clade. There are 1,195 probes aligned with more than one haplogroup and homologous to more than one sequence. The probe sequence count was more that the sequences homologous to the probe for 1,190  probes (99.6%). A clade is defined by a common ancestral haplogroup branch and the probe , on average maps to 20% of the descendant branches.

<a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/3%20-%20Misalignments_Revealed_by_Probes/fused_probe_specificity_for_haplogroups_20250401_234952.xlsx">Excel File</a>


We can also look only at A10, the haplogroup in our case study. This shows us that probe 124094 is one of 62 probes with homology to sequences with assigned haplogroup A10. The are 9 probes that are truly specific for A10 and only have homologies with sequences assigned haplogroup A10.


<a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/020_Haplotree_Dispositive_Evidence/3%20-%20Misalignments_Revealed_by_Probes/A10_case_study_20250402_121343.xlsx">Excel File</a>>

## Conclusion

### The knowledge graph exposes:
 - The signal is consistent across sequences and haplogroups
 - misalignment of sequences to the haplotree
  - clustering not represented in the haplotree
   - the reticulated association of sequences assigned different haplogroups


   This is further dispositive evidence that the haplotree is flawed. 


<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence/4%20-%20Dropped_Haplotree_Variants">NEXT ➡️</a>

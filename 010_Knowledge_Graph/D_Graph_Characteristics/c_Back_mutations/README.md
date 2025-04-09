# Back Mutations

## Key takeaways are:
<ol>
 <li>Backmutation do not exist in the sequence annotations but are within the haplotree</li>
 <li>Back mutations were pruned from the dnode tree but a property was added at the node where they were removed to document the provenance of that change. </li>
 <li>Back mutations impact 21% of the haplotree branches </li>
 <li>224 haplotree branches were defined only by back mutations and pruning them left no defining mutations. At these node there are 818 sequences assigned the haplogroup. </li>
 <li>There are 79,092 probe for the homologous regions of back mutations. They have annotated variants differing from the reference sequence. /li>
 <li>Back mutations are responsible for many failed graph traversals fromEve to the assigned haplogroup nde.</li>
</ol>

## The back mutation conundrum 

Back mutations have traditionally been used in phylogenetics to describe variants that supposedly revert to an ancestral state. However, our analysis challenges this assumption, revealing that back mutations are often **artificial constructs** rather than true evolutionary reversions. By leveraging probe-based analysis, we demonstrate that these inferred reversions are **unnecessary** because homologous probes already capture real, observable variation at the same genomic positions. More critically, the very concept of a back mutation assumes that the original mutation was a singular evolutionary event rather than a recurrent or coexisting variant in the population. This assumption oversimplifies evolutionary history by imposing a directional model onto complex mutational processes. Instead of reconstructing hypothetical reversions, we propose a **data-driven approach where probes replace back mutations**, offering a more transparent, empirical foundation for phylogenetic analysis. This eliminates ambiguity, aligns with real sequence data, and redefines how we interpret mitochondrial evolution.  


<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/haplotree_traversal_with%20back_and_missing_variants.jpeg" width="50%" height="50%">



In the data there is no difference between back mutations and missing variants. Both do not exist The analytical difference is that back mutations would persist in downstream branch definitions whereas missing mutations may not. 



<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/D_Graph_Characteristics/d_Haplotree_Timelines">NEXT ➡️</a>
## The DNODE Tree

The dnode tree was created from the phylotree with back mutations removed from the required variants but retained as a separate property. Name of variants were harmonized to assure fidelity throughout the knowledge graph.

⬇ All the dnode tree branches are shown with their variants and sequence counts in this <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/C_Haplotree/haplotree_all_branches_20250306_004609.xlsx">Excel</a>. In other rendering there may be fewer branches because some branches have no sequence.s


The haplotree (dnode tree) traversal from the root RSRS (Eve) node on paths with a 3-hop descent. The Cypher query producing this result is intuitive:

MATCH path=(d:dnode{lvl:0})-[r:dnode_child*0..3]->() RETURN p ath 

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/haplotree_root_3_hop_desc.png" width="70%" height="70%?">



### Tree Reconciled with Sequences

⬇ The <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/C_Haplotree/haplotree_all_branches_20250401_095342.xlsx">dnode tree report</a> has an <a href="https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&ved=2ahUKEwio9Ja36Lj4AhVbpIkEHUuWCMcQFnoECAIQAQ&url=https%3A%2F%2Fwww.cse.iitb.ac.in%2Finfolab%2FData%2FCourses%2FCS632%2F2007%2FPapers%2Fordpath.pdf&usg=AOvVaw3lQdMD9bzckkhnt1cZVtLY">ORDPATH</a> sorted list of the branches with metadata. The Cypher query and explanations are a the bottom to the worksheet. There are 4.731 branches matched to the 50,000 Genbank sequence with the assigned haplogroup. Set algebra compares the variant lists of the haplotree branch defining variants to the sequence variants. There is a 96% concordance and the discorndant variants (4%) are reveled.    

<hr>

## Missed variants

Set algebra identifies discordance between the haplotree and sequences variants. Neo4j has <a href="https://neo4j.com/docs/cypher-manual/current/values-and-types/lists/">List</a> and <a href="https://neo4j.com/blog/developer/the-power-of-the-path-1/">Path</a> data types and a set of apoc.coll.* functions implementing set algebra. The <a href="https://neo4j.com/docs/apoc/current/overview/apoc.coll/">collect function</a> creates a List by aggregating variants    

### 1. Subtracting  sequence variants from hapolotree path variants

The mt_seq nodes are created using the Genbank name and the full sequence. Variants identified bi project analytics are memorialized as the all_variants property of mt_seq nodes. 

The dnode nodes are created with the list of branch defining variants which is memorialized as the branch_variants property. The collect function creates a list of variants during graph traversals; this is the cumulative   

The knowledge graph was augmented by adding a  relationship between the mt_seq and dnode nodes (seq_dnode). We can then directly query to retrieve the sequence and haplotree variant lists and use set algebra to assess concordance.  

### 2. Dispositive triad: subtracting  child from parent sequence variant lists

Sequences have assigned haplogroups and these can be aligned with the haplotree. The knowledge graph was enhanced by creating the hg_parent_child relationship.  The paired sequences each have their variant lists which can be directly compared using set algebra. 

## Knowledge Graph Property Enhancements

The analytics just discussed produce results which can be memorialized in new properties. These properties enable thw analytics discussed in other sections of this repository. 

- mt_seq node 
    - hg_missed_var 
- hg_parent_child relationship
    - dropped_path_variants
    - all_dropped_variants
    - added_variants  

The capability of relationships to have properties in a key feature differentiating a native graph data base from a relational data base where joins  have not metadata.

<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/D_Graph_Characteristics">NEXT ➡️</a>
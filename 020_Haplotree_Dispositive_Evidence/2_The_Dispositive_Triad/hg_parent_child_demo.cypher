MATCH path1=(sp:mt_seq{name:'MG272663'})-[rhpc:hg_parent_child]->(sc:mt_seq{name:'MG272956', assigned_hg:'M74a'}) 
with path1,sp,rhpc, sc 
match path2=(dp:dnode)-[r:dnode_child]->(dc:dnode) where dp.name=sp.assigned_hg and dc.name=sc.assigned_hg
with path1,path2, sp,sc, dp, dc
match path3 = (sx)-[rhg:seq_dnode]->(dx) where sx in [sp, sc] and dx in [dp,dc]
RETURN path1, path2, path3
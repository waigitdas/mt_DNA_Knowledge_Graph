## DiffMatchPatch-Based Variant Detection in mtDNA

This document describes the logic behind the user-defined function (UDF) `get_probe_variants`, which extracts and classifies variants in mitochondrial DNA (mtDNA) sequences by aligning a reference sequence with a test sequence using the DiffMatchPatch (DMP) algorithm.

### Overview
The `get_probe_variants` UDF analyzes aligned sequences to detect insertions, deletions, and substitutions in mtDNA. The method is robust because it starts with known aligned positions, ensuring that the initial and final alignment points are correct. This allows the method to accurately identify changes without ambiguity about the sequence boundaries.

### Key Steps
1. **Retrieve probe data:**
   - The function queries the Neo4j database for the reference sequence (`ref_probe`) and the test sequence (`probe`) for a given `probe_id`.
   - The starting position (`ref_start_pos`) is also retrieved to establish the alignment reference point.

2. **Alignment using DiffMatchPatch:**
   - The DMP algorithm is applied to compute differences between the reference and test sequences.
   - The output from DMP includes a sequence of operations (`EQUAL`, `INSERT`, `DELETE`) that describe how the test sequence differs from the reference.

3. **Iterative Variant Parsing:**
   - The method processes the DMP output operation-by-operation.
   - **Equal:** If an `EQUAL` operation is found, the current reference position is incremented.
   - **Insert:** If an `INSERT` follows a `DELETE`, the function records it as a substitution.
     - If the `INSERT` is longer than the `DELETE`, the additional characters are treated as an insertion.
   - **Delete:** If a `DELETE` operation follows an `EQUAL`, it is classified as a simple deletion.
     - If a `DELETE` precedes an `INSERT`, it is classified as a substitution.
     - Sequential deletions are handled by iterating until the exact starting and ending positions are determined.

4. **Classification of Variants:**
   - Substitutions are recorded with the reference nucleotide, derived nucleotide, and position.
   - Insertions are recorded with the inserted nucleotide and the reference position.
   - Deletions are recorded with the deleted nucleotide and the reference position.

5. **Output:**
   - The function returns a pipe-delimited string with the following fields:
     - `name`: Variant name
     - `probe_id`: ID of the probe
     - `pos`: Reference position
     - `anc`: Ancestral nucleotide(s)
     - `der`: Derived nucleotide(s)
     - `classification`: Type of variant (`sub`, `ins`, `del`)
     - `method`: Classification method
     - `diffs`: Full DiffMatchPatch output

### Why It Works
- The method starts with known aligned positions, ensuring that the reference and test sequences are properly aligned.
- Sequential deletions are resolved iteratively, allowing precise determination of start and end positions.
- The combination of DMP and iterative parsing allows for accurate detection of complex variants, including insertions and substitutions.

### Example Output
Example from UDF `get_probe_variants`:
```plaintext
A93d|1000783|93|A||del|15|EQUAL: 13: CGATAGCATTGCG *** DELETE: 1: A *** EQUAL: 1: G *** DELETE: 1: A *** INSERT: 2: GC ***
```
This output shows a deletion at position 93, with the ancestral nucleotide being `A` and no derived nucleotide, classified as a deletion (`del`) using method `15`.

### Conclusion
The `get_probe_variants` UDF provides an accurate and detailed classification of mtDNA variants by combining known alignment points with the DMP algorithm and an iterative parsing approach.


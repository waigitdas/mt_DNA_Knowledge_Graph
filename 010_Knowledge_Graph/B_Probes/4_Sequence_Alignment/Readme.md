
## Sequence Alignment Solutions

### Background
Aligning mitochondrial DNA (mtDNA) sequences to a reference genome is challenging due to the complexities introduced by insertions, deletions, and mismatches.

### Overall Process
1. **40-Base Lookups:**
   - We start by using 40-base lookup probes to identify aligned sequences in the reference genome.
   - Gaps between aligned sequences are identified as potential variation sites.

2. **Filling Gaps and Creating Position Probes:**
   - Adjacent aligned sequences are used to fill gaps.
   - A double-split approach is used to create position probes, ensuring that the alignment reflects the correct reference context.

3. **Creating Position Probes:**
   - Position probes are created by identifying the exact position within the reference genome.
   - Each position probe retains key properties:
     - `orig_ref_start_pos` – starting reference position.
     - `ref_len` – length of the extracted reference sequence.
     - `original_probe` – aligned sequence.
   - Position probes serve as the foundation for generating overlap probes.

4. **Creating Overlap Probes:**
   - Most position probes were not specific enough to distinguish between similar sequences.
   - To achieve higher specificity, position probes were stitched together to create overlap probes.
   - The overlap probes retain the following key properties:
     - `subsumed_by` – identifies the position probes that were merged into the overlap probe, ensuring that those position probes are not used directly in later analyses.
     - `source` – a list of `probe_id` values for the position probes that were stitched together to form the overlap probe.
     - `stitched_ref_probe` – Combined reference sequence from the stitched position probes
     - `stitched_probe_start` – Starting position of the stitched reference probe..
     -`stitched_probe_length`– Length of the stitched reference probe.
   - This approach allowed us to increase specificity and resolve ambiguities in alignment.



### Provenance
Provenance is critical to the accuracy and reproducibility of the alignment process. Throughout all these steps, we retain detailed information about the origin of each probe and how it was assembled. This complete provenance enables:
- **Recapitulation:** The alignment process can be fully reconstructed, allowing validation and troubleshooting.
- **Validation:** Computed variants can be cross-referenced with the original probe definitions to confirm accuracy.
- **Quality Control:** Provenance allows robust quality control by ensuring that computed variants align with their expected genomic context.

### Alignment Strategy
1. **Reference Sequence Extraction:**
   - The reference sequence is extracted using `orig_ref_start_pos` and `ref_len`.
   - This ensures that the alignment window is consistent with the reference genome.

2. **Substitution Handling:**
   - Substitutions are computed relative to the extracted reference window.
   - The computed positions are corrected by adding `orig_ref_start_pos` to reflect genomic coordinates.

3. **Insertion Handling:**
   - DMP reports insertions at the base **before** the insertion point.
   - Positions are adjusted by adding an additional `+1` to match the probe definition.

### Outcome
- Nucleotide changes align perfectly with the reference sequence.
- Substitution positions are consistent with probe definitions.
- Insertion positions are correctly aligned after the `+1` adjustment.
- Alignment and computed variants are fully consistent with the probe properties.

### Novelty and Innovation
This method represents a significant advance over previous approaches to mtDNA alignment:
- **Gap Resolution:** The use of 40-base lookup probes to identify and resolve gaps ensures that no information is lost during alignment.
- **Double-Split Strategy:** The double-split approach to create position probes allows for high-resolution alignment while preserving consistency with the reference genome.
- **Stitching for Specificity:** Creating overlap probes from position probes increases specificity, resolving ambiguities that were not addressed in prior methods.
- **Diff-Match-Patch (DMP):** DMP provides a powerful mechanism to detect and resolve both substitutions and insertions, ensuring precise positional reporting.
- **Brute-Force Accuracy:** Unlike previous statistical or probabilistic methods, this approach relies entirely on direct alignment and position tracking, eliminating uncertainty and improving reproducibility.

The combination of these methods provides a comprehensive, deterministic solution to mtDNA alignment, setting a new standard for accuracy and specificity.


<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/010_Knowledge_Graph/C_Haplotree">NEXT ➡️</a>
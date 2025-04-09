# **Probe Mutation Rates by Region**

## 🔎 **Summary of Findings**
Recent probe data reveals that the mitochondrial DNA (mtDNA) haplotree, which is based on maternal inheritance and mutation rates, is incomplete and distorted by underlying functional and adaptive pressures. This challenges long-standing assumptions about mutation rates and the mtDNA evolutionary timeline.

<hr>

## 🧠 Thinking in Graphs

> One of the most important shifts in this work is understanding that:
>
> ### 🧬 More variation does **not** mean more instability.
>
> In regions like HVR1 and HVR2, we see more mutations — but that doesn’t make them unreliable.
> In fact, when analyzed using **fused probes** (multi-base segments), these regions show **remarkable stability**.
>
> Why? Because probes capture the **sequence context** around mutations — not just isolated changes.
> This makes them more specific, more repeatable, and less prone to false signals from repeated use of the same site.
>
> Traditional variant-based models often misread this variation as saturation or instability.
> In graph-based models, we track how mutations cluster and co-occur across many sequences.
> That’s a **structural signal**, not noise.
>
> 🔁 This isn’t just a different measurement — it’s a different way of modeling information.
>
> **If this is your first time exploring this:**
> - Start with [“The Concept on Mutation Saturation”](#the-concept-on-fmutation-saturation),
> - Review the probe vs variant mutation rate tables,
> - Then return to the timeline discussion with this framework in mind.
>
> This is what it means to **think in graphs**: focusing on patterns, structure, and relationships — not just counts.


---
## The Concept on fMutation Saturation 

Saturation occurs when a site has mutated so many times that the original signal is lost, making it difficult to reconstruct evolutionary history. In hypervariable regions of mitochondrial DNA (mtDNA), saturation **appears** to be happening because the same site seems to mutate repeatedly. This can obscure the true evolutionary signal, as earlier mutations are overwritten or masked by subsequent mutations.

Saturation behaves very differently at the variant and probe levels:

- At the **variant level**, you’ve got **four options** at each site — A, T, C, or G. That’s a small set of possibilities, so if mutations happen often enough, the same site can easily mutate back and forth between the same states. That’s why you see saturation at the variant level — it’s just the law of small numbers playing out.

- But at the **probe level**, it’s a whole different game. A probe is a sequence — say, 40 bases long. There are **4⁴⁰ possible combinations** of 40 nucleotides — that’s over a **trillion trillion** possible sequences. The odds of a probe mutating and ending up back at the same sequence are practically zero. That’s why saturation at the probe level is almost impossible — the search space is just too big.

- Here’s the kicker: **Nature doesn’t even use the full 4⁴⁰ space.** What we actually observe is only a small fraction of those possible sequences — life operates within a constrained evolutionary framework. And when you look at probes instead of variants, you see a much more stable and orderly picture because probes capture these deeper biological constraints that single variants miss.

- The apparent instability at the variant level comes from the limited number of possible states at any given site (A, T, C, or G). But probes reveal a more complex picture. Multiple probes can align to the same site while varying in the surrounding sequence, which creates a richer and more stable evolutionary signal. For example, while C152T looks like a single variant, over 5,000 probes encode it with variation in the flanking regions — revealing the deeper evolutionary structure that variants alone can’t show.
## Implications for Timelines

This deeper understanding of saturation has important implications for evolutionary timelines. If the apparent instability at the variant level is an illusion caused by the small number of possible outcomes, then the inferred mutation rate — which underlies most evolutionary dating models — may be artificially inflated.

Variants make it seem like mutations are happening more frequently than they actually are because the limited number of possible outcomes makes saturation happen faster.

### ✅ Why Probes Provide a More Accurate Timeline

✔️ **Probe sequences encode a richer set of information** and are far less susceptible to saturation.\
✔️ **Probes reflect the true evolutionary signal** more accurately.\
✔️ **Probe-based data shows that mutation-based dating methods** that rely on variants alone may systematically underestimate the true time depth of evolutionary events.

### ✅ Revisiting C152T

- C152T **appears frequently** based on variant-level data, but each appearance is not unique — it reflects how often that probe configuration is seen, not how often the site itself is mutating.
- The probe-based signal suggests that the underlying mutation rate is more stable than variant-level data implies — suggesting that the mutation rate is slower and the divergence time is deeper.

### 📊 **Variant vs. Probe Mutation Rates at Tree Depths**

The relationship between path variants and probes suggests that these two signals are independent, with no significant correlation between them (Pearson r = 0.009, p = 0.969). This suggests that the divergence model based on path variants may not fully capture the underlying structural reality reflected in probes. The divergence timeline is therefore distorted — sequence context and drift may contribute to this misalignment, but further analysis is needed to determine their precise effect.

Download the <a href="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/D_Graph_Characteristics/d_Haplotree_Timelines/Probe_variant_tree_depth.xlsx">Excel File</a>


<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/fused_probes_and_variants_by_level.png" width="50%" height="50%">
*Figure: Comparison of variant and probe mutation rates at different tree depths. Pearson correlation coefficient shows the degree of association between probe stability and variant instability.*

---



## 📊 Relative Inferred Time to Divergence (Log Scale)

Probes reveal greater sequence diversity, which reflects deeper evolutionary signals and longer divergence times compared to variants. The higher probe diversity indicates that mutation rates inferred from variants alone are likely inflated — suggesting that divergence events may have occurred earlier than previously estimated.


### Variant Mutation Rates by Region

| Region | Min Pos | Max Pos | Region Length | Variant Count | Variants per Base |
|--------|---------|---------|----------------|----------------|--------------------|
| HVR2   | 10      | 574     | 565            | 1839           | 3.254867           |
| CR     | 575     | 16000   | 15426          | 15249          | 0.988526           |
| HVR1   | 16017   | 16545   | 529            | 1686           | 3.187146           |



### Probe Mutation Rates by Region

| Region | Min Pos | Max Pos | Region Length | Probe Count | Probes per Base |
|--------|---------|---------|----------------|--------------|------------------|
| HVR2   | 10      | 574     | 565            | 13931        | 24.656637        |
| CR     | 575     | 16000   | 15426          | 29980        | 1.943472         |
| HVR1   | 16017   | 16545   | 529            | 17272        | 32.650284        |



---

Regions traditionally labeled as **hypervariable** — and dismissed as unreliable for deep evolutionary timelines — appear surprisingly stable when viewed through probe-based data.

- At the **variant level** — hypervariable regions appear noisy and unstable due to saturation within the small state space of four possible bases (A, T, C, G).  
- At the **probe level** — the broader sequence context (including adjacent variants and flanking regions) provides greater specificity, revealing a deeper and more stable evolutionary signal.  
- The apparent instability at the variant level reflects the small number of possible outcomes — not true evolutionary volatility.  

This means that hypervariable regions are not actually hypervariable in the deeper evolutionary sense. The variability seen at the variant level reflects the small number of possible states, but the probe data shows that these regions are highly structured and evolve more slowly than previously thought.

---

<img src="https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/100_images/inferred_timeline_fused_probes_vs_variants.png" width="50%" height="50%">



This challenges a core assumption in mitochondrial phylogenetics — that hypervariable regions are only useful for short-term lineage tracing. Probes reveal that these regions, when viewed in context, retain a stable and consistent evolutionary signal over deep time. This suggests that variant-based models may be systematically underestimating the true timeline of mitochondrial evolution.


<hr>


## 1. Impact on the Haplotree Timeline

### 🚨 Key Consequences:

**Branch Ages May Be Misleading**\
➡️ High-frequency mutations at the variant level may reflect the limited specificity of individual variants, whereas adjacent variants and flanking regions (probes) provide greater specificity and constrain the available mutation paths.

**TMRCA (Time-to-Most-Recent-Common-Ancestor) Estimates Are Less Reliable**\
➡️ Differences in how probes and variants reflect mutation rates may distort the inferred timeline.\
➡️ Probe-level data suggests that the inferred mutation rate at the variant level is too high, indicating that the TMRCA may be earlier than previously estimated.

---

## 2. Why the Probe-Based Approach Is More Robust

### ✅ How the Current Timeline Is Determined

1. **Bayesian Phylogenetics** – Estimates based on prior mutation models and molecular clock assumptions.
2. **Maximum Likelihood** – Estimates based on probability maximization under a fixed mutation model.
3. **Calibration Points** – Estimates anchored to known historical events.

### ✅ Why the Probe-Based Approach Is More Robust

✔️ **Larger Set of Possible Outcomes → Greater Specificity** – More possible combinations at the probe level reduce saturation.\
✔️ **Higher Resolution of Sequence Changes** – Probes encode richer information.\
✔️ **Structural Signal → Less Noise** – Probes reduce noise from limitations in the set of possible outcomes.\
✔️ **Reduced Timeline Compression** – Probe-based data reflects a more granular picture incorporating sets of adjacent variants and flanking regions.

---

## 3. Final Impact on the Haplotree Timeline

✔️ Probe-based data challenges the mutation rate assumptions that underlie the haplotree model.

✔️ Divergence estimates based on variants alone may be systematically flawed, leading to incorrect branch points, lengths, and relationships between branches.

✔️ The observed probe-level stability suggests that the haplotree model may require a fundamental reassessment.



[📥 Download the mutation ratesFull Dataset](https://github.com/waigitdas/mt_DNA_Knowledge_Graph/blob/main/010_Knowledge_Graph/D_Graph_Characteristics/d_Haplotree_Timelines/region_mutation_rates_fused_probes_20250331_230516.xlsx)


<a href="https://github.com/waigitdas/Mitochondrial-DNA-Research/tree/main/020_Haplotree_Dispositive_Evidence">NEXT ➡️</a>
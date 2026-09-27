import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results/tables/raw_results.csv", encoding="utf-16")

w1 = df[df.workload == 1]
fig, ax = plt.subplots(figsize=(6,4))
for name, g in w1.groupby("structure"):
    ax.plot(g["n"], g["avg_time_ns"]/1e6, marker="o", label=name)
ax.set_xscale("log"); ax.set_yscale("log")
ax.set_xlabel("n"); ax.set_ylabel("avg time for 10,000 get() (ms)")
ax.set_title("Workload 1: Random Access time vs n")
ax.legend(); ax.grid(True, which="both", alpha=0.3)
fig.tight_layout(); fig.savefig("results/plots/workload1_time_vs_n.png", dpi=130)

w2 = df[df.workload == 2]
fig, ax = plt.subplots(figsize=(6,4))
for name, g in w2.groupby("structure"):
    ax.plot(g["n"], g["metric_value"], marker="o", label=name)
ax.set_xscale("log"); ax.set_yscale("log")
ax.set_xlabel("n"); ax.set_ylabel("total comparisons (1,000 contains calls)")
ax.set_title("Workload 2: Search comparisons vs n")
ax.legend(); ax.grid(True, which="both", alpha=0.3)
fig.tight_layout(); fig.savefig("results/plots/workload2_comparisons_vs_n.png", dpi=130)

w3 = df[(df.workload == 3) & (df.phase == "insert")]
fig, ax = plt.subplots(figsize=(6,4))
for name, g in w3.groupby("structure"):
    ax.plot(g["n"], g["avg_time_ns"]/1e6, marker="o", label=name)
ax.set_xscale("log"); ax.set_yscale("log")
ax.set_xlabel("n"); ax.set_ylabel("avg time for 1,000 insertions (ms)")
ax.set_title("Workload 3: Insertion time vs n (front vs middle)")
ax.legend(fontsize=8); ax.grid(True, which="both", alpha=0.3)
fig.tight_layout(); fig.savefig("results/plots/workload3_insert_time_vs_n.png", dpi=130)

w4 = df[df.workload == 4]
fig, ax = plt.subplots(figsize=(6,4))
for name, g in w4.groupby("phase"):
    ax.plot(g["n"], g["avg_time_ns"]/1e6, marker="o", label=name)
ax.set_xscale("log"); ax.set_yscale("log")
ax.set_xlabel("n"); ax.set_ylabel("avg time (ms)")
ax.set_title("Workload 4: MinHeap insert/extractMin time vs n")
ax.legend(); ax.grid(True, which="both", alpha=0.3)
fig.tight_layout(); fig.savefig("results/plots/workload4_heap_time_vs_n.png", dpi=130)
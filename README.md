# Citation Flow Analysis

A Java-based graph analysis project that models citations between scientific papers using the **Cora citation dataset**. It builds a directed citation network and reports citation counts, paper categories, and maximum flow between two selected papers.

## Features

- **Citation graph:** Represents each paper as a vertex and each citation as a directed edge.
- **Citation count (in-degree):** Counts how many incoming citations each paper receives.
- **BFS traversal:** Explores papers level by level from a selected starting paper.
- **DFS traversal:** Explores a path as deeply as possible before backtracking.
- **Maximum flow:** Uses the Edmonds–Karp approach (BFS-based augmenting paths) to calculate maximum flow between a source paper and a sink paper. Each citation edge is assigned unit capacity.
- **Dataset summary:** Prints the number of papers and citations, paper counts by category, and the top 10 cited papers.

## Technology

- Java
- Java Collections Framework (`HashMap`, `ArrayList`, `HashSet`, `Queue`)
- Java NIO file handling (`Path`, `Files`)
- Graph algorithms: BFS, DFS, in-degree calculation, Edmonds–Karp maximum flow

## Project Structure

```text
Citation Flow Analysis/
├── Main.java                # Loads the dataset and runs the analysis
├── Paper.java               # Paper data model
├── CitationGraph.java       # Directed graph representation and paper search
├── CitationAnalysis.java    # Calculates citation counts (in-degree)
├── GraphAlgorithms.java     # BFS and DFS traversals
├── NetworkFlow.java         # Maximum flow using Edmonds–Karp
├── names.tsv                # Paper IDs, one per row
├── labels.tsv               # Category index for each paper
├── names_labels.tsv         # Category names, one per row
├── adjacency.tsv            # Citation edges (source index, target index, optional flag)
├── biadjacency.tsv          # Additional dataset relation file
└── graph.txt                # Sample output / graph information
```

The `.class` files included in some copies are compiled Java bytecode. You can regenerate them by compiling the `.java` files.

## Requirements

- **JDK 11 or newer** is recommended. The code uses modern Java APIs such as `Path.of(...)` and `Stream.toList()`, so **Java 16 or newer** is required for the source as currently written.
- A terminal or command prompt.

Check your Java version:

```bash
java -version
javac -version
```

## How to Compile and Run

Open a terminal and change into the project directory:

```bash
cd "Citation Flow Analysis"
```

Compile the source files:

```bash
javac *.java
```

### 1. Run the standard analysis

```bash
java Main
```

This expects the dataset `.tsv` files to be in the current directory. It loads the Cora dataset and displays a citation-network sample, category totals, and the 10 most cited papers.

### 2. Run maximum-flow analysis

Choose two valid paper IDs from `names.tsv` as the source and sink:

```bash
java Main <source-paper-id> <sink-paper-id>
```

Example format:

```bash
java Main 35 40
```

Replace `35` and `40` with IDs that exist in your `names.tsv` file. The program calculates the maximum flow from the source paper to the sink paper, treating each citation edge as having capacity 1.

### 3. Specify a separate dataset directory

```bash
java Main <dataset-folder>
```

The folder must contain `names.tsv`, `labels.tsv`, `names_labels.tsv`, and `adjacency.tsv`.

To specify both a dataset directory and source/sink paper IDs:

```bash
java Main <dataset-folder> <source-paper-id> <sink-paper-id>
```

## How the Graph Works

A directed edge `A → B` means **paper A cites paper B**.

- **In-degree / citation count:** The number of edges entering a paper. This represents how many incoming citations it has in the loaded network.
- **BFS:** Uses a queue to visit reachable papers in breadth-first order.
- **DFS:** Uses recursion to visit reachable papers depth-first.
- **Maximum flow:** Treats citation edges as unit-capacity connections and repeatedly finds augmenting paths using BFS until no more path exists. The returned value is the maximum flow for the chosen source and sink in this constructed network.

> **Note:** Maximum flow here is a graph-connectivity analysis, not a direct measure of a paper's scientific influence. The result depends on the directed edges and the unit-capacity assumption.

## Dataset

The project includes files for the **Cora citation network**, a collection of scientific publications with category labels.

Dataset references:
- NetSet collection: https://netset.telecom-paris.fr
- Cora dataset page: https://relational.fit.cvut.cz/dataset/CORA

The code expects the data files to use tab-separated values (TSV). In `adjacency.tsv`, the first two columns identify the source and target paper by their row indices in `names.tsv`. If a third column is present, rows marked `True` are included as citation edges.

## Important Implementation Notes

- Every paper is added to the graph before citation edges are loaded.
- The citation direction is **citing paper → cited paper**.
- The citation count is computed from incoming edges.
- The maximum-flow implementation creates residual capacities and reverse edges, as required by the Edmonds–Karp method.
- `GraphAlgorithms` provides BFS and DFS methods, but the current `Main` program does not automatically run those traversals. They can be invoked from Java code using a `CitationGraph` instance.

## Troubleshooting

**`javac` is not recognized**
- Install a JDK and ensure its `bin` directory is added to your system `PATH`.

**Dataset file not found**
- Run `java Main` from the project directory, or pass the dataset directory as the first argument.

**Paper ID not found / invalid source or sink**
- Use paper IDs that appear in `names.tsv`. The source and sink must be different and both must exist in the graph.

**Unexpected category or citation totals**
- Ensure all `.tsv` files belong to the same dataset version and have not been reordered independently.

## Author / Contributions

Add your name, team members, institution, and project/course details here if needed.

# B-Tree Implementation in Java

A complete implementation of a B-tree data structure with insertion operations and performance metrics tracking.

## Features
- **B-tree Operations**:
  - Insertion with node splitting
  - Key search (exact match)
  - Memory-efficient node structure
  - Automatic tree balancing
- **Performance Tracking**:
  - Execution time measurement
  - Memory usage monitoring
  - Insertion success/error statistics
- **File Processing**:
  - Handles large datasets
  - Automatic number sanitization
  - CSV/Text file support with decimal adaptation

## Project Structure
```
src/
├── ArvoreB.java      # Main B-tree implementation
├── No.java           # B-tree node structure and visualization logic
└── Main.java         # CLI interface and performance metrics
```

## Dependencies
- Java JDK 11+
- Any text file containing numeric values

## Installation & Usage
```bash
# Compile
javac src/*.java -d out/

# Run with default parameters (order=4, file=arquivo_menor.txt)
java -cp out Main

# Custom run
java -cp out Main <order> <input-file>
```

## File Format Requirements
Create input files with one numeric value per line:

```
12.5
3,1415  # Commas auto-converted to decimals
7.0
```

## Key Implementation Details

### B-tree Properties
```java
public class ArvoreB {
    private int ordem;  // Tree order (minimum degree)
    private No raiz;    // Root node
    // ...
}
```
- **Node Capacity**: `2*ordem - 1` keys
- **Insertion Complexity**: O(log n)
- **Key Storage**: `Double` values with no duplicates

### Insertion Process
1. Check root capacity
2. Split root if full (see `divideNo()`)
3. Recursive insertion with node splitting
4. Automatic tree height adjustment

## Performance Metrics
Sample output:
```
Execution Summary:
Total lines processed: 250,000
Successfully inserted: 249,850
Errors encountered: 150
Total execution time: 2.456 seconds
Memory used: 85.34 MB
```

## Visualization (No.java)
While primarily a console application, the node structure contains positioning logic for potential GUI integration:
```java
public void updateCoordenates(No parent, double x) {
    // Calculates node positions for tree visualization
    // Uses DIFERENCA_ALTURA (height difference) and DIFERENCA_IRMAOS (sibling spacing)
}
```

## Possible Improvements
- [ ] Deletion operations
- [ ] Graphical tree visualization
- [ ] Concurrent insertions
- [ ] Disk-based storage support

## Contributors
- Bruno Villas Boas da Costa (RA: 317527)
- Julio Macumoto (RA: 344915)
- Wagner Takeshi Obara (RA: 317365)

## License
[MIT License](LICENSE)
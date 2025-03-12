import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ArvoreB bTree = new ArvoreB(4);
        int lineNumber = 0;
        int insertCount = 0;
        int errorCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader("arquivo_menor.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String sanitized = line.replace(',', '.')
                                         .replaceAll("[^\\d.]", "");
                    double value = Double.parseDouble(sanitized);
                    bTree.insere(value);
                    insertCount++;
                } catch (NumberFormatException e) {
                    errorCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        double totalTime = (endTime - startTime) / 1000.0;
        
        System.out.println("\nExecution Summary:");
        System.out.printf("Total lines processed: %,d%n", lineNumber);
        System.out.printf("Successfully inserted: %,d%n", insertCount);
        System.out.printf("Errors encountered: %,d%n", errorCount);
        System.out.printf("Total execution time: %.3f seconds%n", totalTime);

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Memory used: %.2f MB%n", usedMemory / (1024.0 * 1024.0));
    }
}

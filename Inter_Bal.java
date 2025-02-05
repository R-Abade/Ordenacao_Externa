
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

class Element {

    BufferedReader reader; // The BufferedReader for the file
    double value; // The current value read from the BufferedReader

    // Constructor
    public Element(BufferedReader reader, double value) {
        this.reader = reader;
        this.value = value;
    }
}

public class Inter_Bal {

    public void intercalador(File arquivo) throws IOException {
        LinkedList<File> mem = new LinkedList<>(); // List to hold temporary files
        double arr[] = new double[100];
        int i = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                double registro = Double.parseDouble(linha);
                arr[i] = registro;

                if (i == 99) {
                    PriorityQueue<Double> heap = new PriorityQueue<>(100);
                    for (double d : arr) {
                        heap.add(d);
                    }

                    File temp = File.createTempFile("ordExt_resposta", ".txt");
                    try (BufferedWriter saida = new BufferedWriter(new FileWriter(temp))) {
                        while (!heap.isEmpty()) {
                            saida.write(heap.poll() + "\n");
                        }
                        mem.add(temp);
                    }
                    i = 0;
                }
                i++;
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        ordExterno(mem);
    }

    public void ordExterno(LinkedList<File> mem) throws IOException {
        List<BufferedReader> caminhos = new ArrayList<>();
        PriorityQueue<Element> minHeap = new PriorityQueue<>(Comparator.comparingDouble(e -> e.value));
        DecimalFormat df = new DecimalFormat("#.################"); // Format for double values

        try {

            for (File file : mem) {
                BufferedReader leitor = new BufferedReader(new FileReader(file));
                caminhos.add(leitor);
                String linha = leitor.readLine();
                if (linha != null) {
                    double registro = Double.parseDouble(linha);
                    minHeap.add(new Element(leitor, registro));
                }
            }

            try (BufferedWriter resposta = new BufferedWriter(new FileWriter("ordExt_resultado.txt"))) {
                while (!minHeap.isEmpty()) {
                    Element elem = minHeap.poll();
                    resposta.write(df.format(elem.value) + "\n");

                    String linha = elem.reader.readLine();
                    if (linha != null) {
                        double registro = Double.parseDouble(linha);
                        minHeap.add(new Element(elem.reader, registro));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro");
        } finally {
            for (BufferedReader arquivo : caminhos) {
                arquivo.close();
            }
        }
    }
}

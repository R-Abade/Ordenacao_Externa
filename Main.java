import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        double inicioTempo = System.currentTimeMillis();
        int contadorInserts = 0;
        int tamLote = 10000;
        ArrayList<Double> lote = new ArrayList<>(tamLote);
        ArvoreB bTree = new ArvoreB(256);  //autores definiram o indice como n. Quando n = 10, dá estouro de heap

        try (BufferedReader reader = new BufferedReader(new FileReader("ordExt_resultadoTeste.txt"), 8192 * 1024)) { //buffer maior melhora eficiencia de leitura by Novy
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isEmpty()) {
                    continue;
                }

                try {
                    String formatados = linha.replace(',', '.').replaceAll("[^\\d.]", ""); //estava gerando erros por causa da virgula
                    double registro = Double.parseDouble(formatados);
                    lote.add(registro);
                    contadorInserts++;

                    if (lote.size() >= tamLote) { //armazena em lotes para eficiencia, se lote ficar cheio, insere os registros do lote na bTree
                        bTree.processaLote(bTree, lote);
                        lote.clear();
                        System.out.println("Inserido " + contadorInserts + " registros");
                        System.gc(); //evitar problemas de estouro de heap
                    }
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }

            if (!lote.isEmpty()) { //processa se ainda tiver registro a ser lido
                bTree.processaLote(bTree, lote);
            }

        } catch (IOException e) {
            System.err.println("Erro" + e.getMessage());
            e.printStackTrace();
        }

        double fimTempo = System.currentTimeMillis();
        double totalTempo = (fimTempo - inicioTempo) / 1000.0;

        System.out.printf("\nFoi inserido: %,d%n", contadorInserts);
        System.out.printf("Tempo de exec: %.3f segundos%n", totalTempo);

        Runtime runtime = Runtime.getRuntime();
        double usoMemoria = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Memoria usada: %.2f MB%n", usoMemoria / (1024.0 * 1024.0));
        System.out.printf("Ordem da bTree: %d%n", bTree.getOrdem());
        //System.out.printf("QtdeRegistros: %d%n", bTree.getnElementos());
    }

}
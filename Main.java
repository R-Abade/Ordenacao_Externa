import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        double inicioTempo = System.currentTimeMillis();
        int contadorInserts = 0;
        int tamLote = 10000;
        ArrayList<Double> lote = new ArrayList<>(tamLote);
        ArvoreB bTree = new ArvoreB(256); // Ordem da árvore B

        try (BufferedReader leitor = new BufferedReader(new FileReader("ordExt_resultadoTeste.txt"), 8192 * 1024)) {
            double posicao = 0;
            String linha;
            while ((linha = leitor.readLine()) != null) {
                try {
                    String formatados = linha.replace(',', '.').replaceAll("[^\\d.]", "");
                    double registro = Double.parseDouble(formatados);
                    lote.add(registro);
                    contadorInserts++;

                    if (lote.size() >= tamLote) {
                        ArvoreB.processaLote(bTree, lote, posicao);
                        posicao += linha.length() + 1; //atualiza posição
                        lote.clear();
                        System.out.println("Inserido " + contadorInserts + " registros");
                        System.gc(); //forçar coleta para evitar problemas de estouro da heap
                    }
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }

            if (!lote.isEmpty()) {
                ArvoreB.processaLote(bTree, lote, posicao);
            }

        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }

        double fimTempo = System.currentTimeMillis();
        double totalTempo = (fimTempo - inicioTempo) / 1000.0;

        System.out.println("\nFoi inserido: " + String.format("%,d", contadorInserts));
        System.out.println("Tempo de execução: " + String.format("%.3f segundos", totalTempo));

        Runtime runtime = Runtime.getRuntime();
        double usoMemoria = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memória usada: " + String.format("%.2f MB", usoMemoria / (1024.0 * 1024.0)));
        System.out.println("Ordem da B-Tree: " + bTree.getOrdem());
        System.out.println("-------------------------------------------");
        String resultadoBusca = bTree.BuscaChave(bTree.getRaiz(), 0.001564625421857757);
        System.out.println("Registros da página: " + resultadoBusca);
    }
}
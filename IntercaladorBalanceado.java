// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class IntercaladorBalanceado {
   private static final DecimalFormat FORMATADOR = new DecimalFormat("0.####################");
   private static final int LIMITE_MEMORIA = 100;
   private static final int NUMERO_CAMINHOS = 10;

   public IntercaladorBalanceado() {
   }

   public void ordenarArquivo(File arquivoEntrada) throws IOException {
      LinkedList<File> blocosTemp = new LinkedList<>();

      try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoEntrada))) {
         List<Double> bufferMemoria = new ArrayList<>(LIMITE_MEMORIA);
         String linha;

         while ((linha = leitor.readLine()) != null) {
            double numero = Double.parseDouble(linha.trim().replace(",", "."));
            bufferMemoria.add(numero);

            if (bufferMemoria.size() >= LIMITE_MEMORIA) {
               processarBlocoMemoria(bufferMemoria, blocosTemp);
               bufferMemoria.clear();
            }
         }

         if (!bufferMemoria.isEmpty()) {
            processarBlocoMemoria(bufferMemoria, blocosTemp);
         }
      }

      executarOrdenacaoExterna(blocosTemp);
   }

   private void processarBlocoMemoria(List<Double> numeros, LinkedList<File> listaBlocos) throws IOException {
      Collections.sort(numeros);
      File blocoTemp = File.createTempFile("ordExt_bloco", ".txt");
      
      try (BufferedWriter escritor = new BufferedWriter(new FileWriter(blocoTemp))) {
         for (Double numero : numeros) {
            escritor.write(FORMATADOR.format(numero));
            escritor.newLine();
         }
      }
      listaBlocos.add(blocoTemp);
   }

   private void executarOrdenacaoExterna(LinkedList<File> blocos) throws IOException {
      while (blocos.size() > 1) {
         LinkedList<File> novosBlocos = new LinkedList<>();
         List<List<File>> grupos = dividirGrupos(blocos, NUMERO_CAMINHOS);

         for (List<File> grupo : grupos) {
            File blocoMergeado = mergearGrupo(grupo);
            novosBlocos.add(blocoMergeado);
         }

         // Limpeza dos arquivos temporários antigos
         for (File arquivo : blocos) {
            arquivo.delete();
         }

         blocos = novosBlocos;
      }

      // Renomeia o arquivo final
      File resultadoFinal = new File("ordExt_resultado.txt");
      if (!blocos.isEmpty()) {
         blocos.getFirst().renameTo(resultadoFinal);
      }
   }

   private List<List<File>> dividirGrupos(List<File> arquivos, int tamanhoGrupo) {
      List<List<File>> grupos = new ArrayList<>();
      
      for (int i = 0; i < arquivos.size(); i += tamanhoGrupo) {
         int fim = Math.min(i + tamanhoGrupo, arquivos.size());
         grupos.add(new ArrayList<>(arquivos.subList(i, fim)));
      }
      return grupos;
   }

   private File mergearGrupo(List<File> grupoArquivos) throws IOException {
      PriorityQueue<ElementoHeap> heap = new PriorityQueue<>(Comparator.comparingDouble(e -> e.valor));
      List<BufferedReader> leitores = new ArrayList<>();

      try {
         File arquivoMergeado = File.createTempFile("ordExt_merge", ".txt");
         
         // Inicializa heap com primeiros elementos de cada arquivo
         for (File arquivo : grupoArquivos) {
            BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
            leitores.add(leitor);
            String linha = leitor.readLine();
            
            if (linha != null) {
               double valor = Double.parseDouble(linha.trim().replace(",", "."));
               heap.add(new ElementoHeap(leitor, valor));
            }
         }

         // Processa elementos usando heap
         try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivoMergeado))) {
            while (!heap.isEmpty()) {
               ElementoHeap elemento = heap.poll();
               escritor.write(FORMATADOR.format(elemento.valor));
               escritor.newLine();

               String proximaLinha = elemento.leitor.readLine();
               if (proximaLinha != null) {
                  double novoValor = Double.parseDouble(proximaLinha.trim().replace(",", "."));
                  heap.add(new ElementoHeap(elemento.leitor, novoValor));
               }
            }
         }

         // Fecha leitores e remove arquivos temporários
         for (BufferedReader leitor : leitores) {
            leitor.close();
         }
         for (File arquivo : grupoArquivos) {
            arquivo.delete();
         }

         return arquivoMergeado;

      } catch (IOException e) {
         throw new IOException("Falha no merge: " + e.getMessage(), e);
      }
   }

   // Classe auxiliar para o heap
   private static class ElementoHeap {
      final BufferedReader leitor;
      final double valor;

      ElementoHeap(BufferedReader leitor, double valor) {
         this.leitor = leitor;
         this.valor = valor;
      }
   }
}

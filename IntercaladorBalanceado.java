
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class IntercaladorBalanceado {

    private static final DecimalFormat FORMATADOR = new DecimalFormat("0.####################");
    private static final int LIMITE_MEMORIA = 100;
    private static final int NUMERO_CAMINHOS = 10;
    private static final int BUFFER_SIZE = 8192;

    public void ordenarArquivo(File arquivoEntrada) throws IOException {
        System.out.println("Iniciando ordenação do arquivo: " + arquivoEntrada.getAbsolutePath());

        if (!arquivoEntrada.exists()) {
            throw new FileNotFoundException("Arquivo de entrada não encontrado: " + arquivoEntrada.getAbsolutePath());
        }

        LinkedList<File> blocosTemp = new LinkedList<>();
        int totalNumeros = 0;

        // Read and create initial sorted blocks
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoEntrada), BUFFER_SIZE)) {
            double[] bufferMemoria = new double[LIMITE_MEMORIA];
            int count = 0;
            String linha;

            while ((linha = leitor.readLine()) != null) {
                try {
                    bufferMemoria[count] = Double.parseDouble(linha.trim().replace(",", "."));
                    count++;
                    totalNumeros++;

                    if (count >= LIMITE_MEMORIA) {
                        System.out.println("Processando bloco de " + count + " números");
                        processarBlocoMemoria(bufferMemoria, count, blocosTemp);
                        count = 0;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Número inválido ignorado: " + linha);
                }
            }

            if (count > 0) {
                System.out.println("Processando bloco final de " + count + " números");
                processarBlocoMemoria(bufferMemoria, count, blocosTemp);
            }
        }

        System.out.println("Total de números lidos: " + totalNumeros);
        System.out.println("Total de blocos temporários criados: " + blocosTemp.size());

        if (blocosTemp.isEmpty()) {
            System.out.println("Nenhum bloco temporário foi criado. O arquivo de entrada está vazio?");
            return;
        }

        // Perform external sorting
        File arquivoFinal = executarOrdenacaoExterna(blocosTemp);

        if (arquivoFinal != null) {
            File resultadoFinal = new File("ordExt_resultado.txt");
            if (resultadoFinal.exists()) {
                System.out.println("Removendo arquivo de resultado anterior");
                resultadoFinal.delete();
            }

            // Use copy instead of rename to handle cross-filesystem moves
            System.out.println("Copiando resultado final para: " + resultadoFinal.getAbsolutePath());
            copyFile(arquivoFinal, resultadoFinal);
            arquivoFinal.delete();

            System.out.println("Arquivo final criado com sucesso: " + resultadoFinal.getAbsolutePath());
        } else {
            throw new IOException("Falha ao gerar arquivo final");
        }
    }

    private void processarBlocoMemoria(double[] numeros, int count, LinkedList<File> listaBlocos) throws IOException {
        PriorityQueue<Double> heap = new PriorityQueue<>();
        for (int i = 0; i < count; i++) {
            heap.offer(numeros[i]);
        }

        File blocoTemp = File.createTempFile("ordExt_bloco", ".txt");
        System.out.println("Criando bloco temporário: " + blocoTemp.getAbsolutePath());

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(blocoTemp), BUFFER_SIZE)) {
            while (!heap.isEmpty()) {
                escritor.write(FORMATADOR.format(heap.poll()));
                escritor.newLine();
            }
        }
        listaBlocos.add(blocoTemp);
    }

    private File executarOrdenacaoExterna(LinkedList<File> blocos) throws IOException {
        System.out.println("Iniciando ordenação externa com " + blocos.size() + " blocos");

        while (blocos.size() > 1) {
            LinkedList<File> novosBlocos = new LinkedList<>();

            for (int i = 0; i < blocos.size(); i += NUMERO_CAMINHOS) {
                int fim = Math.min(i + NUMERO_CAMINHOS, blocos.size());
                List<File> grupo = new ArrayList<>(blocos.subList(i, fim));
                System.out.println("Mergeando grupo de " + grupo.size() + " arquivos");
                File blocoMergeado = mergearGrupo(grupo);
                novosBlocos.add(blocoMergeado);
            }

            for (File arquivo : blocos) {
                arquivo.delete();
            }

            blocos = novosBlocos;
            System.out.println("Restam " + blocos.size() + " blocos após merge");
        }

        return blocos.isEmpty() ? null : blocos.getFirst();
    }

    private File mergearGrupo(List<File> grupoArquivos) throws IOException {
        PriorityQueue<ElementoHeap> heap = new PriorityQueue<>();
        List<BufferedReader> leitores = new ArrayList<>();
        File arquivoMergeado = File.createTempFile("ordExt_merge", ".txt");

        System.out.println("Criando arquivo mergeado: " + arquivoMergeado.getAbsolutePath());

        try {
            // Initialize readers and heap
            for (File arquivo : grupoArquivos) {
                BufferedReader leitor = new BufferedReader(new FileReader(arquivo), BUFFER_SIZE);
                leitores.add(leitor);
                String linha = leitor.readLine();
                if (linha != null) {
                    double valor = Double.parseDouble(linha.trim().replace(",", "."));
                    heap.offer(new ElementoHeap(leitor, valor));
                }
            }

            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivoMergeado), BUFFER_SIZE)) {
                while (!heap.isEmpty()) {
                    ElementoHeap elemento = heap.poll();
                    escritor.write(FORMATADOR.format(elemento.valor));
                    escritor.newLine();

                    String proximaLinha = elemento.leitor.readLine();
                    if (proximaLinha != null) {
                        double novoValor = Double.parseDouble(proximaLinha.trim().replace(",", "."));
                        heap.offer(new ElementoHeap(elemento.leitor, novoValor));
                    }
                }
            }
        } finally {
            for (BufferedReader leitor : leitores) {
                try {
                    leitor.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar leitor: " + e.getMessage());
                }
            }
        }

        return arquivoMergeado;
    }

    // Helper method to copy files
    private void copyFile(File source, File dest) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(source)); OutputStream out = new BufferedOutputStream(new FileOutputStream(dest))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    private static class ElementoHeap implements Comparable<ElementoHeap> {

        final BufferedReader leitor;
        final double valor;

        ElementoHeap(BufferedReader leitor, double valor) {
            this.leitor = leitor;
            this.valor = valor;
        }

        @Override
        public int compareTo(ElementoHeap outro) {
            return Double.compare(this.valor, outro.valor);
        }
    }
}

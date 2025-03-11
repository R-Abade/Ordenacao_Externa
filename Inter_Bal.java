
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Inter_Bal {

    private static final int tamanhoBuffer = 8192; //quanto maior o buffer, mais eficiente fica - by Novy

    public void intercalador(File arquivo) throws IOException {
        LinkedList<File> arquivosTemps = new LinkedList<>();
        double[] mem = new double[100];
        int i = 0;

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo), tamanhoBuffer)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                double registro = Double.parseDouble(linha);
                mem[i] = registro;
                i++;

                if (i == 100) {
                    arquivoTemp(arquivosTemps, mem, i);
                    i = 0;
                }
            }

            if (i > 0) {
                arquivoTemp(arquivosTemps, mem, i);
            }
        }

        File resposta = intercaladorArquivos(arquivosTemps);
        File resultado = new File("ordExt_resultado.txt");

        if (resposta != null) {
            try (BufferedReader leitor = new BufferedReader(new FileReader(resposta), tamanhoBuffer); BufferedWriter escritor = new BufferedWriter(new FileWriter(resultado), tamanhoBuffer)) {

                DecimalFormat FORMATADOR = new DecimalFormat("0.####################");
                String linha = leitor.readLine();
                while (linha != null) {
                    double registro = Double.parseDouble(linha);
                    String registrado = FORMATADOR.format(registro);
                    escritor.write(registrado);
                    escritor.newLine();
                }
            }

            resposta.delete();
            System.out.println("Resultado: " + resultado.getAbsolutePath());
        }
    }

    public void arquivoTemp(LinkedList<File> arquivosTemps, double[] registros, int tamanhoMem) throws IOException {
        PriorityQueue<Double> heap = new PriorityQueue<>();
        for (int i = 0; i < tamanhoMem; i++) {
            heap.add(registros[i]);
        }

        File temp = File.createTempFile("ordExt_temp", ".txt");
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(temp), tamanhoBuffer)) {
            while (heap.isEmpty() != true) {
                escritor.write(String.valueOf(heap.poll()));
                escritor.newLine();
            }
        }

        arquivosTemps.add(temp);
    }

    public File intercaladorArquivos(LinkedList<File> memoria) throws IOException {
        while (memoria.size() > 1) {
            LinkedList<File> mergedFiles = new LinkedList<>();

            for (int i = 0; i < memoria.size(); i += 10) {
                int fim = Math.min(i + 10, memoria.size());
                LinkedList<File> blocoMerges = new LinkedList<>(memoria.subList(i, fim));
                File tempMergeado = mergeador(blocoMerges);
                mergedFiles.add(tempMergeado);

                for (File file : blocoMerges) {
                    file.delete();
                }
            }

            memoria = mergedFiles;
        }

        if (memoria.isEmpty()) {
            return null;
        } else {
            return memoria.getFirst();
        }
    }

    public File mergeador(List<File> caminhos) throws IOException {
        PriorityQueue<ElementoHeap> heap = new PriorityQueue<>();
        LinkedList<BufferedReader> buffer = new LinkedList<>();
        File mergeFile = File.createTempFile("ordExt_intercalado", ".txt");
        System.out.println("Criando arquivo mergeado: " + mergeFile.getAbsolutePath());

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(mergeFile), tamanhoBuffer)) {
            for (File file : caminhos) {
                BufferedReader leitor = new BufferedReader(new FileReader(file), tamanhoBuffer);
                buffer.add(leitor);
                String linha = leitor.readLine();
                if (linha != null) {
                    double registro = Double.parseDouble(linha);
                    heap.add(new ElementoHeap(leitor, registro));
                }
            }

            while (heap.isEmpty() != true) {
                ElementoHeap elem = heap.poll();
                escritor.write(String.valueOf(elem.valor));
                escritor.newLine();

                String nextLine = elem.leitor.readLine();
                if (nextLine != null) {
                    double valor = Double.parseDouble(nextLine);
                    heap.add(new ElementoHeap(elem.leitor, valor));
                }
            }
        } finally {
            for (BufferedReader temp : buffer) {
                try {
                    temp.close();
                } catch (Exception e) {
                    System.out.println("Erro ao fechar leitor: " + e.getMessage());
                }
            }
            for (File file : caminhos) {
                file.delete();
            }
        }

        return mergeFile;
    }
}

/*   public void ordExterno(List<File> mem) throws IOException {
        PriorityQueue<Entry> heap = new PriorityQueue<>();
        List<BufferedReader> leitores = new ArrayList<>();

        try {
            for (File file : mem) {
                BufferedReader leitor = new BufferedReader(new FileReader(file), tamanhoBuffer);
                leitores.add(leitor);
                String linha = leitor.readLine();
                if (linha != null) {
                    heap.add(new Entry(Double.parseDouble(linha), leitor));
                }
            }

            try (BufferedWriter resposta = new BufferedWriter(new FileWriter("ordExt_resultado.txt"))) {
                while (heap.isEmpty() != true) {
                    Entry menor = heap.poll();
                    resposta.write(String.valueOf(menor.value) + "\n");
                    String linha = menor.reader.readLine();
                    if (linha != null) {
                        heap.add(new Entry(Double.parseDouble(linha), menor.reader));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            for (BufferedReader leitor : leitores) {
                try {
                    leitor.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar leitor: " + e.getMessage());
                }
            }
            for (File file : mem) {
                file.delete();
            }
        }
    }

 */

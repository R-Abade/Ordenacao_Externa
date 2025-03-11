
import java.io.*;

public class Main {

    public static void main(String[] args) {
        double inicio = System.currentTimeMillis();
        File arquivo = new File("ordExt.txt");

        try {
            Inter_Bal intercalador = new Inter_Bal();
            intercalador.intercalador(arquivo);
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        double fimExec = System.currentTimeMillis();
        System.out.println("Tempo de execução em ms: " + (fimExec - inicio));
    }
}

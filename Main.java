
import java.io.*;

public class Main {

    public static void main(String[] args) {
        File arquivoEntrada = new File("ordExt.txt");

        try {
            IntercaladorBalanceado intercalador = new IntercaladorBalanceado();
            intercalador.ordenarArquivo(arquivoEntrada);
        } catch (Exception e) {
            System.err.println("Erro na ordenação: " + e.getMessage());
        }
    }
}

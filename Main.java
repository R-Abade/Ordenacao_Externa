
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        File arquivo = new File("ordExt_teste.txt");
        try {
            Inter_Bal intercalador = new Inter_Bal();
            intercalador.intercalador(arquivo);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao intercalar: " + e.getMessage());
        }
    }
}

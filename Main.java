// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.File;
import java.io.IOException;

public class Main {
   public Main() {
   }

   public static void main(String[] args) {
      File arquivoEntrada = new File("ordExt_teste.txt");

      try {
         IntercaladorBalanceado intercalador = new IntercaladorBalanceado();
         intercalador.ordenarArquivo(arquivoEntrada);
      } catch (IOException e) {
         System.err.println("Erro na ordenação: " + e.getMessage());
         e.printStackTrace();
      }
   }
}

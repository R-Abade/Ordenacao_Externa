import java.io.*;

public class Main {

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        File arquivo = new File("ordExt.txt");

        try {
            Inter_Bal intercalador = new Inter_Bal();
            intercalador.intercalador(arquivo);
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        long fimExec = System.currentTimeMillis();
        long tempoTotal = fimExec - inicio;
        
        // Conversão para horas, minutos, segundos e milissegundos
        long segundos = tempoTotal / 1000;
        long horas = segundos / 3600;
        segundos %= 3600;
        long minutos = segundos / 60;
        segundos %= 60;
        long milissegundos = tempoTotal % 1000;
        
        System.out.println("Tempo de execução: " + horas + "h " + minutos + "m " + segundos + "s " + milissegundos + "ms");
    }
}
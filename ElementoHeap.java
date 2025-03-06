import java.io.*;

public class ElementoHeap implements Comparable<ElementoHeap> {

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
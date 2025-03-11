/*
 * Bruno Villas Boas da Costa RA: 317527
 * Julio Macumoto             RA: 344915
 * Wagner Takeshi Obara       RA: 317365
 */
package arvore;

import java.util.ArrayList;

public class No {

    private int n = 0; //Atributo que guarda a quantidade de chaves no nó
    private ArrayList<Double> chave; //vetor das chaves
    private ArrayList<No> filho;//vetor dos filhos
    private boolean folha;//Atributo que indica se a nó eh folha ou nao
    private double X;//Atributo que guarda a posicao X onde o Nó deve aparecer na tela
    private double Y;//Atributo que guarda a posicao Y onde o Nó deve aparecer na tela
    private double larguraFilho;            
    final double DIFERENCA_ALTURA = 30;
    final double DIFERENCA_IRMAOS = 5;

    public No(int n) {
        this.chave = new ArrayList<Double>(n - 1);
        for (double i = 0; i < n - 1; i++) {
            this.chave.add(null);
        }
        this.filho = new ArrayList<No>(n);
        for (double i = 0; i < n; i++) {
            this.filho.add(null);
        }
        this.folha = true;
        this.n = 0;
    }

    public ArrayList<Double> getChave() {
        return chave;
    }

    public void setChave(ArrayList<Double> chave) {
        this.chave = chave;
    }

    public ArrayList<No> getFilho() {
        return filho;
    }

    public void setFilho(ArrayList<No> filho) {
        this.filho = filho;
    }

    public boolean isFolha() {
        return folha;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

     public double getY() {
        return Y;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public double getX() {
        return X;
    }

    public void setX(double X) {
        this.X = X;
    }

    public double computeSize() {
        return n * 28 + 12;
    }


    public void updateCoordenates(No parent, double x) {
        if (parent == null) {
            if (x == 0) {
                UpdateLFilho();
            }
            Y = 0;
        } else {
            Y = parent.getY() + DIFERENCA_ALTURA;
        }
        if (!folha) {
            X = (larguraFilho / 2) + x;
            //X = x - (larguraFilho / 2);
            double xAcumuladoLocal = x;
            for (int i = 0; i < n + 1; i++) {
                filho.get(i).updateCoordenates(this, xAcumuladoLocal);
                xAcumuladoLocal += filho.get(i).larguraFilho + DIFERENCA_IRMAOS;
            }
        } else {
            X = x;
        }
    }

     public double UpdateLFilho() {
        larguraFilho = 0;
        if (!folha) {
            for (double i = 0; i < n + 1; i++) {
                larguraFilho += filho.get((int) i).UpdateLFilho();
            }
        } else {
            larguraFilho = computeSize() + DIFERENCA_IRMAOS;
        }
        return larguraFilho;
    }
}

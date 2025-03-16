import java.io.*;
import java.util.*;

public class ArvoreB {
    private No raiz;
    private int ordem;
    private int nElementos;

    public ArvoreB(int n) {
        this.raiz = new No(n);
        this.ordem = n;
        nElementos = 0;
    }

    public int getnElementos() {
        return nElementos;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getOrdem() {
        return ordem;
    }

    public No getRaiz() {
        return raiz;
    }

    public void insere(double k, double posicao) {
        if (BuscaChave(raiz, k) == null) {
            if (raiz.getN() == 0) {
                raiz.setChave(0, k);
                raiz.setPosicao(0, posicao); //armazena posicao
                raiz.setN(raiz.getN() + 1);
            } else {
                No r = raiz;
                if (r.getN() == ordem - 1) {
                    No s = new No(ordem);
                    raiz = s;
                    s.setFolha(false);
                    s.setN(0);
                    s.setFilho(0, r);
                    divideNo(s, 0, r);
                    insereNoNaoCheio(s, k, posicao);
                } else {
                    insereNoNaoCheio(r, k, posicao);
                }
            }
            nElementos++;
        }
    }

    public void divideNo(No x, int i, No y) {
        int t = (int) Math.floor((ordem - 1) / 2);
        No z = new No(ordem);
        z.setFolha(y.isFolha());
        z.setN(t);

        for (int j = 0; j < t; j++) {
            if ((ordem - 1) % 2 == 0) {
                z.setChave(j, y.getChave(j + t));
                z.setPosicao(j, y.getPosicao(j + t)); //salva as posições
            } else {
                z.setChave(j, y.getChave(j + t + 1));
                z.setPosicao(j, y.getPosicao(j + t + 1));
            }
            y.setN(y.getN() - 1);
        }

        if (!y.isFolha()) {
            for (int j = 0; j < t + 1; j++) {
                if ((ordem - 1) % 2 == 0) {
                    z.setFilho(j, y.getFilho(j + t));
                } else {
                    z.setFilho(j, y.getFilho(j + t + 1));
                }
            }
        }

        y.setN(t);

        for (int j = x.getN(); j > i; j--) {
            x.setFilho(j + 1, x.getFilho(j));
        }

        x.setFilho(i + 1, z);

        for (int j = x.getN(); j > i; j--) {
            x.setChave(j, x.getChave(j - 1));
            x.setPosicao(j, x.getPosicao(j - 1));
        }

        if ((ordem - 1) % 2 == 0) {
            x.setChave(i, y.getChave(t - 1));
            x.setPosicao(i, y.getPosicao(t - 1));
            y.setN(y.getN() - 1);
        } else {
            x.setChave(i, y.getChave(t));
            x.setPosicao(i, y.getPosicao(t));
        }

        x.setN(x.getN() + 1);
    }

    public void insereNoNaoCheio(No x, double k, double posicao) {
        int i = x.getN() - 1;

        if (x.isFolha()) {
            while (i >= 0 && k < x.getChave(i)) {
                x.setChave(i + 1, x.getChave(i));
                x.setPosicao(i + 1, x.getPosicao(i));
                i--;
            }
            i++;
            x.setChave(i, k);
            x.setPosicao(i, posicao);
            x.setN(x.getN() + 1);
        } else {
            while (i >= 0 && k < x.getChave(i)) {
                i--;
            }
            i++;

            // Divide o filho se estiver cheio
            if (x.getFilho(i).getN() == ordem - 1) {
                divideNo(x, i, x.getFilho(i));
                if (k > x.getChave(i)) {
                    i++;
                }
            }

            insereNoNaoCheio(x.getFilho(i), k, posicao);
        }
    }

    public String BuscaChave(No X, double k) {
        int i = 0;

        while (i < X.getN() && k > X.getChave(i)) {
            i++;
        }

        if (i < X.getN() && k == X.getChave(i)) {
            double posicao = X.getPosicao(i);
            return carregaPagina(posicao); // Carrega a página do arquivo
        }

        if (X.isFolha()) {
            return null;
        } else {
            // Continua a busca no filho apropriado
            return BuscaChave(X.getFilho(i), k);
        }
    }

    public String carregaPagina(double posicao) { //usa RAF para ler os registros e retorna a página com os registros de lá
        try (RandomAccessFile arq = new RandomAccessFile("ordExt_resultadoTeste.txt", "r")) {
            arq.seek((long) posicao);
            byte pagina[] = new byte[4096];
            int registros = arq.read(pagina);
            return new String(pagina, 0, registros);
        }
        catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static void processaLote(ArvoreB tree, ArrayList<Double> lote, double pos) {
        double posicao = pos;
        for (Double registro : lote) {
            tree.insere(registro, pos);
            posicao += String.valueOf(registro).length() + 1; // Atualiza a posição
        }
    }
}
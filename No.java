public class No {
    public int n = 0; // Número de chaves no nó
    public double[] chave; // Array de chaves (tipo double)
    public No[] filho; // Array de nós filhos
    public boolean folha; // Indica se o nó é uma folha
    public double[] posicoes; // Array de posições no arquivo (tipo double)

    public No(int ordem) {
        // Inicializa os arrays com o tamanho adequado
        this.chave = new double[ordem - 1];
        this.filho = new No[ordem];
        this.posicoes = new double[ordem - 1]; // Inicializa o array de posições
        this.folha = true;
        this.n = 0;
    }

    public double getChave(int index) {
        return chave[index];
    }

    public void setChave(int index, double valor) {
        this.chave[index] = valor;
    }

    public No getFilho(int index) {
        return filho[index];
    }

    public void setFilho(int index, No no) {
        this.filho[index] = no;
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

    public double getPosicao(int index) {
        return posicoes[index];
    }

    public void setPosicao(int index, double posicao) {
        this.posicoes[index] = posicao;
    }
}
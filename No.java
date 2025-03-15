public class No {
    private int n = 0; // Number of keys in node
    private double[] chave; // Array of keys (primitive double)
    private No[] filho; // Array of child nodes
    private boolean folha; // Indicates if node is a leaf

    public No(int ordem) {
        // Initialize arrays with proper size
        this.chave = new double[ordem - 1];
        this.filho = new No[ordem];
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
}
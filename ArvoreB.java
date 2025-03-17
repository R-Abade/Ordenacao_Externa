public class ArvoreB {
    // B-Tree attributes
    private No raiz; // Root node
    private int ordem; // Order of the B-Tree
    private int nElementos; // Counter for number of elements in the B-tree

    // Constructor
    public ArvoreB(int n) {
        this.raiz = new No(n);
        this.ordem = n;
        nElementos = 0;
    }

    // Getters and Setters
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

    // Insert method with position parameter
    public void insere(double k, double posicao) {
        // Only insert if key doesn't already exist
        if (buscaNo(raiz, k) == null) {
            // If root is empty
            if (raiz.getN() == 0) {
                raiz.setChave(0, k);
                raiz.setPosicao(0, posicao);
                raiz.setN(raiz.getN() + 1);
            } else {
                No r = raiz;
                // Check if root is full
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

    // Original insert method (for backward compatibility)
    public void insere(double k) {
        // Only insert if key doesn't already exist
        if (buscaNo(raiz, k) == null) {
            // If root is empty
            if (raiz.getN() == 0) {
                raiz.setChave(0, k);
                raiz.setN(raiz.getN() + 1);
            } else {
                No r = raiz;
                // Check if root is full
                if (r.getN() == ordem - 1) {
                    No s = new No(ordem);
                    raiz = s;
                    s.setFolha(false);
                    s.setN(0);
                    s.setFilho(0, r);
                    divideNo(s, 0, r);
                    insereNoNaoCheio(s, k, 0);
                } else {
                    insereNoNaoCheio(r, k, 0);
                }
            }
            nElementos++;
        }
    }

    // Node splitting method
    public void divideNo(No x, int i, No y) {
        int t = (int) Math.floor((ordem - 1) / 2);
        No z = new No(ordem);
        z.setFolha(y.isFolha());
        z.setN(t);

        for (int j = 0; j < t; j++) {
            if ((ordem - 1) % 2 == 0) {
                z.setChave(j, y.getChave(j + t));
                z.setPosicao(j, y.getPosicao(j + t));
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

    // Method to insert a key in a non-full node
    public void insereNoNaoCheio(No x, double k, double posicao) {
        int i = x.getN() - 1;

        if (x.isFolha()) {
            // Find the correct position for insertion
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
            // Find the child where the key should be inserted
            while (i >= 0 && k < x.getChave(i)) {
                i--;
            }
            i++;

            // Split child if full
            if (x.getFilho(i).getN() == ordem - 1) {
                divideNo(x, i, x.getFilho(i));
                if (k > x.getChave(i)) {
                    i++;
                }
            }

            insereNoNaoCheio(x.getFilho(i), k, posicao);
        }
    }

    // Key search method (returns node)
    public No buscaNo(No X, double k) {
        int i = 0;

        // Find position where k might be
        while (i < X.getN() && k > X.getChave(i)) {
            i++;
        }

        // If found
        if (i < X.getN() && k == X.getChave(i)) {
            return X;
        }

        // If leaf and not found
        if (X.isFolha()) {
            return null;
        } else {
            // Continue search in appropriate child
            return buscaNo(X.getFilho(i), k);
        }
    }

    // Key search method (returns page content)
    public String buscaChave(No X, double k) {
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
            return buscaChave(X.getFilho(i), k);
        }
    }

    // Remove method
    public void Remove(double k) {
        if (buscaNo(this.raiz, k) != null) {
            No N = buscaNo(this.raiz, k);
            int i = 0;

            // Find key position
            while (N.getChave(i) < k) {
                i++;
            }

            // If N is a leaf
            if (N.isFolha()) {
                // Shift keys
                for (int j = i + 1; j < N.getN(); j++) {
                    N.setChave(j - 1, N.getChave(j));
                    N.setPosicao(j - 1, N.getPosicao(j));
                }
                N.setN(N.getN() - 1);
                if (N != this.raiz) {
                    Balanceia_Folha(N);
                }
            } else {
                // Replace with predecessor
                No S = Antecessor(this.raiz, k);
                double y = S.getChave(S.getN() - 1);
                double pos = S.getPosicao(S.getN() - 1);
                S.setN(S.getN() - 1);
                N.setChave(i, y);
                N.setPosicao(i, pos);
                Balanceia_Folha(S);
            }
            nElementos--;
        }
    }

    // Balancing leaf node method
    private void Balanceia_Folha(No F) {
        if (F.getN() < Math.floor((ordem - 1) / 2)) {
            No P = getPai(raiz, F);
            int j = 0;

            // Find child position
            while (P.getFilho(j) != F) {
                j++;
            }

            // Check left sibling
            if (j == 0 || P.getFilho(j - 1).getN() == Math.floor((ordem - 1) / 2)) {
                // Check right sibling
                if (j == P.getN() + 1 || P.getFilho(j).getN() == Math.floor((ordem - 1) / 2)) {
                    Diminui_Altura(F);
                } else {
                    Balanceia_Dir_Esq(P, j, P.getFilho(j), F);
                }
            } else {
                Balanceia_Esq_Dir(P, j - 1, P.getFilho(j - 1), F);
            }
        }
    }

    // Method to reduce height
    private void Diminui_Altura(No X) {
        if (X == this.raiz) {
            if (X.getN() == 0) {
                this.raiz = X.getFilho(0);
                X.setFilho(0, null);
            }
        } else {
            double t = Math.floor((ordem - 1) / 2);
            if (X.getN() < t) {
                No P = getPai(raiz, X);
                int j = 0;

                while (P.getFilho(j) != X) {
                    j++;
                }

                if (j > 0) {
                    Juncao_No(getPai(raiz, X), j);
                } else {
                    Juncao_No(getPai(raiz, X), j + 1);
                }
                Diminui_Altura(getPai(raiz, X));
            }
        }
    }

    // Method to balance from left to right
    private void Balanceia_Esq_Dir(No P, int e, No Esq, No Dir) {
        for (int i = 0; i < Dir.getN(); i++) {
            Dir.setChave(i + 1, Dir.getChave(i));
            Dir.setPosicao(i + 1, Dir.getPosicao(i));
        }

        if (!Dir.isFolha()) {
            for (int i = 0; i < Dir.getN(); i++) {
                Dir.setFilho(i + 1, Dir.getFilho(i));
            }
        }

        Dir.setN(Dir.getN() + 1);
        Dir.setChave(0, P.getChave(e));
        Dir.setPosicao(0, P.getPosicao(e));
        P.setChave(e, Esq.getChave(Esq.getN() - 1));
        P.setPosicao(e, Esq.getPosicao(Esq.getN() - 1));
        Dir.setFilho(0, Esq.getFilho(Esq.getN()));
        Esq.setN(Esq.getN() - 1);
    }

    // Method to balance from right to left
    private void Balanceia_Dir_Esq(No P, int e, No Dir, No Esq) {
        Esq.setN(Esq.getN() + 1);
        Esq.setChave(Esq.getN() - 1, P.getChave(e));
        Esq.setPosicao(Esq.getN() - 1, P.getPosicao(e));
        P.setChave(e, Dir.getChave(0));
        P.setPosicao(e, Dir.getPosicao(0));
        Esq.setFilho(Esq.getN(), Dir.getFilho(0));

        for (int j = 1; j < Dir.getN(); j++) {
            Dir.setChave(j - 1, Dir.getChave(j));
            Dir.setPosicao(j - 1, Dir.getPosicao(j));
        }

        if (!Dir.isFolha()) {
            for (int i = 1; i < Dir.getN() + 1; i++) {
                Dir.setFilho(i - 1, Dir.getFilho(i));
            }
        }

        Dir.setN(Dir.getN() - 1);
    }

    // Node merging method
    private void Juncao_No(No X, int i) {
        No Y = X.getFilho(i - 1);
        No Z = X.getFilho(i);

        int k = Y.getN();
        Y.setChave(k, X.getChave(i - 1));
        Y.setPosicao(k, X.getPosicao(i - 1));

        for (int j = 0; j < Z.getN(); j++) {
            Y.setChave(j + k + 1, Z.getChave(j));
            Y.setPosicao(j + k + 1, Z.getPosicao(j));
        }

        if (!Z.isFolha()) {
            for (int j = 0; j <= Z.getN(); j++) {
                Y.setFilho(j + k + 1, Z.getFilho(j));
            }
        }

        Y.setN(Y.getN() + Z.getN() + 1);
        X.setFilho(i, null);

        for (int j = i; j < X.getN(); j++) {
            X.setChave(j - 1, X.getChave(j));
            X.setPosicao(j - 1, X.getPosicao(j));
            X.setFilho(j, X.getFilho(j + 1));
        }

        X.setN(X.getN() - 1);
    }

    // Find predecessor node
    private No Antecessor(No N, double k) {
        int i = 0;
        while (i < N.getN() && N.getChave(i) < k) {
            i++;
        }

        if (N.isFolha()) {
            return N;
        } else {
            return Antecessor(N.getFilho(i), k);
        }
    }

    // Get parent node
    private No getPai(No T, No N) {
        if (this.raiz == N) {
            return null;
        }

        for (int j = 0; j <= T.getN(); j++) {
            if (T.getFilho(j) == N) {
                return T;
            }

            if (T.getFilho(j) != null && !T.getFilho(j).isFolha()) {
                No X = getPai(T.getFilho(j), N);
                if (X != null) {
                    return X;
                }
            }
        }

        return null;
    }

    // Clear B-Tree method
    public void LimparArvore(No N, int ordem) {
        if (N != null) {
            for (int i = 0; i <= N.getN(); i++) {
                if (!N.isFolha() && N.getFilho(i) != null) {
                    LimparArvore(N.getFilho(i), ordem);
                }
                N.setFilho(i, null);
            }

            if (N == this.raiz) {
                this.raiz = new No(ordem);
            }
        }

        nElementos = 0;
    }

    public String carregaPagina(double posicao) {
        try (java.io.RandomAccessFile arq = new java.io.RandomAccessFile("ordExt_resultadoTeste.txt", "r")) {
            arq.seek((long) posicao);

            StringBuilder pagina = new StringBuilder();
            String linha;
            int contador = 0;
            java.io.BufferedReader leitor = new java.io.BufferedReader(new java.io.FileReader(arq.getFD()));
            while ((linha = leitor.readLine()) != null && contador < ordem) {
                pagina.append(linha);
                pagina.append("\n");
                contador++;
            }

            return pagina.toString();
        }
        catch (java.io.IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static void processaLote(ArvoreB tree, java.util.ArrayList<Double> lote, double pos) {
        double posicao = pos;
        for (Double registro : lote) {
            tree.insere(registro, posicao);
            posicao += String.valueOf(registro).length() + 1;
        }
    }
}

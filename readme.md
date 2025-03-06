# Ordenação Externa usando Estratégia de Heap

## Introdução
Esta implementação Java demonstra um algoritmo de ordenação externa projetado para lidar com grandes conjuntos de dados que excedem a memória disponível. A solução emprega uma abordagem baseada em heap para fases eficientes de ordenação e mesclagem, seguindo o princípio do external merge sort com mesclagem balanceada múltipla.

## Características Principais
- **Ordenação em duas fases**: 
  1. **Divisão e Ordenação**: Divide a entrada em blocos do tamanho da memória disponível, ordenando com heap na memória
  2. **Mesclagem**: Combina arquivos ordenados usando fila de prioridade para ordem ótima de mesclagem
- **Operações com heap**:
  - Fila de prioridade para manter ordem durante as mesclagens
  - Estrutura de min-heap para recuperação eficiente de elementos
- **Gerenciamento eficiente de I/O**:
  - Tamanhos de buffer configuráveis (padrão 8KB)
  - Processamento em lotes com mesclagem de 10 vias
- **Gerenciamento de arquivos temporários**:
  - Criação e limpeza automática de arquivos intermediários
  - Ordenação em blocos de 100 elementos por vez

## Implementação Técnica
**Requisitos**: Java 8+  
**Processo de Ordenação**:
1. **Fase Inicial**:
   - Divide o arquivo de entrada em blocos de `MEMORIA_PRIMARIA` (100 elementos)
   - Ordena cada bloco usando `PriorityQueue` (complexidade O(n log n))
   - Armazena blocos ordenados como arquivos temporários

2. **Fase de Mesclagem**:
   - Realiza mesclagens balanceadas de 10 vias (`CAMINHOS` constante)
   - Usa comparador `ElementoHeap` para ordenação das mesclagens
   - Mantém complexidade O(k log k) usando estrutura de heap

**Parâmetros de Configuração**:
```java
private static final int tamanhoBuffer = 8194;    // Tamanho do buffer de I/O
private static final int CAMINHOS = 10;           // Ordem de mesclagem
private static final int MEMORIA_PRIMARIA = 100;  // Elementos por bloco
```

## Uso
1. **Compilação**:
```bash
javac Main.java Inter_Bal.java ElementoHeap.java
```

2. **Execução**:
```bash
java Main
```
- Arquivo de entrada: `ordExt.txt` (um double por linha)
- Saída: Cria `resultado.txt` com valores ordenados

3. **Monitoramento de Performance**:
- Exibe tempo de execução detalhado (horas, minutos, segundos, milissegundos)

## Personalização
Ajuste estas constantes no `Inter_Bal.java` para diferentes cargas:
- `tamanhoBuffer`: Otimize para dispositivos de armazenamento específicos
- `CAMINHOS`: Ajuste o fator de mesclagem para capacidades do hardware
- `MEMORIA_PRIMARIA`: Defina com base na memória disponível

## Formato de Arquivos
- Entrada: Texto plano com um valor numérico por linha
- Saída: Formatada para preservar precisão double (20 casas decimais)
- Limpeza automática de arquivos temporários

Esta implementação é adequada para ordenação de grandes volumes de dados que não cabem na memória, com complexidade de tempo dominada por operações O(n log n) e eficiência de I/O através de streams bufferizadas.

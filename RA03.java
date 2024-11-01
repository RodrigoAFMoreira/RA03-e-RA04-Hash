package avaliacao3;

import java.util.Random;

/**
 *
 * @author RAFMo
 */
class Registro {
    String codigo;

    Registro(String codigo) {
        this.codigo = codigo;
    }
}

class TabelaHashEncadeamento {

    Registro[][] tabela;
    int tamanho;
    int profundidade;

    TabelaHashEncadeamento(int tamanho, int profundidade) {
        this.tamanho = tamanho;
        this.profundidade = profundidade;
        tabela = new Registro[tamanho][profundidade];
    }

    void inserir(Registro registro, int tipoHash, int[] colisoes) {
        int indice = calcularIndiceHash(registro.codigo, tipoHash);

        boolean inserido = false;
        for (int i = 0; i < profundidade; i++) {
            if (tabela[indice][i] == null) {
                tabela[indice][i] = registro;
                inserido = true;
                break;
            }
        }
        if (!inserido) {
            colisoes[0]++;
        }
    }

    void realizarInsercoes(int quantidade, int tipoHash, int[] colisoes) {
        for (int i = 0; i < quantidade; i++) {
            Registro registro = gerarRegistroAleatorio();
            inserir(registro, tipoHash, colisoes);
        }
    }

    boolean buscar(String codigo, int tipoHash, int[] comparacoes) {
        int indice = calcularIndiceHash(codigo, tipoHash);

        for (int i = 0; i < profundidade; i++) {
            comparacoes[0]++;
            if (tabela[indice][i] != null && tabela[indice][i].codigo.equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    long realizarBuscas(int quantidade, int tipoHash, int[] comparacoes) {
        long tempoTotalBusca = 0;
        for (int i = 0; i < quantidade; i++) {
            Registro registroBusca = gerarRegistroAleatorio();
            tempoTotalBusca += Utilidades.medeEmMilise(() -> buscar(registroBusca.codigo, tipoHash, comparacoes));
        }
        return tempoTotalBusca;
    }

    private int calcularIndiceHash(String codigo, int tipoHash) {
        switch (tipoHash) {
            case 1:
                return hashRestoDivisao(codigo);
            case 2:
                return hashSomaDigitos(codigo);
            case 3:
                return hashMultiplicacaoCaractere(codigo);
        }
        return -1;
    }

    int hashRestoDivisao(String codigo) {
        int soma = codigo.chars().sum();
        return soma % tamanho;
    }

    int hashSomaDigitos(String codigo) {
        int soma = codigo.chars().map(Character::getNumericValue).sum();
        return soma % tamanho;
    }

    int hashMultiplicacaoCaractere(String codigo) {
        int hash = 0;
        int constante = 31;
        for (char c : codigo.toCharArray()) {
            hash = (hash * constante) + c;
        }
        return (hash < 0) ? -hash % tamanho : hash % tamanho;
    }

    static Registro gerarRegistroAleatorio() {
        Random random = new Random();
        String codigo = String.format("%09d", random.nextInt(1000000000));
        return new Registro(codigo);
    }
}

public class RA03 {

    public static void main(String[] args) {
        int[] tamanhosTabela = {100, 1000, 10000};
        int profundidadeEncadeamento = 10;
        int[] tamanhosConjunto = {1000000, 5000000, 20000000};

        for (int tipoHash = 1; tipoHash <= 3; tipoHash++) {
            long[] temposInsercao = new long[Utilidades.comprimento(tamanhosTabela)];
            long[] temposBusca = new long[Utilidades.comprimento(tamanhosTabela)];
            int[] colisoes = new int[Utilidades.comprimento(tamanhosTabela)];
            int[] comparacoes = new int[Utilidades.comprimento(tamanhosTabela)];

            for (int i = 0; i < Utilidades.comprimento(tamanhosTabela); i++) {
                int tamanhoTabela = tamanhosTabela[i];
                TabelaHashEncadeamento tabela = new TabelaHashEncadeamento(tamanhoTabela, profundidadeEncadeamento);
                int[] colisoesAtual = {0};
                long tempoInicioInsercao = System.currentTimeMillis();

                tabela.realizarInsercoes(tamanhosConjunto[i], tipoHash, colisoesAtual);
                long tempoFimInsercao = System.currentTimeMillis();
                temposInsercao[i] = tempoFimInsercao - tempoInicioInsercao;
                colisoes[i] = colisoesAtual[0];

                int[] comparacoesAtual = {0};
                long tempoTotalBusca = tabela.realizarBuscas(5, tipoHash, comparacoesAtual);
                temposBusca[i] = tempoTotalBusca / 5;
                comparacoes[i] = comparacoesAtual[0];
            }

            String[] seriesNames = {
                "Tempo de Insercao (ms) - Hash " + tipoHash,
                "Tempo de Busca (ms) - Hash " + tipoHash,
                "Colisoes (Normalizadas) - Hash " + tipoHash,
                "Comparacoes na Busca (Normalizadas) - Hash " + tipoHash
            };
            //Grafico
            double[] tamanhosTabelaDouble = new double[Utilidades.comprimento(tamanhosTabela)];
            double[] temposInsercaoDouble = new double[Utilidades.comprimentoLong(temposInsercao)];
            double[] temposBuscaDouble = new double[Utilidades.comprimentoLong(temposBusca)];
            double[] colisoesNormalizadas = new double[Utilidades.comprimento(colisoes)];
            double[] comparacoesNormalizadas = new double[Utilidades.comprimento(comparacoes)];
            double fatorNormalizacao = 10000.0;

            for (int i = 0; i <Utilidades.comprimento(tamanhosTabela); i++) {
                tamanhosTabelaDouble[i] = tamanhosTabela[i];
                temposInsercaoDouble[i] = temposInsercao[i];
                temposBuscaDouble[i] = temposBusca[i];
                colisoesNormalizadas[i] = colisoes[i] / fatorNormalizacao;
                comparacoesNormalizadas[i] = comparacoes[i] / fatorNormalizacao;
            }

            double[][] yDataSeries = {temposInsercaoDouble, temposBuscaDouble, colisoesNormalizadas, comparacoesNormalizadas};
            Utilidades.gerarGrafico(
                "Desempenho da Tabela Hash - Tipo de Hash " + tipoHash,
                "Tamanho da Tabela",
                "Tempo (ms) / Quantidade Normalizada",
                tamanhosTabelaDouble,
                seriesNames,
                yDataSeries
            );
        }
            System.out.println("Hash 1. . . RestoDivisao");
            System.out.println("Hash 2. . . SomaDigitos");
            System.out.println("Hash 3. . . MultiplicacaoCaractere");
    }  }

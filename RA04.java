package avaliacao3;

/**
 *
 * @author RAFMo
 */
class Resultado {

    double tempo;
    int troca;
    int iteracao;

    public Resultado(double tempo, int troca, int iteracao) {
        this.tempo = tempo;
        this.troca = troca;
        this.iteracao = iteracao;
    }
}

class ShellSort {

    public static Resultado ordenar(int[] array) {
        int troca = 0;
        int iteracao = 0;
        long inicio = System.nanoTime();
        for (int brecha = Utilidades.comprimento(array) / 2; brecha > 0; brecha /= 2) {
            for (int i = brecha; i < Utilidades.comprimento(array); i++) {
                int temp = array[i];
                int j;
                for (j = i; j >= brecha && array[j - brecha] > temp; j -= brecha) {
                    array[j] = array[j - brecha];
                    troca++;
                    iteracao++;
                }
                array[j] = temp;
                iteracao++;
            }
        }
        long fim = System.nanoTime();
        return new Resultado((fim - inicio) / 1e6, troca, iteracao);
    }

    public static double[] executaShellSort(int tamanhoVetor, int execucoes) {
        double totalShellTempo = 0;
        int totalShellTroca = 0;
        int totalShellIteracao = 0;

        for (int j = 0; j < execucoes; j++) {
            int[] array = Utilidades.geraRandomArray(tamanhoVetor, 19);
            Resultado resultado = ordenar(array);
            totalShellTempo += resultado.tempo;
            totalShellTroca += resultado.troca;
            totalShellIteracao += resultado.iteracao;
        }
        double mediaTempo = totalShellTempo / execucoes;
        double mediaTroca = (double) totalShellTroca / execucoes;
        double mediaIteracao = (double) totalShellIteracao / execucoes;
        return new double[]{mediaTempo, mediaTroca, mediaIteracao};
    }
}

class GnomeSort {

    public static Resultado ordenar(int[] array) {
        int troca = 0;
        int iteracao = 0;
        long inicio = System.nanoTime();
        int index = 0;
        while (index < Utilidades.comprimento(array)) {
            if (index == 0 || array[index] >= array[index - 1]) {
                index++;
            } else {
                int temp = array[index];
                array[index] = array[index - 1];
                array[index - 1] = temp;
                troca++;
                index--;
            }
            iteracao++;
        }
        long fim = System.nanoTime();
        return new Resultado((fim - inicio) / 1e6, troca, iteracao);
    }

    public static double[] executaGnomeSort(int tamanhoVetor, int execucoes) {
        double totalGnomeTempo = 0;
        int totalGnomeTroca = 0;
        int totalGnomeIteracao = 0;

        for (int j = 0; j < execucoes; j++) {
            int[] array = Utilidades.geraRandomArray(tamanhoVetor, 19);
            Resultado resultado = ordenar(array);
            totalGnomeTempo += resultado.tempo;
            totalGnomeTroca += resultado.troca;
            totalGnomeIteracao += resultado.iteracao;
        }
        double mediaTempo = totalGnomeTempo / execucoes;
        double mediaTroca = (double) totalGnomeTroca / execucoes;
        double mediaIteracao = (double) totalGnomeIteracao / execucoes;
        return new double[]{mediaTempo, mediaTroca, mediaIteracao};
    }
}

public class RA04 {

    public static void main(String[] args) {
        int[] tam = {1000, 10000, 100000, 500000, 1000000};
        double[] tamanhoVetor = new double[Utilidades.comprimento(tam)];

        double[] tempoShellSort = new double[Utilidades.comprimento(tam)];
        double[] trocaShellSort = new double[Utilidades.comprimento(tam)];
        double[] iteracaoShellSort = new double[Utilidades.comprimento(tam)];

        double[] tempoGnomeSort = new double[Utilidades.comprimento(tam)];
        double[] trocaGnomeSort = new double[Utilidades.comprimento(tam)];
        double[] iteracaoGnomeSort = new double[Utilidades.comprimento(tam)];

        for (int i = 0; i < Utilidades.comprimento(tam); i++) {
            int t = tam[i];
            tamanhoVetor[i] = t;
            // shell
            double[] resultadosShell = ShellSort.executaShellSort(t, 5);
            tempoShellSort[i] = resultadosShell[0];
            trocaShellSort[i] = resultadosShell[1];
            iteracaoShellSort[i] = resultadosShell[2];
            System.out.println("----------------------------");
            System.out.println("Shell Sort - Tamanho do vetor: " + t);
            System.out.println("Media de tempo (ms): " + tempoShellSort[i]);
            System.out.println("Media de trocas: " + trocaShellSort[i]);
            System.out.println("Media de iteracoes: " + iteracaoShellSort[i]);
            //gnome
            double[] resultadosGnome = GnomeSort.executaGnomeSort(t, 5);
            tempoGnomeSort[i] = resultadosGnome[0];
            trocaGnomeSort[i] = resultadosGnome[1];
            iteracaoGnomeSort[i] = resultadosGnome[2];
            System.out.println("Gnome Sort - Tamanho do vetor: " + t);
            System.out.println("Media de tempo (ms): " + tempoGnomeSort[i]);
            System.out.println("Media de trocas: " + trocaGnomeSort[i]);
            System.out.println("Media de iteracoes: " + iteracaoGnomeSort[i]);

            // Gráficos
            String[] serieShell = {"Tempo Shell Sort (ms)", "Trocas Shell Sort", "Iterações Shell Sort"};
            double[][] yDadoShell = {tempoShellSort, trocaShellSort, iteracaoShellSort};

            String[] serieGnome = {"Tempo Gnome Sort (ms)", "Trocas Gnome Sort", "Iterações Gnome Sort"};
            double[][] yDadoGnome = {tempoGnomeSort, trocaGnomeSort, iteracaoGnomeSort};

            Utilidades.gerarGrafico("Desempenho do Shell Sort", "Tamanho do Vetor", "Tempo (ms) / Contagem", tamanhoVetor, serieShell, yDadoShell);
            Utilidades.gerarGrafico("Desempenho do Gnome Sort", "Tamanho do Vetor", "Tempo (ms) / Contagem", tamanhoVetor, serieGnome, yDadoGnome);

        }
    }
}

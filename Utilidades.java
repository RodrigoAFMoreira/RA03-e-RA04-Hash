package avaliacao3;

/**
 *
 * @author RAFMo
 */
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.SwingWrapper;
import java.util.Random;

public class Utilidades {

    public static int[] geraRandomArray(int tam, long seed) {
        Random rand = new Random(seed);
        int[] array = new int[tam];
        for (int i = 0; i < tam; i++) {
            array[i] = rand.nextInt();
        }
        return array;
    }

    public static double medeEmMilise(Runnable task) {
        long inicio = System.nanoTime();
        task.run();
        long fim = System.nanoTime();
        return (fim - inicio) / 1e6;
    }

    public static void gerarGrafico(String titulo, String xAxisTitle, String yAxisTitle,
            double[] xDado, String[] serieNome, double[][] yDado) {

        XYChart chart = new XYChartBuilder().width(800).height(600)
                .title(titulo)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        for (int i = 0; i < comprimentoString(serieNome); i++) {
            chart.addSeries(serieNome[i], xDado, yDado[i]);
        }

        new SwingWrapper<>(chart).displayChart();
    }

    public static int comprimento(int[] array) {
        int count = 0;
        for (int ignored : array) {
            count++;
        }
        return count;
    }

    public static int comprimentoLong(long[] array) {
        int count = 0;
        for (long ignored : array) {
            count++;
        }
        return count;
    }

    public static int comprimentoString(String[] array) {
        int count = 0;
        for (String ignored : array) {
            count++;
        }
        return count;
    }

    public static int comprimentoChar(char[] array) {
        int count = 0;
        for (char ignored : array) {
            count++;
        }
        return count;
    }

}

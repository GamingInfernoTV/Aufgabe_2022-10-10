package de.medieninformatik.main;

import de.medieninformatik.threads.Sorter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

// An die Korrektur: ist die Implementation des Application-Plugins so den Anforderungen entsprechend?

/**
 * Main-Klasse zum Testen der Klasse {@link Sorter}
 *
 * @author Aaron Pöhlmann <code>30115</code>
 * @author Malte Kasolowsky <code>30114</code>
 */
public class Mainstream {
    /**
     * Testet {@link Sorter} mit eines Integer-Arrays und vergleicht es mit {@link Arrays#sort(Object[], Comparator)}
     *
     * @param args Nicht benutzt
     */
    public static void main(String[] args) {
        // größe des Arrays
        final int size = 10_000_000;
        // maximallänge der subArrays
        final int switchSize = 100;
        // misst die Laufzeit
        final int nThreads = Runtime.getRuntime().availableProcessors();
        // füllt das Array mit random int Werten
        final Integer[] values = new Random().ints(size).boxed().toArray(Integer[]::new);
        // kopiert das Array für ein vergleichs Array
        final Integer[] compare = Arrays.copyOf(values, values.length);
        final ForkJoinPool exec = new ForkJoinPool(nThreads);

        System.out.println("Sorting array using own implementation...");
        // aufruf der sortArray-Methode und der Sorter-Klasse
        sortArray(values, arr -> exec.invoke(new Sorter<>(switchSize, arr, Integer::compareTo)));

        System.out.println("Sorting array using build in sort...");
        sortArray(compare, Arrays::sort);

        boolean success = Arrays.equals(values, compare);
        System.out.printf("Sorting was%s successful.%n", success ? "" : " not");
        if (!success) {
            System.out.println(Arrays.toString(values));
            System.out.println(Arrays.toString(compare));
        }
    }

    /**
     * Die Methode dient dazu die Laufzeit des Programms zu messen und übergibt einem {@link Consumer},
     * der das Array sortieren soll, das zu sortierende Array
     *
     * @param values das zu sortierende Array
     * @param sorter Ein Consumer, der das Array sortieren soll
     * @param <T>    Der Objekttyp der Elemente im Array
     */
    private static <T> void sortArray(T[] values, Consumer<T[]> sorter) {
        long start = System.currentTimeMillis();
        sorter.accept(values);
        System.out.printf("Sorting needed: %dms%n", System.currentTimeMillis() - start);
    }
}

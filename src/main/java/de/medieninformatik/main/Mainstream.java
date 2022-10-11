package de.medieninformatik.main;

import de.medieninformatik.threads.Sorter;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public class Mainstream {
    public static void main(String[] args) {
        final int size = 10_000_000;
        final int switchSize = 100;
        final int nThreads = Runtime.getRuntime().availableProcessors();
        final Integer[] values = new Random().ints(size).boxed().toArray(Integer[]::new);
        final Integer[] compare = Arrays.copyOf(values, values.length);
        final ForkJoinPool exec = new ForkJoinPool(nThreads);

        System.out.println("Sorting array using own implementation");
        sortArray(values, arr -> exec.invoke(new Sorter<>(switchSize, arr, Integer::compareTo)));

        System.out.println("Sorting array using build in sort");
        sortArray(compare, Arrays::sort);

        boolean success = Arrays.equals(values, compare);
        System.out.printf("Sorting was%s successful%n", success ? "" : " not");
        if (!success) {
            System.out.println(Arrays.toString(values));
            System.out.println(Arrays.toString(compare));
        }
    }

    private static <T> void sortArray(T[] values, Consumer<T[]> sorter) {
        long start = System.currentTimeMillis();
        sorter.accept(values);
        System.out.printf("Sorting needed %dms%n", System.currentTimeMillis() - start);
    }
}

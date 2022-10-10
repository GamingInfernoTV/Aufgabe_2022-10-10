package de.medieninformatik.main;

import de.medieninformatik.threads.Sorter;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Mainstream {
    public static void main(String[] args) {
        final int size = 10_000_000;
        final int switchSize = 100;
        final int nThreads = Runtime.getRuntime().availableProcessors();
        final Integer[] values = new Random().ints(size).boxed().toArray(Integer[]::new);
        final ForkJoinPool exec = new ForkJoinPool(nThreads);
        Integer[] compare = Arrays.copyOf(values, values.length);
        long start = System.currentTimeMillis();
        exec.invoke(new Sorter<>(switchSize, values, Integer::compareTo));
        long end = System.currentTimeMillis() - start;
        System.out.println("Sorting needed: " + end);
        start = System.currentTimeMillis();
        Arrays.parallelSort(compare);
        end = System.currentTimeMillis() - start;
        System.out.println("Compare Sorting needed: " + end);
        boolean success = Arrays.compare(values, compare) == 0;
        System.out.printf("Sorting was%s successful", success ? "" : " not");
    }
}

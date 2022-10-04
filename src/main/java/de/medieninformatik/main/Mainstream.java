package de.medieninformatik.main;

import de.medieninformatik.threads.Sorter;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Mainstream {
    public static void main(String[] args) {
        final int size = 10_000;
        final int switchSize = (int) (size * 0.1);
        final int nThreads = Runtime.getRuntime().availableProcessors();
        final Integer[] values = (Integer[]) new Random().ints(size).boxed().toArray();

        ForkJoinPool exec = new ForkJoinPool(nThreads);
        Integer[] compare = Arrays.copyOf(values, values.length);
        Integer[] result = exec.invoke(new Sorter<>(switchSize, values));
        Arrays.parallelSort(compare);
        boolean success = Arrays.compare(result, compare) == 0;
        System.out.printf("Sorting was%s successful", success ? " " : " not");
    }
}

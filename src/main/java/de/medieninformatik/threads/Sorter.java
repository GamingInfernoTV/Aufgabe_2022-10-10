package de.medieninformatik.threads;

import java.util.concurrent.RecursiveTask;

public class Sorter<T> extends RecursiveTask<T[]> {
    private final int SWITCH_SIZE;
    private final T[] values;
    private final int left;
    private final int right;

    public Sorter(int switch_size, T[] values) {
        this(switch_size, values, 0, values.length); ;
    }

    private Sorter(int switch_size, T[] values, int left, int right) {
        SWITCH_SIZE = switch_size;
        this.values = values;
        this.left = left;
        this.right = right;
    }

    @Override
    protected T[] compute() {
        return null;
    }

    private void mergeSort(T[] values, int left, int right) {

    }

    private void insertionSort(T[] values) {

    }
}

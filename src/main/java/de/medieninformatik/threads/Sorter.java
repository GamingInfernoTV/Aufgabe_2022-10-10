package de.medieninformatik.threads;

import java.util.concurrent.RecursiveTask;

public class Sorter<T> extends RecursiveTask<T[]> {
    private final int SWITCH_SIZE;
    private final T[] values;

    public Sorter(int switch_size, T[] values) {
        SWITCH_SIZE = switch_size;
        this.values = values;
    }

    @Override
    protected T[] compute() {
        return null;
    }

    private void mergeSort(T[] values) {

    }

    private void insertionSort(T[] values) {

    }
}

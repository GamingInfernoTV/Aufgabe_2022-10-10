package de.medieninformatik.threads;

import java.util.Comparator;
import java.util.concurrent.RecursiveAction;

public class Sorter<T> extends RecursiveAction {
    private final int SWITCH_SIZE;
    private final T[] values;
    private final int left;
    private final int right;
    private final Comparator<T> comparator;

    public Sorter(int switch_size, T[] values, Comparator<T> c) {
        this(switch_size, values, 0, values.length, c);
    }

    private Sorter(int switch_size, T[] values, int left, int right, Comparator<T> c) {
        SWITCH_SIZE = switch_size;
        this.values = values;
        this.left = left;
        this.right = right;
        this.comparator = c;
    }

    @Override
    protected void compute() {
        if (left < right + SWITCH_SIZE) {
            // TODO: insertion sort
        }
        else {
            final int mid = (left + right) / 2;
            final Sorter<T> leftSort = new Sorter<>(SWITCH_SIZE, values, left, mid, comparator);
            final Sorter<T> rightSort = new Sorter<>(SWITCH_SIZE, values, mid, right, comparator);
            rightSort.fork();
            leftSort.compute();
            rightSort.join();
            // TODO: merge sorted arrays
        }
    }
}

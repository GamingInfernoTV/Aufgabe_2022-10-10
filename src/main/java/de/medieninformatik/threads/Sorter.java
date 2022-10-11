package de.medieninformatik.threads;

import java.util.Arrays;
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
        if (right - left <= SWITCH_SIZE) {
            // TODO: implement insertion sort from left to right (exclusive)
            for (int i = left; i < right; i++) {
                T valuesToSort = values[i];
                int j = i;
                while (j > left && comparator.compare(valuesToSort, values[j - 1]) < 0) {
                    values[j] = values[j - 1];
                    j--;
                }
                values[j] = valuesToSort; //Das kannst du dann löschen
            }
        } else {
            final int mid = (left + right) / 2;
            final Sorter<T> leftSort = new Sorter<>(SWITCH_SIZE, values, left, mid, comparator);
            final Sorter<T> rightSort = new Sorter<>(SWITCH_SIZE, values, mid, right, comparator);
            rightSort.fork();
            leftSort.compute();
            rightSort.join();
            int leftSize = mid - left;
            int rightSize = right - mid;

            T[] leftArr = Arrays.copyOfRange(this.values, left, mid);
            T[] rightArr = Arrays.copyOfRange(this.values, mid, right);

            int i = 0, j = 0, k = left;
            while (i < leftSize && j < rightSize) {
                if (comparator.compare(leftArr[i], rightArr[j]) <= 0) {
                    values[k++] = leftArr[i++];
                } else {
                    values[k++] = rightArr[j++];
                }
            }
            while (i < leftSize) {
                values[k++] = leftArr[i++];
            }
            while (j < rightSize) {
                values[k++] = rightArr[j++];
            }
        }
    }
}

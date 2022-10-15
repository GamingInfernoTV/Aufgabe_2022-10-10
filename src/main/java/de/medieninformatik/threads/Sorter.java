package de.medieninformatik.threads;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.RecursiveAction;

/**
 * Sorter Klasse mit den benötigten Variablen
 * @param <T> generischer Datentyp
 */
public class Sorter<T> extends RecursiveAction {
    private final int switchSize;
    private final T[] values;
    private final int left;
    private final int right;
    private final Comparator<T> comparator;

    /**
     * Konstruktor der Sorter-Klasse
     *
     * @param switchSize maximallänge der subArrays
     * @param values zu sortierendes Array
     * @param c übergebener Comparator
     */
    public Sorter(int switchSize, T[] values, Comparator<T> c) {
        this(switchSize, values, 0, values.length, c);
    }

    /**
     *Konstruktor der Sorter-Klasse der die benötigten Variablen übergibt
     *
     * @param switchSize maximallänge der subArrays
     * @param values zu sortierendes Array
     * @param left linkes Ende des Arrays
     * @param right rechtes Ende des Arrays
     * @param c übergebener Comparator
     */
    private Sorter(int switchSize, T[] values, int left, int right, Comparator<T> c) {
        this.switchSize = switchSize;
        this.values = values;
        this.left = left;
        this.right = right;
        this.comparator = c;
    }

    /**
     * Die compute Methode unterteilt das angegebene Array in subArrays.
     * Unterschreitet die Länge dieser subArrays einen bestimmten Wert, wird der
     * InsertionSort angewendent statt dem mergeSort. Am Ende werden die subArrays in der richtigen
     * Reihenfolge wieder zusammen gesetzt.
     */
    @Override
    protected void compute() {
        if (right - left <= switchSize) {
            insertionSort(values, left, right, comparator);
        } else {
            final int mid = (left + right) / 2;
            final Sorter<T> leftSort = new Sorter<>(switchSize, values, left, mid, comparator);
            final Sorter<T> rightSort = new Sorter<>(switchSize, values, mid, right, comparator);

            rightSort.fork();
            leftSort.compute();
            rightSort.join();

            merge(values, left, mid, right, comparator);
        }
    }

    /**
     * Die Methode sortiert die subArrays mittels InsertionSort-Algorithmus
     *
     * @param values zu sortierendes Array
     * @param left der Index, der das linke Ende des subArrays angibt
     * @param right der Index, der das rechte Ende des subArrays angibt
     * @param c übergebener Comparator
     * @param <T> generischer Datentyp
     */
    private static <T> void insertionSort(T[] values, int left, int right, Comparator<T> c) {
        for (int i = left; i < right; i++) {
            T valuesToSort = values[i];
            int j = i;
            while (j > left && c.compare(valuesToSort, values[j - 1]) < 0) {
                values[j] = values[j - 1];
                j--;
            }
            values[j] = valuesToSort;
        }
    }

    /**
     * Die Methode unterteilt das Array in subArrays (left, right) mittels mergeSort
     *
     * @param values zu sortierendes Arraay
     * @param left der Index, der das linke Ende des subArrays angibt
     * @param mid der Index, der die Mitte des subArrays angibt
     * @param right der Index, der das rechte Ende des subArrays angibt
     * @param c übergebener Comparator
     * @param <T> generischer Datentyp
     */
    private static <T> void merge(T[] values, int left, int mid, int right, Comparator<T> c) {
        final int leftSize = mid - left;
        final int rightSize = right - mid;
        final T[] leftArr = Arrays.copyOfRange(values, left, mid);
        final T[] rightArr = Arrays.copyOfRange(values, mid, right);

        int i = 0, j = 0, k = left;
        while (i < leftSize && j < rightSize) {
            values[k++] = c.compare(leftArr[i], rightArr[j]) <= 0 ? leftArr[i++] : rightArr[j++];
        }
        while (i < leftSize) {
            values[k++] = leftArr[i++];
        }
        while (j < rightSize) {
            values[k++] = rightArr[j++];
        }
    }
}

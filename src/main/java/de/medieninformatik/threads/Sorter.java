package de.medieninformatik.threads;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.RecursiveAction;

/**
 * Implementiert das Sortieren eines generischen Arrays mittels {@link RecursiveAction},
 * welche von einem Thread Pool ausgeführt werden.
 * Da beim Sortieren die ursprüngliche Instanz genutzt wird, wird keine {@link java.util.concurrent.RecursiveTask}
 * benötigt, da keine Rückgabe vonnöten ist.
 *
 * @param <T> Der Typ der Objekte, die im zu sortierenden Array beinhaltet sind
 * @author Aaron Pöhlmann <code>30115</code>
 * @author Malte Kasolowsky <code>30114</code>
 */
public class Sorter<T> extends RecursiveAction {
    private final int switchSize;
    private final transient T[] values;
    private final int left;
    private final int right;
    private final transient Comparator<T> comparator;

    /**
     * Konstruktor; ruft den Driver-Konstruktor mit dem gesamten Bereich des zu sortierenden Arrays auf
     *
     * @param switchSize Größe des subArrays, bei der statt des Merge Sorts
     *                   {@link Sorter#insertionSort(Object[], int, int, Comparator)} angewendet wird
     * @param values     das zu sortierende Array
     * @param c          Comparator zum Vergleiche der Array-Elemente
     */
    public Sorter(int switchSize, T[] values, Comparator<T> c) {
        this(switchSize, values, 0, values.length, c);
    }

    /**
     * Konstruktor; als Driver-Methode dient
     *
     * @param switchSize Größe des subArrays, bei der statt des Merge Sorts
     *                   {@link Sorter#insertionSort(Object[], int, int, Comparator)} angewendet wird
     * @param values     zu sortierendes Array
     * @param left       linkes Ende des subArrays
     * @param right      rechtes Ende des subArrays
     * @param c          Comparator zum Vergleiche der Array-Elemente
     */
    private Sorter(int switchSize, T[] values, int left, int right, Comparator<T> c) {
        this.switchSize = switchSize;
        this.values = values;
        this.left = left;
        this.right = right;
        this.comparator = c;
    }

    /**
     * Sortiert ein definiertes subArray mittels InsertionSort-Algorithmus
     *
     * @param values das zu sortierende Array
     * @param left   der Index, der das linke Ende des subArrays angibt
     * @param right  der Index, der das rechte Ende des subArrays angibt
     * @param c      Comparator zum Vergleiche der Array-Elemente
     * @param <T>    Objekttyp der Elemente des Arrays
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
     * Merged die beiden definierten subArrays in das Hauptarray
     *
     * @param values das zu sortierende Array
     * @param left   der Index, der das linke Ende des subArrays angibt
     * @param mid    der Index, der die Mitte des subArrays angibt
     * @param right  der Index, der das rechte Ende des subArrays angibt
     * @param c      Comparator zum Vergleiche der Array-Elemente
     * @param <T>    Objekttyp der Elemente des Arrays
     */
    private static <T> void merge(T[] values, int left, int mid, int right, Comparator<T> c) {
        final int leftSize = mid - left;
        final int rightSize = right - mid;
        final T[] leftArr = Arrays.copyOfRange(values, left, mid);
        final T[] rightArr = Arrays.copyOfRange(values, mid, right);

        int i = 0;
        int j = 0;
        int k = left;
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

    /**
     * Ist die Größe des dem Konstruktor übergebenen subArrays größer als die definierte SWITCH-SIZE,
     * so wird das subArray in zwei weitere, gleich große subArrays aufgeteilt und für diese mittels des
     * Thread Pools die compute-Methode rekursiv ausgeführt und diese beiden subArrays mit
     * {@link Sorter#merge(Object[], int, int, int, Comparator)} wieder zusammengeführt.
     * Ist die Größe des subArrays kleiner oder gleich der SWITCH-SIZE, so wird dieses mit
     * {@link Sorter#insertionSort(Object[], int, int, Comparator)} sortiert
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
}

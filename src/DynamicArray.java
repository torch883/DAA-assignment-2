import java.util.NoSuchElementException;

public class DynamicArray<T> {

    private Object[] data;
    private int size;

    public long lastOpMovements = 0;

    public DynamicArray() {
        data = new Object[8];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void growIfNeeded() {
        if (size == data.length) {
            Object[] bigger = new Object[data.length * 2];
            System.arraycopy(data, 0, bigger, 0, size);
            data = bigger;
        }
    }

    public void add(T x) {
        growIfNeeded();
        data[size] = x;
        size++;
        lastOpMovements = 1;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        }
        growIfNeeded();
        long moved = 0;
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            moved++;
        }
        data[index] = x;
        size++;
        lastOpMovements = moved;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        }
        @SuppressWarnings("unchecked")
        T removed = (T) data[index];
        long moved = 0;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            moved++;
        }
        data[size - 1] = null;
        size--;
        lastOpMovements = moved;
        return removed;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        }
        return (T) data[index];
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null ? x == null : data[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    public int countComparisonsForContains(T x) {
        int comparisons = 0;
        for (int i = 0; i < size; i++) {
            comparisons++;
            if (data[i] == null ? x == null : data[i].equals(x)) {
                break;
            }
        }
        return comparisons;
    }
}
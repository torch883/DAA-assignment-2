public class MinHeap<T extends Comparable<T>> {

    private Object[] data;
    private int size;

    public long comparisonCount = 0;

    public MinHeap() {
        data = new Object[16];
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

    @SuppressWarnings("unchecked")
    private T at(int i) {
        return (T) data[i];
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i) { return 2 * i + 1; }
    private int right(int i) { return 2 * i + 2; }

    private void swap(int i, int j) {
        Object tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public void insert(T x) {
        growIfNeeded();
        data[size] = x;
        int i = size;
        size++;
        siftUp(i);
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = parent(i);
            comparisonCount++;
            if (at(i).compareTo(at(p)) < 0) {
                swap(i, p);
                i = p;
            } else {
                break;
            }
        }
    }

    public T peekMin() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("heap is empty");
        }
        return at(0);
    }

    public T extractMin() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("heap is empty");
        }
        T min = at(0);
        size--;
        data[0] = data[size];
        data[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return min;
    }

    private void siftDown(int i) {
        while (true) {
            int l = left(i);
            int r = right(i);
            int smallest = i;

            if (l < size) {
                comparisonCount++;
                if (at(l).compareTo(at(smallest)) < 0) {
                    smallest = l;
                }
            }
            if (r < size) {
                comparisonCount++;
                if (at(r).compareTo(at(smallest)) < 0) {
                    smallest = r;
                }
            }
            if (smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    public boolean isValidHeap() {
        for (int i = 1; i < size; i++) {
            if (at(i).compareTo(at(parent(i))) < 0) {
                return false;
            }
        }
        return true;
    }
}
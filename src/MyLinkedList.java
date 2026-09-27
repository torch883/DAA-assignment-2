public class MyLinkedList<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public long lastOpMovements = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T x) {
        Node<T> node = new Node<>(x);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
        lastOpMovements = 1;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node<T> node = new Node<>(x);
        long steps = 0;
        if (index == 0) {
            node.next = head;
            head = node;
            if (tail == null) tail = node;
        } else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                steps++;
            }
            node.next = prev.next;
            prev.next = node;
        }
        size++;
        lastOpMovements = steps + 1;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        }
        long steps = 0;
        T removedValue;
        if (index == 0) {
            removedValue = head.value;
            head = head.next;
            if (head == null) tail = null;
        } else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                steps++;
            }
            Node<T> toRemove = prev.next;
            removedValue = toRemove.value;
            prev.next = toRemove.next;
            if (toRemove == tail) tail = prev;
        }
        size--;
        lastOpMovements = steps + 1;
        return removedValue;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        }
        Node<T> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.value;
    }

    public boolean contains(T x) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.value == null ? x == null : cur.value.equals(x)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public int countComparisonsForContains(T x) {
        int comparisons = 0;
        Node<T> cur = head;
        while (cur != null) {
            comparisons++;
            if (cur.value == null ? x == null : cur.value.equals(x)) {
                break;
            }
            cur = cur.next;
        }
        return comparisons;
    }
}
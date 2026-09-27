import java.util.Random;

public class Tests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();
        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
        } else {
            failed++;
            System.out.println("FAILED: " + name);
        }
    }

    private static void testDynamicArray() {
        DynamicArray<Integer> a = new DynamicArray<>();

        check("empty size == 0", a.size() == 0);
        check("empty isEmpty", a.isEmpty());
        check("empty contains false", !a.contains(1));

        a.add(10);
        check("one element size", a.size() == 1);
        check("one element get(0)", a.get(0) == 10);

        a.add(20);
        a.add(10);
        check("duplicates contains", a.contains(10));
        check("size after adds", a.size() == 3);

        a.add(1, 99);
        check("add(index) shifts correctly", a.get(1) == 99 && a.get(2) == 20);

        int removed = a.remove(1);
        check("remove returns correct value", removed == 99);
        check("remove shrinks size", a.size() == 3);

        a.add(a.size(), 555);
        check("add at end via index", a.get(a.size() - 1) == 555);

        boolean threw = false;
        try {
            a.get(-1);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        check("get(-1) throws", threw);

        threw = false;
        try {
            a.get(a.size());
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        check("get(size) throws", threw);

        DynamicArray<Integer> big = new DynamicArray<>();
        java.util.ArrayList<Integer> ref = new java.util.ArrayList<>();
        Random rnd = new Random(42);
        for (int i = 0; i < 20000; i++) {
            int v = rnd.nextInt(1000);
            big.add(v);
            ref.add(v);
        }
        boolean matches = true;
        for (int i = 0; i < ref.size(); i++) {
            if (!big.get(i).equals(ref.get(i))) {
                matches = false;
                break;
            }
        }
        check("large input matches ArrayList", matches);
    }

    private static void testLinkedList() {
        MyLinkedList<Integer> l = new MyLinkedList<>();

        check("empty size == 0", l.size() == 0);
        check("empty contains false", !l.contains(5));

        l.add(1);
        l.add(2);
        l.add(3);
        check("size after 3 adds", l.size() == 3);
        check("get(0)", l.get(0) == 1);
        check("get(2)", l.get(2) == 3);

        l.add(0, 0);
        check("add at head", l.get(0) == 0 && l.get(1) == 1);

        l.add(2, 100);
        check("add in middle", l.get(2) == 100);

        int removedHead = l.remove(0);
        check("remove head value", removedHead == 0);

        int removedTail = l.remove(l.size() - 1);
        check("remove tail value", removedTail == 3);

        l.add(100);
        check("duplicate values allowed", l.contains(100));

        boolean threw = false;
        try {
            l.remove(999);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        check("remove invalid index throws", threw);

        MyLinkedList<Integer> big = new MyLinkedList<>();
        java.util.LinkedList<Integer> ref = new java.util.LinkedList<>();
        Random rnd = new Random(42);
        for (int i = 0; i < 5000; i++) {
            int v = rnd.nextInt(1000);
            big.add(v);
            ref.add(v);
        }
        boolean matches = true;
        for (int i = 0; i < ref.size(); i++) {
            if (!big.get(i).equals(ref.get(i))) {
                matches = false;
                break;
            }
        }
        check("large input matches java.util.LinkedList", matches);
    }
    private static void testMinHeap() {
        MinHeap<Integer> h = new MinHeap<>();

        boolean threw = false;
        try {
            h.peekMin();
        } catch (java.util.NoSuchElementException e) {
            threw = true;
        }
        check("peekMin on empty throws", threw);

        h.insert(5);
        check("single element peekMin", h.peekMin() == 5);
        check("heap property after single insert", h.isValidHeap());

        h.insert(3);
        h.insert(8);
        h.insert(1);
        h.insert(3);
        check("heap property after several inserts", h.isValidHeap());
        check("peekMin is the minimum", h.peekMin() == 1);

        Random rnd = new Random(42);
        MinHeap<Integer> big = new MinHeap<>();
        int n = 5000;
        for (int i = 0; i < n; i++) {
            big.insert(rnd.nextInt(100000));
        }
        int prev = Integer.MIN_VALUE;
        boolean nonDecreasing = true;
        boolean validAtEveryStep = true;
        for (int i = 0; i < n; i++) {
            if (!big.isValidHeap()) validAtEveryStep = false;
            int cur = big.extractMin();
            if (cur < prev) nonDecreasing = false;
            prev = cur;
        }
        check("extractMin non-decreasing over " + n + " elements", nonDecreasing);
        check("heap property held before every extraction", validAtEveryStep);
        check("heap empty after extracting everything", big.isEmpty());
    }
}
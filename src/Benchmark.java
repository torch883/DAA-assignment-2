import java.util.Random;

public class Benchmark {

    static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    static final int REPEATS = 5;
    static final long SEED = 42;

    public static void main(String[] args) {
        System.out.println("workload,structure,n,phase,avg_time_ns,metric_name,metric_value");
        workload1RandomAccess();
        workload2Search();
        workload3InsertRemove();
        workload4PriorityProcessing();
    }

    private static void workload1RandomAccess() {
        for (int n : SIZES) {
            int[] baseData = randomInts(n, SEED);
            int[] indices = randomIndices(10_000, n, SEED + 1);

            long arrTime = 0;
            for (int r = 0; r < REPEATS; r++) {
                DynamicArray<Integer> arr = new DynamicArray<>();
                for (int v : baseData) arr.add(v);
                long start = System.nanoTime();
                for (int idx : indices) arr.get(idx);
                arrTime += System.nanoTime() - start;
            }
            System.out.println("1,DynamicArray," + n + ",get," + (arrTime / REPEATS) + ",accesses," + indices.length);

            long listTime = 0;
            for (int r = 0; r < REPEATS; r++) {
                MyLinkedList<Integer> list = new MyLinkedList<>();
                for (int v : baseData) list.add(v);
                long start = System.nanoTime();
                for (int idx : indices) list.get(idx);
                listTime += System.nanoTime() - start;
            }
            System.out.println("1,LinkedList," + n + ",get," + (listTime / REPEATS) + ",accesses," + indices.length);
        }
    }

    private static void workload2Search() {
        for (int n : SIZES) {
            int[] baseData = randomInts(n, SEED);
            int[] queries = randomInts(1_000, SEED + 2);

            long arrTime = 0;
            long arrComparisons = 0;
            for (int r = 0; r < REPEATS; r++) {
                DynamicArray<Integer> arr = new DynamicArray<>();
                for (int v : baseData) arr.add(v);
                long start = System.nanoTime();
                long comps = 0;
                for (int q : queries) comps += arr.countComparisonsForContains(q);
                arrTime += System.nanoTime() - start;
                arrComparisons = comps;
            }
            System.out.println("2,DynamicArray," + n + ",contains," + (arrTime / REPEATS) + ",comparisons," + arrComparisons);

            long listTime = 0;
            long listComparisons = 0;
            for (int r = 0; r < REPEATS; r++) {
                MyLinkedList<Integer> list = new MyLinkedList<>();
                for (int v : baseData) list.add(v);
                long start = System.nanoTime();
                long comps = 0;
                for (int q : queries) comps += list.countComparisonsForContains(q);
                listTime += System.nanoTime() - start;
                listComparisons = comps;
            }
            System.out.println("2,LinkedList," + n + ",contains," + (listTime / REPEATS) + ",comparisons," + listComparisons);
        }
    }

    private static void workload3InsertRemove() {
        int m = 1_000;
        for (int n : SIZES) {
            int[] baseData = randomInts(n, SEED);

            benchInsert(baseData, n, m, 0, "DynamicArray-front");
            benchInsert(baseData, n, m, 0, "LinkedList-front");
            benchRemove(baseData, n, m, 0, "DynamicArray-front");
            benchRemove(baseData, n, m, 0, "LinkedList-front");

            int mid = n / 2;
            benchInsert(baseData, n, m, mid, "DynamicArray-middle");
            benchInsert(baseData, n, m, mid, "LinkedList-middle");
            benchRemove(baseData, n, m, mid, "DynamicArray-middle");
            benchRemove(baseData, n, m, mid, "LinkedList-middle");
        }
    }

    private static void benchInsert(int[] baseData, int n, int m, int index, String label) {
        boolean isArray = label.startsWith("DynamicArray");
        long totalTime = 0;
        long totalMoves = 0;
        for (int r = 0; r < REPEATS; r++) {
            long moves = 0;
            long start, elapsed;
            if (isArray) {
                DynamicArray<Integer> arr = new DynamicArray<>();
                for (int v : baseData) arr.add(v);
                start = System.nanoTime();
                for (int i = 0; i < m; i++) {
                    arr.add(index, i);
                    moves += arr.lastOpMovements;
                }
                elapsed = System.nanoTime() - start;
            } else {
                MyLinkedList<Integer> list = new MyLinkedList<>();
                for (int v : baseData) list.add(v);
                start = System.nanoTime();
                for (int i = 0; i < m; i++) {
                    list.add(index, i);
                    moves += list.lastOpMovements;
                }
                elapsed = System.nanoTime() - start;
            }
            totalTime += elapsed;
            totalMoves = moves;
        }
        System.out.println("3," + label + "," + n + ",insert," + (totalTime / REPEATS) + ",movements," + totalMoves);
    }

    private static void benchRemove(int[] baseData, int n, int m, int index, String label) {
        boolean isArray = label.startsWith("DynamicArray");
        long totalTime = 0;
        long totalMoves = 0;
        for (int r = 0; r < REPEATS; r++) {
            long moves = 0;
            long start, elapsed;
            int removeCount = Math.min(m, n);
            if (isArray) {
                DynamicArray<Integer> arr = new DynamicArray<>();
                for (int v : baseData) arr.add(v);
                start = System.nanoTime();
                for (int i = 0; i < removeCount; i++) {
                    int safeIndex = Math.min(index, arr.size() - 1);
                    arr.remove(safeIndex);
                    moves += arr.lastOpMovements;
                }
                elapsed = System.nanoTime() - start;
            } else {
                MyLinkedList<Integer> list = new MyLinkedList<>();
                for (int v : baseData) list.add(v);
                start = System.nanoTime();
                for (int i = 0; i < removeCount; i++) {
                    int safeIndex = Math.min(index, list.size() - 1);
                    list.remove(safeIndex);
                    moves += list.lastOpMovements;
                }
                elapsed = System.nanoTime() - start;
            }
            totalTime += elapsed;
            totalMoves = moves;
        }
        System.out.println("3," + label + "," + n + ",remove," + (totalTime / REPEATS) + ",movements," + totalMoves);
    }

    private static void workload4PriorityProcessing() {
        for (int n : SIZES) {
            int[] baseData = randomInts(n, SEED);

            long insertTime = 0;
            long extractTime = 0;
            long comparisons = 0;
            boolean nonDecreasingAll = true;

            for (int r = 0; r < REPEATS; r++) {
                MinHeap<Integer> heap = new MinHeap<>();
                heap.comparisonCount = 0;

                long start = System.nanoTime();
                for (int v : baseData) heap.insert(v);
                insertTime += System.nanoTime() - start;

                start = System.nanoTime();
                int prev = Integer.MIN_VALUE;
                boolean nonDecreasing = true;
                for (int i = 0; i < n; i++) {
                    int cur = heap.extractMin();
                    if (cur < prev) nonDecreasing = false;
                    prev = cur;
                }
                extractTime += System.nanoTime() - start;

                comparisons = heap.comparisonCount;
                nonDecreasingAll = nonDecreasingAll && nonDecreasing;
            }

            System.out.println("4,MinHeap," + n + ",insert," + (insertTime / REPEATS) + ",comparisons," + comparisons);
            System.out.println("4,MinHeap," + n + ",extractMin," + (extractTime / REPEATS) + ",non_decreasing," + (nonDecreasingAll ? 1 : 0));
        }
    }

    private static int[] randomInts(int count, long seed) {
        Random rnd = new Random(seed);
        int[] out = new int[count];
        for (int i = 0; i < count; i++) out[i] = rnd.nextInt(1_000_000);
        return out;
    }

    private static int[] randomIndices(int count, int bound, long seed) {
        Random rnd = new Random(seed);
        int[] out = new int[count];
        for (int i = 0; i < count; i++) out[i] = bound == 0 ? 0 : rnd.nextInt(bound);
        return out;
    }
}
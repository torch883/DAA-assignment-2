import java.util.Random;

public class Benchmark {

    static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    static final int REPEATS = 5;
    static final long SEED = 42;

    public static void main(String[] args) {
        System.out.println("workload,structure,n,phase,avg_time_ns,metric_name,metric_value");
        workload1RandomAccess();
        workload2Search();
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
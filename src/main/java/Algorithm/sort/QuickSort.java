package Algorithm.sort;

import java.util.List;

public class QuickSort {
    // 快速排序，a是数组，n表示数组的大小
    public static void quickSort(List<Integer> a, int n) {
        if (n <=0) return;
        quickSortInternally(a, 0, n - 1);
    }

    // 在 O(n) 的时间复杂度内查找一个无序数组中的第 K 大元素
    public static int KthSmallest(List<Integer> a, int k) {
        if (a == null || k > a.size())
            return -1;

        int i = partition(a, 0, a.size() - 1); // 获取分区点
        while (k != i + 1){
            if (i + 1 < k) {
                i = partition(a, i + 1, a.size() - 1); // 获取分区点
            } else {
                i = partition(a, 0, i - 1); // 获取分区点
            }
        }

        return a.get(i);
    }

    private static void quickSortInternally(List<Integer> a, int p, int q) {
        if (p >= q) return;

        int i = partition(a, p, q); // 获取分区点
        quickSortInternally(a, p, i - 1);
        quickSortInternally(a, i + 1, q);

    }

    private static int partition(List<Integer> a, int p, int q) {
        int pivot = a.get(q);
        int i = p;
        for (int j = p; j < q; ++j) {
            if (a.get(j) < pivot) {
                if (i != j) {
                    swipe(a, i, j);
                }
                ++i;
            }
        }
        swipe(a, i, q);
        return i;
    }

    private static void swipe(List<Integer> a, int i, int j) {
        if (i == j)
            return;

        int tmp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, tmp);
    }


}

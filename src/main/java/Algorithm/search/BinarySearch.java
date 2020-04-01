package Algorithm.search;

import java.util.List;

public class BinarySearch {
    // 二分查找，a表示数组，n表示数组大小，value表示要查找的数据
    public static int binarySearch(List<Integer> a, int n, int value) {
        if (n < 1)
            return -1;

        int low = 0;
        int high = n - 1;

        while (low <= high){
            int mid = low + ((high - low) >> 1);
            if(a.get(mid) == value) return mid;
            else if(a.get(mid) > value) high = mid - 1;
            else if(a.get(mid) < value) low = mid + 1;
        }
        return -1;
    }

    // 二分查找的递归，a表示数组，n表示数组大小，value表示要查找的数据
    public static int binarySearchInternally(List<Integer> a, int n, int value) {
        if (n < 1) return -1;
        return bsearchInternally(a, 0, n - 1, value);
    }

    private static int bsearchInternally(List<Integer> a, int low, int high, int value) {
        if (low > high) return -1;
        int mid = low + ((high - low) >> 1);
        if (a.get(mid) == value) return mid;
        else if (a.get(mid) > value)
            return bsearchInternally(a, low, mid - 1, value);
        else return bsearchInternally(a, mid + 1, high, value);
    }
}

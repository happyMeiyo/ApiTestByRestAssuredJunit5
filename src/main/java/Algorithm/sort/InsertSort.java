package Algorithm.sort;

import java.util.List;

public class InsertSort {
    // 插入排序，a表示数组，n表示数组大小
    public static void insertionSortArray(Integer[] a, int n) {
        if (n <= 1)
            return;

        for(int i=1; i < n; ++i) {
            int value = a[i];
            int j = i -1;

            for (; j >= 0; --j) {
                if (a[j] > value) {
                    a[j+1] = a[j];
                }
                else break;
            }
            a[j+1] = value;
        }
    }

    // 插入排序，a表示数组，n表示数组大小
    public static void insertionSortList(List<Integer> a, int n) {
        if (n <= 1)
            return;

        for(int i=1; i < n; ++i) {
            int value = a.get(i);
            int j = i - 1;

            // 查找插入的位置
            for (; j >=0; --j){
                if(a.get(j) > value){
                    a.set(j + 1, a.get(j)); //移动数据
                }
                else break;
            }
            a.set(j+1, value); //插入数据
        }
    }
}

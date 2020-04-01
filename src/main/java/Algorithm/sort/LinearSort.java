package Algorithm.sort;

import java.util.List;

public class LinearSort {

    // 计数排序，a是数组，n是数组大小。假设数组中存储的都是非负整数。
    public static void countingSort(List<Integer> a, int n) {
        if (n <= 1) return;

        //查找数组中数据的范围
        int max = 0;
        for (int num :a) {
            if (max < num) max = num;
        }

        int[] c = new int[max + 1];  //请一个计数数组c，下标大小[0,max]
        for (int i = 0; i < max + 1; ++i) {
            c[i] = 0;
        }

        // 计算每个元素的个数，放入c中
        for (int num:a) {
            c[num]++;
        }

        // 依次累加
        for (int i = 1; i <= max; ++i){
            c[i] = c[i - 1] + c[i];
        }

        // 临时数组r，存储排序之后的结果
        int[] r = new int[n];
        for(int i = n - 1; i >= 0; --i){
            int index = c[a.get(i)] - 1;
            r[index] = a.get(i);
            c[a.get(i)]--;

        }

        // 将结果拷贝给a数组
        for(int i = 0; i < n; ++i)
        {
            a.set(i, r[i]);
        }
    }
}

import java.util.List;

public class Algorithm {

    // 冒泡排序，a表示数组，n表示数组大小
    public void bubbleSortList(List<Integer> a, int n) {
        if (n <= 1)
            return;

        for(int i=0; i < n; ++i) {
            // 提前退出冒泡循环的标志位
            boolean flag = false;
            for (int j = 0; j < n - i -1; ++j) {
                if (a.get(j) > a.get(j + 1)) {
                    int k = a.get(j);
                    a.set(j, a.get(j + 1));
                    a.set(j + 1, k);
                    flag = true; // 表示有数据交换
                }
            }
            // 没有数据交换，提前退出
            if (!flag)
                return;
        }
    }

    public void bubbleSort(Integer[] a, int n) {
        if (n <= 1)
            return;

        for(int i=0; i < n; ++i) {
            // 提前退出冒泡循环的标志位
            boolean flag = false;
            for (int j = 0; j < n - i -1; ++j) {
                if (a[j] > a[j + 1]) {
                    int k = a[j];
                    a[j]=a[j + 1];
                    a[j + 1] = k;
                    flag = true; // 表示有数据交换
                }
            }
            // 没有数据交换，提前退出
            if (!flag)
                return;
        }
    }

    // 插入排序，a表示数组，n表示数组大小
    public void insertionSortArray(Integer[] a, int n) {
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
    public void insertionSortList(List<Integer> a, int n) {
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

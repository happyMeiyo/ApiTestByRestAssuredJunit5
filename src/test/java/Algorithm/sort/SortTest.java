package Algorithm.sort;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@DisplayName("测试排序算法")
class SortTest {

    static Stream<Arguments> intListProvider() {
        return Stream.of(
                arguments(Arrays.asList(1,4,3,2,2,1), Arrays.asList(1,1,2,2,3,4), 6),
                arguments(Collections.singletonList(1), Collections.singletonList(1), 1),
                arguments(Arrays.asList(4,2), Arrays.asList(2,4), 2),
                arguments(Arrays.asList(1,2,3), Arrays.asList(1,2,3), 3),
                arguments(Arrays.asList(6,5,4,3,2,1), Arrays.asList(1,2,3,4,5,6), 6),
                arguments(Arrays.asList(2,5,3,0,2,3,0,3),Arrays.asList(0,0,2,2,3,3,3,5),8)
        );
    }

    @DisplayName("测试冒泡排序")
    @Description("测试冒泡排序")
    @ParameterizedTest(name = "冒泡排序测试, a={0}, len={1}")
    @MethodSource("intListProvider")
    void bubbleSortList(List<Integer> a, List<Integer> b, int len) {
        BubbleSort.bubbleSortList(a, len);
        assertIterableEquals(b,a,"排序成功");

    }

    @DisplayName("测试冒泡排序")
    @Description("测试冒泡排序")
    @ParameterizedTest(name = "冒泡排序测试, a={0}, len={1}")
    @MethodSource("intListProvider")
    void bubbleSortArray(List<Integer> aList, List<Integer> bList, int len) {

        Integer[] a = new Integer[len];
        aList.toArray(a);
        Integer[] b = new Integer[len];
        bList.toArray(b);
        BubbleSort.bubbleSort(a, len);

        assertArrayEquals(a, b, "排序成功");
    }


    @DisplayName("测试插入排序")
    @Description("测试插入排序")
    @ParameterizedTest(name = "插入排序测试, a={0}, len={1}")
    @MethodSource("intListProvider")
    void insertionSort(List<Integer> aList, List<Integer> bList, int len) {
        Integer[] a = new Integer[len];
        aList.toArray(a);
        Integer[] b = new Integer[len];
        bList.toArray(b);
        InsertSort.insertionSortArray(a, len);

        assertArrayEquals(a, b, "排序成功");
    }

    @DisplayName("测试插入排序")
    @Description("测试插入排序")
    @ParameterizedTest(name = "插入排序测试, a={0}, len={1}")
    @MethodSource("intListProvider")
    void insertionSortList(List<Integer> a, List<Integer> b, int len) {
        InsertSort.insertionSortList(a, len);
        assertIterableEquals(b,a,"排序成功");

    }


    @DisplayName("测试快速排序")
    @Description("测试快速排序")
    @ParameterizedTest(name = "测试快速排序, a={0}, len={1}")
    @MethodSource("intListProvider")
    void quickSortList(List<Integer> a, List<Integer> b, int len) {
        QuickSort.quickSort(a, len);
        assertIterableEquals(b,a,"排序成功");

    }

    @DisplayName("测试计数排序")
    @Description("测试计数排序")
    @ParameterizedTest(name = "测试计数排序, a={0}, len={1}")
    @MethodSource("intListProvider")
    void linearSortList(List<Integer> a, List<Integer> b, int len) {
        LinearSort.countingSort(a, len);
        assertIterableEquals(b,a,"排序成功");

    }


    static Stream<Arguments> intListAndKProvider() {
        return Stream.of(
                arguments(Arrays.asList(1,4,3,2,2,1), 2, 1),
                arguments(Collections.singletonList(1), 1, 1),
                arguments(Arrays.asList(4,2), 1, 2),
                arguments(Arrays.asList(1,2,3), 3, 3),
                arguments(Arrays.asList(6,5,4,3,2,1), 6, 6),
                arguments(Arrays.asList(6,5,4,3,2,1), 7, -1)
        );
    }

    @DisplayName("测试查找第K大元素")
    @Description("测试查找第K大元素")
    @ParameterizedTest(name = "测试查找第K大元素， k = {1}")
    @MethodSource("intListAndKProvider")
    void KthSmallest(List<Integer> a, int k, int value) {
        int result = QuickSort.KthSmallest(a, k);
        assertEquals(value,result,"获取值成功");
    }




}
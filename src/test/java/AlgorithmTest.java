
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class AlgorithmTest {
    Algorithm agh = new Algorithm();

    static Stream<Arguments> intListProvider() {
        return Stream.of(
                arguments(Arrays.asList(1,4,3,2,2,1), Arrays.asList(1,1,2,2,3,4), 6),
                arguments(Collections.singletonList(1), Collections.singletonList(1), 1),
                arguments(Arrays.asList(4,2), Arrays.asList(2,4), 2),
                arguments(Arrays.asList(1,2,3), Arrays.asList(1,2,3), 3),
                arguments(Arrays.asList(6,5,4,3,2,1), Arrays.asList(1,2,3,4,5,6), 6)
        );
    }

    @ParameterizedTest
    @MethodSource("intListProvider")
    void bubbleSortList(List<Integer> a, List<Integer> b, int len) {
        agh.bubbleSortList(a, len);
        assertIterableEquals(b,a,"排序成功");

    }

    @ParameterizedTest
    @MethodSource("intListProvider")
    void bubbleSortArray(List<Integer> aList, List<Integer> bList, int len) {

        Integer[] a = new Integer[len];
        aList.toArray(a);
        Integer[] b = new Integer[len];
        bList.toArray(b);
        agh.bubbleSort(a, len);

        assertArrayEquals(a, b, "排序成功");
    }

    @ParameterizedTest
    @MethodSource("intListProvider")
    void insertionSort(List<Integer> aList, List<Integer> bList, int len) {
        Integer[] a = new Integer[len];
        aList.toArray(a);
        Integer[] b = new Integer[len];
        bList.toArray(b);
        agh.insertionSortArray(a, len);

        assertArrayEquals(a, b, "排序成功");
    }

    @ParameterizedTest
    @MethodSource("intListProvider")
    void insertionSortList(List<Integer> a, List<Integer> b, int len) {
        agh.insertionSortList(a, len);
        assertIterableEquals(b,a,"排序成功");

    }
}
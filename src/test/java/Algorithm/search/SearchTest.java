package Algorithm.search;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SearchTest {

    static Stream<Arguments> dataProviderCommon() {
        return Stream.of(
                arguments(Arrays.asList(8, 11, 19, 23, 27, 33, 45, 55, 67, 98), 10, 19, 2),
                arguments(Arrays.asList(1, 3, 4, 5, 6, 8, 8, 8, 11, 18), 10, 8, 7)
        );
    }

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments(Arrays.asList(8, 11, 19, 23, 27, 33, 45, 55, 67, 98), 10, 19, 2),
                arguments(Arrays.asList(1, 3, 4, 5, 6, 8, 8, 8, 11, 18), 10, 8, 5)
        );
    }

    @DisplayName("测试二分查找")
    @Description("测试二分查找")
    @ParameterizedTest(name = "二分查找测试, a={0}, len={1}, value={2}")
    @MethodSource("dataProviderCommon")
    void binarySearch(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.binarySearch(a, len, value);
        assertEquals(index, data);
    }

    @DisplayName("测试二分查找递归实现")
    @Description("测试二分查找递归实现")
    @ParameterizedTest(name = "二分查找测试, a={0}, len={1}, value={2}")
    @MethodSource("dataProviderCommon")
    void binarySearchInternally(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.binarySearchInternally(a, len, value);
        assertEquals(index, data);
    }

    @DisplayName("测试二分查找变形问题")
    @Description("测试查找第一个值等于给定值的元素")
    @ParameterizedTest(name = "查找第一个值等于给定值的元素, a={0}, len={1}, value={2}")
    @MethodSource("dataProvider")
    void firstValueEqualByBsearch(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.firstValueEqualByBsearch(a, len, value);
        assertEquals(index, data);
    }

    static Stream<Arguments> dataProviderLastEqual() {
        return Stream.of(
                arguments(Arrays.asList(8, 11, 19, 23, 27, 33, 45, 55, 67, 98), 10, 19, 2),
                arguments(Arrays.asList(1, 3, 4, 5, 6, 8, 8, 8, 11, 18), 10, 8, 7)
        );
    }

    @DisplayName("测试二分查找变形问题")
    @Description("测试查找最后一个值等于给定值的元素")
    @ParameterizedTest(name = "查找最后一个值等于给定值的元素, a={0}, len={1}, value={2}")
    @MethodSource("dataProviderLastEqual")
    void lastValueEqualByBsearch(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.lastValueEqualByBsearch(a, len, value);
        assertEquals(index, data);
    }

    static Stream<Arguments> dataProviderfirstGE() {
        return Stream.of(
                arguments(Arrays.asList(8, 11, 19, 23, 27, 33, 45, 55, 67, 98), 10, 18, 2),
                arguments(Arrays.asList(1, 3, 4, 5, 6, 8, 8, 8, 11, 18), 10, 8, 5),
                arguments(Arrays.asList(3, 4, 6, 7, 10), 5, 5, 2)
        );
    }

    @DisplayName("测试二分查找变形问题")
    @Description("测试查找第一个大于等于给定值的元素")
    @ParameterizedTest(name = "查找第一个大于等于给定值的元素, a={0}, len={1}, value={2}")
    @MethodSource("dataProviderfirstGE")
    void firstValueGEByBsearch(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.firstValueGEByBsearch(a, len, value);
        assertEquals(index, data);

    }


    static Stream<Arguments> dataProviderfirstLE() {
        return Stream.of(
                arguments(Arrays.asList(8, 11, 19, 23, 27, 33, 45, 55, 67, 98), 10, 18, 1),
                arguments(Arrays.asList(1, 3, 4, 5, 6, 8, 8, 8, 11, 18), 10, 8, 7),
                arguments(Arrays.asList(3, 4, 6, 7, 10), 5, 5, 1)
        );
    }

    @DisplayName("测试二分查找变形问题")
    @Description("测试查找最后一个小于等于给定值的元素")
    @ParameterizedTest(name = "查找最后一个小于等于给定值的元素, a={0}, len={1}, value={2}")
    @MethodSource("dataProviderfirstLE")
    void lastValueLEByBsearch(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.lastValueLEByBsearch(a, len, value);
        assertEquals(index, data);

    }

    @DisplayName("测试二分查找变形问题")
    @Description("测试查找最后一个小于等于给定值的元素")
    @ParameterizedTest(name = "查找最后一个小于等于给定值的元素, a={0}, len={1}, value={2}")
    @MethodSource("dataProviderfirstLE")
    void lastValueLEByBsearch2(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.lastValueLEByBsearch2(a, len, value);
        assertEquals(index, data);

    }
}


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

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments(Arrays.asList(8, 11, 19, 23, 27, 33, 45, 55, 67, 98), 6, 19, 2)
        );
    }

    @DisplayName("测试二分查找")
    @Description("测试二分查找")
    @ParameterizedTest(name = "二分查找测试, a={0}, len={1}")
    @MethodSource("dataProvider")
    void binarySearch(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.binarySearch(a, len, value);
        assertEquals(index, data);
    }

    @DisplayName("测试二分查找递归实现")
    @Description("测试二分查找递归实现")
    @ParameterizedTest(name = "二分查找测试, a={0}, len={1}")
    @MethodSource("dataProvider")
    void binarySearchInternally(List<Integer> a, int len, int value, int index) {
        int data = BinarySearch.binarySearchInternally(a, len, value);
        assertEquals(index, data);
    }
}


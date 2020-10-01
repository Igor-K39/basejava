package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        int[] values = {4, 5, 6, 6, 7, 1, 2, 2, 3, 7, 8, 9, 0};
        System.out.println("The minimal value is " + minValue(values));
        System.out.println("The oddOrEven (sum):   " + oddOrEven(Arrays.stream(values).boxed().collect(Collectors.toList())));
        values[0]++;
        System.out.println("The oddOrEven (sum++): " + oddOrEven(Arrays.stream(values).boxed().collect(Collectors.toList())));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .sorted()
                .distinct()
                .reduce((x, y) -> x * 10 + y)
                .orElse(-1);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream()
                .collect(Collectors.partitioningBy(x -> x % 2 == 0));

        return map.get(map.get(false).size() % 2 == 0);
    }
}

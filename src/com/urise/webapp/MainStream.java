package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
        AtomicInteger result = new AtomicInteger();
        Arrays.stream(values)
                .sorted()
                .distinct()
                .boxed()
                .collect(Collectors.toList())
                .forEach(x -> {
                    result.updateAndGet(v -> v * 10);
                    result.addAndGet(x);
                });
        return result.get();
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        AtomicInteger sum = new AtomicInteger();
        List<Integer> odds = new ArrayList<>();
        List<Integer> evens = new ArrayList<>();

        integers.forEach(value -> {
            sum.addAndGet(value);
            if (value % 2 == 0) {
                evens.add(value);
            } else {
                odds.add(value);
            }
        });
        return sum.get() % 2 == 0 ? odds : evens;
    }
}

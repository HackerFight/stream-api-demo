package com.qiuguan.stream.api.demo.create;

import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-07-16 01:38:47 Tuesday
 */
public class ParallelReduceDemo {

    public static void main(String[] args) {

        Integer reduce = List.of(1, 2, 3, 4).parallelStream()
                .reduce(0, (a, b) -> {
                    System.out.println("a : " + a + ", b : " + b + ", Thread" + Thread.currentThread().getName());
                    return a - b;
                });

        System.out.println("reduce = " + reduce);
    }
}

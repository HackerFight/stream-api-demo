package com.qiuguan.stream.api.demo.create;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-07-15 23:22:11 Monday
 */
public class ParallelStreamDemo {

    public static void main(String[] args) {

        Set<String> hashSet = new HashSet<>();
        hashSet.add("e");
        hashSet.add("r");
        hashSet.add("t");
        System.out.println("hashSet = " + hashSet);

        Map<String, String> collect = Set.of("A", "L", "B", "E", "R", "T")
                .parallelStream()
                .map(String::toLowerCase)
                .collect(Collector.of(
                        () -> {
                            System.out.println("Supplier: new ConcurrentHashMap() " + "Thread: " + Thread.currentThread().getName());
                            return new ConcurrentHashMap<>();
                        },
                        (map, item) -> {
                            System.out.println("Accumulator: map: " + map + " item: " +   item + "  Thread: " + Thread.currentThread().getName());
                            map.put(item.toUpperCase(), item);
                        },
                        /*
                           left 和 right 分别代表相邻2个数据段的处理结果，一般是在不同的线程中出现，按出现的顺序排序，left为前，right 为后，即使right的部分先
                           完成，依旧会在right的位置等着left先完成
                         */

                        (left, right) -> {
                            System.out.println("Combiner: LEFT" +  left + " RIGHT:" + right + " Thread: " + Thread.currentThread().getName());
                            left.putAll(right);
                            return left;
                        },
                        Collector.Characteristics.IDENTITY_FINISH,
                        Collector.Characteristics.CONCURRENT

                ));

        System.out.println("collect = " + collect);
    }
}

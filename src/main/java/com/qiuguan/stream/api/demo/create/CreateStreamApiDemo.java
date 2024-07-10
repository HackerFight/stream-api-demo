package com.qiuguan.stream.api.demo.create;


import com.qiuguan.stream.api.demo.bean.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author fu yuan hui
 * @since 2024-07-09 22:42:06 Tuesday
 * <p>
 * Stream本身不是数据结构，不会存储数据或改变数据源，它仅定义处理方式，可以视为一种高级迭代器，不仅支持顺序处理，
 * 还能进行并行处理，为集合的过滤，映射，聚合等操作提供了一种高效简洁的实现方式。
 */
public class CreateStreamApiDemo {

    public static void main(String[] args) {

        testParallelStream();
    }


    public static void testParallelStream() {
        //转成并行流
        Stream<String> parallelStream = Stream.of("beiJin", "a", "b", "c", "d", "e").parallel();
        //parallelStream.forEach(System.out::println);
        //parallelStream.forEachOrdered(System.out::println);


        Stream<String> stringStream = List.of("a", "b", "c").parallelStream();
        stringStream.forEach(System.out::println);

    }

    public static void testNotLimitStream() {
        //这样会生成无数个'China', 所以要集合limit操作
        //Stream<String> generate = Stream.generate(() -> "China");
        Stream<String> generate = Stream.generate(() -> "China").limit(5);
        //generate.forEach(System.out::println);

        //生成5个随机数
        Stream<Double> limit = Stream.generate(Math::random).limit(5);
        //limit.forEach(System.out::println);

        /*
          iterate() 方法主要是用来生成数学序列或实现迭代算法
         */
        // 这样会生成一个包含无限数字的等差数列
        //Stream<Integer> iterate = Stream.iterate(2, n -> n + 2);
        Stream<Integer> iterate = Stream.iterate(2, n -> n + 2).limit(5);
        iterate.forEach(System.out::println);

        //还可以直接指定终止条件，不用在使用limit来限制了
        Stream<Integer> iterate1 = Stream.iterate(2, n -> n <= 20, n -> n + 2);
        iterate1.forEach(System.out::println);
    }


    public static void testBasicDataType() {
        //从指定的基本类型数据中创建流
        IntStream intStream = IntStream.of(1, 2,3);
        //intStream.forEach(System.out::println);

        //从基本类型转成包装类型
        Stream<Integer> boxed = intStream.boxed();

        //从范围指定流 [1, 5), 包含1,不包含5
        IntStream intStream2 = IntStream.range(1, 5);
        //intStream2.forEach(System.out::println);

        //从范围指定流 [1, 5]
        IntStream intStream3 = IntStream.rangeClosed(1, 5);
        //intStream3.forEach(System.out::println);


        //生成随机5个整数的流
        IntStream ints = new Random().ints(5);
        ints.forEach(System.out::println);
    }

    public static void testFileStream() {
        Path path = Paths.get("test.txt");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testStreamBuilder() {
        Stream.Builder<String> builder = Stream.builder();
        builder.add("a");
        builder.add("b");
        if (Math.random() > 0.5) {
            builder.add("c"); //50%的机会会添加c
        }

        //一旦调用build()方法就不能再继续往builder中添加元素，否则会抛出异常
        Stream<String> stream = builder.build();
        stream.forEach(System.out::println);
    }

    public static void testStreamOf() {
        Stream<String> stream1 = Stream.of("a", "b", "c");
        //stream1.forEach(System.out::println);
        Stream<String> stream2 = Stream.of("x", "y", "z");

        //合并流
        Stream<String> concat = Stream.concat(stream2, stream1);
        concat.forEach(System.out::println);
    }

    public static void testArray() {
        String[] array = {"a", "b", "c", "d"};
        Stream<String> stream = Arrays.stream(array);
        stream.forEach(System.out::println);
    }

    public static void testCollection() {
        List<String> lists = List.of("a", "b", "c", "d");
        Stream<String> stream = lists.stream();
        stream.forEach(System.out::println);
    }

    public static void test1() {
        List<Person> peoples = List.of(new Person("张三", 33, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 40, "日本"));

        List<Person> collect = peoples.stream().filter(person -> person.getAge() > 30).toList();
        System.out.println("collect = " + collect);
    }
}

package com.qiuguan.stream.api.demo.create;

import com.qiuguan.stream.api.demo.bean.Person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fu yuan hui
 * @since 2024-07-09 22:42:06 Tuesday
 *
 * Stream本身不是数据结构，不会存储数据或改变数据源，它仅定义处理方式，可以视为一种高级迭代器，不仅支持顺序处理，
 * 还能进行并行处理，为集合的过滤，映射，聚合等操作提供了一种高效简洁的实现方式。
 *
 *
 */
public class CreateStreamApiDemo {

    public static void main(String[] args) {

        Stream<String> stream1 = Stream.of("a", "b", "c");
        //stream1.forEach(System.out::println);
        Stream<String> stream2 = Stream.of("x", "y", "z");

        //合并流
        Stream<String> concat = Stream.concat(stream2, stream1);
        concat.forEach(System.out::println);


//        String[] array = {"a", "b", "c", "d"};
//        Stream<String> stream = Arrays.stream(array);
//        stream.forEach(System.out::println);


//        List<String> lists = List.of("a", "b", "c", "d");
//        Stream<String> stream = lists.stream();
//        stream.forEach(System.out::println);


//        List<Person> peoples = List.of(new Person("张三", 33, "中国"),
//                new Person("AoLi", 35, "澳大利亚"),
//                new Person("Tony", 46, "美国"),
//                new Person("田七", 26, "中国"),
//                new Person("波多野结衣", 40, "日本"));
//
//        List<Person> collect = peoples.stream().filter(person -> person.getAge() > 30).toList();
//        System.out.println("collect = " + collect);



    }
}

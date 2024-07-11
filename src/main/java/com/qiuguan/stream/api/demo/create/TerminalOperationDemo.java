package com.qiuguan.stream.api.demo.create;

import com.qiuguan.stream.api.demo.bean.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author fu yuan hui
 * @since 2024-07-11 22:09:11 Thursday
 */
public class TerminalOperationDemo {

    public static void main(String[] args) {
        testCollect();
    }

    public static void testCollect() {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "美国"),
                new Person("Tony", 46, "男","美国"),
                new Person("田七", 26, "男","中国"),
                new Person("波多野结衣", 30, "女","日本"),
                new Person("波多野结衣", 30, "女","日本2")
        );

        //转成Map, 姓名作为key, person作为value
        //Map<String, Person> personMap = persons.stream().collect(Collectors.toMap(Person::getName, person -> person));
        //personMap.forEach((k, v) -> System.out.println("key = " + k + " value = " + v));

        //如果key重复了会报错，所以用第三个参数指定key规则
        Map<String, String> personCountryMap = persons.stream()
                .collect(Collectors.toMap(
                        Person::getName,
                        Person::getCountry,
                        (k1, k2) -> k2));
        personCountryMap.forEach((k, v) -> System.out.println("key = " + k + " value = " + v));

        System.out.println("-------------------------------------------------");

        //分组
        Map<String, List<Person>> collect = persons.stream().collect(Collectors.groupingBy(Person::getCountry));
        collect.forEach((k, v) -> System.out.println("key = " + k + " value = " + v));

        System.out.println("-------------------------------------------------");

        //分区,年龄大于30的分一个区，小于等于30的分一个区
        Map<Boolean, List<Person>> partitioningBy = persons.stream().collect(Collectors.partitioningBy(person -> person.getAge() > 30));
        partitioningBy.forEach((k, v) -> System.out.println("key = " + k + " value = " + v));

        System.out.println("-------------------------------------------------");

        //字符串的拼接,要比reduce好用
        String collect1 = persons.stream()
                .map(Person::getName)
                //默认是逗号
                .collect(Collectors.joining("#"));
        System.out.println("collect1 = " + collect1);

        System.out.println("-------------------------------------------------");

        //按照年龄进行统计
        IntSummaryStatistics ageSummary = persons.stream().collect(Collectors.summarizingInt(Person::getAge));
        System.out.println("年龄的平均值 = " + ageSummary.getAverage());
        System.out.println("年龄的最大值 = " + ageSummary.getMax());
        System.out.println("年龄的最小值 = " + ageSummary.getMin());

    }

    public static void testReduce() {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "美国"),
                new Person("Tony", 46, "男","美国"),
                new Person("田七", 26, "男","中国"),
                new Person("波多野结衣", 30, "女","日本")
        );


        //用reduce来实现年龄的求和
        int reduce = persons.stream()
                .mapToInt(Person::getAge)
                .reduce(0, (a, b) -> a + b);
        System.out.println("reduce 指定初始值 = " + reduce);

        //不指定初始值，它返回的是OptionalInt
        OptionalInt optionalInt = persons.stream()
                .mapToInt(Person::getAge)
                .reduce((a, b) -> a + b);
        optionalInt.ifPresent(System.out::println);


        //用reduce来实现字符串的拼接，这个聚合操作就无法实现了, 这个在收集的时候用join会更方便
        String reduceName = persons.stream()
                .map(Person::getName)
                .reduce("", (a, b) -> a + b + ",");
        System.out.println("reduce1 = " + reduceName);
    }
    public static void testAggregate() {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "澳大利亚"),
                new Person("Tony", 46, "男","美国"),
                new Person("田七", 26, "男","中国"),
                new Person("波多野结衣", 30, "女","日本")
        );

        //计算流中元素的个数
        long count = persons.stream().count();
        System.out.println("count = " + count);

        //统计流中元素年龄最大的一个
        persons.stream()
                .min(Comparator.comparingInt(Person::getAge))
                //.max(Comparator.comparingInt(Person::getAge))
                .ifPresent(System.out::println);



        //求和，平均值等这些要作用在基本数据类型上
        IntStream intStream = persons.stream().mapToInt(Person::getAge);
        int sum = intStream.sum();
        System.out.println("sum = " + sum);

        //求平均值
        IntStream intStream1 = persons.stream().mapToInt(Person::getAge);
        intStream1.average().ifPresent(System.out::println);

    }

    public static void testMatch() {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));

        //如果任意一个元素满足条件，则返回true
        boolean anyMatch = peoples.stream()
                .anyMatch(person -> person.getAge() > 100);
        System.out.println("anyMatch = " + anyMatch);

        System.out.println("------------------------------------");

        //如果流中所有元素都不符合条件的，就返回true
        boolean noneMatch = peoples.stream()
                .noneMatch(person -> person.getAge() > 30);
        System.out.println("noneMatch = " + noneMatch);

        System.out.println("------------------------------------");


        //如果流中所有元素都符合条件的，就返回true
        boolean allMatch = peoples.stream()
                .allMatch(person -> person.getAge() > 10);
        System.out.println("allMatch = " + allMatch);

        System.out.println("------------------------------------");

        //查找第一个元素
        Optional<Person> first = peoples.stream().findFirst();
        first.ifPresent(System.out::println);

        System.out.println("------------------------------------");

        //查找任意一个元素
        Optional<Person> findAny = peoples.stream().findAny();
        findAny.ifPresent(System.out::println);
    }
}

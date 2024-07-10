package com.qiuguan.stream.api.demo.create;

import com.qiuguan.stream.api.demo.bean.Person;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author fu yuan hui
 * @since 2024-07-10 23:18:34 Wednesday
 * <p>
 * {@link src/resources/images/middle.drawio.png }
 */
public class MiddleOperationDemo {

    public static void main(String[] args) {

        testSorted();
    }

    public static void testSorted() {
        /*
            Moon
            apple
            blueBerry
            cheery
            pear
            默认的排序是基于 String 类的自然顺序，而这种自然顺序是依赖于字典序（lexicographical order），但它是区分大小写的。
            在 Java 中，String 的自然顺序是基于 Unicode 值进行排序的，因此大写字母会排在小写字母之前。具体来说，所有大写字母的 Unicode
            值都小于小写字母的 Unicode 值，这导致在排序时，大写字母会优先出现在排序结果中。
         */
        Stream.of("blueBerry", "cheery", "apple", "pear", "Moon")
                .sorted()
                .forEach(System.out::println);


        System.out.println("----------------------------------------------------------------");
        /*
           按字符长度自然排序(从小到大)
         */
        Stream.of("blueBerry", "cheery", "apple", "pear", "Moon")
                .sorted(Comparator.comparingInt(String::length))
                .forEach(System.out::println);


        System.out.println("----------------------------------------------------------------");
        /*
           按字符长度降序排序,调用 reversed()方法
         */
        Stream.of("blueBerry", "cheery", "apple", "pear", "Moon")
                .sorted(Comparator.comparingInt(String::length).reversed())
                .forEach(System.out::println);


        System.out.println("--------------------------------------------------------");

        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("aoLiNa", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));


        /*
           输出结果：
            Person(name=张三, age=16, country=中国)
            Person(name=田七七, age=26, country=中国)
            Person(name=Tony, age=46, country=美国)
            Person(name=波多野结衣, age=30, country=日本)
            Person(name=AoLiNa, age=35, country=澳大利亚)
         */
        peoples.stream()
                //根据姓名的长度自然排序
                //.sorted(Comparator.comparing(person -> person, (o1, o2) -> o1.getName().length() - o2.getName().length()))
                //.sorted(Comparator.comparing(p -> p, Comparator.comparing(o -> o.getName().length())))
                .sorted(Comparator.comparingInt(p -> p.getName().length()))
                .forEach(System.out::println);

        System.out.println("-------------------------------------------------------");

        /*
          输出结果：英文的大写，英文的小写，中文
            Person(name=Tony, age=46, country=美国)
            Person(name=aoLiNa, age=35, country=澳大利亚)
            Person(name=张三, age=16, country=中国)
            Person(name=波多野结衣, age=30, country=日本)
            Person(name=田七七, age=26, country=中国)
         */
        peoples.stream()
                // 这种写法被优化成下面的写法，按照名字自然排序
                //.sorted(Comparator.comparing(Person::getName, (o1, o2) -> o1.compareTo(o2)))
                .sorted(Comparator.comparing(Person::getName, Comparator.naturalOrder()))
                .forEach(System.out::println);


        System.out.println("--------------------------------------------------------");
        /*
          按照年龄自然排序，等价于：sorted(Comparator.comparingInt(Person::getAge))
          注意看下comparing方法
         */
        peoples.stream()
                .sorted(Comparator.comparing(Person::getAge))
                .forEach(System.out::println);
    }

    public static void testMapTo() {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));

        IntStream intStream = peoples.stream().mapToInt(Person::getAge);
        //intStream.forEach(System.out::println);

        //把所有名字长度超过3个字符的人的年龄打印出来：
        peoples.stream().mapMultiToInt((person, consumer) -> {
            if (person.getName().length() >= 5) {
                consumer.accept(person.getAge());
            }
        }).forEach(System.out::println);
    }

    public static void testFlatMap() {
        List<List<Person>> peopleGroups = List.of(
                List.of(
                        new Person("张三", 16, "中国"),
                        new Person("AoLi", 35, "澳大利亚")
                ),
                List.of(new Person("Tony", 46, "美国"),
                        new Person("田七", 26, "中国")
                ),
                List.of(new Person("波多野结衣", 30, "日本"))
        );


        peopleGroups.stream()
                .flatMap(Collection::stream)
                .map(Person::getName)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");

        peopleGroups.stream()
                .flatMap(people -> people.stream().map(Person::getName))
                .forEach(System.out::println);
    }

    public static void testMap() {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));


        //提取对象中的名字
        peoples.stream()
                .map(Person::getName)
                .forEach(System.out::println);


        peoples.stream()
                //返回的是一个Stream流，所以用Stream.of方法将每一个转换后的元素单独创建一个流，最终flatMap会将这些流合并
                .flatMap(person -> Stream.of(person.getName()))
                .forEach(System.out::println);
    }

    public static void testFilter() {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"),
                new Person("波多野结衣", 30, "日本"));

        /*
          提取年龄大于30的人
         */
        peoples.stream()
                .filter(person -> person.getAge() > 30)
                .forEach(System.out::println);

        System.out.println("--------------------------------------------------------");
         /*
          提取流中的前2个元素，如果指定数量大于流中元素数量，则返回所有元素
         */
        peoples.stream()
                .limit(2)
                .forEach(System.out::println);


        System.out.println("--------------------------------------------------------");
        /*
          跳过前2个元素,如果指定数量大于流中元素数量，则返回空流
         */
        peoples.stream()
                .skip(2)
                .forEach(System.out::println);

        System.out.println("--------------------------------------------------------");

        /*
          distinct() : 去重
         */
        Stream.of("Apple", "Apple", "Orange", "Apple", "Orange")
                .distinct()
                .forEach(System.out::println);


        System.out.println("--------------------------------------------------------");

        /*
        对象如果去重，必须要重写equal和hashcode方法,我用的是lombok的@Data注解，已经帮我实现了
         */
        peoples.stream()
                .distinct()
                .forEach(System.out::println);

    }
}

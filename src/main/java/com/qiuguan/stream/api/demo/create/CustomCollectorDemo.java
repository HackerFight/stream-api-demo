package com.qiuguan.stream.api.demo.create;

import com.qiuguan.stream.api.demo.bean.Person;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author fu yuan hui
 * @since 2024-07-14 23:08:54 Sunday
 */
public class CustomCollectorDemo {

    public static void main(String[] args) {

        testCustomCollect();

    }

    public static void testCollect2() {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "美国"),
                new Person("Tony", 46, "男","美国"),
                new Person("田七", 26, "男","中国"),
                new Person("波多野结衣", 30, "女","日本"),
                new Person("波多野结衣", 30, "女","日本2")
        );


        HashMap<String, List<Person>> collect = persons
                .stream()
                .parallel()
                .collect(Collector.of(
                        HashMap::new,
                        (map, person) -> {
                            String name = Thread.currentThread().getName();
                            System.out.println("累加器[ " + name +"] 执行 = " + map);
                            map.computeIfAbsent(person.getCountry(), k -> new ArrayList<>()).add(person);
                        },
                        //目的是在并行流中合并多各自累加器处理后的独立。我这里将right累加器的数据添加到left中，然后返回leftMap数据个线程
                        //如果是串行流该方法将不会执行
                        (left, right) -> {
                            String name = Thread.currentThread().getName();
                            System.out.println("\n合并器[ " + name +" ]执行>>>>>>>>>>>>: \n left: " + left + System.lineSeparator() + " right: " + right );
                            //注意：我这里的合并是指： 把线程A的Map的key=中国的List元素 和 把线程B的Map的key=中国的List元素进行合并，也就是将key相同的list合并到一个List中
                            //merge方法的第三个参数是指对value进一步进行操作
                            right.forEach((k, v) -> left.merge(k, v, (list1, list2) -> {
                                list1.addAll(list2);
                                return list1;
                            }));

                            return left;
                        },
                        Collector.Characteristics.IDENTITY_FINISH
                        //不将累加器的结果作为最终结果，而是自定义，比如我这里，直接返回分组个数
                        //map -> map.size()
                ));

        System.out.println("collect = " + collect);
    }

    public static void testCustomCollect() {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "美国"),
                new Person("Tony", 46, "男","美国"),
                new Person("田七", 26, "男","中国"),
                new Person("波多野结衣", 30, "女","日本"),
                new Person("波多野结衣", 30, "女","日本2")
        );


        List<Person> collect = persons.stream()
                //.parallel()
                .collect(Collector.of(
                        () -> {
                            String name = Thread.currentThread().getName();
                            System.out.println("供应器执行[ "  + name  +"]>>>>>>>>>>>>>>>\n");
                            return new ArrayList<>();
                        },
                        (list, person) -> {
                            String name = Thread.currentThread().getName();
                            System.out.println("累加器执行[ " +  name +"] >>>>>>>>>>>>>>> " + list);
                            list.add(person);
                        },
                        (left, right) -> {
                            String name = Thread.currentThread().getName();
                            System.out.println("\n组合器执行[" + name  +"] >>>>>>>>>>>>>>>> " + left);
                            left.addAll(right);
                            return left;
                        },
                        Collector.Characteristics.IDENTITY_FINISH
                ));

        System.out.println(">>>>>>>> collect = " + collect);

    }
}

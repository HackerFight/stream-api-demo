package com.qiuguan.stream.api.demo.create;

import com.qiuguan.stream.api.demo.bean.Person;

import java.util.List;

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

        List<Person> peoples = List.of(new Person("张三", 33, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 40, "日本"));
    }
}

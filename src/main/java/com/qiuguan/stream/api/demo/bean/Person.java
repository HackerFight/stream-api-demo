package com.qiuguan.stream.api.demo.bean;

import lombok.Data;

/**
 * @author fu yuan hui
 * @since 2024-07-09 22:43:45 Tuesday
 */
@Data
public class Person {

    private String name;

    private Integer age;

    private String sex;

    private String country;


    public Person(String name, Integer age, String country) {
        this.name = name;
        this.age = age;
        this.country = country;
    }

    public Person(String name, Integer age, String sex, String country) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.country = country;
    }
}

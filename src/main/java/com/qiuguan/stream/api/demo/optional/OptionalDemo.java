package com.qiuguan.stream.api.demo.optional;

import java.util.Optional;

/**
 * @author fu yuan hui
 * @since 2024-07-15 02:44:09 Monday
 *
 *
 * Optional 不推荐使用场景：
 * 1. 不应该作用在类属性上，这回增加内存开销，并且会使得对象的序列化变得复杂
 * 2. 不应该作用在方法参数，构造器参数上。应该通过方法，构造器重载来解决
 * 3. 不应该作用在集合上，因为集合已经有完善的判空方法了。
 * 4. 不建议使用 optional.get()方法，因为当没有数据时会抛出NoSuchElementException,就算当前isPresent()方法
 *    也不建议，因为这种写法浪费了Optional的初衷，也普通判空没有区别，应该使用 isPresent((Consumer<? super T> action)方法
 *    或者 orElse(), 或者 orElseGet()  或 orElseThrow()
 */
public class OptionalDemo {

    public static void main(String[] args) {


        Optional<String> optional = Optional.of("abc");
        System.out.println("optional.isEmpty() = " + optional.isEmpty());
        System.out.println("optional.isPresent() = " + optional.isPresent());
        optional.ifPresent(System.out::println);
    }
}

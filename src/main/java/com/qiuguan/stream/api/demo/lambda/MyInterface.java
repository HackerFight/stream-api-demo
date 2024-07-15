package com.qiuguan.stream.api.demo.lambda;

/**
 * @author fu yuan hui
 * @since 2024-07-15 02:03:57 Monday
 *
 *
 * 函数式接口并不依赖于{@link FunctionalInterface} 这个注解，函数式接口明确就是只有一个抽象方法的就可以是函数式接口
 * 添加了 {@link FunctionalInterface} 注解的目的是为了在编译阶段进行提示，更开发人员更好的明确这个接口的意图
 *
 * 关键词：lambda 就是函数式接口的实例。等同于 person 是 Person Class的实例。
 */
@FunctionalInterface
public interface MyInterface {

    void send();
}

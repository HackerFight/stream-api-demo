# <font color='green'> Stream API  </font>
-- ------------

环境：JDK17
-- ------------

# 1. 认识Stream
Stream本身不是数据结构，不会存储数据或改变数据源，它仅定义处理方式，可以视为一种高级迭代器，不仅支持顺序处理，
还能进行并行处理，为集合的过滤，映射，聚合等操作提供了一种高效简洁的实现方式。 <br>
![stream2.drawio.png](src%2Fmain%2Fresources%2Fimages%2Fstream2.drawio.png)


# 2. 创建Stream
## 2.1 从集合创建流
任何实现了`Collection`接口的集合都可以通过调用`stream()`或者`parallelStream()`方法创建流。
```java
public class CreateStreamApiDemo {

    public static void main(String[] args) {
        List<String> lists = List.of("a", "b", "c", "d");
        Stream<String> stream = lists.stream();
        stream.forEach(System.out::println);
    }
}
```
## 2.2 从数组创建流
```java
import java.util.Arrays;
import java.util.stream.Stream;

public class CreateStreamApiDemo {

    public static void main(String[] args) {
        String[] array = {"a", "b", "c", "d"};
        Stream<String> stream = Arrays.stream(array);
        stream.forEach(System.out::println);
    }
}    
```
## 2.3 调用Stream.of()从一组值中创建流
```java
public class CreateStreamApiDemo {

    public static void main(String[] args) {

        Stream<String> stream1 = Stream.of("a", "b", "c");
        //stream1.forEach(System.out::println);
        Stream<String> stream2 = Stream.of("x", "y", "z");

        //合并流
        Stream<String> concat = Stream.concat(stream2, stream1);
        concat.forEach(System.out::println);
    }
}
```
## 2.4 使用Stream.builder()方法动态添加元素和创建流
```java
public class CreateStreamApiDemo {

    public static void main(String[] args) {

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
}    
```
## 2.5 从文件创建流
```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author fu yuan hui
 * @since 2024-07-09 22:42:06 Tuesday
 *
 */
public class CreateStreamApiDemo {

    public static void main(String[] args) {

        Path path = Paths.get("test.txt");
        //逐行读取
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}    
```
## 2.6 基本类型数据的流

```java
import java.util.concurrent.ThreadLocalRandom;

public class CreateStreamApiDemo {

    public static void main(String[] args) {
        //从指定的基本类型数据中创建流
        IntStream intStream = IntStream.of(1, 2, 3);
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
}    
```

## 2.7 创建无限流：没有固定大小的流
> 可以使用 `generate()`, `iterate()` 创建无限流, 创建无限流时要谨慎，必须无限循环的发生，所以一般结合`limit`操作
```java
public class CreateStreamApiDemo {
    
    public static void main(String[] args) {
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
}
```

## 2.8 创建并行流或者串行流调用`parallel()`方法转成并行流
```java
public class CreateStreamApiDemo {
    
    public static void main(String[] args) {
        //转成并行流
        Stream<String> parallelStream = Stream.of("beiJin", "a", "b", "c", "d", "e").parallel();
        //parallelStream.forEach(System.out::println);
        parallelStream.forEachOrdered(System.out::println);


        Stream<String> stringStream = List.of("a", "b", "c").parallelStream();
        stringStream.forEach(System.out::println);

    }
}
```

# 3. 中间操作
![middle.drawio.png](src%2Fmain%2Fresources%2Fimages%2Fmiddle.drawio.png)

## 3.1 过滤和切片
1. filter() 过滤
2. distinct() 去重，底层为了一个HashSet, 注意对象去重要重写equals和hashcode方法
3. limit(n) 提取流中的前n个元素，如果指定数量大于流中元素数量，则返回所有元素
4. skip(n) 跳过前n个元素，如果指定数量大于流中元素数量，则返回空流

```java
public class MiddleOperationDemo {
    
    public static void main(String[] args) {
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
```

## 3.2 映射
* map() 方法:  适用于单层结构的流，进行元素一对一的转换，例如更改数据类型或者提取信息，对于嵌套的集合，数组或其他多层结构的数据处理不够灵活，这种情况下flatMap成为更合适的选择。
* flatMap()方法: 不仅能够执行map的转换功能，还能够扁平化多层数据结构，将他们转换合并成一个单层流.

### 3.2.1 测试map
```java
public class MiddleOperationDemo {

    public static void main(String[] args) {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));


        //提取对象中的名字
        peoples.stream()
                .map(Person::getName)
                .forEach(System.out::println);
    }
}
```

### 3.2.2 测试 flatMap
```java
public class MiddleOperationDemo {

    public static void main(String[] args) {

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
                //扁平化成单一的流，就是它会自动的进行合并
                .flatMap(Collection::stream)
                //再用map提取数据
                .map(Person::getName)
                .forEach(System.out::println);

    }
}
```
图示一下:

![img.png](src%2Fmain%2Fresources%2Fimages%2Fimg.png)
> 从第二个Stream到第三个Stream,就是flatMap的自动合并，将多层流合并成单层流

还可以这样:

```java
public class MiddleOperationDemo {

    public static void main(String[] args) {

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
                .flatMap(people -> people.stream().map(Person::getName))
                .forEach(System.out::println);
    }
}
```

图示一下：

![img_1.png](src%2Fmain%2Fresources%2Fimages%2Fimg_1.png)
> 从第二个Stream到第三个Stream,就是flatMap的自动合并，将多层流合并成单层流

<br>

前面我们说了, `flatMap`也可以做`map`的功能，接下来我们看下用`flatMap`代替`map`实现提取对象的名字

```java
public class MiddleOperationDemo {

    public static void main(String[] args) {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));


        //提取对象中的名字
        peoples.stream()
                //返回的是一个Stream流，所以用Stream.of方法将每一个转换后的元素单独创建一个流，最终flatMap会将这些流合并
                .flatMap(person -> Stream.of(person.getName()))
                .forEach(System.out::println);
    }
}
```
> 虽然能实现功能，但是一般不推荐这么做，既复杂又影响性能

### 3.2.3 测试 mapToInt

```java
public class MiddleOperationDemo {

    public static void main(String[] args) {
        List<Person> peoples = List.of(new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本"));

        //提取对象中的年龄
        IntStream intStream = peoples.stream().mapToInt(Person::getAge);
        //intStream.forEach(System.out::println);

        //把所有名字长度超过3个字符的人的年龄打印出来：
        peoples.stream().mapMultiToInt((person, consumer) -> {
            if (person.getName().length() >= 5) {
                consumer.accept(person.getAge());
            }
        }).forEach(System.out::println);
    }
}
```
![img.png](img.png)
> 可以看到 `mapMultiToInt`方法有2个参数，一个是流中的元素，另一个是IntConsumer


## 3.3 排序

字符串排序：
```java
public class MiddleOperationDemo {
    
    public static void main(String[] args) {
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
    }
}
```

对象排序：

```java
public class MiddleOperationDemo {

    public static void main(String[] args) {
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
}
```

问题? <br>
对比 `Comparator.comparing(Person::getAge)` 方法 `stream().mapMultiToInt`方法，为什么参数传递会不一样呢？
![img_1.png](img_1.png) <br>
![img_2.png](img_2.png)

为什么写法上不同呢？
```java
   peoples.stream()
          .sorted(Comparator.comparing(Person::getAge))
          .forEach(System.out::println);


    peoples.stream().mapMultiToInt((person, consumer) -> {
       if (person.getName().length() >= 5) {
          consumer.accept(person.getAge());
       }
    }).forEach(System.out::println);
```
> 为什么一个传1个参数，一个传2个参数呢？

<b>原因是因为 `mapMultiToInt`的参数是 `BiConsumer<T, U>`, 而T,U都是参数，所以参数是2个; 而 `comparing`方法的参数是 `Function<T, R>`, 其中T是参数，R是返回值，所以它的参数是一个。</b>

# 4. 终端操作
![img_2.png](src%2Fmain%2Fresources%2Fimages%2Fimg_2.png)

## 4.1 查找与匹配
![img_4.png](img_4.png)
这类属于`短路操作`，这个因为这些操作再找到符合条件的元素后会立即结束处理，返回结果，而不需要处理整个流
有效短路了流的遍历，提高了处理效率，特别适用再快速筛选和数据验证的场景中。<br>

* anyMatch(): 如果流中有任意一个元素符合条件，则返回true，否则返回false。
* noneMatch(): 如果流中所有元素都不符合条件，则返回true，否则返回false。
* allMatch(): 如果流中所有元素都符合条件，则返回true，否则返回false。
* findFirst(): 返回流中第一个元素
* findAny(): 符合流中任意一个元素

```java
public class TerminalOperationDemo {
    
    public static void main(String[] args) {
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
```

## 4.2 聚合操作
![img_5.png](img_5.png)
```java
public class TerminalOperationDemo {
    
    public static void main(String[] args) {
        List<Person> persons = List.of(
                new Person("张三", 16, "中国"),
                new Person("AoLi", 35, "澳大利亚"),
                new Person("Tony", 46, "美国"),
                new Person("田七", 26, "中国"),
                new Person("波多野结衣", 30, "日本")
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

        //请平均值
        IntStream intStream1 = persons.stream().mapToInt(Person::getAge);
        intStream1.average().ifPresent(System.out::println);
    }
}
```

## 4.3 reduce 规约
> 本质上`聚合操作`是`reduce规约操作`的一种特殊形式，聚合操作适用于快捷简单的统计任务，比如求和，平均值，最大最小值等等，而规约操作reduce则更为通用，
它可以通过自定义的累加器函数对流中的所有元素进行迭代处理，以累计中最终的结果。可以实现任何类型的结果汇总，不仅限于数学上的聚合，而是任何形式的数据
合并，比如拼接字符串，合并集合等等。

```java
public class TerminalOperationDemo {
    
    public static void main(String[] args) {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "澳大利亚"),
                new Person("Tony", 46, "男", "美国"),
                new Person("田七", 26, "男", "中国"),
                new Person("波多野结衣", 30, "女", "日本")
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
                //如果不指定初始值，它返回的是Optional
                .reduce("", (a, b) -> a + b + ",");
        System.out.println("reduceName = " + reduceName);
    }
}
```

我们看下用`reduce`求和方法的参数:
![img_7.png](img_7.png)
![img_8.png](img_8.png)
正好对应着:
```java
// a + b 是返回值
.reduce(0, (a, b) -> a + b);
```
<br>
<br>

再来看下用`reduce`拼接字符串的方法:
![img_9.png](img_9.png)
```java
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T,T,T> {
   //........
}

@FunctionalInterface
public interface BiFunction<T, U, R> {
    
    //2个参数，1个返回值
    R apply(T t, U u);
}
```

## 4.4 collect 收集
> 调用collect可以将流中的元素收集起来放到新的List, Set, Map中等，还提供了分组，分区，字符串拼接以及各种统计功能。

```java
public class TerminalOperationDemo {

    public static void main(String[] args) {
        testCollect();
    }

    public static void testCollect() {
        List<Person> persons = List.of(
                new Person("张三", 16, "男", "中国"),
                new Person("AoLi", 35, "女", "美国"),
                new Person("Tony", 46, "男", "美国"),
                new Person("田七", 26, "男", "中国"),
                new Person("波多野结衣", 30, "女", "日本"),
                new Person("波多野结衣", 30, "女", "日本2")
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
}
```


## 4.5 自定义collector来模拟收集功能
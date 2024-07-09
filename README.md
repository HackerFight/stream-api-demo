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
## 2.3 从文件创建流
## 2.4 从IO创建流
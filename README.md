# <font color='green'> Stream API  </font>
-- ------------

环境：最低JDK8
-- ------------

# 1. 认识Stream
Stream本身不是数据结构，不会存储数据或改变数据源，它仅定义处理方式，可以视为一种高级迭代器，不仅支持顺序处理，
还能进行并行处理，为集合的过滤，映射，聚合等操作提供了一种高效简洁的实现方式。 <br>
![stream2.drawio.png](src%2Fmain%2Fresources%2Fimages%2Fstream2.drawio.png)


# 2. 创建Stream
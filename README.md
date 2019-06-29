# yajad-jmeter-dubbo-plugin

YAJAD（亚加德） - Yet Another Jmeter Apache Dubbo plugin

名称上来说，就是：又一个jmeter的dubbo插件

## 为什么是一个新的插件？

dubbo官方已经有一个jmeter插件（[https://github.com/dubbo/jmeter-plugins-for-apache-dubbo](https://github.com/dubbo/jmeter-plugins-for-apache-dubbo)），但在日常的使用中，发现存在诸多的不便，于是萌生了改进的念头。是基于官方的开发，然后推pull request，还是新写一个呢？

考虑了一下，还是新写一个，理由如下：

1. 官方代码版本是基于Java 7，个人已经习惯了Java 8，且Java 8也是一个比较“古老”，比较稳定，使用比较广泛的版本，不想也没必要再去兼容Java 7
2. 官方插件部分代码细节不够好，比如有的变量单词拼写错误、泛型变量没有充分应用菱形语法来简化声明等 ，导致IDE一直提醒，这对有强迫症的开发人员来说，很难以忍受
3. 官方插件提供的接口传参机制不够友好，也不够强大，细节请参考[功能特性](#功能特性)说明
4. 选项不实用
	* 异步/同步的设置，不论是测试接口的性能还是功能的正确性，异步都没什么用，这种设置就是多余的
	* 列出接口列表的功能很鸡肋，真正测试的时候，接口通常会很多，会导致列表很长，很难选，虽然从插件代码上看，有自动补全机制，但实际使用似乎不生效。且真正测试的时候，因为还需要知道接口的参数，所以通常会看文档或源码，这就使得这个功能更鸡肋
5. 不想背负“包袱”，官方的插件讲究的是通用，而自己要做的更多的是满足自身的需求，不需要去考虑其他自己用不到的场景

## 功能特性

### 版本支持

Java 8、jmeter 5.0+

### 不需要加载接口参数所需的jar包

得益于默认的`hessian`序列化机制良好的兼容性，参数是自定义类时，本插件不需要导入对应的jar包。但同时也因此不保证其他序列化机制不需要导入jar包，如果使用其他序列化机制，出现无法调用接口，不兼容的情况是有可能的。因为在日常的场景中，用的都是`hessian`序列化，不考虑其他序列化机制，这也是前面所说的，不背“包袱”。

### 传参使用yaml格式

#### yaml的可读性好

这点很重要，这会强制大家测试的时候，书写的参数格式都是统一美观的。

#### yaml扩展性更好

json的扩展性有限，比如如果要传递一个`Long`类型，或者新的Java 8日期对象`LocalDate`，那么官方插件使用的json格式是没办法的。官方的插件如果要扩展参数类型，就需要转变json格式，比如：

```json
{"param1": {"type": "Long", "value": 123}}
```

这样显然写起来会很烦琐。

yaml在解析上可以做一些扩展，可以支持更多的类型：

* `123L`，就表示转成`Long`类型
* `!!java.time.LocalDate 2017-05-01`表示将日期`2017-05-01`转化成`LocalDate`类型

#### yaml兼容json格式

yaml是json格式的超集，是兼容json格式的，参数同样可以用json格式书写（有细节要求，比如键值对的冒号后面要加一个空格）。但不建议用json来书写参数，原因如前面所述，json的可读性和扩展性均不如yaml。

### 输入输出更加美观

* 官方插件在输出结果信息时，没有明确设置`UTF-8`字符编码（需自行修改jmeter的配置），在某些情况下中文会有乱码。本插件对结果设置了输出编码，不需要额外的配置也能正常显示中文
* 官方插件接口传参使用的是json格式，而填写json的输入框很窄，使得json参数看起来很不明朗。本插件使用更大的输入框，且输入的参数格式为yaml，参数信息的可读性好

* 官方插件在输出接口调用结果时，没有对结果的json进行格式化，会造成查看上的不便。本插件对接口结果进行格式化输出，可以更容易查看结果

### sampler时间优化

不计算起始的连接时间（如连接zookeeper），以接口真正调用的时间开始计算，有利于测试接口的响应速度

## 共同的缺点

* 由于两个插件使用的都是将其他格式转化成Java参数格式，所以，均会存在一些无法转换的情况

## 使用

下载xxx及相应依赖的jar包

### 依赖的jar包

* curator-client-2.12.0.jar
* curator-framework-2.12.0.jar
* gson-2.8.2.jar
* guava-16.0.1.jar
* javassist-3.15.0-GA.jar
* jline-0.9.94.jar
* jmeter-apache-dubbo-plugin-1.0-SNAPSHOT.jar
* log4j-over-slf4j-1.7.5.jar
* netty-3.7.0.Final.jar
* slf4j-api-1.7.5.jar
* snakeyaml-engine-1.0.jar
* zkclient-0.1.jar
* zookeeper-3.4.6.jar

### yaml示例

```java
interface OrderService {
    boolean submit(Integer userId, String orderNo);
}
```

```yaml
- 123
- ABCDEFG
```

参数列表如果都是自定义类型，则可以用省略列表格式（不用在前面加`-`）

```java
interface OrderService {
    OrderParamDto query(OrderParamDto orderParamDto, Page page);
}
```

```yaml
com.kelystor.dto.OrderParamDto:
  id: "123"
  price: 100
com.kelystor.dto.Page:
  page: 1
  rows: 2
```

参数列表如果包含自定义类型，又包含基本类型，则需以`-`开头（表示列表），参数是自定义类型时，则字段缩进要多一层（4个空格）

```java
interface OrderService {
    OrderParamDto query(OrderParamDto orderParamDto, Page page, Integer userId);
}
```

```yaml
- com.kelystor.dto.OrderParamDto:
    id: "123"
    price: 100
    status: CANCEL
- com.kelystor.dto.Page:
      page: 1
      rows: 2
      sort:
        - 
          field: name3
          order: desc
        - 
          field: name5
          order: asc
- ${userId}
```

更多yaml的相关语法，请参考：

* [https://www.jianshu.com/p/97222440cd08](https://www.jianshu.com/p/97222440cd08)
* [https://yaml.org/spec/1.2/spec.html](https://yaml.org/spec/1.2/spec.html)

长整型在数字后面加`L`或`l`，如：`123L`
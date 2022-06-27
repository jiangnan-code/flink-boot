[TOC]

**Flink-Boot 脚手架封装了Flink API,屏蔽掉组装Flink API细节，使得普通开发者可以以单机模式的开发方式开发出具备分布式运行的程序。普通开发者完全不需要理解分布式计算的理论知识和Flink框架的细节，便可以快速编写业务代码实现。**

为了进一步提升开发者使用该脚手架开发大型项目的敏捷的度，该脚手架工程默认集成Spring框架进行Bean管理，同时将微服务以及WEB开发领域中经常用到的框架集成进来，进一步提升开发速度。比如集成Mybatis ORM框架，Hibernate Validator校验框架等，具体见下面的脚手架特性。

使用该脚手架工程可以满足一个项目组仅需一名开发人员具备**Flink的基本知识**，其他人员甚至不需要懂Flink，整个项目组都可以使用Flink框架解决业务中的痛点问题。

# 一：工程结构

* flink-base :基础工程，封装了开发Flink工程的必须参数，同时集成Spring容器，为后续集成Spring各类框架提供了支撑。
   1. 可以在本地开发环境和Flink集群运行环境中随意切换。
   2. 可以在增量检查点和全量检查点之间随意切换。
   3. 内置使用HDFS作为检查点的持久存储介质。
   4. 默认使用Kafka作为数据源。
 * flink-client：业务工程，该工程依赖flink-base工程，开发任务在该工程中进行业务逻辑的开发。
 * flink-annotation:注解配置Bean,flink-client依赖进该工程后，便可以在类名和类的字段名上添加注解进行Bean的配置和对象间依赖注入。
 * flink-retry:重启机制,flink-client依赖进该工程后，便可以在重试的方法上添加@Retryable注解，设置方法执行失败后的重试机制。
 * flink-cache-annotation:接口缓冲,flink-client依赖进该工程后，便可以在方法上添加@Cacheable注解，设置方法开启缓冲机制。
 * flink-mybatis:ORM，flink-client依赖进该工程后，便可以使用Mybatis的数据库映射特性快速进行数据库表的交互，增删改查等。
 * flink-dubbo-comsumer:校验，flink-client依赖进该工程后，便可以调用注册在dubbo中的服务进行消费。
 * flink-validate:校验，flink-client依赖进该工程后，便可以在要校验的POJO的字段上添加@Size，@NotBlank等校验注解。
 * flink-sql：Flink SQL语句XML话，将Flink SQL语句剥离出JAVA代码中，编写进XML中，自动维护Flink SQL API读取XML中的SQL语句并翻译为Flink的DataStream API代码运行。



# 二：快速开始

## 1.启动类

编写Flink作业，自定义启动入口类需要继承com.intsmaze.flink.base.env.BaseFlink抽象类，实现如下抽象方法：

*  public String getTopoName()：定义本作业的名称。
*  public String getConfigName()：定义本作业需要读取的spring配置文件的名称
*  public String getPropertiesName()：定义本作业需要读取的properties配置文件的名称。该文件中默认要配置Kafka服务的IP，读取的Kafka主题名。
*  public void createTopology(StreamExecutionEnvironment builder)：构造本作业的拓扑结构。

### 示例

如下是SimpleClient（com.intsmaze.flink.client.SimpleClient）类的示例代码，该类继承了BaseFlink，可以看到对应实现的方法中分别设置如下：

* SimpleClient流处理程序的作业名为intsmaze-calc
* 读取的Spring配置文件名为topology-base.xml
* 读取的properties配置文件名为config.properties
* createTopology(StreamExecutionEnvironment builder)方法中第一行getKafkaSpout(String topic)封装了读取Kafka数据源的实现，开发者基于该方法返回的数据流进行逻辑业务的计算。

```java
public class SimpleClient extends BaseFlink {

   .......

    @Override
    public String getTopoName() {
        return "intsmaze-calc";
    }

    @Override
    public String getConfigName() {
        return "topology-base.xml";
    }

    @Override
    public String getPropertiesName() {
        return "config.properties";
    }

    @Override
    public void createTopology(StreamExecutionEnvironment builder) {

        DataStream<String> inputDataStrem = getKafkaSpout(properties.getProperty("read-topic").trim());

        DataStream<String> processDataStream = inputDataStrem.flatMap(new SimpleFunction());

        // 指定分区策略
        FlinkKafkaProducer<String> triggerProducer = new FlinkKafkaProducer<String>(
                properties.getProperty("kafkaIp").trim(),
                properties.getProperty("send-topic").trim(),
                new SimpleStringSchema());
        triggerProducer.setWriteTimestampToKafka(true);
        processDataStream.addSink(triggerProducer);
    }
}
```



## 2.数据源

* 对于Kafka作为数据源，可以直接在自定义Topology类的createTopology方法中调用如下方法，参数为要获取数据的Kafka的主题名。

  ```java
  DataStream<String> inputDataStrem = getKafkaSpout(properties.getProperty("read-topic").trim());
  ```

  然后在resources文件夹下的config.properties文件中配置如下参数。

  ```
  # 读取Kafka服务中主题的名词
  read-topic=intsmaze-calc
  # Kafka集群的ip信息
  kafkaIp=intsmaze-201:9092
  ```



* 对于自定义数据源，用户需要编写自定义DataSource类，该类需要继承com.intsmaze.flink.base.transform.CommonDataSource抽象类，实现如下方法。

  ```java
  //获取本作业在Spring配置文件中配置的bean对象
  public abstract void open(StormBeanFactory beanFactory);
  //本作业spout生成数据的方法，在该方法内编写业务逻辑产生源数据，产生的数据以String类型进行返回。
  public abstract String sendMessage();
  ```



### 示例

```java
public class SimpleDataSource extends CommonDataSource {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	......

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        ...//构造读取各类外部系统数据的连接实例
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》 随书代码
     *
     * @auther: intsmaze(刘洋)
     * @date: 2020/10/15 18:33
     */
    @Override
    public String sendMess() throws InterruptedException {
        Thread.sleep(1000);
		......
        MainData mainData = new MainData();
        ......//通过外部系统数据的连接实例读取外部系统数据，封装进MainData对象中，然后返回即可。
        return gson.toJson(mainData);
    }
}
```



## 3. Spring容器

该容器模式配置了JdbcTemplate实例，数据库连接池采用Druid，在业务方法中只需要获取容器中的JdbcTemplate实例便可以快速与关系型数据库进行交互。

```xml
<beans ......
       default-lazy-init="true" default-init-method="init">

    <context:property-placeholder location="classpath:config.properties"/>

    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url"
                  value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <constructor-arg ref="druidDataSource"></constructor-arg>
    </bean>
    
    <bean id="dataService" class="com.intsmaze.flink.base.service.DataService">
        <property name="jdbcTemplate" ref="jdbcTemplate"></property>
    </bean>

</beans>
```



## 4. 业务逻辑

本作业计算的业务逻辑在Flink转换操作符中进行实现，一般来说开发者只需要实现flatMap算子即可以满足大部分算子的使用。

用户编写的自定义Bolt类需要继承com.intsmaze.flink.base.transform.CommonFunction抽象类，均需实现如下方法。

```java
//本作业业务逻辑计算的方法，参数message为Kafka主题中读取过来的参数，默认参数为String类型，如果需要将处理的数据发送给Kakfa主题中，则要通过return将处理的数据返回即可。
public abstract String execute(String message);
```

可以根据情况选择重写open(Configuration parameters)方法，同时重写的open(Configuration parameters)方法的第一行要调用父类的open(Configuration parameters)方法。

```
public void open(Configuration parameters){
	super.open(parameters);
	......
	//获取在Spring配置文件中配置的实例
	XXX xxx=beanFactory.getBean(XXX.class);
}
```



### 示例

```java
public class SimpleFunction extends CommonFunction {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    public String execute(String message) {

        LinkedList<SourceData> dataLinkedList = gson.fromJson(message, new TypeToken<LinkedList<SourceData>>() {
        }.getType());
        LinkedList<FlowData> flowDataLinkedList = gson.fromJson(message, new TypeToken<LinkedList<FlowData>>() {
        }.getType());

        for (int i = 0; i < dataLinkedList.size(); i++) {
            FlowData flowData = flowDataLinkedList.get(i);
            String flowUUID = dataService.findUUID(flowData);
            if (StringUtils.isBlank(flowUUID)) {
                flowUUID = UUID.randomUUID().toString();
                flowData.setUuid(flowUUID);
                dataService.insertFlow(flowData);
            }
        }
        return flowDataLinkedList.toString();
    }
}
```



## 5. (Standalone/Yarn)集群/本地运行

在自定义的Topology类编写Main方法，创建自定义的Topology对象后，调用对象的run(...)方法。

```java
public class SimpleClient extends BaseFlink {

    /**
     * 本地启动参数  -isLocal local
     * 集群启动参数  -isIncremental isIncremental
     */
    public static void main(String[] args) throws Exception {
        SimpleClient topo = new SimpleClient();
        topo.run(ParameterTool.fromArgs(args));
    }
    
    .......
}        
```

**默认不传参数为集群模式启动，且不开启检查点机制。**

* 传参有如下三种情况：
  * 本地模式：-isLocal local
  * 集群模式，且增量检查点：-isIncremental isIncremental
  * 集群模式，且全量检查点：-isIncremental full
* 检查点默认使用hdfs集群，且默认hdfs服务和Flink服务位于同一集群中。



* 以Standalone模式的发布命令：

```
./bin/flink run -c com.intsmaze.flink.client.SimpleClient \
..../flink-client-12.0-SNAPSHOT.jar \
-isLocal local
```

* 以Yarn模式的发布命令：

```
 ./bin/flink run -m yarn-cluster -ynm intsmaze-flink-boot -ys 1 -c com.intsmaze.flink.client.SimpleClient /root/flink-client-1.12.jar -isLocal local
```

集成其他框架的客户端类的启动方式可参考上面示例。

# 三：集成flink-validate

依赖topology-base.xml配置文件，无序添加额外配置。

核心校验逻辑方法为：

```
        Map<String, StringBuffer> validate = ValidatorUtil.validate(flowData);
        if (validate != null) {
            System.out.println(validate);
            return null;
        }
```

然后就可以运行com.intsmaze.flink.client.ValidateClient类，启动一个集成注解校验功能的Flink流处理程序。



# 四：集成flink-retry

将如下代码添加进 flink-client工程的topology-retry.xml文件中。

```
    <import resource="topology-base.xml"></import>

    <import resource="base-annotation.xml"></import>
```

将如下代码添加进 flink-annotation工程的base-annotation.xml文件中。

```
<context:component-scan base-package="com.intsmaze">
    <context:include-filter type="annotation"
                            expression="org.aspectj.lang.annotation.Aspect"/>
</context:component-scan>
```

将如下依赖添加进 flink-annotation工程的pom.xml文件中。

```
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.1</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.1</version>
</dependency>
```

然后就可以运行com.intsmaze.flink.client.RetryClient类，启动一个开启注解重试机制的Flink流处理程序。



# 五：集成flink-mybatis

将如下代码添加进 flink-client工程的topology-mybatis.xml文件中。

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd"
       default-lazy-init="true" default-init-method="init">

    <context:component-scan base-package="com.intsmaze"/>

    <import resource="classpath:topology-base.xml" />

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="druidDataSource" />
        <property name="mapperLocations" value="classpath*:com/intsmaze/mapper/*.xml" />
        <property name="transactionFactory">
            <bean class="org.apache.ibatis.transaction.managed.ManagedTransactionFactory" />
        </property>
    </bean>

    <mybatis:scan base-package="com.intsmaze.flink.mybatis.mapper" />

</beans>
```

然后就可以运行com.intsmaze.flink.client.MybatisClient类，启动一个集成Mybatis的Flink流处理程序。



# 六：集成Spring注解

将如下代码添加进 flink-annotation工程的base-annotation.xml文件中。

```
<context:component-scan base-package="com.intsmaze">
    <context:include-filter type="annotation"
                            expression="org.aspectj.lang.annotation.Aspect"/>
</context:component-scan>
```

将如下代码添加进 flink-client工程的topology-annotation.xml文件中。

```
    <import resource="topology-base.xml"></import>

    <import resource="base-annotation.xml"></import>
```

然后就可以运行com.intsmaze.flink.client.AnnotationClient类，启动一个通过注解配置Bean的Flink流处理程序。

# 七：集成Spring 接口缓冲

将如下代码添加进 flink-client工程的topology-cache.xml文件中。

```
    <import resource="topology-base.xml"></import>

    <import resource="base-annotation.xml"></import>

    <cache:annotation-driven/>

    <bean id="cacheManager"
          class="org.springframework.cache.support.SimpleCacheManager">
        <property name="caches">
            <set>
                <bean
                        class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"
                        p:name="default"/>
                <bean
                        class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"
                        p:name="FlowData.findUUID"/>
            </set>
        </property>
    </bean>
```

将如下代码添加进 flink-annotation工程的base-annotation.xml文件中。

    <context:component-scan base-package="com.intsmaze">
        <context:include-filter type="annotation"
                                expression="org.aspectj.lang.annotation.Aspect"/>
    </context:component-scan>
然后就可以运行com.intsmaze.flink.client.CacheinterfaceClient类，启动一个集成接口缓冲的Flink流处理程序。

# 八：集成Dubbo消费组客户端

将如下代码添加进 flink-client工程的topology-dubbo-consumer.xml文件中。

```
    <import resource="topology-base.xml"></import>

    <import resource="spring-dubbo-consumer.xml"></import>
```

将如下代码添加进 flink-dubbo-consumer工程的spring-dubbo-consumer.xml文件中。

```
    <dubbo:application name="consumer-of-helloworld-app"  />

    <dubbo:registry protocol="zookeeper" address="127.0.0.1:2181 "/>

    <dubbo:reference id="dubboService" interface="com.intsmaze.dubbo.provider.DubboService" />
```



运行步骤：

1. 先运行 flink-other-service工程下 dubbo.provider子model的com.intsmaze.dubbo.provider.ProviderClient类启动服务注册者提供服务。
2. 然后运行flink-client工程DubboConsumerClient类即可使用服务消费组进行消费调用。

# 九：集成redis客户端

将如下代码添加进 flink-client工程的topology-redis.xml文件中。

```
<import resource="topology-base.xml"></import>

<import resource="spring-redis.xml"></import>
```

将如下代码添加进 flink-redis工程的spring-redis.xml文件中。

```
  <bean id="jedisPool" class="redis.clients.jedis.JedisPool" lazy-init="true">
        <constructor-arg ref="jedisPoolConfig"></constructor-arg>
        <constructor-arg value="127.0.0.1"></constructor-arg>
        <constructor-arg value="6379"></constructor-arg>
        <constructor-arg value="30"></constructor-arg>
  </bean>
 
  <bean id="JedisCluster" class=" redis.clients.jedis.JedisCluster" lazy-init="true">
        <constructor-arg>
            <set>
                <ref bean="hostAndPort-one"></ref>
                <ref bean="hostAndPort-two"></ref>
            </set>
        </constructor-arg>
        <constructor-arg value="6000"></constructor-arg>
        <constructor-arg value="1000"></constructor-arg>
        <constructor-arg ref="jedisPoolConfig"></constructor-arg>
    </bean>

    <bean id="jedisSentinelPool" class="redis.clients.jedis.JedisSentinelPool" lazy-init="true">
        <!--master-name的名称一定要和集群的名称一样，不然无法连接上-->
        <constructor-arg value="master-name"></constructor-arg>
        <constructor-arg>
            <set>
                <value>192.168.19.131:26380</value>
                <value>192.168.19.131:26381</value>
            </set>
        </constructor-arg>
        <constructor-arg ref="jedisPoolConfig"></constructor-arg>
    </bean>
    
<bean id="shardedJedisPool" class="redis.clients.jedis.ShardedJedisPool" lazy-init="true">
        <constructor-arg ref="jedisPoolConfig"></constructor-arg>
        <constructor-arg>
            <list>
                <ref bean="jedisShardInfo-one"></ref>
                <ref bean="jedisShardInfo-two"></ref>
            </list>
        </constructor-arg>
    </bean>

```



# 十：动态加载实时编译类（规则引擎核心）



1.  com.intsmaze.flink.dynamic.DynamicService（新增的java类，要实现该接口）
2.  com.intsmaze.test.CompileJavaFileToRedisTest（测试类，将用户定义的java类编译，并存储在redis中）
3. com.intsmaze.test.LoadClassFromRedis（从redis中获取编译的java文件，通过反射创建对应的对象）



将如下代码添加进flink-dynamic-load-class工程的spring-classload.xml文件中。

```
	<import resource="spring-redis.xml"></import>

	<bean id="compileJavaFileRedisTemplate" class="com.intsmaze.flink.dynamic.base.utils.CompileJavaFileRedisTemplate">
		<property name="jedisPool" ref="jedisPool" />
		<property name="fileSystemClassLoader" ref="fileSystemClassLoader" />
	</bean>


	<bean id="fileSystemClassLoader" class="com.intsmaze.flink.dynamic.base.FileSystemClassLoader">
		<property name="jedisPool" ref="jedisPool" />
	</bean>
```

将如下代码添加进 flink-client工程的topology-load-class-redis.xml文件中。

```
    <import resource="topology-base.xml"></import>

    <import resource="spring-classload.xml"></import>
```


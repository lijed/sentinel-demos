# 工程简介

本工程集成sentinel core 来实现限流和fallback。

**依赖的关键jar的版本**

- spring boot的版本是2.3.12.RELEASE， 
- Sentinel的版本是1.8.1

# 依赖的sentinel的 依赖

使用sentinel core 的依赖

```xml
      <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-core</artifactId>
            <version>1.8.1</version>
        </dependency>

```

# 使用com.alibaba.csp.sentinel.SphO

```java
public class SentinelSph0Demo {

    static {
        intFlowRule();
    }

    public static void main(String[] args) throws InterruptedException {
        while(true) {
            TimeUnit.MILLISECONDS.sleep(50);
            doSomething();
        }
    }

    public static void doSomething() {
        if (SphO.entry("doSomething")) {
            try {
                //业务处理逻辑
                System.out.println("Hello World" + System.currentTimeMillis());
            } finally {
                //资源使用完一定要exit
                SphO.exit();
            }
        } else {
            // 资源访问被限制
            System.out.println("被限流了");
        }
    }

    private static void intFlowRule() {
        List<FlowRule> flowRules = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("doSomething");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(5);

        flowRules.add(flowRule);

        FlowRuleManager.loadRules(flowRules);
    }
}

```

# 使用com.alibaba.csp.sentinel.SphU

```java
public class SentinelSphUDemo {
    public static void main(String[] args) throws InterruptedException {
        intFlowRule();

        while (true) {
            doSomething();
        }
    }

    /**
     *  每秒的QPS是5，如果超过5，将抛出 FlowException
     */
    private static void doSomething() {
        try (Entry doSomething = SphU.entry("doSomething")) {
            TimeUnit.MILLISECONDS.sleep(150);
            System.out.println("do some process");
        } catch (BlockException | InterruptedException ex) {
            ex.printStackTrace();
            System.out.println("被限流了");
        }
    }

    private static void intFlowRule() {
        List<FlowRule> flowRules = new ArrayList<FlowRule>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("doSomething");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(5);

        flowRules.add(flowRule);

        FlowRuleManager.loadRules(flowRules);
    }
}

```

# 注解的方式实现流控

## 依赖的jar

```xml

	<!--sentinel注解的jar-->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-annotation-aspectj</artifactId>
            <version>1.8.1</version>
        </dependency>

```

## 关键java 代码

### 需要开启SentinelResourceAspect

`com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect`

```java
@Configuration
public class SentinelConfig {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
```



###  @SentinelResource声明一个resource

```java
@Service
public class SentinelAnnotationService {


    /**
     *
     * 注意异常处理方式是静态方法
     * @return
     */
    @SentinelResource(value = "service1", blockHandler = "handleException", blockHandlerClass = ExceptionUtils.class)
    public String service1() {
        System.out.println("service1 success");
        return "service01 return success";
    }

//fallback 也必须是静态的方法 当指定fallbackClass
//    @SentinelResource(value = "service2", fallback = "fallback")
    @SentinelResource(value = "service2", fallback = "fallback", fallbackClass = FallBackService.class)
    public String service2() {
        System.out.println("service2 success");
        return "service2 return success";
    }

    public String fallback() {
        return "系统繁忙，稍后再试";
    }
}
```

```
public class ExceptionUtils {

    public static String handleException(Exception exception) {
        return exception.getMessage();
    }
}
```

### 流控规则的加载

#### 方法1-Spring boot 启动类

```java

    public static void main(String[] args) {
        intFlowRule(); // 更改时使用Java的SPI
        SpringApplication.run(SentinelDemosApplication.class, args);
    }

    public static void intFlowRule() {
        List<FlowRule> flowRules = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("service1");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(1);
        flowRules.add(flowRule);

        FlowRule flowRule1 = new FlowRule();
        flowRule1.setResource("service2");
        flowRule1.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule1.setCount(1);
        flowRules.add(flowRule1);

        FlowRuleManager.loadRules(flowRules);
    }

```



#### 方法2 --Java SPI

1.  创建META-INF目录，然后创建目录 services

2. 创建一个文件，文件的名字为：com.alibaba.csp.sentinel.init.InitFunc

3. 创建一个InitFunc实现类，来创建具体的flowrule 

   ```java
   public class FlowRuleInitFunct implements InitFunc {
   
       @Override
       public void init() throws Exception {
           SentinelDemosApplication.intFlowRule();
       }
   }
   
   ```

# 集成sentinel-dashboard

## 依赖的jar

```xml
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-transport-simple-http</artifactId>
            <version>1.8.1</version>
        </dependency>
```



## 开启Sentinel-dashboard的vm 参数

在不使用spring-cloud-alibaba-sentinel-starter时，在启动springboot 应用程序的时候，需要添加vm 参数 `-Dcsp.sentinel.dashboard.server=localhost:9991` 来上报数据到sentinel dashboard。

![image-20220116170917615](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20220116170917615.png)



> 关于sentinel-dashboard的启动，请参考[sentinel-dashboard的安装](./Sentinel-Dashboard的安装.md)

#  

# Spring cloud 集成sentinel



##  jar 依赖

```xml
    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>2.3.12.RELEASE</spring-boot.version>
        <spring-cloud-alibaba.version>2.2.6.RELEASE</spring-cloud-alibaba.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
	</dependencies>

```

## application.properties配置

```properties
#########################Spring Cloud 配置  =====================================================================
# Sentinel 控制台地址
spring.cloud.sentinel.transport.dashboard=localhost:9991

# 取消Sentinel控制台懒加载
# 默认情况下 Sentinel 会在客户端首次调用的时候进行初始化，开始向控制台发送心跳包
# 配置 sentinel.eager=true 时，取消Sentinel控制台懒加载功能
spring.cloud.sentinel.eager=true
```


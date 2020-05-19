# seckillsystem
主要分为四个主要模块
1.api：用于存放所有的api接口
2.basic：用于存放一些工具类，常量类等等
3.service:包含各个子服务，每个服务都需要包含发布到Dubbo的端口号
  application.yml文件配置如下
  ~~~
  dubbo:
  application:
    name: user-service #修改名称
  registry:
    protocol: zookeeper
    address: 106.54.13.67:2181
  protocol:
    port: 28800 #本地发布服务的端口号
server:
  port: 8080 #模块运行的端口号
  ~~~
  SpringBoot启动类添加 @EnabbleDubbo 注解
  ~~~
@SpringBootApplication
@EnableDubbo
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
~~~
在服务类中添加Dubbo注解
~~~
package org.softwareb.userservice.service;

import com.alibaba.dubbo.config.annotation.Service; //注意 此注解来自com.alibaba.dubbo中
import org.softwareb.api.user.IUserService;

@Service //添加此注解
public class UserService implements IUserService {

    @Override
    public String test() {
        return "Hello World!";
    }
}
~~~
最后需要引入maven配置 以及服务所继承的接口依赖
~~~
    <!--Dubbo依赖-->
        <dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>0.2.0</version>
        </dependency>
    <!--Dubbo需要的接口-->
        <dependency>
            <groupId>org.softwareb</groupId>
            <artifactId>user-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
~~~

4.web：此模块主要为项目中后台模块，也就是服务的调用模块（controller模块）
需要注意Dubbo配置 application.yml配置如下（每一个子模块的配置需要更改监听端口）
~~~
server:
  port: 9090 #此处每一个模块都需要更改，不然会发生冲突
dubbo:
  application:
    name: user
  registry:
    protocol: zookeeper
    address: 106.54.13.67:2181
~~~
接口的调用需要使用@Reference注解
~~~
@RestController
@RequestMapping("/user")
public class SSOController {

    @Reference //此处需要使用reference注解而不是@Autowired
    private IUserService userService;

    @RequestMapping("/hello")
    public String test(){
        String res = userService.test();
        return res;
    }
}
~~~
同理，需要在SpringBoot项目主类中添加@EnableDubbo注解，即可调用服务

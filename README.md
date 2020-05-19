# seckillsystem
主要分为四个主要模块
1.api：用于存放所有的api接口
2.basic：用于存放一些工具类，常量类等等
3.service:包含各个子服务，每个服务都需要包含发布到Dubbo的端口号
  xml文件配置如下
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

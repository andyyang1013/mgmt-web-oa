# OA系统 #
该项目共包含3个模块，公共模块、管理后台、接口服务，其中管理后台和接口服务需要依赖公共的模块core。

<a name="获取代码"></a>
## 获取代码【GIT代码管理】
+ GitLab地址：https://git.daicaihang.cn/yxy/oa.git
 

## 环境要求 ##

+ JDK 1.8
+ Redis 3.2.10
+ Mysql 5.6.37
+ lombok 插件


## 技术栈 ##

+ Spring Boot
+ Spring Cache
+ Redis 
+ Pagehelper
+ Druid
+ Log4j2
+ Mybatis-plus

## 框架功能点 ##

+ 分布式全局唯一ID，由mybatis-plus提供工具类 IdWorker 处理
+ 分布式session共享(redis共享)
+ 返回消息统一封装
+ 异常信息统一处理
+ Cors解决跨域
+ 登录拦截器、异常拦截器
+ 日期格式支持多种转换格式："yyyy", "yyyy-MM", "yyyy-MM-dd",  "yyyy-MM-dd HH:mm" ,"yyyy-MM-dd HH:mm:ss"
+ 多环境配置
+ 基本工具类:com.yxy.oa.util包下
   
## maven打包命令 ##

+ mvn clean package -Dmaven.test.skip=true  -U
 
## 业务需求 ##

 

 











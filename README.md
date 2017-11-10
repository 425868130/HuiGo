
<h1 style="color:red">&nbsp;&nbsp;<img src="http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/logo.png?x-oss-process=style/pic-70-peaktop" style="width:80px;">惠Go</h1>

## 项目简介:
>本项目是个人的校内实训小项目，项目名称：惠Go网。这是一个简单的团购网站项目，包含了会员模块、团购模块、商家模块、后台管理模块等。主要实现了用户的登陆注册、团购活动的浏览与参团、商品搜索、个人信息管理、店铺及商品管理、平台管理等功能，各个功能模块互相协作实现最简单的电子商务网站业务流程。  
说明：由于团队成员使用了不同的前端UI组件导致了前端页面风格不协调。

## 项目人员组成:  
* 何昶源
* 徐嘉伟
* 周祥
* 黄帅英
## 技术关键词:
Vue、axois、iView、Bootstrap、jQuery、Ajax、SSH(Spring+Spring MVC+Hibernate)、Restful、Maven、前后端分离、OSS（对象存储服务）、邮箱验证  

## 部署说明:
* 环境需求：Tomcat8.0、JDK.18、MySQL5.7、IntelliJ IDEA  

* 属性配置文件路径:  
数据库脚本文件：src/resources/sql/数据库文件.sql  
数据库连接配置文件：src/resources/properties/jdbc.properties  
spring配置文件：src/resources/spring下的spring-servlet.xml以及applicationContext.xml  
Hibernate配置文件：src/resources/hibernate/hibernate.cfg.xml  
邮箱服务器配置：src/resources/properties/mail.properties  
阿里云OSS配置：src/resources/properties/ossConfig.properties  
前端路由地址配置：src/webapp/WEB-INF/res/js/project/webRouter.js  

* 开发/线上环境切换:  
后端环境的切换请打开web.xml文件找到如下代码:

        <!--配置当前激活的属性，为上下文设置默认的profile-->
        <context-param>
            <param-name>spring.profiles.default</param-name>
            <param-value>dev</param-value>
        </context-param>
修改dev为prod即可启用线上环境，此时spring将使用线上环境的dataSource Bean对应值为jdbc.properties中的线上配置。  

前端环境的切换请打开webRouter.js,修改变量env为dev,此处并非单纯切换线上/开发模式。dev模式为前后端分离模式,此时前端页面不需要通过后端模板渲染，可单独静态部署，为Spring Restful风格。如果为prod则将webapp与后端代码一同打包，此时前端代码需要通过后端的视图引擎渲染后输出，为传统Spring MVC模式。

详细部署文档参见[用户手册][docx]

## 部分界面预览
用户登录  
![用户登录][login]  
商家注册  
![商家注册][userReg]  
首页  
![首页][index]  
商品详情页  
![商品详情页][goods]  
店铺详情页  
![店铺详情页][shop]  
后台管理页面  
![后台管理页面][manager]  


[docx]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/%E7%94%A8%E6%88%B7%E6%89%8B%E5%86%8C-%E6%83%A0Go%E7%BD%91.docx
[login]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/login.png?x-oss-process=style/pic-70-peaktop
[userReg]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/userReg.png?x-oss-process=style/pic-70-peaktop
[index]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/index.png?x-oss-process=style/pic-70-peaktop
[goods]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/goods.png?x-oss-process=style/pic-70-peaktop
[shop]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/shop.png?x-oss-process=style/pic-70-peaktop
[manager]:http://my-dist.oss-cn-hangzhou.aliyuncs.com/java/HuiGo/manager.png?x-oss-process=style/pic-70-peaktop


更新时间：2017-11-10 10:42:10


## 应用使用

视频链接 https://pan.baidu.com/s/18tQQBGOeGRhZDyQ0nt2-0g

![](https://ws2.sinaimg.cn/large/006tNc79gy1fzhzgjddbyj30u00u0e86.jpg)

## 项目架构

本项目最主要的框架是MVP（Model, View, Presenter）。

![](https://ws1.sinaimg.cn/large/006tNc79gy1fzinstfop6j31460u0q7w.jpg)

MVP框架由3部分组成：View负责显示，Presenter负责逻辑处理，Model提供数据。在MVP模式里通常包含3个要素（加上View interface是4个）：

- View:负责绘制UI元素、与用户进行交互(在Android中体现为Activity/Fragment)
- Model:负责存储、检索、操纵数据(有时也实现一个Model interface用来降低耦合)
- Presenter:作为View与Model交互的中间纽带，处理与用户交互的负责逻辑。
- View interface:需要View实现的接口，View通过View interface与Presenter进行交互，降低耦合，方便进行单元测试

当我们将Activity复杂的逻辑处理移至另外的一个类（Presenter）中时，Activity其实就是MVP模式中的View，它负责UI元素的初始化，建立UI元素与Presenter的关联（Listener之类），同时自己也会处理一些简单的逻辑（复杂的逻辑交由 Presenter处理）。

MVP的Presenter是框架的控制者，承担了大量的逻辑操作，而MVC的Controller更多时候承担一种转发的作用。因此我们将此前在Activty中包含的大量逻辑操作放到控制层中，避免Activity的臃肿。Activity只需要调用View interface来实现基本的逻辑控制即可。

具体的，

+ app文件夹中放置的是activity，fragment

+ common文件夹内放置的是各类组件，如表情包，音频录制，图片请求处理，以及presenter基类

+ factory文件夹内放置了model，各个具体的presenter和推送消息接受的工厂类

## 技术选型

+ 客户端

  + 工厂模式

    工厂模式主要是为创建对象提供过渡接口，以便将创建对象的具体过程屏蔽隔离起来，达到提高灵活性的目的。 例如，我们对网络请求与相应封装了一个工厂，开辟了一个有四个线程的线程池和一个全局的json解析类，当需要发起请求时，就将对应的Runnable类交由工厂异步执行，当需要解析对应的返回值时，也交由工厂解析，当需要处理推送来的消息时也统一交由工厂类处理。

  + glide

    这是谷歌推荐的图片加载库，它的API与Picasso非常像，学习成本比较低，而且它采用了非常好的缓存策略和图片压缩算法，加载的速度非常快，也能极大地减少内存的使用量。

  + SmoothEmojiKeyBoard

    流畅切换 输入法/表情(不闪动)自动适应 输入法高度变化,高度跟随,亦可限制表情栏最小高度,避免输入法高度太小导致表情显示不完整；自动适应 动态显示/隐藏navigationbar

  + butterknife

    这是一个View注入框架，能通过自动解析注解来搜索资源文件并赋值给Activity中的字段，如使用@BindView，@BindColor替代原生的findViewById，getColor等方法，或者给View视图的监听器绑定方法，如使用@OnClick 替代 setOnClickListener等方法。而且与缓慢的反射相比，Butter Knife使用再编译时生成的代码来执行View的查找，因此不必担心注解的性能问题。

  + ucrop

    这是一个图片裁剪框架，它可以支持各种比例裁剪框和自由的图片旋转缩放，使用起来也比较简单方便，几行代码就可以实现功能的调用。

    ```java
    Uri sourceUri = Uri.parse("http://star.xiziwang.net/uploads/allimg/140512/19_140512150412_1.jpg");
            //裁剪后保存到文件中
     Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
    UCrop.of(sourceUri, destinationUri).withAspectRatio(16, 9).withMaxResultSize(300, 300).start(this);
    ```

  + Retrofit

    Retrofit 主要针对restful api，将网络请求变成方法的调用，使用起来非常简洁方便。我们在平时的作业中也屡有使用，通过注解来实现各类网络操作（get，post，put，delete）。

+ Server端

  + bean

    编写过程中遵守bean这样的代码规范，保证了服务器的各个类的属性为private，为每个类提供默认构造方法和提供属性对应的getter和setter，并配备serializable接口。

  + restful service

    数据消息的发送与接收都采用json格式，并且在api设计上遵循restful原则

    + 将API部署在专用域名之下
    + 每个网址代表一种资源，不包含动词
    + 提供过滤参数
    + 返回约定的状态码

+ 推送

  + OSS服务

    考虑到聊天应用中图片，音频视频文件占用存储空间较大，我们需要把资源文件单独存储来保证服务器的高效性，因此我们采用了阿里云的oss服务。这样也能减少我们重复部署静态资源服务器的麻烦。

  + 第三方推送平台

    一个聊天应用需要推送服务来保证客户端收到其他人发来的消息，而推送的实现方式大概有三种。

    + 轮询。客户端定期询问服务器有没有新的消息, 这样服务器不用管客户端的地址是什么, 客户端来问, 直接告诉它就行。
    + SMS通知， 让客户端拦截手机短信, 服务器在有新消息时给用户的手机号发一条特殊的短信。
    + 长连接，这大概是目前情况下最佳的方案了, 客户端主动和服务器建立TCP长连接之后, 客户端定期向服务器发送心跳包, 有消息的时候, 服务器直接通过这个已经建立好的TCP连接通知客户端。

    由上可知自建推送服务的难度是很高的，不仅要求开发人员有丰富的开发经验，还要求其对网络编程方面的知识有深入的了解，必须保证服务能长时间零差错地运行。因此我们选择第三方推送平台，基于长连接实现，保证了聊天的可靠性。

## 应用部署

1. 安装JAVA，Mysql，Tomcat

2. 启动Tomcat

   ```sh
   sudo systemctl start tomcat   //启动
   sudo systemctl restart tomcat  //重启
   sudo systemctl stop tomcat   //停止
   sudo service tomcat status // 查看状态
   ```

3. 部署Tomcat

   首先我们配置Tomcat Web管理界面：

   ```
   sudo vi /usr/share/tomcat/conf/tomcat-users.xml
   ```

   英文输入法情况下按 **i** 进入编辑状态，在非注释空白地方回车加入：

   ```
       <user username="admin" password="adminpwd" roles="manager-gui,admin-gui"/>
   ```

   以上操作配置了Tomcat Web管理员的账户和密码以及可管理的权限。
   紧接着按**Esc**进入退出操作，**shift+q** 进入退出操作，此时输入 **wq** 保存并退出；回车即可完成。

   此时你可以启动Tomcat，并在你自己电脑使用浏览器访问你的服务器了。

   ```
   http://ip_address:8080
   ```

   默认端口为8080。你还可以进入管理界面：

   ```
   http://ip_address:8080/manager/html
   ```

   输入上面配置的用户名和密码即可进入管理界面。

   接着我们配置项目

   执行命令：

   ```
   cd /usr/share/tomcat/conf/
   ls
   ```

   以上两个命令执行后，可以看到服务器的配置文件：
   [![image](https://user-images.githubusercontent.com/5687320/28243830-0e280f0c-6a0b-11e7-9306-314c5734c209.png)](https://user-images.githubusercontent.com/5687320/28243830-0e280f0c-6a0b-11e7-9306-314c5734c209.png)

   开始编辑:

   ```
   vi server.xml 
   ```

   插入以下代码

   ```xml
   <Service name="Catalina-italker">
       <Connector port="8688" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
       <Connector port="8689" protocol="AJP/1.3" redirectPort="8443" />
       <Engine name="Catalina" defaultHost="localhost">
           <Realm className="org.apache.catalina.realm.LockOutRealm">
               <Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase" />
           </Realm>
           <Host name="localhost" appBase="webapps-italker" unpackWARs="true" autoDeploy="true">
               <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="italker_access_log." suffix=".txt" pattern="%h %l %u %t &quot;%r&quot; %s %b" />
           </Host>
       </Engine>
   </Service>
   ```

4. 启动应用

   如上我们指明了项目运行的代码文件和应用名称，接着在tomcat目录下创建文件夹 **webapps-italker**，

   把服务器代码生成的war文件拷贝在该文件夹下。

## 应用测试

+ 服务器压力测试

  我们采用apache benchmark来对服务器进行压力测试，模型请求1000个请求，10个并发，发现请求全部成功，请求时间评价不到0.1s。

  ```sh
  ab -n1000 -c10 http://localhost:8080/api/user 
  ```

  ![](https://ws3.sinaimg.cn/large/006tNc79ly1fzi04eepxdj31ay0hq78b.jpg)

  ![](https://ws2.sinaimg.cn/large/006tNc79ly1fzi03i2yarj31440u04a8.jpg)

+ 安卓性能测试

  利用**Android Profiler**进行性能测试，发现CPU占用率良好，没有内存垃圾。

  ![](https://ws4.sinaimg.cn/large/006tNc79ly1fzi09n5jvlj31ah0u0u0x.jpg)

  ![image-20190124214215606](/var/folders/27/4j7qwl0n383_wdy85h_txrzr0000gn/T/abnerworks.Typora/image-20190124214215606.png)

+ 第三方平台测试

  阿里云服务器实际测试延时接近200ms

  推送平台免费版本支持不超过20人的并发长连接


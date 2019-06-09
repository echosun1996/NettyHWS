# NettyHWS 使用说明

## 介绍

NettyHWS 是一款基于 Netty 的实现HTTP 、 Socket 和 WebSocket 通信的框架。

- 为了让 Java 程序员能将更多的精力放在基于网络通信的业务逻辑实现上，而不是过多的纠结于网络底层 NIO 的实现以及处理难以调试的网络问题，Netty 应运而生。
- 为了让开发者将更多的精力投入到业务层逻辑的实现上，而不是重复的建造底层通信的轮子， NettyHWS 应运而生。

NettyHWS 的命名由两大部分组成，分别是Netty和HWS，前者说明了本框架是基于 Netty 封装而成的。后者是 HTTP 、 WebSocket 和 Socket 首字母缩写。

通过本框架的使用，希望能够简化游戏、视频等高并发网络业务的代码。

## 名词解释
- Mapping 映射
形式为 *Map<String, Class>* ，由一个路径和对应的处理类组成。

- Control 控制器
使用反射机制，调用对应的类处理 *URI* 请求。



## 设计原理

1. 服务启动时调用 *NettyHWSApplication* 方法，传入包名。该方法搜索指定包路径下所有的类，通过判断类中是否含有特定的注解，进而加载指定的类。（目前支持HttpMapping、WebSocketMapping、SocketMapping 。）
```java
public class TestMain {
    public static void main(String[] args) {
        // 启动服务
        new NettyHWSApplication(13142,"org.nettyhws.test.demo",true);
    }
}
```

2. *NettyHWSApplication* 方法首先执行 *inserClassFromPackageName* 方法，该方法会在给定的包名中查找符合注解要求的类，并将符合要求的类和对应的映射路径存入工厂类 *Mapping* 中。随后，会执行 *startServer* 方法，执行*NettyHWSServer* 下 *connect* 方法。
3. *connect* 方法执行一般化的 *Netty* 逻辑。最后为 *SocketChannel* 添加一个自定义的 *ChannelInboundHandler* 。
4. 在 *ChannelInboundHandler* 中会根据协议的不同，执行不同的代码逻辑，接着调用不同的控制器（ *control* ）处理。需要注意的是， *WebSocket* 第一个数据包走 *HTTP* 协议，且含有 `Upgrade: websocket` 标记。故而，判断是 *WebSocket* 第一个数据包后，设置一个 *URI*
标记，保存请求的 URI ，然后下个 *WebSocket* 数据包通过标记来判断是否有已经注册的控制器以处理。 
  ```http
  HttpObjectAggregator$AggregatedFullHttpRequest(decodeResult: success, version: HTTP/1.1, content: CompositeByteBuf(ridx: 0, widx: 0, cap: 0, components=0))
  GET /websss HTTP/1.1
  Upgrade: websocket
  Connection: Upgrade
  Host: localhost:13142
  Origin: file://
  Pragma: no-cache
  Cache-Control: no-cache
  Sec-WebSocket-Key: Un5ZJQ/JGC07xea3N8jOGw==
  Sec-WebSocket-Version: 13
  Sec-WebSocket-Extensions: x-webkit-deflate-frame
  User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/605.1.15 (KHTML, like Gecko)
  content-length: 0
  ```



## 使用方法
- 添加maven
```xml
<dependencies>
    <dependency>
      <groupId>org.nettyhws</groupId>
      <artifactId>nettyhws</artifactId>
      <version>1.1.5</version><!--对应NettyHWS版本-->
    </dependency>
    <dependency>
      <groupId>io.netty</groupId>
      <artifactId>netty-all</artifactId>
      <version>4.1.13.Final</version>
      <scope>compile</scope>
    </dependency>
</dependencies>
```
- 创建一个Server  

```java
public class TestMain {
    public static void main(String[] args) {
        // 启动服务
        new NettyHWSApplication(13142,"org.nettyhws.test.demo",true);
    }
}
```
- 如果使用的通信协议是 HTTP ，则需要创建一个类，并在类前添加一个 **HttpMapping** 注解，注解中注明需要绑定的路径。这个类继承 **HttpBoss** 这个接口，实现 **httpReceiver** 方法。

```java
@HttpMapping("/HttpTest")
public class HttpDemo implements HttpBoss {
    @Override
    public void httpReceiver(ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext, String post, Map<Object, Object> param) {
        for(Object o:param.keySet()){
            System.out.println("Param: key= "+ o.toString() + " and value= " + param.get(o).toString());
        }
        System.out.println("POST: "+post);
        channelInboundHandler.response(channelHandlerContext,"Server:Receive HTTP Message!", HttpCode.OK);

    }
}
```

- 如果使用的通信协议是 WebSocket ，则需要创建一个类，并在类前添加 **WebSocketMapping** 注解，注解中注明需要绑定的 **路径** 。
```java
@WebSocketMapping("/WebSocketTest")
public class WebSocketDemo implements WebSocketBoss {
    @Override
    public void webSocketReceiver(ChannelInboundHandler channelInboundHandler, ChannelHandlerContext channelHandlerContext, String message) {
        System.out.println("WebSocket Message: "+message);
        channelInboundHandler.response(channelHandlerContext,"Server:Receive WebSocketDemo Message!", HttpCode.WEB_SOCKET);
    }
}


```
> 亦可参考Demo。

## 源代码目录结构说明

包中存在下面的目录和文件：
```
NettyHWS
├── README.md 说明文档
├── pom.xml maven 配置信息
└── src/
    └── main/
        └── java/
            └── org/
                └── nettyhws/
                    └── nettyhws/
                        ├── ChannelInboundHandler.java 协议实现类
                        ├── NettyHWSApplication.java NettyHWS 服务启动入口
                        ├── NettyHWSServer.java Netty 一般化代码
                        ├── agreement/
                        │   └── HttpMessage.java 保存 HTTP 协议的信息和携带的参数
                        ├── annotations/
                        │   ├── HttpMapping.java HTTP 注解
                        │   └── WebSocketMapping.java WebSocket 注解
                        ├── constant/
                        │   ├── Config.java 静态方法，保存框架静态常量
                        │   └── HttpCode.java 规定HTTP返回值
                        ├── control/
                        │   ├── HttpControl.java HTTP 控制器
                        │   └── WebSocketControl.java WebSocket 控制器
                        ├── def/
                        │   └── Mapping.java 保存控制器和对应路径关系的工厂类
                        ├── i/
                        │   ├── HttpBoss.java HTTP 接口
                        │   ├── RequestManager.java 用于向客户端返回消息的接口
                        │   └── WebSocketBoss.java WebSocket 接口
                        ├── log/
                        │   └── SystemLog.java 系统消息
                        ├── son/
                        │   ├── AbstractHttpBoss.java 抽象类，用于执行 HttpBoss 方法
                        │   └── AbstractWebSocketBoss.java 抽象类，用于执行 WebSocketBoss 方法
                        └── tools/
                            ├── PackageUtil.java 根据包名获取包下面所有的类名
                            └── ShareCon.java 用于处理 HTTP 请求，提取其中的参数
```


## 版权协议
对 NettyHWS 代码的修改和变更，需要遵守 Apache Licence 2.0 。


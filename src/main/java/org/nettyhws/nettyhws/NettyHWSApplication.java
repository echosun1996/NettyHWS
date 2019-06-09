package org.nettyhws.nettyhws;

import org.nettyhws.nettyhws.annotations.HttpMapping;
import org.nettyhws.nettyhws.annotations.WebSocketMapping;
import org.nettyhws.nettyhws.constant.Config;
import org.nettyhws.nettyhws.def.Mapping;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.tools.PackageUtil;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

/**
 * NettyHWS 主服务。
 *
 * @author thenk008,echosun
 */
public class NettyHWSApplication {
    static {
        SystemLog.WELCOME();
    }

    /**
     * NettyHWS 服务启动入口。
     *
     * @param port 服务监听端口
     * @param packageName 包名
     * @param recursive 是否迭代
     */
    public NettyHWSApplication(int port,String packageName,Boolean recursive) {
        insertClassFromPackageName(packageName,recursive);
        startServer(port);
    }

    /**
     * 负责启动所有控制器。
     */
    private void startServer(int port){
        NettyHWSServer httpsServer=new NettyHWSServer();
        httpsServer.connect(port);
    }

    /**
     * 通过包名将所有类导入。
     * 注意！这里会递归查找所有符合条件的包。只要符合注解要求，无论几层，都会被注册到系统中。
     *
     * @param packageName 包名
     * @param recursive 是否迭代
     */
    private void insertClassFromPackageName(String packageName,boolean recursive){
        HashMap<String,Class> httpClassMap= new HashMap<>();
        HashMap<String,Class> webSocketClassMap= new HashMap<>();
        try {
            // 得到包下所有的类。
            Set<Class<?>> classSet =PackageUtil.getClasses(packageName,recursive);
            // 包名检查。
            if(classSet.size()==0){
                SystemLog.FATAL("初始化错误。请检查包是否存在或错写成类名。");
            }
            // 遍历所有的类。
            for (Class clazz : classSet) {
                // 标志循环到的类是否符合注解规范。
                boolean isNettyHWSController=false;
                // 标志符合注解规范的类是哪个具体类型。
                String annotationType = null;
                // 得到并循环所有注解。
                Annotation[] httpMappings = clazz.getAnnotations();
                for(Annotation annotation:httpMappings) {
                    SystemLog.DEBUG(clazz.getName() + "has Annotation:", annotation.toString());
                    // 在所有的已知注解规范中寻找是否有匹配的注解名。
                    for(int i=0;i<Config.ANNOTATION_NAME.length;i++){
                        if(annotation.toString().indexOf(Config.ANNOTATION_NAME[i])==0){
                            isNettyHWSController=true;
                            annotationType=Config.ANNOTATION_NAME[i];
                            break;
                        }
                    }
                }
                // 不符合规范则继续分析下一个类。
                if(!isNettyHWSController){
                    continue;
                }
                // 符合注解规范，则认为该类是一个映射。
                // 判断是一个 HTTP 的映射。
                if(annotationType.equals(Config.HTTP_MAPPING)){
                    HttpMapping httpMapping= (HttpMapping) clazz.getAnnotation(HttpMapping.class);
                    String uri=uriHandle(httpMapping.value());
                    if(httpClassMap.get(uri)!=null){
                        SystemLog.ERROR("已有 HTTP 绑定 "+httpClassMap.get(httpMapping.value()).getName());
                        SystemLog.ERROR("欲绑定 "+clazz.getName()+" 出现冲突！");
                        SystemLog.FATAL("请检查是否出现同一路径被重复绑定");
                    }
                    SystemLog.INFO("新增 HTTP 绑定:"+clazz.getName()+" 绑定到路径:"+uri);
                    httpClassMap.put(uri,clazz);
                }
                // 判断是一个 WebSocketDemo 的映射。
                else if (annotationType.equals(Config.WEBSOCKET_MAPPING)){
                    WebSocketMapping webSocketMapping= (WebSocketMapping) clazz.getAnnotation(WebSocketMapping.class);
                    String uri=uriHandle(webSocketMapping.value());
                    if(webSocketClassMap.get(uri)!=null){
                        SystemLog.ERROR("已有 WebSocketDemo 绑定 "+webSocketClassMap.get(webSocketMapping.value()).getName());
                        SystemLog.ERROR("欲绑定 "+clazz.getName()+" 出现冲突！");
                        SystemLog.FATAL("请检查是否出现同一路径被重复绑定");
                    }
                    SystemLog.INFO("新增 WebSocketDemo 绑定:"+clazz.getName()+" 绑定到路径:"+uri);
                    webSocketClassMap.put(uri,clazz);
                }
            }
            // 将所有的映射存入工厂类中。
            Mapping.get().setHttpController(httpClassMap);
            Mapping.get().setWebSocketController(webSocketClassMap);
        } catch (ClassCastException e){
            SystemLog.FATAL(packageName+" 该类无法被识别");
        }
    }

    /**
     * 处理注解中的 URI，将末尾的斜线（'/'）删除。
     *
     * @param oriURI 处理前的 URI
     * @return 删除末尾斜线后的URI
     */
    private String uriHandle(String oriURI){
        if(oriURI.charAt(oriURI.length()-1)=='/'){
            SystemLog.ERROR("发现不建议的路径："+oriURI+" 不建议以斜线结束");
            int i=oriURI.length()-1;
            while(oriURI.charAt(oriURI.length()-1)=='/'){
                oriURI = oriURI.substring(0,oriURI.length() - 1);
            }
            SystemLog.ERROR("已修正为："+oriURI);
        }
        return oriURI;
    }
}

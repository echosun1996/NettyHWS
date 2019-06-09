package org.nettyhws.nettyhws;

import org.nettyhws.nettyhws.annotations.HttpMapping;
import org.nettyhws.nettyhws.annotations.WebSocketMapping;
import org.nettyhws.nettyhws.constant.Config;
import org.nettyhws.nettyhws.def.ResConfig;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.tools.PackageUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

/**
 * NettyHWS 主服务
 *
 * @author echosun
 */
public class NettyHWSApplication {
    // Controller 路径，启动服务后将在这个路径中通过注解判断是否注册到服务中

    NettyHWSServer httpsServer;
    int port;

    static {
        SystemLog.WELCOME();
    }
    public NettyHWSApplication(int port,String packageName) {
        inserClassFromPackageName(packageName);
        startServer(port);
    }

    /**
     * 负责启动所有控制器。
     */
    private void startServer(int port){
        httpsServer=new NettyHWSServer();
        httpsServer.connect(port);
//        this.port=port;
//        for (String key : urlToClassMap.keySet()) {
//            System.out.println("key= "+ key + " and value= " + urlToClassMap.get(key));
//
//            port+=10;
//        }

    }

    /**
     * 通过包名将所有类导入。
     * 注意！这里会递归查找所有符合条件的包。只要符合注解要求，无论几层，都会被注册到系统中。
     * @param packageName 包名
     */
    private void inserClassFromPackageName(String packageName){
        HashMap<String,Class> httpClassMap= new HashMap<>();
        HashMap<String,Class> webSocketClassMap= new HashMap<>();
        try {
            Set<Class<?>> classSet =PackageUtil.getClasses(packageName);
            if(classSet.size()==0){
                SystemLog.FATAL("初始化错误。请检查包是否存在或错写成类名。");
            }

            for (Class clazz : classSet) {
                boolean isNettyHWSController=false;
                String annotationType = null;

                Annotation[] httpMappings = clazz.getAnnotations();
                for(Annotation annotation:httpMappings) {

                    SystemLog.DEBUG(clazz.getName() + "has Annotation:", annotation.toString());

                    for(int i=0;i<Config.ANNOTATION_NAME.length;i++){
                        if(annotation.toString().indexOf(Config.ANNOTATION_NAME[i])==0){
                            isNettyHWSController=true;
                            annotationType=Config.ANNOTATION_NAME[i];
                            break;
                        }
                    }
                }
                if(!isNettyHWSController){
                    continue;
                }

                if(annotationType.equals(Config.HTTP_MAPPING)){
                    HttpMapping httpMapping= (HttpMapping) clazz.getAnnotation(HttpMapping.class);
                    String uri=uriHandle(httpMapping.value());
                    if(httpClassMap.get(uri)!=null){
                        SystemLog.ERROR("已有 HTTP 绑定 "+httpClassMap.get(httpMapping.value()).getName());
                        SystemLog.ERROR("欲绑定 "+clazz.getName()+" 出现冲突！");
                        SystemLog.FATAL("请检查是否出现同一路径被重复绑定。");
                    }
                    SystemLog.INFO("新增 HTTP 绑定:"+clazz.getName()+" 绑定到路径:"+uri);
                    httpClassMap.put(uri,clazz);
                    
                }
                else if (annotationType.equals(Config.WEBSOCKET_MAPPING)){
                    WebSocketMapping webSocketMapping= (WebSocketMapping) clazz.getAnnotation(WebSocketMapping.class);
                    String uri=uriHandle(webSocketMapping.value());
                    if(webSocketClassMap.get(uri)!=null){
                        SystemLog.ERROR("已有 WebSocket 绑定 "+webSocketClassMap.get(webSocketMapping.value()).getName());
                        SystemLog.ERROR("欲绑定 "+clazz.getName()+" 出现冲突！");
                        SystemLog.FATAL("请检查是否出现同一路径被重复绑定。");
                    }
                    SystemLog.INFO("新增 WebSocket 绑定:"+clazz.getName()+" 绑定到路径:"+uri);
                    webSocketClassMap.put(uri,clazz);
                }



            }
            ResConfig.get().setHttpController(httpClassMap);
            ResConfig.get().setWebSocketController(webSocketClassMap);

        } catch (ClassCastException e){
            SystemLog.FATAL(packageName+" 该类无法被识别");
        }
    }

    /**
     * 处理注解中的 URI
     * @param oriURL
     * @return
     */
    String uriHandle(String oriURL){
        if(oriURL.charAt(oriURL.length()-1)=='/'){
            SystemLog.ERROR("发现不建议的路径："+oriURL+" 不建议以斜线结束。");
            int i=oriURL.length()-1;
            while(oriURL.charAt(oriURL.length()-1)=='/'){
                oriURL = oriURL.substring(0,oriURL.length() - 1);
            }

            SystemLog.ERROR("已修正为："+oriURL);
        }

        return oriURL;
    }







}

package org.nettyhws.nettyhws;

import com.sun.net.httpserver.HttpsServer;
import org.nettyhws.nettyhws.annotations.HttpMapping;
import org.nettyhws.nettyhws.constant.Config;
import org.nettyhws.nettyhws.log.SystemLog;
import org.nettyhws.nettyhws.tools.PackageUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * NettyHWS 主服务
 *
 * @author echosun
 */
public class NettyHWSApplication {
    // Controller 路径，启动服务后将在这个路径中通过注解判断是否注册到服务中
    Map<String, Class> urlToClassMap;
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
//        httpsServer.connect(port,);
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
        urlToClassMap= new HashMap<>();
        try {
            Set<Class<?>> classSet =PackageUtil.getClasses(packageName);
            if(classSet.size()==0){
                SystemLog.FATAL("初始化错误。请检查包是否存在或错写成类名。");
            }

            for (Class clazz : classSet) {
                boolean isNettyHWSController=false;
                Annotation[] httpMappings = clazz.getAnnotations();
                for(Annotation annotation:httpMappings) {

                    SystemLog.DEBUG(clazz.getName() + "has Annotation:", annotation.toString());

                    for(int i=0;i<Config.ANNOTATION_NAME.length;i++){
                        if(annotation.toString().indexOf(Config.ANNOTATION_NAME[i])==0){
                            isNettyHWSController=true;
                        }
                    }
                }
                if(!isNettyHWSController){
                    continue;
                }
                HttpMapping httpMapping= (HttpMapping) clazz.getAnnotation(HttpMapping.class);
                if(urlToClassMap.get(httpMapping.value())!=null){
                    SystemLog.ERROR("已有绑定 "+urlToClassMap.get(httpMapping.value()).getName());
                    SystemLog.ERROR("欲绑定 "+clazz.getName()+" 出现冲突！");
                    SystemLog.FATAL("请检查是否出现同一路径被重复绑定。");
                }
                SystemLog.INFO("新增绑定:"+clazz.getName());
                urlToClassMap.put(httpMapping.value(),clazz);
            }

        } catch (ClassCastException e){
            SystemLog.FATAL(packageName+" 该类无法被识别");
        }
    }








}
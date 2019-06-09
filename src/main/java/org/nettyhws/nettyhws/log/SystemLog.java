package org.nettyhws.nettyhws.log;

import org.nettyhws.nettyhws.constant.Config;

/**
 * 系统消息。
 *
 * @author echosun
 */
public class SystemLog {
    /**
     * 调试。
     * 细粒度记录应用程序的正常运行过程中的信息，帮助调试和诊断应用程序。
     * @param tag 调试信息标签
     * @param message 调试信息内容
     */
    public static void DEBUG(String tag, String message){
        if(Config.DEGUB) {
            System.out.println("================================================================");
            System.out.println("[DEBUG] " + tag);
            System.out.println(message);
            System.out.println("================================================================");
        }
    }

    /**
     * 欢迎信息。
     */
    public static void WELCOME(){
        System.out.println("" +
                "[INFO] ------------------------------------------------------------------------\n" +
                "[INFO] NettyHWS Started.\n" +
                "[INFO] ------------------------------------------------------------------------");
    }

    /**
     * 提示信息
     * 粗粒度记录应用程序的正常运行过程中的关键信息。
     *
     * @param info 一般运行消息
     */
    public static void INFO(String info){
        System.out.println("[INFO] "+info);
    }

    /**
     * 警告。
     * 预期之外的运行状况，可能会出现潜在错误的情形。
     *
     * @param warn 警告信息
     */
    public static void WARN(String warn){
        System.out.println("[WARN] "+warn);
    }

    /**
     * 错误。
     * 错误事件，影响正常使用。但仍然不影响系统的继续运行。
     *
     * @param error 错误信息
     */
    public static void ERROR(String error){
        System.out.println("[ERROR] "+error);
    }

    /**
     * 严重。
     * 严重的错误事件，将会导致应用程序的退出。慎用。
     *
     * @param fatal 严重错误信息
     */
    public static void FATAL(String fatal){
        System.out.println("[FATAL] "+fatal);
        System.out.println("[FATAL] 系统出现严重错误，已终止运行。");
        System.exit(-1);
    }
}

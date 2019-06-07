package org.nettyhws.nettyhws.log;

/**
 * 系统消息。
 *
 * @author echosun
 */
public class SystemLog {

    public static void WELCOME(){
        System.out.println("" +
                "[INFO] ------------------------------------------------------------------------\n" +
                "[INFO] nettyhws Started.\n" +
                "[INFO] ------------------------------------------------------------------------");
    }

    public static void INFO(String info){
        System.out.println("[INFO] "+info);
    }
    public static void WARNING(String warning){
        System.out.println("[WARNING] "+warning);
    }
    public static void DEBUG(String tag, String message){
        System.out.println("================================================================");
        System.out.println("[DEBUG] "+tag);
        System.out.println(message);
        System.out.println("================================================================");
    }
}

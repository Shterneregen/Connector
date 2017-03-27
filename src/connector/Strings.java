
package connector;

public class Strings {
    private static final String STR_EXIT = "</exit/>";
    private static final String STR_EXIT_ALL = "</exit-all/>";
    private static final String STR_GET_ALL_MSG = "</GET-ALL/>";   
    private static final String STR_GOOD_CONN = "</GOOD-CONNECTION/>";
    private static final String STR_SEND_PUB_KEY = "</sendpubk/>";
    private static final String STR_ON_RSA = "</STR-ON-RSA/>";
    
    private static final String STR_WRONG_PASS = "--- Неправильный пароль ---";
    private static final String STR_STOP_SERVER = "--- Сервер был остановлен ---";
    private static final String STR_SAME_NIC = "--- Такой ник уже есть в чате ---";
    private static final String STR_WRONG_PORT = "Неверный номер порта";
    private static final String STR_WRONG_IP = "Неверный формат IP";

    private static final String STR_SET_IP = "IP установлен: ";
    private static final String STR_SET_PORT = "Порт установлен: ";
    private static final String STR_SET_NIC = "Ник установлен: ";
    private static final String STR_SET_PASS = "Пароль установлен";
    private static final String STR_YOU_EXIT = "--- Вы покинули чат ---";
    private static final String STR_NON_ACK = "--- Сервер не отвечает ---";   
    
    private static final String STR_TO_CONN = "Нет подключения";
    
//    private static final String STR_WRONG_PASS = "--- Wrong password ---";
//    private static final String STR_STOP_SERVER = "--- The server has been shut down ---";
//    private static final String STR_SAME_NIC = "--- This nickname is already in the chat ---";
//    private static final String STR_SAME_NIC = "</BAD-NIC/>";
//    private static final String STR_YOU_EXIT = "--- You left the conversation ---";
//    private static final String STR_NON_ACK = "--- The server is not responding ---"; 
    
    
    private static final String MAIN_NAME = "Connector 1.1";
    
    
    public static String getSTR_GET_ALL_MSG() {
        return STR_GET_ALL_MSG;
    }

    public static String getMAIN_NAME() {
        return MAIN_NAME;
    }

    public static String getSTR_EXIT_ALL() {
        return STR_EXIT_ALL;
    }

    public static String getSTR_YOU_EXIT() {
        return STR_YOU_EXIT;
    }

    public static String getSTR_SET_IP() {
        return STR_SET_IP;
    }

    public static String getSTR_SET_PORT() {
        return STR_SET_PORT;
    }

    public static String getSTR_SET_NIC() {
        return STR_SET_NIC;
    }

    public static String getSTR_SET_PASS() {
        return STR_SET_PASS;
    }

    public static String getSTR_WRONG_PORT() {
        return STR_WRONG_PORT;
    }

    public static String getSTR_WRONG_IP() {
        return STR_WRONG_IP;
    }

    public static String getSTR_SAME_NIC() {
        return STR_SAME_NIC;
    }

    public static String getSTR_STOP_SERVER() {
        return STR_STOP_SERVER;
    }

    public static String getSTR_WRONG_PASS() {
        return STR_WRONG_PASS;
    }

    public static String getSTR_EXIT() {
        return STR_EXIT;
    }
    
    public static String getSTR_TO_CONN() {
        return STR_TO_CONN;
    }
    
    public static String getSTR_NON_ACK() {
        return STR_NON_ACK;
    }
    
    public static String getSTR_GOOD_CONN() {
        return STR_GOOD_CONN;
    }
    
    public static String getSTR_SEND_PUB_KEY() {
        return STR_SEND_PUB_KEY;
    }
    
    public static String getSTR_ON_RSA() {
        return STR_ON_RSA;
    }
}

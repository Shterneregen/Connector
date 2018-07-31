package connector.model;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Сообщение, передаётся между сервером и клиентом
 *
 * @author Yura
 */
public class Message implements Serializable {

    private boolean fSystemMessage;
    private String psw;
    private String name;
    private String message;
    private PublicKey publicKey;

    public boolean isfSystemMessage() {
        return fSystemMessage;
    }

    public String getPsw() {
        return psw;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Для передачи открытого ключа
     *
     * @param publicKey открытый ключ
     */
    public Message(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Для инициализации клиента на сервере
     *
     * @param psw пароль сессии
     * @param name имя пользователя
     */
    public Message(String psw, String name) {
        this.psw = psw;
        this.name = name;
    }

    /**
     * Стандартное сообщение от клиента
     *
     * @param message сообщение
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * От сервера, обычное
     *
     * @param message сообщение сервера
     * @param fSystemMessage флаг системного сообщения
     */
    public Message(String message, boolean fSystemMessage) {
        this.message = message;
        this.fSystemMessage = fSystemMessage;
    }

    /**
     * От сервера, ответ на первое сообщение клиента
     *
     * @param message сообщение
     * @param fSystemMessage флаг системного сообщения
     * @param publicKey открытый ключ сервера
     */
    public Message(String message, boolean fSystemMessage, PublicKey publicKey) {
        this.message = message;
        this.fSystemMessage = fSystemMessage;
        this.publicKey = publicKey;
    }
}

package connector.model;

import java.io.Serializable;
import java.security.PublicKey;

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

    // От клиента
    public Message(String psw, String name, PublicKey publicKey) {
        this.psw = psw;
        this.name = name;
        this.publicKey = publicKey;
    }

    public Message(String message) {
        this.message = message;
    }

    // От сервера
    public Message(String message, boolean fSystemMessage) {
        this.message = message;
        this.fSystemMessage = fSystemMessage;
    }

    public Message(String message, boolean fSystemMessage, PublicKey publicKey) {
        this.message = message;
        this.fSystemMessage = fSystemMessage;
        this.publicKey = publicKey;
    }
}

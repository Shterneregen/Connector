package connector.model;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable{
    private boolean fSystemMessage;   
    private String pass;
    private String name;
    private String message;
    private PublicKey publicKey;

    public boolean isfSystemMessage() {
        return fSystemMessage;
    }

    public String getPass() {
        return pass;
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
    public Message(String pass, String name, PublicKey publicKey){
        this.pass = pass;
        this.name = name;
        this.publicKey = publicKey;
    }
    
    public Message(String message){
        this.message = message;
    }
    
    // От сервера
    public Message(String message, boolean fSystemMessage){
        this.message = message;
        this.fSystemMessage = fSystemMessage;
    }
    
    public Message(String message, boolean fSystemMessage, PublicKey publicKey){
        this.message = message;
        this.fSystemMessage = fSystemMessage;
        this.publicKey = publicKey;
    }
}

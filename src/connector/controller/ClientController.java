/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.controller;

import connector.constant.ClientType;
import connector.model.Client;
import connector.model.Message;
import connector.utils.Encryption;
import connector.utils.Utils;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yura
 */
public class ClientController extends java.util.Observable {

    private Client client;
    private Resender resender;
    private Message message;
    private String receiveStr;

    private Encryption serverEncryption;
    private Encryption clientEncryption;

    private ServerController serverController;
    private ClientType clientType;

    public ClientController(ClientType clientType) {
        this.clientType = clientType;
        clientEncryption = new Encryption();
        serverEncryption = new Encryption();
    }

    public void sendMsg(String message) throws IOException {
        client.getOutputStream().writeObject(new Message(serverEncryption.encrypt(message)));
    }

    public boolean setConnection(String ip, String port, String nic, String psw) {

        Optional<String> optionalIp = Utils.getAndCheckIP(ip);
        Optional<Integer> optionalPort = Utils.getAndCheckPort(port);

        if (optionalIp.isPresent() && optionalPort.isPresent()) {
            if (clientType.equals(ClientType.CLIENT_WITH_SERVER)) {
                serverController = new ServerController(port, psw);
                serverController.startServer();
            }
            client = new Client(optionalIp.get(), optionalPort.get(), nic, psw);
        }

        try {
            // Запускаем поток получения сообщений от серверной части
            resender = new Resender();
            resender.start();
            // Client send pub key
            client.getOutputStream().writeObject(new Message(clientEncryption.getPublicKeyFromKeypair()));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean disonnect() {
        if (clientType.equals(ClientType.CLIENT_WITH_SERVER)) {
            serverController.stopServer();
        } else {
            resender.setStop();
        }
        return true;
    }

    public String getReceiveStr() {
        return receiveStr;
    }

    public Message getMessage() {
        return message;
    }

    public void resenderSetStop() {
        resender.setStop();
    }

    //<editor-fold defaultstate="collapsed" desc="class Resender">
    private class Resender extends Thread {

        private boolean stoped = false;
        private boolean firstMsg = true;
//        private Message message;
        private String commandToMsg;

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            try {
                while (!stoped) {
                    try {
                        message = (Message) client.getInputStream().readObject();
                        if (firstMsg) {
                            firstMsg = false;
                            // Client get pub key from server
                            serverEncryption.createPair(message.getPublicKey());

                            // Client send psw & nic
                            String pfStr = client.getPsw();
                            client.getOutputStream().writeObject(new Message(
                                    serverEncryption.encrypt(pfStr),
                                    serverEncryption.encrypt(client.getNicname())));
                            continue;
                        }

                        // Client receive msg from server
                        receiveStr = Utils.removeTheTrash(clientEncryption.decrypt(message.getMessage()));

                        setChanged();
                        notifyObservers();
                    } catch (IOException | ClassNotFoundException e) {
                        break;
                    }
                }
            } finally {
                client.closeStreams();
            }
        }
    }
    //</editor-fold>

}

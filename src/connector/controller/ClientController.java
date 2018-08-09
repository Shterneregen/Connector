/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.controller;

import connector.constant.ClientType;
import connector.model.Client;
import connector.model.Message;
import connector.resources.ControlLines;
import connector.utils.Encryption;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import connector.view.ClientPanel;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yura
 */
public class ClientController {

    private Client client;
    private Resender resender;
    private String receiveStr;

    private Encryption serverEncryption;
    private Encryption clientEncryption;

    private ServerController serverController;
    private ClientType clientType;
    private ClientPanel clientPanel;

    public ClientController(ClientType clientType) {
        this.clientType = clientType;
        clientEncryption = new Encryption();
        serverEncryption = new Encryption();
    }

    public void sendMsg(String message) throws IOException {
        client.getOutputStream().writeObject(new Message(serverEncryption.encrypt(message)));
    }

    public boolean setConnection(String ip, String port, String nic, String psw, ClientPanel clientPanel) {
        this.clientPanel = clientPanel;

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

    public boolean setConnection(String ip, String port, String nic, String psw) {
        return setConnection(ip, port, nic, psw, null);
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

    //<editor-fold defaultstate="collapsed" desc="class Resender">
    private class Resender extends Thread {

        private boolean stoped = false;
        private boolean firstMsg = true;
        private Message message;
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
                    } catch (IOException | ClassNotFoundException e) {
                        break;
                    }
                    switch (receiveStr) {
                        case ControlLines.STR_WRONG_PASS:
                            commandToMsg = ProjectProperties.getString("wrong_pass");
                            break;
                        case ControlLines.STR_SAME_NIC:
                            commandToMsg = ProjectProperties.getString("same_nic");
                            break;
                        case ControlLines.STR_STOP_SERVER:
                            commandToMsg = ProjectProperties.getString("stop_server");
                            break;
                        default:
                            break;
                    }

                    if (clientPanel != null) {
                        // Оконный режим
                        switch (receiveStr) {
                            case ControlLines.STR_WRONG_PASS:
                            case ControlLines.STR_SAME_NIC:
                            case ControlLines.STR_STOP_SERVER:
                                clientPanel.setStrChat(clientPanel.getStrChat() + "\n" + receiveStr);
                                clientPanel.getTpOutput().append(commandToMsg + "\n");
                                clientPanel.getTpOutput().setCaretPosition(clientPanel.getTpOutput().getText().length());
                                if (receiveStr.equals(ControlLines.STR_STOP_SERVER)) {
                                    resender.setStop();
                                }
                                clientPanel.exit();
                                break;
                            default:
                                clientPanel.setStrChat(clientPanel.getStrChat() + "\n" + receiveStr);
                                if (!message.isfSystemMessage()) {
                                    clientPanel.getTpOutput().append(receiveStr + "\n");
                                    clientPanel.getTpOutput().setCaretPosition(clientPanel.getTpOutput().getText().length());
                                }
                                break;
                        }
                    } else {
                        // Консольный режим
                        switch (receiveStr) {
                            case ControlLines.STR_WRONG_PASS:
                            case ControlLines.STR_SAME_NIC:
                            case ControlLines.STR_STOP_SERVER:
                                System.out.println(commandToMsg);
                                if (receiveStr.equals(ControlLines.STR_STOP_SERVER)) {
                                    resender.setStop();
                                }
                                break;
                            default:
                                if (!message.isfSystemMessage()) {
                                    System.out.println(receiveStr);
                                }
                                break;
                        }
                    }
                }
            } finally {
                client.closeStreams();
            }
        }
    }
    //</editor-fold>

}

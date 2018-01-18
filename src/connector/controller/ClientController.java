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
import java.util.Properties;
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
    private Properties stringsFile;

    private Encryption serverEncryption;
    private Encryption clientEncryption;

    private ServerController serverController;
    private ClientType clientType;
    private ClientPanel clientPanel;

    public ClientController(ClientType clientType) {
        this.clientType = clientType;
        clientEncryption = new Encryption();
        serverEncryption = new Encryption();
        stringsFile = ProjectProperties.getInstance().getStringsFile();
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
            resender = new Resender();
            resender.start();
            String pfStr = client.getPsw();
            client.getOutputStream().writeObject(new Message(Encryption.encode(pfStr, pfStr),
                    Encryption.encode(client.getNicname(), pfStr), clientEncryption.getPublicKeyFromKeypair()));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean disonnection() {
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
        private boolean bFirst = true;
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
                        if (bFirst) {
                            bFirst = false;
                            serverEncryption.createPair(message.getPublicKey());
                        }
                        receiveStr = Utils.removeTheTrash(clientEncryption.decrypt(message.getMessage()));
                    } catch (IOException | ClassNotFoundException e) {
                        break;
                    }
                    switch (receiveStr) {
                        case ControlLines.STR_WRONG_PASS:
                            commandToMsg = stringsFile.getProperty("wrong_pass");
                            break;
                        case ControlLines.STR_SAME_NIC:
                            commandToMsg = stringsFile.getProperty("same_nic");
                            break;
                        case ControlLines.STR_STOP_SERVER:
                            commandToMsg = stringsFile.getProperty("stop_server");
                            break;
                        default:
                            break;
                    }

                    switch (receiveStr) {
                        case ControlLines.STR_WRONG_PASS:
                        case ControlLines.STR_SAME_NIC:
                        case ControlLines.STR_STOP_SERVER:
                            clientPanel.setStrChat(clientPanel.getStrChat() + "\n" + receiveStr);
                            clientPanel.getTpOutput().append(commandToMsg + "\n");
                            clientPanel.getTpOutput().setCaretPosition(clientPanel.getTpOutput().getText().length());
//                            clientPanel.setErrConn(true);
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
                }
            } finally {
                client.closeStreams();
            }
        }
    }
    //</editor-fold>

}

package connector.controller;

import connector.constant.ClientType;
import connector.model.Client;
import connector.model.Message;
import connector.utils.Encryption;
import connector.utils.NetUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController extends java.util.Observable {

    private static final Logger LOG = Logger.getLogger(ClientController.class.getName());

    private Client client;
    private Receiver receiver;
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

    public void sendMessage(String message) throws IOException {
        client.getOutputStream().writeObject(new Message(serverEncryption.encrypt(message)));
    }

    public boolean setConnection(String ip, String port, String nic, String psw) {

        Optional<String> optionalIp = NetUtils.getAndCheckIP(ip);
        Optional<Integer> optionalPort = NetUtils.getAndCheckPort(port);

        if (optionalIp.isPresent() && optionalPort.isPresent()) {
            if (clientType.equals(ClientType.CLIENT_WITH_SERVER)) {
                serverController = new ServerController(port, psw);
                serverController.startServer();
            }
            client = new Client(optionalIp.get(), optionalPort.get(), nic, psw);
        }

        try {
            receiver = new Receiver();
            receiver.start();
            // Client sends public key
            client.getOutputStream().writeObject(new Message(clientEncryption.getPublicKeyFromKeypair()));
            return true;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public void disconnect() {
        if (clientType.equals(ClientType.CLIENT_WITH_SERVER)) {
            serverController.stopServer();
        } else {
            receiver.setStop();
        }
    }

    public String getReceiveStr() {
        return receiveStr;
    }

    public Message getMessage() {
        return message;
    }

    public void stopReceiver() {
        receiver.setStop();
    }

    public String getNicname() {
        return client.getNicname();
    }

    private class Receiver extends Thread {

        private boolean stop = false;
        private boolean firstMsg = true;

        void setStop() {
            stop = true;
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    try {
                        message = (Message) client.getInputStream().readObject();
                        if (firstMsg) {
                            firstMsg = false;
                            // Client receives public key from server
                            serverEncryption.setPublicKey(message.getPublicKey());

                            // Client sends psw & nic
                            String pfStr = client.getPsw();
                            client.getOutputStream().writeObject(new Message(
                                    serverEncryption.encrypt(pfStr),
                                    serverEncryption.encrypt(client.getNicname())));
                            continue;
                        }

                        // Client receives message from server
                        receiveStr = clientEncryption.decrypt(message.getMessage());

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
}

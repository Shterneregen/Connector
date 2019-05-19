package connector.controller;

import connector.constant.ControlLines;
import connector.model.Message;
import connector.utils.Encryption;
import connector.utils.ProjectProperties;
import connector.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionController extends Thread {

    private static final Logger LOG = Logger.getLogger(ConnectionController.class.getName());

    private static List<ConnectionController> connections = Collections.synchronizedList(new ArrayList<>());
    private static List<String> listNames = new ArrayList<>();

    private Socket socket;
    private Boolean wrongNic = false;
    private boolean stop = false;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Encryption clientEncryption;
    private Encryption serverEncryption;
    private String name;
    private String psw;
    private StringBuilder buffChat;
    private boolean firstMsg = true;

    ConnectionController(Socket soc, Encryption serverEncryption, String psw) {
        this.socket = soc;
        this.serverEncryption = serverEncryption;
        this.psw = psw;
        clientEncryption = new Encryption();
        try {
            inputStream = new ObjectInputStream(this.socket.getInputStream());
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        buffChat = new StringBuilder();
    }

    private void setStop() {
        stop = true;
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                Message message = (Message) inputStream.readObject();

                if (firstMsg) {
                    // Server receive client public key
                    clientEncryption.setPublicKey(message.getPublicKey());

                    // Server send its public key to client
                    ConnectionController.this.outputStream
                            .writeObject(new Message(serverEncryption.getPublicKeyFromKeypair()));
                    firstMsg = false;
                    continue;
                }

                // Server receives psw & nic from client
                name = Utils.removeTheTrash(serverEncryption.decrypt(message.getName()));
                String pswFromClient = Utils.removeTheTrash(serverEncryption.decrypt(message.getPsw()));

                if (pswFromClient.equals(psw)) {
                    // Check nickname uniqueness
                    wrongNic = Utils.isNotUniqueNicname(name, listNames);
                    stop = Utils.isNotUniqueNicname(name, listNames);

                    if (!wrongNic) {
                        synchronized (listNames) {
                            listNames.add(name);
                        }
                        // Notify everyone about new user
                        sendBroadcastMessage(name + " " + ProjectProperties.getString("server.msg.join"));

                        // Get next message from client
                        while (!stop) {
                            message = (Message) inputStream.readObject();
                            String msgFromClient = Utils.removeTheTrash(serverEncryption.decrypt(message.getMessage()));

                            switch (msgFromClient) {
                                // Notify everyone that the current user is out
                                case ControlLines.STR_EXIT:
                                    connections.remove(ConnectionController.this);
                                    sendBroadcastMessage(name + " " + ProjectProperties.getString("server.msg.left"));
                                    setStop();
                                    break;
                                // Sending all session messages
                                case ControlLines.STR_GET_ALL_MSG:
                                    String msg = "----- "
                                            + ProjectProperties.getString("server.msg.allMsg")
                                            + " -----"
                                            + new String(buffChat)
                                            + "\n----------------------\n";
                                    this.sendMessage(msg);
                                    break;
                                // Sending message to all users
                                default:
                                    sendBroadcastMessage(name + ": " + msgFromClient);
                                    break;
                            }
                        }
                    } else {
                        this.sendMessage(ControlLines.STR_SAME_NIC);
                    }
                } else {
                    // To make bruteforce harder
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                    this.sendMessage(ControlLines.STR_WRONG_PASS);
                    this.setStop();
                }
            }
        } catch (java.net.SocketException se) {
            System.out.println("Connection SocketException");
        } catch (IOException | ClassNotFoundException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            close();
        }
    }

    /**
     * Sends a message to all users
     */
    private void sendBroadcastMessage(String broadcastMsg) {
        synchronized (connections) {
            for (ConnectionController connection : connections) {
                String msg = "[" + Utils.getCurrentTime() + "] " + broadcastMsg;
                connection.sendMessage(msg);
                buffChat.append(msg).append("\n");
            }
        }
    }

    /**
     * Sends a message to a particular user
     */
    private void sendMessage(String message) {
        try {
            outputStream.writeObject(new Message(clientEncryption.encrypt(message), false));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Closes the input and output streams and socket
     */
    private void close() {
        Utils.close(inputStream);
        Utils.close(outputStream);
        Utils.close(socket);

        synchronized (connections) {
            connections.remove(ConnectionController.this);
        }
        if (!wrongNic && listNames != null && !listNames.isEmpty()) {
            synchronized (listNames) {
                listNames.remove(name);
            }
        }
    }

    /**
     * Notify all clients that the server is stopped
     */
    static void stopServerNotification() {
        if (connections != null && !connections.isEmpty()) {
            synchronized (connections) {
                connections.forEach(connection -> connection.sendMessage(ControlLines.STR_STOP_SERVER));
            }
        }
    }

    static List<ConnectionController> getConnections() {
        return connections;
    }

    public int getUserCount() {
        return connections != null ? connections.size() : 0;
    }
}

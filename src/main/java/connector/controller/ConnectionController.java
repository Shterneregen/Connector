/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.controller;

import connector.model.Message;
import connector.constant.ControlLines;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yura
 */
public class ConnectionController extends Thread {

    private static List<ConnectionController> connections = Collections.synchronizedList(new ArrayList<ConnectionController>());
    private static List<String> listNames = new ArrayList<>();

    private Socket socket;
    private Boolean flagWrongNic = false;
    private boolean stoped = false;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Encryption clientEncryption;
    private Encryption serverEncryption;
    private String name;
    private Properties stringsFile;
    private String psw;
    private StringBuilder buffChat;
    private boolean firstMsg = true;

    public ConnectionController(Socket soc, Encryption serverEncryption, String psw) {
        this.socket = soc;
        this.serverEncryption = serverEncryption;
        this.psw = psw;
        clientEncryption = new Encryption();
        try {
            inputStream = new ObjectInputStream(this.socket.getInputStream());
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, e);
        }
        stringsFile = ProjectProperties.getInstance().getLangFile();
        buffChat = new StringBuilder("");
    }

    public void setStop() {
        stoped = true;
    }

    @Override
    public void run() {
        try {
            while (!stoped) {
                Message message = (Message) inputStream.readObject();

                if (firstMsg) {
                    // Server receive client public key
                    clientEncryption.createPair(message.getPublicKey());

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
                    flagWrongNic = stoped = Utils.checkNicname(name, listNames);

                    if (!flagWrongNic) {
                        synchronized (listNames) {
                            listNames.add(name);
                        }
                        // Notify everyone about new user
                        sendBroadcastMsg(name + " " + stringsFile.getProperty("server.msg.join"));

                        // Get next message from client
                        while (!stoped) {
                            message = (Message) inputStream.readObject();
                            String msgFromClient = Utils.removeTheTrash(serverEncryption.decrypt(message.getMessage()));

                            switch (msgFromClient) {
                                // Notify everyone that the current user is out
                                case ControlLines.STR_EXIT:
                                    connections.remove(ConnectionController.this);
                                    sendBroadcastMsg(name + " " + stringsFile.getProperty("server.msg.left"));
                                    setStop();
                                    break;
                                // Sending all session messages
                                case ControlLines.STR_GET_ALL_MSG:
                                    String msg = "----- "
                                            + stringsFile.getProperty("server.msg.allMsg")
                                            + " -----"
                                            + new String(buffChat)
                                            + "\n----------------------\n";
                                    this.sendMsg(msg);
                                    break;
                                // Sending message to all users
                                default:
                                    sendBroadcastMsg(name + ": " + msgFromClient);
                                    break;
                            }
                        }
                    } else {
                        this.sendMsg(ControlLines.STR_SAME_NIC);
                    }
                } else {
                    // To make bruteforce harder
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.sendMsg(ControlLines.STR_WRONG_PASS);
                    this.setStop();
                }
            }
        } catch (java.net.SocketException se) {
            System.out.println("Connection SocketException");
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close();
        }
    }

    /**
     * Sends a message to all users
     */
    private void sendBroadcastMsg(String broadcastMsg) {
        synchronized (connections) {
            for (ConnectionController connection : connections) {
                String msg = "[" + Utils.getTime(false) + "] " + broadcastMsg;
                connection.sendMsg(msg);
                buffChat.append(msg).append("\n");
            }
        }
    }

    /**
     * Sends a message to a particular user
     */
    private void sendMsg(String msg) {
        try {
            outputStream.writeObject(new Message(clientEncryption.encrypt(msg), false));
        } catch (IOException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
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
        if (!flagWrongNic && listNames != null && !listNames.isEmpty()) {
            synchronized (listNames) {
                listNames.remove(name);
            }
        }
    }

    /**
     * Notify all clients that the server is stopped
     */
    public static void stopServerNotification() {
        if (connections != null && !connections.isEmpty()) {
            synchronized (connections) {
                connections.stream().forEach((connection) -> {
                    connection.sendMsg(ControlLines.STR_STOP_SERVER);
                });
            }
        }
    }

    public static List<ConnectionController> getConnections() {
        return connections;
    }

    public int getUserCount() {
        return connections != null ? connections.size() : 0;
    }
}

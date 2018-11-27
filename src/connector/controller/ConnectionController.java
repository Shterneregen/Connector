/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.controller;

import connector.model.Message;
import connector.resources.ControlLines;
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
    private int userNumber;
    private StringBuilder buffChat;
    private boolean firstMsg = true;

    public ConnectionController(Socket soc, Encryption serverEncryption, String psw) {
        this.socket = soc;
        this.serverEncryption = serverEncryption;
        this.psw = psw;
        userNumber = 0;
        clientEncryption = new Encryption();
        try {
            inputStream = new ObjectInputStream(this.socket.getInputStream());
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, e);
        }
        stringsFile = ProjectProperties.getInstance().getStringsFile();
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
                    // Server receive pub key from client
                    clientEncryption.createPair(message.getPublicKey());

                    // Server send pub key to client
                    ConnectionController.this.outputStream
                            .writeObject(new Message(serverEncryption.getPublicKeyFromKeypair()));
                    firstMsg = false;
                    continue;
                }

                // Server receive psw & nic from client
                name = Utils.removeTheTrash(serverEncryption.decrypt(message.getName()));
                String pswFromClient = Utils.removeTheTrash(serverEncryption.decrypt(message.getPsw()));

                if (pswFromClient.equals(psw)) {
                    // Проверяет, есть ли такой же ник в чате
                    flagWrongNic = stoped = Utils.checkNicname(name, listNames);

                    if (!flagWrongNic) {
                        userNumber++;

                        synchronized (listNames) {
                            listNames.add(name);
                        }
                        // Оповещаем всех, что вошел новый участник
                        sendBroadcastMsg(name + " " + stringsFile.getProperty("server.msg.join"));

                        // В цикле получаем очередное сообщение от данного клиента и рассылаем остальным
                        while (!stoped) {
                            message = (Message) inputStream.readObject();
                            String msgFromClient = Utils.removeTheTrash(serverEncryption.decrypt(message.getMessage()));

                            switch (msgFromClient) {
                                // Оповещаем всех, что данный клиент вышел
                                case ControlLines.STR_EXIT:
                                    connections.remove(ConnectionController.this);
                                    sendBroadcastMsg(name + " " + stringsFile.getProperty("server.msg.left"));
                                    userNumber--;
                                    setStop();
                                    break;
                                // Отправляем все сообщения сессии
                                case ControlLines.STR_GET_ALL_MSG:
                                    String msg = "----- "
                                            + stringsFile.getProperty("server.msg.allMsg")
                                            + " -----"
                                            + new String(buffChat)
                                            + "\n----------------------\n";
//                                    sendMsg(ConnectionController.this, msg);
                                    this.sendMsg(msg);
                                    break;
                                // Отправляем всем клиентам очередное сообщение
                                default:
                                    sendBroadcastMsg(name + ": " + msgFromClient);
                                    break;
                            }
                        }
                    } else {
                        this.sendMsg(ControlLines.STR_SAME_NIC);
                    }
                } else {
                    // Чтобы сложнее было воспользоваться брут форсом - ставлю задержку ответа 
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    sendMsg(ConnectionController.this, ControlLines.STR_WRONG_PASS);
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
     * Sends a message to all participants
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
     * Sends a message to a particular participant
     */
    private void sendMsg(String msg) {
        try {
            this.getOutputStream().writeObject(new Message(this.getClientEncryption().encrypt(msg), false));
        } catch (IOException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Закрывает входной и выходной потоки и сокет
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
                for (ConnectionController connection : connections) {
                    connection.sendMsg(ControlLines.STR_STOP_SERVER);
                }
            }
        }
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public Encryption getClientEncryption() {
        return clientEncryption;
    }

    public static List<ConnectionController> getConnections() {
        return connections;
    }
}

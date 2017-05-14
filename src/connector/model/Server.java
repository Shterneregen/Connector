/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.model;

import connector.utils.Encryption;
import connector.resources.ControlLines;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yura
 */
public class Server {
    private int port;
    private int userNumber;
    private ServerThread serverThread;
    private StringBuilder buffChat;
    private String pfStr;
    private Boolean isStartServer = false;
    private Encryption serverEncryption;
    private int conf;
    private ArrayList<String> listNames;
    private Properties stringsFile;

    private List<Connection> connections; 

    public Server() {
        stringsFile = ProjectProperties.getInstance().getStringsFile();
        connections = Collections.synchronizedList(new ArrayList<Connection>());    
        buffChat = new StringBuilder("");
        listNames = new ArrayList<String>();
        serverEncryption = new Encryption();
        serverEncryption.doThis();
        port = 9988;
        userNumber = 0;
        pfStr = "9988";        
    }
    
    public void createServerThread(){
        serverThread = new ServerThread();
    }
    
    public void startServer(){
        serverThread.start();
    }
    
    public void setStopServerThread(){
        serverThread.setStop();
    }
    
    public void closeAllServerConnection(){
        closeAll();
    }
    
    private void closeAll() {
        try {
            if (connections.size() != 0) {
                synchronized (connections) {
                    Iterator<Connection> iter = connections.iterator();
                    while (iter.hasNext()) {Connection thisConnection = iter.next();
                        thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt(ControlLines.STR_EXIT_ALL), true));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты! (closeAll)");
            e.printStackTrace();
        }
    }

    private class Connection extends Thread {
        private Socket socket;
        private Boolean flagWrongNic = false;
        private boolean stoped = false;
        private boolean closed = false;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private Message message;
        private Encryption clientEncryption;
        private String name = "";

        public Connection(Socket soc) {
            this.socket = soc;
            clientEncryption = new Encryption();

            try {
                inputStream = new ObjectInputStream(this.socket.getInputStream());
//                System.out.println("ServerFrame: inputStream");
                outputStream = new ObjectOutputStream(this.socket.getOutputStream());
//                System.out.println("ServerFrame: outputStream");
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            try {
                while (!stoped) {
                    message = (Message) inputStream.readObject();
                    String pass = Encryption.decode(message.getPass(), pfStr);
//                    System.out.println("ServerFrame got pass: " + pass);

                    name = Encryption.decode(message.getName(), pfStr);
//                    System.out.println("ServerFrame got name: " + name);//

                    clientEncryption.createPair(message.getPublicKey());
                    Connection.this.outputStream.writeObject(new Message(clientEncryption.encrypt(ControlLines.STR_SEND_PUB_KEY), true, serverEncryption.getPublicKeyFromKeypair()));
//                    System.out.println("ServerFrame got key: "/* + message.getPublicKey()*/);

                    if (stoped) {
                        break;
                    }
                    if (pass.equals(pfStr)) {
                        if (stoped) {
                            break;
                        }
                        for (int i = 0; i < listNames.size(); i++) {
                            if (name.equals(listNames.get(i))) {                                              
                                Connection.this.outputStream.writeObject(new Message(clientEncryption.encrypt(ControlLines.STR_SAME_NIC), false));
                                flagWrongNic = true;
                                stoped = true;
                                break;
                            } else {
                                flagWrongNic = false;
                            }
                        }

                        if (!flagWrongNic) {
                            userNumber++;
//                            lbNumUs.setText(" Пользователей: " + userNumber);

                            synchronized (listNames) {
                                listNames.add(name);
                            }
                            synchronized (connections) {
                                Iterator<Connection> iter = connections.iterator();
                                while (iter.hasNext()) {
                                    Connection thisConnection = iter.next();
                                    thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt("[" + getTime(false) + "] " + name + " " + stringsFile.getProperty("server.msg.join")), false));
                                    buffChat.append("[" + getTime(false) + "] " + name + " " + stringsFile.getProperty("server.msg.join") + "\n");
                                }
                            }
                            String str = "";
                            while (!stoped) {
                                try {
                                    message = (Message) inputStream.readObject();
                                    str = Utils.removeTheTrash(serverEncryption.decrypt(message.getMessage()));
//                                    System.out.println("ServerFrame enc message: " + message.getMessage());
//                                    System.out.println("ServerFrame dec message: " + str);
                                } catch (Exception e) {
                                    setStop();
                                    break;
                                }
                                if (stoped) {
                                    break;
                                }
                                if (str.equals(ControlLines.STR_EXIT)) {
                                    synchronized (connections) {
                                        Iterator<Connection> iter = connections.iterator();
                                        while (iter.hasNext()) {
                                            Connection thisConnection = iter.next();                                           
                                            thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt("[" + getTime(false) + "] " + name + " " + stringsFile.getProperty("server.msg.left")), false));                                      
//                                            thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt("[" + getTime(false) + "] " + name + " вышел из чата" + "\n"), false));                                      
                                        }
                                        buffChat.append("[" + getTime(false) + "] " + name + " " + stringsFile.getProperty("server.msg.left") + "\n");
                                    }
                                    userNumber--;
//                                    lbNumUs.setText(" Пользователей: " + userNumber);
                                    stoped = true;
                                    break;
                                }
                                if (str.equals(ControlLines.STR_EXIT_ALL)) {
                                    setStop();
                                    break;
                                }
                                if (str.equals(ControlLines.STR_GET_ALL_MSG)) {
                                    Connection.this.outputStream.writeObject(
                                            new Message(clientEncryption.encrypt("----- " + stringsFile.getProperty("server.msg.allMsg") + " -----" + new String(buffChat)
                                                    + "\n----------------------\n"), false));
                                    //break;
                                }

                                // Отправляем всем клиентам очередное сообщение
                                if (!str.equals(ControlLines.STR_GET_ALL_MSG)) {
                                    synchronized (connections) {
                                        Iterator<Connection> iter = connections.iterator();

                                        while (iter.hasNext()) {
                                            Connection thisConnection = iter.next();
                                            thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt("[" + getTime(false) + "] " + name + ": " + str), false));                                         
                                        }
                                        buffChat.append("[" + getTime(false) + "] " + name + ": " + str + "\n");
                                    }
                                }
                            }
                        }
                    } else {
//                        Connection.this.out.println(Encryption.encode(Utils.getSTR_WRONG_PASS(), pfStr)); 
//                        Connection.this.out.println(Encryption.encode("--- Сервер не отвечает --- 3"+"\n", pfStr));
                        this.setStop();
                        //close();
                        //Connection.this.out.println(Utils.getSTR_WRONG_PASS());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //closeAll();
            } finally {
                close();
            }
        }
        // Возвращает дату (ch == 1) или время (ch == 0)
        private String getTime(boolean ch) {
            //Date calendar = Calendar.getInstance().getTime();
            long curTime = System.currentTimeMillis();
            // Хотите значение типа Date, с этим временем?
            //Date curDate = new Date(curTime);
            // Хотите строку в формате, удобном Вам?
            String curStringDate = ch ? new SimpleDateFormat("dd.MM.yyyy").format(curTime) : new SimpleDateFormat("kk:mm:ss").format(curTime);
            return curStringDate;
        }

        /**
         * Закрывает входной и выходной потоки и сокет
         */
        public void close() {
            if (!closed) {
                closed = true;
                try {
                    this.outputStream.close();
                    this.inputStream.close();
                    this.outputStream.flush();                    
                    this.socket.close();

                    synchronized (connections) {
                        connections.remove(Connection.this);
                    }
                    if (!flagWrongNic) {
                        synchronized (listNames) {
                            listNames.remove(name);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Потоки не были закрыты! (close)");
                }
            }
        }
    } 
    
    private class ServerThread extends Thread {

        private boolean stoped = false;
        private ServerSocket serverSocket = null;
        private Socket socket;

        //Прекращает пересылку сообщений
        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);

                while (!stoped) {
                    socket = serverSocket.accept();
                    if (stoped) {
                        break;
                    }
                    Connection con = new Connection(socket);
                    connections.add(con);

                    con.start();
                }
            } catch (IOException e) {
                //closeAll();
//                try {
//                    if (serverSocket != null) {
//                        serverSocket.close();
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
            } finally {
                //closeAll();
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(connector.view.ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }    
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public ServerThread getServerThread() {
        return serverThread;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public StringBuilder getBuffChat() {
        return buffChat;
    }

    public void setBuffChat(StringBuilder buffChat) {
        this.buffChat = buffChat;
    }

    public String getPfStr() {
        return pfStr;
    }

    public void setPfStr(String pfStr) {
        this.pfStr = pfStr;
    }

    public Boolean getIsStartServer() {
        return isStartServer;
    }

    public void setIsStartServer(Boolean isStartServer) {
        this.isStartServer = isStartServer;
    }

    public Encryption getServerEncryption() {
        return serverEncryption;
    }

    public void setServerEncryption(Encryption serverEncryption) {
        this.serverEncryption = serverEncryption;
    }

    public int getConf() {
        return conf;
    }

    public void setConf(int conf) {
        this.conf = conf;
    }

    public ArrayList<String> getListNames() {
        return listNames;
    }

    public void setListNames(ArrayList<String> listNames) {
        this.listNames = listNames;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }    
}

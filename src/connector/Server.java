package connector;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.net.*;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.JFrame;
//import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.text.AbstractDocument;

public class Server extends javax.swing.JFrame {

    private static final int SERVER = 1;
    private static int port;
    private static int numUs;
    private static ServerThread serverThread;
    private static StringBuilder buffChat = new StringBuilder("");
    private String pfStr;
    private Boolean isStartServer = false;
    private Encryption serverEncryption;
//    private String strPort;
    //private Socket socket;

    ArrayList<String> listNames;

    private List<Connection> connections
            = Collections.synchronizedList(new ArrayList<Connection>());
    //private ServerSocket serverSocket;
    int conf;
    int fromClient;
    int alone;

    public Server(String s, int conf) {
        super(s);

        listNames = new ArrayList<String>();
        serverEncryption = new Encryption();
        serverEncryption.doThis();
        this.conf = conf;
        port = 9988;
        numUs = 0;
        pfStr = "9988";
//        strPort = "9988";
        fromClient = 1;
        alone = 0;

        initComponents();
        ((AbstractDocument) tfPort.getDocument()).setDocumentFilter(new Utils().new DocumentFilterForPort());
        btStopServer.setEnabled(false);

        lb.setText(" Ваш локальный IP ");

//        tfIP.setText(getMyLocalIP());
        lbNumUs.setText(" Пользователей: " + numUs);

        setResizable(false);
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {

            }

            public void windowClosed(WindowEvent event) {

            }

            public void windowClosing(WindowEvent event) {
//                stopServer();
//                try {
//                    serverThread.setStop();
//                } catch (Exception e) {
//
//                    //e.printStackTrace();
//                }
                if (isStartServer && conf == alone) {
                    stopServer();
                }
                setVisible(false);
                dispose();
            }

            public void windowDeactivated(WindowEvent event) {

            }

            public void windowDeiconified(WindowEvent event) {

            }

            public void windowIconified(WindowEvent event) {
                new Tray().setTrayIcon(Server.this, null, SERVER);
                setVisible(false);
            }

            public void windowOpened(WindowEvent event) {

            }

        });
    }

//    public String getMyLocalIP(){
//        InetAddress addr = null;
//        try {
//            addr = InetAddress.getLocalHost();
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        String myLANIP = addr.getHostAddress();
//        return myLANIP;
//    }
    private static String getInterfaceInfo(NetworkInterface nif) throws IOException {
//         final String NL = System.getProperty("line.separator");
        String ipAddress = "";

        Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();

        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddr = inetAddresses.nextElement();

            if (inetAddr instanceof Inet4Address) {
                ipAddress = inetAddr.getHostAddress();
            }
        }
        return ipAddress;
    }

    public ArrayList<String> getMyLocalIP() {
        ArrayList<String> listAddr = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces
                    = NetworkInterface.getNetworkInterfaces();
//            String ip = "";

            while (interfaces.hasMoreElements()) {
                NetworkInterface nif = interfaces.nextElement();

                if (!getInterfaceInfo(nif).equals("")) {
                    listAddr.add(getInterfaceInfo(nif));
                }
//            System.out.println(getInterfaceInfo(nif));
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listAddr;
    }

    public void setPort(String strPort) {
        if (Integer.parseInt(strPort) <= 0 || Integer.parseInt(strPort) > 65535) {
            lbNumUs.setText(Strings.getSTR_WRONG_PORT());
        } else {
            this.port = Integer.parseInt(strPort);
            lbNumUs.setText(Strings.getSTR_SET_PORT() + port);
        }
    }

    public void setPas(String pas) {
        this.pfStr = pas;
        lbNumUs.setText(Strings.getSTR_SET_PASS());
    }

    public static StringBuilder getBuffChat() {
        return buffChat;
    }

    void closeAll() {
        try {
            if (connections.size() != 0) {
                synchronized (connections) {
                    Iterator<Connection> iter = connections.iterator();
                    while (iter.hasNext()) {
//                        ((Connection) iter.next()).out.println(Encryption.encode(Utils.getSTR_STOP_SERVER(), pfStr));                              
//                        ((Connection) iter.next()).setStop();
//                        ((Connection) iter.next()).close();
//                        (Connection) iter.next().serverSocket.close();
//                        ((Connection) iter.next()).out.println(Encryption.encode(Strings.getSTR_EXIT_ALL(), pfStr));
                        Connection thisConnection = iter.next();
                        thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt(Strings.getSTR_EXIT_ALL()), true));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты! (closeAll)");
            e.printStackTrace();
        }
    }

    private class Connection extends Thread {
//        private BufferedReader in;
//        private PrintWriter out;
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
//                in = new BufferedReader(new InputStreamReader(
//                        socket.getInputStream()));
//                out = new PrintWriter(socket.getOutputStream(), true);

                inputStream = new ObjectInputStream(this.socket.getInputStream());
//                System.out.println("Server: inputStream");
                outputStream = new ObjectOutputStream(this.socket.getOutputStream());
//                System.out.println("Server: outputStream");

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
//                    System.out.println("Server got pass: " + pass);

                    name = Encryption.decode(message.getName(), pfStr);
//                    System.out.println("Server got name: " + name);//

                    clientEncryption.createPair(message.getPublicKey());
                    Connection.this.outputStream.writeObject(new Message(clientEncryption.encrypt(Strings.getSTR_SEND_PUB_KEY()), true, serverEncryption.getPublicKeyFromKeypair()));
//                    System.out.println("Server got key: "/* + message.getPublicKey()*/);

                    if (stoped) {
                        break;
                    }
                    if (pass.equals(pfStr)) {
                        if (stoped) {
                            break;
                        }
                        for (int i = 0; i < listNames.size(); i++) {
                            if (name.equals(listNames.get(i))) {                                              
                                Connection.this.outputStream.writeObject(new Message(clientEncryption.encrypt(Strings.getSTR_SAME_NIC()), false));
//                                System.out.println("Server: " + Strings.getSTR_SAME_NIC());
                                flagWrongNic = true;
                                stoped = true;
                                break;
                            } else {
                                flagWrongNic = false;
                            }
                        }

                        if (!flagWrongNic) {
                            numUs++;
                            lbNumUs.setText(" Пользователей: " + numUs);

                            synchronized (listNames) {
                                listNames.add(name);
                            }
                            synchronized (connections) {
                                Iterator<Connection> iter = connections.iterator();
                                while (iter.hasNext()) {
                                    Connection thisConnection = iter.next();
                                    thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt("[" + getTime(false) + "] " + name + " присоединился к чату"), false));
                                    buffChat.append("[" + getTime(false) + "] " + name + " присоединился к чату" + "\n");
                                }
                            }
                            String str = "";
                            while (!stoped) {
                                try {
                                    message = (Message) inputStream.readObject();
                                    str = Utils.removeTheTrash(serverEncryption.decrypt(message.getMessage()));
//                                    System.out.println("Server enc message: " + message.getMessage());
//                                    System.out.println("Server dec message: " + str);
                                } catch (Exception e) {
                                    setStop();
                                    break;
                                }
                                if (stoped) {
                                    break;
                                }
                                if (str.equals(Strings.getSTR_EXIT())) {
                                    synchronized (connections) {
                                        Iterator<Connection> iter = connections.iterator();
                                        while (iter.hasNext()) {
                                            Connection thisConnection = iter.next();
//                                            if(connections.equals(thisConnection))                                           
                                            thisConnection.outputStream.writeObject(new Message(thisConnection.clientEncryption.encrypt("[" + getTime(false) + "] " + name + " вышел из чата" + "\n"), false));                                      
                                        }
                                        buffChat.append("[" + getTime(false) + "] " + name + " вышел из чата" + "\n");
                                    }
                                    numUs--;
                                    lbNumUs.setText(" Пользователей: " + numUs);
                                    stoped = true;
                                    break;
                                }
                                if (str.equals(Strings.getSTR_EXIT_ALL())) {
                                    setStop();
                                    break;
                                }
                                if (str.equals(Strings.getSTR_GET_ALL_MSG())) {
                                    Connection.this.outputStream.writeObject(
                                            new Message(clientEncryption.encrypt("----- Все сообщения -----" + new String(buffChat)
                                                    + "\n----------------------\n"), false));
                                    //break;
                                }

                                // Отправляем всем клиентам очередное сообщение
                                if (!str.equals(Strings.getSTR_GET_ALL_MSG())) {
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
//                    this.in.close();
//                    this.out.close();
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

    public void startServer() {
        isStartServer = true;
        serverThread = new ServerThread();
        serverThread.start();

        btStartServer.setEnabled(false);
        btStopServer.setEnabled(true);
        btSetPort.setEnabled(false);
        btSetPas.setEnabled(false);

        tfPort.setEditable(false);
        pfPas.setEditable(false);
    }

    public void stopServer() {
        isStartServer = false;
        serverThread.setStop();
        closeAll();

        btStartServer.setEnabled(true);
        btStopServer.setEnabled(false);
        btSetPort.setEnabled(true);
        btSetPas.setEnabled(true);

        tfPort.setEditable(true);
        pfPas.setEditable(true);

        numUs = 0;
        lbNumUs.setText(" Пользователей: " + numUs);
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
//                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfPort = new javax.swing.JTextField();
        btSetPort = new javax.swing.JButton();
        btStartServer = new javax.swing.JButton();
        btStopServer = new javax.swing.JButton();
        lb = new javax.swing.JLabel();
        tfIP = new javax.swing.JTextField();
        lbNumUs = new javax.swing.JLabel();
        pfPas = new javax.swing.JPasswordField();
        btSetPas = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tfPort.setText("9988");
        tfPort.setToolTipText("");
        tfPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPortKeyPressed(evt);
            }
        });

        btSetPort.setText("Установить порт");
        btSetPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSetPortActionPerformed(evt);
            }
        });

        btStartServer.setText("Запустить сервер");
        btStartServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStartServerActionPerformed(evt);
            }
        });

        btStopServer.setText("Остановить сервер");
        btStopServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStopServerActionPerformed(evt);
            }
        });

        lb.setText(" ");

        tfIP.setEditable(false);

        lbNumUs.setText(" ");

        pfPas.setText("9988");
        pfPas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pfPasKeyPressed(evt);
            }
        });

        btSetPas.setText("Установить пароль");
        btSetPas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSetPasActionPerformed(evt);
            }
        });

        jMenu1.setText("Сервер");

        jMenuItem2.setText("Новое серверное окно");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Клиент");

        jMenuItem1.setText("Новое клиентское окно");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lbNumUs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(pfPas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btSetPas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btSetPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(btStartServer)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btStopServer)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfIP, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPort)
                    .addComponent(btSetPort))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pfPas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSetPas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btStartServer)
                    .addComponent(btStopServer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbNumUs)
                .addContainerGap())
        );

        tfPort.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSetPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSetPortActionPerformed
        setPort(tfPort.getText());
    }//GEN-LAST:event_btSetPortActionPerformed

    private void btStartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStartServerActionPerformed
        startServer();
    }//GEN-LAST:event_btStartServerActionPerformed

    private void btStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStopServerActionPerformed
        stopServer();
    }//GEN-LAST:event_btStopServerActionPerformed

    private void btSetPasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSetPasActionPerformed
        //setPas(btSetPas.getText());
        setPas(String.valueOf(pfPas.getPassword()));
    }//GEN-LAST:event_btSetPasActionPerformed

    private void tfPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPortKeyPressed
        if (evt.getKeyCode() == 10) {
            setPort(tfPort.getText());
        }
    }//GEN-LAST:event_tfPortKeyPressed

    private void pfPasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pfPasKeyPressed
        if (evt.getKeyCode() == 10) {
            setPas(btSetPas.getText());
        }
    }//GEN-LAST:event_pfPasKeyPressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ClientFrame client = new ClientFrame(Strings.getMAIN_NAME());
        client.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Server server = new Server(Strings.getMAIN_NAME(), 0);
        server.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSetPas;
    private javax.swing.JButton btSetPort;
    private javax.swing.JButton btStartServer;
    private javax.swing.JButton btStopServer;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JLabel lb;
    private javax.swing.JLabel lbNumUs;
    private javax.swing.JPasswordField pfPas;
    private javax.swing.JTextField tfIP;
    private javax.swing.JTextField tfPort;
    // End of variables declaration//GEN-END:variables

}

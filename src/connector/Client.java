package connector;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.text.AbstractDocument;
//import connector.Utils.StatusBar;
//import connector.tab.ButtonTabComponent;
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.URL;
//import javax.imageio.ImageIO;
//import javax.swing.BorderFactory;
//import javax.swing.Icon;
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTabbedPane;
//import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
//import javax.swing.plaf.basic.BasicButtonUI;

public class Client extends javax.swing.JPanel { 
    private Socket socket;
    private int port;
    private String ip;
    private String nicname;  
    private String strChat;
    private String receiveStr;
    private String pfStr;
    private String strBtSetIP;
    private String strBtSetPort;
    private String strBtSetNic;
    private String strBtSetPas;
    private int conf;
    private int serverMark;
    private int clientMark;
    private Server server;
    private Resender resender;
    private Boolean flagGoodConn;   
    private boolean errConn;
    private boolean bRsa;
    private Encryption serverEncryption;
    private Encryption clientEncryption;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;      
    private ArrayList<String> listAddr = new ArrayList<String>();
//    private final JTabbedPane pane;
//    private static final int CLIENT = 0;
//    private BufferedReader in;
//    private PrintWriter out;
//    private String message;
//    private String strBtStartClient;
//    private String strBtStopClient;    
//    StatusBar statusBar;     
//    private String strPort;
//    private String strNic;
//    private String strForTfInput = "Подключитесь к серверу";
//    private boolean bMinSetCon = false;
//    private boolean bMinSetCon = false;
//    private int wPSetCon;
//    private int hPSetCon;
//    private int wFrame;
//    private int hFrame;
//    private static final String CLIENT_BACKGROUND = "images/dark.jpg";
//    public Client(final JTabbedPane pane) {
    public Client(int conf) {
        this.conf = conf;
        port = 9988;
        nicname = "NicName";
        pfStr = "9988";

        strBtSetIP = "Установить IP";
        strBtSetPort = "Установить порт";
        strBtSetNic = "Установить Ник";
        strBtSetPas = "Установить пароль";
        server = new Server(Strings.getMAIN_NAME(),1);
        flagGoodConn = false;
        errConn = false;
        listAddr = server.getMyLocalIP();
        serverMark = 1;
        clientMark = 0;
        strChat = "Нет сообщений";
        bRsa = false;
        clientEncryption = new Encryption();
        clientEncryption.doThis();
        serverEncryption = new Encryption();

        initComponents();
        btSettings.setToolTipText("Настройки соединения");
        for (int i = 0; i < listAddr.size(); i++) {
            tfIP.addItem(listAddr.get(i));
        }
        if (conf == serverMark) {
            btStartClient.setText("Создать диалог");
            btStopClient.setText("Остановить диалог");
            tfIP.setEditable(false);
            
        } else if(conf == clientMark){
            btStartClient.setText("Подключиться");
            btStopClient.setText("Выйти");
        }
        ((AbstractDocument) tfPort.getDocument()).setDocumentFilter(new Utils().new DocumentFilterForPort());

        tpOutput.setWrapStyleWord(true);// слова не будут разрываться в том месте, где они «натыкаются» на границу компонента, а будут целиком перенесены на новую строку
        tpOutput.setLineWrap(true);     // длинные строки будут укладываться в несколько строк вместо одной, уходящей за границы компонента
        tpOutput.setEditable(false);
        tfInput.setText(Strings.getSTR_TO_CONN());
        tfInput.setEditable(false);
        btStopClient.setEnabled(false);
        btSent.setEnabled(false);
        btSettings.setEnabled(false);       

        cbNewConv.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cbNewConv.isSelected()) {                   
                    Client.this.conf = serverMark;
                    btStartClient.setText("Создать диалог");
                    btStopClient.setText("Остановить диалог");
                    tfIP.setEditable(false);
                    btSettings.setEnabled(true);
                    tfIP.removeAllItems();
                    for (int i = 0; i < listAddr.size(); i++) {
                        tfIP.addItem(listAddr.get(i));
                    }
                } else {                   
                    Client.this.conf = clientMark;
                    btStartClient.setText("Присоединиться");
                    btStopClient.setText("Выйти");
                    tfIP.setEditable(true);
                    btSettings.setEnabled(false);
                    tfIP.removeAllItems();
                    for (int i = 0; i < listAddr.size(); i++) {
                        tfIP.addItem(listAddr.get(i));
                    }
                }
            }
        });
//        JButton button = new Client.TabButton();
//        JButton button = new TabButton();
//        add(button);
//        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
//        ((AbstractDocument) tfIP.getDocument()).setDocumentFilter(new Utils().new DocumentFilterForIP());
        
//        btSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connector/images/setting.png")));
//        try {
//            btSettings.setIcon((Icon) ImageIO.read(Client.class.getResourceAsStream("images/icon.png")));
//        } catch (IOException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }       
//        icon = ImageIO.read(ClientFrame.class.getResourceAsStream("images/icon.png"));
//        btStartClient.setText(ip);
//        strIP = "127.0.0.1";
//        strPort = "9988";
//        strNic = "NicName";       
//        setContentPane(new BgPanel());
//        statusBar = new Utils().new StatusBar();
//        add(statusBar, java.awt.BorderLayout.SOUTH);
        
//        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        if (pane == null) {
//            throw new NullPointerException("TabbedPane is null");
//        }
//        this.pane = pane;
//        setOpaque(false);
//        make JLabel read titles from JTabbedPane
//        JLabel label = new JLabel() {
//            public String getText() {
//                int i = pane.indexOfTabComponent(Client.this);
//                if (i != -1) {
//                    return pane.getTitleAt(i);
//                }
//                return null;
//            }
//        };
//        add(label);
//        add more space between the label and the button
//        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
//        tab button
//        JButton button = new Client.TabButton();
//        add(button);
//        //add more space to the top of the component
//        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
//        super(s);
    }

    public boolean checkString(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setPort(String strPort) {
//        if(!checkString(strPort)){            
//            tfInput.setText("Неверный номер порта");
//        } else 
        if (Integer.parseInt(strPort) <= 0 || Integer.parseInt(strPort) > 65535) {
            tfInput.setText(Strings.getSTR_WRONG_PORT());
        } else {
            this.port = Integer.parseInt(strPort);
            tfInput.setText(Strings.getSTR_SET_PORT() + port);
        }
    }

    private boolean checkIP(String string) {
        if (checkString(string)) {
            if (!(Integer.parseInt(string) >= 0) || !(Integer.parseInt(string) < 256)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void setIP(String ip) {
        char[] chArr = ip.toCharArray();

        String ip_1 = "", ip_2 = "", ip_3 = "", ip_4 = "";
        int i = 0;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_1 += chArr[i];
        }
        if (!checkIP(ip_1)) {
            tfInput.setText(Strings.getSTR_WRONG_IP());
            return;
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_2 += chArr[i];
        }
        if (!checkIP(ip_2)) {
            tfInput.setText(Strings.getSTR_WRONG_IP());
            return;
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_3 += chArr[i];
        }
        if (!checkIP(ip_3)) {
            tfInput.setText(Strings.getSTR_WRONG_IP());
            return;
        }
        i++;
        for (; i < chArr.length; i++) {
            ip_4 += chArr[i];
        }
        if (!checkIP(ip_4)) {
            tfInput.setText(Strings.getSTR_WRONG_IP());
            return;
        }

        tfInput.setText(Strings.getSTR_SET_IP() + ip_1 + "." + ip_2 + "." + ip_3 + "." + ip_4);
        this.ip = ip;
    }

    public void setNic(String nicname) {
        this.nicname = nicname;
        tfInput.setText(Strings.getSTR_SET_NIC() + nicname);
    }

    public void setPas(String pas) {
        this.pfStr = pas;
        tfInput.setText(Strings.getSTR_SET_PASS());
    }

    // Закрывает потоки и сокет
    private void close() {
        try {
//            in.close();
//            out.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            
            socket.close();
        } catch (Exception e) {
            //System.err.println("Потоки не были закрыты!");
            tfInput.setText("Что-то не так");
        }
    }

    private void setConnection() {
        try {
            socket = new Socket(ip, port);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            out = new PrintWriter(socket.getOutputStream(), true);
//            System.out.println("Client: Socket");           
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());
//            System.out.println("Client: outputStream");
            inputStream   = new ObjectInputStream(this.socket.getInputStream());
//            System.out.println("Client: inputStream");
            //out.println(pfStr);
            //out.println(nicname);

            resender = new Resender();
            resender.start();
//            Thread.currentThread().sleep(1000);
            strChat = "";
//            out.println(Encryption.encode(pfStr, pfStr));   // Отсылаем пароль
            outputStream.writeObject(new Message(Encryption.encode(pfStr, pfStr),Encryption.encode(nicname, pfStr), clientEncryption.getPublicKeyFromKeypair()));
            
//            System.out.println("Encrypted from client to server: "+Encryption.encode(nicname, pfStr));
//            System.out.println(Encryption.decode(Encryption.encode("Decrypted from client to server: "+nicname, pfStr), pfStr));
         
//            out.println(Encryption.encode(nicname, pfStr)); // Отсылаем ник
//            out.println(encryption.encrypt(nicname));
            flagGoodConn = true;
            setButtonAfterStart();
            
        } catch (IOException ex) {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            tpOutput.append(Strings.getSTR_NON_ACK() + "\n");
            tpOutput.setCaretPosition(tpOutput.getText().length());
            errConn = true;
            flagGoodConn = false;
            exit();
        }
    }

    public void clientSendMsg(String message) throws UnsupportedEncodingException {
        //if (!message.equals("") && flagGoodConn) {
        if (!message.equals("")) {
            if (message.equals(Strings.getSTR_EXIT())) {
                if (!errConn) {
                    tpOutput.append(Strings.getSTR_YOU_EXIT() + "\n");
                    resender.setStop();
                }
                if (errConn && flagGoodConn) {
                    resender.setStop();
                }
            }
            
            try {
                    outputStream.writeObject(new Message(serverEncryption.encrypt(message)));
//                    System.out.println("Client send message: "+serverEncryption.encrypt(message));
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }            
//            tpOutput.append("\n"+Encryption.encode(message, pfStr)+"\n");
            if (message.equals(Strings.getSTR_EXIT())) {
                //errConn = false;
                exit();
            }
        }
    }

    private void exit() {
        setButtonBeforeStart();

        close();
        flagGoodConn = false;
        errConn = false;
    }

    void setButtonAfterStart() {

        btStartClient.setEnabled(false);
        btStopClient.setEnabled(true);
        btSent.setEnabled(true);

        if (!(conf == serverMark)) {
//            btSetIP.setEnabled(false);
            tfIP.setEditable(false);
        }

//        btSetPort.setEnabled(false);
        tfPort.setEditable(false);

//        btSetNic.setEnabled(false);
        tfNic.setEditable(false);

//        btSetPas.setEnabled(false);
        pfPas.setEditable(false);

        tfInput.setEditable(true);
        tfInput.setText("");

        cbNewConv.setEnabled(false);
    }

    void setButtonBeforeStart() {

        btStartClient.setEnabled(true); // true    false
        btStopClient.setEnabled(false);
        btSent.setEnabled(false);

        if (!(conf == serverMark)) {
//            btSetIP.setEnabled(true);
            tfIP.setEditable(true);
        }

//        btSetPort.setEnabled(true);
        tfPort.setEditable(true);

//        btSetNic.setEnabled(true);
        tfNic.setEditable(true);

//        btSetPas.setEnabled(true);
        pfPas.setEditable(true);

        tfInput.setEditable(false);
        tfInput.setText(Strings.getSTR_TO_CONN());

        cbNewConv.setEnabled(true);
    }
    
    private class Resender extends Thread {

        private boolean stoped = false;
        private int count = 0;
        private boolean bFirst = true;
        private Message message;

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            try {
                while (!stoped) {

                    try {

                        message = (Message) inputStream.readObject();

                        if (bFirst) {
                            bFirst = false;
                            serverEncryption.createPair(message.getPublicKey());
                        }
//                        receiveStr = clientEncryption.decrypt(message.getMessage()).replaceAll("\t", "");//removeTheTrash
                        receiveStr = Utils.removeTheTrash(clientEncryption.decrypt(message.getMessage()));
//                        System.out.println(nicname +" client receiveStr: " + receiveStr);
                    } catch (IOException | ClassNotFoundException e) {
                        break;
                    }
                    count++;
                    if (stoped) {
                        break;
                    }
                    ///////////////////////////////////////////////////////// 
                    if (receiveStr.equals(Strings.getSTR_WRONG_PASS())
                            || receiveStr.equals(Strings.getSTR_SAME_NIC())
                            || receiveStr.equals(Strings.getSTR_STOP_SERVER())
                            || receiveStr.equals("")) {
                        strChat = strChat + "\n" + receiveStr;
                        tpOutput.append(receiveStr + "\n");
//                        tpOutput.append(receiveStr);
                        tpOutput.setCaretPosition(tpOutput.getText().length());
                        errConn = true;

                        if (receiveStr.equals(Strings.getSTR_STOP_SERVER())) {
                            resender.setStop();
                        }
//                        System.out.println(nicname +" client receive att: " + receiveStr);
                        exit();
                        break;
                    }
                    if (receiveStr.equals(Strings.getSTR_EXIT_ALL())) {
//                        out.println(Encryption.encode(Strings.getSTR_EXIT_ALL(), pfStr));
//                        outputStream.writeObject(new Message(clientEncryption.encrypt(Strings.getSTR_EXIT_ALL())));
                        outputStream.writeObject(new Message(serverEncryption.encrypt(Strings.getSTR_EXIT_ALL())));
                        strChat = strChat + "\n" + Strings.getSTR_STOP_SERVER();
                        tpOutput.append(Strings.getSTR_STOP_SERVER() + "\n");
                        tpOutput.setCaretPosition(tpOutput.getText().length());
                        resender.setStop();
                        exit();
                        break;
                    } 
                    /////////////////////////////////////////////////////////
                    else {
                        strChat = strChat + "\n" + receiveStr;
                        if(!message.isfSystemMessage()){
                            tpOutput.append(receiveStr + "\n");
                            tpOutput.setCaretPosition(tpOutput.getText().length());
                        }
                        
                    }
                }
            } catch (IOException e) {
                //System.err.println("Ошибка при получении сообщения.");
                tpOutput.append("--- Ошибка при получении сообщения ---" + "\n");
                //e.printStackTrace();
            }
        }
    }

    public String getStrChat() {
        return strChat;
    }

    public String getReceiveStr() {
        return receiveStr;
    }

    public Boolean getFlagGoodConn() {
        return flagGoodConn;
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfInput = new javax.swing.JTextField();
        btSent = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        pSetCon = new javax.swing.JPanel();
        tfPort = new javax.swing.JTextField();
        pfPas = new javax.swing.JPasswordField();
        tfNic = new javax.swing.JTextField();
        tfIP = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbNewConv = new javax.swing.JCheckBox();
        btSettings = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tpOutput = new javax.swing.JTextArea();
        btStartClient = new javax.swing.JButton();
        btStopClient = new javax.swing.JButton();

        setOpaque(false);

        tfInput.setOpaque(false);
        tfInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfInputKeyPressed(evt);
            }
        });

        btSent.setText(">");
        btSent.setAlignmentY(0.0F);
        btSent.setFocusable(false);
        btSent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSentActionPerformed(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setOpaque(false);

        pSetCon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pSetCon.setOpaque(false);

        tfPort.setText("9988");
        tfPort.setMinimumSize(new java.awt.Dimension(0, 0));
        tfPort.setOpaque(false);
        tfPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPortKeyPressed(evt);
            }
        });

        pfPas.setText("9988");
        pfPas.setMinimumSize(new java.awt.Dimension(0, 0));
        pfPas.setOpaque(false);
        pfPas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pfPasKeyPressed(evt);
            }
        });

        tfNic.setText("NicName");
        tfNic.setMinimumSize(new java.awt.Dimension(0, 0));
        tfNic.setOpaque(false);
        tfNic.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfNicKeyPressed(evt);
            }
        });

        tfIP.setEditable(true);
        tfIP.setOpaque(false);
        tfIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIPKeyPressed(evt);
            }
        });

        jLabel1.setText("Адрес:");

        jLabel2.setText("Порт:");

        jLabel3.setText("Ник:");

        jLabel4.setText("Пароль:");

        cbNewConv.setText("Новый диалог");
        cbNewConv.setOpaque(false);

        btSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/connector/images/setting.png"))); // NOI18N
        btSettings.setBorderPainted(false);
        btSettings.setContentAreaFilled(false);
        btSettings.setFocusable(false);
        btSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pSetConLayout = new javax.swing.GroupLayout(pSetCon);
        pSetCon.setLayout(pSetConLayout);
        pSetConLayout.setHorizontalGroup(
            pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSetConLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pSetConLayout.createSequentialGroup()
                        .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pSetConLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfIP, 0, 0, Short.MAX_VALUE))
                            .addGroup(pSetConLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(tfNic, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pSetConLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pfPas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pSetConLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pSetConLayout.createSequentialGroup()
                        .addComponent(cbNewConv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pSetConLayout.setVerticalGroup(
            pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSetConLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tfIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfNic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(pfPas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbNewConv)
                    .addComponent(btSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jSplitPane1.setTopComponent(pSetCon);

        jPanel2.setOpaque(false);

        jScrollPane1.setOpaque(false);

        tpOutput.setColumns(20);
        tpOutput.setRows(5);
        tpOutput.setOpaque(false);
        jScrollPane1.setViewportView(tpOutput);

        btStartClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStartClientActionPerformed(evt);
            }
        });

        btStopClient.setText("   ");
        btStopClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStopClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btStartClient, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btStopClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btStopClient, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btStartClient, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btSent, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSent))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tfPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPortKeyPressed
        if (evt.getKeyCode() == 10) {
            setPort(tfPort.getText());
        }
    }//GEN-LAST:event_tfPortKeyPressed

    private void pfPasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pfPasKeyPressed
        if (evt.getKeyCode() == 10) {
            setPas(String.valueOf(pfPas.getPassword()));
        }
    }//GEN-LAST:event_pfPasKeyPressed

    private void tfNicKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNicKeyPressed
        if (evt.getKeyCode() == 10) {
            setNic(tfNic.getText());
        }
    }//GEN-LAST:event_tfNicKeyPressed

    private void btStartClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStartClientActionPerformed
        if (conf == serverMark) {
            server.setPort(tfPort.getText());
            server.setPas(String.valueOf(pfPas.getPassword()));
            server.startServer();
        }
        setNic(tfNic.getText());
        setIP((String) tfIP.getSelectedItem());
        setPas(String.valueOf(pfPas.getPassword()));
        setPort(tfPort.getText());
        setConnection();
    }//GEN-LAST:event_btStartClientActionPerformed

    private void btStopClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStopClientActionPerformed
        try {
            clientSendMsg(Strings.getSTR_EXIT());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (conf == serverMark) {
            server.stopServer();
        }
    }//GEN-LAST:event_btStopClientActionPerformed

    private void tfInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfInputKeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                clientSendMsg(tfInput.getText());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            tfInput.setText("");
        }
    }//GEN-LAST:event_tfInputKeyPressed

    private void btSentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSentActionPerformed
        String message = tfInput.getText();
        try {
            clientSendMsg(message);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfInput.setText("");
    }//GEN-LAST:event_btSentActionPerformed

    private void tfIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIPKeyPressed
        if (evt.getKeyCode() == 10) {
            setIP((String) tfIP.getSelectedItem());
        }
    }//GEN-LAST:event_tfIPKeyPressed

    private void btSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSettingsActionPerformed
        server.setVisible(true);
    }//GEN-LAST:event_btSettingsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSent;
    private javax.swing.JButton btSettings;
    private javax.swing.JButton btStartClient;
    private javax.swing.JButton btStopClient;
    private javax.swing.JCheckBox cbNewConv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pSetCon;
    private javax.swing.JPasswordField pfPas;
    private javax.swing.JComboBox<String> tfIP;
    private javax.swing.JTextField tfInput;
    private javax.swing.JTextField tfNic;
    private javax.swing.JTextField tfPort;
    private javax.swing.JTextArea tpOutput;
    // End of variables declaration//GEN-END:variables

//    class TabButton extends JButton implements ActionListener {
//        public TabButton() {
//            int size = 17;
//            setPreferredSize(new Dimension(size, size));
//            setToolTipText("close this tab");
//            //Make the button looks the same for all Laf's
//            setUI(new BasicButtonUI());
//            //Make it transparent
//            setContentAreaFilled(false);
//            //No need to be focusable
//            setFocusable(false);
//            setBorder(BorderFactory.createEtchedBorder());
//            setBorderPainted(false);
//            //Making nice rollover effect
//            //we use the same listener for all buttons
//            addMouseListener(buttonMouseListener);
//            setRolloverEnabled(true);
//            //Close the proper tab by clicking the button
//            addActionListener(this);
//        }
// 
//        public void actionPerformed(ActionEvent e) {
//            int i = pane.indexOfTabComponent(Client.this);
//            if (i != -1) {
//                pane.remove(i);
//            }
//        }
// 
//        //we don't want to update UI for this button
//        public void updateUI() {
//        }
// 
//        //paint the cross
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2 = (Graphics2D) g.create();
//            //shift the image for pressed buttons
//            if (getModel().isPressed()) {
//                g2.translate(1, 1);
//            }
//            g2.setStroke(new BasicStroke(2));
//            g2.setColor(Color.BLACK);
//            if (getModel().isRollover()) {
//                g2.setColor(Color.MAGENTA);
//            }
//            int delta = 6;
//            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
//            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
//            g2.dispose();
//        }
//    } 
}

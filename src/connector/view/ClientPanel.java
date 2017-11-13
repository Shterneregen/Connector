package connector.view;

import connector.utils.Encryption;
import connector.resources.ControlLines;
import connector.utils.Utils;
import connector.model.Message;
import connector.model.Client;
import connector.constant.ClientType;
import connector.constant.ServerConfig;
import connector.utils.ProjectProperties;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.text.AbstractDocument;

/**
 * Панель клиента
 *
 * @author Yura
 */
public class ClientPanel extends javax.swing.JPanel {

    private Client client;

    private String strChat;
    private String receiveStr;
    private int conf;

    private ServerFrame serverFrame;
    private Resender resender;

    private Boolean flagGoodConn;
    private boolean errConn;
    private boolean bRsa;

    private Encryption serverEncryption;
    private Encryption clientEncryption;

    private Properties stringsFile;

    private List<String> listAddr; // Массив локальных IP адресов
//    private final JTabbedPane pane;

    public ClientPanel(int conf) {
        stringsFile = ProjectProperties.getInstance().getStringsFile();
        this.conf = conf;

        flagGoodConn = false;
        errConn = false;
        listAddr = Utils.getMyLocalIP();
        bRsa = false;
        clientEncryption = new Encryption();
        serverEncryption = new Encryption();

        initComponents();
        setItemsNames();
//        btSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("../resources/images/setting.png")));
//        btSettings.setToolTipText(stringsFile.getProperty("clientPanel.button.setting"));
        listAddr.stream().forEach((address) -> {
            tfIP.addItem(address);
        });
        if (conf == ClientType.CLIENT_WITH_SERVER) {
            btStartClient.setText(stringsFile.getProperty("clientPanel.button.creat_conversation"));
            btStopClient.setText(stringsFile.getProperty("clientPanel.button.stop_conversation"));
            tfIP.setEditable(false);
        } else if (conf == ClientType.CLIENT_WITHOUT_SERVER) {
            btStartClient.setText(stringsFile.getProperty("clientPanel.button.join"));
            btStopClient.setText(stringsFile.getProperty("clientPanel.button.exit"));
        }
        ((AbstractDocument) tfPort.getDocument()).setDocumentFilter(new Utils().new DocumentFilterForPort());

        tpOutput.setWrapStyleWord(true);// слова не будут разрываться в том месте, где они «натыкаются» на границу компонента, а будут целиком перенесены на новую строку
        tpOutput.setLineWrap(true);     // длинные строки будут укладываться в несколько строк вместо одной, уходящей за границы компонента
        tpOutput.setEditable(false);
        tfInput.setText(stringsFile.getProperty("no_conn"));
        tfInput.setEditable(false);
        btStopClient.setEnabled(false);
        btSent.setEnabled(false);
//        btSettings.setEnabled(false);

        cbNewConversation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cbNewConversation.isSelected()) {
                    ClientPanel.this.conf = ClientType.CLIENT_WITH_SERVER;
                    btStartClient.setText(stringsFile.getProperty("clientPanel.button.creat_conversation"));
                    btStopClient.setText(stringsFile.getProperty("clientPanel.button.stop_conversation"));
                    tfIP.setEditable(false);
//                    btSettings.setEnabled(true);
                } else {
                    ClientPanel.this.conf = ClientType.CLIENT_WITHOUT_SERVER;
                    btStartClient.setText(stringsFile.getProperty("clientPanel.button.join"));
                    btStopClient.setText(stringsFile.getProperty("clientPanel.button.exit"));
                    tfIP.setEditable(true);
//                    btSettings.setEnabled(false);
                }
                tfIP.removeAllItems();
                listAddr.stream().forEach((address) -> {
                    tfIP.addItem(address);
                });
            }
        });
    }

    private void setConnection() {
        try {
            resender = new Resender();
            resender.start();
            strChat = "";
            String pfStr = client.getPsw();
            client.getOutputStream().writeObject(new Message(Encryption.encode(pfStr, pfStr),
                    Encryption.encode(client.getNicname(), pfStr), clientEncryption.getPublicKeyFromKeypair()));
            flagGoodConn = true;
            setButtonAfterStart();
        } catch (IOException ex) {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            tpOutput.append(stringsFile.getProperty("STR_NON_ACK") + "\n");
//            tpOutput.setCaretPosition(tpOutput.getText().length());
            errConn = true;
            flagGoodConn = false;
            exit();
        }
    }

    public void clientSendMsg(String message) {
        if (!message.replaceAll("\\s+", "").equals("")) {
            try {
                sendMsg(message);
            } catch (IOException ex) {
                Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sendMsg(String message) throws IOException {
        client.getOutputStream().writeObject(new Message(serverEncryption.encrypt(message)));
    }

    private void exit() {
        setButtonBeforeStart();
        flagGoodConn = false;
        errConn = false;
    }

    void setButtonAfterStart() {
        btStartClient.setEnabled(false);
        btStopClient.setEnabled(true);
        btSent.setEnabled(true);

        if (conf != ClientType.CLIENT_WITH_SERVER) {
            tfIP.setEditable(false);
        }
        tfPort.setEditable(false);
        tfNic.setEditable(false);
        pfPas.setEditable(false);

        tfInput.setEditable(true);
        tfInput.setText("");

        cbNewConversation.setEnabled(false);
    }

    void setButtonBeforeStart() {
        btStartClient.setEnabled(true);
        btStopClient.setEnabled(false);
        btSent.setEnabled(false);

        if (conf != ClientType.CLIENT_WITH_SERVER) {
            tfIP.setEditable(true);
        }
        tfPort.setEditable(true);
        tfNic.setEditable(true);
        pfPas.setEditable(true);

        tfInput.setEditable(false);
        tfInput.setText(stringsFile.getProperty("no_conn"));

        cbNewConversation.setEnabled(true);
    }

    private void setItemsNames() {
        lbIP.setText(stringsFile.getProperty("serverFrame.lb.ip"));
        lbPort.setText(stringsFile.getProperty("serverFrame.lb.port"));
        lbNic.setText(stringsFile.getProperty("lb.nic"));
        lbPass.setText(stringsFile.getProperty("serverFrame.lb.pass"));
        cbNewConversation.setText(stringsFile.getProperty("clientPanel.cb.newConversation"));

        tfNic.setText(stringsFile.getProperty("str.defaultNicName"));
        tfPort.setText(stringsFile.getProperty("str.defaultPort"));
        pfPas.setText(stringsFile.getProperty("str.defaultPsw"));
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
                            strChat = strChat + "\n" + receiveStr;
                            tpOutput.append(commandToMsg + "\n");
                            tpOutput.setCaretPosition(tpOutput.getText().length());
                            errConn = true;
                            if (receiveStr.equals(ControlLines.STR_STOP_SERVER)) {
                                resender.setStop();
                            }
                            exit();
                            break;
                        default:
                            strChat = strChat + "\n" + receiveStr;
                            if (!message.isfSystemMessage()) {
                                tpOutput.append(receiveStr + "\n");
                                tpOutput.setCaretPosition(tpOutput.getText().length());
                            }
                            break;
                    }
                }
            } 
            finally {
                client.closeStreams();
            }
        }
    }
    //</editor-fold>

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
        lbIP = new javax.swing.JLabel();
        lbPort = new javax.swing.JLabel();
        lbNic = new javax.swing.JLabel();
        lbPass = new javax.swing.JLabel();
        cbNewConversation = new javax.swing.JCheckBox();
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

        tfPort.setMinimumSize(new java.awt.Dimension(0, 0));
        tfPort.setOpaque(false);

        pfPas.setMinimumSize(new java.awt.Dimension(0, 0));
        pfPas.setOpaque(false);

        tfNic.setMinimumSize(new java.awt.Dimension(0, 0));
        tfNic.setOpaque(false);

        tfIP.setEditable(true);
        tfIP.setOpaque(false);

        lbIP.setText("Адрес:");

        lbPort.setText("Порт:");

        lbNic.setText("Ник:");

        lbPass.setText("Пароль:");

        cbNewConversation.setText("Новый диалог");
        cbNewConversation.setOpaque(false);

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
                                .addComponent(lbIP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfIP, 0, 0, Short.MAX_VALUE))
                            .addGroup(pSetConLayout.createSequentialGroup()
                                .addComponent(lbNic)
                                .addGap(18, 18, 18)
                                .addComponent(tfNic, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pSetConLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbPass)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pfPas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pSetConLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lbPort)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pSetConLayout.createSequentialGroup()
                        .addComponent(cbNewConversation)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pSetConLayout.setVerticalGroup(
            pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSetConLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbIP)
                        .addComponent(lbPort)
                        .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tfIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pSetConLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNic)
                    .addComponent(tfNic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPass)
                    .addComponent(pfPas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cbNewConversation)
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

    private void btStartClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStartClientActionPerformed
        String ip = Utils.getAndCheckIP((String) tfIP.getSelectedItem());
        int port = Utils.getAndCheckPort(tfPort.getText());
        if (ip != null && port > 0) {
            if (conf == ClientType.CLIENT_WITH_SERVER) {
                serverFrame = new ServerFrame(ControlLines.MAIN_NAME, ServerConfig.SERVER_FROM_CLIENT);
                serverFrame.startServer(tfPort.getText(), String.valueOf(pfPas.getPassword()));
            }
            client = new Client(port, ip, tfNic.getText(), String.valueOf(pfPas.getPassword()));
            setConnection();
        }
    }//GEN-LAST:event_btStartClientActionPerformed

    private void btStopClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStopClientActionPerformed
        if (conf == ClientType.CLIENT_WITH_SERVER) {
            serverFrame.stopServer();
        } else {
            resender.setStop();
            tpOutput.append(stringsFile.getProperty("you_exit") + "\n");
            clientSendMsg(ControlLines.STR_EXIT);
            exit();
        }
    }//GEN-LAST:event_btStopClientActionPerformed

    private void tfInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfInputKeyPressed
        if (evt.getKeyCode() == 10) {
            clientSendMsg(tfInput.getText());
            tfInput.setText("");
        }
    }//GEN-LAST:event_tfInputKeyPressed

    private void btSentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSentActionPerformed
        clientSendMsg(tfInput.getText());
        tfInput.setText("");
    }//GEN-LAST:event_btSentActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSent;
    private javax.swing.JButton btStartClient;
    private javax.swing.JButton btStopClient;
    private javax.swing.JCheckBox cbNewConversation;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbIP;
    private javax.swing.JLabel lbNic;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbPort;
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

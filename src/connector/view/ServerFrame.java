package connector.view;

import connector.resources.ControlLines;
import connector.Tray;
import connector.utils.Utils;
import static connector.constant.ServerConfig.*;
import static connector.constant.TrayType.SERVER_TRAY;
import connector.model.Server;
import connector.utils.ProjectProperties;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.text.AbstractDocument;

public class ServerFrame extends javax.swing.JFrame {

    private Server server;
    private Properties stringsFile;

    public ServerFrame(String s, int conf) {
        super(s);
        server = new Server();
        stringsFile = ProjectProperties.getInstance().getStringsFile();

        initComponents();
        setItemsNames();
        ((AbstractDocument) tfPort.getDocument()).setDocumentFilter(new Utils().new DocumentFilterForPort());

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
                if (server.getIsStartServer() && conf == ONLY_SERVER) {
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
                new Tray().setTrayIcon(ServerFrame.this, null, SERVER_TRAY);
                setVisible(false);
            }

            public void windowOpened(WindowEvent event) {

            }

        });
    }

    private void setItemsNames() {
        ArrayList<String> listAddr = Utils.getMyLocalIP();
        for (int i = 0; i < listAddr.size(); i++) {
            jcbIP.addItem(listAddr.get(i));
        }
        jmClient.setText(stringsFile.getProperty("clientFrame.jm.client"));
        jmiNewClientWindow.setText(stringsFile.getProperty("clientFrame.jmi.newClientWindow"));

        jmServer.setText(stringsFile.getProperty("clientFrame.jm.server"));
        jmiNewServerWindow.setText(stringsFile.getProperty("clientFrame.jmi.newServerWindow"));

        lbNumUs.setText(stringsFile.getProperty("serverFrame.str.users") + server.getUserNumber());
        lbPort.setText(stringsFile.getProperty("serverFrame.lb.port"));
        lbPass.setText(stringsFile.getProperty("serverFrame.lb.pass"));
        lbIP.setText(stringsFile.getProperty("serverFrame.lb.ip"));

        btStartServer.setText(stringsFile.getProperty("serverFrame.button.startServer"));
        btStopServer.setText(stringsFile.getProperty("serverFrame.button.stopServer"));
        btStopServer.setEnabled(false);

//        lbYourIP.setText(" Ваш локальный IP ");
    }

//    public String getMyLocalIP(){
//        InetAddress addr = null;
//        try {
//            addr = InetAddress.getLocalHost();
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        String myLANIP = addr.getHostAddress();
//        return myLANIP;
//    }
    public void setPort(String strPort) {
        if (Integer.parseInt(strPort) <= 0 || Integer.parseInt(strPort) > 65535) {
            lbNumUs.setText(stringsFile.getProperty("wrong_port"));
        } else {
            server.setPort(Integer.parseInt(strPort));
//            lbNumUs.setText(stringsFile.getProperty("set_port"));
        }
    }

    public void setPass(String strPort) {
        server.setPfStr(strPort);
    }

    public StringBuilder getBuffChat() {
        return server.getBuffChat();
    }

    public void startServer() {
        server.setIsStartServer(true);
        server.createServerThread();
        server.startServer();

        btStartServer.setEnabled(false);
        btStopServer.setEnabled(true);

        tfPort.setEditable(false);
        pfPas.setEditable(false);
    }

    public void stopServer() {
        server.setIsStartServer(false);
        server.setStopServerThread();
        server.closeAllServerConnection();

        btStartServer.setEnabled(true);
        btStopServer.setEnabled(false);

        tfPort.setEditable(true);
        pfPas.setEditable(true);

        server.setUserNumber(0);
        lbNumUs.setText(stringsFile.getProperty("serverFrame.str.users") + server.getUserNumber());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfPort = new javax.swing.JTextField();
        btStartServer = new javax.swing.JButton();
        btStopServer = new javax.swing.JButton();
        lbIP = new javax.swing.JLabel();
        lbNumUs = new javax.swing.JLabel();
        pfPas = new javax.swing.JPasswordField();
        jcbIP = new javax.swing.JComboBox<>();
        lbPort = new javax.swing.JLabel();
        lbPass = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmServer = new javax.swing.JMenu();
        jmiNewServerWindow = new javax.swing.JMenuItem();
        jmClient = new javax.swing.JMenu();
        jmiNewClientWindow = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tfPort.setText("9988");
        tfPort.setToolTipText("");
        tfPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPortKeyPressed(evt);
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

        lbIP.setText("IP адрес:");

        lbNumUs.setText(" ");

        pfPas.setText("9988");
        pfPas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pfPasKeyPressed(evt);
            }
        });

        lbPort.setText("Порт");

        lbPass.setText("Пароль");

        jmServer.setText("Сервер");

        jmiNewServerWindow.setText("Новое серверное окно");
        jmiNewServerWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewServerWindowActionPerformed(evt);
            }
        });
        jmServer.add(jmiNewServerWindow);

        jMenuBar1.add(jmServer);

        jmClient.setText("Клиент");

        jmiNewClientWindow.setText("Новое клиентское окно");
        jmiNewClientWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewClientWindowActionPerformed(evt);
            }
        });
        jmClient.add(jmiNewClientWindow);

        jMenuBar1.add(jmClient);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbNumUs, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbIP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lbPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tfPort)
                                .addComponent(btStartServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btStopServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(pfPas)
                                    .addComponent(lbPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jcbIP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPort)
                    .addComponent(lbPass, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfPort)
                    .addComponent(pfPas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btStartServer)
                    .addComponent(btStopServer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbIP, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbNumUs)
                .addContainerGap())
        );

        tfPort.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btStartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStartServerActionPerformed
        startServer();
    }//GEN-LAST:event_btStartServerActionPerformed

    private void btStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btStopServerActionPerformed
        stopServer();
    }//GEN-LAST:event_btStopServerActionPerformed

    private void tfPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPortKeyPressed
        if (evt.getKeyCode() == 10) {
            String strPort = tfPort.getText();
            if (Integer.parseInt(strPort) <= 0 || Integer.parseInt(strPort) > 65535) {
                lbNumUs.setText(stringsFile.getProperty("wrong_port"));
                tfPort.setText(stringsFile.getProperty("wrong_port"));
            } else {
                server.setPort(Integer.parseInt(strPort));
//                lbNumUs.setText(stringsFile.getProperty("set_port"));
            }
            setPort(tfPort.getText());
        }
    }//GEN-LAST:event_tfPortKeyPressed

    private void pfPasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pfPasKeyPressed
        if (evt.getKeyCode() == 10) {
            server.setPfStr(String.valueOf(pfPas.getPassword()));
//            server.setPfStr(String.valueOf(pfPas.getPassword()));
//            lbNumUs.setText(stringsFile.getProperty("set_pass"));
        }
    }//GEN-LAST:event_pfPasKeyPressed

    private void jmiNewClientWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiNewClientWindowActionPerformed
        ClientFrame client = new ClientFrame(ControlLines.MAIN_NAME);
        client.setVisible(true);
    }//GEN-LAST:event_jmiNewClientWindowActionPerformed

    private void jmiNewServerWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiNewServerWindowActionPerformed
        ServerFrame server = new ServerFrame(ControlLines.MAIN_NAME, ONLY_SERVER);
        server.setVisible(true);
    }//GEN-LAST:event_jmiNewServerWindowActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btStartServer;
    private javax.swing.JButton btStopServer;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JComboBox<String> jcbIP;
    private javax.swing.JMenu jmClient;
    private javax.swing.JMenu jmServer;
    private javax.swing.JMenuItem jmiNewClientWindow;
    private javax.swing.JMenuItem jmiNewServerWindow;
    private javax.swing.JLabel lbIP;
    private javax.swing.JLabel lbNumUs;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbPort;
    private javax.swing.JPasswordField pfPas;
    private javax.swing.JTextField tfPort;
    // End of variables declaration//GEN-END:variables

}

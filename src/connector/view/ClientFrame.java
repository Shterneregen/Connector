package connector.view;

import connector.constant.ClientType;
import connector.constant.ServerConfig;
import connector.resources.ControlLines;
import connector.model.Tray;
import connector.constant.TrayType;
import connector.utils.ProjectProperties;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientFrame extends javax.swing.JFrame {

    private ClientPanel mainPanel;
    private String strChat;
    private Properties stringsFile;

//    Utils.StatusBar statusBar;
    public ClientPanel getMainPanel() {
        return mainPanel;
    }

    public ClientFrame(String s) {
        super(s);
        stringsFile = ProjectProperties.getInstance().getStringsFile();
//        try { 
//            icon = ImageIO.read(ClientFrame.class.getResourceAsStream("../resources/images/icon.png"));
//        } catch (IOException ex) {
//            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        setIconImage(icon);
// statusBar = new Utils().new StatusBar();
////            getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
//            getContentPane().add(statusBar,java.awt.BorderLayout.SOUTH);
////            getContentPane().add(statusBar,java.awt.BorderLayout.SOUTH);
//            statusBar.setMessage("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");

        mainPanel = new ClientPanel(ClientType.CLIENT_WITHOUT_SERVER);

        initComponents();
        setMenuItemsNames();
//        jTabbedPane1.add(mainPanel);
//        jpTop.add(mainPanel);

//        jPanel1.setLayout(new FlowLayout());
//        jPanel1.add(mainPanel);
        if (ProjectProperties.CLIENT_BACKGROUND != null) {
            BgPanel bgPanel = new BgPanel();
            bgPanel.add(mainPanel);
            this.setContentPane(bgPanel);
        } else {
            this.add(mainPanel);
        }

//        listClients.add(mainPanel);        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowClosing(WindowEvent event) {
                if (mainPanel.getFlagGoodConn()) {
                    mainPanel.clientSendMsg(ControlLines.STR_EXIT);
                }
                setVisible(false);
                dispose();
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {

//                if (numCl != 0) {
//                    new Tray().setTrayIcon(ClientFrame.this, listClients, CLIENT);
                new Tray().setTrayIcon(ClientFrame.this, mainPanel, TrayType.CLIENT_TRAY);
//                } else {
//                    new Tray().setTrayIcon(ClientFrame.this, null, CLIENT);
//                }
                setState(JFrame.ICONIFIED);
                setVisible(false);
            }

            public void windowOpened(WindowEvent event) {
            }
        });
        pack();
    }

    public String getStrChat() {
        return strChat;
    }

    private void setMenuItemsNames() {
        jmFile.setText(stringsFile.getProperty("clientFrame.jm.file"));
        jmiExit.setText(stringsFile.getProperty("clientFrame.jmi.exit"));

        jmClient.setText(stringsFile.getProperty("clientFrame.jm.client"));
        jmiNewClientWindow.setText(stringsFile.getProperty("clientFrame.jmi.newClientWindow"));
        jmiStopCurrentClient.setText(stringsFile.getProperty("clientFrame.jmi.stopCurrentClient"));
        jmiGetAllSessionMsgs.setText(stringsFile.getProperty("clientFrame.jmi.getAllSessionMsgs"));

        jmServer.setText(stringsFile.getProperty("clientFrame.jm.server"));
        jmiNewServerWindow.setText(stringsFile.getProperty("clientFrame.jmi.newServerWindow"));

        jmHelp.setText(stringsFile.getProperty("clientFrame.jm.help"));
        jmiAbout.setText(stringsFile.getProperty("clientFrame.jmi.about"));
        jmiManual.setText(stringsFile.getProperty("clientFrame.jmi.manual"));
    }

    class BgPanel extends JPanel {

        public void paintComponent(Graphics g) {
            //im = ImageIO.read(new File("D:\\Tests\\fon.jpg"));
            g.drawImage(ProjectProperties.CLIENT_BACKGROUND, 0, 0, null);
            repaint();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jmFile = new javax.swing.JMenu();
        jmiExit = new javax.swing.JMenuItem();
        jmClient = new javax.swing.JMenu();
        jmiNewClientWindow = new javax.swing.JMenuItem();
        jmiStopCurrentClient = new javax.swing.JMenuItem();
        jmiGetAllSessionMsgs = new javax.swing.JMenuItem();
        jmServer = new javax.swing.JMenu();
        jmiNewServerWindow = new javax.swing.JMenuItem();
        jmHelp = new javax.swing.JMenu();
        jmiAbout = new javax.swing.JMenuItem();
        jmiManual = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("fClient"); // NOI18N

        jMenuBar1.setOpaque(false);

        jmFile.setText("Файл");

        jmiExit.setText("Выход");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiExitActionPerformed(evt);
            }
        });
        jmFile.add(jmiExit);

        jMenuBar1.add(jmFile);

        jmClient.setText("Клиент");

        jmiNewClientWindow.setText("Новое клиентское окно");
        jmiNewClientWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewClientWindowActionPerformed(evt);
            }
        });
        jmClient.add(jmiNewClientWindow);

        jmiStopCurrentClient.setText("Остановить текущего");
        jmiStopCurrentClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiStopCurrentClientActionPerformed(evt);
            }
        });
        jmClient.add(jmiStopCurrentClient);

        jmiGetAllSessionMsgs.setText("Получить все сообщения сеанса");
        jmiGetAllSessionMsgs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiGetAllSessionMsgsActionPerformed(evt);
            }
        });
        jmClient.add(jmiGetAllSessionMsgs);

        jMenuBar1.add(jmClient);

        jmServer.setText("Сервер");

        jmiNewServerWindow.setText("Новое серверное окно");
        jmiNewServerWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiNewServerWindowActionPerformed(evt);
            }
        });
        jmServer.add(jmiNewServerWindow);

        jMenuBar1.add(jmServer);

        jmHelp.setText("Справка");

        jmiAbout.setText("О программе");
        jmHelp.add(jmiAbout);

        jmiManual.setText("Инструкция");
        jmHelp.add(jmiManual);

        jMenuBar1.add(jmHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGap(0, 449, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmiNewServerWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiNewServerWindowActionPerformed
        ServerFrame server = new ServerFrame(ControlLines.MAIN_NAME, ServerConfig.ONLY_SERVER);
        server.setVisible(true);
    }//GEN-LAST:event_jmiNewServerWindowActionPerformed

    private void jmiStopCurrentClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiStopCurrentClientActionPerformed
//        if (!errConn && flagGoodConn) {
//            try {
//                clientSendMsg(Utils.getSTR_EXIT());
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            tfInput.setText("Клиент не запущен");
//        }
    }//GEN-LAST:event_jmiStopCurrentClientActionPerformed

    private void jmiNewClientWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiNewClientWindowActionPerformed
        ClientFrame client = new ClientFrame(ControlLines.MAIN_NAME);
        client.setVisible(true);
    }//GEN-LAST:event_jmiNewClientWindowActionPerformed

    private void jmiGetAllSessionMsgsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiGetAllSessionMsgsActionPerformed
//        if(flagGoodConn){
//        tpOutput.setText("");
//        try {
//            clientSendMsg(Utils.getSTR_GET_ALL_MSG());
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        }else{
//            tfInput.setText("Клиент не запущен");
//        }
    }//GEN-LAST:event_jmiGetAllSessionMsgsActionPerformed

    private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jmiExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jmClient;
    private javax.swing.JMenu jmFile;
    private javax.swing.JMenu jmHelp;
    private javax.swing.JMenu jmServer;
    private javax.swing.JMenuItem jmiAbout;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiGetAllSessionMsgs;
    private javax.swing.JMenuItem jmiManual;
    private javax.swing.JMenuItem jmiNewClientWindow;
    private javax.swing.JMenuItem jmiNewServerWindow;
    private javax.swing.JMenuItem jmiStopCurrentClient;
    // End of variables declaration//GEN-END:variables
}

package connector.view;

import connector.Strings;
import connector.Tray;
import static connector.constant.ClientType.CLIENT_WITHOUT_SERVER;
import static connector.constant.ServerConfig.ONLY_SERVER;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientFrame extends javax.swing.JFrame {

    private static final int CLIENT = 0;
    private MainPanel mainPanel;
    private String strChat;
    private static final String CLIENT_BACKGROUND = "../resources/images/fon33.jpg";
//    private static int numCl;
//    private Image icon;

//    private static ArrayList<Client> listClients = new ArrayList<Client>();
//    Utils.StatusBar statusBar;

    public MainPanel getMainPanel() {
        return mainPanel;
    }
    
    public ClientFrame(String s) {
        super(s);
//        numCl = 0;
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

        mainPanel = new MainPanel(CLIENT_WITHOUT_SERVER);       
        
        initComponents();
//        jTabbedPane1.add(mainPanel);
//        jpTop.add(mainPanel);

//        jPanel1.setLayout(new FlowLayout());
//        jPanel1.add(mainPanel);

        BgPanel bgPanel = new BgPanel();
        bgPanel.add(mainPanel);
//        setContentPane(new BgPanel());
        this.setContentPane(bgPanel);
        

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
                    try {
                        mainPanel.clientSendMsg(Strings.STR_EXIT);
                    } catch (UnsupportedEncodingException ex) {
//                        tpOutput.append("\n --- Исключение из  windowClosing---");
                        Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                    new Tray().setTrayIcon(ClientFrame.this, mainPanel, CLIENT);
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

    class BgPanel extends JPanel {
        public void paintComponent(Graphics g) {
            Image im = null;

            //im = ImageIO.read(new File("D:\\Tests\\fon.jpg"));
            URL imageURL = (ClientFrame.class.getResource(CLIENT_BACKGROUND));
            im = Toolkit.getDefaultToolkit().getImage(imageURL);

            g.drawImage(im, 0, 0, null);
            repaint();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("fClient"); // NOI18N

        jMenuBar1.setOpaque(false);

        jMenu4.setText("Файл");

        jMenuItem7.setText("Выход");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("Клиент");

        jMenuItem3.setText("Новое клиентское окно");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Остановить текущего");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem6.setText("Получить все сообщения сеанса");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu1.setText("Сервер");

        jMenuItem2.setText("Новое серверное окно");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Справка");

        jMenuItem1.setText("О программе");
        jMenu2.add(jMenuItem1);

        jMenuItem5.setText("Инструкция");
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

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

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ServerFrame server = new ServerFrame(Strings.MAIN_NAME, ONLY_SERVER);
        server.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
//        if (!errConn && flagGoodConn) {
//            try {
//                clientSendMsg(Utils.getSTR_EXIT());
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            tfInput.setText("Клиент не запущен");
//        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        ClientFrame client = new ClientFrame(Strings.MAIN_NAME);
        client.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
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
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    // End of variables declaration//GEN-END:variables
}

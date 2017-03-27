package connector;

import connector.tab.ButtonTabComponent;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.text.AbstractDocument;

public class ClientFrame extends javax.swing.JFrame {

    private static final int CLIENT = 0;
    private Client client;
    private String strChat;
    private static final String CLIENT_BACKGROUND = "images/fon33.jpg";
//    private static int numCl;
    private Image icon;

//    private static ArrayList<Client> listClients = new ArrayList<Client>();
//    Utils.StatusBar statusBar;

    public Client getClient() {
        return client;
    }
    
    public ClientFrame(String s) {
        super(s);
//        numCl = 0;
        try { 
            icon = ImageIO.read(ClientFrame.class.getResourceAsStream("images/icon.png"));
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setIconImage(icon);
// statusBar = new Utils().new StatusBar();
////            getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
//            getContentPane().add(statusBar,java.awt.BorderLayout.SOUTH);
////            getContentPane().add(statusBar,java.awt.BorderLayout.SOUTH);
//            statusBar.setMessage("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");

        client = new Client(0);
        
        
        initComponents();

//        client.setName("Диалог");
//        jTabbedPane1.add(client);
//        jpTop.add(client);

//        jPanel1.setLayout(new FlowLayout());
//        jPanel1.add(client);

        BgPanel bgPanel = new BgPanel();
        bgPanel.add(client);
//        setContentPane(new BgPanel());
        this.setContentPane(bgPanel);
        

//        listClients.add(client);
        
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {

            }

            public void windowClosed(WindowEvent event) {

            }

            public void windowClosing(WindowEvent event) {
                if (client.getFlagGoodConn()) {
                    try {
                        client.clientSendMsg(Strings.getSTR_EXIT());
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
                    new Tray().setTrayIcon(ClientFrame.this, client, CLIENT);
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

//    public void addPanel(JPanel jp)
//{
//    JPanel jp1=new JPanel();
//    int count=jp.getComponentCount()-1;
//        int i=0;
//    for(;i>=0;i--)
//    {
//         jp1.addComponent(
//              ((Component) // this casts the clone back to component. This is maybe superfluous.
//               ((Cloneable)jp.getComponent(i) // You have to ensure that all components that are returned are in fact instances of Cloneable.
//               ).clone()
//              ));
//    }
//    //after this I am setting bounds of jp1.
//    this.add(jp1);
//}

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
        Server server = new Server(Strings.getMAIN_NAME(),0);
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
        ClientFrame client = new ClientFrame(Strings.getMAIN_NAME());
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
    
    /////////////////////////////////////////////////////////////////////
        //        Client client = new Client(1);
        //        client.setName("Диалог Master "+ (++numCl));
        //        listClients.add(client);
        //        jpClient.add(client);

        //        repaint();
        //        jTabbedPane1.setTabPlacement(JTabbedPane.TOP);

        //        Client client = new Client(Utils.getMAIN_NAME());
        //        client.setName(clientPanelName);
        //        jTabbedPane1.add(client);
        //        JTabbedPane pane = new JTabbedPane();
        //        add(pane);
        //        jTabbedPane1.setTabComponentAt(numCl++, new ButtonTabComponent(jTabbedPane1));
        //        jTabbedPane1.add(new ButtonTabComponent(jTabbedPane1));
    /////////////////////////////////////////////////////////////////////
        //        Client client = new Client(0);
        //        client.setName("Диалог Slave " + (++numCl));
        //        listClients.add(client);
        //        jpClient.add(client);
    /////////////////////////////////////////////////////////////////////
    

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

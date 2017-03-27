package connector;

import java.awt.*;
import static java.awt.Frame.NORMAL;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Tray {

//    public JFrame frame;
    public static final String APPLICATION_NAME_SERVER = "Server";
    public static final String APPLICATION_NAME_CLIENT = "Client";
    public static final String ICON_SERVER = "images/save.png";
    public static final String ICON_CLIENT = "images/icon.png";
    private TrayIcon trayIcon;
    private SystemTray tray;   
    private Link link;
    
    
    public Tray(){
        trayIcon = null;
        tray = SystemTray.getSystemTray();
        link = null;
        
    }
//    ArrayList<Client> listClients = new ArrayList<Client>();

//    public void setTrayIcon(JFrame frame, ArrayList<Client> listClients, int conf) {
    public void setTrayIcon(JFrame frame, Client client, int conf) {     
//        this.listClients = listClients;
        if (!SystemTray.isSupported()) {
            return;
        }
        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("Выйти");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayMenu.add(item);

        MenuItem item1 = new MenuItem("Развернуть");
        item1.addActionListener(new ActionListener() {
//            SystemTray tray = SystemTray.getSystemTray();

            @Override
            public void actionPerformed(ActionEvent e) {
//                if (listClients !=null) {
////                    if (conf == 0 & listClients !=null) {
//                    link.setStop();
//                }
                
                if (client !=null) {
                    link.setStop();
                }

                frame.setVisible(true);
                frame.setState(JFrame.NORMAL);
                //frame.setExtendedState(NORMAL);
//                tray.remove(trayIcon);
                tray = SystemTray.getSystemTray();
                tray.remove(trayIcon);
            }
        });
        trayMenu.add(item1);

        URL imageURL = (conf == 1 ? Tray.class.getResource(ICON_SERVER) : Tray.class.getResource(ICON_CLIENT));

        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        //TrayIcon 
        trayIcon = new TrayIcon(icon, (conf == 1 ? APPLICATION_NAME_SERVER : APPLICATION_NAME_CLIENT), trayMenu);
        trayIcon.setImageAutoSize(true);
//        trayIcon.addMouseListener(new MouseListener(){
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (client !=null) {
//                    link.setStop();
//                }
//
//                frame.setVisible(true);
//                frame.setState(JFrame.NORMAL);
//                getTray().remove(trayIcon);
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });

        
//        if (listClients !=null) {
////            if (conf == 0 & listClients !=null) {
//            for (int i = 0; i < listClients.size(); i++) {
//                link = new Link(listClients.get(i));
//                link.start();
//            }
//        }
        
        if (client !=null) {
                link = new Link(client);
                link.start();
        }

        //SystemTray 
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private class Link extends Thread {

        Client client;
        private boolean stoped = false;

        private String msg = "";
        private String oldMsg = "Нет сообщений";

        public Link(Client client) {
            this.client = client;
        }

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            while (!stoped) {
                msg = client.getStrChat();
//                if(msg.equals(null)) {continue;}
                if (!msg.equals(oldMsg)) {
                    trayIcon.displayMessage(client.getName(), client.getReceiveStr(),
                            TrayIcon.MessageType.INFO);
                    //Toolkit.getDefaultToolkit().beep();
                    try {
                        Utils.PlaySound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                oldMsg = msg;
                try {
                    this.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}

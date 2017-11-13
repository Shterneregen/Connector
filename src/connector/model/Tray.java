package connector.model;

import connector.view.ClientPanel;
import static connector.constant.TrayType.SERVER_TRAY;
import connector.resources.ControlLines;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Tray {

    public static final String APPLICATION_NAME_SERVER = "Server";
    public static final String APPLICATION_NAME_CLIENT = "Client";
    public static final String ICON_SERVER = "../resources/images/save.png";
    public static final String ICON_CLIENT = "../resources/images/icon.png";
    private TrayIcon trayIcon;
    private SystemTray tray;
    private Link link;
    private Properties stringsFile;

    public Tray() {
        stringsFile = ProjectProperties.getInstance().getStringsFile();
        trayIcon = null;
        tray = SystemTray.getSystemTray();
        link = null;

    }
//    ArrayList<Client> listClients = new ArrayList<Client>();

//    public void setTrayIcon(JFrame frame, ArrayList<Client> listClients, int conf) {
    public void setTrayIcon(JFrame frame, ClientPanel client, int conf) {
//        this.listClients = listClients;
        if (!SystemTray.isSupported()) {
            return;
        }
        PopupMenu trayMenu = new PopupMenu();

        MenuItem item1 = new MenuItem("Развернуть");
        item1.addActionListener(new ActionListener() {
//            SystemTray tray = SystemTray.getSystemTray();

            @Override
            public void actionPerformed(ActionEvent e) {
//                if (listClients !=null) {
////                    if (conf == 0 & listClients !=null) {
//                    link.setStop();
//                }

                if (client != null) {
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

        MenuItem itemExit = new MenuItem("Выйти");
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayMenu.add(itemExit);

        URL imageURL = conf == SERVER_TRAY
                ? Tray.class.getResource(ICON_SERVER)
                : Tray.class.getResource(ICON_CLIENT);

        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        trayIcon = new TrayIcon(icon,
                (conf == SERVER_TRAY ? APPLICATION_NAME_SERVER : APPLICATION_NAME_CLIENT), trayMenu);
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
        if (client != null) {
            link = new Link(client);
            link.start();
        }

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private class Link extends Thread {

        ClientPanel client;
        private boolean stoped = false;

        private String msg = "";
        private String oldMsg = "Нет сообщений";

        public Link(ClientPanel client) {
            this.client = client;
        }

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            while (!stoped) {
                msg = client.getStrChat();
                if (!msg.equals(oldMsg)) {
                    String receiveStr;
                    if (msg.equals(ControlLines.STR_STOP_SERVER)) {
                        receiveStr = stringsFile.getProperty("stop_server");
                    } else {
                        receiveStr = client.getReceiveStr();
                    }
                    trayIcon.displayMessage(client.getName(), receiveStr,
                            TrayIcon.MessageType.INFO);
//                    Toolkit.getDefaultToolkit().beep();
                    try {
                        Utils.PlaySound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
                oldMsg = msg;
                try {
                    this.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}

package connector.model;

import connector.constant.Switch;
import connector.constant.TrayType;
import connector.view.ClientPanel;
import connector.resources.ControlLines;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Tray {

//    public static final String ICON_SERVER = "../resources/images/save.png";
//    public static final String ICON_CLIENT = "../resources/images/icon.png";
    private TrayIcon trayIcon;
    private SystemTray tray;
    private Link link;
    private Properties stringsFile;
//    ArrayList<Client> listClients = new ArrayList<Client>();

    public Tray() {
        stringsFile = ProjectProperties.getInstance().getStringsFile();
        tray = SystemTray.getSystemTray();
    }

//    public void setTrayIcon(JFrame frame, ArrayList<Client> listClients, int conf) {
    public void setTrayIcon(JFrame frame, ClientPanel client, TrayType trayType) {
//        this.listClients = listClients;
        if (!SystemTray.isSupported()) {
            if (client != null) {
                link.setStop();
            }
            frame.setVisible(true);
            frame.setState(JFrame.NORMAL);
            return;
        }
        PopupMenu trayMenu = new PopupMenu();

        MenuItem itemExtend = new MenuItem(stringsFile.getProperty("str.exit"));
        itemExtend.addActionListener((ActionEvent e) -> {
//                if (listClients !=null) {
////                    if (conf == 0 & listClients !=null) {
//                    link.setStop();
//                }
            if (client != null) {
                link.setStop();
            }

            frame.setVisible(true);
            frame.setState(JFrame.NORMAL);
            tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
        });
        trayMenu.add(itemExtend);

        MenuItem itemExit = new MenuItem(stringsFile.getProperty("str.expand"));
        itemExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        trayMenu.add(itemExit);

//        URL imageURL = Tray.class.getResource(ICON_CLIENT);
//        Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
        trayIcon = new TrayIcon(trayType.equals(TrayType.SERVER_TRAY)
                ? ProjectProperties.SERVER_IMAGE
                : ProjectProperties.CLIENT_IMAGE,
                trayType.equals(TrayType.SERVER_TRAY)
                ? ProjectProperties.SERVER_NAME_SELECT
                : ProjectProperties.CLIENT_NAME_SELECT,
                trayMenu);
        trayIcon.setImageAutoSize(true);

        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (client != null) {
                    link.setStop();
                }
                frame.setVisible(true);
                frame.setState(JFrame.NORMAL);
                tray.remove(trayIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

//        if (listClients !=null) {
////            if (conf == 0 & listClients !=null) {
//            for (int i = 0; i < listClients.size(); i++) {
//                link = new Link(listClients.get(i));
//                link.start();
//            }
//        }
        if (client != null && ProjectProperties.POP_UP_SWITCH.equals(Switch.ON)) {
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

        private ClientPanel client;
        private boolean stoped = false;

        private String msg;
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
                    String receiveStr = msg.equals(ControlLines.STR_STOP_SERVER)
                            ? stringsFile.getProperty("stop_server")
                            : client.getReceiveStr();
                    trayIcon.displayMessage(client.getName(), receiveStr, TrayIcon.MessageType.INFO);
                    if (ProjectProperties.SOUND_SWITCH.equals(Switch.ON)) {
                        try {
                            Utils.PlaySound();
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                            Toolkit.getDefaultToolkit().beep();
                            Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
                        }
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

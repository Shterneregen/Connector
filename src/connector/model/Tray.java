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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tray {

    private TrayIcon trayIcon;
    private SystemTray tray;
    private Link link;
    private ProjectProperties projectProperties;
    private Properties stringsFile;
//    ArrayList<Client> listClients = new ArrayList<Client>();

    public Tray() {
        projectProperties = ProjectProperties.getInstance();
        stringsFile = projectProperties.getStringsFile();
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
                ? projectProperties.SERVER_IMAGE
                : projectProperties.CLIENT_IMAGE,
                trayType.equals(TrayType.SERVER_TRAY)
                ? projectProperties.SERVER_NAME_SELECT
                : projectProperties.CLIENT_NAME_SELECT,
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
        if (client != null && projectProperties.POP_UP_SWITCH.equals(Switch.ON)) {
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

        private ClientPanel clientPanel;
        private boolean stoped = false;

        private String msg;
        private String oldMsg = "Нет сообщений";

        public Link(ClientPanel client) {
            this.clientPanel = client;
        }

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            while (!stoped) {
                msg = clientPanel.getStrChat();
                if (!msg.equals(oldMsg)) {
                    String receiveStr = msg.equals(ControlLines.STR_STOP_SERVER)
                            ? stringsFile.getProperty("stop_server")
                            : clientPanel.getClientController().getReceiveStr();
//                            : client.getReceiveStr();
                    trayIcon.displayMessage(clientPanel.getName(), receiveStr, TrayIcon.MessageType.INFO);
                    if (projectProperties.SOUND_SWITCH.equals(Switch.ON)) {
                        try {
                            Utils.PlaySound(projectProperties.SOUND_FILE_FILE);
                        } catch (Exception ex) {
                            Toolkit.getDefaultToolkit().beep();
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

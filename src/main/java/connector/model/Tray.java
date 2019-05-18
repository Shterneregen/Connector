package connector.model;

import connector.constant.Switch;
import connector.constant.TrayType;
import connector.controller.ClientController;
import connector.view.ClientPanel;
import connector.constant.ControlLines;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class Tray implements Observer {

    private TrayIcon trayIcon;
    private SystemTray tray;
    private ProjectProperties projectProperties;
    private Properties stringsFile;

    private ClientController clientController;
//    ArrayList<Client> listClients = new ArrayList<Client>();

    public Tray() {
        projectProperties = ProjectProperties.getInstance();
        stringsFile = projectProperties.getLangFile();
        tray = SystemTray.getSystemTray();
    }

//    public void setTrayIcon(JFrame frame, ArrayList<Client> listClients, int conf) {
    public void setTrayIcon(JFrame frame, ClientPanel client, TrayType trayType) {
//        this.listClients = listClients;
        if (!SystemTray.isSupported()) {
            frame.setVisible(true);
            frame.setState(JFrame.NORMAL);
            return;
        }
        clientController = client.getClientController();
        clientController.addObserver(this);
        PopupMenu trayMenu = new PopupMenu();

        MenuItem itemExtend = new MenuItem(stringsFile.getProperty("str.exit"));
        itemExtend.addActionListener((ActionEvent e) -> {
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
                clientController.deleteObserver(Tray.this);
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

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        if (!(o instanceof ClientController)) {
            return;
        }
        clientController = (ClientController) o;
        String msg = clientController.getReceiveStr();
//        System.out.println("msg: " + msg);

        String receiveStr = msg.equals(ControlLines.STR_STOP_SERVER)
                ? stringsFile.getProperty("stop_server")
                : msg;
        if (projectProperties.POP_UP_SWITCH.equals(Switch.ON)) {
            trayIcon.displayMessage(clientController.getNicname(), receiveStr, TrayIcon.MessageType.INFO);
        }
        if (projectProperties.SOUND_SWITCH.equals(Switch.ON)) {
            try {
                Utils.playSound(projectProperties.SOUND_FILE_FILE);
            } catch (Exception ex) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

}

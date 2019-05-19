package connector.model;

import connector.constant.ControlLines;
import connector.constant.TrayType;
import connector.controller.ClientController;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import connector.view.ClientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class Tray implements Observer {

    private TrayIcon trayIcon;
    private SystemTray tray;
    private JFrame frame;

    private ClientController clientController;

    public Tray() {
        tray = SystemTray.getSystemTray();
    }

    public void setTrayIcon(JFrame frame, ClientPanel client, TrayType trayType) {
        if (!SystemTray.isSupported()) {
            frame.setVisible(true);
            frame.setState(JFrame.NORMAL);
            return;
        }
        this.frame = frame;
        clientController = client.getClientController();
        if (clientController != null) {
            clientController.addObserver(this);
        }

        MenuItem itemExtend = new MenuItem(ProjectProperties.getString("str.expand"));
        itemExtend.addActionListener((ActionEvent e) -> {
            frame.setVisible(true);
            frame.setState(JFrame.NORMAL);
            tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
        });

        MenuItem itemExit = new MenuItem(ProjectProperties.getString("str.exit"));
        itemExit.addActionListener((ActionEvent e) -> System.exit(0));

        PopupMenu trayMenu = new PopupMenu();
        trayMenu.add(itemExtend);
        trayMenu.add(itemExit);

        Image trayImage = trayType.equals(TrayType.SERVER_TRAY)
                ? ProjectProperties.getServerTrayImage()
                : ProjectProperties.getClientTrayImage();
        String trayName = trayType.equals(TrayType.SERVER_TRAY)
                ? ProjectProperties.getServerTrayTitle()
                : ProjectProperties.getClientTrayTitle();
        trayIcon = new TrayIcon(trayImage, trayName, trayMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(getTrayMouseListener());

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

        String receiveStr = msg.equals(ControlLines.STR_STOP_SERVER)
                ? ProjectProperties.getString("stop_server")
                : msg;
        if (ProjectProperties.isPopUpOn()) {
            trayIcon.displayMessage(clientController.getNicname(), receiveStr, TrayIcon.MessageType.INFO);
        }
        if (ProjectProperties.isSoundOn()) {
            try {
                Utils.playSound(ProjectProperties.getSoundFile());
            } catch (Exception ex) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    private MouseListener getTrayMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clientController != null) {
                    clientController.deleteObserver(Tray.this);
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
        };
    }

}

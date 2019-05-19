package connector.view;

import connector.constant.ClientType;
import connector.constant.ControlLines;
import connector.constant.ServerConfig;
import connector.constant.TrayType;
import connector.model.Tray;
import connector.utils.ProjectProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ClientFrame extends javax.swing.JFrame {

    public ClientFrame(String title) {
        super(title);
        ClientPanel mainPanel = new ClientPanel(ClientType.CLIENT_WITHOUT_SERVER);

        initComponents();
        setMenuItemsNames();
        if (ProjectProperties.getClientBackground() != null) {
            BgPanel bgPanel = new BgPanel(ProjectProperties.getClientBackground());
            bgPanel.add(mainPanel);
            this.setContentPane(bgPanel);
        } else {
            JPanel bgPanel = new JPanel();
            bgPanel.add(mainPanel);
            this.setContentPane(bgPanel);
        }

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowClosing(WindowEvent event) {
                if (mainPanel.isGoodConnection()) {
                    mainPanel.sendMessage(ControlLines.STR_EXIT);
                }
                setVisible(false);
                dispose();
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {
                new Tray().setTrayIcon(ClientFrame.this, mainPanel, TrayType.CLIENT_TRAY);
                setState(JFrame.ICONIFIED);
                setVisible(false);
            }

            public void windowOpened(WindowEvent event) {
            }
        });
        pack();
    }

    private void setMenuItemsNames() {
        jmFile.setText(ProjectProperties.getString("clientFrame.jm.file"));
        jmiExit.setText(ProjectProperties.getString("clientFrame.jmi.exit"));

        jmClient.setText(ProjectProperties.getString("clientFrame.jm.client"));
        jmiNewClientWindow.setText(ProjectProperties.getString("clientFrame.jmi.newClientWindow"));

        jmServer.setText(ProjectProperties.getString("clientFrame.jm.server"));
        jmiNewServerWindow.setText(ProjectProperties.getString("clientFrame.jmi.newServerWindow"));
    }

    class BgPanel extends JPanel {

        Image background;

        BgPanel(Image background) {
            this.background = background;
        }

        public void paintComponent(Graphics g) {
            g.drawImage(background, 0, 0, null);
            repaint();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        jMenuBar1 = new JMenuBar();
        jmFile = new JMenu();
        jmiExit = new JMenuItem();
        jmClient = new JMenu();
        jmiNewClientWindow = new JMenuItem();
        jmServer = new JMenu();
        jmiNewServerWindow = new JMenuItem();

        setDefaultCloseOperation(0);
        setName("fClient");
        Container contentPane = getContentPane();

        jMenuBar1.setOpaque(false);

        jmiExit.addActionListener(evt -> jmiExitActionPerformed());
        jmFile.add(jmiExit);
        jMenuBar1.add(jmFile);

        jmiNewClientWindow.addActionListener(evt -> jmiNewClientWindowActionPerformed());
        jmClient.add(jmiNewClientWindow);

        jMenuBar1.add(jmClient);

        jmiNewServerWindow.addActionListener(evt -> jmiNewServerWindowActionPerformed());
        jmServer.add(jmiNewServerWindow);
        jMenuBar1.add(jmServer);
        setJMenuBar(jMenuBar1);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGap(0, 449, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGap(0, 463, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
    }
    // </editor-fold>

    private void jmiNewServerWindowActionPerformed() {
        ServerFrame server = new ServerFrame(ControlLines.MAIN_NAME, ServerConfig.ONLY_SERVER);
        server.setVisible(true);
    }

    private void jmiNewClientWindowActionPerformed() {
        ClientFrame client = new ClientFrame(ControlLines.MAIN_NAME);
        client.setVisible(true);
    }

    private void jmiExitActionPerformed() {
        System.exit(0);
    }

    // Variables declaration - do not modify
    private JMenuBar jMenuBar1;
    private JMenu jmFile;
    private JMenuItem jmiExit;
    private JMenu jmClient;
    private JMenuItem jmiNewClientWindow;
    private JMenu jmServer;
    private JMenuItem jmiNewServerWindow;
    // End of variables declaration
}

package connector.view;

import connector.constant.ControlLines;
import connector.constant.ServerConfig;
import connector.constant.TrayType;
import connector.controller.ServerController;
import connector.model.Tray;
import connector.utils.ProjectProperties;
import connector.utils.Utils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Optional;

class ServerFrame extends JFrame {

    private ServerController serverController;

    ServerFrame(String frameName, ServerConfig serverConfig) {
        super(frameName);

        initComponents();
        setItemsNames();
        ((AbstractDocument) tfPort.getDocument()).setDocumentFilter(new Utils().new DigitsFilter());
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowClosing(WindowEvent event) {
                stopServer();

                if (serverConfig.equals(ServerConfig.ONLY_SERVER)) {
                    System.exit(0);
                } else {
                    setVisible(false);
                    dispose();
                }
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {
                new Tray().setTrayIcon(ServerFrame.this, null, TrayType.SERVER_TRAY);
                setVisible(false);
            }

            public void windowOpened(WindowEvent event) {

            }
        });
    }

    private void setItemsNames() {
        ArrayList<String> listAddr = Utils.getLocalIpList();
        for (String aListAddr : listAddr) {
            jcbIP.addItem(aListAddr);
        }
        jmClient.setText(ProjectProperties.getString("clientFrame.jm.client"));
        jmiNewClientWindow.setText(ProjectProperties.getString("clientFrame.jmi.newClientWindow"));

        jmServer.setText(ProjectProperties.getString("clientFrame.jm.server"));
        jmiNewServerWindow.setText(ProjectProperties.getString("clientFrame.jmi.newServerWindow"));

        lbUsers.setText(ProjectProperties.getString("lb.users"));
        lbNumUs.setText("0");

        lbPort.setText(ProjectProperties.getString("serverFrame.lb.port"));
        lbPass.setText(ProjectProperties.getString("serverFrame.lb.pass"));
        lbIP.setText(ProjectProperties.getString("serverFrame.lb.ip"));

        btStartServer.setText(ProjectProperties.getString("serverFrame.button.startServer"));
        btStopServer.setText(ProjectProperties.getString("serverFrame.button.stopServer"));
        btStopServer.setEnabled(false);

        tfPort.setText(ProjectProperties.getString("str.defaultPort"));
        pfPas.setText(ProjectProperties.getString("str.defaultPsw"));
    }

    private void startServer(String port, String psw) {
        Optional<Integer> checkPort = Utils.getAndCheckPort(port);
        Optional<String> checkPsw = checkPsw(psw);
        if (checkPort.isPresent() && checkPsw.isPresent()) {
            btStartServer.setEnabled(false);
            btStopServer.setEnabled(true);

            tfPort.setEditable(false);
            pfPas.setEditable(false);

            serverController = new ServerController(port, psw);
            serverController.startServer();
        } else {
            String errorPort = !checkPort.isPresent()
                    ? ProjectProperties.getString("wrong_port") + "; "
                    : "";

            String errorPsw = !checkPsw.isPresent()
                    ? ProjectProperties.getString("tf.enter_pass") + "; "
                    : "";

            lbNumUs.setText(errorPort + errorPsw);
        }
    }

    private void stopServer() {
        if (serverController != null) {
            serverController.stopServer();
        }
        btStartServer.setEnabled(true);
        btStopServer.setEnabled(false);

        tfPort.setEditable(true);
        pfPas.setEditable(true);

        lbNumUs.setText("0");
    }

    private Optional<String> checkPsw(String psw) {
        return psw == null || psw.equals("")
                ? Optional.empty()
                : Optional.of(psw);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        tfPort = new JTextField();
        btStartServer = new JButton();
        btStopServer = new JButton();
        lbIP = new JLabel();
        lbNumUs = new JLabel();
        pfPas = new JPasswordField();
        jcbIP = new JComboBox<>();
        lbPort = new JLabel();
        lbPass = new JLabel();
        lbUsers = new JLabel();
        jMenuBar1 = new JMenuBar();
        jmServer = new JMenu();
        jmiNewServerWindow = new JMenuItem();
        jmClient = new JMenu();
        jmiNewClientWindow = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        btStartServer.setText("Start server");
        btStartServer.addActionListener(evt -> startServer());

        btStopServer.setText("Stop server");
        btStopServer.addActionListener(evt -> stopServer());

        lbIP.setText("IP address:");

        lbNumUs.setText(" ");

        pfPas.setText("9988");

        lbPort.setText("Port");
        lbPass.setText("Password");
        lbUsers.setText(" ");
        jmServer.setText("Server");

        jmiNewServerWindow.setText("New server window");
        jmiNewServerWindow.addActionListener(evt1 -> jmiNewServerWindowActionPerformed());
        jmServer.add(jmiNewServerWindow);

        jMenuBar1.add(jmServer);

        jmClient.setText("Client");

        jmiNewClientWindow.setText("New client window");
        jmiNewClientWindow.addActionListener(evt -> jmiNewClientWindowActionPerformed());
        jmClient.add(jmiNewClientWindow);

        jMenuBar1.add(jmClient);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lbUsers, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbIP, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbPort, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tfPort, GroupLayout.Alignment.TRAILING)
                                        .addComponent(btStartServer, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btStopServer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(pfPas)
                                                        .addComponent(lbPass, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addComponent(lbNumUs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(jcbIP, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbPort)
                                        .addComponent(lbPass, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tfPort)
                                        .addComponent(pfPas))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btStartServer)
                                        .addComponent(btStopServer))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbIP, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jcbIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbNumUs)
                                        .addComponent(lbUsers))
                                .addContainerGap())
        );

        tfPort.getAccessibleContext().setAccessibleName("");

        pack();
    }

    private void startServer() {
        startServer(tfPort.getText(), String.valueOf(pfPas.getPassword()));
    }

    private void jmiNewClientWindowActionPerformed() {
        ClientFrame client = new ClientFrame(ControlLines.MAIN_NAME);
        client.setVisible(true);
    }

    private void jmiNewServerWindowActionPerformed() {
        ServerFrame newServer = new ServerFrame(ControlLines.MAIN_NAME, ServerConfig.ONLY_SERVER);
        newServer.setVisible(true);
    }

    // Variables declaration - do not modify
    private JButton btStartServer;
    private JButton btStopServer;
    private JMenuBar jMenuBar1;
    private JComboBox<String> jcbIP;
    private JMenu jmClient;
    private JMenu jmServer;
    private JMenuItem jmiNewClientWindow;
    private JMenuItem jmiNewServerWindow;
    private JLabel lbIP;
    private JLabel lbNumUs;
    private JLabel lbPass;
    private JLabel lbPort;
    private JLabel lbUsers;
    private JPasswordField pfPas;
    private JTextField tfPort;
    // End of variables declaration

}

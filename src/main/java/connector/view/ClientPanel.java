package connector.view;

import connector.constant.ClientType;
import connector.constant.ControlLines;
import connector.controller.ClientController;
import connector.utils.DigitsFilter;
import connector.utils.NetUtils;
import connector.utils.ProjectProperties;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientPanel extends JPanel implements Observer {

    private ClientController clientController;

    private String strChat;
    private ClientType clientType;
    private Boolean goodConnection;
    private List<String> localIpList;

    ClientPanel(ClientType clientType) {
        this.clientType = clientType;

        goodConnection = false;
        localIpList = NetUtils.getLocalIpList();

        initComponents();
        setItemsNames();
        localIpList.forEach(address -> tfIP.addItem(address));
        if (clientType.equals(ClientType.CLIENT_WITH_SERVER)) {
            btStartClient.setText(ProjectProperties.getString("clientPanel.button.create_conversation"));
            btStopClient.setText(ProjectProperties.getString("clientPanel.button.stop_conversation"));
            tfIP.setEditable(false);
        } else if (clientType.equals(ClientType.CLIENT_WITHOUT_SERVER)) {
            btStartClient.setText(ProjectProperties.getString("clientPanel.button.join"));
            btStopClient.setText(ProjectProperties.getString("str.exit"));
        }
        ((AbstractDocument) tfPort.getDocument()).setDocumentFilter(new DigitsFilter());

        tpOutput.setWrapStyleWord(true);
        tpOutput.setLineWrap(true);
        tpOutput.setEditable(false);
        tfInput.setText(ProjectProperties.getString("no_conn"));
        tfInput.setEditable(false);
        btStopClient.setEnabled(false);
        btSent.setEnabled(false);

        cbNewConversation.addItemListener(e -> {
            if (cbNewConversation.isSelected()) {
                ClientPanel.this.clientType = ClientType.CLIENT_WITH_SERVER;
                btStartClient.setText(ProjectProperties.getString("clientPanel.button.create_conversation"));
                btStopClient.setText(ProjectProperties.getString("clientPanel.button.stop_conversation"));
                tfIP.setEditable(false);
            } else {
                ClientPanel.this.clientType = ClientType.CLIENT_WITHOUT_SERVER;
                btStartClient.setText(ProjectProperties.getString("clientPanel.button.join"));
                btStopClient.setText(ProjectProperties.getString("str.exit"));
                tfIP.setEditable(true);
            }
            tfIP.removeAllItems();
            localIpList.forEach(address -> tfIP.addItem(address));
        });
    }

    void sendMessage(String message) {
        if (!message.replaceAll("\\s+", "").equals("")) {
            try {
                clientController.sendMessage(message);
            } catch (IOException ex) {
                Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void exit() {
        setButtonBeforeStart();
        goodConnection = false;
    }

    private void setButtonAfterStart() {
        btStartClient.setEnabled(false);
        btStopClient.setEnabled(true);
        btSent.setEnabled(true);

        if (clientType.equals(ClientType.CLIENT_WITHOUT_SERVER)) {
            tfIP.setEditable(false);
        }
        tfPort.setEditable(false);
        tfNic.setEditable(false);
        pfPas.setEditable(false);

        tfInput.setEditable(true);
        tfInput.setText("");

        cbNewConversation.setEnabled(false);
    }

    private void setButtonBeforeStart() {
        btStartClient.setEnabled(true);
        btStopClient.setEnabled(false);
        btSent.setEnabled(false);

        if (clientType.equals(ClientType.CLIENT_WITHOUT_SERVER)) {
            tfIP.setEditable(true);
        }
        tfPort.setEditable(true);
        tfNic.setEditable(true);
        pfPas.setEditable(true);

        tfInput.setEditable(false);
        tfInput.setText(ProjectProperties.getString("no_conn"));

        cbNewConversation.setEnabled(true);
    }

    private void setItemsNames() {
        lbIP.setText(ProjectProperties.getString("serverFrame.lb.ip"));
        lbPort.setText(ProjectProperties.getString("serverFrame.lb.port"));
        lbNic.setText(ProjectProperties.getString("lb.nic"));
        lbPass.setText(ProjectProperties.getString("serverFrame.lb.pass"));
        cbNewConversation.setText(ProjectProperties.getString("clientPanel.cb.newConversation"));

        tfNic.setText(ProjectProperties.getString("str.defaultNicName"));
        tfPort.setText(ProjectProperties.getString("str.defaultPort"));
        pfPas.setText(ProjectProperties.getString("str.defaultPsw"));
    }

    private String getStrChat() {
        return strChat;
    }

    Boolean isGoodConnection() {
        return goodConnection;
    }

    private JTextArea getTpOutput() {
        return tpOutput;
    }

    private void setStrChat(String strChat) {
        this.strChat = strChat;
    }

    public ClientController getClientController() {
        return clientController;
    }

    @Override
    public void update(Observable o, Object o1) {
        if (!(o instanceof ClientController)) {
            return;
        }
        clientController = (ClientController) o;
        String receiveStr = clientController.getReceiveStr();

        String commandToMsg = "";

        switch (receiveStr) {
            case ControlLines.STR_WRONG_PASS:
                commandToMsg = ProjectProperties.getString("wrong_pass");
                break;
            case ControlLines.STR_SAME_NIC:
                commandToMsg = ProjectProperties.getString("same_nic");
                break;
            case ControlLines.STR_STOP_SERVER:
                commandToMsg = ProjectProperties.getString("stop_server");
                break;
            default:
                break;
        }

        switch (receiveStr) {
            case ControlLines.STR_WRONG_PASS:
            case ControlLines.STR_SAME_NIC:
            case ControlLines.STR_STOP_SERVER:
                this.setStrChat(this.getStrChat() + "\n" + receiveStr);
                this.getTpOutput().append(commandToMsg + "\n");
                this.getTpOutput().setCaretPosition(this.getTpOutput().getText().length());
                if (receiveStr.equals(ControlLines.STR_STOP_SERVER)) {
                    clientController.stopReceiver();
                }
                this.exit();
                break;
            default:
                this.setStrChat(this.getStrChat() + "\n" + receiveStr);
                if (clientController.getMessage().isNotSystemMessage()) {
                    this.getTpOutput().append(receiveStr + "\n");
                    this.getTpOutput().setCaretPosition(this.getTpOutput().getText().length());
                }
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        tfInput = new JTextField();
        btSent = new JButton();
        jSplitPane1 = new JSplitPane();
        pSetCon = new JPanel();
        tfPort = new JTextField();
        pfPas = new JPasswordField();
        tfNic = new JTextField();
        tfIP = new JComboBox<>();
        lbIP = new JLabel();
        lbPort = new JLabel();
        lbNic = new JLabel();
        lbPass = new JLabel();
        cbNewConversation = new JCheckBox();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        tpOutput = new JTextArea();
        btStartClient = new JButton();
        btStopClient = new JButton();

        setOpaque(false);

        tfInput.setOpaque(false);
        tfInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfInputKeyPressed(evt);
            }
        });

        btSent.setText(">");
        btSent.setAlignmentY(0.0F);
        btSent.setFocusable(false);
        btSent.addActionListener(evt -> sendMessage());

        jSplitPane1.setBorder(null);
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setOpaque(false);

        pSetCon.setBorder(BorderFactory.createEtchedBorder());
        pSetCon.setOpaque(false);

        tfPort.setMinimumSize(new Dimension(0, 0));
        tfPort.setOpaque(false);

        pfPas.setMinimumSize(new Dimension(0, 0));
        pfPas.setOpaque(false);

        tfNic.setMinimumSize(new Dimension(0, 0));
        tfNic.setOpaque(false);

        tfIP.setEditable(true);
        tfIP.setOpaque(false);

        cbNewConversation.setOpaque(false);

        GroupLayout pSetConLayout = new GroupLayout(pSetCon);
        pSetCon.setLayout(pSetConLayout);
        pSetConLayout.setHorizontalGroup(
                pSetConLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pSetConLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pSetConLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pSetConLayout.createSequentialGroup()
                                                .addGroup(pSetConLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(pSetConLayout.createSequentialGroup()
                                                                .addComponent(lbIP)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfIP, 0, 0, Short.MAX_VALUE))
                                                        .addGroup(pSetConLayout.createSequentialGroup()
                                                                .addComponent(lbNic)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(tfNic, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(pSetConLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(pSetConLayout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(lbPass)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(pfPas, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, pSetConLayout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(lbPort)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfPort, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(pSetConLayout.createSequentialGroup()
                                                .addComponent(cbNewConversation)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        pSetConLayout.setVerticalGroup(
                pSetConLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pSetConLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pSetConLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pSetConLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(lbIP)
                                                .addComponent(lbPort)
                                                .addComponent(tfPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(tfIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pSetConLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbNic)
                                        .addComponent(tfNic, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbPass)
                                        .addComponent(pfPas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(cbNewConversation)
                                .addGap(0, 0, 0))
        );

        jSplitPane1.setTopComponent(pSetCon);

        jPanel2.setOpaque(false);

        jScrollPane1.setOpaque(false);

        tpOutput.setColumns(20);
        tpOutput.setRows(5);
        tpOutput.setOpaque(false);
        jScrollPane1.setViewportView(tpOutput);

        btStartClient.addActionListener(evt -> startClient());
        btStopClient.addActionListener(evt -> stopClient());

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btStartClient, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btStopClient, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btStopClient, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(btStartClient, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tfInput)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btSent, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jSplitPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSplitPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(tfInput, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btSent))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private void startClient() {
        clientController = new ClientController(clientType);
        clientController.addObserver(this);
        boolean isConnection = clientController.setConnection(
                (String) tfIP.getSelectedItem(),
                tfPort.getText(),
                tfNic.getText(),
                String.valueOf(pfPas.getPassword()));
        if (isConnection) {
            strChat = "";
            goodConnection = true;
            setButtonAfterStart();
        } else {
            tpOutput.append(ProjectProperties.getString("STR_NON_ACK") + "\n");
            goodConnection = false;
            exit();
        }
    }

    private void stopClient() {
        clientController.disconnect();
        if (!clientType.equals(ClientType.CLIENT_WITH_SERVER)) {
            tpOutput.append(ProjectProperties.getString("you_exit") + "\n");
            sendMessage(ControlLines.STR_EXIT);
            exit();
        }
//        clientController.deleteObserver(this);
    }

    private void tfInputKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            sendMessage(tfInput.getText());
            tfInput.setText("");
        }
    }

    private void sendMessage() {
        sendMessage(tfInput.getText());
        tfInput.setText("");
    }

    // Variables declaration - do not modify
    private JButton btSent;
    private JButton btStartClient;
    private JButton btStopClient;
    private JCheckBox cbNewConversation;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JSplitPane jSplitPane1;
    private JLabel lbIP;
    private JLabel lbNic;
    private JLabel lbPass;
    private JLabel lbPort;
    private JPanel pSetCon;
    private JPasswordField pfPas;
    private JComboBox<String> tfIP;
    private JTextField tfInput;
    private JTextField tfNic;
    private JTextField tfPort;
    private JTextArea tpOutput;
    // End of variables declaration

}

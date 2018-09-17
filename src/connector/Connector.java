package connector;

import connector.constant.ClientType;
import connector.controller.ClientController;
import connector.controller.ServerController;
import connector.resources.ControlLines;
import connector.utils.ProjectProperties;
import connector.utils.Utils;
import connector.view.ClientFrame;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Connector {

    private static ClientFrame clientFrame;
    private static Logger log = Logger.getLogger(Connector.class.getName());
    private static String encoding;

    public static void main(String[] args) {
        encoding = System.getProperty("console.encoding", "Cp866");
        ProjectProperties pp = ProjectProperties.getInstance();

        if (args.length > 0) {
            List<String> argList = new ArrayList<>(Arrays.asList(args));
            String mode = argList.get(0);

            if (mode.equals("-h") || mode.equals("-help") || mode.equals("/?") || mode.equals("?")) {
                System.out.println("java -jar Connector.jar -s [port] [psw]");
                System.out.println("java -jar Connector.jar -c [port] [psw] [nickname]");
                System.out.println("-s\t" + ProjectProperties.getString("server.about"));
                System.out.println("-c\t" + ProjectProperties.getString("client.about"));
                return;
            }

            String port;
            String psw;

            if (argList.size() > 1) {
                port = argList.get(1);
                psw = argList.size() > 2
                        ? argList.get(2)
                        : getPassword();
            } else {
                Scanner in = new Scanner(System.in, encoding);
                System.out.print(ProjectProperties.getString("tf.enter_port") + ": ");
                port = in.nextLine();
                psw = getPassword();
            }

            if (mode.equals("-s")) {
                List<String> listAddr = Utils.getMyLocalIP();
                listAddr.forEach(i -> System.out.println(i));
                startServer(port, psw);
            } else if (mode.equals("-c")) {
                String nic;
                if (argList.size() > 3) {
                    nic = argList.get(3);
                } else {
                    Scanner in = new Scanner(System.in, encoding);
                    System.out.print(ProjectProperties.getString("tf.enter_nic") + ": ");
                    nic = in.nextLine();
                }
                ClientController clientController = new ClientController(ClientType.CLIENT_WITHOUT_SERVER);
                boolean isConnection = clientController.setConnection("127.0.0.1", port, nic, psw);

                if (isConnection) {
                    Sender sender = new Sender(clientController);
                    sender.start();
                }
            }
        } else {
            clientFrame = new ClientFrame(ControlLines.MAIN_NAME + " main");
            clientFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            clientFrame.setVisible(true);

//            new ClientFrame(ControlLines.MAIN_NAME).setVisible(true);
//            new ServerFrame("Server", ServerConfig.ONLY_SERVER).setVisible(true);
        }
    }

    private static String getPassword() {
        Console cnsl = System.console();
        if (cnsl != null) {
            char[] pwd = cnsl.readPassword(ProjectProperties.getString("tf.enter_pass") + ": ");
            return String.valueOf(pwd);
        }
        throw new RuntimeException("Cannot read console");
    }

    private static void clientSendMsg(ClientController clientController, String message) {
        if (!message.replaceAll("\\s+", "").equals("")) {
            try {
                clientController.sendMsg(message);
            } catch (IOException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void startServer(String port, String psw) {
        ServerController serverController = new ServerController(port, psw);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                serverController.stopServer();
            }
        });
        serverController.startServer();
    }

    private static class Sender extends Thread {

        ClientController clientController;

        private boolean stoped = false;

        public Sender(ClientController clientController) {
            this.clientController = clientController;
        }

        public void setStop() {
            stoped = true;
        }

        @Override
        public void run() {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    clientController.disonnect();
                }
            });
            while (!stoped) {
                Scanner in = new Scanner(System.in, encoding);
                if (!in.hasNextLine()) {
                    clientController.disonnect();
                    stoped = true;
                    return;
                }
                String msg = in.nextLine();
                clientSendMsg(clientController, msg);
            }
        }
    }
}

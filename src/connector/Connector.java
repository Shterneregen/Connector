package connector;

import connector.controller.ServerController;
import connector.resources.ControlLines;
import connector.utils.ProjectProperties;
import connector.view.ClientFrame;
import java.util.Scanner;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Connector {

    private static ClientFrame clientFrame;
    private ProjectProperties projectProperties = ProjectProperties.getInstance();

    public static void main(String[] args) {
        if (args.length > 0) {
            String port = args[0];
            startServer(port);
        } else {
            clientFrame = new ClientFrame(ControlLines.MAIN_NAME + " main");
            clientFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            clientFrame.setVisible(true);

//            new ClientFrame(ControlLines.MAIN_NAME).setVisible(true);
//            new ServerFrame("Server", ServerConfig.ONLY_SERVER).setVisible(true);
        }
    }

    private static void startServer(String port) {
        Scanner in = new Scanner(System.in);

        System.out.print("Введите пароль: ");
        String psw = in.nextLine();

        ServerController serverController = new ServerController(port, psw);
        serverController.startServer();
    }
}

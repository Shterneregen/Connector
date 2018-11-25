package connector;

import connector.resources.ControlLines;
import connector.view.ClientFrame;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Connector {

    private static ClientFrame clientFrame;
    private static Logger log = Logger.getLogger(Connector.class.getName());

    public static void main(String[] args) {
        if (args.length > 0) {
            ConsoleMode.launch(args);
        } else {
            clientFrame = new ClientFrame(ControlLines.MAIN_NAME + " main");
            clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientFrame.setVisible(true);

//            new ClientFrame(ControlLines.MAIN_NAME).setVisible(true);
//            new ServerFrame("Server", ServerConfig.ONLY_SERVER).setVisible(true);
        }
    }
}

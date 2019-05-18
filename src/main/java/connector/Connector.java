package connector;

import connector.resources.ControlLines;
import connector.view.ClientFrame;

import java.util.logging.Logger;
import javax.swing.JFrame;

public class Connector {

    public static void main(String[] args) {
        if (args.length > 0) {
            ConsoleMode.launch(args);
        } else {
            ClientFrame clientFrame = new ClientFrame(ControlLines.MAIN_NAME + " main");
            clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientFrame.setVisible(true);
        }
    }
}

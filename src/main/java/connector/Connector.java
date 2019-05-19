package connector;

import connector.constant.ControlLines;
import connector.utils.ProjectProperties;
import connector.view.ClientFrame;

import javax.swing.JFrame;

public class Connector {

    public static void main(String[] args) {
        ProjectProperties.refreshProjectProperties();
        if (args.length > 0) {
            ConsoleMode.launch(args);
        } else {
            ClientFrame clientFrame = new ClientFrame(ControlLines.MAIN_NAME + " main");
            clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientFrame.setVisible(true);
        }
    }
}

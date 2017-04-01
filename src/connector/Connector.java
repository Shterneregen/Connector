package connector;

import connector.resources.ControlLines;
import connector.utils.ProjectProperties;
import connector.view.ClientFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Connector {

    private static ClientFrame clientFrame;
//    private ProjectProperties projectProperties = ProjectProperties.getInstance();
    public static void main(String[] args) {
        clientFrame = new ClientFrame(ControlLines.MAIN_NAME+" main");
        clientFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
        
//        new ClientFrame(ControlLines.MAIN_NAME).setVisible(true);
//        new ClientFrame(Strings.getMAIN_NAME()).setVisible(true);
    }
}

package connector;

import connector.view.ClientFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Connector {

    private static ClientFrame clientFrame;
    public static void main(String[] args) {
        clientFrame = new ClientFrame(Strings.MAIN_NAME+" main");
        clientFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
        
        new ClientFrame(Strings.MAIN_NAME).setVisible(true);
//        new ClientFrame(Strings.getMAIN_NAME()).setVisible(true);
    }
}

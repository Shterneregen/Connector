/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.model;

/**
 *
 * @author Yura
 */
public class ServerManager {

    private Server serverThread;
    private Boolean isStartServer = false;

    public void createServer(int port, String psw) {
        serverThread = new Server(port, psw);
        isStartServer = true;
        serverThread.start();
    }

    public void stopServer() {
        isStartServer = false;
        serverThread.stopServer();
    }

    public Boolean getIsStartServer() {
        return isStartServer;
    }

    public void setIsStartServer(Boolean isStartServer) {
        this.isStartServer = isStartServer;
    }

}

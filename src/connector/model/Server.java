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
public class Server {

    private ServerThread serverThread;
    private Boolean isStartServer = false;
//    private int conf;

    public void createServerThreadAndStart(int port, String psw) {
        serverThread = new ServerThread(port, psw);
        isStartServer = true;
        serverThread.start();
    }

    public void setStopServerThread() {
        serverThread.setStop();
    }

    public void closeAllServerConnection() {
        serverThread.closeAll();
    }

//    public int getPort() {
//        return port;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }

    public ServerThread getServerThread() {
        return serverThread;
    }

    public Boolean getIsStartServer() {
        return isStartServer;
    }

    public void setIsStartServer(Boolean isStartServer) {
        this.isStartServer = isStartServer;
    }

//    public int getConf() {
//        return conf;
//    }
//
//    public void setConf(int conf) {
//        this.conf = conf;
//    }
}

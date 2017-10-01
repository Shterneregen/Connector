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

    private int port;
    private ServerThread serverThread;
    private String pfStr;
    private Boolean isStartServer = false;
    private int conf;

//    public Server() {
////        port = 9988;
////        pfStr = "9988";
//    }

    public void createServerThreadAndStart() {
        serverThread = new ServerThread(port);
        isStartServer = true;
        serverThread.start();
    }

//    public void startServer() {
//        serverThread.start();
//    }

    public void setStopServerThread() {
        serverThread.setStop();
    }

    public void closeAllServerConnection() {
        serverThread.closeAll();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ServerThread getServerThread() {
        return serverThread;
    }

//    public void setServerThread(ServerThread serverThread) {
//        this.serverThread = serverThread;
//    }

    public String getPfStr() {
        return pfStr;
    }

    public void setPfStr(String pfStr) {
        this.pfStr = pfStr;
    }

    public Boolean getIsStartServer() {
        return isStartServer;
    }

    public void setIsStartServer(Boolean isStartServer) {
        this.isStartServer = isStartServer;
    }

    public int getConf() {
        return conf;
    }

    public void setConf(int conf) {
        this.conf = conf;
    }
}

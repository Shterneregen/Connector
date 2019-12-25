package connector.controller;

import connector.utils.Encryption;
import connector.utils.NetUtils;
import connector.utils.Utils;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerController extends Thread {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    private int port;
    private String psw;
    private ServerSocket serverSocket;
    private Encryption serverEncryption;
    private boolean stop = false;

    public ServerController(String port, String psw) {
        Optional<Integer> checkPort = NetUtils.getAndCheckPort(port);
        if (checkPort.isPresent()) {
            this.port = checkPort.get();
            this.psw = psw;
            serverEncryption = new Encryption();
        }
    }

    public void startServer() {
        this.start();
    }

    public void stopServer() {
        ConnectionController.stopServerNotification();
        this.setStop();
        Utils.close(serverSocket);
    }

    public void setStop() {
        stop = true;
    }

    @Override
    public void run() {
        LOG.info("Server is started!");

        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (!stop) {
                socket = serverSocket.accept();
                if (stop) {
                    break;
                }
                ConnectionController con = new ConnectionController(socket, serverEncryption, psw);
                ConnectionController.getConnections().add(con);
                con.start();
            }
        } catch (SocketException se) {
            LOG.warning("Main SocketException");
            LOG.log(Level.SEVERE, se.getMessage(), se);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            Utils.close(serverSocket);
            Utils.close(socket);
        }
    }

}

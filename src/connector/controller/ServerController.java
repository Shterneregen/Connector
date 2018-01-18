/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.controller;

import connector.utils.Encryption;
import connector.utils.Utils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yura
 */
public class ServerController extends Thread {

    private int port;
    private String psw;
    private ServerSocket serverSocket;
    private Encryption serverEncryption;
    private boolean stoped = false;

    public ServerController(String port, String psw) {
        Optional<Integer> checkPort = Utils.getAndCheckPort(port);
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
        ConnectionController.closeAllConnections();
        this.setStop();
        this.closeServerSocket();
    }

    //Прекращает пересылку сообщений
    public void setStop() {
        stoped = true;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (!stoped) {
                socket = serverSocket.accept();
                if (stoped) {
                    break;
                }
                ConnectionController con = new ConnectionController(socket, serverEncryption, psw);
                ConnectionController.getConnections().add(con);
                con.start();
            }
        } catch (SocketException se) {
            System.out.println("Main SocketException done");
        } catch (IOException e) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void closeServerSocket() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

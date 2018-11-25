/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.model;

import connector.utils.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yura
 */
public class Client {

    private Socket socket;
    private int port;
    private String ip;
    private String nicname;
    private String psw;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Client(String ip, int port, String nicname, String psw) {
        this.ip = ip;
        this.port = port;
        this.nicname = nicname;
        this.psw = psw;

        try {
            socket = new Socket(ip, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            closeStreams();
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeStreams() {
        Utils.close(inputStream);
        Utils.close(outputStream);
        Utils.close(socket);
    }

    //<editor-fold defaultstate="collapsed" desc="get-set">
    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public String getNicname() {
        return nicname;
    }

    public String getPsw() {
        return psw;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
    //</editor-fold>

}

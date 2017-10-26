/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector.model;

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

    public Client(int port, String ip, String nicname, String psw) {
        this.port = port;
        this.ip = ip;
        this.nicname = nicname;
        this.psw = psw;
        initSocket();

//        try {
//            socket = new Socket(ip, port);
//            outputStream = new ObjectOutputStream(socket.getOutputStream());
//            inputStream = new ObjectInputStream(socket.getInputStream());
//        } catch (IOException ex) {
//            closeStreams();
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void initSocket() {
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStreams() {
        try {
            outputStream = new ObjectOutputStream(this.getSocket().getOutputStream());
            inputStream = new ObjectInputStream(this.getSocket().getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeStreams() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ioe) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ioe);
        }
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream.flush();
            }
        } catch (IOException ioe) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ioe);
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get-set">
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNicname() {
        return nicname;
    }

    public void setNicname(String nicname) {
        this.nicname = nicname;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }
    //</editor-fold>

}

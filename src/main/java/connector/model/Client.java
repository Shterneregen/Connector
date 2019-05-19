package connector.model;

import connector.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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

}

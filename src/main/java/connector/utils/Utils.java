package connector.utils;

import javax.sound.sampled.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public class DigitsFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

            StringBuilder buffer = new StringBuilder(string);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                char ch = buffer.charAt(i);
                if (!Character.isDigit(ch)) {
                    buffer.deleteCharAt(i);
                }
            }
            string = buffer.toString();
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            StringBuffer buffer;
            if (string != null) {
                buffer = new StringBuffer(string);
                for (int i = buffer.length() - 1; i >= 0; i--) {

                    char ch = buffer.charAt(i);
                    if (!Character.isDigit(ch)) {
                        buffer.deleteCharAt(i);
                    }
                }
                string = buffer.toString();
            }
            super.replace(fb, offset, length, string, attrs);
        }
    }

    public static void playSound(File soundFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat()); // получаем информацию о звуке из потока
        Clip clip = (Clip) AudioSystem.getLine(info); // инициализируем проигрыватель
        clip.open(stream);
        clip.start();
    }

    public static String removeTheTrash(String s) {
        char[] buf = new char[1024];
        int length = s.length();
        char[] oldChars = (length < 1024) ? buf : new char[length];
        s.getChars(0, length, oldChars, 0);
        int newLen = 0;
        for (int j = 0; j < length; j++) {
            char ch = oldChars[j];
            if (ch >= ' ') {
                oldChars[newLen] = ch;
                newLen++;
            }
        }
        if (newLen != length) {
            s = new String(oldChars, 0, newLen);
        }
        return s;
    }

    private static String getInterfaceInfo(NetworkInterface nif) {
        String ipAddress = "";
        Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddr = inetAddresses.nextElement();
            if (inetAddr instanceof Inet4Address) {
                ipAddress = inetAddr.getHostAddress();
            }
        }
        return ipAddress;
    }

    public static ArrayList<String> getLocalIpList() {
        ArrayList<String> listAddr = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nif = interfaces.nextElement();

                if (!getInterfaceInfo(nif).equals("")) {
                    listAddr.add(getInterfaceInfo(nif));
                }
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return listAddr;
    }

    public static Optional<Integer> getAndCheckPort(String strPort) {
        try {
            int port = Integer.parseInt(strPort);
            return (port <= 0 || port > 65535)
                    ? Optional.empty()
                    : Optional.of(port);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isNotGoodIpOctet(String string) {
        return !isInteger(string) || Integer.parseInt(string) < 0 || Integer.parseInt(string) >= 256;
    }

    public static Optional<String> getAndCheckIP(String ip) {
        char[] chArr = ip.toCharArray();
        StringBuilder ip_1 = new StringBuilder();
        StringBuilder ip_2 = new StringBuilder();
        StringBuilder ip_3 = new StringBuilder();
        StringBuilder ip_4 = new StringBuilder();
        int i = 0;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_1.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ip_1.toString())) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_2.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ip_2.toString())) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_3.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ip_3.toString())) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            ip_4.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ip_4.toString())) {
            return Optional.empty();
        }
        return Optional.of(ip);
    }

    public static String getCurrentTime() {
        long curTime = System.currentTimeMillis();
        return new SimpleDateFormat("kk:mm:ss").format(curTime);
    }

    public static String getCurrentDate() {
        long curTime = System.currentTimeMillis();
        return new SimpleDateFormat("dd.MM.yyyy").format(curTime);
    }

    /**
     * Проверяет, есть ли такой же ник в чате
     *
     * @param nicname   ник для проверки
     * @param listNames список ников
     * @return true - есть в списке, false - нет в списке
     */
    public static boolean isNotUniqueNicname(String nicname, List<String> listNames) {
        for (String userName : listNames) {
            if (nicname.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

}

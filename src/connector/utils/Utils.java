package connector.utils;

import connector.view.ServerFrame;
import java.awt.Dimension;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Класс с дополнительными функциями. Вынесены сюда, чтобы не засорять основной код
 *
 * @author Yura
 */
public class Utils {

    /**
     * Фильтр для поля порта, позволяет вводить только цифры.
     */
    public class DocumentFilterForPort extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

            StringBuffer buffer = new StringBuffer(string);
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
            StringBuffer buffer = null;
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

    /**
     * Воспроизводит звук.
     *
     * @param soundFile
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public static void playSound(File soundFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat()); // получаем информацию о звуке из потока
        Clip clip = (Clip) AudioSystem.getLine(info); // инициализируем проигрыватель
        clip.open(stream); // воспроизводим файл
        clip.start(); // закрываем проигрыватель
    }

    /**
     * Удаляет управляющие символы из строки.
     *
     * @param s
     * @return
     */
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

    private static String getInterfaceInfo(NetworkInterface nif) throws IOException {
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

    /**
     * Возвращает массив локальных IP
     *
     * @return массив локальных IP
     */
    public static ArrayList<String> getMyLocalIP() {
        ArrayList<String> listAddr = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nif = interfaces.nextElement();

                if (!getInterfaceInfo(nif).equals("")) {
                    listAddr.add(getInterfaceInfo(nif));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
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

    public static boolean checkString(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean checkIP(String string) {
        return checkString(string) && Integer.parseInt(string) >= 0 && Integer.parseInt(string) < 256;
    }

    public static Optional<String> getAndCheckIP(String ip) {
        char[] chArr = ip.toCharArray();
        String ip_1 = "", ip_2 = "", ip_3 = "", ip_4 = "";
        int i = 0;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_1 += chArr[i];
        }
        if (!checkIP(ip_1)) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_2 += chArr[i];
        }
        if (!checkIP(ip_2)) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ip_3 += chArr[i];
        }
        if (!checkIP(ip_3)) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            ip_4 += chArr[i];
        }
        if (!checkIP(ip_4)) {
            return Optional.empty();
        }
        return Optional.of(ip);
    }

    /**
     * Возвращает дату или время
     *
     * @param ch true - дату, false - время
     * @return дату или время
     */
    public static String getTime(boolean ch) {
        long curTime = System.currentTimeMillis();
        String curStringDate = ch
                ? new SimpleDateFormat("dd.MM.yyyy").format(curTime)
                : new SimpleDateFormat("kk:mm:ss").format(curTime);
        return curStringDate;
    }

    /**
     * Проверяет, есть ли такой же ник в чате
     *
     * @param nicname ник для проверки
     * @param listNames список ников
     * @return true - есть в списке, false - нет в списке
     */
    public static boolean checkNicname(String nicname, List<String> listNames) {
        boolean res = false;
        for (String userName : listNames) {
            if (nicname.equals(userName)) {
                res = true;
                break;
            } else {
                res = false;
            }
        }
        return res;
    }

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Не используется">
    public String getMyLocalIPOne() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        String myLANIP = addr.getHostAddress();
        return myLANIP;
    }

    public class StatusBar extends JLabel {

        public StatusBar() {
            super();
            super.setPreferredSize(new Dimension(100, 16));
            setMessage("Ready");
        }

        public void setMessage(String message) {
            setText(" " + message);
        }

    }

//        Utils.StatusBar statusBar = new Utils().new StatusBar();
//        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
//        statusBar.setMessage("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
    // Фильтр для поля IP, на данный момент не используется.
    public class DocumentFilterForIP extends DocumentFilter {

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

            StringBuffer buffer = new StringBuffer(string);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                char ch = buffer.charAt(i);
                if (!Character.isDigit(ch) && ch != '-') {
                    buffer.deleteCharAt(i);
                }
            }
            string = buffer.toString();
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            StringBuffer buffer = null;
            if (string != null) {
                buffer = new StringBuffer(string);
                for (int i = buffer.length() - 1; i >= 0; i--) {

                    char ch = buffer.charAt(i);
                    if (!Character.isDigit(ch) && ch != '.') {
                        buffer.deleteCharAt(i);
                    }
                }
                string = buffer.toString();
            }
            super.replace(fb, offset, length, string, attrs);
        }
    }
    //</editor-fold>
}

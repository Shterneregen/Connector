package connector.utils;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetUtils {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    private NetUtils() {
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

    public static List<String> getLocalIpList() {
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
        StringBuilder ipOct1 = new StringBuilder();
        StringBuilder ipOct2 = new StringBuilder();
        StringBuilder ipOct3 = new StringBuilder();
        StringBuilder ipOct4 = new StringBuilder();
        int i = 0;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ipOct1.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ipOct1.toString())) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ipOct2.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ipOct2.toString())) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            if (chArr[i] == '.') {
                break;
            }
            ipOct3.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ipOct3.toString())) {
            return Optional.empty();
        }
        i++;
        for (; i < chArr.length; i++) {
            ipOct4.append(chArr[i]);
        }
        if (isNotGoodIpOctet(ipOct4.toString())) {
            return Optional.empty();
        }
        return Optional.of(ip);
    }
}

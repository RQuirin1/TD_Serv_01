import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Outils {
    public ArrayList<Ip_v4> getSystemIP() throws SocketException {
        ArrayList<Ip_v4> mesIP = new ArrayList<>();
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = netInterfaces.nextElement();
            if (netInterface.isUp() && !netInterface.isLoopback() && !netInterface.isVirtual()){
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().contains(".")) {
                        mesIP.add(new Ip_v4(netInterface.getDisplayName(), inetAddress.getHostName(), inetAddress.getHostAddress()));
                    }
                }
            }
        }


        return null;
    }

}

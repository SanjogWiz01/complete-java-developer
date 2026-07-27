import java.net.InetAddress;

public class NetworkBasics {
    public static void main(String[] args) throws Exception {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("Host Name: " + localHost.getHostName());
        System.out.println("IP Address: " + localHost.getHostAddress());

        int tcpPort = 8080;
        int udpPort = 9000;
        System.out.println("Example TCP Port: " + tcpPort);
        System.out.println("Example UDP Port: " + udpPort);
        System.out.println("Ports identify process endpoints on a machine.");
    }
}

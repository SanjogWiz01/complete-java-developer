import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketProgrammingUDP {
    public static void main(String[] args) throws Exception {
        int port = 7001;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("SocketProgrammingUDP server on " + port);
            socket.receive(packet);
            System.out.println("Message from client: " + new String(packet.getData(), 0, packet.getLength()));
        }
    }
}

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClientDemo {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int serverPort = 6000;

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] message = "Hello from UDP client".getBytes();
            DatagramPacket request = new DatagramPacket(
                    message,
                    message.length,
                    InetAddress.getByName(host),
                    serverPort
            );
            socket.send(request);

            byte[] responseBuffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(response);
            String reply = new String(response.getData(), 0, response.getLength());
            System.out.println("Server response: " + reply);
        }
    }
}

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerDemo {
    public static void main(String[] args) throws Exception {
        int port = 6000;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server listening on port " + port);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);

            String data = new String(request.getData(), 0, request.getLength());
            System.out.println("Received: " + data);

            byte[] reply = ("UDP ACK: " + data).getBytes();
            DatagramPacket response = new DatagramPacket(
                    reply,
                    reply.length,
                    request.getAddress(),
                    request.getPort()
            );
            socket.send(response);
        }
    }
}

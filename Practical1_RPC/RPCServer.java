import java.net.*;
import java.io.*;

public class RPCServer {
    public static void main(String[] args) {
        int port = 5000;
        System.out.println("RPC Server started on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("Waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Read two integers from the client
                int num1 = in.readInt();
                int num2 = in.readInt();
                System.out.println("Received: " + num1 + " and " + num2);

                // Compute sum (this is the "remote procedure")
                int sum = num1 + num2;
                System.out.println("Sending sum: " + sum);

                // Send the result back
                out.writeInt(sum);
                out.flush();

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

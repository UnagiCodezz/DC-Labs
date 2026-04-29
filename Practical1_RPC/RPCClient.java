import java.net.*;
import java.io.*;
import java.util.Scanner;

public class RPCClient {

    // Local function for comparison
    public static int localSum(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first integer: ");
        int num1 = sc.nextInt();
        System.out.print("Enter second integer: ");
        int num2 = sc.nextInt();

        // --- Local Function Call with Timing ---
        long localStart = System.nanoTime();
        int localResult = localSum(num1, num2);
        long localEnd = System.nanoTime();
        long localTime = localEnd - localStart;

        System.out.println("\n--- Local Function Call ---");
        System.out.println("Result: " + localResult);
        System.out.println("Time taken: " + localTime + " nanoseconds");

        // --- RPC Call with Timing ---
        try {
            long rpcStart = System.nanoTime();

            Socket socket = new Socket("localhost", 5000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // Send two integers to server
            out.writeInt(num1);
            out.writeInt(num2);
            out.flush();

            // Receive result from server
            int rpcResult = in.readInt();

            long rpcEnd = System.nanoTime();
            long rpcTime = rpcEnd - rpcStart;

            System.out.println("\n--- RPC Call ---");
            System.out.println("Result: " + rpcResult);
            System.out.println("Time taken: " + rpcTime + " nanoseconds");

            // --- Comparison ---
            System.out.println("\n--- Performance Comparison ---");
            System.out.println("Local call time : " + localTime + " ns");
            System.out.println("RPC call time   : " + rpcTime + " ns");
            System.out.println("RPC is " + (rpcTime / Math.max(localTime, 1)) + "x slower than local call");
            System.out.println("Overhead of RPC : " + (rpcTime - localTime) + " ns");

            socket.close();
        } catch (IOException e) {
            System.out.println("Error: Could not connect to server. Make sure RPCServer is running.");
            e.printStackTrace();
        }
        sc.close();
    }
}

import java.util.Scanner;

/*
 * Dynamic Load Balancing - Least Connections Method
 * 
 * How it works:
 * 1. There are multiple servers, each with a current count of active connections.
 * 2. When a new request (task) arrives, it is assigned to the server
 *    that currently has the FEWEST active connections.
 * 3. After assignment, that server's connection count increases by 1.
 * 4. When a task finishes, the server's count decreases (simulated here).
 * 
 * This is DYNAMIC because decisions change based on real-time server load.
 */

public class Practical9_LeastConnections {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of servers: ");
        int numServers = sc.nextInt();

        String[] serverNames = new String[numServers];
        int[] activeConnections = new int[numServers];

        for (int i = 0; i < numServers; i++) {
            serverNames[i] = "Server " + (i + 1);
            System.out.print("Enter initial active connections for " + serverNames[i] + ": ");
            activeConnections[i] = sc.nextInt();
        }

        System.out.print("\nEnter number of incoming requests to simulate: ");
        int numRequests = sc.nextInt();

        System.out.println("\n========== LEAST CONNECTIONS LOAD BALANCING ==========\n");

        // Show initial state
        System.out.println("Initial server state:");
        printServerState(serverNames, activeConnections, numServers);

        // Process each request
        for (int req = 1; req <= numRequests; req++) {
            System.out.println("\n--- Request " + req + " arrives ---");

            // Find the server with the least connections
            int minIndex = 0;
            for (int i = 1; i < numServers; i++) {
                if (activeConnections[i] < activeConnections[minIndex]) {
                    minIndex = i;
                }
            }

            System.out.println("  " + serverNames[minIndex] + " has fewest connections (" + activeConnections[minIndex] + ")");
            activeConnections[minIndex]++;
            System.out.println("  Request " + req + " assigned to " + serverNames[minIndex]);
            System.out.println("  " + serverNames[minIndex] + " connections: " + (activeConnections[minIndex] - 1) + " -> " + activeConnections[minIndex]);

            // Optionally simulate a task completion (for dynamic behavior)
            System.out.print("  Simulate a task completion? (1=yes on a specific server, 0=no): ");
            int simComplete = sc.nextInt();
            if (simComplete == 1) {
                System.out.print("  Enter server number (1-" + numServers + ") where task completed: ");
                int sNum = sc.nextInt() - 1;
                if (sNum >= 0 && sNum < numServers && activeConnections[sNum] > 0) {
                    activeConnections[sNum]--;
                    System.out.println("  Task completed on " + serverNames[sNum] + ". Connections: " + (activeConnections[sNum] + 1) + " -> " + activeConnections[sNum]);
                }
            }

            printServerState(serverNames, activeConnections, numServers);
        }

        System.out.println("\n--- Final Server State ---");
        printServerState(serverNames, activeConnections, numServers);
        sc.close();
    }

    static void printServerState(String[] names, int[] connections, int n) {
        System.out.println("  +-------------+-------------+");
        System.out.println("  | Server      | Connections |");
        System.out.println("  +-------------+-------------+");
        for (int i = 0; i < n; i++) {
            System.out.printf("  | %-11s | %-11d |%n", names[i], connections[i]);
        }
        System.out.println("  +-------------+-------------+");
    }
}

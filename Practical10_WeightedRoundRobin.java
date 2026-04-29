import java.util.Scanner;

/*
 * Static Load Balancing - Weighted Round Robin Method
 * 
 * How it works:
 * 1. Each server is assigned a WEIGHT (higher weight = more capacity).
 * 2. Tasks are distributed in round-robin order, but each server receives
 *    a number of consecutive tasks proportional to its weight.
 * 3. For example: Server A (weight=3), Server B (weight=1), Server C (weight=2)
 *    -> Tasks go: A, A, A, B, C, C, A, A, A, B, C, C, ...
 * 
 * This is STATIC because the weights are fixed and don't change at runtime.
 */

public class Practical10_WeightedRoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of servers: ");
        int numServers = sc.nextInt();

        String[] serverNames = new String[numServers];
        int[] weights = new int[numServers];

        for (int i = 0; i < numServers; i++) {
            serverNames[i] = "Server " + (i + 1);
            System.out.print("Enter weight for " + serverNames[i] + ": ");
            weights[i] = sc.nextInt();
        }

        System.out.print("\nEnter number of tasks to distribute: ");
        int numTasks = sc.nextInt();

        System.out.println("\n========== WEIGHTED ROUND ROBIN LOAD BALANCING ==========\n");

        // Display server configuration
        System.out.println("Server Configuration:");
        System.out.println("  +-------------+--------+");
        System.out.println("  | Server      | Weight |");
        System.out.println("  +-------------+--------+");
        for (int i = 0; i < numServers; i++) {
            System.out.printf("  | %-11s | %-6d |%n", serverNames[i], weights[i]);
        }
        System.out.println("  +-------------+--------+");

        // Calculate total weight for one complete round
        int totalWeight = 0;
        for (int i = 0; i < numServers; i++) {
            totalWeight += weights[i];
        }
        System.out.println("\nTotal weight per round: " + totalWeight);
        System.out.println("One round distributes " + totalWeight + " tasks.\n");

        // Distribute tasks using weighted round robin
        System.out.println("--- Task Distribution ---\n");
        int[] taskCount = new int[numServers]; // how many tasks each server got
        int taskNum = 1;
        int serverIndex = 0;
        int currentWeightCount = 0;

        while (taskNum <= numTasks) {
            // Assign 'weight' number of tasks to current server
            currentWeightCount = 0;
            while (currentWeightCount < weights[serverIndex] && taskNum <= numTasks) {
                System.out.println("  Task " + taskNum + " -> " + serverNames[serverIndex]
                        + " (slot " + (currentWeightCount + 1) + " of " + weights[serverIndex] + ")");
                taskCount[serverIndex]++;
                currentWeightCount++;
                taskNum++;
            }

            // Move to next server in round-robin
            serverIndex = (serverIndex + 1) % numServers;
        }

        // Summary
        System.out.println("\n--- Distribution Summary ---");
        System.out.println("  +-------------+--------+-------+");
        System.out.println("  | Server      | Weight | Tasks |");
        System.out.println("  +-------------+--------+-------+");
        for (int i = 0; i < numServers; i++) {
            System.out.printf("  | %-11s | %-6d | %-5d |%n", serverNames[i], weights[i], taskCount[i]);
        }
        System.out.println("  +-------------+--------+-------+");
        System.out.println("  Total tasks distributed: " + (taskNum - 1));

        sc.close();
    }
}

import java.util.Scanner;

/*
 * Bully Algorithm for Leader/Coordinator Election
 * 
 * How it works:
 * 1. When a process detects the coordinator has failed, it starts an election.
 * 2. It sends ELECTION messages to all processes with HIGHER IDs.
 * 3. If no higher-ID process responds, it becomes the new coordinator.
 * 4. If a higher-ID process responds (OK), that process takes over the election.
 * 5. The process with the highest ID wins and sends COORDINATOR message to all.
 */

public class Practical4_BullyAlgorithm {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total number of processes: ");
        int n = sc.nextInt();

        int[] processes = new int[n];
        boolean[] isAlive = new boolean[n];

        // Initialize processes with IDs
        System.out.println("Enter process IDs:");
        for (int i = 0; i < n; i++) {
            System.out.print("  Process " + (i + 1) + " ID: ");
            processes[i] = sc.nextInt();
            isAlive[i] = true; // all processes are alive initially
        }

        // Ask which process has crashed (the coordinator)
        System.out.print("\nEnter the ID of the crashed process (coordinator): ");
        int crashedId = sc.nextInt();

        // Mark the crashed process as dead
        for (int i = 0; i < n; i++) {
            if (processes[i] == crashedId) {
                isAlive[i] = false;
                System.out.println("Process " + crashedId + " has been marked as CRASHED.");
                break;
            }
        }

        // Ask which process detects the failure and initiates election
        System.out.print("Enter the ID of the process that initiates the election: ");
        int initiatorId = sc.nextInt();

        System.out.println("\n========== BULLY ELECTION STARTS ==========\n");

        // Run the bully algorithm starting from the initiator
        bullyElection(processes, isAlive, n, initiatorId);

        sc.close();
    }

    static void bullyElection(int[] processes, boolean[] isAlive, int n, int initiatorId) {
        System.out.println("Process " + initiatorId + " starts election.");

        // Send ELECTION message to all processes with higher IDs
        boolean gotResponse = false;
        int highestResponder = -1;

        for (int i = 0; i < n; i++) {
            if (processes[i] > initiatorId && isAlive[i]) {
                System.out.println("  Process " + initiatorId + " sends ELECTION to Process " + processes[i]);
                System.out.println("  Process " + processes[i] + " sends OK to Process " + initiatorId);
                gotResponse = true;

                // The highest alive process that responded
                if (processes[i] > highestResponder) {
                    highestResponder = processes[i];
                }
            } else if (processes[i] > initiatorId && !isAlive[i]) {
                System.out.println("  Process " + initiatorId + " sends ELECTION to Process " + processes[i] + " -> NO RESPONSE (crashed)");
            }
        }

        if (!gotResponse) {
            // No higher process responded, initiator becomes coordinator
            System.out.println("\n  No higher process responded.");
            System.out.println("  Process " + initiatorId + " becomes the NEW COORDINATOR!");
            announceCoordinator(processes, isAlive, n, initiatorId);
        } else {
            // Higher processes take over - each one that responded also runs the election
            System.out.println("\n  Higher processes responded. They take over the election.");

            // Among the alive processes with higher IDs, the highest one wins
            // Simulate each higher process also sending elections
            for (int i = 0; i < n; i++) {
                if (processes[i] > initiatorId && isAlive[i] && processes[i] != highestResponder) {
                    // This process also sends election to higher ones
                    System.out.println("\n  Process " + processes[i] + " starts its own election:");
                    for (int j = 0; j < n; j++) {
                        if (processes[j] > processes[i] && isAlive[j]) {
                            System.out.println("    Process " + processes[i] + " sends ELECTION to Process " + processes[j]);
                            System.out.println("    Process " + processes[j] + " sends OK to Process " + processes[i]);
                        } else if (processes[j] > processes[i] && !isAlive[j]) {
                            System.out.println("    Process " + processes[i] + " sends ELECTION to Process " + processes[j] + " -> NO RESPONSE (crashed)");
                        }
                    }
                }
            }

            // The highest alive process becomes coordinator
            System.out.println("\n  Process " + highestResponder + " starts its own election:");
            boolean anyHigher = false;
            for (int i = 0; i < n; i++) {
                if (processes[i] > highestResponder && isAlive[i]) {
                    anyHigher = true;
                }
            }
            if (!anyHigher) {
                System.out.println("    No higher process exists or is alive.");
                System.out.println("    Process " + highestResponder + " becomes the NEW COORDINATOR!");
                announceCoordinator(processes, isAlive, n, highestResponder);
            }
        }
    }

    static void announceCoordinator(int[] processes, boolean[] isAlive, int n, int coordinatorId) {
        System.out.println("\n--- COORDINATOR Announcement ---");
        for (int i = 0; i < n; i++) {
            if (processes[i] != coordinatorId && isAlive[i]) {
                System.out.println("  Process " + coordinatorId + " sends COORDINATOR message to Process " + processes[i]);
            }
        }
        System.out.println("\n==> New Coordinator is Process " + coordinatorId + " <==");
    }
}

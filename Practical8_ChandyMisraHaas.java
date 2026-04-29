import java.util.Scanner;

/*
 * Chandy-Misra-Haas Algorithm for Distributed Deadlock Detection
 * 
 * How it works:
 * 1. When a process Pi is blocked waiting for Pj, it sends a PROBE message (i, j, k)
 *    where i=initiator, j=sender, k=receiver.
 * 2. If Pk (the receiver) is also blocked, it forwards the probe to the process
 *    it is waiting for, updating the sender/receiver fields.
 * 3. If the probe returns to the initiator (i == k), a DEADLOCK is detected.
 *
 * The wait-for relationships form a graph. A cycle = deadlock.
 */

public class Practical8_ChandyMisraHaas {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // waitFor[i] = process that process i is waiting for
        // -1 means process i is not waiting for anyone
        int[] waitFor = new int[n];

        System.out.println("Enter which process each process is waiting for (-1 if not waiting):");
        for (int i = 0; i < n; i++) {
            System.out.print("  Process " + i + " is waiting for: ");
            waitFor[i] = sc.nextInt();
        }

        System.out.print("\nEnter the process that initiates deadlock detection: ");
        int initiator = sc.nextInt();

        System.out.println("\n========== CHANDY-MISRA-HAAS ALGORITHM ==========\n");

        if (waitFor[initiator] == -1) {
            System.out.println("Process " + initiator + " is NOT waiting for anyone. No deadlock to detect.");
            sc.close();
            return;
        }

        // Initiator sends probe (initiator, initiator, waitFor[initiator])
        int probeInit = initiator;
        int sender = initiator;
        int receiver = waitFor[initiator];

        System.out.println("Process " + initiator + " is blocked, waiting for Process " + receiver);
        System.out.println("Process " + initiator + " initiates probe (" + probeInit + ", " + sender + ", " + receiver + ")\n");

        boolean deadlockDetected = false;
        // Track visited to avoid infinite loop in non-deadlock scenarios
        boolean[] visited = new boolean[n];
        visited[initiator] = true;

        int step = 1;
        while (true) {
            System.out.println("Step " + step + ": Probe (" + probeInit + ", " + sender + ", " + receiver + ") arrives at Process " + receiver);

            // Check if probe has returned to initiator
            if (receiver == probeInit) {
                System.out.println("  Probe returned to initiator (Process " + probeInit + ")!");
                deadlockDetected = true;
                break;
            }

            // Check if receiver is also blocked
            if (waitFor[receiver] == -1) {
                System.out.println("  Process " + receiver + " is NOT blocked. Probe discarded.");
                break;
            }

            if (visited[receiver]) {
                // We've visited this node before but it's not the initiator
                // This means there's a cycle not involving the initiator
                System.out.println("  Already visited Process " + receiver + ". Stopping.");
                break;
            }

            visited[receiver] = true;

            // Forward the probe
            int nextReceiver = waitFor[receiver];
            System.out.println("  Process " + receiver + " is also blocked (waiting for Process " + nextReceiver + ")");
            System.out.println("  Process " + receiver + " forwards probe (" + probeInit + ", " + receiver + ", " + nextReceiver + ")");
            System.out.println();

            sender = receiver;
            receiver = nextReceiver;
            step++;
        }

        System.out.println();
        if (deadlockDetected) {
            System.out.println("*** DEADLOCK DETECTED! ***");

            // Print the cycle
            System.out.print("Deadlock cycle: Process " + initiator);
            int current = waitFor[initiator];
            while (current != initiator) {
                System.out.print(" -> Process " + current);
                current = waitFor[current];
            }
            System.out.println(" -> Process " + initiator);
        } else {
            System.out.println("No deadlock detected. Process " + initiator + " is safe.");
        }

        sc.close();
    }
}

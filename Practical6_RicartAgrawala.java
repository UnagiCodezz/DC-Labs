import java.util.Scanner;

/*
 * Ricart-Agrawala Algorithm for Distributed Mutual Exclusion
 * 
 * How it works:
 * 1. When a process wants to enter the critical section, it sends REQUEST
 *    messages (with its timestamp) to ALL other processes.
 * 2. A process receiving a REQUEST:
 *    - If it is NOT interested in CS -> sends REPLY immediately.
 *    - If it IS in CS -> defers the REPLY.
 *    - If it also wants CS -> compares timestamps. Lower timestamp wins;
 *      higher timestamp defers.
 * 3. A process enters CS only after receiving REPLY from ALL other processes.
 * 4. On exiting CS, it sends deferred REPLYs.
 */

public class Practical6_RicartAgrawala {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Each process has a timestamp for when it requests CS
        // 0 means "not requesting"
        int[] requestTimestamp = new int[n];
        boolean[] wantsCS = new boolean[n];

        System.out.println("For each process, enter whether it wants Critical Section and its request timestamp.");
        System.out.println("(Enter 0 for timestamp if the process does NOT want CS)\n");

        for (int i = 0; i < n; i++) {
            System.out.print("  Does Process " + i + " want CS? (1=yes, 0=no): ");
            int wants = sc.nextInt();
            wantsCS[i] = (wants == 1);
            if (wantsCS[i]) {
                System.out.print("  Enter request timestamp for Process " + i + ": ");
                requestTimestamp[i] = sc.nextInt();
            } else {
                requestTimestamp[i] = 0;
            }
        }

        System.out.println("\n========== RICART-AGRAWALA ALGORITHM ==========\n");

        // For each process that wants CS, simulate the algorithm
        for (int i = 0; i < n; i++) {
            if (!wantsCS[i]) continue;

            System.out.println("Process " + i + " wants CS (timestamp=" + requestTimestamp[i] + ")");
            System.out.println("  Sends REQUEST to all other processes...\n");

            int repliesReceived = 0;
            int[] deferredBy = new int[n]; // processes that deferred our request
            int deferredCount = 0;

            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                System.out.print("  Process " + j + " receives REQUEST from Process " + i + ": ");

                if (!wantsCS[j]) {
                    // Not interested in CS, send REPLY immediately
                    System.out.println("Not interested in CS -> sends REPLY");
                    repliesReceived++;
                } else {
                    // Both want CS, compare timestamps
                    if (requestTimestamp[i] < requestTimestamp[j]) {
                        // Requester has lower timestamp -> gets priority
                        System.out.println("Both want CS, but P" + i + "(ts=" + requestTimestamp[i] + ") < P" + j + "(ts=" + requestTimestamp[j] + ") -> sends REPLY");
                        repliesReceived++;
                    } else if (requestTimestamp[i] > requestTimestamp[j]) {
                        // Receiver has lower timestamp -> defers reply
                        System.out.println("Both want CS, but P" + j + "(ts=" + requestTimestamp[j] + ") < P" + i + "(ts=" + requestTimestamp[i] + ") -> DEFERS REPLY");
                        deferredBy[deferredCount++] = j;
                    } else {
                        // Same timestamp, use process ID to break tie (lower ID wins)
                        if (i < j) {
                            System.out.println("Same timestamp, P" + i + " has lower ID -> sends REPLY");
                            repliesReceived++;
                        } else {
                            System.out.println("Same timestamp, P" + j + " has lower ID -> DEFERS REPLY");
                            deferredBy[deferredCount++] = j;
                        }
                    }
                }
            }

            System.out.println("\n  Process " + i + " received " + repliesReceived + " out of " + (n - 1) + " REPLIES");
            if (repliesReceived == n - 1) {
                System.out.println("  *** Process " + i + " ENTERS Critical Section ***");
                System.out.println("  *** Process " + i + " EXITS Critical Section ***");

                // Send deferred replies (none in this case since all replied)
            } else {
                System.out.println("  Process " + i + " WAITS (needs " + (n - 1 - repliesReceived) + " more replies)");
            }
            System.out.println();
        }

        // Determine execution order based on timestamps
        System.out.println("--- EXECUTION ORDER (by timestamp, lower goes first) ---");
        // Simple selection sort to order requesting processes by timestamp
        int[] requestingProcesses = new int[n];
        int[] timestamps = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (wantsCS[i]) {
                requestingProcesses[count] = i;
                timestamps[count] = requestTimestamp[i];
                count++;
            }
        }

        // Sort by timestamp (then by ID for ties)
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (timestamps[j] < timestamps[i] || (timestamps[j] == timestamps[i] && requestingProcesses[j] < requestingProcesses[i])) {
                    int temp = timestamps[i]; timestamps[i] = timestamps[j]; timestamps[j] = temp;
                    temp = requestingProcesses[i]; requestingProcesses[i] = requestingProcesses[j]; requestingProcesses[j] = temp;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            System.out.println("  " + (i + 1) + ". Process " + requestingProcesses[i] + " (timestamp=" + timestamps[i] + ")");
        }

        sc.close();
    }
}

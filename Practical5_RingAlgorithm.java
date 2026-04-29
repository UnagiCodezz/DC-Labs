import java.util.Scanner;

/*
 * Ring Algorithm for Leader/Coordinator Election
 * 
 * How it works:
 * 1. Processes are arranged in a logical ring.
 * 2. When a process detects coordinator failure, it creates an ELECTION message
 *    with its own ID and sends it to its neighbor.
 * 3. Each process adds its ID and forwards the message.
 * 4. Crashed processes are skipped.
 * 5. When the message returns to the initiator, the highest ID wins.
 */

public class Practical5_RingAlgorithm {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] processes = new int[n];
        boolean[] isAlive = new boolean[n];

        System.out.println("Enter process IDs (in ring order):");
        for (int i = 0; i < n; i++) {
            System.out.print("  Position " + i + ": ");
            processes[i] = sc.nextInt();
            isAlive[i] = true;
        }

        System.out.print("\nEnter ID of the crashed process: ");
        int crashedId = sc.nextInt();
        for (int i = 0; i < n; i++) {
            if (processes[i] == crashedId) {
                isAlive[i] = false;
                break;
            }
        }
        System.out.println("Process " + crashedId + " marked as CRASHED.");

        System.out.print("Enter ID of the process that initiates election: ");
        int initiatorId = sc.nextInt();

        int initiatorIndex = -1;
        for (int i = 0; i < n; i++) {
            if (processes[i] == initiatorId) {
                initiatorIndex = i;
                break;
            }
        }

        System.out.println("\n========== RING ELECTION STARTS ==========\n");

        // Election message collects IDs
        int[] electionList = new int[n];
        int listSize = 0;

        electionList[listSize++] = initiatorId;
        System.out.println("Process " + initiatorId + " creates ELECTION message with [" + initiatorId + "]");

        int currentIndex = (initiatorIndex + 1) % n;
        while (currentIndex != initiatorIndex) {
            if (!isAlive[currentIndex]) {
                System.out.println("Process " + processes[currentIndex] + " is CRASHED, skipping...");
            } else {
                electionList[listSize++] = processes[currentIndex];
                System.out.print("Process " + processes[currentIndex] + " adds its ID -> [");
                for (int i = 0; i < listSize; i++) {
                    if (i > 0) System.out.print(", ");
                    System.out.print(electionList[i]);
                }
                System.out.println("]");
            }
            currentIndex = (currentIndex + 1) % n;
        }

        System.out.println("\nMessage returned to initiator (Process " + initiatorId + ")");

        // Find highest ID
        int maxId = electionList[0];
        for (int i = 1; i < listSize; i++) {
            if (electionList[i] > maxId) maxId = electionList[i];
        }

        System.out.println("\n==> Process " + maxId + " is the NEW COORDINATOR <==");

        // Circulate COORDINATOR message
        System.out.println("\n--- COORDINATOR message circulated ---");
        currentIndex = (initiatorIndex + 1) % n;
        while (currentIndex != initiatorIndex) {
            if (isAlive[currentIndex]) {
                System.out.println("  Process " + processes[currentIndex] + " acknowledges: coordinator is " + maxId);
            }
            currentIndex = (currentIndex + 1) % n;
        }

        sc.close();
    }
}

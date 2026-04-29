import java.util.Scanner;

/*
 * Berkeley's Clock Synchronization Algorithm
 * 
 * How it works:
 * 1. A coordinator (master) polls all nodes for their clock times.
 * 2. The coordinator calculates the average of all clocks (including its own).
 * 3. The coordinator sends each node the adjustment (offset) they need to apply.
 * 4. All clocks are now synchronized to the average time.
 */

public class Practical3_BerkeleyClock {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of nodes (including coordinator): ");
        int n = sc.nextInt();

        int[] clocks = new int[n];

        // Node 0 is the coordinator (master)
        System.out.println("Enter clock times for each node (in simple integer format, e.g., minutes past midnight):");
        for (int i = 0; i < n; i++) {
            if (i == 0)
                System.out.print("  Coordinator (Node 0) clock time: ");
            else
                System.out.print("  Node " + i + " clock time: ");
            clocks[i] = sc.nextInt();
        }

        // Step 1: Coordinator collects all clock values (already done via input)
        System.out.println("\n--- Step 1: Coordinator polls all clocks ---");
        for (int i = 0; i < n; i++) {
            System.out.println("  Node " + i + " reports time: " + clocks[i]);
        }

        // Step 2: Calculate the average time
        int totalSum = 0;
        for (int i = 0; i < n; i++) {
            totalSum += clocks[i];
        }
        int averageTime = totalSum / n;

        System.out.println("\n--- Step 2: Calculate average ---");
        System.out.println("  Sum of all clocks = " + totalSum);
        System.out.println("  Average time = " + totalSum + " / " + n + " = " + averageTime);

        // Step 3: Calculate adjustments for each node
        System.out.println("\n--- Step 3: Calculate adjustments ---");
        int[] adjustments = new int[n];
        for (int i = 0; i < n; i++) {
            adjustments[i] = averageTime - clocks[i];
            String direction;
            if (adjustments[i] > 0)
                direction = "(move forward by " + adjustments[i] + ")";
            else if (adjustments[i] < 0)
                direction = "(move backward by " + Math.abs(adjustments[i]) + ")";
            else
                direction = "(no change needed)";
            System.out.println("  Node " + i + ": adjustment = " + adjustments[i] + " " + direction);
        }

        // Step 4: Apply adjustments and show synchronized clocks
        System.out.println("\n--- Step 4: Synchronized clocks ---");
        for (int i = 0; i < n; i++) {
            int newTime = clocks[i] + adjustments[i];
            System.out.println("  Node " + i + ": " + clocks[i] + " + (" + adjustments[i] + ") = " + newTime);
        }

        System.out.println("\nAll clocks are now synchronized to time: " + averageTime);
        sc.close();
    }
}

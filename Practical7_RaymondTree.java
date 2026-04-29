import java.util.Scanner;

/*
 * Raymond's Tree-Based Algorithm for Distributed Mutual Exclusion
 * 
 * How it works:
 * 1. Processes are arranged in a logical TREE structure.
 * 2. There is a single TOKEN in the system. The process holding the token
 *    can enter the critical section.
 * 3. Each process maintains a variable 'holder' pointing to the neighbor
 *    that is in the direction of the token.
 * 4. When a process wants CS and doesn't have the token, it sends a REQUEST
 *    to its 'holder'. The request propagates toward the token.
 * 5. The token holder, after finishing CS, sends the token to the next
 *    requester based on its local queue.
 */

public class Practical7_RaymondTree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Tree structure: parent[i] = parent of process i, -1 if root
        int[] parent = new int[n];
        int[] holder = new int[n]; // direction toward token

        System.out.println("\nDefine the tree structure (enter parent for each process, -1 for root):");
        for (int i = 0; i < n; i++) {
            System.out.print("  Parent of Process " + i + ": ");
            parent[i] = sc.nextInt();
        }

        System.out.print("\nWhich process currently holds the TOKEN? ");
        int tokenHolder = sc.nextInt();

        // Initialize holder pointers (each points toward the token)
        // Using BFS-like approach: for each node, holder points toward token
        initializeHolders(holder, parent, n, tokenHolder);

        System.out.println("\nInitial holder pointers (direction toward token):");
        for (int i = 0; i < n; i++) {
            if (i == tokenHolder)
                System.out.println("  Process " + i + ": holds TOKEN (holder = self)");
            else
                System.out.println("  Process " + i + ": holder = Process " + holder[i]);
        }

        System.out.print("\nWhich process wants to enter Critical Section? ");
        int requester = sc.nextInt();

        System.out.println("\n========== RAYMOND'S ALGORITHM ==========\n");

        if (requester == tokenHolder) {
            System.out.println("Process " + requester + " already has the TOKEN.");
            System.out.println("*** Process " + requester + " ENTERS Critical Section ***");
            System.out.println("*** Process " + requester + " EXITS Critical Section ***");
        } else {
            // Trace the path from requester to token holder
            System.out.println("Process " + requester + " wants CS but doesn't have the token.\n");

            // Build the request path
            int[] path = new int[n];
            int pathLen = 0;
            path[pathLen++] = requester;
            int current = requester;

            // Follow holder pointers to reach the token
            while (current != tokenHolder) {
                current = holder[current];
                path[pathLen++] = current;
            }

            // Step 1: REQUEST propagation
            System.out.println("Step 1: REQUEST propagation along the tree");
            for (int i = 0; i < pathLen - 1; i++) {
                System.out.println("  Process " + path[i] + " sends REQUEST to Process " + path[i + 1]);
            }

            // Step 2: TOKEN transfer
            System.out.println("\nStep 2: TOKEN transfer back to requester");
            System.out.println("  Process " + tokenHolder + " finishes CS / is idle, releases TOKEN");
            for (int i = pathLen - 1; i > 0; i--) {
                System.out.println("  Process " + path[i] + " sends TOKEN to Process " + path[i - 1]);
                // Update holder pointer
                holder[path[i]] = path[i - 1];
            }

            // Step 3: Requester enters CS
            holder[requester] = requester;
            System.out.println("\n*** Process " + requester + " receives TOKEN ***");
            System.out.println("*** Process " + requester + " ENTERS Critical Section ***");
            System.out.println("*** Process " + requester + " EXITS Critical Section ***");

            // Show updated holder pointers
            System.out.println("\nUpdated holder pointers:");
            for (int i = 0; i < n; i++) {
                if (i == requester)
                    System.out.println("  Process " + i + ": holds TOKEN (holder = self)");
                else
                    System.out.println("  Process " + i + ": holder = Process " + holder[i]);
            }
        }

        sc.close();
    }

    // Initialize holder pointers: each process points toward the token holder
    static void initializeHolders(int[] holder, int[] parent, int n, int tokenHolder) {
        // For the token holder, holder points to self
        holder[tokenHolder] = tokenHolder;

        // For every other node, find the path to tokenHolder and set holder
        // to the next node on that path
        for (int i = 0; i < n; i++) {
            if (i == tokenHolder) continue;

            // Find path from i to tokenHolder using parent pointers
            // First find path from i to root
            int[] pathFromI = new int[n];
            int lenI = 0;
            int curr = i;
            while (curr != -1) {
                pathFromI[lenI++] = curr;
                curr = parent[curr];
            }

            // Find path from tokenHolder to root
            int[] pathFromToken = new int[n];
            int lenT = 0;
            curr = tokenHolder;
            while (curr != -1) {
                pathFromToken[lenT++] = curr;
                curr = parent[curr];
            }

            // Find the LCA (Lowest Common Ancestor) and build path
            // Mark nodes in tokenHolder's path
            boolean[] inTokenPath = new boolean[n];
            for (int j = 0; j < lenT; j++) {
                inTokenPath[pathFromToken[j]] = true;
            }

            // Find first node in i's path that is also in token's path (LCA)
            int lcaIndexInI = -1;
            for (int j = 0; j < lenI; j++) {
                if (inTokenPath[pathFromI[j]]) {
                    lcaIndexInI = j;
                    break;
                }
            }

            // holder[i] should point to the next node toward tokenHolder
            if (lcaIndexInI == 0) {
                // i is the LCA itself or on the path
                // Find which child leads to tokenHolder
                for (int j = 0; j < lenT - 1; j++) {
                    if (pathFromToken[j + 1] == i) {
                        holder[i] = pathFromToken[j];
                        break;
                    }
                }
            } else {
                // Next node toward token is parent (going up toward LCA first)
                holder[i] = pathFromI[1]; // parent of i
            }
        }
    }
}

# Test Inputs for All DC Lab Practicals

Copy-paste the inputs shown below when the program asks.

---

## Practical 1 — RPC (Sum + Timing)

**Step 1:** Open a terminal, compile & run the server:
```
cd Practical1_RPC
javac RPCServer.java
java RPCServer
```

**Step 2:** Open a SECOND terminal, compile & run the client:
```
cd Practical1_RPC
javac RPCClient.java
java RPCClient
```

**Client Input:**
```
Enter first integer: 25
Enter second integer: 17
```
**Expected:** Both local and RPC return 42. RPC will show significantly more time (network overhead).

---

## Practical 2 — RMI (Arithmetic)

**Step 1:** Open a terminal, compile & run the server:
```
cd Practical2_RMI
javac *.java
java ArithmeticServer
```

**Step 2:** Open a SECOND terminal, run the client:
```
cd Practical2_RMI
java ArithmeticClient
```

**Client Input:**
```
Enter first number: 20
Enter second number: 4
```
**Expected:** Add=24, Subtract=16, Multiply=80, Divide=5.0

**To test divide-by-zero:** Enter second number as `0`.

---

## Practical 3 — Berkeley's Clock Synchronization

```
javac Practical3_BerkeleyClock.java
java Practical3_BerkeleyClock
```

**Input:**
```
Enter number of nodes (including coordinator): 4
  Coordinator (Node 0) clock time: 100
  Node 1 clock time: 110
  Node 2 clock time: 125
  Node 3 clock time: 90
```
**Expected:** Average = (100+110+125+90)/4 = 106. Adjustments: Node 0: +6, Node 1: -4, Node 2: -19, Node 3: +16.

---

## Practical 4 — Bully Algorithm

```
javac Practical4_BullyAlgorithm.java
java Practical4_BullyAlgorithm
```

**Input:**
```
Enter total number of processes: 5
  Process 1 ID: 1
  Process 2 ID: 2
  Process 3 ID: 3
  Process 4 ID: 4
  Process 5 ID: 5

Enter the ID of the crashed process (coordinator): 5
Enter the ID of the process that initiates the election: 2
```
**Expected:** Process 2 sends ELECTION to 3 and 4. Process 5 is crashed. Process 3 and 4 respond OK. Process 4 (highest alive) becomes the new coordinator.

---

## Practical 5 — Ring Algorithm

```
javac Practical5_RingAlgorithm.java
java Practical5_RingAlgorithm
```

**Input:**
```
Enter number of processes: 5
  Position 0: 1
  Position 1: 2
  Position 2: 3
  Position 3: 4
  Position 4: 5

Enter ID of the crashed process: 5
Enter ID of the process that initiates election: 2
```
**Expected:** Election message goes 2→3→4→(skip 5)→1→back to 2. Message collects [2,3,4,1]. Highest = 4. Process 4 is new coordinator.

---

## Practical 6 — Ricart-Agrawala (Mutual Exclusion)

```
javac Practical6_RicartAgrawala.java
java Practical6_RicartAgrawala
```

**Input:**
```
Enter number of processes: 3
  Does Process 0 want CS? (1=yes, 0=no): 1
  Enter request timestamp for Process 0: 5
  Does Process 1 want CS? (1=yes, 0=no): 1
  Enter request timestamp for Process 1: 3
  Does Process 2 want CS? (1=yes, 0=no): 0
```
**Expected:** Process 1 (ts=3) has lower timestamp, so it gets priority. Process 0 (ts=5) must wait. Execution order: Process 1 first, then Process 0.

---

## Practical 7 — Raymond's Tree-Based (Mutual Exclusion)

```
javac Practical7_RaymondTree.java
java Practical7_RaymondTree
```

**Input (tree with 4 nodes):**
```
Enter number of processes: 4
  Parent of Process 0: -1
  Parent of Process 1: 0
  Parent of Process 2: 0
  Parent of Process 3: 1

Which process currently holds the TOKEN? 0
Which process wants to enter Critical Section? 3
```

**Tree structure being created:**
```
       P0 (root, has TOKEN)
      /  \
    P1    P2
    |
    P3 (wants CS)
```

**Expected:** P3 sends REQUEST to P1, P1 forwards to P0. Token goes P0→P1→P3. Process 3 enters CS.

---

## Practical 8 — Chandy-Misra-Haas (Deadlock Detection)

```
javac Practical8_ChandyMisraHaas.java
java Practical8_ChandyMisraHaas
```

**Input (WITH deadlock):**
```
Enter number of processes: 3
  Process 0 is waiting for: 1
  Process 1 is waiting for: 2
  Process 2 is waiting for: 0

Enter the process that initiates deadlock detection: 0
```
**Expected:** Probe travels 0→1→2→0. Cycle detected! **DEADLOCK: P0 → P1 → P2 → P0**

**Input (WITHOUT deadlock):**
```
Enter number of processes: 3
  Process 0 is waiting for: 1
  Process 1 is waiting for: 2
  Process 2 is waiting for: -1

Enter the process that initiates deadlock detection: 0
```
**Expected:** Probe travels 0→1→2. P2 is not blocked. No deadlock.

---

## Practical 9 — Least Connections (Dynamic Load Balancing)

```
javac Practical9_LeastConnections.java
java Practical9_LeastConnections
```

**Input:**
```
Enter number of servers: 3
Enter initial active connections for Server 1: 2
Enter initial active connections for Server 2: 5
Enter initial active connections for Server 3: 1

Enter number of incoming requests to simulate: 4
```

Then for each request:
```
--- Request 1 ---
  Simulate a task completion? (1=yes on a specific server, 0=no): 0

--- Request 2 ---
  Simulate a task completion? (1=yes on a specific server, 0=no): 0

--- Request 3 ---
  Simulate a task completion? (1=yes on a specific server, 0=no): 1
  Enter server number (1-3) where task completed: 1

--- Request 4 ---
  Simulate a task completion? (1=yes on a specific server, 0=no): 0
```

**Expected:** Requests go to the server with fewest connections each time. Server 3 (1 conn) gets first request, then Server 1 and Server 3 tie, etc.

---

## Practical 10 — Weighted Round Robin (Static Load Balancing)

```
javac Practical10_WeightedRoundRobin.java
java Practical10_WeightedRoundRobin
```

**Input:**
```
Enter number of servers: 3
Enter weight for Server 1: 3
Enter weight for Server 2: 1
Enter weight for Server 3: 2

Enter number of tasks to distribute: 10
```

**Expected distribution:**
- Tasks 1-3 → Server 1 (weight 3)
- Task 4 → Server 2 (weight 1)
- Tasks 5-6 → Server 3 (weight 2)
- Tasks 7-9 → Server 1 (weight 3, round 2)
- Task 10 → Server 2 (weight 1, round 2)

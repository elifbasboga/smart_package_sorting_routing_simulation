
# 📦 ParcelSortX: Smart Package Sorting and Routing Simulation

**ParcelSortX** is an intelligent parcel routing and sorting simulation project built using classical data structures in Java. The system models logistics workflows by integrating key structures such as **queue**, **stack**, **binary search tree (BST)**, **hash table**, and **circular linked list** in a cohesive simulation environment.

---

## 🚀 Project Overview

ParcelSortX simulates a time-driven system that receives incoming parcels and routes them to the correct city-based distribution terminals. At each **tick** (time step), new parcels are generated, prioritized, sorted, and dispatched. The following data structures work together seamlessly:

* **Queue**: Holds newly created parcels for initial processing
* **Stack**: Temporarily stores parcels before dispatch
* **Binary Search Tree (BST)**: Handles city-based routing
* **Hash Table**: Efficient lookup and tracking of all registered parcels
* **Circular Linked List**: Cycles through active terminal cities at each tick

---

## 🔧 Usage

### Requirements

* Java 17 or higher
* A Java IDE (e.g., IntelliJ, Eclipse) or terminal access

### Compile and Run

```bash
javac Main.java
java Main
```

> This is a CLI-based demo version. You can run `Main.java` directly without a GUI.

---

## 📋 Simulation Workflow

Each tick executes the following operations:

1. New parcels are generated with random cities, priorities, and IDs
2. Parcels are enqueued and then pushed to a stack
3. Based on destination, parcels are dispatched using the BST
4. The active terminal city changes in a circular fashion
5. Undeliverable parcels (e.g., unknown cities) are returned and reprocessed

---

## 📄 Sample Simulation Output

```
=================== TICK 1 ===================
[Main Log] Generated and queued: [P100, Dest: Istanbul, Priority: 2]
[Main Log] Dispatched parcel to BST city: [P100, Dest: Istanbul]
[Route Log] Delivering to: Istanbul
[Route Log] Active terminal: Bursa
...
===== FINAL REPORT =====
Total parcels generated: 20
Total parcels dispatched: 22
Total parcels returned: 2
```

> A full delivery report is shown at the end of the simulation.

---

## 🎯 Features

* Priority-based dispatching
* Returns and redelivery logic
* City routing via BST
* Rotating terminal cities using a circular linked list
* Developer-friendly logging and traceability

---

## 📌 Notes

* The simulation is **non-deterministic**: parcel combinations vary with each run.

Dilan Elif Başboğa
Ahsen Keçeci


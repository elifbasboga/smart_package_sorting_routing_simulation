package src;

import java.io.*;
import java.util.*;

// ------------------ PARCEL CLASS ------------------
class Parcel {
    String id;
    String destination;
    int priority;
    int arrivalTick;
    int returnCount;
    String status;

    public Parcel(String id, String destination, int priority, int arrivalTick) {
        this.id = id;
        this.destination = destination;
        this.priority = priority;
        this.arrivalTick = arrivalTick;
        this.returnCount = 0;
        this.status = "Pending";
    }

    @Override
    public String toString() {
        return "[" + id + ", Dest: " + destination + ", Priority: " + priority +
                ", ArrivalTick: " + arrivalTick + ", ReturnCount: " + returnCount +
                ", Status: " + status + "]";
    }
}

// ------------------ QUEUE ------------------
class ParcelQueue {
    private Queue<Parcel> queue;

    public ParcelQueue() {
        this.queue = new LinkedList<>();
    }

    public void enqueue(Parcel p) {
        queue.add(p);
    }

    public Parcel dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ------------------ STACK ------------------
class ParcelStack {
    private List<Parcel> stack = new ArrayList<>();

    public void push(Parcel p) {
        stack.add(p);
    }

    public Parcel pop() {
        if (isEmpty()) return null;
        return stack.remove(stack.size() - 1);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}

// ------------------ HASH TABLE ------------------
class HashEntry {
    String key;
    Parcel value;
    HashEntry next;

    public HashEntry(String key, Parcel value) {
        this.key = key;
        this.value = value;
    }
}

class ManualHashTable {
    private HashEntry[] buckets;
    private int capacity;

    public ManualHashTable(int capacity) {
        this.capacity = capacity;
        this.buckets = new HashEntry[capacity];
    }

    private int getBucketIndex(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(String key, Parcel value) {
        int index = getBucketIndex(key);
        HashEntry head = buckets[index];
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }
        HashEntry newEntry = new HashEntry(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
    }

    public Parcel get(String key) {
        int index = getBucketIndex(key);
        HashEntry head = buckets[index];
        while (head != null) {
            if (head.key.equals(key)) return head.value;
            head = head.next;
        }
        return null;
    }

    public void printAll() {
        for (HashEntry head : buckets) {
            while (head != null) {
                System.out.println(" → " + head.value);
                head = head.next;
            }
        }
    }
}

class ParcelTracker {
    ManualHashTable table = new ManualHashTable(32);

    public void track(Parcel p) {
        table.put(p.id, p);
        log("Tracking parcel: " + p);
    }

    public Parcel get(String id) {
        return table.get(id);
    }

    public void printAllTracked() {
        table.printAll();
    }

    private void log(String msg) {
        System.out.println("[Tracker Log] " + msg);
    }
}

// ------------------ CITY BST ------------------
class CityBST {
    private class TreeNode {
        String cityName;
        List<Parcel> parcels;
        TreeNode left, right;

        TreeNode(String cityName) {
            this.cityName = cityName;
            this.parcels = new ArrayList<>();
            left = right = null;
        }
    }

    private TreeNode root;

    public CityBST() {
        root = null;
    }

    // Paketi BST'ye ekler
    public void addParcel(Parcel p) {
        root = insert(root, p);
        p.status = "Dispatched";
    }

    private TreeNode insert(TreeNode node, Parcel p) {
        if (node == null) {
            TreeNode newNode = new TreeNode(p.destination);
            newNode.parcels.add(p);
            return newNode;
        }

        int cmp = p.destination.compareToIgnoreCase(node.cityName);
        if (cmp < 0) {
            node.left = insert(node.left, p);
        } else if (cmp > 0) {
            node.right = insert(node.right, p);
        } else {
            node.parcels.add(p);
        }
        return node;
    }

    // Verilen şehirdeki paketleri döner (null değil, boş liste bile olabilir)
    public List<Parcel> getParcels(String city) {
        TreeNode node = search(root, city);
        if (node != null) return node.parcels;
        else return new ArrayList<>();
    }

    private TreeNode search(TreeNode node, String city) {
        if (node == null) return null;
        int cmp = city.compareToIgnoreCase(node.cityName);
        if (cmp == 0) return node;
        else if (cmp < 0) return search(node.left, city);
        else return search(node.right, city);
    }

    // Ağacı sıralı şekilde yazdırır
    public void printAllParcels() {
        System.out.println("City BST Parcel Distribution:");
        inOrder(root);
    }

    private void inOrder(TreeNode node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.println("[" + node.cityName + "] (" + node.parcels.size() + " parcels)");
        for (Parcel p : node.parcels) {
            System.out.println(" → " + p);
        }
        inOrder(node.right);
    }
}

// ------------------ ROUTE ------------------
class RouteNode {
    String location;
    RouteNode next;

    public RouteNode(String location) {
        this.location = location;
    }
}

class RouteCircularList {
    private RouteNode head = null;
    private RouteNode current = null;
    public int rotationInterval = 3;

    public void loadFromConfig(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            RouteNode prev = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("interval=")) {
                    rotationInterval = Integer.parseInt(line.split("=")[1].trim());
                    continue;
                }

                if (line.isEmpty()) continue;
                RouteNode node = new RouteNode(line);
                if (head == null) head = node;
                else prev.next = node;
                prev = node;
            }
            if (prev != null) {
                prev.next = head;
                current = head;
            } else {
                System.out.println("[Route Log] No cities loaded in route.");
                head = null;
                current = null;
            }
        } catch (IOException e) {
            System.out.println("[Route Log] Error reading config: " + e.getMessage());
            head = null;
            current = null;
        }
    }

    public boolean isLoaded() {
        return head != null && current != null;
    }

    public void rotateAndLogDeliveries() {
        if (current == null) {
            System.out.println("[Route Log] No route loaded.");
            return;
        }

        for (int i = 0; i < rotationInterval; i++) {
            System.out.println("[Route Log] Delivering to: " + current.location);
            current = current.next;
        }

        System.out.println("[Route Log] Active terminal: " + current.location);
    }

    public String getCurrentLocation() {
        return current != null ? current.location : null;
    }
}

// ------------------ MAIN SIMULATION ------------------
public class ParcelSortX {
    private static int randomParcelPerTick = 2;

    private static int totalGenerated = 0;
    private static int totalDispatched = 0;
    private static int totalReturned = 0;

    private static void log(String msg) {
        System.out.println("[Main Log] " + msg);
    }

    private static String[] possibleCities = {"Istanbul", "Ankara", "Izmir", "Bursa"};

    private static Parcel generateRandomParcel(int tick, int idSuffix) {
        Random rand = new Random();
        String id = "P" + tick + String.format("%02d", idSuffix);
        String dest = possibleCities[rand.nextInt(possibleCities.length)];
        int priority = rand.nextInt(3) + 1;
        totalGenerated++;
        return new Parcel(id, dest, priority, tick);
    }

    public static void loadGlobalConfig(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("randomParcelPerTick=")) {
                    randomParcelPerTick = Integer.parseInt(line.split("=")[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("[Config Log] Error reading config: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        loadGlobalConfig("config.txt");

        ParcelQueue incoming = new ParcelQueue();
        ParcelStack sortingStack = new ParcelStack();
        CityBST cityBST = new CityBST();
        ParcelTracker tracker = new ParcelTracker();
        RouteCircularList route = new RouteCircularList();
        route.loadFromConfig("route_config.txt");

        int maxTick = 10;

        for (int tick = 1; tick <= maxTick; tick++) {
            log("Tick " + tick + " started");

            // 1) Yeni rastgele paket üret
            for (int i = 0; i < randomParcelPerTick; i++) {
                Parcel p = generateRandomParcel(tick, i);
                incoming.enqueue(p);
                tracker.track(p);
                log("Generated and queued: " + p);
            }

            // 2) Gelen kuyruktan stack'e aktar (sıraya göre)
            while (!incoming.isEmpty()) {
                Parcel p = incoming.dequeue();
                sortingStack.push(p);
                log("Moved from queue to stack: " + p);
            }

            // 3) Stack'ten paketleri işleyip şehirlere dağıt (BST'ye ekle)
            while (!sortingStack.isEmpty()) {
                Parcel p = sortingStack.pop();

                // Eğer iade sayısı 3 veya üstü ise paket sistemden atılır
                if (p.returnCount >= 3) {
                    log("Discarding parcel due to too many returns: " + p);
                    continue;
                }

                cityBST.addParcel(p);
                totalDispatched++;
                log("Dispatched parcel to BST city: " + p);
            }

            // 4) Rotasyonu yap ve aktif terminali yazdır
            if (route.isLoaded()) {
                route.rotateAndLogDeliveries();
            } else {
                log("No delivery route loaded.");
            }

            // 5) Örnek: 1 paket iade edelim (demo amaçlı)
            // Normalde iade mantığı daha kapsamlı olabilir
            if (tick % 4 == 0) { // 4. tickte iade testi
                Parcel someParcel = new Parcel("ReturnedP" + tick, "Ankara", 2, tick);
                someParcel.returnCount++;
                someParcel.status = "Returned";
                sortingStack.push(someParcel);
                totalReturned++;
                log("Demo: Parcel returned and pushed back to stack: " + someParcel);
            }

            log("Tick " + tick + " ended\n");
        }

        // Son rapor
        System.out.println("\n===== FINAL REPORT =====");
        System.out.println("Total parcels generated: " + totalGenerated);
        System.out.println("Total parcels dispatched: " + totalDispatched);
        System.out.println("Total parcels returned: " + totalReturned);
        cityBST.printAllParcels();
        tracker.printAllTracked();
    }
}
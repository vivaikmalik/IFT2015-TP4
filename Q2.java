import java.util.*;

public class Q2 {
    private RBNode root;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class RBNode {
        int key;
        String value;
        boolean color;
        int accessCount;
        long lastAccessTime;
        RBNode left, right, parent;

        public RBNode(int key, String value, long timestamp) { // initialization of constructor node
            this.key = key;
            this.value = value;
            this.color = RED;
            this.accessCount = 1;
            this.lastAccessTime = timestamp;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

    }

    public Q2() {
        this.root = null; // initialization of constructor - root is null
    }

    public void insert(int key, String value, long timestamp) {
        if (this.root == null) {
            this.root = new RBNode(key, value, timestamp); // if root is null, create a new node
            this.root.color = BLACK; // root is black
            this.root.parent = null; // root has no parent
        } else {
            RBNode parentNode = null;
            RBNode node = this.root;
            while (node != null) {
                parentNode = node;
                if (key < node.key) {
                    node = node.left;
                } else if (key > node.key) {
                    node = node.right;
                } else { // cannot have duplicates in red black trees
                    node.accessCount++;
                    node.lastAccessTime = timestamp;
                    return;
                }
            }
            RBNode newNode = new RBNode(key, value, timestamp); // create a new node
            newNode.parent = parentNode; // new node has no parent

            if (key < parentNode.key) {
                parentNode.left = newNode;
            } else {
                parentNode.right = newNode;
            } // define the new left and right

            this.fixInsert(newNode);
        }
        private void fixInsert(RBNode node) {
            
        }
    }

    public String get(int key, long timestamp) {
        // TODO:
        return null;
    }

    public boolean delete(int key) {
        // TODO:
        return false;
    }

    public List<String> getRangeValues(int minKey, int maxKey) {
        // TODO:
        return new ArrayList<>();
    }

    public int getBlackHeight() {
        // TODO:
        return 0;
    }

    public boolean verifyProperties() {
        // TODO:
        return false;
    }

    public List<Integer> getMostAccessedKeys(int k) {
        // TODO:
        return new ArrayList<>();
    }

    public void evictOldEntries(long currentTime, long maxAge) {
        // TODO:
    }

    public int countRedNodes() {
        // TODO:
        return 0;
    }

    public Map<String, Integer> getColorStatisticsByLevel() {
        // TODO:
        return new HashMap<>();
    }

}

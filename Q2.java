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

    }

    private void fixInsert(RBNode node) {
        if (node == this.root) {
            node.color = BLACK;
            return;
        }
        // loop below is to fix RED-RED issues that arise since insertion always red, if
        // parent is red. rearrangement necessary
        while (node.parent != null && node.parent.color == RED) {

            RBNode parent = node.parent;
            RBNode grandParent = parent.parent; // if parent is red, grandparent must exist (root cannot be red)
            RBNode uncle = grandParent.right == parent ? grandParent.left : grandParent.right; // uncle is the other

            if (uncle != null && uncle.color == RED) {
                parent.color = BLACK;
                uncle.color = BLACK;
                grandParent.color = RED;
                node = grandParent;
            } else {
                if (node == parent.left && parent == grandParent.left) {
                    this.rotateRight(parent);
                    parent.color = BLACK;
                }
                if (node == parent.right && parent == grandParent.right) {
                    this.rotateLeft(parent);
                    parent.color = BLACK;
                }
                if (node == parent.left && parent == grandParent.right) {
                    this.rotateRight(parent);
                    this.rotateLeft(grandParent);
                    node.color = BLACK;
                }
                if (node == parent.right && parent == grandParent.left) {
                    this.rotateLeft(parent);
                    this.rotateRight(grandParent);
                    node.color = BLACK;
                }
            }
        }
    }

    public String get(int key, long timestamp) {
        if (this.root == null) {
            return null;
        }
        RBNode node = this.root;
        while (node != null) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                node.accessCount++;
                node.lastAccessTime = timestamp;
                return node.value;
            }
        }
        return null;
    }

    public boolean delete(int key) {
        // TODO:
        return false;
    }

    public List<String> getRangeValues(int minKey, int maxKey) {
        if (this.root == null) {
            return new ArrayList<>();
        }
        List<String> values = new ArrayList<>();
        RBNode node = this.root;
        while (node != null) { // end while loop when node is null (end of tree)
            if (node.key < minKey) {
                node = node.right; // if current node is less than the min, go to the right
            } else if (node.key > maxKey) {
                node = node.left; // if current node is greater than the max, go to the left
            } else {
                values.add(node.value); // if current node is between min and max, add it to the list
                node = node.right; // go to the right
            }
        }
        return values;
    }

    public int getBlackHeight() {
        if (this.root == null) {
            return 0;
        }
        int blackHeight = 0;
        RBNode node = this.root;
        while (node != null) { // end while loop when node is null (end of tree)
            if (node.color == BLACK) { // if current node is black, increment black height
                blackHeight++;
            }
            node = node.left; // go to the left. all paths will have same number of black nodes since
                              // red-black tree property
        }
        return blackHeight;
    }

    public boolean verifyProperties() {
        if (this.root == null) {
            return true; // if root does not exist. valid
        }
        if (this.root.color != BLACK) { // root must be black
            return false;
        }
        if (RBTverifyProperties(this.root) == -1) {
            return false;
        }
        return true;
    }

    private int RBTverifyProperties(RBNode node) {
        /**
         * recursive function to check properties of red-black tree (back to back red
         * nodes, root is black, all paths have same number of black nodes)
         * function returns an int because we must count black height on both sides of
         * the tree
         * to check whether this property holds
         */
        if (node == null) {
            return 1; // node does not exist. which is valid
        }
        if (node.color == RED) {
            if (node.left != null && node.left.color == RED) {
                return -1; // if left child is red, return false
            }
            if (node.right != null && node.right.color == RED) {
                return -1; // if right child is red, return false
            } // cannot have red-red nodes
        } // if node is black, check left and right
        int leftBlackHeight = RBTverifyProperties(node.left); // check left
        int rightBlackHeight = RBTverifyProperties(node.right); // check right
        if (leftBlackHeight == -1 || rightBlackHeight == -1) {
            return -1; // if either side is invalid, return false
        }
        if (leftBlackHeight != rightBlackHeight) {
            return -1; // if left and right black heights are not equal, return false
        }
        if (node.color == BLACK)
            return leftBlackHeight + 1;
        return leftBlackHeight;
    }

    public List<Integer> getMostAccessedKeys(int k) {
        Map<Integer, Integer> keyCountMap = new HashMap<>(); // initialize map
        collectMapOfKeyCounts(this.root, keyCountMap); // collect access counts recursively
        List<Integer> keys = new ArrayList<>(); // initialize list
        for (Map.Entry<Integer, Integer> entry : keyCountMap.entrySet()) {
            keys.add(entry.getKey()); // populate keys array from map
        }
        keys.sort((a, b) -> keyCountMap.get(b) - keyCountMap.get(a)); // sort keys by access count
        return keys.subList(0, Math.min(k, keys.size())); // return top k keys
    }

    /// gemini used for this helper function which helps collect the key counts per
    /// node
    private void collectMapOfKeyCounts(RBNode node, Map<Integer, Integer> keyCountMap) {
        if (node == null) {
            return; // if node does not exist, get out of helper function
        }
        // In-order traversal
        collectMapOfKeyCounts(node.left, keyCountMap); // first check left subtree

        // Store the key and its access count
        keyCountMap.put(node.key, node.accessCount);

        collectMapOfKeyCounts(node.right, keyCountMap); // then check right subtree
    }

    public void evictOldEntries(long currentTime, long maxAge) {
        // in order traversal
        List<Integer> keysToDelete = new ArrayList<>();
        if (this.root == null) {
            return;
        }
        evictOldEntries(this.root, currentTime, maxAge, keysToDelete);
        for (int key : keysToDelete) {
            this.delete(key);
        }
    }

    private void evictOldEntries(RBNode node, long currentTime, long maxAge, List<Integer> keysToDelete) {
        /*
         * In order recursive traversal to delete nodes as requested in the PDF
         * questionnaire
         */
        if (node == null) {
            return;
        }
        evictOldEntries(node.left, currentTime, maxAge, keysToDelete);
        if (node.lastAccessTime < currentTime - maxAge) {
            keysToDelete.add(node.key);
        }
        evictOldEntries(node.right, currentTime, maxAge, keysToDelete);
    } // gemini used. at first i had deleted the nodes insteaf of adding directly to
      // the list
      // but gemini made me realize that i should add the keys to be deleted to a list
      // before deleting them all.
      // deleting while going recursively could restructure the tree, and cause errors
      // etc.

    public int countRedNodes() {
        return countRedNodes(this.root);
    }

    private int countRedNodes(RBNode node) {
        /*
         * In order recursive traversal to count red nodes as requested in the PDF
         * questionnaire. This helper function follows the same logis as the last 2
         * helper functions above.
         */
        if (node == null) {
            return 0;
        }
        int count = countRedNodes(node.left);
        if (node.color == RED) {
            count++;
        }
        count += countRedNodes(node.right);
        return count;
    }

    public Map<String, Integer> getColorStatisticsByLevel() {
        Map<String, Integer> map = new TreeMap<>(); // initialize a tree map

        return getColorStatisticsByLevel(this.root, 0, map); // start recursivity at level = 0
    }

    private Map<String, Integer> getColorStatisticsByLevel(RBNode node, int level, Map<String, Integer> map) {
        /*
         * Recursive method to cound red notes at each level. This uses a tree map to
         * store the results.
         * The map is sorted by key (level) in ascending order. The inputs are the node
         * to start at, the level of the node, and the map containing the current counts
         * 
         */
        if (node == null) {
            return map;
        }
        if (node.color == RED) {
            String levelString = String.valueOf(level); // convert to string like PDF states

            if (map.get(levelString) == null) { // check if key exists. if not create it and set value to 1
                map.put(levelString, 1);
            } else {
                map.put(levelString, map.get(levelString) + 1); // if key exists, increment value by 1
            }
        }
        getColorStatisticsByLevel(node.left, level + 1, map); // recursively check left subtree
        getColorStatisticsByLevel(node.right, level + 1, map); // recursively check right subtree
        return map;
    }

}

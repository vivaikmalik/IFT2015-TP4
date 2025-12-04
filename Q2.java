
/*
2 * ACADEMIC INTEGRITY ATTESTATION 
3 *
7 * [ ] I have used one or more generative AI tools. 
8 * Details below:
9 *
10 * Tool(s) used: Gemini 3.0
11 *
12 * Reason(s) for use:
13 * Gemini was used to help with certain methods in this class and to review the logic once the methods were created.
14 * The methods that used Gemini have comments / docstrings in them that detail why / how the LLM was used.
15 *
16 * Affected code sections:
17 * ___________________________________________________
18 * ___________________________________________________
19 */
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
                } // while loop used to find parent of new node
            }
            RBNode newNode = new RBNode(key, value, timestamp); // create a new node
            newNode.parent = parentNode; // initialize parent of new node as last node in the while loop

            if (key < parentNode.key) {
                parentNode.left = newNode;
            } else {
                parentNode.right = newNode;
            } // define as left or right child of parent, depending on key value

            this.fixInsert(newNode); // fix any red-black tree properties that may have been violated post insertion
        }
    }

    private void fixInsert(RBNode node) {
        /*
         * 
         * this helper function is used to fix the red-black tree properties after an
         * element has been inserted
         * 
         */
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

            if (uncle != null && uncle.color == RED) { // if uncle is red, simply do some recoloring, then recheck with

                parent.color = BLACK;
                uncle.color = BLACK;
                grandParent.color = RED;
                node = grandParent; // node = grandparent.node, recheck with while loop. recolour if necesary. worst
                                    // case: root stays black.
            } else { // if uncle is black or null
                if (parent == grandParent.left) {
                    if (node == parent.right) { // if node is right child of parent, but parent is left child of
                                                // grandparent: L-R situation
                        node = parent; // update node to parent for rotation
                        leftRotate(node); // rotate left around parent so that we have L-L
                        parent = node.parent; // update parent of node
                        grandParent = parent.parent; // update grandparent to node

                    }
                    // if code does not go into above if. we have L-L situation. this is the normal
                    // case. simply recolour and rotate.
                    parent.color = BLACK; // recolor parent to black
                    grandParent.color = RED; // recolor grandparent to red
                    rightRotate(grandParent); // rotate right around grandparent since now in L-L situation
                } else {
                    // below we have mirror situation of above scenario. R-L situation - no comments
                    // necessary
                    if (node == parent.right) {
                        node = parent;
                        rightRotate(node);
                        parent = node.parent;
                        grandParent = parent.parent;

                    }
                    parent.color = BLACK;
                    grandParent.color = RED;
                    leftRotate(grandParent);
                }

            }
        }
    }

    private void leftRotate(RBNode node) {
        /*
         * 
         * this helper function is used to rotate the tree to the left
         * 
         */
        if (node == null || node.right == null)
            return; // this ensures that the node is not null and has a right child

        RBNode rightChild = node.right; // assign right child value
        node.right = rightChild.left; // assign left subtree of right child to right of OG node

        if (rightChild.left != null) {
            rightChild.left.parent = node; // update parent of left subtree of right child of OG node
        }
        rightChild.parent = node.parent; // update parent of right child of OG node

        if (node.parent == null) {
            this.root = rightChild; // update root pointer if OG node is root
        } // this needs to be checked before the else if below

        else if (node == node.parent.left) {
            node.parent.left = rightChild; // if OG node left child of parent, update pointer so that it now points to
                                           // the right child
        }

        else {
            node.parent.right = rightChild; // if OG node right child of parent, update pointer so that it now points
                                            // to the right child
        }
        rightChild.left = node; // OG node now left child of right child
        node.parent = rightChild; // update parent pointer of OG node
    }

    private void rightRotate(RBNode node) {
        /*
         * 
         * this helper function is used to rotate the tree to the right
         * there are no comments below since it is the EXACT SAME AS THE FUNCTION ABOVE,
         * just mirrored
         */
        if (node == null || node.left == null)
            return;

        RBNode leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;

        if (node.parent == null) {
            this.root = leftChild;
        }

        else if (node == node.parent.left) {
            node.parent.left = leftChild;
        }

        else {
            node.parent.right = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    public String get(int key, long timestamp) {
        if (this.root == null) {
            return null; // if root is null, return null
        }
        RBNode node = this.root;
        while (node != null) {
            if (key < node.key) {
                node = node.left; // if key is less than node key, move to left child
            } else if (key > node.key) {
                node = node.right; // if key is greater than node key, move to right child
            } else {
                node.accessCount++; // increment accessCount
                node.lastAccessTime = timestamp; // update lastAccessTime
                return node.value; // return value
            }
        }
        return null; // if not found, return null (key does not exist)
    }

    public boolean delete(int key) {
        /*
         * 
         * this function is used to delete a node from the red-black tree
         * for this function, we need
         * 1 - finding the node that will replace the deleted node
         * 2 - deleting the node
         * 3 - fixing the red-black tree properties. this will all be done through
         * different helper functions
         * 
         */

        return false;
    }

    private RBNode findSuccessor(RBNode node) {
        /*
         * 
         * this helper function is used to find the successor WHEN THERE ARE TWO
         * CHILDREN of the node to be deleted
         * in fact, if note to be deleted is a leaf nothing to be done, except for
         * checking RBT properties
         * if node has one child, we can just replace it with its child
         * if node has two children, we need to find the successor.
         * this method assumes that node will be repalced by smallest node in right
         * subtree
         * 
         */
        if (node == null) {
            return null;
        }
        // Traverse down the leftmost path
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void replaceSubtree(RBNode node, RBNode child) {
        /*
         * 
         * this helper function is used to repalce the subtree with node to be deleted,
         * with the subtree of child
         * 
         */
        if (node.parent == null) {
            this.root = child; // if node is root, update root pointer to child
        } else if (node == node.parent.left) {
            node.parent.left = child; // if node is left child, then update parent's left child to subtree
        } else {
            node.parent.right = child; // if node is right child, then update parent's right child to subtree
        }
        child.parent = node.parent; // update child's parent pointer to node's parent
    }

    public List<String> getRangeValues(int minKey, int maxKey) {
        if (this.root == null) {
            return new ArrayList<>();
        }
        List<String> values = new ArrayList<>();
        getRangeValuesHelper(this.root, minKey, maxKey, values);
        return values;
    }

    private void getRangeValuesHelper(RBNode node, int minKey, int maxKey, List<String> values) {
        /*
         * 
         * this helper function is used to get the values of the nodes in the range. it
         * uses a recursive in order traversal of the tree
         * this ensures that the values are returned in the sorted order required in the
         * question
         * LEFT SUBTREE -> ROOT -> RIGHT SUBTREE
         */
        if (node == null) { // exit if null node is null
            return;
        }
        if (node.key > minKey) { // if node key is greater than the min. there could be possible values in left
                                 // subtree smaller than minKey
            getRangeValuesHelper(node.left, minKey, maxKey, values);
        }
        if (node.key >= minKey && node.key <= maxKey) {
            values.add(node.value);
        }
        if (node.key < maxKey) { // if node key is smaller than the max. there could be possible values in right
                                 // subtree greater than maxKey
            getRangeValuesHelper(node.right, minKey, maxKey, values);
        }
    }

    public int getBlackHeight() { // we have coded this function assuming that the Red-Black tree properties ARE
                                  // ALL SATISFIED. nothing specified in assignment. so i believe this is correct.
                                  // anyways, all functions in this class fix red-black tree properties as they
                                  // are called. thus, whenever this function is called, RBT properties will
                                  // always be satisfied.
                                  // so we can take the shortcut of just counting black nodes on leftmost path.
        if (this.root == null) {
            return 0;
        }
        int blackHeight = 0;
        RBNode node = this.root;
        while (node != null) { // end while loop when node is null (end of tree)
            if (node.color == BLACK) { // if current node is black, increment black height
                blackHeight++;
            }
            node = node.left; // go to the left. all paths will have same number of black nodes since this is
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
        if (RBTverifyProperties(this.root) == -1) { // if RBTverifyProperties returns -1, then it is not a valid
                                                    // red-black tree
            return false;
        }
        return true; // if all properties are satisfied, return true
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
        Map<Integer, Integer> keyCountMap = new HashMap<>(); // initialize HashMap

        collectMapOfKeyCounts(this.root, keyCountMap); // collect access counts recursively
        List<Integer> keys = new ArrayList<>(); // initialize list
        // populate list with keys from map
        for (Map.Entry<Integer, Integer> entry : keyCountMap.entrySet()) {
            keys.add(entry.getKey()); // populate keys array from map
        }
        // sort keys by access count
        keys.sort((a, b) -> keyCountMap.get(b) - keyCountMap.get(a)); // gemini used to help with this sorting line of
                                                                      // code
        return keys.subList(0, Math.min(k, keys.size())); // return top k keys, or all keys if k is gretaer than the
                                                          // size of the list
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

        // in order traversal : left, root, right
    }

    public void evictOldEntries(long currentTime, long maxAge) {
        // in order traversal
        List<Integer> keysToDelete = new ArrayList<>();
        if (this.root == null) {
            return;
        }
        evictOldEntries(this.root, currentTime, maxAge, keysToDelete);
        for (int key : keysToDelete) {
            this.delete(key); // use delete function to delete nodes from tree
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
        if (currentTime - node.lastAccessTime > maxAge) {
            keysToDelete.add(node.key);
        }
        evictOldEntries(node.right, currentTime, maxAge, keysToDelete);
    } // gemini used. at first i had deleted the nodes insteaf of adding directly to
      // the list
      // but gemini made me realize that i should add the keys to be deleted to a list
      // before deleting them all.
      // deleting while going recursively could restructure the tree, and cause errors
      // etc., would need to recheck all properties each time, and recheck which keys
      // to delete each time

    public int countRedNodes() {
        return countRedNodes(this.root);
    }

    private int countRedNodes(RBNode node) {
        /*
         * In order recursive traversal to count red nodes as requested in the PDF
         * questionnaire. This helper function follows the same logic as the last 2
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

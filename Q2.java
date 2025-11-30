import java.util.*;

public class Q2 {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class RBNode {
        int key;
        String value;
        boolean color;
        int accessCount;
        long lastAccessTime;
        RBNode left, right, parent;

        public RBNode(int key, String value, long timestamp) {
        }
    }

    public Q2() {
    }

    public void insert(int key, String value, long timestamp) {
        // TODO:
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

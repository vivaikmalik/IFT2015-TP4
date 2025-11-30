
import java.util.*;


//for this question a lot of my code is inspired by what has been done on the website GeeksforGeeks for vairous problems.



public class Q1 {

    //Q1 - DONE
    public static boolean hasCycle(int[][] graph) {
        boolean[] visited = new boolean[graph.length];
        boolean[] backtrack_log = new boolean[graph.length];
        
        for(int i=0; i < graph.length; i++){
            if(!visited[i] && helperQ1(graph, i, visited, backtrack_log))
                return true;

        }
        return false;
    }

    static boolean helperQ1(int[][] graph, int node, boolean[] visited, boolean[] backtrack_log){
        if (backtrack_log[node]) return true;

        if (visited[node]) return false;

        visited[node] = true;
        backtrack_log[node] = true;

        for (int dep: graph[node]){
            if(helperQ1(graph, dep, visited, backtrack_log)){
                return true;

            }


        }

        backtrack_log[node] = false;
        return false;

    }


    //DFS ALGORITHM MADE JUST TO HELP ME FIGURE OUT QUESTION 1 
    public ArrayList<Integer> dfs_algorithm(int[][] graph ){
        boolean[] visited_nodes = new boolean[graph.length];
        ArrayList<Integer> path = new ArrayList<>();
        dfs_helper(graph, visited_nodes, 0, path);
        return path;
    }

    private void dfs_helper(int[][] graph, boolean[] visited_nodes, int current, ArrayList<Integer> path){
        visited_nodes[current] = true;
        path.add(current);

        for (int i=0; i< graph[current].length; i++){
            if (graph[current][i]==1  && !visited_nodes[i]){
                dfs_helper(graph, visited_nodes, i, path);
            }
        }
    }

    //Q2 - DONE
    public static List<List<Integer>> findAllCycles(int[][] graph) {
        List<List<Integer>> cycles_total = new ArrayList<>();
        for (int i=0; i<graph.length; i++){
            List<Integer> path = new ArrayList<>();
            helperQ2(graph, i, path, cycles_total);
        }

        Collections.sort(cycles_total, (c1, c2)-> {
            int minlenght= Math.min(c1.size(), c2.size());
            for (int i=0; i<minlenght; i++){
                if (!c1.get(i).equals(c2.get(i))){
                    return c1.get(i) - c2.get(i);
                }
            }

            return c1.size() - c2.size();
        });
        return cycles_total;
    }

    public static void helperQ2(int[][] graph, int node, List<Integer> path, List<List<Integer>>cycles_total){
        if (path.contains(node)){
            //get sublist to get the cycle from main list:  find start index and then length is the size of the path 
            int start_of_cycle = path.indexOf(node);
            List<Integer> sublist = new ArrayList<>(path.subList(start_of_cycle, path.size()));

            int min = sublist.get(0);
            int minindex = 0;
            for (int i=1; i<sublist.size(); i++){
                if (sublist.get(i) <min){
                    min = sublist.get(i);
                    minindex=i;
                }
            }

            List<Integer> rotation = new ArrayList<>();
            for (int i=0; i<sublist.size(); i++){
                rotation.add(sublist.get((minindex+i) % sublist.size()));
            }

            cycles_total.add(rotation);

            return;
        }
        path.add(node);
        //recursively redo for all the nodes that are connected to our original node

        for (int dep: graph[node]){
                helperQ2(graph, dep, path, cycles_total);
            }

        path.remove(path.size()-1); //get out of the cycle once recorded (backtrack)
        
    }


    //Q3  - DONE 
    //from online research Tarjan's algorithm is the best so using it 

    static int time; 
    static List<List<Integer>> result;
    static LinkedList<Integer> adj[];

    public static List<List<Integer>> stronglyConnectedComponents(int[][] graph) {
        adj = new LinkedList[graph.length]; //create adjency list

        for (int i=0; i<graph.length; i++) adj[i]= new LinkedList<>(); //ajency matrix into a list 
        for (int i=0; i< graph.length; i++){
            for (int j :graph[i]){ //if grah[i][j] ==1 then theres edge i -> j
                adj[i].add(j);
            }
        }

        int[] disc = new int[graph.length]; //discovery time to go to a node 
        int[] low = new int[graph.length]; //lowest discovery time 
        boolean[] stackMember = new boolean[graph.length];
        Stack<Integer> st = new Stack<>();

        Arrays.fill(disc, -1); 
        Arrays.fill(low, -1);
        time = 0;
        result = new ArrayList<>();

        for (int i = 0; i< graph.length; i++){
            if (disc[i]==-1){
                TarjanDFS(i, low, disc, stackMember, st);
            }
        }

        Collections.sort(result, (scc1, scc2)-> {
            int sizecomp = Integer.compare(scc2.size(), scc1.size());
            if (sizecomp !=0){
                return sizecomp;
            }
            //used chagpt for tie breaker
            int min1=scc1.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
            int min2 = scc2.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);

            return Integer.compare(min1, min2);
        });
    
        return result;
    }

    private static void TarjanDFS(int u, int[] low, int[] disc, boolean[] stackMember, Stack<Integer> st){
        disc[u] = low[u]= time++;
        stackMember[u] = true;
        st.push(u);

        for (int v: adj[u]){
            if (disc[v]==-1){
                TarjanDFS(v, low, disc, stackMember, st);
                low[u] = Math.min(low[u], low[v]);
            }

            else if (stackMember[v]){
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]){
            List<Integer> scc =  new ArrayList<>();
            int w;
            do {
                w = st.pop();
                stackMember[w] = false;
                scc.add(w);
            } while (w!=u);

            result.add(scc);
        }
    }


    //Q4 - should maybe redo  
    public static int countReachableNodes(int[][] graph, int start) {
        boolean[] visited = new boolean[graph.length];
        dfsq4(graph, start, visited);

        int count = 0;
        for (boolean v: visited){
            if (v){
                count++;
            }
        }
        return count;

        
   
    }

    private static void dfsq4(int[][] graph, int current, boolean[] visited){
        if (visited[current]){
            return;
        }

        visited[current] = true;

        for (int dep: graph[current]){
            dfsq4(graph, dep, visited);
        }
    }

    //Q5 -  DIFFICULT
    public static List<Integer> findBridgeNodes(int[][] graph) {
       int fullscc = stronglyConnectedComponents(graph).size();
        List<Integer> bridgeNodes = new ArrayList<>();
        
        for (int u =0; u<graph.length; u++){
            int[][] newgraph = new int[graph.length][graph.length];
            for (int i=0; i<graph.length; i++){
                    if(i==u){
                        newgraph[i]= new int[0];
                    } else {
                        List<Integer> newdep=new ArrayList<>();
                        for (int v:graph[i]){
                            if (v!=u){
                                newdep.add(v);
                            }
                        }

                        newgraph[i] = newdep.stream().mapToInt(x->x).toArray();
                    }
            }
            int newscc = stronglyConnectedComponents(newgraph).size();
            if (newscc > fullscc) bridgeNodes.add(u);
        }

        Collections.sort(bridgeNodes);
        return bridgeNodes;
    }

    //Q6 - DONE
    public static Map<Integer, Integer> getFinishingTimes(int[][] graph) {
        boolean[] visited = new boolean[graph.length];
        Map<Integer, Integer> finishingTimes = new HashMap<>();
        int[] time = {1}; //allowing change whne running dfs

        for (int node=0; node<graph.length; node++){
            if (!visited[node]){
                dfs_q6(graph, visited, node, finishingTimes, time);
            }
        }

        return finishingTimes;
    }

    //use my dfs function as a basis to redo it but assign finishing time when a node has seen all of its connected nodes 
    private static void dfs_q6(int[][] graph, boolean[] visited, int current, Map<Integer, Integer> finishingTimes, int[] time){
        visited[current] = true;

        for (int dep1: graph[current]){
            if (!visited[dep1]){
                dfs_q6(graph, visited, dep1 , finishingTimes, time);
            }
        }

        finishingTimes.put(current, time[0]);
        time[0]++;
    }

    //Q7 - DONE
    public static boolean canInstallAll(int[][] graph, List<Integer> broken) {
        for (int node=0; node< graph.length; node++){
            if (!broken.contains(node)){
                boolean[] visited_nodes = new boolean[graph.length];
                if (dfs_q7(graph, node, broken, visited_nodes)){
                    return false;
                }
            }
        }
        return true;
    }
    //same thing as before reuisng logic of dfs but modifying it slightly to fit the question 
    private static boolean dfs_q7(int[][]graph, int current, List<Integer> broken, boolean[] visited_nodes){
        visited_nodes[current] = true;

        if (broken.contains(current)){
            return true;
        } //can't install the package 

        for (int dep2: graph[current]){
            if (!visited_nodes[dep2]){
                if (dfs_q7(graph, dep2, broken, visited_nodes)){
                    return true;
                }
            }
        }

        return false;
    }
    
    //Q8 - am i returning the list in increasing order ??? need to check 
    public static List<Integer> findMinimalDependencySet(int[][] graph, int target) {
        boolean[] visited = new boolean[graph.length];
        //find all the dependencies doing DFS
        Stack<Integer> s = new Stack<>();
        s.push(target);
        visited[target] = true;

        while(!s.isEmpty()){
            int node = s.pop();
            for (int dep3: graph[node]){
                if (!visited[dep3]){
                    visited[dep3] =true;
                    s.push(dep3);
                }
            }
        }

        //find minimal out of all the dependencies
        List<Integer> minimal = new ArrayList<>();
        for (int potential=0; potential< graph.length; potential++){
            if (!visited[potential] || potential==target) continue;
            boolean isroot = true;

            for(int other=0; other<graph.length; other++){
                if (other!= potential && visited[other]){
                    for (int dep: graph[other]){
                        if (dep ==potential){
                            isroot=false;
                            break;
                        }
                    }
                }
                if (!isroot) break;
 
            }

            if (isroot) minimal.add(potential);
            
        }

        return minimal; 

    }

    //Q9 - used (done )
    public static int longestDependencyChain(int[][] graph) {
        int[] longest_path = new int[graph.length];
        boolean[] accounted = new boolean[graph.length];
        boolean[] onpath = new boolean[graph.length];

        if (graph==null|| graph.length==0){
            return 0;
        }

        int longest = 0; 

        for (int u=0; u< graph.length; u++){
            longest = Math.max(longest, dfsq9(graph, u, longest_path, accounted, onpath));
        }
        return longest -1;
    }

    private static int dfsq9(int[][] graph, int current, int[] longest_path, boolean[] accounted, boolean[] onpath){

        if (accounted[current]){
            return longest_path[current];
        }

        onpath[current] = true;
        int best = 1; //minimum is the node itself 

        for (int next: graph[current]){
            if (onpath[next]){
                    continue;
            }

                int potential = 1 + dfsq9(graph, next, longest_path, accounted, onpath);
                best = Math.max(best, potential);
            
        }

        onpath[current] = false;
        longest_path[current] =best;
        accounted[current]=true;

        return best;
    }

    //Q10 - used (done )
    public static List<Integer> findAllSourceNodes(int[][] graph) {
        ArrayList<Integer> nodes = new ArrayList<>();

        int[] numdep = new int[graph.length];

        for (int i=0; i<graph.length; i++){
                for (int j: graph[i]){
                    if (j>=0 && j<graph.length){
                    numdep[j]++;
                    }
                }
            }

            for (int j=0; j<graph.length; j++){
                if (numdep[j]==0){
                    nodes.add(j);
                }
            }
        return nodes;
    }
}
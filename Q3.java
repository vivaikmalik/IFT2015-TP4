import java.util.*;

public class Q3 {

    //Q1 - DONE (used: https://www.reddit.com/r/learnprogramming/comments/1mob2k0/help_with_leetcode_hard_cat_and_mouse/)

    private static final int mouse_win = 1;
    private static final int cat_win = 2;
    private static final int draw = 0;
    private static final int mouse_turn = 0;
    private static final int cat_turn = 1;
    private static int N;
    private static int max_moves;

    private static int[][][] cache;
    private static int [][] gamegraph;
    

    public static int gameResult(int[][] graph) {
        gamegraph = graph;
        N = graph.length; 
        max_moves = 4*N + 200;
        cache = new int[max_moves][N][N];

        for (int[][] mat:cache){
            for (int[] row:mat){
                Arrays.fill(row, -1);
            }
        }

        return dp(1, 2, mouse_turn, 0);

    }

    private static int dp(int mousepos, int catpos, int player, int moves){
        if (moves >= max_moves) return draw;

        if (mousepos==catpos) return cat_win;
        if (mousepos==0) return mouse_win;

        if(cache[moves][mousepos][catpos]!=-1){
            return cache[moves][mousepos][catpos];
        }

        if(player==mouse_turn){
            boolean candraw=false;

            for(int nextmouspos: gamegraph[mousepos]){
                int result = dp(nextmouspos, catpos, cat_turn, moves +1);

                if (result == mouse_win){
                    return cache[moves][mouse_pos][cat_pos] = mouse_win;
                }

                if (result==draw){
                    candraw=true;
                }
            }

            return cache[moves][mousepos][catpos]= (candraw)? draw: cat_win;
        } else{
            boolean candraw = false;
            for (int nextcatpos: gamegraph[catpos]){
                if (nextcatpos ==0) continue;
                int result = dp(mousepos, nextcatpos, mouse_turn, moves+1);

                if (result==cat_win){
                    return cache[moves][mouse_pos][cat_pos] = cat_win;

                }
                if (result==draw){
                    candraw=true;
                }
            }
            return cache[moves][mousepos][catpos]= (candraw)? draw: mouse_win;

        }
    }

    //Q2 - DONE
    public static List<Integer> getMouseWinningMoves(int[][] graph) {
        int result = gameResult(graph);
        List<Integer> moves = new ArrayList<>();

        if (result!=mouse_win){
            return moves;
        }

        for (int nextmouspos: gamegraph[1]){
            int resultafter = dp(nextmousepos, 2, cat_turn, 1);
            if (resultafter==mouse_win){
                moves.add(nextmousepos);
            }
        }

        Collections.sort(moves);
        return moves;
    }

    public static int minMovesToWin(int[][] graph, int player) {
        // TODO
        return -1;
    }

    //Q4 - DONE
    public static int gameResultFrom(int[][] graph, int mousePos, int catPos, boolean mouseTurn) {
        int whosturn = mouseTurn? mouse_turn:cat_turn;
        return dp(mousepos, catpos, whosturn, 0);
    }

    public static List<String> getAllDrawPositions(int[][] graph) {
        // TODO
        return new ArrayList<>();
    }

    public static Map<String, Integer> analyzeAllPositions(int[][] graph) {
        // TODO
        return new HashMap<>();
    }

    public static int countWinningPositions(int[][] graph, int player) {
        // TODO
        return 0;
    }

    public static List<Integer> findSafeNodes(int[][] graph) {
        // TODO
        return new ArrayList<>();
    }

}

//Theortical questions: 
// a) The game's ending depends on the position of cat and mouse and who will play next. Now imagine you had a cycle in the graph, we could have that the mouse and the cat keep repeating the same cycle of optimal moves without ever stopping. That would happen if the cycle makes it that neither the cat or the mouse can win. We add a stopping criteria for this specific reason. 
// b) we hve to take into account the number of unqiue states the game can be in. For the number of sttaes we have N nodes, so the mouse can have N value. Same for the cat. Then the number of player is 2 (cat and mouse). Also the maximum number of move has complexity N. So if we look at what are all the different states that exist it would be, in the worst case: O(N x N x N x 2) which is equivalent to O(N^3). Now we also have to account for the time it takes to loop thruogh stages. We know our graph has N node, so in the worst case there are N vertices. So it would take complexity of O(N). In the worst case we would get O(N x N^3) = O(N^4)
//c) Well, the  mouse would always win the game if the node the mouse starts on is directly linked to the refuge node. Then the first move for the mouse would just be to go to the refuge => win. The cat is not allowed to go first. 
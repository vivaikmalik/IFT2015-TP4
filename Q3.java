
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
14 * The methods that used Gemini have comments / docstrings in them that detail why / how it was used.
15 *
16 * Affected code sections:
17 * ___________________________________________________
18 * ___________________________________________________
19 */
import java.util.*;

public class Q3 {

    // Q1 - DONE (used: gemini used to validate whether logic of method was correct
    // after finished coding
    // https://www.reddit.com/r/learnprogramming/comments/1mob2k0/help_with_leetcode_hard_cat_and_mouse/)

    private static final int mouse_win = 1;
    private static final int cat_win = 2;
    private static final int draw = 0;
    private static final int mouse_turn = 0;
    private static final int cat_turn = 1;
    private static int N;
    private static int max_moves;

    private static int[][][] cache;
    private static int[][] gamegraph;

    public static int gameResult(int[][] graph) {
        gamegraph = graph;
        N = graph.length;
        max_moves = 4 * N + 200;
        cache = new int[max_moves][N][N];

        for (int[][] mat : cache) {
            for (int[] row : mat) {
                Arrays.fill(row, -1);
            }
        }

        return dp(1, 2, mouse_turn, 0);

    }

    private static int dp(int mousepos, int catpos, int player, int moves) {
        if (moves >= max_moves)
            return draw;

        if (mousepos == catpos)
            return cat_win;
        if (mousepos == 0)
            return mouse_win;

        if (cache[moves][mousepos][catpos] != -1) {
            return cache[moves][mousepos][catpos];
        }

        if (player == mouse_turn) {
            boolean candraw = false;

            for (int nextmouspos : gamegraph[mousepos]) {
                int result = dp(nextmouspos, catpos, cat_turn, moves + 1);

                if (result == mouse_win) {
                    return cache[moves][mousepos][catpos] = mouse_win;
                }

                if (result == draw) {
                    candraw = true;
                }
            }

            return cache[moves][mousepos][catpos] = (candraw) ? draw : cat_win;
        } else {
            boolean candraw = false;
            for (int nextcatpos : gamegraph[catpos]) {
                if (nextcatpos == 0)
                    continue;
                int result = dp(mousepos, nextcatpos, mouse_turn, moves + 1);

                if (result == cat_win) {
                    return cache[moves][mousepos][catpos] = cat_win;

                }
                if (result == draw) {
                    candraw = true;
                }
            }
            return cache[moves][mousepos][catpos] = (candraw) ? draw : mouse_win;

        }
    }

    // Q2 - DONE
    public static List<Integer> getMouseWinningMoves(int[][] graph) {
        int result = gameResult(graph);
        List<Integer> moves = new ArrayList<>();

        if (result != mouse_win) {
            return moves;
        }

        for (int nextmouspos : gamegraph[1]) {
            int resultafter = dp(nextmouspos, 2, cat_turn, 1);
            if (resultafter == mouse_win) {
                moves.add(nextmouspos);
            }
        }

        Collections.sort(moves);
        return moves;
    }

    public static int minMovesToWin(int[][] graph, int player) {
        gameResult(graph);

        int win = (player == mouse_turn) ? mouse_win : cat_win;
        int starmouse_pos = 1;
        int startcat_pos = 2;

        // win is before the last move (same as chess with checkmate). So if mouse wins
        // by going to the winning box, then made 1 move. So that's odd.

        for (int m = 0; m < 4 * graph.length + 200; m++) {
            int result = cache[m][starmouse_pos][startcat_pos]; // modify logic from earlier to count the number of
                                                                // moves
            // first scenario its the mouse's turn
            if (result == win) {
                if (player == mouse_turn) { // mouse has even turns, so can win on an odd move. Also if moved last then
                                            // it won, so check if number of moves is divisible by 2.
                    if (m % 2 != 0) {
                        return m;
                    }
                } else {
                    if (m % 2 == 0) { // cat plays on odd number, if last to move it is an even move => wins on even
                                      // move, return total number of displacements
                        return m;
                    }
                }
            }
            return m;
        }
        return -1; // when impossible to win
    }

    // Q4 - DONE
    public static int gameResultFrom(int[][] graph, int mousePos, int catPos, boolean mouseTurn) {
        gamegraph = graph;
        cache = new int[4 * graph.length + 200][graph.length][graph.length];

        for (int[][] m : cache) {
            for (int[] row : m) {
                Arrays.fill(row, -1);
            }
        }

        int whosturn = mouseTurn ? mouse_turn : cat_turn;
        return dp(mousePos, catPos, whosturn, 0);
    }

    public static List<String> getAllDrawPositions(int[][] graph) {
        gameResult(graph);

        List<String> drawpositions = new ArrayList<>();
        for (int i = 0; i < N; i++) { // mouse position starts at 0
            for (int j = 1; j < N; j++) { // cat position starts at 1
                int mouseResult = dp(i, j, mouse_turn, 0); // mouse plays first
                if (mouseResult == draw) { // condition on draw and add draw positions
                    drawpositions.add(i + "-" + j + "-0");
                }
                int catResult = dp(i, j, cat_turn, 0); // cat plays second, but we want to REANALYZE game as of now that
                                                       // mouse has played
                if (catResult == draw) { // condition on draw and add draw positions
                    drawpositions.add(i + "-" + j + "-1");
                }
            }
        }
        Collections.sort(drawpositions); // why this ask alice ??
        return drawpositions;
    }

    public static Map<String, Integer> analyzeAllPositions(int[][] graph) {
        gameResult(graph);

        Map<String, Integer> positions = new TreeMap<>(); // use of tree map recommended by Gemini - ensures
                                                          // lexigraphical order apparently. I had used hash map
                                                          // initially
        for (int i = 0; i < N; i++) { // mouse position starts at 0
            for (int j = 1; j < N; j++) { // cat position starts at 1
                int mouseResult = dp(i, j, mouse_turn, 0); // mouse plays first
                positions.put(i + "-" + j + "-0", mouseResult);
                int catResult = dp(i, j, cat_turn, 0); // cat plays second, but we want to REANALYZE game as of now that
                // mouse has played
                positions.put(i + "-" + j + "-1", catResult);
            }
        }
        // very similar logic as last question just not conditioning on result == draw
        // (copy past from last question basically ;)
        return positions;
    }

    public static int countWinningPositions(int[][] graph, int player) {
        gameResult(graph); // initialze game

        if (player == mouse_win) { // condition if mouse wins
            int count = 0;
            for (int i = 0; i < N; i++) { // loop over mouse moves
                for (int j = 1; j < N; j++) { // loop over cat moves
                    if (dp(i, j, mouse_turn, 0) == mouse_win) {
                        count++;
                    } // condition if mouse wins when it plays next
                    if (dp(i, j, cat_turn, 0) == mouse_win) {
                        count++;
                    } // condition if mouse wins when cat plays next
                }
            }
            return count;
        } else { // condition if cat wins
            int count = 0;
            for (int i = 0; i < N; i++) { // loop over mouse moves
                for (int j = 1; j < N; j++) { // loop over cat moves
                    if (dp(i, j, cat_turn, 0) == cat_win) {
                        count++;
                    } // condition if cat wins when it plays next
                    if (dp(i, j, mouse_turn, 0) == cat_win) {
                        count++;
                    } // condition if cat wins when mouse plays next
                }
            }
            return count;
        }
    }

    public static List<Integer> findSafeNodes(int[][] graph) {
        gameResult(graph);

        boolean isSafe = true; // gemini helped decide to use this boolean. assume mouse is safe at first

        List<Integer> safenodes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 1; j < N; j++) {
                if (dp(i, j, mouse_turn, 0) != mouse_win) { // if mouse can't win, it's not safe, mouse will not win,
                                                            // so value should not be added to list
                    isSafe = false;
                    break;
                }
            }
            if (isSafe)
                safenodes.add(i);
            isSafe = true;
        }
        return safenodes; // right now i am returning all nodes, including the node = 0. we do not know
                          // how to fix this.
        // i am assuming i can delete the node = 0 from the list of safenodes, but we
        // are not exactly sure how
    }

}

// Theortical questions:
// a) The game's ending depends on the position of cat and mouse and who will
// play next. Now imagine you had a cycle in the graph, we could have that the
// mouse and the cat keep repeating the same cycle of optimal moves without ever
// stopping. That would happen if the cycle makes it that neither the cat or the
// mouse can win. We add a stopping criteria for this specific reason.
// b) we hve to take into account the number of unqiue states the game can be
// in. For the number of sttaes we have N nodes, so the mouse can have N value.
// Same for the cat. Then the number of player is 2 (cat and mouse). Also the
// maximum number of move has complexity N. So if we look at what are all the
// different states that exist it would be, in the worst case: O(N x N x N x 2)
// which is equivalent to O(N^3). Now we also have to account for the time it
// takes to loop thruogh stages. We know our graph has N node, so in the worst
// case there are N vertices. So it would take complexity of O(N). In the worst
// case we would get O(N x N^3) = O(N^4)
// c) Well, the mouse would always win the game if the node the mouse starts on
// is directly linked to the refuge node. Then the first move for the mouse
// would just be to go to the refuge => win. The cat is not allowed to go first.
import java.io.*;
import java.util.*;

public class Main {

    static int[][] board;
    static int N, K;
    static int result;
    static int[] row, column, diag, anti_diag;
    static int[] t_row, t_column, t_diag, t_anti_diag;
    public static void main(String[] args) throws IOException {

        //for test
//        int version = 2;
//        Scanner scanner = new Scanner(System.in);
//        String[] input = scanner.nextLine().split(" ");
//        N = Integer.parseInt(input[0]);
//        K = Integer.parseInt(input[1]);

        //for Submission
        int version = Integer.parseInt(args[0]);
        String inputFile = args[1];
        String outputFile = args[2];

        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String[] line = br.readLine().split(" ");
        N = Integer.parseInt(line[0]);
        K = Integer.parseInt(line[1]);

        // Initialize the static data
        board = new int[N + 1][N + 1];
        row = new int[N + 1];
        column = new int[N + 1];
        diag = new int[2 * N + 1];
        anti_diag = new int[2 * N + 1];

        t_row = new int[N + 1];
        t_column = new int[N + 1];
        t_diag = new int[2 * N + 1];
        t_anti_diag = new int[2 * N + 1];

        // Read the coordinates and mark holes as -1
        for (int i = 0; i < K; i++) {
           // String[] coordinates = scanner.nextLine().split(" ");
            String[] coordinates = br.readLine().split(" ");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            board[x][y] = -1;
            t_row[x]++;
            t_column[y]++;
            t_diag[x - y + N]++;
            t_anti_diag[x + y]++;
        }
        br.close();

        result = 0; // Number of solutions found

        // Version 1 : Iterative backtracking
        if (version == 1) {
            Stack<int[]> stack = new Stack<>();
            int totalQueens = N; // Desired number of queens to place

            stack.push(new int[]{0, 0, 0});

            int[] next_possible_cell = new int[]{1, 1};

            while (!stack.isEmpty()) {
                int[] position = stack.peek();
                int row = position[0];
                int col = position[1];
                int queensPlaced = position[2];

                if (queensPlaced == totalQueens) {
                    // Do backtracking
                    result ++;
                    // update next_possible_cell
                    if (row == N && col == N) { // reached the end of the matrix
                        unmark_Queen(row, col);
                        stack.pop();
                        position = stack.pop();
                        row = position[0];
                        col = position[1];
                        unmark_Queen(row, col); // unmark twice because this state is also over
                    } else {
                        unmark_Queen(row, col);
                        stack.pop();
                    }

                    if (col== N) {
                        next_possible_cell[0] = row+1 ;
                        next_possible_cell[1] = 1;
                    } else {
                        next_possible_cell[0] = row;
                        next_possible_cell[1] = col+1;
                    }


                } else {
                    // Search for cells to place next queen -> place queen and stack
                    int r = next_possible_cell[0];
                    int c = next_possible_cell[1];    //iterate start from the last placed queen

                    while (r <= N && c <= N) {
                        if (is_cell_safe(r, c)) {
                            // place queen here
                            mark_Queen(r, c);
                            stack.push(new int[]{r, c, queensPlaced+1});

                            // update next_possible_cell
                            if (c== N) {
                                next_possible_cell[0] = r+1 ;
                                next_possible_cell[1] = 1;
                            } else {
                                next_possible_cell[0] = r;
                                next_possible_cell[1] = c+1;
                            }
                            break;
                        }
                        //look at next cell
                        if (c== N) {
                            r ++;
                            c = 1;
                        } else {
                            c++;
                        }
                    }

                    if (r==N+1) {  //couldn't place anymore queens until the matrix end
                        // Do back tracking
                        int[] pop = stack.pop();
                        r = pop[0];
                        c = pop[1];
                        unmark_Queen(r, c);
                        // update next cell
                        if (c== N) {
                            next_possible_cell[0] = r+1 ;
                            next_possible_cell[1] = 1;
                        } else {
                            next_possible_cell[0] = r;
                            next_possible_cell[1] = c+1;
                        }
                    }
                    //end of BIG while loop
                }
            }
        } else {
            // Version2 : Recursive backtracking
            solveQueens(1, 1, 0);
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
        bw.write(Integer.toString(result));
        bw.close();

    }
    static void solveQueens(int row, int col, int queensPlaced) {
        if (queensPlaced == N) {
            // Base case: desired number of lizards are placed
            result++;
            return;
        }

        if (row > N || (row == N && col > N)) {
            // Base case: reached the end of the matrix
            return;
        }

        // Check if the current cell is safe
        if (is_cell_safe(row, col)) {
            // Place a queen in the current cell
            mark_Queen(row, col);

            // Recursive call to place the next queen in the next cell
            int nextRow = row;
            int nextCol = col + 1;

            if (nextCol > N) {
                // Move to the next row if we have reached the end of the current row
                nextRow++;
                nextCol = 1;
            }

            solveQueens(nextRow, nextCol, queensPlaced + 1);

            // Backtrack: remove the lizard and continue searching for other solutions
            unmark_Queen(row, col);
        }

        // Recursive call to move to the next cell
        int nextRow = row;
        int nextCol = col + 1;

        if (nextCol > N) {
            // Move to the next row if we have reached the end of the current row
            nextRow++;
            nextCol = 1;
        }

        solveQueens(nextRow, nextCol, queensPlaced);
    }
    static boolean is_cell_safe (int row, int col) {
        if (board[row][col] == -1 || board[row][col] == 1) {    //this place is a tree
            return false;
        }
        ArrayList<Character> r_c_d_a = get_Queens(row, col);
        if (r_c_d_a.isEmpty()) {    //no queens threatening, so safe
            return true;
        } else {
            for (Character x : r_c_d_a) {   //there is a queen threat, so check for holes
                if (x == 'r') {
                    if (t_row[row] == 0) {
                        return false;
                    }
                    for (int c = col; c >= 1; c--) {
                        if (board[row][c] == 1) {   //encounter a queen first, not safe
                            return false;
                        }
                        if (board[row][c] == -1) {  //encounter a tree first, the row is safe
                            break;
                        }
                    }
                } else if (x == 'c') {
                    if (t_column[col] == 0) {
                        return false;
                    }
                    for (int r = row; r >= 1; r--) {
                        if (board[r][col] == 1) {   //encounter a queen first, not safe
                            return false;
                        }
                        if (board[r][col] == -1) {  //encounter a tree first, the col is safe
                            break;
                        }
                    }

                } else if (x == 'd') {
                    if (t_diag[row-col+N] == 0) {
                        return false;
                    }
                    int r = row;
                    int c = col;
                    while (r > 1 && c > 1) {
                        if (board[--r][--c] == 1) {
                            return false;
                        }
                        if (board[r][c] == -1) {
                            break;
                        }
                    }

                } else if (x == 'a') {
                    if (t_anti_diag[row+col] == 0) {
                        return false;
                    }
                    int r = row;
                    int c = col;
                    while (r > 1 && c < N) {
                        if (board[--r][++c] == 1) {
                            return false;
                        }
                        if (board[r][c] == -1) {
                            break;
                        }
                    }
                }
            }
        }
        return true;    //pass all the check, is safe
    }
    static void mark_Queen (int r, int c) {
        board[r][c] = 1;
        row[r] ++;
        column[c] ++;
        diag[r -c + N] ++;
        anti_diag[r+c] ++;
    }
    static void unmark_Queen (int r, int c) {
        board[r][c] = 0;
        row[r] --;
        column[c] --;
        diag[r -c + N] --;
        anti_diag[r+c] --;
    }
    private static ArrayList<Character> get_Queens (int r, int c){
        ArrayList<Character> result = new ArrayList<>();
        if (row[r] > 0) {
            result.add('r');
        }
        if  (column[c] > 0) {
            result.add('c');
        }
        if (diag[r - c + N] > 0) {
            result.add('d');
        }
        if (anti_diag[r + c] > 0) {
            result.add('a');
        }
        return result;

    }

    static void print_board (int[][] board){
        int n = board.length;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}

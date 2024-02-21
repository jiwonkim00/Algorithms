import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
//        BufferedWriter bfw = new BufferedWriter(new FileWriter("./2.in"));
//        bfw.write("4 3");
//        bfw.newLine();
//        bfw.write("1 3");
//        bfw.newLine();
//        bfw.write("3 1");
//        bfw.newLine();
//        bfw.write("4 4");
//        bfw.close();
//        BufferedWriter bfw2 = new BufferedWriter(new FileWriter("./2.out"));
//        bfw2.write("0");
//        bfw2.close();
        int r = 8;
        int c = 8;
        if (--r >0 ) {
            System.out.println(r);
        }

    }
}
class LizardPlacement {

    private static int n = 8; // Size of the board
    private static int m = 8; // Number of lizards

    private static int[][] trees = {{5, 5}, {6, 3}}; // Tree positions

    private static List<int[]> lizardPositions = new ArrayList<>();

    static void solveLizardPlacement() {
        for (int lizard = 1; lizard <= m; lizard++) {
            for (int x = 1; x <= n; x++) {
                for (int y = 1; y <= n; y++) {
                    int[] position = {lizard, x, y};
                    lizardPositions.add(position);
                }
            }
        }

        Iterator<int[]> iterator = lizardPositions.iterator();
        while (iterator.hasNext()) {
            int[] position = iterator.next();
            int lizard = position[0];
            int x = position[1];
            int y = position[2];

            boolean validPosition = true;
            for (int[] tree : trees) {
                int treeX = tree[0];
                int treeY = tree[1];
                if (x == treeX && y == treeY) {
                    validPosition = false;
                    break;
                }
            }

            if (!validPosition) {
                iterator.remove();
            }
        }

        iterator = lizardPositions.iterator();
        while (iterator.hasNext()) {
            int[] position1 = iterator.next();
            int lizard1 = position1[0];
            int x1 = position1[1];
            int y1 = position1[2];

            Iterator<int[]> innerIterator = lizardPositions.iterator();
            while (innerIterator.hasNext()) {
                int[] position2 = innerIterator.next();
                int lizard2 = position2[0];
                int x2 = position2[1];
                int y2 = position2[2];

                if (lizard1 != lizard2 && (x1 == x2 || y1 == y2 || Math.abs(x1 - x2) == Math.abs(y1 - y2))) {
                    innerIterator.remove();
                }
            }
        }

        for (int[] position : lizardPositions) {
            int lizard = position[0];
            int x = position[1];
            int y = position[2];
            System.out.println("ans(" + lizard + "," + x + "," + y + ")");
        }
    }
}

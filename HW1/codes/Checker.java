import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Checker {

    public static void main(String[] args) {
        String mainInput = args[0];
        String mainOutput = args[1];

        //Read Main Input
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(mainInput));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String[] read_1 = scanner.nextLine().split(" ");
        int[] info = {Integer.parseInt(read_1[0]), Integer.parseInt(read_1[1])};

        int[] num_array = new int[info[0]];
        int i = info[1];

        String[] read_2 = scanner.nextLine().split(" ");
        for (int k = 0; k < read_2.length; k++) {
            num_array[k] = Integer.parseInt(read_2[k]);
        }

        //Read Main Output
        Scanner scanner1 = null;
        try {
            scanner1 = new Scanner(new File(mainOutput));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String res = scanner1.nextLine();
        int main_result = Integer.parseInt(res);

        System.out.println(checker(num_array, i, main_result));

    }

    public static int checker(int[] arr, int i, int result) {
        //i-th element
        Arrays.sort(arr);
        return arr[i-1] == result ? 1: 0;
    }
}

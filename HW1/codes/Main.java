import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static int r_partition(int[] arr, int left, int right) {
        int pivotIndex = new Random().nextInt(right - left + 1) + left;
        int pivot = arr[pivotIndex];
        swap(arr, pivotIndex, right);
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (arr[i] < pivot) {
                swap(arr, i, storeIndex);
                storeIndex++;
            }
        }

        swap(arr, storeIndex, right);
        return storeIndex; // the index where the pivot is stored
    }

    public static int randomizedSelect(int[] arr, int left, int right, int i) {
        if (left == right) {
            return arr[left];
        }

        int pivotIndex = r_partition(arr, left, right);
        int k = pivotIndex - left + 1;

        if (i == k) {
            return arr[pivotIndex];
        } else if (i < k) {
            return randomizedSelect(arr, left, pivotIndex - 1, i);
        } else {
            return randomizedSelect(arr, pivotIndex + 1, right, i - k);
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    ////////////////////////////////////////////////////////

    public static int findMedianofMedians(int[] arr) {
        int n = arr.length;

        if (n == 1) {
            return arr[0];
        }
        if (n<= 5) {
            return median(arr, 0, arr.length-1);
        }
        int[] medians = new int[(n + 4) / 5];
        for (int i = 0; i < n / 5; i++) {

            medians[i] = median(arr, i * 5, i * 5 + 4);
        }
        if (n % 5 != 0) {
            medians[medians.length - 1] = median(arr, n - n % 5, n - 1);
        }

        return findMedianofMedians(medians);
    }

    private static int median(int[] arr, int start, int end) {
        if (arr.length == 1) {
            return arr[0];
        }
        //insertion sort
        for (int k = start +1; k <= end; k++) {
            int j = k-1;
            int insertionItem = arr[k];
            while (j >= start && insertionItem < arr[j]) {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = insertionItem;
        }

        return arr[(start + end)/2];
    }

    public static int d_partition(int[] arr, int left, int right, int median, int pivotIndex) {
        swap(arr, pivotIndex, right);

        int i = left-1;
        for (int j = left; j<right; j++) {

            if(arr[j] < median) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i+1, right);

        return i+1;
    }
    public static int deterministicSelect(int[] arr, int left, int right, int i) {
        if (arr.length <= 5) {
            //code for insertion sort
            for (int k = left + 1; k <= right; k++) {
                int j = k - 1;
                int insertionItem = arr[k];
                while (j >= left && insertionItem < arr[j]) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = insertionItem;
            }

            return arr[i - 1];  //return k-th element
        }

        int median = findMedianofMedians(Arrays.copyOfRange(arr, left, right+1));

        int pivotIndex = 0;
        for (int k = left; k <= right; k++) {
            if (arr[k] == median) {
                pivotIndex = k;
                break;
            }
        }

        int pivotIndex_afterSort = d_partition(arr, left, right, median, pivotIndex);

        int q = pivotIndex_afterSort - left + 1;

        if (i == q) {
            return arr[pivotIndex_afterSort];
        } else if (i < q) {
            return deterministicSelect(arr, left, pivotIndex_afterSort - 1, i);
        } else {
            return deterministicSelect(arr, pivotIndex_afterSort + 1, right, i - q );
        }

    }




    public static void main(String[] args)  {

        int version = Integer.parseInt(args[0]); // 1: random, 2: deterministic
        String input_loc = args[1];   // ex.) ./input/test1.in
        String output_loc = args[2];  // ex.) ./output/test1.out

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(input_loc));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String[] read_1 = scanner.nextLine().split(" ");

        int[] info = {Integer.parseInt(read_1[0]), Integer.parseInt(read_1[1])};
        int[] num_array = new int[info[0]];
        int i = info[1];

        String[] read_2 = scanner.nextLine().split(" ");
        for (int k = 0; k< read_2.length; k++) {
            num_array[k] = Integer.parseInt(read_2[k]);
        }

//Write Result

        int result = version == 1 ? randomizedSelect(num_array, 0, num_array.length-1, i) : deterministicSelect(num_array, 0, num_array.length-1, i);
        try {
            FileWriter fileWriter2 = new FileWriter(output_loc);
            fileWriter2.write(Integer.toString(result));
            fileWriter2.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //to make file////////////////////////////////
/*
//Write
        String firstLine = "18 11\n";
        String secondLine = "1 1 1 5 5 5 5 3 3 3 6 6 6 8 8 8 8 9";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("./2.in");
            fileWriter.write(firstLine);
            fileWriter.write(secondLine);
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//Read
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("./2.in"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String[] read_1 = scanner.nextLine().split(" ");
        int[] info = {Integer.parseInt(read_1[0]), Integer.parseInt(read_1[1])};

        int[] num_array = new int[info[0]];
        int i = info[1];

        String[] read_2 = scanner.nextLine().split(" ");
        for (int k = 0; k< read_2.length; k++) {
            num_array[k] = Integer.parseInt(read_2[k]);
        }

//Write Result

        int result = randomizedSelect(num_array, 0, num_array.length-1, i);
        try {
            FileWriter fileWriter2 = new FileWriter("./2.out");
            fileWriter2.write(Integer.toString(result));
            fileWriter2.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

 */

    //for TEST////////////////////////////////////
    /*
        int length = 1000000;

        Random rd = new Random(); // creating Random object
        int[] test_arr = new int[length];
        for (int i = 0; i < test_arr.length; i++) {
            test_arr[i] = rd.nextInt(); // storing random integers in an array
            //System.out.print(test_arr[i] + " ");
        }
        System.out.println();

        int i = rd.nextInt(length) + 1;
        System.out.println("i : " + i);

        long r_startTime = System.nanoTime();
        int r_result = randomizedSelect(test_arr, 0, test_arr.length - 1, i);

        long r_endTime   = System.nanoTime();
        long r_totalTime = r_endTime - r_startTime;
        System.out.println("Randomized Selection Time : " + r_totalTime);

        long d_startTime = System.nanoTime();
        int d_result = deterministicSelect(test_arr, 0, test_arr.length-1, i);

        long d_endTime   = System.nanoTime();
        long d_totalTime = d_endTime - d_startTime;
        System.out.println("Deterministic Selection Time : " + d_totalTime);


        System.out.println("Randomized Selection : " + r_result);
        System.out.println("Deterministic Selection : " + d_result);

        Arrays.sort(test_arr);
        System.out.println("Checker : " + test_arr[i-1]);
     */

    }


}
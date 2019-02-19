import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        int[] arr1 = division(arr, n);
        int[] arr2 = division(arr1, n);

        for (int i = 0; i < n; i++) {
            System.out.print(arr2[i] + " ");
        }
    }

    public static int[] division(int[] a, int m){
        //find maximum
        int max = 0;
        for (int i = 0; i < m; i++) {
            if (a[i] > max){
                max = a[i];
            }
        }

        //divide maximums by 2
        for (int i = 0; i < m; i++) {
            if (a[i] == max){
                a[i] /= 2;
            }
        }

        return a;
    }
}
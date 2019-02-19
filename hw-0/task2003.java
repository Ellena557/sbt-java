import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int sum = 0;

        for (int i = 0; i < n; i++) {
            int b = scanner.nextInt();
            sum += b * Math.pow(-1, i);
        }

        System.out.println(sum);
    }
}
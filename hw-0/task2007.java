import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int cnt = 0;

        while (n % 2 != 1){
            cnt += 1;
            n = n/2;
        }


        System.out.println(cnt);
    }
}
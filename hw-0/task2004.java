import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int year = scanner.nextInt();
        int res = 0;

        if (year % 4 == 0 && year % 100 != 0){
            res = 1;
        }

        if (year % 400 == 0 ){
            res = 1;
        }

        System.out.println(res);
    }
}
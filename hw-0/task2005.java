import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int ind = 0;
        int loc_min = 10001;

        for (int i = 1; i < n+1; i++) {
            int elem = scanner.nextInt();
            if (elem < loc_min){
                ind = i;
                loc_min = elem;
            }
        }

        System.out.println(ind);
    }
}
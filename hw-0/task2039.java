import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String string = new String();
        int num = 0;
        int predletter = 0;

        string = scanner.nextLine();
        int n = string.length();

        for (int i = 0; i < n; i++) {
            int letter = 0;
            char c = string.charAt(i);
            if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))){
                letter = 1;
            }

            if (predletter == 0 && letter == 1){
                num += 1;

            }
            predletter = letter;
        }

        System.out.println(num);
    }
}
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        String[] strings = new String[n];
        String badletters = "eyuioa";
        int[] goods = new int[n];

        for (int i = 0; i < n; i++) {
            strings[i] = scanner.next();
            int num = 0;
            goods[i] = 1;
            int l = strings[i].length();
            for (int j = 0; j < l; j++) {
                char c = strings[i].charAt(j);
                String s = "" + c;
                if (badletters.contains(s)){
                    num += 1;
                }
                else{
                    num = 0;
                }
                if (num >= 3){
                    goods[i] = 0;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (goods[i] == 1){
                System.out.println(strings[i]);
            }
        }
    }
}
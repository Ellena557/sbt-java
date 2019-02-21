import java.util.*;

public class Solution2056 {
    public static void main(String[] args) {
        int max = 0;
        ArrayList<String> goodwords = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> map = new HashMap<>();

        while(scanner.hasNext()){
            String word = scanner.next();
            word = word.toLowerCase();
            map.compute(word, (k, v) -> (v == null) ? 1 : v+1);
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()){
            int value = entry.getValue();
            if (value > max){
                max = value;
            }
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()){
            int value = entry.getValue();
            String key = entry.getKey();
            if (value == max){
                goodwords.add(key);
            }
        }

        Collections.sort(goodwords);
        for (int i = 0; i < goodwords.size(); i++) {
            System.out.println(goodwords.get(i));
        }
    }

}

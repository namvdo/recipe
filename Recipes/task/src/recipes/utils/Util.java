package recipes.utils;

import java.util.Random;

public class Util {
    public static String randomEmail(int length) {
        String letter = "zxcvbnmasdfghjklpoiuytrewq";
        var str = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++)  {
            str.append(letter.charAt(random.nextInt() % letter.length()));
        }
        return str.toString() + "@gmail.com";
    }
}

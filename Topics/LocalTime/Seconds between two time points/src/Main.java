import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        int input = scanner.nextInt();
        LocalTime time = LocalTime.ofSecondOfDay(input);
        System.out.println(time);
    }
}
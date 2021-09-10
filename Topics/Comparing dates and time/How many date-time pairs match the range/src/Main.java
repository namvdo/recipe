import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args) throws IOException {
        // put your code here
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LocalDateTime leftRage = LocalDateTime.parse(reader.readLine());
        LocalDateTime rightRage = LocalDateTime.parse(reader.readLine());
        if (leftRage.isAfter(rightRage)) {
            LocalDateTime temp = LocalDateTime.of(leftRage.toLocalDate(), leftRage.toLocalTime());
            leftRage = LocalDateTime.of(rightRage.toLocalDate(), rightRage.toLocalTime());
            rightRage = LocalDateTime.of(temp.toLocalDate(), temp.toLocalTime());
        }
        int count = 0;
        List<LocalDateTime> list = new ArrayList<>();
        int input = Integer.parseInt(reader.readLine());
        while (input != 0) {
            list.add(LocalDateTime.parse(reader.readLine()));
            input--;
        }
        for(var date: list)  {
            if (leftRage.equals(rightRage) && rightRage.equals(date)) {
                continue;
            }
            if (date.isEqual(leftRage) || date.isAfter(leftRage) && date.isBefore(rightRage)) {
                count++;
            }
        }
        System.out.println(count);
    }
}
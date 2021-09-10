import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

class Main {
    public static void main(String[] args) throws IOException {
        // put your code here
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String val = reader.readLine();
        LocalDateTime dateTime = LocalDateTime.parse(val);
        LocalDateTime newTime = dateTime.plusHours(11);
        System.out.println(newTime.toLocalDate());
    }
}

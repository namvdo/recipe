import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

class Main {
    public static void main(String[] args) throws IOException {
        // put your code here
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String date = reader.readLine();
        LocalDate newDate = LocalDate.parse(date);
        LocalDate moreWeeks = newDate.plusWeeks(2);
        System.out.println(moreWeeks);
    }
}
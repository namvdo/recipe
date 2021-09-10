import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

class Main {
    public static void main(String[] args) throws IOException {
        // put your code here
        var reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        LocalDate date = LocalDate.parse(input.split(" ")[0]);
        int curYear = date.getYear();
        int days = Integer.parseInt(input.split(" ")[1]);
        LocalDate newDate = date.plusDays(days);
        if (newDate.getYear() != curYear) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}

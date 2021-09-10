import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

class Main {
    public static void main(String[] args) throws IOException {
        // put your code here
        var reader = new BufferedReader(new InputStreamReader(System.in));
        String lines = reader.readLine();
        LocalDate x = LocalDate.parse(lines.split(" ")[0]);
        LocalDate m = LocalDate.parse(lines.split(" ")[1]);
        LocalDate n = LocalDate.parse(lines.split(" ")[2]);
        if (x.isAfter(m) && x.isBefore(n)) {
            System.out.println("true");
        } else if (x.isBefore(m) && x.isAfter(n)){
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
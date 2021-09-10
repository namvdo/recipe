import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

class Main {
    public static void main(String[] args) throws IOException {
        // put your code here
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int test = Integer.parseInt(reader.readLine());
        Map<String, String> map = new LinkedHashMap<>();
        do {
            String[] inputs = reader.readLine().split(" ");
            map.put(inputs[0], inputs[1]) ;
            test--;
        } while (test != 0);
        var initTime = LocalTime.parse("19:30").plusMinutes(30);
        for(var entry : map.entrySet()) {
            if (initTime.isBefore(LocalTime.parse(entry.getValue()))) {
                System.out.println(entry.getKey());
            }
        }
    }
}

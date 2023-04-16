import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class task2 {

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("data.xml"))) {
            List<String> selectedFields = getSelectedFields();
            String line;
            Map<String, String> data = new LinkedHashMap<>();
            while ((line = br.readLine()) != null) {
                for (String field : selectedFields) {
                    if (line.contains("<" + field + ">")) {
                        String value = line.replaceAll("<" + field + ">|</" + field + ">", "").trim();
                        data.put(field, value);
                    }
                }
            }
            String json = convertToJSON(data);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getSelectedFields() {
        Scanner scanner = new Scanner(System.in);
        List<String> fields = new ArrayList<>();
        while (true) {
            System.out.println("Enter a field name (or 'done' to finish):");
            String field = scanner.nextLine();
            if (field.equals("done")) {
                break;
            } else {
                fields.add(field);
            }
        }
        scanner.close();
        return fields;
    }

    private static String convertToJSON(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (String key : data.keySet()) {
            sb.append("\"" + key + "\" : \"" + data.get(key) + "\", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append(" }");
        return sb.toString();
    }
}

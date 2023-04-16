import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class task2 {
    private static final String FILENAME = "data.xml";
    private static final String[] VALID_FIELDS = { "name", "postalZip", "region", "country", "address", "list" };

    public static void main(String[] args) {
        // Read user-selected fields
        List<String> selectedFields = readSelectedFields();

        // Parse XML file and extract data
        List<Map<String, String>> dataList = parseXML(selectedFields);

        // Convert data to JSON
        String json = convertToJSON(dataList);

        // Print JSON output
        System.out.println(json);
    }

    private static List<String> readSelectedFields() {
        Scanner scanner = new Scanner(System.in);
        List<String> selectedFields = new ArrayList<>();

        System.out.println("Enter fields to select (one at a time):");
        System.out.println("(Available fields: name, postalZip, region, country, address, list)");
        System.out.println("Enter 'done' when finished.");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            if (isValidField(input)) {
                selectedFields.add(input);
            } else {
                System.out.println("Invalid field. Please enter a valid field.");
            }
        }

        return selectedFields;
    }

    private static boolean isValidField(String field) {
        for (String validField : VALID_FIELDS) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    private static List<Map<String, String>> parseXML(List<String> selectedFields) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                private Map<String, String> dataMap = new HashMap<>();
                private StringBuilder currentData = new StringBuilder();
                private boolean isDataValid = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    currentData.setLength(0); // clear current data buffer

                    if (qName.equalsIgnoreCase("record")) {
                        dataMap = new HashMap<>();
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (qName.equalsIgnoreCase("record")) {
                        if (isDataValid) {
                            dataList.add(dataMap);
                        }
                    } else if (isValidField(qName) && selectedFields.contains(qName)) {
                        dataMap.put(qName, currentData.toString());
                        isDataValid = true;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    currentData.append(ch, start, length);
                }
            };

            saxParser.parse(new File(FILENAME), handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private static String convertToJSON(List<Map<String, String>> dataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (Map<String, String> data : dataList) {
            sb.append("{ ");
            for (String key : data.keySet()) {
                sb.append("\"" + key + "\" : \"" + data.get(key) + "\", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(" }, ");
        }
        if (!dataList.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append(" ]");
        return sb.toString();
    }
}

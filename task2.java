import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

public class task2 {

    private static List<String> availableFields = Arrays.asList("name", "postalZip", "region", "country", "address", "list");
    private static List<String> selectedFields = new ArrayList<>();

    public static void main(String[] args) {

        // Get user-selected fields
        selectedFields = getSelectedFields();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            final List<Map<String, String>> dataList = new ArrayList<>();

            DefaultHandler handler = new DefaultHandler() {

                private Map<String, String> currentData = null;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (selectedFields.contains(qName)) {
                        currentData = new LinkedHashMap<>();
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (currentData != null) {
                        dataList.add(currentData);
                        currentData = null;
                    }
                }

                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (currentData != null) {
                        String value = new String(ch, start, length).trim();
                        currentData.put(selectedFields.get(selectedFields.indexOf(value) - 1), value);
                    }
                }

            };

            saxParser.parse("data.xml", handler);

            String json = convertToJSON(dataList);
            System.out.println(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static List<String> getSelectedFields() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a field name (or 'done' to finish):");
            String field = scanner.nextLine();
            if (field.equals("done")) {
                break;
            } else if (!isValidField(field)) {
                System.out.println("Invalid field name. Please enter a valid field name.");
            } else if (selectedFields.contains(field)) {
                System.out.println("Field already selected. Please enter a different field name.");
            } else if (!availableFields.contains(field)) {
                System.out.println("Field not available. Please enter a different field name.");
            } else {
                selectedFields.add(field);
            }
        }
        scanner.close();
        return selectedFields;
    }

    private static boolean isValidField(String field) {
        return field.matches("[a-zA-Z]+[a-zA-Z0-9]*");
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
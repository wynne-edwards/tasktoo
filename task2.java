import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class task2 {
    private static List<String> selectedFields;
    private static List<Map<String, String>> dataList;
    private static String currentField;
    private static StringBuilder currentValue;
    private static Map<String, String> currentData;
    
    public static void main(String[] args) {
        selectedFields = new ArrayList<>();
        dataList = new ArrayList<>();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter field names (separated by commas): ");
        String input = scanner.nextLine();
        selectedFields.addAll(Arrays.asList(input.split(",")));
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("record")) {
                        currentData = new HashMap<>();
                    } else if (selectedFields.contains(qName)) {
                        currentValue = new StringBuilder();
                        currentField = qName;
                    }
                }
                
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (currentField != null) {
                        currentValue.append(new String(ch, start, length));
                    }
                }
                
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("record")) {
                        dataList.add(currentData);
                        currentData = null;
                    } else if (qName.equalsIgnoreCase(currentField)) {
                        currentData.put(currentField, currentValue.toString());
                        currentValue = null;
                        currentField = null;
                    }
                }
            };
            saxParser.parse(new File("data.xml"), handler);
            
            String json = convertToJSON(dataList);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String convertToJSON(List<Map<String, String>> dataList) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        for (Map<String, String> data : dataList) {
            builder.append("{\n");
            for (Map.Entry<String, String> entry : data.entrySet()) {
                builder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\",\n");
            }
            builder.setLength(builder.length() - 2); // remove the last comma
            builder.append("\n},\n");
        }
        builder.setLength(builder.length() - 2); // remove the last comma
        builder.append("\n]");
        return builder.toString();
    }
}

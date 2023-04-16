import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class task2 {

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("data.xml"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("<name>")) {
                    String name = line.replaceAll("<name>|</name>", "").trim();
                    System.out.println("Name: " + name);
                } else if (line.contains("<postalZip>")) {
                    String postalZip = line.replaceAll("<postalZip>|</postalZip>", "").trim();
                    System.out.println("Postal Zip: " + postalZip);
                } else if (line.contains("<region>")) {
                    String region = line.replaceAll("<region>|</region>", "").trim();
                    System.out.println("Region: " + region);
                } else if (line.contains("<country>")) {
                    String country = line.replaceAll("<country>|</country>", "").trim();
                    System.out.println("Country: " + country);
                } else if (line.contains("<address>")) {
                    String address = line.replaceAll("<address>|</address>", "").trim();
                    System.out.println("Address: " + address);
                } else if (line.contains("<list>")) {
                    String list = line.replaceAll("<list>|</list>", "").trim();
                    System.out.println("List: " + list);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Kobra10_FileHandling {
    public static void main(String[] args) {
        String path = "kobra-demo.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("Kobra file handling demo");
            writer.newLine();
            writer.write("Line number 2");
            System.out.println("Wrote to " + path);
        } catch (IOException e) {
            System.out.println("Write error: " + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            System.out.println("File contents:");
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        } catch (IOException e) {
            System.out.println("Read error: " + e.getMessage());
        }
    }
}

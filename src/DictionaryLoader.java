import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DictionaryLoader {
    public static Set<String> loadDictionary(String filename) throws IOException {
        Set<String> dictionary = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            dictionary.add(line.trim().toUpperCase());
        }
        reader.close();
        return dictionary;
    }
}
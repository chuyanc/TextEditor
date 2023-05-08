import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordSuggester {
    private List<String> dictionary;

    public WordSuggester() {
        dictionary = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date", "fig", "grape", "kiwi", "lemon", "mango", "orange"));
    }

    public List<String> getSuggestions(String word) {
        List<String> suggestions = new ArrayList<>();
        for (String entry : dictionary) {
            if (entry.startsWith(word)) {
                suggestions.add(entry);
            }
        }
        return suggestions;
    }
}
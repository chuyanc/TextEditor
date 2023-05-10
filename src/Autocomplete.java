import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Autocomplete implements IAutocomplete {
    private Node root = new Node();
    @Override
    public void addWord(String word, long weight) {
        if (!isValid(word)) {
            return;
        }
        Node node = this.root;
        for (char character : word.toLowerCase().toCharArray()) {
            node.setPrefixes(node.getPrefixes() + 1);
            if (node.getReferences()[character - 97] == null) {
                node.setReferenceByIndex(character - 97, new Node());
            }
            node = node.getReferences()[character - 97];
        }
        node.setTerm(new Term(word, weight));
        node.setWords(1);
        node.setPrefixes(node.getPrefixes() + 1);
    }

    @Override
    public Node buildTrie(String filename, int k) {
        // read lines, check valid, and add word
        int lineCount = -1;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (lineCount == -1) {
                    lineCount = Integer.parseInt(line.trim());
                    continue;
                }

                if (lineCount == 0) {
                    break;
                }

                // Process each line of the file
                line = line.trim();
                String[] parts = line.split("\t");
                long weight = Long.parseLong(parts[0]);
                String word = parts[1];
                if (isValid(word)) {
                    addWord(word, weight);
                }
                lineCount --;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.root;
    }

    @Override
    public Node getSubTrie(String prefix) {
        if (!isValid(prefix)) {
            return null;
        }
        Node node = this.root;
        for (char character : prefix.toLowerCase().toCharArray()) {
            if (node.getReferences()[character - 97] == null) {
                return null;
            }
            node = node.getReferences()[character - 97];
        }
        return node;
    }

    @Override
    public int countPrefixes(String prefix) {
        if (!isValid(prefix)) {
            return 0;
        }
        return getSubTrie(prefix).getPrefixes();
    }

    @Override
    public List<ITerm> getSuggestions(String prefix) {
        if (!isValid(prefix)) {
            return new ArrayList<>();
        }

        List<ITerm> result = new ArrayList<>();
        Node node = getSubTrie(prefix);

        if (node != null) {
            dfs(node, result);
        }

        return result;
    }

    private void dfs(Node node, List<ITerm> result) {
        // If the current node represents the end of a word, add it to the list

        if (node.getWords() == 1) {
            result.add(new Term(node.getTerm().getTerm(), node.getTerm().getWeight()));
        }

        // Traverse all the child nodes of the current node recursively
        for (int i = 0; i < node.getReferences().length; i++) {
            Node child = node.getReferences()[i];
            if (child != null) {
                dfs(child, result);
            }
        }
    }

    private boolean isValid(String word) {
        for (char character : word.toCharArray()) {
            if ((character < 65) || (character > 90 && character < 97) || (character > 122)) {
                return false;
            }
        }
        return true;
    }
}

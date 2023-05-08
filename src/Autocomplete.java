import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Autocomplete implements IAutocomplete {
    private Node root;
    private int maxSuggestions;

    public Autocomplete() {
        root = new Node();
    }

    @Override
    public void addWord(String word, long weight) {
        if (word == null || weight < 0) {
            return;
        }

        Node currNode = root;
        currNode.setPrefixes(currNode.getPrefixes() + 1);
        for (char c : word.toLowerCase().toCharArray()) {
            if (c - 'a' < 0 || c > 'z') {
                return;
            }
            if (currNode.getReferences()[c - 'a'] == null) {
                currNode.getReferences()[c - 'a'] = new Node();
                currNode.setReferences(currNode.getReferences());
            }
            currNode = currNode.getReferences()[c - 'a'];
            currNode.setPrefixes(currNode.getPrefixes() + 1);
        }
        currNode.setWords(1);
        currNode.setTerm(new Term(word, weight));

    }


    @Override
    public Node buildTrie(String filename, int k) {
        root = new Node();

        maxSuggestions = k;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    continue;
                }
                String[] readLine = line.trim().split("\t");
                if (readLine.length != 2) {
                    continue;
                }

                long weight = Long.parseLong(readLine[0]);
                String word = readLine[1];
                addWord(word, weight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public Node getSubTrie(String prefix) {
        if (prefix == null) {
            return null;
        }
        prefix = prefix.toLowerCase().trim();
        Node currNode = root;

        if (prefix.equals("")) {
            return root;
        }
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (c - 'a' < 0 || c > 'z') {
                return null;
            }
            if (currNode.getReferences()[c - 'a'] != null) {
                currNode = currNode.getReferences()[c - 'a'];
            } else {
                return null;
            }
        }
        return currNode;
    }

    @Override
    public int countPrefixes(String prefix) {
        prefix = prefix.toLowerCase().trim();
        Node currNode = root;
        if (prefix == "") {
            return root.getPrefixes();
        }
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (c - 'a' < 0 || c  > 'z') {
                return 0;
            }
            if (currNode.getReferences()[c - 'a'] != null) {
                currNode = currNode.getReferences()[c - 'a'];
            } else {
                return 0;
            }
        }
        return currNode.getPrefixes();
    }

    @Override
    public List<ITerm> getSuggestions(String prefix) {
        List<ITerm> suggestions = new ArrayList<>();
        Node subTrie = getSubTrie(prefix);
        if (subTrie == null) {
            return new ArrayList<>();
        }
        if (this.countPrefixes(prefix) == 0) {
            return new ArrayList<>();
        }
        getSuggestionsHelper(subTrie, suggestions);
        return suggestions;
    }

    private void getSuggestionsHelper(Node node, List<ITerm> suggestions) {
        if (node == null) {
            return;
        }
        if (node.getWords() == 1) {
            suggestions.add(new Term(node.getTerm().getTerm(), node.getTerm().getWeight()));
        }
        for (Node n : node.getReferences()) {
            getSuggestionsHelper(n, suggestions);
        }
    }
}

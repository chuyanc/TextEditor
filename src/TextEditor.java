import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TextEditor {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextEditor::createTextEditor);
    }

    public static void createTextEditor() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        SuggestionPanel suggestionPanel = new SuggestionPanel();
        Autocomplete autocomplete = new Autocomplete();
        autocomplete.buildTrie("C:\\Users\\90749\\IdeaProjects\\_594project\\src\\wiktionary.txt",7);

        JTextArea textArea = createAutocompleteTextArea(suggestionPanel, autocomplete);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(suggestionPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }


    public static JTextArea createAutocompleteTextArea(SuggestionPanel suggestionPanel, Autocomplete autocomplete) {
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.addKeyListener(new KeyAdapter() {
            List<ITerm> suggestions;

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB && suggestions != null && !suggestions.isEmpty()) {
                    e.consume(); // Consume the event to prevent tab character from being added to textArea
                    int caretPosition = textArea.getCaretPosition();
                    String text = textArea.getText();
                    int wordStart = getWordStart(text, caretPosition);
                    ITerm suggestedTerm = suggestions.get(0);
                    String suggestedWord = suggestedTerm.getTerm();

                    try {
                        textArea.getDocument().remove(wordStart, caretPosition - wordStart);
                        textArea.getDocument().insertString(wordStart, suggestedWord, null);
                        textArea.setCaretPosition(wordStart + suggestedWord.length());
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Character.isLetter(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    int caretPosition = textArea.getCaretPosition();
                    String text = textArea.getText();
                    int wordStart = getWordStart(text, caretPosition);
                    if (wordStart < caretPosition) {
                        String word = text.substring(wordStart, caretPosition);
                        suggestions = autocomplete.getSuggestions(word);
                        suggestionPanel.updateSuggestions(suggestions);
                    }
                }
            }

            private int getWordStart(String text, int caretPosition) {
                int wordStart = caretPosition - 1;
                while (wordStart >= 0 && Character.isLetter(text.charAt(wordStart))) {
                    wordStart--;
                }
                return wordStart + 1;
            }
        });

        return textArea;
    }



    public static class SuggestionPanel extends JPanel {
        private JLabel suggestionLabel;

        public SuggestionPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            suggestionLabel = new JLabel();
            add(suggestionLabel);
        }

        public void updateSuggestions(List<ITerm> suggestions) {
            suggestions.sort((s1, s2) -> Long.compare(s2.getWeight(), s1.getWeight())); // Sort suggestions by descending weight
            int maxSuggestions = Math.min(5, suggestions.size()); // Limit the number of suggestions to display

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < maxSuggestions; i++) {
                ITerm suggestion = suggestions.get(i);
                sb.append(suggestion.getTerm()).append(", ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            suggestionLabel.setText(sb.toString());
        }
    }
}
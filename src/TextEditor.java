import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextEditor {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextEditor::createTextEditor);
    }

    public static void createTextEditor() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTextArea textArea = createAutocompleteTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        SuggestionPanel suggestionPanel = new SuggestionPanel();

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(suggestionPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }


    public static JTextArea createAutocompleteTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume(); // Prevents the focus from being transferred to another component
                    int caretPosition = textArea.getCaretPosition();
                    String text = textArea.getText();
                    int wordStart = getWordStart(text, caretPosition);
                    if (wordStart < caretPosition) {
                        String word = text.substring(wordStart, caretPosition);
                        List<String> suggestions = getSuggestions(word);
                        if (!suggestions.isEmpty()) {
                            String suggestedWord = suggestions.get(0);
                            try {
                                textArea.getDocument().remove(wordStart, word.length());
                                textArea.getDocument().insertString(wordStart, suggestedWord, null);
                                textArea.setCaretPosition(wordStart + suggestedWord.length());
                                textArea.moveCaretPosition(wordStart);
                            } catch (BadLocationException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
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

            private List<String> getSuggestions(String word) {
                List<String> dictionary = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date", "fig", "grape", "kiwi", "lemon", "mango", "orange"));
                List<String> suggestions = new ArrayList<>();
                for (String entry : dictionary) {
                    if (entry.startsWith(word)) {
                        suggestions.add(entry);
                    }
                }
                return suggestions;
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

//        public void updateSuggestions(List<String> suggestions) {
//            StringBuilder sb = new StringBuilder();
//            for (String suggestion : suggestions) {
//                sb.append(suggestion).append(", ");
//            }
//            if (sb.length() > 0) {
//                sb.setLength(sb.length() - 2);
//            }
//            suggestionLabel.setText(sb.toString());
//        }
    }
}
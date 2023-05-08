import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class TextKeyListener implements KeyListener {

    private JTextArea textArea;
    private List<String> dictionary;

    public TextKeyListener(JTextArea textArea) {
        this.textArea = textArea;
        this.dictionary = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "date", "fig", "grape", "kiwi", "lemon", "mango", "orange"));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
            int caretPosition = textArea.getCaretPosition();
            String text = textArea.getText();
            int wordStart = getWordStart(text, caretPosition);
            String word = text.substring(wordStart, caretPosition);
            String suggestion = getSuggestion(word);
            if (suggestion != null) {
                try {
                    textArea.getDocument().insertString(caretPosition, suggestion.substring(word.length()), null);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.consume();
        }
    }

    private int getWordStart(String text, int caretPosition) {
        int wordStart = caretPosition - 1;
        while (wordStart >= 0 && Character.isLetter(text.charAt(wordStart))) {
            wordStart--;
        }
        return wordStart + 1;
    }

    private String getSuggestion(String word) {
        for (String entry : dictionary) {
            if (entry.startsWith(word)) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}


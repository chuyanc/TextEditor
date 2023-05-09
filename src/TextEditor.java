import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import java.util.Queue;

public class TextEditor {

    private static JTextArea textArea;
    private static Queue<Character> selectedTextQueue = new LinkedList<>();
    private static int selectionStart = -1;

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


        // 0509 Change
        // Add MouseListener to handle text selection
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectionStart = textArea.getCaretPosition();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int selectionEnd = textArea.getCaretPosition();
                if (selectionStart != -1 && selectionStart != selectionEnd) {
                    // Add selected text to the queue
                    selectedTextQueue.clear();
                    for (int i = selectionStart; i < selectionEnd; i++) {
                        selectedTextQueue.add(textArea.getText().charAt(i));
                    }
                }
            }
        });

        // Add KeyListener to handle text replacement
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !selectedTextQueue.isEmpty()) {
                    // Replace selected text with backspace key
                    e.consume();
                    try {
                        textArea.getDocument().remove(selectionStart, selectedTextQueue.size());
                        selectedTextQueue.clear();
                        selectionStart = -1;
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE && !selectedTextQueue.isEmpty()) {
                    // Replace selected text with delete key
                    e.consume();
                    try {
                        textArea.getDocument().remove(selectionStart, selectedTextQueue.size());
                        selectedTextQueue.clear();
                        selectionStart = -1;
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (Character.isLetterOrDigit(e.getKeyChar()) && !selectedTextQueue.isEmpty()) {
                    // Replace selected text with typed character
                    e.consume();
                    try {
                        textArea.getDocument().remove(selectionStart, selectedTextQueue.size());
                        textArea.getDocument().insertString(selectionStart, String.valueOf(e.getKeyChar()), null);
                        selectedTextQueue.clear();
                        selectionStart = -1;
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // 0509 Change END

        // 0508 Change
        JMenuBar menuBar = new JMenuBar();
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        JMenuItem copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        JMenuItem pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        copyMenuItem.setText("Copy");
        pasteMenuItem.setText("Paste");
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        frame.setJMenuBar(menuBar);
        // 0508 Change End
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
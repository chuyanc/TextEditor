import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Queue;


class TextKeyListener implements KeyListener {

    private final JTextArea textArea;
    private final LinkedList<Character> textContent;
    private final Queue<String> selectedTextQueue;
    private final JPopupMenu popupMenu;

//    public TextKeyListener(JTextArea textArea) {
//        this.textArea = textArea;
//        textContent = new LinkedList<>();
//    }
    public TextKeyListener(JTextArea textArea) {
        this.textArea = textArea;
        textContent = new LinkedList<>();
        selectedTextQueue = new LinkedList<>();
        popupMenu = new JPopupMenu();
        JMenuItem copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        JMenuItem pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        popupMenu.add(copyMenuItem);
        popupMenu.add(pasteMenuItem);
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    String selectedText = textArea.getSelectedText();
                    if (selectedText != null && !selectedText.isEmpty()) {
                        selectedTextQueue.add(selectedText);
                    }
                    popupMenu.show(textArea, e.getX(), e.getY());
                }
            }
        });
    }


    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (Character.isISOControl(keyChar)) {
            handleControlCharacter(keyChar);
        } else {
            textContent.add(keyChar);
        }
        updateTextArea();
    }

    private void handleControlCharacter(char keyChar) {
        int caretPosition = textArea.getCaretPosition();
        if (keyChar == KeyEvent.VK_BACK_SPACE && caretPosition > 0) {
            textContent.remove(caretPosition - 1);
        } else if (keyChar == KeyEvent.VK_DELETE && caretPosition < textContent.size()) {
            textContent.remove(caretPosition);
        }
    }

    private void updateTextArea() {
        StringBuilder textBuilder = new StringBuilder();
        for (char c : textContent) {
            textBuilder.append(c);
        }
        textArea.setText(textBuilder.toString());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            int caretPosition = textArea.getCaretPosition();
            if (caretPosition > 0) {
                try {
                    textArea.getDocument().remove(caretPosition - 1, 1);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
                textArea.setCaretPosition(caretPosition - 1);
                e.consume();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    public Queue<String> getSelectedTextQueue() {
        return selectedTextQueue;
    }


}

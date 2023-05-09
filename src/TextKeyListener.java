import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

class TextKeyListener implements KeyListener {

    private final JTextArea textArea;
    private Stack<String> undoStack;
    private Stack<String> redoStack;


    private final LinkedList<Character> textContent;


    public TextKeyListener(JTextArea textArea) {
        this.textArea = textArea;
        textContent = new LinkedList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }


    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (Character.isISOControl(keyChar)){
            return;
        }
        int caretPosition = textArea.getCaretPosition();
        if (caretPosition >= 0) {
            textContent.add(caretPosition, keyChar);
            undoStack.push(Character.toString(keyChar));
            System.out.println("1:"+undoStack.peek());
            updateTextArea();
            textArea.setCaretPosition(caretPosition + 1);
            e.consume();
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
//        if (keyCode == KeyEvent.VK_TAB) {
//            int caretPosition = textArea.getCaretPosition();
//            if (caretPosition > 0) {
//                try {
//                    textArea.getDocument().remove(caretPosition - 1, 1);
//                } catch (BadLocationException ex) {
//                    throw new RuntimeException(ex);
//                }
//                textArea.setCaretPosition(caretPosition - 1);
//                e.consume();
//            }
//        } else
        if (keyCode == KeyEvent.VK_CONTROL){
            int caretPosition = textArea.getCaretPosition();
            if (caretPosition > 0) {
                char current = textContent.get(caretPosition-1);
                undoStack.push(Character.toString(current));
                textContent.remove(caretPosition-1);
                updateTextArea();
                textArea.setCaretPosition(caretPosition - 1);
                e.consume();
            }
        }
        if (keyCode == KeyEvent.VK_Z && e.isControlDown()){
            undo();

        }
        if (keyCode == KeyEvent.VK_Y && e.isControlDown()){
            redo();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            String prevText = undoStack.pop();
            redoStack.push(prevText);
            int size = textContent.size();
            for (int i = textContent.size()-1; i > size - prevText.length() - 1; i--){
                textContent.remove(i);
                updateTextArea();
            }
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            String nextText = redoStack.pop();
            undoStack.push(nextText);
            for (char c : nextText.toCharArray()){
                textContent.add(c);
            }
            updateTextArea();
        }
    }



}

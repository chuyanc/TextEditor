import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

class TextKeyListener implements KeyListener {

    private final JTextArea textArea;
    private Stack<Map.Entry<String, Integer>> undoStack;
    private Stack<Map.Entry<String, Integer>> redoStack;
    private int lastKeyCode;
    private boolean lastUndo = false;


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
            if (lastUndo){
                redoStack.clear();
                lastUndo = false;
            }
            lastKeyCode = keyChar;
            textContent.add(caretPosition, keyChar);
            Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>(Character.toString(keyChar), textArea.getCaretPosition());
            undoStack.push(entry);
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
        if (keyCode == KeyEvent.VK_BACK_SPACE){
            lastKeyCode = KeyEvent.VK_BACK_SPACE;
            int caretPosition = textArea.getCaretPosition();
            if (caretPosition > 0) {
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
            Map.Entry<String, Integer> entry = undoStack.pop();
            String prevText = entry.getKey();
            int pos = entry.getValue();
            redoStack.push(entry);
            if (!Character.isISOControl(lastKeyCode)){
                deleteText(prevText.length(), pos+1);
                lastUndo = true;
            }

        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Map.Entry<String, Integer> entry = redoStack.pop();
            String nextText = entry.getKey();
            int pos = entry.getValue();
            undoStack.push(new AbstractMap.SimpleEntry<>(nextText, pos));
            if (!Character.isISOControl(lastKeyCode)){
                insertText(nextText, pos);
            }
        }
    }


    public void deleteText(int textSize, int pos){
        if (pos <= 0) return;
        for (int i = pos-1; i > pos-textSize-1; i--){
            textContent.remove(i);
        }
        updateTextArea();
        textArea.setCaretPosition(pos-1);
    }

    public void insertText(String toBeInserted, int pos){
        for (int i = 0; i < toBeInserted.length(); i++){
            textContent.add(pos+i, toBeInserted.charAt(i));
        }
        updateTextArea();
        textArea.setCaretPosition(pos+1);
    }



}

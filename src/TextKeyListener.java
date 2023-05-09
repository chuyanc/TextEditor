import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import java.util.Queue;

class TextKeyListener implements KeyListener {

    private final JTextArea textArea;
    private final LinkedList<Character> textContent;


    public TextKeyListener(JTextArea textArea) {
        this.textArea = textArea;
        textContent = new LinkedList<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        char keyChar = e.getKeyChar();
//        if (Character.isISOControl(keyChar)) {
//            handleControlCharacter(keyChar);
//        } else {
//            textContent.add(keyChar);
//        }
//        updateTextArea();
        char keyChar = e.getKeyChar();
        int caretPosition = textArea.getCaretPosition();
        if (caretPosition >= 0) {
            textContent.add(caretPosition, keyChar);
            updateTextArea();
            textArea.setCaretPosition(caretPosition + 1);
            e.consume();
        }
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

//    @Override
//    public void keyPressed(KeyEvent e) {
//        int keyCode = e.getKeyCode();
//        if (keyCode == KeyEvent.VK_BACK_SPACE) {
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
//        }
//    }



    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_CONTROL){
            int caretPosition = textArea.getCaretPosition();
            if (caretPosition > 0) {
                textContent.remove(caretPosition-1);
                updateTextArea();
                textArea.setCaretPosition(caretPosition - 1);
                e.consume();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }


}

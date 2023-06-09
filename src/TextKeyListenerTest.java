import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextKeyListenerTest {

    private JTextArea textArea;
    private TextKeyListener textKeyListener;

    @BeforeEach
    public void setUp() {
        textArea = new JTextArea();
        textKeyListener = new TextKeyListener(textArea);
        textArea.addKeyListener(textKeyListener);
    }

    @Test
    public void testInsertCharacter() {
        textArea.setText("");
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a'));
        assertEquals("a", textArea.getText());
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'b'));
        assertEquals("ab", textArea.getText());
        textArea.setCaretPosition(1);
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'c'));
        assertEquals("acb", textArea.getText());
    }

    @Test
    public void testDeleteCharacter() {
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a'));
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'b'));
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'c'));
        textArea.setCaretPosition(2);
        KeyEvent backspaceEvent = new KeyEvent(textArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        textKeyListener.keyPressed(backspaceEvent);
        assertEquals("ac", textArea.getText());
        textArea.setCaretPosition(1);
        textKeyListener.keyPressed(backspaceEvent);
        assertEquals("c", textArea.getText());
    }

    @Test
    public void testUndoRedo() {
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a'));
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'b'));
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'c'));
        textKeyListener.keyTyped(new KeyEvent(textArea, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'd'));
        KeyEvent undoEvent = new KeyEvent(textArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_Z, KeyEvent.CHAR_UNDEFINED);
        textKeyListener.keyPressed(undoEvent);
        textArea.setCaretPosition(1);
        textKeyListener.keyPressed(undoEvent);
        assertEquals("ab", textArea.getText());
        KeyEvent redoEvent = new KeyEvent(textArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_Y, KeyEvent.CHAR_UNDEFINED);
        textArea.setCaretPosition(0);
        textKeyListener.keyPressed(redoEvent);
        textKeyListener.keyPressed(redoEvent);
        assertEquals("abcd", textArea.getText());
        textKeyListener.keyPressed(undoEvent);
        textKeyListener.keyPressed(undoEvent);
        textKeyListener.keyPressed(undoEvent);
        assertEquals("a", textArea.getText());

    }


}

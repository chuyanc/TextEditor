import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        KeyEvent backspaceEvent = new KeyEvent(textArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        textKeyListener.keyPressed(backspaceEvent);
        assertEquals("ac", textArea.getText());
        textArea.setCaretPosition(1);
        KeyEvent backspaceEvent2 = new KeyEvent(textArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED);
        textKeyListener.keyPressed(backspaceEvent2);
        assertEquals("c", textArea.getText());
    }


}

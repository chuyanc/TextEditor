import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.event.KeyEvent;
import java.util.List;
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
    }

    @Test
    public void testDeleteCharacter() {
        textArea.setText("ab");
        textArea.setCaretPosition(1);
        KeyEvent backspaceEvent = new KeyEvent(textArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        textKeyListener.keyPressed(backspaceEvent);
        assertEquals("b", textArea.getText());
    }

    @Test
    void testGetSuggestions() {
        WordSuggester wordSuggester = new WordSuggester();

        List<String> suggestions1 = wordSuggester.getSuggestions("a");
        assertEquals(1, suggestions1.size());
        assertEquals("apple", suggestions1.get(0));

        List<String> suggestions2 = wordSuggester.getSuggestions("b");
        assertEquals(1, suggestions2.size());
        assertEquals("banana", suggestions2.get(0));

        List<String> suggestions3 = wordSuggester.getSuggestions("z");
        assertEquals(0, suggestions3.size());
    }
}

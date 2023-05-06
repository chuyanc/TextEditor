import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class TextEditor {

    public static void main(String[] args) throws InterruptedException, AWTException {

        SwingUtilities.invokeLater(TextEditor::createTextEditor);

        Thread.sleep(2000);

        Robot robot = new Robot();
        robot.setAutoDelay(100);

        type(robot, "Hello, world");
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        type(robot, "This is a test.");
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);

        type(robot, " line.");

        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);

        type(robot, "new ");
    }
    private static void type(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            boolean isUpperCase = Character.isUpperCase(c);
            boolean isLowerCase = Character.isLowerCase(c);

            if (isUpperCase) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            } else if (isLowerCase) {
                c = Character.toUpperCase(c);
            }

            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException("Key code not found for character: '" + c + "'");
            }

            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);

            if (isUpperCase) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
    }

    public static void createTextEditor() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        TextKeyListener textKeyListener = new TextKeyListener(textArea);
        textArea.addKeyListener(textKeyListener);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}


import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SimpleTextEditor extends JTextPane {

    private static final long serialVersionUID = 1L;


    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new TextEditor().createTextEditor();
        // new TextEditor().setVisible(true);

    }
}

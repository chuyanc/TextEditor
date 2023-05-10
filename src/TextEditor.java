
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.awt.Window;



import java.util.*;



public class TextEditor extends JFrame implements ActionListener {

    private final String[] dragDropExtensionFilter = {".txt", ".dat", ".log", ".xml", ".mf", ".html"};
    private static long serialVersionUID = 1L;

    private JComboBox<Integer> fontSize;
    private JComboBox<String> fontType;

    //setup icons - Bold and Italic
    private File file;
    private final ImageIcon boldIcon = new ImageIcon(TextEditor.class.getResource("icons/bold.png"));
    private final ImageIcon italicIcon = new ImageIcon("icons/italic.png");

    // setup icons - File Menu
    private static ImageIcon newIcon = new ImageIcon(TextEditor.class.getResource("icons/new.png"));
    private static ImageIcon openIcon = new ImageIcon(TextEditor.class.getResource("icons/open.png"));
    private static ImageIcon saveIcon = new ImageIcon(TextEditor.class.getResource("icons/save.png"));
    private static ImageIcon closeIcon = new ImageIcon(TextEditor.class.getResource("icons/close.png"));

    // setup icons - Edit Menu
     private static ImageIcon clearIcon = new ImageIcon(TextEditor.class.getResource("icons/clear.png"));
    private static ImageIcon cutIcon = new ImageIcon(TextEditor.class.getResource("icons/cut.png"));
    private static ImageIcon copyIcon = new ImageIcon(TextEditor.class.getResource("icons/copy.png"));
    private static ImageIcon pasteIcon = new ImageIcon(TextEditor.class.getResource("icons/paste.png"));
    private ImageIcon selectAllIcon = new ImageIcon(TextEditor.class.getResource("icons/selectall.png"));
    private ImageIcon wordwrapIcon = new ImageIcon(TextEditor.class.getResource("icons/wordwrap.png"));


    private JMenuBar menuBar;
    private JMenu editMenu;
    private JMenu findMenu;
    private JMenu aboutMenu;
    private JMenu menuFile;
    private JMenu menuFind;

    private JMenuItem newFile;
    private JMenuItem openFile;
    private  JMenuItem saveFile;
    private JMenuItem close;
    private JMenuItem clearFile;
//    private  JMenuItem quickFind;
    private  JMenuItem closeMenuItem;
    private  JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;

    JButton newButton, openButton, saveButton, clearButton, quickButton, aboutMeButton, aboutButton, closeButton, boldButton, italicButton;
    private JMenuItem cut, copy, paste, selectAll, wordWrap;
    private boolean edit = false;


    private static ImageIcon searchIcon = new ImageIcon(TextEditor.class.getResource("icons/search.png"));
    private Action selectAllAction;



    private static JTextArea textArea;
    private static Queue<Character> selectedTextQueue = new LinkedList<>();
    private static int selectionStart = -1;
    private static JToolBar mainToolbar;

    public void createTextEditor() {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        textArea = new JTextArea("", 0, 0);
        TextKeyListener textKeyListener = new TextKeyListener(textArea);
        textArea.addKeyListener(textKeyListener);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setWrapStyleWord(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        getContentPane().add(panel);


////        textArea = createAutocompleteTextArea();
//        JScrollPane scrollPane = new JScrollPane(textArea);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//
        SuggestionPanel suggestionPanel = new SuggestionPanel();
////        TextKeyListener textKeyListener = new TextKeyListener(textArea);
////        textArea.addKeyListener(textKeyListener);


        frame.add(scrollPane);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(suggestionPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        // Kelly's Change
        menuBar = new JMenuBar();
        editMenu = new JMenu("Edit");
        findMenu = new JMenu("Find");

        menuFile = new JMenu("Save");
        menuFind = new JMenu("Search");

        newFile = new JMenuItem("New", newIcon);
        openFile = new JMenuItem("Open", openIcon);
        saveFile = new JMenuItem("Save", saveIcon);
        close = new JMenuItem("Quit", closeIcon);

        closeMenuItem = new JMenuItem("Quit", closeIcon);
        copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());

        menuBar = new JMenuBar();
        menuBar.add(editMenu);
        menuBar.add(menuFile);

        // Set Actions:
        selectAllAction = new SelectAllAction("Select All", clearIcon, "Select all text", (KeyEvent.VK_A),
                textArea);


        // Save File
        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuFile.add(saveFile);


        // Select All Text
        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");
        selectAll.setIcon(selectAllIcon);
        selectAll.setToolTipText("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        editMenu.add(selectAll);

        // Clear File (Code)

        // Cut Text
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        editMenu.add(cut);

        // WordWrap
        wordWrap = new JMenuItem();
        wordWrap.setText("Word Wrap");
        wordWrap.setIcon(wordwrapIcon);
        wordWrap.setToolTipText("Word Wrap");

        //Short cut key or key stroke
        wordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        editMenu.add(wordWrap);

        /* CODE FOR WORD WRAP OPERATION
         * BY DEFAULT WORD WRAPPING IS ENABLED.
         */
        wordWrap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textArea.setLineWrap(!textArea.getLineWrap());
            }
        });

        // Copy Text
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        editMenu.add(copy);

        // Paste Text
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        editMenu.add(paste);

        // Find Word


        mainToolbar = new JToolBar();



        quickButton = new JButton(searchIcon);
        quickButton.setToolTipText("Quick Search");
        quickButton.addActionListener(this);
        mainToolbar.add(quickButton);
        mainToolbar.addSeparator();



        boldButton = new JButton(boldIcon);
        boldButton.setToolTipText("Bold");
        boldButton.addActionListener(this);
        menuBar.add(boldButton);


        italicButton = new JButton(italicIcon);
        italicButton.setToolTipText("Italic");
        italicButton.addActionListener(this);
        menuBar.add(italicButton);


        closeButton = new JButton(closeIcon);
        closeButton.setToolTipText("Quit");
        closeButton.addActionListener(this);
        menuBar.add(closeButton);


        newButton = new JButton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        menuBar.add(newButton);



        clearButton = new JButton(clearIcon);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        menuBar.add(clearButton);





        //Adding Action Listener on fontType JComboBox
        fontType = new JComboBox<String>();
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (String font : fonts) {
            //Adding font family names to font[] array
            fontType.addItem(font);
        }
        //Setting maximize size of the fontType ComboBox
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        menuBar.add(fontType);


        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //Getting the selected fontType value from ComboBox
                String p = fontType.getSelectedItem().toString();
                //Getting size of the current font or text
                int s = textArea.getFont().getSize();
                textArea.setFont(new Font(p, Font.PLAIN, s));
            }
        });


        fontSize = new JComboBox<Integer>();

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        menuBar.add(fontSize);

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                String fontFamily = textArea.getFont().getFamily();

                Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
                textArea.setFont(font1);
            }
        });

        frame.setJMenuBar(menuBar);
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

    protected JTextArea getEditor() {
        return textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Save")) {
            saveFile();
        }
        if (e.getSource() == boldButton) {
            if (textArea.getFont().getStyle() == Font.BOLD) {
                textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
            }
        }
        if (e.getSource() == italicButton) {
            if (textArea.getFont().getStyle() == Font.ITALIC) {
                textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
            }
        }
        if (e.getSource() == close || e.getSource() == closeButton) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save and exit
                    saveFile();
                    System.exit(0);
                } else if (n == 1) {// no save and exit
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        }
        if (e.getSource() == newFile || e.getSource() == newButton) {
            if (edit) {
                Object[] options = {"Save", "No Save", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file at first ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    textArea.setText("");
                }
            } else {
                textArea.setText("");
            }
        }
        if (e.getSource() == clearFile || e.getSource() == clearButton) {

            Object[] options = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(this, "Are you sure to clear the text Area ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {// clear
                textArea.setText("");
            }
        }
    }


    public static class SuggestionPanel extends JPanel {
        private JLabel suggestionLabel;

        public SuggestionPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            suggestionLabel = new JLabel();
            add(suggestionLabel);
        }

        public void updateSuggestions(List<String> suggestions) {
            StringBuilder sb = new StringBuilder();
            for (String suggestion : suggestions) {
                sb.append(suggestion).append(", ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            suggestionLabel.setText(sb.toString());
        }
    }

    class SelectAllAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SelectAllAction(String text, ImageIcon icon, String desc, Integer mnemonic, final JTextArea textArea) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }
    }
    private void saveFile() {
        // Open a file chooser
        JFileChooser fileChoose = new JFileChooser();
        // Open the file, only this time we call
        int option = fileChoose.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File openFile = fileChoose.getSelectedFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
                out.write(textArea.getText());
                out.close();
                // enableAutoComplete(openFile);
                edit = false;
            } catch (Exception ex) { // again, catch any exceptions and...
                // ...write to the debug console
                System.err.println(ex.getMessage());
            }
        }
    }
//    private void newFile() {
//        // Open a file chooser
//        JFileChooser fileChoose = new JFileChooser();
//        // Open the file, only this time we call
//        int option = fileChoose.showSaveDialog(this);
//
//        if (option == JFileChooser.APPROVE_OPTION) {
//            file = null;
//        }
//    }


}
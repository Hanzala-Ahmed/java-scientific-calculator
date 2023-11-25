import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class CalculatorUI {
    private JFrame frame;
    private JTextField displayArea;
    private JPanel buttonPanel;
    private final int buttonHeight = 50; // Set the desired button height

    public CalculatorUI() {
        frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup the display area
        displayArea = new JTextField();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("SansSerif", Font.BOLD, 30));
        displayArea.setPreferredSize(new Dimension(400, 100));
        displayArea.setHorizontalAlignment(JTextField.LEFT);
        frame.add(displayArea, BorderLayout.NORTH);

        // Setup the button panel with 8 rows and 4 columns
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 4, 5, 5)); // 8 rows, 4 columns

        // Adding buttons to the panel
        String[] buttons = {
            "AC", "C", "(", ")", "sin", "cos", "tan", "log",
            "ln", "x!", "x²", "√", "1/x", "π", "Ans", "Mod",
            "7", "8", "9", "/", "4", "5", "6", "*", 
            "1", "2", "3", "-", "0", ".", "=", "+"
        };

        for (String label : buttons) {
            addButton(label, buttonPanel);
        }

        // Add keyboard bindings
        addKeyBindings(buttonPanel);

        // Add the button panel to the frame
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Adjust the window to fit the preferred sizes of its components
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    private void addButton(String label, Container parent) {
        JButton button = new JButton(label);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, buttonHeight));
        button.addActionListener(new ButtonClickListener());
        parent.add(button);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            handleCommand(command);
        }
    }

    private void handleCommand(String command) {
        // Logic to handle command...
        switch (command) {
            case "AC":
                displayArea.setText("");
                break;
            case "C":
                String text = displayArea.getText();
                if (!text.isEmpty()) {
                    displayArea.setText(text.substring(0, text.length() - 1));
                }
                break;
            case "=":
                // Insert expression evaluation logic here
                break;
            default:
                displayArea.setText(displayArea.getText() + command);
                break;
        }
    }

    private void addKeyBindings(JPanel panel) {
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        // Bind number keys
        for (int i = 0; i <= 9; i++) {
            int numKey = KeyEvent.VK_0 + i;
            String numVal = String.valueOf(i);
            im.put(KeyStroke.getKeyStroke(numKey, 0), numVal);
            am.put(numVal, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleCommand(numVal);
                }
            });
        }

        // Bind operators and special keys
        bindKeyStrokeToAction(im, am, KeyEvent.VK_EQUALS, "+", "+");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_ADD, "+", "+");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_MINUS, "-", "-");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_SUBTRACT, "-", "-");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_MULTIPLY, "*", "*");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_DIVIDE, "/", "/");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_ENTER, "=", "=");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_BACK_SPACE, "C", "C");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_ESCAPE, "AC", "AC");
        bindKeyStrokeToAction(im, am, KeyEvent.VK_PERIOD, ".", ".");
        // Add more bindings if needed...
    }

    private void bindKeyStrokeToAction(InputMap im, ActionMap am, int keyCode, String keyStrokeName, String actionCommand) {
        im.put(KeyStroke.getKeyStroke(keyCode, 0), keyStrokeName);
        am.put(keyStrokeName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCommand(actionCommand);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorUI());
    }
}

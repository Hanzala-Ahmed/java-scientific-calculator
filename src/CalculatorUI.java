import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class CalculatorUI {
    private JFrame frame;
    private JTextField displayArea;
    private JPanel buttonPanel;
    private final int buttonHeight = 50; // Set the desired button height
    private DecimalFormat df = new DecimalFormat("#.######");
    private double lastResult = 0.0; // To store the last calculated result
    private double memoryValue = 0.0; // To store the value in memory

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
        addKeyBindings(buttonPanel); // Add key bindings to the panel

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
        switch (command) {
            case "AC":
                displayArea.setText("");
                lastResult = 0.0;
                break;
            case "C":
                String text = displayArea.getText();
                if (!text.isEmpty()) {
                    displayArea.setText(text.substring(0, text.length() - 1));
                }
                break;
            case "=":
                evaluate();
                break;
            case "sin":
            case "cos":
            case "tan":
            case "log":
            case "ln":
            case "x²":
            case "√":
            case "1/x":
                applyScientificFunction(command);
                break;
            case "π":
                displayArea.setText(displayArea.getText() + Math.PI);
                break;
           case "Ans":
                displayArea.setText(df.format(memoryValue));
                break;
            default:
                displayArea.setText(displayArea.getText() + command);
                break;
        }
    }
    
    private void applyScientificFunction(String function) {
        try {
            double value = Double.parseDouble(displayArea.getText());
            double result = 0;
            switch (function) {
                case "sin":
                    result = Math.sin(Math.toRadians(value));
                    break;
                case "cos":
                    result = Math.cos(Math.toRadians(value));
                    break;
                case "tan":
                    result = Math.tan(Math.toRadians(value));
                    break;
                case "log":
                    result = Math.log10(value);
                    break;
                case "ln":
                    result = Math.log(value);
                    break;
                case "x²":
                    result = Math.pow(value, 2);
                    break;
                case "√":
                    result = Math.sqrt(value);
                    break;
                case "1/x":
                    result = 1 / value;
                    break;
                case "π":
                    result = Math.PI;
                    break;
            }
            memoryValue = result;
            displayArea.setText(df.format(result));
        } catch (NumberFormatException e) {
            displayArea.setText("Error");
        }
    }

    private void calculateFactorial() {
        try {
            int value = Integer.parseInt(displayArea.getText());
            int result = 1;
            for (int i = 1; i <= value; i++) {
                result *= i;
            }
            memoryValue = result;
            displayArea.setText(df.format(result));
        } catch (NumberFormatException e) {
            displayArea.setText("Error");
        }
    }

    private void evaluate() {
        // The evaluate method now uses the script engine to calculate the result
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            Object result = engine.eval(displayArea.getText().replaceAll("√", "Math.sqrt")
                                                          .replaceAll("π", "Math.PI")
                                                          .replaceAll("x²", "Math.pow(2)")
                                                          .replaceAll("ln", "Math.log")
                                                          .replaceAll("log", "Math.log10")
                                                          .replaceAll("Mod", "%"));
            memoryValue = Double.parseDouble(result.toString());
            displayArea.setText(df.format(memoryValue));
        } catch (ScriptException e) {
            displayArea.setText("Error");
        } catch (NumberFormatException e) {
            displayArea.setText("Num Error");
        }
    }

    private void addKeyBindings(JPanel panel) {
    InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = panel.getActionMap();

    // Bind number keys and some operators
    for (int i = 0; i <= 9; i++) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(Character.forDigit(i, 10), 0);
        bindKeyStrokeToAction(im, am, keyStroke, String.valueOf(i));
    }

    bindKeyStrokeToAction(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "=");
    bindKeyStrokeToAction(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "C");
    bindKeyStrokeToAction(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "AC");
    // Add more bindings for other keys if necessary
}

    private void bindKeyStrokeToAction(InputMap im, ActionMap am, KeyStroke keyStroke, String actionCommand) {
    am.put(actionCommand, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleCommand(actionCommand);
        }
    });
    im.put(keyStroke, actionCommand);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorUI());
    }
}

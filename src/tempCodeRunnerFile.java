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
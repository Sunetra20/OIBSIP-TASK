import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NumberGame {
    private static final int MAX_NUMBER = 100;
    private static final int MAX_ATTEMPTS = 10;
    private static final int BASE_POINTS = 100;
    
    private static Random random = new Random();
    
    private int numberToGuess;
    private int attemptsLeft;
    private int totalScore;
    private int roundsPlayed;
    
    // GUI Components
    private JFrame frame;
    private JLabel titleLabel;
    private JLabel promptLabel;
    private JTextField guessField;
    private JButton submitButton;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JTextArea historyArea;
    private JButton newGameButton;
    private JPanel mainPanel;
    private BackgroundPanel backgroundPanel;

    public NumberGame() {
        initializeGame();
        createGUI();
        startNewRound();
    }
    
    private void initializeGame() {
        totalScore = 0;
        roundsPlayed = 0;
    }
    
    private void createGUI() {
        // Set up the main frame
        frame = new JFrame("Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLayout(new BorderLayout());
        
        // Create custom background panel
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        
        // Main panel with semi-transparent background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 200));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Fonts
        Font titleFont = new Font("Verdana", Font.BOLD, 22);
        Font labelFont = new Font("Verdana", Font.PLAIN, 13);
        Font buttonFont = new Font("Verdana", Font.BOLD, 13);
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titleLabel = new JLabel("🎮 Number Guessing Game 🎮");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(50, 50, 150));
        titlePanel.add(titleLabel);
        
        // Game panel
        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new GridLayout(5, 1, 8, 8));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        promptLabel = new JLabel("Guess a number between 1 and " + MAX_NUMBER);
        promptLabel.setFont(labelFont);
        
        attemptsLabel = new JLabel("Attempts left: " + MAX_ATTEMPTS);
        attemptsLabel.setFont(labelFont);
        
        scoreLabel = new JLabel("Score: " + totalScore + " | Round: " + roundsPlayed);
        scoreLabel.setFont(labelFont);
        
        guessField = new JTextField();
        guessField.setFont(labelFont);
        guessField.setPreferredSize(new Dimension(100, 28));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        
        // Styled submit button
        submitButton = new JButton("SUBMIT GUESS");
        styleButton(submitButton, new Color(65, 105, 225)); // RoyalBlue
        submitButton.setFont(buttonFont);
        submitButton.setPreferredSize(new Dimension(120, 32));
        submitButton.addActionListener(e -> checkGuess());
        
        // History area
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        historyArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(180, 120));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 255, 100), 1));
        
        // New game button
        newGameButton = new JButton("NEW ROUND");
        styleButton(newGameButton, new Color(34, 139, 34)); // ForestGreen
        newGameButton.setFont(buttonFont);
        newGameButton.setPreferredSize(new Dimension(120, 32));
        newGameButton.addActionListener(e -> startNewRound());
        newGameButton.setVisible(false);
        
        // Add components to panels
        gamePanel.add(promptLabel);
        gamePanel.add(attemptsLabel);
        gamePanel.add(scoreLabel);
        gamePanel.add(guessField);
        gamePanel.add(submitButton);
        
        // Control panel for buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.add(newGameButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.EAST);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        frame.add(backgroundPanel);
        
        // Add keyboard listener
        guessField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkGuess();
                }
            }
        });
        
        frame.setVisible(true);
    }
    
    // Custom background panel with gaming pattern
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
           try {
            // Load your image (change the path to your image file)
            backgroundImage = ImageIO.read(new File("background.jpg"));
           } catch (IOException e) {
            e.printStackTrace();
            // Fallback to gradient if image fails to load
            backgroundImage = null;
           }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (backgroundImage != null) {
            // Draw the image scaled to fit the panel
               g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
            // Gradient background
             GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 182, 193),  // Hot pink
                getWidth(), getHeight(), new Color(219, 112, 147)  // Medium violet-red
);
             g2d.setPaint(gradient);
             g2d.fillRect(0, 0, getWidth(), getHeight());
             g2d.setColor(new Color(0, 0, 0, 100));
             g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        
             // Gaming pattern elements
            g2d.setColor(new Color(100, 255, 100, 30));
            for (int i = 0; i < 200; i++) {
                int x = (int)(Math.random() * getWidth());
                int y = (int)(Math.random() * getHeight());
                int size = (int)(Math.random() * 5) + 1;
                g2d.fillOval(x, y, size, size);
            }
            
            // Grid pattern
            g2d.setColor(new Color(150, 150, 255, 10));
            for (int x = 0; x < getWidth(); x += 20) {
                g2d.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += 20) {
                g2d.drawLine(0, y, getWidth(), y);
            }
            
            // Corner decorations
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(100, 200, 255, 100));
            int cornerSize = 50;
            // Top-left
            g2d.drawLine(0, 0, cornerSize, 0);
            g2d.drawLine(0, 0, 0, cornerSize);
            // Top-right
            g2d.drawLine(getWidth(), 0, getWidth()-cornerSize, 0);
            g2d.drawLine(getWidth(), 0, getWidth(), cornerSize);
            // Bottom-left
            g2d.drawLine(0, getHeight(), cornerSize, getHeight());
            g2d.drawLine(0, getHeight(), 0, getHeight()-cornerSize);
            // Bottom-right
            g2d.drawLine(getWidth(), getHeight(), getWidth()-cornerSize, getHeight());
            g2d.drawLine(getWidth(), getHeight(), getWidth(), getHeight()-cornerSize);
        }
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.brighter(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.brighter().brighter(), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.brighter(), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });
    }
    
    private void startNewRound() {
        numberToGuess = random.nextInt(MAX_NUMBER) + 1;
        attemptsLeft = MAX_ATTEMPTS;
        roundsPlayed++;
        
        promptLabel.setText("Guess a number between 1 and " + MAX_NUMBER);
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        scoreLabel.setText("Score: " + totalScore + " | Round: " + roundsPlayed);
        guessField.setText("");
        guessField.setEnabled(true);
        submitButton.setEnabled(true);
        newGameButton.setVisible(false);
        historyArea.setText("");
        
        addHistoryMessage("--- Round " + roundsPlayed + " ---");
        addHistoryMessage("Guess between 1 and " + MAX_NUMBER);
    }
    
    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            
            if (guess < 1 || guess > MAX_NUMBER) {
                addHistoryMessage("Please enter between 1-" + MAX_NUMBER);
                return;
            }
            
            attemptsLeft--;
            attemptsLabel.setText("Attempts left: " + attemptsLeft);
            
            if (guess == numberToGuess) {
                int roundScore = BASE_POINTS - (MAX_ATTEMPTS - attemptsLeft) * 5;
                totalScore += Math.max(roundScore, 10);
                
                addHistoryMessage("🎉 CORRECT! Number was " + numberToGuess);
                addHistoryMessage("Guessed in " + (MAX_ATTEMPTS - attemptsLeft) + " attempts!");
                addHistoryMessage("+" + roundScore + " points (Total: " + totalScore + ")");
                
                scoreLabel.setText("Score: " + totalScore + " | Round: " + roundsPlayed);
                guessField.setEnabled(false);
                submitButton.setEnabled(false);
                newGameButton.setVisible(true);
            } else if (guess < numberToGuess) {
                addHistoryMessage(guess + " is too LOW");
            } else {
                addHistoryMessage(guess + " is too HIGH");
            }
            
            if (attemptsLeft == 0 && guess != numberToGuess) {
                addHistoryMessage("💀 GAME OVER! Number was " + numberToGuess);
                guessField.setEnabled(false);
                submitButton.setEnabled(false);
                newGameButton.setVisible(true);
            }
            
            guessField.setText("");
            guessField.requestFocus();
        } catch (NumberFormatException ex) {
            addHistoryMessage("Please enter a valid number!");
            guessField.setText("");
        }
    }
    
    private void addHistoryMessage(String message) {
        historyArea.append(message + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NumberGame());
    }
}
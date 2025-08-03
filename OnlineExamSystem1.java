import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class OnlineExamSystem1 {
    private static HashMap<String, String> users = new HashMap<>();
    private static String currentUser = null;
    private static JFrame frame;
    private static int currentQuestion = 0, score = 0;
    private static String[][] questions = {
        {"Java is a ____?", "Language", "Framework", "Browser", "OS", "Language"},
        {"Which method is entry point in Java?", "start()", "main()", "run()", "init()", "main()"},
        {"Which keyword is used to inherit a class?", "extends", "final", "implements", "inherits", "extends"},
        {"Which one is a loop in Java?", "for", "foreach", "iterate", "loop", "for"},
        {"Java is ___ typed language", "statically", "dynamically", "loosely", "not", "statically"},
        {"Which company developed Java?", "Microsoft", "Sun Microsystems", "Google", "Apple", "Sun Microsystems"},
        {"What is JVM?", "Compiler", "Interpreter", "Runtime Environment", "Editor", "Runtime Environment"},
        {"Which package includes Scanner class?", "java.input", "java.util", "java.io", "java.text", "java.util"},
        {"What does JDK stand for?", "Java Docs Kit", "Java Dev Kit", "Java Developer Kit", "Java Deploy Kit", "Java Developer Kit"},
        {"Which symbol is used to inherit in Java?", ":", "::", "->", "extends", "extends"}
    };
    private static java.util.Timer timer;

    public static void main(String[] args) {
        users.put("admin", "admin123");
        SwingUtilities.invokeLater(() -> showLogin());
    }

    private static void showLogin() {
        frame = new JFrame("Login - Online Exam");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("Online Exam Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(80, 10, 250, 30);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 25);
        JTextField userField = new JTextField();
        userField.setBounds(150, 60, 180, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 100, 180, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        loginButton.setBackground(new Color(0, 150, 136));
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (users.containsKey(username) && users.get(username).equals(password)) {
                currentUser = username;
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials");
            }
        });

        frame.add(title);
        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loginButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showDashboard() {
        frame.getContentPane().removeAll();
        frame.setTitle("Welcome " + currentUser);
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JButton updateBtn = new JButton("Update Password");
        JButton examBtn = new JButton("Start Exam");
        JButton logoutBtn = new JButton("Logout");

        updateBtn.addActionListener(e -> showUpdateProfile());
        examBtn.addActionListener(e -> startExam());
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            frame.dispose();
            showLogin();
        });

        frame.add(new JLabel("Welcome, " + currentUser, SwingConstants.CENTER));
        frame.add(updateBtn);
        frame.add(examBtn);
        frame.add(logoutBtn);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    private static void showUpdateProfile() {
        frame.getContentPane().removeAll();
        frame.setTitle("Update Password");
        frame.setLayout(null);

        JLabel label = new JLabel("New Password:");
        label.setBounds(50, 50, 120, 25);
        JPasswordField newPass = new JPasswordField();
        newPass.setBounds(170, 50, 150, 25);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(100, 100, 100, 30);
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(210, 100, 100, 30);

        updateBtn.addActionListener(e -> {
            String newPassVal = new String(newPass.getPassword());
            if (!newPassVal.isEmpty()) {
                users.put(currentUser, newPassVal);
                JOptionPane.showMessageDialog(frame, "Password updated!");
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Password can't be empty.");
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        frame.add(label);
        frame.add(newPass);
        frame.add(updateBtn);
        frame.add(backBtn);
        frame.revalidate();
        frame.repaint();
    }

    private static void startExam() {
        currentQuestion = 0;
        score = 0;
        askQuestion();
    }

    private static void askQuestion() {
        if (currentQuestion >= questions.length) {
            JOptionPane.showMessageDialog(frame, "Exam finished!\nYour Score: " + score + "/" + questions.length);
            showDashboard();
            return;
        }

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        String[] q = questions[currentQuestion];
        JPanel centerPanel = new JPanel(new GridLayout(5, 1));
        JLabel qLabel = new JLabel("Q" + (currentQuestion + 1) + ". " + q[0], SwingConstants.CENTER);
        JRadioButton opt1 = new JRadioButton(q[1]);
        JRadioButton opt2 = new JRadioButton(q[2]);
        JRadioButton opt3 = new JRadioButton(q[3]);
        JRadioButton opt4 = new JRadioButton(q[4]);

        ButtonGroup group = new ButtonGroup();
        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);

        centerPanel.add(qLabel);
        centerPanel.add(opt1);
        centerPanel.add(opt2);
        centerPanel.add(opt3);
        centerPanel.add(opt4);

        JLabel timerLabel = new JLabel("Time Left: 60s", SwingConstants.CENTER);
        frame.add(timerLabel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        JButton nextBtn = new JButton("Next");
        frame.add(nextBtn, BorderLayout.SOUTH);

        final int[] time = {60};
        if (timer != null) timer.cancel();
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                time[0]--;
                timerLabel.setText("Time Left: " + time[0] + "s");
                if (time[0] <= 0) {
                    timer.cancel();
                    checkAnswerAndProceed(opt1, opt2, opt3, opt4, q[5]);
                }
            }
        }, 1000, 1000);

        nextBtn.addActionListener(e -> {
            timer.cancel();
            checkAnswerAndProceed(opt1, opt2, opt3, opt4, q[5]);
        });

        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    private static void checkAnswerAndProceed(JRadioButton o1, JRadioButton o2, JRadioButton o3, JRadioButton o4, String correct) {
        String selected = null;
        if (o1.isSelected()) selected = o1.getText();
        else if (o2.isSelected()) selected = o2.getText();
        else if (o3.isSelected()) selected = o3.getText();
        else if (o4.isSelected()) selected = o4.getText();

        if (selected != null && selected.equals(correct)) {
            score++;
        }
        currentQuestion++;
        askQuestion();
    }
}

import javax.swing.*;

public class MainApplicationFrame extends JFrame {
    private QuestionBank questionBank;
    private JPanel currentPanel;

    public MainApplicationFrame(QuestionBank questionBank) {
        this.questionBank = questionBank;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Quiz Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        JMenuItem defaultViewMenuItem = new JMenuItem("Default View");
        JMenuItem testViewMenuItem = new JMenuItem("Test View");
        JMenuItem testViewPageItem = new JMenuItem("Page View");

        defaultViewMenuItem.addActionListener(e -> switchToDefaultView());
        testViewMenuItem.addActionListener(e -> switchToTestView());
        testViewPageItem .addActionListener(e -> switchToPageView());

        viewMenu.add(defaultViewMenuItem);
        viewMenu.add(testViewMenuItem);
        viewMenu.add(testViewPageItem);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        switchToDefaultView(); // Start with the Default View
    }

    private void switchToDefaultView() {
        if (!(currentPanel instanceof QuestionPage)) {
            currentPanel = new QuestionPage(questionBank); // Assuming QuestionPage constructor accepts QuestionBank
            setContentPane(currentPanel);
            validate();
            repaint();
        }
    }

    private void switchToTestView() {
        if (!(currentPanel instanceof TestPage)) {
            currentPanel = new TestPage(questionBank); // Assuming TestPage constructor accepts QuestionBank
            setContentPane(currentPanel);
            validate();
            repaint();
        }
    }

    private void switchToPageView() {
        if (!(currentPanel instanceof ExportPage)) {
            currentPanel = new ExportPage(questionBank); // Assuming TestPage constructor accepts QuestionBank
            setContentPane(currentPanel);
            validate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuestionBank questionBank = new QuestionBank(); // Initialize your QuestionBank here
            new MainApplicationFrame(questionBank).setVisible(true);
        });
    }
}


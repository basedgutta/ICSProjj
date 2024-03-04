import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OpeningPage extends JPanel {
    private JButton startButton;

    public OpeningPage(ActionListener startButtonListener) {
        // Set the panel layout to BoxLayout along the Y_AXIS for vertical alignment
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Welcome to Question Bank");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
        // Add some space between the label and the button
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Top, Left, Bottom, Right padding

        // Adding the titleLabel directly to the OpeningPage panel
        add(titleLabel);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.addActionListener(startButtonListener);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(startButton);


        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button panel

        // Adding the buttonPanel to the OpeningPage panel
        add(buttonPanel);
    }
}

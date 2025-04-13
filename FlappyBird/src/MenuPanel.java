import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JButton startButton;
    private JButton quitButton;
    private JFrame parentFrame;
    private Image backgroundImage;

    public MenuPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout());

        // Load background image (same as game background)
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        // Create buttons
        startButton = new JButton("Start Game");
        quitButton = new JButton("Quit");

        // Style buttons
        styleButton(startButton);
        styleButton(quitButton);

        // Add action listeners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20));
        buttonPanel.setOpaque(false); // Make transparent
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        // Add to main panel with constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(buttonPanel, gbc);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void startGame() {
        parentFrame.getContentPane().removeAll();
        FlappyBird flappyBird = new FlappyBird();
        parentFrame.add(flappyBird);
        parentFrame.pack();
        flappyBird.requestFocus();
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        String title = "Flappy Bird";
        g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 150);
    }
}
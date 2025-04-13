import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // membuat panel
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Tampilkan menu pertama kali
        MenuPanel menuPanel = new MenuPanel(frame);
        frame.add(menuPanel);
        frame.setVisible(true);
    }
}
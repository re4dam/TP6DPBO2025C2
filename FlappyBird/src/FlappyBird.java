import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    // image attributes
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    // player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // pipes attributes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    // game logic
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    boolean gameOver = false;

    // score system
    int score = 0;
    JLabel scoreLabel;
    ArrayList<Integer> passedPipePairs = new ArrayList<>(); // to track which pipes have been passed

    // constructor
    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        // setBackground(Color.blue);
        setLayout(null); // absolute positioning for score label

        // load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        // create player
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        // initializes score label
        scoreLabel = new JLabel("Score : 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setBounds(0, 20, frameWidth, 30);
        add(scoreLabel);

        // pipes just pipes
        pipesCooldown = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("pipa " + score);
                if (!gameOver) {
                    placePipes();
                }
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    private void resetGame() {
        // Stop existing timers
        gameLoop.stop();
        pipesCooldown.stop();

        // clear pipes
        pipes.clear();

        // reset player position
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        // reset game state
        gameOver = false;
        score = 0;
        scoreLabel.setText("Score: 0");

        // restart timers
        pipesCooldown.start();
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                pipes.remove(i);
            }
        }

        if (gameOver) {
            // Display game over text
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics fm = g.getFontMetrics();
            String gameOverText = "Game Over";
            g.drawString(gameOverText, (frameWidth - fm.stringWidth(gameOverText)) / 2, frameHeight / 2 - 30);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            fm = g.getFontMetrics();
            String restartText = "Press 'R' to restart";
            g.drawString(restartText, (frameWidth - fm.stringWidth(restartText)) / 2, frameHeight / 2 + 10);

            String menuText = "Press 'M' to return to menu";
            g.drawString(menuText, (frameWidth - fm.stringWidth(menuText)) / 2, frameHeight / 2 + 40);

            // Display final score
            g.setFont(new Font("Arial", Font.BOLD, 24));
            fm = g.getFontMetrics();
            String scoreText = "Final Score: " + score;
            g.drawString(scoreText, (frameWidth - fm.stringWidth(scoreText)) / 2, frameHeight / 2 + 80);
        }
    }

    public void placePipes() {
        // ini alternatif dari claude
        int gapPosition = 150 + (int) (Math.random() * (frameHeight - 300));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, gapPosition - pipeHeight, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, gapPosition + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);

        // Add new pipes to tracking list with initial state (not passed)
        passedPipePairs.add(0);
    }

    public void move() {
        if (gameOver) return;

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // check if player has fallen off the screen
        if (player.getPosY() > frameHeight) {
            gameOver();
            return;
        }

        for (int i = 0; i < pipes.size(); i += 2) {
            Pipe upperPipe = pipes.get(i);
            Pipe lowerPipe = pipes.get(i+1);

            // pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            upperPipe.setPosX(upperPipe.getPosX() + upperPipe.getVelocityX());
            lowerPipe.setPosX(lowerPipe.getPosX() + lowerPipe.getVelocityX());

            // check collision with pipes
            if (checkCollision(player, upperPipe) || checkCollision(player, lowerPipe)){
                gameOver();
                return;
            }

            // Check if player has passed this pipe pair
            int pairIndex = i/2;
            if (passedPipePairs.size() > pairIndex &&
                    upperPipe.getPosX() + upperPipe.getWidth() < player.getPosX() &&
                    passedPipePairs.get(pairIndex) == 0) {

                passedPipePairs.set(pairIndex, 1); // Mark this pipe pair as passed
                score++;
                scoreLabel.setText("Score: " + score);
            }
        }

        // Remove pipes that are off screen
        for (int i = pipes.size() - 1; i >= 0; i--) {
            if (pipes.get(i).getPosX() + pipes.get(i).getWidth() < 0) {
                pipes.remove(i);
                // Also remove the corresponding tracking if it's the first pipe of a pair
                if (i % 2 == 0 && i/2 < passedPipePairs.size()) {
                    passedPipePairs.remove(i/2);
                }
            }
        }
    }

    private boolean checkCollision(Player player, Pipe pipe) {
        Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
        Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());

        return playerRect.intersects(pipeRect);
    }

    public void returnToMenu(){
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel(topFrame);
        topFrame.add(menuPanel);
        topFrame.revalidate();
        topFrame.repaint();
    }

    private void gameOver() {
        gameOver = true;
        // show game over dialog;
        showGameOverDialog();
    }

    private void showGameOverDialog() {
        // The popup message is now drawn directly on the panel instead of using JOptionPane
        // This gives a better game experience as it doesn't interrupt the game flow
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            player.setVelocityY(-10);
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            resetGame();
        } else if (e.getKeyCode() == KeyEvent.VK_M && gameOver) {
            returnToMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

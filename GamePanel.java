import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int[] snakeX = new int[750];
    private int[] snakeY = new int[750];
    private int snakeLength = 3;

    private int[] xPos = {
            25, 50, 75, 100, 125, 150, 175, 200, 225, 250,
            275, 300, 325, 350, 375, 400, 425, 450, 475, 500,
            525, 550, 575, 600, 625, 650, 675, 700, 725, 750,
            775, 800, 825, 850
    };

    // Removed 25 and 50 to prevent enemy spawning in snakeTitle area
    private int[] yPos = {
            75, 100, 125, 150, 175, 200, 225, 250,
            275, 300, 325, 350, 375, 400, 425, 450,
            475, 500, 525, 550, 575, 600, 625
    };

    private Random random = new Random();
    private int enemyX, enemyY;

    private int move = 0;
    private int score = 0;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean gameOver = false;

    private ImageIcon snakeTitle = new ImageIcon(getClass().getResource("assets/snaketitle.jpg"));
    private ImageIcon snakeImage = new ImageIcon(getClass().getResource("assets/snakeimage.png"));
    private ImageIcon snakeLeft = new ImageIcon(getClass().getResource("assets/leftmouth.png"));
    private ImageIcon snakeRight = new ImageIcon(getClass().getResource("assets/rightmouth.png"));
    private ImageIcon snakeDown = new ImageIcon(getClass().getResource("assets/downmouth.png"));
    private ImageIcon snakeUp = new ImageIcon(getClass().getResource("assets/upmouth.png"));
    private ImageIcon enemy = new ImageIcon(getClass().getResource("assets/enemy.png"));

    private Timer timer;
    private int delay = 100;

    private JComboBox<String> speedComboBox;

    public GamePanel() {
        setLayout(null);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Initialize speed combo box
        String[] speeds = {"Slow", "Normal", "Fast"};
        speedComboBox = new JComboBox<>(speeds);
        speedComboBox.setFocusable(false);
        speedComboBox.setSelectedIndex(0); // Initially "Slow"

        // Position combo box at top-right corner (adjust as needed)
        speedComboBox.setBounds(750, 20, 100, 25);

        speedComboBox.addActionListener(e -> {
            String selected = (String) speedComboBox.getSelectedItem();
            if ("Slow".equals(selected)) {
                timer.setDelay(200);
            } else if ("Normal".equals(selected)) {
                timer.setDelay(100);
            } else if ("Fast".equals(selected)) {
                timer.setDelay(50);
            }
        });

        this.add(speedComboBox);

        // Initialize timer with slow speed since slow is default now
        timer = new Timer(200, this);
        timer.start();
        newEnemy();
    }

    private void newEnemy() {
        do {
            enemyX = xPos[random.nextInt(xPos.length)];
            enemyY = yPos[random.nextInt(yPos.length)];
        } while (enemyY < 75);  // Safety check to keep enemy in game area

        for (int i = snakeLength - 1; i >= 0; i--) {
            if (snakeX[i] == enemyX && snakeY[i] == enemyY) {
                newEnemy();
                return;
            }
        }
    }

    private void growSnake() {
        snakeX[snakeLength] = snakeX[snakeLength - 1];
        snakeY[snakeLength] = snakeY[snakeLength - 1];
        snakeLength++;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.drawRect(24, 10, 852, 55);
        g.drawRect(24, 74, 852, 576);

        snakeTitle.paintIcon(this, g, 25, 11);

        g.setColor(Color.BLACK);
        g.fillRect(25, 75, 851, 575);

        if (move == 0) {
            snakeX[0] = 100;
            snakeX[1] = 75;
            snakeX[2] = 50;
            snakeY[0] = 100;
            snakeY[1] = 100;
            snakeY[2] = 100;
        }

        // Draw head
        if (left) snakeLeft.paintIcon(this, g, snakeX[0], snakeY[0]);
        else if (right) snakeRight.paintIcon(this, g, snakeX[0], snakeY[0]);
        else if (up) snakeUp.paintIcon(this, g, snakeX[0], snakeY[0]);
        else if (down) snakeDown.paintIcon(this, g, snakeX[0], snakeY[0]);

        // Draw body
        for (int i = 1; i < snakeLength; i++) {
            snakeImage.paintIcon(this, g, snakeX[i], snakeY[i]);
        }

        // Draw enemy
        enemy.paintIcon(this, g, enemyX, enemyY);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 300, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press SPACE to Restart", 320, 350);
        }

        // Draw score and length on top right (near combo box)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Score : " + score, 650, 30);
        g.drawString("Length : " + (snakeLength - 3), 650, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        if (left) snakeX[0] -= 25;
        if (right) snakeX[0] += 25;
        if (up) snakeY[0] -= 25;
        if (down) snakeY[0] += 25;

        // Wall wrapping
        if (snakeX[0] > 850) snakeX[0] = 25;
        if (snakeX[0] < 25) snakeX[0] = 850;
        if (snakeY[0] > 625) snakeY[0] = 75;
        if (snakeY[0] < 75) snakeY[0] = 625;

        checkEnemyCollision();
        checkSelfCollision();
        repaint();
    }

    private void checkSelfCollision() {
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                timer.stop();
                gameOver = true;
            }
        }
    }

    private void checkEnemyCollision() {
        if (snakeX[0] == enemyX && snakeY[0] == enemyY) {
            newEnemy();
            growSnake();
            score++;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            restart();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && !right) {
            left = true;
            up = false;
            down = false;
            right = false;
            move++;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && !left) {
            right = true;
            up = false;
            down = false;
            left = false;
            move++;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && !down) {
            up = true;
            left = false;
            right = false;
            down = false;
            move++;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && !up) {
            down = true;
            left = false;
            right = false;
            up = false;
            move++;
        }
    }

    private void restart() {
        gameOver = false;
        score = 0;
        move = 0;
        snakeLength = 3;
        left = false;
        right = true;
        up = false;
        down = false;
        timer.start();
        newEnemy();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}

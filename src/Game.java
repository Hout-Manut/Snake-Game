package src;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.util.Random;
// import java.util.LinkedList;
// import java.util.Queue;

public class Game extends JPanel implements ActionListener {

    static final int WIDTH = 1280;
    static final int HEIGHT = 720;
    static final int PIXEL_SIZE = 20;
    static final int NUMBER_OF_PIXELS = (WIDTH * HEIGHT) / (PIXEL_SIZE * PIXEL_SIZE);
    static final int START_X = WIDTH / 2;
    static final int START_Y = HEIGHT / 2;

    static final String backgroundColor = "#eaeaea";

    final int[] x = new int[NUMBER_OF_PIXELS];
    final int[] y = new int[NUMBER_OF_PIXELS];

    int speed = 100;

    int length = 3;
    int appleEaten;
    int appleY;
    int appleX;
    char direction = 'R';
    int gameState = 0;
    /*
     * 0 = Menu
     * 1 = Playing
     * 2 = Gameover
     */

    Random random;
    Timer timer;

    // Menu items:

    static final int buttonWidth = 200;
    static final int buttonHeight = 50;

    static final int startButtonX = (WIDTH - 200) / 2;
    static final int startButtonY = (HEIGHT - 50) / 2;
    static final int leaderButtonX = (WIDTH - 200) / 2;
    static final int leaderButtonY = ((HEIGHT - 50) / 2) + 75;
    static final int exitButtonX = (WIDTH - 200) / 2;
    static final int exitButtonY = ((HEIGHT - 50) / 2) + 150;

    public Rectangle startRec = new Rectangle(startButtonX, startButtonY, buttonWidth, buttonHeight);
    static String startColor = "#808080";
    static String startStringColor = "#ffffff";
    public Rectangle leaderRec = new Rectangle(leaderButtonX, leaderButtonY, buttonWidth, buttonHeight);
    static String leaderColor = "#808080";
    static String leaderStringColor = "#ffffff";
    public Rectangle exitRec = new Rectangle(exitButtonX, exitButtonY, buttonWidth, buttonHeight);
    static String exitColor = "#808080";
    static String exitStringColor = "#ffffff";

    Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new MyMouseAdapter(startRec, leaderRec, exitRec));
        timer = new Timer(speed, this);
        timer.start();
        start();
    }

    public void start() {
        timer.stop();
        addApple();
        gameState = 0;
        x[0] = START_X;
        x[1] = START_X;
        x[2] = START_X;
        y[0] = START_Y;
        y[1] = START_Y;
        y[2] = START_Y;
        timer = new Timer(speed, this);
        timer.start();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            if (gameState == 0) {
                Font customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 50.0f);
                g2d.setColor(Color.black);
                g2d.setFont(customFont);
                FontMetrics metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Snake", (WIDTH - metrics.stringWidth("Snake")) / 2, HEIGHT / 4);

                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                int buttonStringHeight = (HEIGHT + 25) / 2;

                g2d.setColor(Color.decode(startColor));
                g2d.fillRoundRect(startButtonX, startButtonY, buttonWidth, buttonHeight, 5, 5);
                g2d.setColor(Color.decode(leaderColor));
                g2d.fillRoundRect(leaderButtonX, leaderButtonY, buttonWidth, buttonHeight, 5, 5);
                g2d.setColor(Color.decode(exitColor));
                g2d.fillRoundRect(exitButtonX, exitButtonY, buttonWidth, buttonHeight, 5, 5);

                g2d.setColor(Color.decode(startStringColor));
                g2d.drawString("Start", (WIDTH - metrics.stringWidth("Start")) / 2, buttonStringHeight);
                g2d.setColor(Color.decode(leaderStringColor));
                g2d.drawString("Leaderboard", (WIDTH - metrics.stringWidth("Leaderboard")) / 2,
                        buttonStringHeight + 75);
                g2d.setColor(Color.decode(exitStringColor));
                g2d.drawString("Exit", (WIDTH - metrics.stringWidth("Exit")) / 2, buttonStringHeight + 150);

            } else if (gameState == 1) {
                g2d.setColor(new Color(245, 75, 60));
                g2d.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

                g2d.setColor(Color.white);
                g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                for (int i = 1; i < length; i++) {
                    g2d.setColor(new Color(40, 200, 150));
                    g2d.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
                }
                g2d.setColor(Color.white);
                g2d.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
                FontMetrics metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Score: " + appleEaten, (WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2,
                        g2d.getFont().getSize());

            } else {
                g2d.setColor(Color.red);
                g2d.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
                FontMetrics metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

                g2d.setColor(Color.white);
                g2d.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Score: " + appleEaten, (WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2,
                        g2d.getFont().getSize());
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void addApple() {
        while (true) {
            appleX = random.nextInt((int) (WIDTH / PIXEL_SIZE)) * PIXEL_SIZE;
            appleY = random.nextInt((int) (HEIGHT / PIXEL_SIZE)) * PIXEL_SIZE;

            for (int i = length; i > 0; i--)
                if (!(appleX == x[i] && appleY == y[i]))
                    continue;
            break;
        }
        // appleX = random.nextInt((int) (WIDTH / PIXEL_SIZE)) * PIXEL_SIZE;
        // appleY = random.nextInt((int) (HEIGHT / PIXEL_SIZE)) * PIXEL_SIZE;
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (direction == 'L')
            x[0] = x[0] - PIXEL_SIZE;
        else if (direction == 'R')
            x[0] = x[0] + PIXEL_SIZE;
        else if (direction == 'U')
            y[0] = y[0] - PIXEL_SIZE;
        else
            y[0] = y[0] + PIXEL_SIZE;
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            length++;
            appleEaten++;
            addApple();
        }
    }

    public void checkHit() {
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                gameState = 2;
                System.out.println("You hit yourself.");
                break;
            }

            if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
                gameState = 2;
                System.out.println("You hit a wall.");
                break;
            }

        }
        if (!(gameState == 1)) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (gameState == 1) {
            move();
            checkApple();
            checkHit();
        }
        repaint();
    }

    public Font loadCustomFont(String fontPath, float size) throws FontFormatException, IOException {
        File fontFile = new File(fontPath);
        return Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
    }

    public class MyKeyAdapter extends KeyAdapter {
        // private Queue<KeyEvent> queue = new LinkedList<KeyEvent>();

        @Override
        public void keyPressed(KeyEvent e) {
            if (gameState == 1) {
                // queue.add(e);
                // System.out.println("Pressed: " + e.getKeyCode());
                // }

                // @Override
                // public void run() {
                // System.out.println("Running");
                // while (!queue.isEmpty()) {
                // KeyEvent e = queue.remove();
                // System.out.println(e.getKeyCode());
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        if (direction != 'R')
                            direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        if (direction != 'L')
                            direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        if (direction != 'D')
                            direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        if (direction != 'U')
                            direction = 'D';
                        break;
                }
            }
        }
    }

    public class MyMouseAdapter extends MouseAdapter {
        private Rectangle startRec;
        private Rectangle leaderRec;
        private Rectangle exitRec;

        public MyMouseAdapter(Rectangle startRec, Rectangle leaderRec, Rectangle exitRec) {
            this.startRec = startRec;
            this.leaderRec = leaderRec;
            this.exitRec = exitRec;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (gameState) {
                case 0:
                    if (startRec.contains(e.getPoint())) {
                        gameState = 1;
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            switch (gameState) {
                case 0:
                    if (startRec.contains(e.getPoint())) {
                        startColor = "#eeeeee";
                        startStringColor = "#000000";
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            e.getComponent().setCursor(Cursor.getDefaultCursor());
        }

    }
}

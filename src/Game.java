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

    static final int fps = 24;

    float speed = 40.0f;
    int length = 3;
    int appleEaten;
    int appleY;
    int appleX;
    char direction = 'R';
    int gameState = 0;
    long frames = 0;
    int alpha = 255;
    /*
     * 0 = Menu
     * 1 = Playing
     * 2 = Gameover
     * 3 = Leaderboard
     */

    Random random;
    Timer timer;





    Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        timer = new Timer(fps, this);
    }

    public void start() {
        addApple();
        gameState = 1;
        for (int i = 0; i < length; i++) {
            x[i] = START_X - (i * PIXEL_SIZE);
            y[i] = START_Y;
        }
        timer.start();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font customFont;
        FontMetrics metrics;

        try {
            if (gameState == 1) {
                g2d.setColor(new Color(245, 75, 60));
                g2d.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                for (int i = 1; i < length; i++) {
                    g2d.setColor(new Color(40, 200, 150));
                    g2d.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
                }
                if (x[0] < 200 && y[0] < 100 && alpha > 60) {
                    alpha -= 20;
                } else if (!(x[0] < 200 && y[0] < 100) && alpha < 255) {
                    alpha += 20;
                }
                g2d.setColor(new Color(0, 0, 0, alpha));
                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                g2d.setFont(customFont);
                g2d.drawString("Score: " + appleEaten, 10, g2d.getFont().getSize());

            } else {
                g2d.setColor(Color.red);
                customFont = loadCustomBoldFont("src/Assets/GeosansLight.ttf", 50.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

                g2d.setColor(Color.black);
                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 34.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Score: " + appleEaten, (WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2,
                        (HEIGHT / 2) + 50);
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
        }
        if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
            gameState = 2;
            System.out.println("You hit a wall.");
        }
        if (!(gameState == 1)) {
            timer.stop();
        }
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        frames++;
        switch (gameState) {

            case 1:
                if (frames % (speed / 10) == 0) {
                    move();
                    checkApple();
                    checkHit();
                }
                break;
        }
        repaint();
    }

    public Font loadCustomFont(String fontPath, float size) throws FontFormatException, IOException {
        File fontFile = new File(fontPath);
        return Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
    }

    public Font loadCustomBoldFont(String fontPath, float size) throws FontFormatException, IOException {
        File fontFile = new File(fontPath);
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
        return baseFont.deriveFont(Font.BOLD);
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

    public void requestFocusForComponent(Component component) {
        if (component != null) {
            component.requestFocusInWindow();
        }
    }
}

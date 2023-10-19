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
    int width;
    int height;

    final int PIXEL_SIZE = 20;
    int NUMBER_OF_PIXELS;
    int start_x;
    int start_y;

    static final String backgroundColor = "#eaeaea";

    int[] y;
    int[] x;
    private Panel panel;

    static final int fps = 60;
    private int delay = 1000 / fps;

    private float speed;
    private int length;
    private int appleEaten;
    private int appleY;
    private int appleX;
    private char direction;
    private long frames = 0;
    private int hudAlpha;
    private int alpha;
    private int countdown;
    private int gameState;
    /*
     * 0 = Countdown
     * 1 = Playing
     * 2 = Gameover
     * 3 = Leaderboard
     */

    private Random random;
    private Timer timer;
    private MyKeyAdapter keyAdapter;

    Game(Panel panel, int width, int height) {
        this.width = width;
        this.height = height;
        random = new Random();
        keyAdapter = new MyKeyAdapter();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(keyAdapter);
        this.panel = panel;
        timer = new Timer(delay, this);
    }

    public void start() {
        System.out.println(width + height);
        this.setPreferredSize(new Dimension(width, height));
        NUMBER_OF_PIXELS = (width * height) / (PIXEL_SIZE * PIXEL_SIZE);
        x = new int[NUMBER_OF_PIXELS];
        y = new int[NUMBER_OF_PIXELS];
        start_x = width / 2;
        start_y = height / 2;

        appleEaten = 0;
        gameState = 0;
        countdown = 3;
        alpha = 0;
        hudAlpha = 255;
        direction = 'R';
        length = 3;
        speed = 40.0f;
        for (int i = 0; i < length; i++) {
            x[i] = start_x - (i * PIXEL_SIZE);
            y[i] = start_y;
        }

        addApple();
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

                g2d.setColor(new Color(93, 158, 149));
                g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                g2d.setColor(new Color(139, 173, 169));
                for (int i = 1; i < length; i++) {
                    g2d.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
                }
                if (x[0] < 200 && y[0] < 100 && hudAlpha > 60) {
                    hudAlpha -= 20;
                } else if (!(x[0] < 200 && y[0] < 100) && hudAlpha < 255) {
                    hudAlpha += 20;
                }
                g2d.setColor(new Color(0, 0, 0, hudAlpha));
                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                g2d.setFont(customFont);
                g2d.drawString("Score: " + appleEaten, 10, g2d.getFont().getSize());

            } else if (gameState == 0) {
                g2d.setColor(new Color(245, 75, 60, alpha));
                g2d.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

                g2d.setColor(new Color(93, 158, 149, alpha));
                g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                g2d.setColor(new Color(139, 173, 169, alpha));
                for (int i = 1; i < length; i++) {
                    g2d.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
                }
                g2d.setColor(Color.red);
                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 50.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString(countdown + "", (width - metrics.stringWidth(countdown + "")) / 2, height / 2);
            } else {
                g2d.setColor(Color.red);
                customFont = loadCustomBoldFont("src/Assets/GeosansLight.ttf", 50.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Game Over", (width - metrics.stringWidth("Game Over")) / 2, height / 2);

                g2d.setColor(Color.black);
                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 34.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Score: " + appleEaten, (width - metrics.stringWidth("Score: " + appleEaten)) / 2,
                        (height / 2) + 50);
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
            appleX = random.nextInt((int) (width / PIXEL_SIZE)) * PIXEL_SIZE;
            appleY = random.nextInt((int) (height / PIXEL_SIZE)) * PIXEL_SIZE;

            for (int i = length; i > 0; i--)
                if (!(appleX == x[i] && appleY == y[i]))
                    continue;
            break;
        }
        // appleX = random.nextInt((int) (width / PIXEL_SIZE)) * PIXEL_SIZE;
        // appleY = random.nextInt((int) (height / PIXEL_SIZE)) * PIXEL_SIZE;
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'L':
                x[0] = x[0] - PIXEL_SIZE;
                break;
            case 'R':
                x[0] = x[0] + PIXEL_SIZE;
                break;
            case 'U':
                y[0] = y[0] - PIXEL_SIZE;
                break;
            case 'D':
                y[0] = y[0] + PIXEL_SIZE;
                break;
        }
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
        if (x[0] < 0 || x[0] > width || y[0] < 0 || y[0] > height) {
            gameState = 2;
            System.out.println("You hit a wall.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        frames++;
        switch (gameState) {
            case 0:
                alpha += 7;
                if (countdown > 0) {
                    if (frames % 30 == 0)
                        countdown--;
                } else {
                    if (frames % 30 == 0)
                        gameState = 1;
                    alpha = 255;
                }
                if (alpha > 255)
                    alpha = 255;
                break;
            case 1:
                if (frames % (speed / 10) == 0) {
                    move();
                    checkApple();
                    checkHit();
                }
                break;
            case 2:
                if (frames % 30 == 0) {
                    timer.stop();
                    panel.switchToMenu();
                }
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
        // private Queue<KeyEvent> keyQueue = new LinkedList<KeyEvent>();

        @Override
        public void keyPressed(KeyEvent e) {
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

    public void requestFocusForComponent(Component component) {
        if (component != null) {
            component.requestFocusInWindow();
        }
    }
}

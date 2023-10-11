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

    static final int fps = 16;

    float speed = 70.0f;
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
    String startColor = "#808080";
    String startStringColor = "#ffffff";
    public Rectangle leaderRec = new Rectangle(leaderButtonX, leaderButtonY, buttonWidth, buttonHeight);
    String leaderColor = "#808080";
    String leaderStringColor = "#ffffff";
    public Rectangle exitRec = new Rectangle(exitButtonX, exitButtonY, buttonWidth, buttonHeight);
    String exitStringColor = "#ffffff";
    String exitColor = "#808080";

    int fakeLength;
    int fakeStartX;
    int fakeStartY;
    int[] fakeX = new int[NUMBER_OF_PIXELS];
    int[] fakeY = new int[NUMBER_OF_PIXELS];
    char fakeDirection = 'D';
    float fakeSpeed = 30.0f;

    Game() {
        random = new Random();
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(startRec, leaderRec, exitRec);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        timer = new Timer(fps, this);
        timer.start();
        start();
    }

    public void start() {
        timer.stop();
        addApple();
        gameState = 0;
        for (int i = 0; i < length; i++) {
            x[i] = START_X - (i * PIXEL_SIZE);
            y[i] = START_Y;
        }
        fakeStartX = random.nextInt((int) (WIDTH / PIXEL_SIZE)) * PIXEL_SIZE;
        fakeStartY = random.nextInt((int) (HEIGHT / PIXEL_SIZE)) * PIXEL_SIZE;
        fakeLength = random.nextInt(20) + 3;
        for (int i = 0; i < fakeLength; i++) {
            fakeX[i] = fakeStartX - (i * PIXEL_SIZE);
            fakeY[i] = fakeStartY;
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
            if (gameState == 0) {

                g2d.setColor(new Color(60, 60, 60, 30));
                g2d.fillRect(fakeX[0], fakeY[0], PIXEL_SIZE, PIXEL_SIZE);
                g2d.setColor(new Color(100, 100, 100, 30));
                for (int i = 1; i < fakeLength; i++) {
                    g2d.fillRect(fakeX[i], fakeY[i], PIXEL_SIZE, PIXEL_SIZE);
                }

                customFont = loadCustomBoldFont("src/Assets/GeosansLight.ttf", 50.0f);
                g2d.setColor(Color.black);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                g2d.drawString("Snake", (WIDTH - metrics.stringWidth("Snake")) / 2, HEIGHT / 4);

                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                int buttonStringHeight = (HEIGHT + 25) / 2;

                g2d.setColor(Color.decode(startColor));
                g2d.fillRoundRect(startButtonX, startButtonY, buttonWidth, buttonHeight, 7, 7);
                g2d.setColor(Color.decode(leaderColor));
                g2d.fillRoundRect(leaderButtonX, leaderButtonY, buttonWidth, buttonHeight, 7, 7);
                g2d.setColor(Color.decode(exitColor));
                g2d.fillRoundRect(exitButtonX, exitButtonY, buttonWidth, buttonHeight, 7, 7);

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

                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                for (int i = 1; i < length; i++) {
                    g2d.setColor(new Color(40, 200, 150));
                    g2d.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
                }
                if (x[0] < 200 && y[0] < 100 && alpha > 60) {
                    alpha -= 20;
                } else if (alpha < 255) {
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

    public void fakeMove() {
        for (int i = fakeLength; i > 0; i--) {
            fakeX[i] = fakeX[i - 1];
            fakeY[i] = fakeY[i - 1];
        }
        int randomDirection = random.nextInt(20);
        switch (randomDirection) {
            case 1:
                if (!(fakeDirection == 'R'))
                    fakeDirection = 'L';
                break;
            case 2:
                if (!(fakeDirection == 'L'))
                    fakeDirection = 'R';
                break;
            case 3:
                if (!(fakeDirection == 'D'))
                    fakeDirection = 'U';
                break;
            case 4:
                if (!(fakeDirection == 'U'))
                    fakeDirection = 'D';
                break;
            default:
                break;
        }
        if (fakeDirection == 'L')
            fakeX[0] = fakeX[0] - PIXEL_SIZE;
        else if (fakeDirection == 'R')
            fakeX[0] = fakeX[0] + PIXEL_SIZE;
        else if (fakeDirection == 'U')
            fakeY[0] = fakeY[0] - PIXEL_SIZE;
        else
            fakeY[0] = fakeY[0] + PIXEL_SIZE;
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

    public void checkFakeHit() {
        if (fakeX[0] < 0)
            fakeX[0] = WIDTH;
        else if (fakeX[0] > WIDTH)
            fakeX[0] = 0;
        else if (fakeY[0] < 0)
            fakeY[0] = HEIGHT;
        else if (fakeY[0] > HEIGHT)
            fakeY[0] = 0;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        frames++;
        switch (gameState) {
            case 0:
                if (frames % (fakeSpeed / 10) == 0) {
                    fakeMove();
                    checkFakeHit();
                }
                break;
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
            Point location = e.getPoint();
            switch (gameState) {
                case 0:
                    if (startRec.contains(location)) {
                        gameState = 1;
                    } else if (leaderRec.contains(location)) {
                        gameState = 3;
                    } else if (exitRec.contains(location)) {
                        System.exit(0);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point location = e.getPoint();
            switch (gameState) {
                case 0:
                    if (startRec.contains(location)) {
                        startColor = "#d9d9d9";
                        startStringColor = "#000000";
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (leaderRec.contains(location)) {
                        leaderColor = "#d9d9d9";
                        leaderStringColor = "#000000";
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (exitRec.contains(location)) {
                        exitColor = "#d9d9d9";
                        exitStringColor = "#000000";
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        startColor = "#808080";
                        leaderColor = "#808080";
                        exitColor = "#808080";
                        startStringColor = "#ffffff";
                        leaderStringColor = "#ffffff";
                        exitStringColor = "#ffffff";
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getDefaultCursor());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

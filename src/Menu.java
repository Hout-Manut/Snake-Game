package src;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import java.util.Random;

public class Menu extends JPanel implements ActionListener {

    static final int WIDTH = 1280;
    static final int HEIGHT = 720;
    static final int PIXEL_SIZE = 20;
    static final int NUMBER_OF_PIXELS = (WIDTH * HEIGHT) / (PIXEL_SIZE * PIXEL_SIZE);

    static final String backgroundColor = "#eaeaea";
    static final int fps = 32;

    private Panel panel;

    int gameState;
    long frames = 0;

    Random random;
    Timer timer;

    // Menu items:

    static int selected = 0;

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

    Menu(Panel panel) {
        random = new Random();
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(startRec, leaderRec, exitRec);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.panel = panel;
        fakeStartX = random.nextInt((int) (WIDTH / PIXEL_SIZE)) * PIXEL_SIZE;
        fakeStartY = random.nextInt((int) (HEIGHT / PIXEL_SIZE)) * PIXEL_SIZE;
        fakeLength = random.nextInt(15) + 5;
        for (int i = 0; i < fakeLength; i++) {
            fakeX[i] = fakeStartX - (i * PIXEL_SIZE);
            fakeY[i] = fakeStartY;
        }
        timer = new Timer(fps, this);
        timer.start();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font customFont;
        FontMetrics metrics;

        try {
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
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
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
    public void actionPerformed(ActionEvent e) {
        frames++;
        switch (gameState) {
            case -1:
                System.exit(0);
            case 1:
                timer.stop();
                panel.changeToGame();
                break;
            case 2:
                break;
        }

        if (frames % (fakeSpeed / 10) == 0) {
            fakeMove();
            checkFakeHit();
        }
        startColor = "#808080";
        leaderColor = "#808080";
        exitColor = "#808080";
        startStringColor = "#ffffff";
        leaderStringColor = "#ffffff";
        exitStringColor = "#ffffff";
        switch (selected) {
            case 0:
                break;
            case 1:
                startColor = "#d9d9d9";
                startStringColor = "#000000";
                break;
            case 2:
                leaderColor = "#d9d9d9";
                leaderStringColor = "#000000";
                break;
            case 3:
                exitColor = "#d9d9d9";
                exitStringColor = "#000000";
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
            // queue.add(e);
            // System.out.println("Pressed: " + e.getKeyCode());
            // }

            // @Override
            // public void run() {
            // System.out.println("Running");
            // while (!queue.isEmpty()) {
            // KeyEvent e = queue.remove();
            // System.out.println(e.getKeyCode());
            if (selected == 0)
                selected = 1;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (selected > 1)
                        selected--;
                    break;
                case KeyEvent.VK_DOWN:
                    if (selected < 3)
                        selected++;
                    break;
                case KeyEvent.VK_ENTER:
                    switch (selected) {
                        case 1:
                            gameState = 1;
                            break;
                        case 3:
                            gameState = -1;
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
                        gameState = -1;
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
                        selected = 1;
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (leaderRec.contains(location)) {
                        selected = 2;
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (exitRec.contains(location)) {
                        selected = 3;
                        Component component = e.getComponent();
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        selected = 0;
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

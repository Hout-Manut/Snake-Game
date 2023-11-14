package src;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;
// import java.util.Queue;

public class Game extends JPanel implements ActionListener {

    private static final int width = 1280;
    private static final int height = 720;

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
    private char lastDirection;
    private char bufferDirection;
    private long frames = 0;
    private int hudAlpha;
    private int gameoverAlpha;
    private int alpha;
    private int countdown;
    private int gameState;
    private int transition;
    private double transitionFrame;;
    private int mouse;
    private int menuAlpha;
    private int restartAlpha;
    private LinkedList<Character> orientation;
    private char oriBuffer;
    /*
    * V = vertical
    * H = horizontal
    * U = up and right
    * R = right and down
    * D = down and left
    * L = left and up
    */
    int size;
    int cut = 3;

    private int selected;
    /*
     * 0 = Countdown
     * 1 = Playing
     * 2 = Dying
     * 3 = Gameover
     */

    private Random random;
    private Timer timer;
    private MyKeyAdapter keyAdapter;
    private MyMouseAdapter mouseAdapter;
    
    private Rectangle menuRec;
    private Rectangle restartRec;
    
    final int buttonWidth = 200;
    final int buttonHeight = 50;
    final int buttonStringHeight;
    
    private int menuButtonX;
    private int menuButtonY;
    private int restartButtonX;
    private int restartButtonY;
    
    private static final String buttonIdle = "#888080";
    private static final String buttonHover = "#dbd9d9";
    
    private String restartColor = buttonIdle;
    private String restartStringColor = "#ffffff";
    
    private String menuColor = buttonIdle;
    private String menuStringColor = "#ffffff";
    
    private Color rgb;

    Game(Panel panel) {
        this.panel = panel;
        random = new Random();
        keyAdapter = new MyKeyAdapter();
        mouseAdapter = new MyMouseAdapter();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(keyAdapter);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        timer = new Timer(delay, this);

        restartButtonX = (int) ((width - buttonWidth) * 0.80);
        restartButtonY = ((height - buttonHeight) / 2) - 60;
        menuButtonX = (int) ((width - buttonWidth) * 0.80);
        menuButtonY = ((height - buttonHeight) / 2) + 60;
        buttonStringHeight = (height + 25) / 2;

        menuRec = new Rectangle(menuButtonX, menuButtonY, buttonWidth, buttonHeight);
        restartRec = new Rectangle(restartButtonX, restartButtonY, buttonWidth, buttonHeight);
    }

    public void start() {
        this.setCursor(Cursor.getDefaultCursor());
        NUMBER_OF_PIXELS = (width * height) / (PIXEL_SIZE * PIXEL_SIZE);
        x = new int[NUMBER_OF_PIXELS];
        y = new int[NUMBER_OF_PIXELS];
        orientation = new LinkedList<Character>();
        start_x = width / 2;
        start_y = height / 2;

        appleEaten = 0;
        gameState = 0;
        countdown = 3;
        transitionFrame = 0;
        alpha = 0;
        gameoverAlpha = 0;
        hudAlpha = 255;
        direction = 'R';
        bufferDirection = 'R';
        oriBuffer = 'H';
        length = 5;
        speed = 40.0f;
        menuAlpha = 0;
        restartAlpha = 0;
        for (int i = 0; i < length; i++) {
            x[i] = start_x - (i * PIXEL_SIZE);
            y[i] = start_y;
            orientation.add(oriBuffer);
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
            switch (gameState) {
                case 1:
                    g2d.setColor(new Color(245, 75, 60));
                    g2d.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

                    g2d.setColor(new Color(93, 158, 149));
                    g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                    g2d.setColor(new Color(139, 173, 169));
                    for (int i = 1; i < length; i++) {
                        size = (int)(cut * i/length);
                        switch (orientation.get(i - 1)) {
                            case 'H':
                                g2d.fillRect(x[i], y[i] + size, PIXEL_SIZE, PIXEL_SIZE - (size * 2));
                                break;
                            case 'V':
                                g2d.fillRect(x[i] + size, y[i], PIXEL_SIZE - (size * 2), PIXEL_SIZE);
                                break;
                            case 'U':
                                g2d.fillRect(x[i], y[i] + size, PIXEL_SIZE - (size * 2), PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i], PIXEL_SIZE - (size * 2), PIXEL_SIZE - size);
                                break;
                            case 'R':
                                g2d.fillRect(x[i] + size, y[i], PIXEL_SIZE - (size * 2), PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE, PIXEL_SIZE - (size * 2));
                                break;
                            case 'D':
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE - size, PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE - (size * 2), PIXEL_SIZE - size);
                                break;
                            case 'L':
                                g2d.fillRect(x[i], y[i] + size, PIXEL_SIZE - size, PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE - (size * 2), PIXEL_SIZE - size);
                                break;
                        }
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
                    break;
                case 0:
                    g2d.setColor(new Color(245, 75, 60, alpha));
                    g2d.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

                    g2d.setColor(new Color(93, 158, 149, alpha));
                    g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                    g2d.setColor(new Color(139, 173, 169, alpha));
                    for (int i = 1; i < length; i++) {
                        size = (int)(cut * i/length);
                        switch (orientation.get(i - 1)) {
                            case 'H':
                                g2d.fillRect(x[i], y[i] + size, PIXEL_SIZE, PIXEL_SIZE - (size * 2));
                                break;
                            case 'V':
                                g2d.fillRect(x[i] + size, y[i], PIXEL_SIZE - (size * 2), PIXEL_SIZE);
                                break;
                            case 'U':
                                g2d.fillRect(x[i], y[i] + size, PIXEL_SIZE - (size * 2), PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i], PIXEL_SIZE - (size * 2), PIXEL_SIZE - size);
                                break;
                            case 'R':
                                g2d.fillRect(x[i] + size, y[i], PIXEL_SIZE - (size * 2), PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE, PIXEL_SIZE - (size * 2));
                                break;
                            case 'D':
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE - size, PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE - (size * 2), PIXEL_SIZE - size);
                                break;
                            case 'L':
                                g2d.fillRect(x[i], y[i] + size, PIXEL_SIZE - size, PIXEL_SIZE - (size * 2));
                                g2d.fillRect(x[i] + size, y[i] + size, PIXEL_SIZE - (size * 2), PIXEL_SIZE - size);
                                break;
                        }
                    }
                    g2d.setColor(Color.red);
                    customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 50.0f);
                    g2d.setFont(customFont);
                    metrics = getFontMetrics(g2d.getFont());
                    g2d.drawString(countdown + "", (width - metrics.stringWidth(countdown + "")) / 2, height / 2);
                    break;
                case 2:
                    g2d.setColor(new Color(245, 75, 60, alpha));
                    g2d.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

                    g2d.setColor(new Color(93, 158, 149, alpha));
                    g2d.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

                    g2d.setColor(new Color(139, 173, 169, alpha));
                    for (int i = 1; i < length; i++) {
                        g2d.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
                    }
                    g2d.setColor(new Color(0, 0, 0, alpha));
                    customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                    g2d.setFont(customFont);
                    g2d.drawString("Score: " + appleEaten, 10, g2d.getFont().getSize());

                    g2d.setColor(new Color(255, 20, 20, gameoverAlpha));
                    customFont = loadCustomBoldFont("src/Assets/GeosansLight.ttf", 50.0f);
                    g2d.setFont(customFont);
                    metrics = getFontMetrics(g2d.getFont());
                    g2d.drawString("Game Over", (width - metrics.stringWidth("Game Over")) / 2,
                            (height / 2) + transition - 30);

                    g2d.setColor(new Color(10, 10, 10, gameoverAlpha));
                    customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 34.0f);
                    g2d.setFont(customFont);
                    metrics = getFontMetrics(g2d.getFont());
                    g2d.drawString("Score: " + appleEaten, (width - metrics.stringWidth("Score: " + appleEaten)) / 2,
                            (height / 2) + 50);

                    rgb = Color.decode(restartColor);
                    g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), restartAlpha));
                    g2d.fillRoundRect(restartButtonX, restartButtonY, buttonWidth, buttonHeight,
                            7, 7);
                    rgb = Color.decode(menuColor);
                    g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), menuAlpha));
                    g2d.fillRoundRect(menuButtonX, menuButtonY, buttonWidth, buttonHeight,
                            7, 7);

                    rgb = Color.decode(menuStringColor);
                    g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                    g2d.drawString("Restart", (int) ((width - metrics.stringWidth("Restart")) * 0.8),
                            buttonStringHeight - 60);
                    rgb = Color.decode(restartStringColor);
                    g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                    g2d.drawString("Menu", (int) ((width - metrics.stringWidth("Menu")) * 0.8),
                            buttonStringHeight + 60);
                    break;
                case 3:
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

                    g2d.setColor(Color.decode(menuColor));
                    g2d.fillRoundRect(restartButtonX, restartButtonY, buttonWidth, buttonHeight,
                            7, 7);
                    g2d.setColor(Color.decode(restartColor));
                    g2d.fillRoundRect(menuButtonX, menuButtonY, buttonWidth, buttonHeight,
                            7, 7);
                    break;
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
        boolean overlap;
        do {
            appleX = random.nextInt((int) (width / PIXEL_SIZE)) * PIXEL_SIZE;
            appleY = random.nextInt((int) (height / PIXEL_SIZE)) * PIXEL_SIZE;
            overlap = false;
            for (int i = length; i > 0; i--)
                if (appleX == x[i] && appleY == y[i]){
                    overlap = true;
                    break;
                }
        } while (overlap);
//        System.out.println("Apple added at (" + appleX + ", " + appleY + ")");
    
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'L':
                x[0] = x[0] - PIXEL_SIZE;
                switch (lastDirection) {
                    case 'U':
                        oriBuffer = 'L';
                        break;
                    case 'D':
                        oriBuffer = 'U';
                        break;
                    case 'L':
                        oriBuffer = 'H';
                        break;
                }

                break;
            case 'R':
                x[0] = x[0] + PIXEL_SIZE;
                switch (lastDirection) {
                    case 'U':
                        oriBuffer = 'D';
                        break;
                    case 'D':
                        oriBuffer = 'R';
                        break;
                    case 'R':
                        oriBuffer = 'H';
                        break;
                }
                break;
            case 'U':
                y[0] = y[0] - PIXEL_SIZE;
                switch (lastDirection) {
                    case 'L':
                        oriBuffer = 'R';
                        break;
                    case 'R':
                        oriBuffer = 'U';
                        break;
                    case 'U':
                        oriBuffer = 'V';
                        break;
                }
                break;
            case 'D':
                y[0] = y[0] + PIXEL_SIZE;
                switch (lastDirection) {
                    case 'L':
                        oriBuffer = 'D';
                        break;
                    case 'R':
                        oriBuffer = 'L';
                        break;
                    case 'D':
                        oriBuffer = 'V';
                        break;
                }
                break;
        }
        orientation.addFirst(oriBuffer);
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            length++;
            appleEaten++;
            addApple();
        }
        else{
            orientation.removeLast();
        }
    }

    public void checkHit() {
        for (int i = length; i > 1; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                gameState = 2;
                System.out.println("You hit yourself.");
                break;
            }
        }
        if (x[0] < 0 || x[0] >= width || y[0] < 0 || y[0] >= height) {
            gameState = 2;
            System.out.println("You hit a wall.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        frames++;
        switch (gameState) {
            case 0:
                direction = 'R';
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
                mouse++;
                if (mouse > 60)
                    showCursor(false);
                else
                    showCursor(true);
                if (frames % (speed / 10) == 0) {
                    lastDirection = direction;
                    direction = bufferDirection;
                    move();
                    checkApple();
                    checkHit();
                }
                break;
            case 2:
                showCursor(true);
                transitionFrame++;
                if (transitionFrame < 30)
                    transition = easeOutCubic(transitionFrame / 30, 100, 5);
                if (transitionFrame > 30 && transitionFrame < 60)
                    menuAlpha += 7;
                if (menuAlpha > 255)
                    menuAlpha = 255;
                restartAlpha = menuAlpha;
                gameoverAlpha += 7;
                alpha -= 7;
                if (gameoverAlpha > 255)
                    gameoverAlpha = 255;
                if (alpha <= 120)
                    alpha = 120;

                restartColor = buttonIdle;
                menuColor = buttonIdle;
                restartStringColor = "#ffffff";
                menuStringColor = "#ffffff";
                switch (selected) {
                    case 0:
                        break;
                    case 1:
                        restartColor = buttonHover;
                        restartStringColor = "#222222";
                        break;
                    case 2:
                        menuColor = buttonHover;
                        menuStringColor = "#222222";
                        break;
                }
                if (transitionFrame > 300) gameState = 3;
                break;
            case 3:
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
            if (gameState == 1)
                mouse += 60;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (direction != 'R')
                        bufferDirection = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (direction != 'L')
                        bufferDirection = 'R';
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (direction != 'D')
                        bufferDirection = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (direction != 'U')
                        bufferDirection = 'D';
                    break;
            }
        }
    }

    public class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            mouse = 0;
            Point location = e.getPoint();
            switch (gameState) {
                case 3:
                    if (menuRec.contains(location)) {
                        gameState = 1;
                    } else if (restartRec.contains(location)) {
                        gameState = 0; // change later
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouse = 0;
            Point location = e.getPoint();
            Component component = e.getComponent();
            switch (gameState) {
                case 2:
                    if (menuRec.contains(location)) {
                        selected = 1;
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (restartRec.contains(location)) {
                        selected = 2;
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        selected = 0;
                        component.setCursor(Cursor.getDefaultCursor());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void showCursor(boolean visible) {
        if (!visible) {
            Toolkit t = Toolkit.getDefaultToolkit();
            Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");
            this.setCursor(noCursor);
        } else {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    public int easeOutCubic(double x, int end, int b) {
        return (int) (end * Math.pow(1 - x, b));
    }

    public void requestFocusForComponent(Component component) {
        if (component != null) {
            component.requestFocusInWindow();
        }
    }
}

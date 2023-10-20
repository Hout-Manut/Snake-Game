package src;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import java.util.Random;

public class Menu extends JPanel implements ActionListener {

    private int width;
    private int height;
    private static int PIXEL_SIZE = 20;
    // private static int NUMBER_OF_PIXELS;

    String backgroundColor = "#eaeaea";
    static final int fps = 60;

    private Panel panel;

    private int delay = 1000 / fps;
    private int state;
    private long frames = 0;
    private int offset;
    private int alpha;
    private int selected;
    private Color rgb;

    private Random random;
    private Timer timer;

    // Menu items:

    final int buttonWidth = 200;
    final int buttonHeight = 50;

    int startButtonX = (width - 200) / 2;
    int startButtonY = (height - 50) / 2;
    int leaderButtonX = (width - 200) / 2;
    int leaderButtonY = ((height - 50) / 2) + 75;
    int exitButtonX = (width - 200) / 2;
    int exitButtonY = ((height - 50) / 2) + 150;

    private static final String buttonIdle = "#888080";
    private static final String buttonHover = "#dbd9d9";
    

    private String startColor = buttonIdle;
    private String startStringColor = "#ffffff";

    private String leaderColor = buttonIdle;
    private String leaderStringColor = "#ffffff";

    private String exitColor = buttonIdle;
    private String exitStringColor = "#ffffff";

    private Rectangle startRec;
    private Rectangle leaderRec;
    private Rectangle exitRec;

    int fakeLength;
    int fakeStartX;
    int fakeStartY;
    int[] fakeX = new int[512];
    int[] fakeY = new int[512];
    char fakeDirection;
    float fakeSpeed;
    // boolean windowChanged = false;

    Menu(Panel panel) {
        random = new Random();
        MyMouseAdapter mouseAdapter = new MyMouseAdapter();
        this.setPreferredSize(new Dimension(1280, 720));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        // this.addComponentListener(new MyComponentAdapter());
        this.panel = panel;
        this.width = panel.frameW;
        this.height = panel.frameH;
        startButtonX = (width - 200) / 2;
        startButtonY = (height - 50) / 2;
        leaderButtonX = (width - 200) / 2;
        leaderButtonY = ((height - 50) / 2) + 75;
        exitButtonX = (width - 200) / 2;
        exitButtonY = ((height - 50) / 2) + 150;

        startRec = new Rectangle(startButtonX, startButtonY, buttonWidth, buttonHeight);
        leaderRec = new Rectangle(leaderButtonX, leaderButtonY, buttonWidth, buttonHeight);
        exitRec = new Rectangle(exitButtonX, exitButtonY, buttonWidth, buttonHeight);
        timer = new Timer(delay, this);
    }
    
    public void start() {
        this.setCursor(Cursor.getDefaultCursor());
        state = 0;
        selected = 0;
        offset = 2;
        alpha = 255;
        fakeSpeed = 40.0f;
        fakeDirection = 'R';
        fakeStartX = random.nextInt((int) (width / PIXEL_SIZE)) * PIXEL_SIZE;
        fakeStartY = random.nextInt((int) (height / PIXEL_SIZE)) * PIXEL_SIZE;
        fakeLength = random.nextInt(15) + 5;
        for (int i = 0; i < fakeLength; i++) {
            fakeX[i] = fakeStartX - (i * PIXEL_SIZE);
            fakeY[i] = fakeStartY;
        }
        timer.start();
    }

    // public void calculateComponent() {
    //     // NUMBER_OF_PIXELS = (width * height) / (PIXEL_SIZE * PIXEL_SIZE);
    //     startButtonX = (width - 200) / 2;
    //     startButtonY = (height - 50) / 2;
    //     leaderButtonX = (width - 200) / 2;
    //     leaderButtonY = ((height - 50) / 2) + 75;
    //     exitButtonX = (width - 200) / 2;
    //     exitButtonY = ((height - 50) / 2) + 150;

    //     startRec = new Rectangle(startButtonX, startButtonY, buttonWidth, buttonHeight);
    //     leaderRec = new Rectangle(leaderButtonX, leaderButtonY, buttonWidth, buttonHeight);
    //     exitRec = new Rectangle(exitButtonX, exitButtonY, buttonWidth, buttonHeight);
    //     windowChanged = false;
    // }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font customFont;
        FontMetrics metrics;

        try {

            if (state == 0) {
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
                g2d.drawString("Snake", (width - metrics.stringWidth("Snake")) / 2, height / 4);

                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                int buttonStringHeight = (height + 25) / 2;

                g2d.setColor(Color.decode(startColor));
                g2d.fillRoundRect((width - 200) / 2, (height - 50) / 2, buttonWidth, buttonHeight, 7, 7);
                g2d.setColor(Color.decode(leaderColor));
                g2d.fillRoundRect((width - 200) / 2, ((height - 50) / 2) + 75, buttonWidth, buttonHeight, 7, 7);
                g2d.setColor(Color.decode(exitColor));
                g2d.fillRoundRect((width - 200) / 2, ((height - 50) / 2) + 150, buttonWidth, buttonHeight, 7, 7);

                g2d.setColor(Color.decode(startStringColor));
                g2d.drawString("Start", (width - metrics.stringWidth("Start")) / 2, buttonStringHeight);
                g2d.setColor(Color.decode(leaderStringColor));
                g2d.drawString("Leaderboard", (width - metrics.stringWidth("Leaderboard")) / 2,
                        buttonStringHeight + 75);
                g2d.setColor(Color.decode(exitStringColor));
                g2d.drawString("Exit", (width - metrics.stringWidth("Exit")) / 2, buttonStringHeight + 150);
            } else {
                customFont = loadCustomFont("src/Assets/GeosansLight.ttf", 30.0f);
                g2d.setFont(customFont);
                metrics = getFontMetrics(g2d.getFont());
                int buttonStringHeight = (height + 25) / 2;

                rgb = Color.decode(startColor);
                g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                g2d.fillRoundRect(startButtonX, startButtonY + offset, buttonWidth, buttonHeight, 7, 7);
                rgb = Color.decode(leaderColor);
                g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                g2d.fillRoundRect(leaderButtonX, leaderButtonY + offset, buttonWidth, buttonHeight, 7, 7);
                rgb = Color.decode(exitColor);
                g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                g2d.fillRoundRect(exitButtonX, exitButtonY + offset, buttonWidth, buttonHeight, 7, 7);

                rgb = Color.decode(startStringColor);
                g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                g2d.drawString("Start", (width - metrics.stringWidth("Start")) / 2, buttonStringHeight + offset);
                rgb = Color.decode(leaderStringColor);
                g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                g2d.drawString("Leaderboard", (width - metrics.stringWidth("Leaderboard")) / 2,
                        buttonStringHeight + 75 + offset);
                rgb = Color.decode(exitStringColor);
                g2d.setColor(new Color(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), alpha));
                g2d.drawString("Exit", (width - metrics.stringWidth("Exit")) / 2, buttonStringHeight + 150 + offset);

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

    public void fakeMove() {
        for (int i = fakeLength; i > 0; i--) {
            fakeX[i] = fakeX[i - 1];
            fakeY[i] = fakeY[i - 1];
        }
        int randomDirection = random.nextInt(25);
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
            fakeX[0] = width;
        else if (fakeX[0] > width)
            fakeX[0] = 0;
        else if (fakeY[0] < 0)
            fakeY[0] = height;
        else if (fakeY[0] > height)
            fakeY[0] = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frames++;
        // if (windowChanged)
        //     calculateComponent();
        switch (state) {
            case -1:
                System.exit(0);
            case 1:
                if (offset <= 512 && alpha > 0) {
                    offset += (offset / 2) + 1;
                    alpha /= 2;
                } else {
                    timer.stop();
                    panel.switchToGame();
                }
                break;
            case 2:
                break;
        }

        if (frames % (fakeSpeed / 10) == 0) {
            fakeMove();
            checkFakeHit();
        }
        startColor = buttonIdle;
        leaderColor = buttonIdle;
        exitColor = buttonIdle;
        startStringColor = "#ffffff";
        leaderStringColor = "#ffffff";
        exitStringColor = "#ffffff";
        switch (selected) {
            case 0:
                break;
            case 1:
                startColor = buttonHover;
                startStringColor = "#222222";
                break;
            case 2:
                leaderColor = buttonHover;
                leaderStringColor = "#222222";
                break;
            case 3:
                exitColor = buttonHover;
                exitStringColor = "#222222";
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

        @Override
        public void keyPressed(KeyEvent e) {
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
                            state = 1;
                            break;
                        case 3:
                            state = -1;
                            break;

                    }
            }
        }
    }

    public class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point location = e.getPoint();
            switch (state) {
                case 0:
                    if (startRec.contains(location)) {
                        state = 1;
                    } else if (leaderRec.contains(location)) {
                        state = 0; // change later
                    } else if (exitRec.contains(location)) {
                        state = -1;
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point location = e.getPoint();
            Component component = e.getComponent();
            switch (state) {
                case 0:
                    if (startRec.contains(location)) {
                        selected = 1;
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (leaderRec.contains(location)) {
                        selected = 2;
                        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (exitRec.contains(location)) {
                        selected = 3;
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

    // public class MyComponentAdapter extends ComponentAdapter {
    //     @Override
    //     public void componentResized(ComponentEvent e) {
    //         Dimension dimension = e.getComponent().getSize();
    //         width = dimension.width;
    //         height = dimension.height;
    //         windowChanged = true;
    //     }
    // }

    public void requestFocusForComponent(Component component) {
        if (component != null) {
            component.requestFocusInWindow();
        }
    }
}
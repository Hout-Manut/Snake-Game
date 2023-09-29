package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Game extends JPanel implements ActionListener, MouseListener {

    static final int WIDTH = 1280;
    static final int HEIGHT = 720;
    static final int PIXEL_SIZE = 20;
    static final int NUMBER_OF_PIXELS = (WIDTH * HEIGHT) / (PIXEL_SIZE * PIXEL_SIZE);
    static final int START_X = WIDTH / 2;
    static final int START_Y = HEIGHT / 2;

    static final String backgroundColor = "#3d3d3d";

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


    Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.decode(backgroundColor));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start();
    }

    public void start() {
        addApple();
        gameState = 1;
        timer = new Timer(speed, this);
        timer.start();
    }

    public void draw(Graphics g) {
        if (gameState == 0) {

        }
        else if (gameState == 1) {
            g.setColor(new Color(245, 75, 60));
            g.fillOval(appleX, appleY, PIXEL_SIZE, PIXEL_SIZE);

            g.setColor(Color.white);
            g.fillRect(x[0], y[0], PIXEL_SIZE, PIXEL_SIZE);

            for (int i = 1; i < length; i++) {
                g.setColor(new Color(40, 200, 150));
                g.fillRect(x[i], y[i], PIXEL_SIZE, PIXEL_SIZE);
            }
            g.setColor(Color.white);
            g.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, (WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2,
                    g.getFont().getSize());

        } else {
            g.setColor(Color.red);
            g.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

            g.setColor(Color.white);
            g.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, (WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2,
                    g.getFont().getSize());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void addApple() {
        // while (true) {
        //     appleX = random.nextInt((int) (WIDTH / PIXEL_SIZE)) * PIXEL_SIZE;
        //     appleY = random.nextInt((int) (HEIGHT / PIXEL_SIZE)) * PIXEL_SIZE;

        //     for (int i = length; i > 0; i--)
        //         if (!(appleX == x[i] && appleY == y[i]))
        //             break;
        // }
        appleX = random.nextInt((int) (WIDTH / PIXEL_SIZE)) * PIXEL_SIZE;
        appleY = random.nextInt((int) (HEIGHT / PIXEL_SIZE)) * PIXEL_SIZE;
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

    public class MyKeyAdapter extends KeyAdapter {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }
}

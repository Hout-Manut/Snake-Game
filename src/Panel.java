package src;

import java.awt.*;
import javax.swing.*;

public class Panel extends JFrame {
    int frameW = 1280;
    int frameH = 720;

    private Menu menu;
    private Game game;

    Panel() {
        menu = new Menu(this);
        this.add(menu);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setLocationRelativeTo(null);
        this.setLocation(300, 200);
        this.pack();
        this.setVisible(true);
        Image iconImage = Toolkit.getDefaultToolkit().getImage("src/Assets/python.png");
        this.setIconImage(iconImage);
        this.setMinimumSize(new Dimension(600, 400));
        menu.start(frameW, frameH);
    }

    public void switchToMenu() {
        this.add(menu);
        this.remove(game);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        menu.requestFocusForComponent(menu);
        menu.start(frameW, frameH);
    }
    
    public void switchToGame(int width, int height) {
        frameW = width;
        frameH = height;
        game = new Game(this, width, height);
        this.add(game);
        this.remove(menu);
        this.pack();
        this.setVisible(true);
        game.requestFocusForComponent(game);
        game.start();
        this.setResizable(false);
    }
}

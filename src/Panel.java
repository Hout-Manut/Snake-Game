package src;

import java.awt.*;
import javax.swing.*;

public class Panel extends JFrame {
    public int frameW = 1280;
    public int frameH = 720;

    private Menu menu;
    private Game game;

    Panel() {
        menu = new Menu(this);
        this.add(menu);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(300, 200);
        this.pack();
        this.setVisible(true);
        Image iconImage = Toolkit.getDefaultToolkit().getImage("src/Assets/python.png");
        this.setIconImage(iconImage);
        this.setMinimumSize(new Dimension(600, 400));
        menu.start();
    }

    public void switchToMenu() {
        this.add(menu);
        this.remove(game);
        this.pack();
        this.setVisible(true);
        menu.requestFocusForComponent(menu);
        System.out.println(frameW + " " + frameH);
        menu.start();
    }
    
    public void switchToGame() {
        game = new Game(this);
        this.add(game);
        this.remove(menu);
        this.pack();
        this.setVisible(true);
        game.requestFocusForComponent(game);
        game.start();
    }
}

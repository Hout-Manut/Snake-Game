package src;

import java.awt.*;
import javax.swing.*;

public class Panel extends JFrame {
    public int width = 1280;
    public int height = 720;

    private Menu menu;
    private Game game;
    private Dimension screen;
    private int x, y;

    Panel() {
        menu = new Menu(this);
        game = new Game(this);
        Toolkit tk = Toolkit.getDefaultToolkit();
        screen = tk.getScreenSize();
        x = (screen.width - width) / 2;
        y = (screen.height - height) / 2;
        this.add(menu);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(x, y);
        Image iconImage = Toolkit.getDefaultToolkit().getImage("src/Assets/python.png");
        this.setIconImage(iconImage);
        this.pack();
        this.setVisible(true);
        menu.start();
    }

    public void restart() {
        game.start();
    }

    public void switchToMenu() {
        this.add(menu);
        this.remove(game);
        this.pack();
        this.setVisible(true);
        menu.requestFocusForComponent(menu);
        menu.start();
    }
    
    public void switchToGame() {
        this.add(game);
        this.remove(menu);
        this.pack();
        this.setVisible(true);
        game.requestFocusForComponent(game);
        game.start();
    }
}

package src;

import java.awt.*;
import javax.swing.*;

public class Panel extends JFrame {

    private CardLayout cardLayout;
    private JPanel cards;
    private Game game;
    private Menu menu;

    Panel() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        menu = new Menu(this);
        game = new Game();
        cards.add(menu, "Menu");
        cards.add(game, "Game");

        this.add(cards);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLocation(300, 200);
        this.pack();
        this.setVisible(true);
        Image iconImage = Toolkit.getDefaultToolkit().getImage("src/Assets/python.png");
        this.setIconImage(iconImage);
    }

    public void changeToGame() {
        cardLayout.show(cards, "Game");
        game.requestFocusForComponent(game);
        game.start();
    }
}

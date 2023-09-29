package src;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.*;

public class Panel extends JFrame{

    
    Panel(){
        Game game = new Game();
        this.add(game);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        Image iconImage = Toolkit.getDefaultToolkit().getImage("src/Assets/python.png");
        this.setIconImage(iconImage);
        }
}

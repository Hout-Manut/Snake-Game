package src;

import javax.swing.*;

public class Panel extends JFrame{
    
    Panel(){
        Game game = new Game();
        this.add(game);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake Game");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        }
}

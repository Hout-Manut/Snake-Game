package Source;

import javax.swing.JFrame;

public class Screen extends JFrame{

	private static final long serialVersionUID = 1L;

	Screen() {
		Screen screen = new Screen();
		this.add(screen);
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
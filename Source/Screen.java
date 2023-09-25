package Source;

public class Screen {
    private int width, height;
    private char[][] screen;

    public Screen(int x, int y) {
        this.width = x;
        this.height = y;
        this.screen = new char[this.width][this.height];
    }

    public void StartScreen() {
        for (int i = 0; i < this.height; i++)
            for (int j = 0; j < this.width; j++)
                this.screen[i][j] = '.';
    }

    public void PrintScreen() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++)
                System.out.print(this.screen[i][j]);
            System.out.println();
        }
    }
}

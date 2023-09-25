package Source;

public class Main {
    public static void main(String[] args) {
        final int GAME_WIDTH = 50;
        final int GAME_HEIGHT = 30;
        final int SNAKE_START_X = GAME_WIDTH / 2;
        final int SNAKE_START_Y = GAME_HEIGHT / 2;

        Screen screen = new Screen(GAME_WIDTH, GAME_HEIGHT);
        screen.StartScreen();
        screen.PrintScreen();
    }
}
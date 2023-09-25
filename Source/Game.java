package Source;

public class Game {
    private int x, y;
    private char symbol;

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void SetX(int newX) {
        this.x = newX;
    }

    public void SetY(int newY) {
        this.y = newY;
    }

    public void SetSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }
}

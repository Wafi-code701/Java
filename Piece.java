package assignment2.ver2;
import java.awt.*;

public class Piece {
    public BLOCK type;
    public Color color;
    public int[][] shape; // rotations
    private int row, col, rotation;

    Piece(BLOCK type, Color color, int[][] shape) {
        this.type = type;
        this.color = color;
        this.shape = shape;
        this.row = 0;
        this.col = 0;//COLS / 2 - 2;
        this.rotation = 0;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getRotation() {
        return rotation;
    }
}
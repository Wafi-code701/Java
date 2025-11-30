package assignment2.ver2;
import java.awt.Color;
import java.util.*;
import java.util.function.*;

public class GameLogic{

    public static boolean secondColor = false;
    private Color[][] board = new Color[Board.ROWS][Board.COLS];
    private Random rng = new Random();
    private Map<BLOCK, Color> blockColorsMap;
    private Piece current;
    private Piece next;
    private int score = 0;
    private int linesCleared = 0;
    private boolean gameOver = false;


    public void changeColors() {
        blockColorsMap =null;
        if(!secondColor)
            blockColorsMap = Map.of(
                    BLOCK.I, new Color(0, 240, 240),
                    BLOCK.O, Color.YELLOW,
                    BLOCK.T, Color.MAGENTA,
                    BLOCK.S, new Color(0, 240, 0),
                    BLOCK.Z, Color.red,
                    BLOCK.J, new Color(0, 0, 240),
                    BLOCK.L, new Color(240, 120, 0)
            );

        else
            blockColorsMap = Map.of(BLOCK.I, Color.WHITE,
                BLOCK.O, Color.WHITE,
                BLOCK.T, Color.WHITE,
                BLOCK.S, Color.WHITE,
                BLOCK.Z, Color.WHITE,
                BLOCK.J, Color.WHITE,
                BLOCK.L, Color.WHITE);
        secondColor = !secondColor;
    }





    public GameLogic() {
        reset();
    }

    public void reset() {
        secondColor = false;
        changeColors();
        for (int r = 0; r < Board.ROWS; r++) Arrays.fill(board[r], null);
        score = 0;
        linesCleared = 0;
        gameOver = false;
        next = randomPiece();
        spawnPiece();
    }

    public Color[][] getBoard() { return board; }
    public Piece getCurrent() { return current; }
    public Piece getNext() { return next; }
    public int getScore() { return score; }
    public int getLinesCleared() { return linesCleared; }
    public boolean isGameOver() { return gameOver; }

    public void tick() {
        if (!moveDown()) {
            lockPiece();
            clearLines();
            spawnPiece();
        }
    }

    public void moveLeft() {
        if (!collides(current.getRow(), current.getCol() - 1, current.getRotation()))
            current.setCol(current.getCol() - 1);
    }
    public void moveRight() {
        if (!collides(current.getRow(), current.getCol() + 1, current.getRotation()))
            current.setCol(current.getCol() + 1);
    }

    public void rotate() {
        int newRot = (current.getRotation() + 1) % 4;
        if (!collides(current.getRow(), current.getCol(), newRot))
            current.setRotation(newRot);
    }
    public boolean moveDown() {
        if (!collides(current.getRow() + 1, current.getCol(), current.getRotation())) {
            current.setRow(current.getRow()+1);
            return true;
        }
        return false;
    }
    public void hardDrop() {
        while (moveDown())
            score += 2;
    }

    private void spawnPiece() {
        current = next;
        next = randomPiece();
        current.setRow(0);
        current.setCol(Board.COLS / 2 - BLOCK.COLS / 2);
        if (collides(current.getRow() + 1, current.getCol(), current.getRotation())) {
            gameOver = true;
        }
    }


    IntSupplier giveMeRandomNumber = ()-> rng.nextInt(BLOCK.values().length);

    private Piece randomPiece() {
        BLOCK t = BLOCK.getBlock(giveMeRandomNumber.getAsInt());
        return new Piece(t, blockColorsMap.get(t), BLOCK.SHAPES[t.ordinal()]);
    }

    private boolean collides(int newRow, int newCol, int newRot) {
        int[] mask = current.shape[newRot];
        for (int i = 0; i < 16; i++) {
            int mr = i / 4, mc = i % 4;
            if (mask[i] == 1) {
                int r = newRow + mr, c = newCol + mc;
                if (r < 0) continue;
                if (r >= Board.ROWS || c < 0 || c >= Board.COLS) return true;
                if (board[r][c] != null) return true;
            }
        }
        return false;
    }

    private void lockPiece() {
        int[] mask = current.shape[current.getRotation()];
        for (int i = 0; i < 16; i++) {
            int mr = i / 4, mc = i % 4;
            if (mask[i] == 1) {
                int r = current.getRow() + mr, c = current.getCol() + mc;
                if (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS) {
                    board[r][c] = current.color;
                }
            }
        }
    }

    private void clearLines() {
        for (int r = Board.ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < Board.COLS; c++) {
                if (board[r][c] == null) { full = false; break; }
            }
            if (full) {
                linesCleared++;
                score += 100;
                for (int rr = r; rr > 0; rr--) {
                    System.arraycopy(board[rr - 1], 0, board[rr], 0, Board.COLS);
                }
                Arrays.fill(board[0], null);
                r++;
            }
        }
    }
}
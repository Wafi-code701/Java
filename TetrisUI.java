package assignment2.ver2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class TetrisUI extends JPanel implements ActionListener, KeyListener {
    private GameLogic logic;
    private Timer timer;

    public TetrisUI() {
        setPreferredSize(new Dimension(420, 610));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        logic = new GameLogic();
        timer = new Timer(500, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!logic.isGameOver()) {
            logic.tick();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g,BLOCK.LENGTH);
        drawCurrentPiece(g,BLOCK.LENGTH);
        drawGameScore(g);

        if(logic.isGameOver()) drawGameOver(g);
    }

    private void drawBoard(Graphics g, int len) {
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                Color cell = logic.getBoard()[r][c];
                if (cell != null) {
                    g.setColor(cell);
                    g.fillRect((c * len)+Board.H_MARGIN, r * len+Board.V_MARGIN, len, len);
                }
                g.setColor(Color.DARK_GRAY);
                g.drawRoundRect((c * len)+Board.H_MARGIN, r * len+Board.V_MARGIN, len, len,5,5);
            }
        }
    }

    private void drawCurrentPiece(Graphics g,int len) {
        Piece p = logic.getCurrent();
        if (p != null) {
            int[] mask = p.shape[p.getRotation()];
            for (int i = 0; i < 16; i++) {
                int mr = i / 4, mc = i % 4;
                if (mask[i] == 1) {
                    int r = p.getRow() + mr, c = p.getCol() + mc;
                    if (r >= 0) {
                        g.setColor(p.color);
                        g.fillRoundRect(c * len+3, r * len+2, len, len,5,5);
                        g.setColor(Color.DARK_GRAY);
                        g.drawRoundRect(c * len+3, r * len+2, len, len,5,5);
                    }
                }
            }
        }
    }

    private void drawGameScore(Graphics g) {
        g.setColor(Color.GREEN);
        final short VSPACE = 15;
        short i=1;
        g.drawString("2413CCS-4", 320, VSPACE*i++);
        g.drawString("Assignment: 2", 320, VSPACE*i++);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + logic.getScore(), 320, VSPACE*i++);
        g.drawString("Lines: " + logic.getLinesCleared(), 320, VSPACE*i++);



    }

    private void drawGameOver(Graphics g) {
            Sound.runVoice1();
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("GAME OVER", 150, 300);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (logic.isGameOver()) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> logic.moveLeft();
            case KeyEvent.VK_RIGHT -> logic.moveRight();
            case KeyEvent.VK_UP -> logic.rotate();
            case KeyEvent.VK_DOWN -> logic.moveDown();
            case KeyEvent.VK_SPACE -> logic.hardDrop();
            case KeyEvent.VK_R -> logic.reset();
            case KeyEvent.VK_0 -> logic.changeColors();
        }
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}


}
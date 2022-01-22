package org.cis120.chess;


import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;


public abstract class ChessPiece {

    /* Current position of the object */
    private Position position;

    protected int moveNumber;
    private final String player;
    private final String pieceName;

    protected TreeSet<Position> validPositions;
    protected TreeSet<Position> validAttacks;

    private BufferedImage img;
    public final static int pieceSize = 80;

    public static boolean toEnPassant = false;
    public static int WHITE_POS_X = 4;
    public static int WHITE_POS_Y = 0;
    public static int BLACK_POS_X = 4;
    public static int BLACK_POS_Y = 7;

    public ChessPiece(int posX, int posY, String player, String pieceName) {
        position = new Position(posX, posY);

        this.player = player;
        this.pieceName = pieceName;
        this.moveNumber = 0;
        validPositions = new TreeSet<>();
        validAttacks = new TreeSet<>();

        try {
            if (img == null) {
                img = ImageIO.read(new File(pieceName));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public int getPosX() {
        return position.getPositionX();
    }

    public int getPosY() {
        return position.getPositionY();
    }

    public String getPlayer() {
        return player;
    }

    public String getPieceName() {
        return pieceName;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setPosX(int posX) {
        this.position.setPositionX(posX);
    }

    public void setPosY(int posY) {
        this.position.setPositionY(posY);
    }

    public TreeSet<Position> getValidPositions(ChessPiece[][] chB) {
        this.setValidPositions(chB);
        return new TreeSet<>(validPositions);
    }

    public TreeSet<Position> getValidAttacks(ChessPiece[][] chB) {
        this.setValidAttacks(chB);
        return new TreeSet<>(validAttacks);
    }


    protected void setValidPositions(ChessPiece[][] chB) {
        validPositions.clear();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (this.validPath(x, y, chB)) {
                    if (chB[x][y] == null) {
                        if (this.validMove(x, y)) {
                            Position p = new Position(x, y);
                            this.validPositions.add(p);
                        }
                    } else {
                        if (this.validCapture(chB[x][y])) {
                            Position p = new Position(x, y);
                            this.validPositions.add(p);
                        }
                    }
                }
            }
        }
    }


    protected void setValidAttacks(ChessPiece[][] chB) {
        validAttacks.clear();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (this.validPath(x, y, chB)) {
                    if (chB[x][y] == null) {
                        if (this.validMove(x, y)) {
                            Position p = new Position(x, y);
                            this.validAttacks.add(p);
                        }
                    } else {
                        if (this.validCapture(chB[x][y])) {
                            Position p = new Position(x, y);
                            this.validAttacks.add(p);
                        }
                    }
                }
            }
        }
    }

    public void move(int moveX, int moveY) {
        this.setPosX(moveX);
        this.setPosY(moveY);
        this.moveNumber++;
        if (toEnPassant) {
            toEnPassant = false;
        }
    }

    public boolean validCapture(ChessPiece piece) {
        if (piece == null) {
            return false;
        }
        if (this.getPlayer().equals(piece.getPlayer())) {
            return false;
        } else {
            return validMove(piece.getPosX(), piece.getPosY());
        }
    }

    public void draw(Graphics g) {
        int x = this.getPosX() * pieceSize;
        int y = Math.abs((this.getPosY() - 7) * pieceSize);
        g.drawImage(img, x, y, pieceSize, pieceSize, null);
    }

    public abstract boolean validMove(int moveX, int moveY);

    public abstract boolean validPath(int endX, int endY, ChessPiece[][] path);
}

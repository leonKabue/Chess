package org.cis120.chess;


public class Rook extends ChessPiece {
    public Rook(int posX, int posY, String player, String pieceName) {
        super(posX, posY, player, pieceName);
    }

    @Override
    public boolean validMove(int moveX, int moveY) {
        if (moveX > 7 || moveX < 0 || moveY > 7 || moveY < 0) {
            return false;
        } else {
            return (this.getPosX() == moveX && this.getPosY() != moveY) ||
                    (this.getPosY() == moveY && this.getPosX() != moveX);
        }
    }

    @Override
    public boolean validPath(int endX, int endY, ChessPiece[][] path) {
        if (this.validMove(endX, endY)) {

            boolean valid;

            if (this.getPosX() < endX) {
                for (int x = (this.getPosX() + 1); x < endX; x++) {
                    valid = (path[x][endY] == null);
                    if (!valid) {
                        return false;
                    }
                }
                return true;
            }
            if (this.getPosX() > endX) {
                for (int x = (endX + 1); x < this.getPosX(); x++) {
                    valid = (path[x][endY] == null);
                    if (!valid) {
                        return false;
                    }
                }
                return true;
            }
            if (this.getPosY() < endY) {
                for (int y = (this.getPosY() + 1); y < endY; y++) {
                    valid = path[endX][y] == null;
                    if (!valid) {
                        return false;
                    }
                }
                return true;
            }
            if (this.getPosY() > endY) {
                for (int y = (endY + 1); y < this.getPosY(); y++) {
                    valid = path[endX][y] == null;
                    if (!valid) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void move(int moveX, int moveY) {
        this.setPosX(moveX);
        this.setPosY(moveY);
        this.moveNumber++;
        if (toEnPassant) {
            toEnPassant = false;
        }
    }

}

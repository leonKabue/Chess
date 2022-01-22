package org.cis120.chess;


public class Knight extends ChessPiece {
    public Knight(int posX, int posY, String player, String pieceName) {
        super(posX, posY, player, pieceName);
    }

    @Override
    public boolean validMove(int moveX, int moveY) {
        if (moveX > 7 || moveX < 0 || moveY > 7 || moveY < 0) {
            return false;
        } else {
            return (this.getPosX() + 1 == moveX && this.getPosY() + 2 == moveY) ||
                    (this.getPosX() + 1 == moveX && this.getPosY() - 2 == moveY) ||
                    (this.getPosX() + 2 == moveX && this.getPosY() + 1 == moveY) ||
                    (this.getPosX() + 2 == moveX && this.getPosY() - 1 == moveY) ||
                    (this.getPosX() - 1 == moveX && this.getPosY() + 2 == moveY) ||
                    (this.getPosX() - 1 == moveX && this.getPosY() - 2 == moveY) ||
                    (this.getPosX() - 2 == moveX && this.getPosY() + 1 == moveY) ||
                    (this.getPosX() - 2 == moveX && this.getPosY() - 1 == moveY);
        }
    }

    @Override
    public boolean validPath(int endX, int endY, ChessPiece[][] path) {
        return validMove(endX, endY);
    }

    @Override
    public void setValidAttacks(ChessPiece[][] board) {
        validAttacks.clear();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (validMove(x, y)) {
                    validAttacks.add(new Position(x, y));
                }
            }
        }
    }
}

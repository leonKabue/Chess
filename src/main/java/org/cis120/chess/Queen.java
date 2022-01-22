package org.cis120.chess;

import java.awt.*;

public class Queen extends ChessPiece {
    public Queen(int posX, int posY, String player, String pieceName) {
        super(posX, posY, player, pieceName);
    }


    @Override
    public boolean validMove(int moveX, int moveY) {
        if (moveX > 7 || moveX < 0 || moveY > 7 || moveY < 0) {
            return false;
        } else {
            return this.validMoveAsKing(moveX, moveY) ||
                    this.validMoveAsRook(moveX, moveY) ||
                    this.validMoveAsBishop(moveX, moveY);
        }
    }

    @Override
    public boolean validPath(int endX, int endY, ChessPiece[][] path) {
        return this.validPathAsKing(endX, endY, path) ||
                this.validPathAsRook(endX, endY, path) ||
                this.validPathAsBishop(endX, endY, path);
    }

    private boolean validMoveAsKing(int moveX, int moveY) {
        return (this.getPosX() + 1 == moveX && moveY == this.getPosY()) ||
                (this.getPosX() - 1 == moveX && moveY == this.getPosY()) ||
                (this.getPosX() + 1 == moveX && this.getPosY() + 1 == moveY) ||
                (this.getPosX() - 1 == moveX && this.getPosY() - 1 == moveY) ||
                (this.getPosX() - 1 == moveX && this.getPosY() + 1 == moveY) ||
                (this.getPosX() + 1 == moveX && this.getPosY() - 1 == moveY) ||
                (this.getPosY() + 1 == moveY && moveX == this.getPosX()) ||
                (this.getPosY() - 1 == moveY && moveX == this.getPosX());
    }

    private boolean validPathAsKing(int endX, int endY, ChessPiece[][] path) {
        return this.validMoveAsKing(endX, endY);
    }

    private boolean validMoveAsRook(int moveX, int moveY) {
        return (this.getPosX() == moveX && this.getPosY() != moveY) ||
                (this.getPosY() == moveY && this.getPosX() != moveX);
    }

    private boolean validPathAsRook(int endX, int endY, ChessPiece[][] path) {
        if (this.validMoveAsRook(endX, endY)) {

            boolean valid;

            if (this.getPosX() < endX) {
                for (int x = (this.getPosX() + 1); x < endX; x++) {
                    valid = path[x][endY] == null;
                    if (!valid) {
                        return false;
                    }
                }
            }
            if (this.getPosX() > endX) {
                for (int x = (endX + 1); x < this.getPosX(); x++) {
                    valid = path[x][endY] == null;
                    if (!valid) {
                        return false;
                    }
                }
            }
            if (this.getPosY() < endY) {
                for (int y = (this.getPosY() + 1); y < endY; y++) {
                    valid = path[endX][y] == null;
                    if (!valid) {
                        return false;
                    }
                }
            }
            if (this.getPosY() > endY) {
                for (int y = (endY + 1); y < this.getPosY(); y++) {
                    valid = path[endX][y] == null;
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validMoveAsBishop(int moveX, int moveY) {
        return (Math.abs(this.getPosX() - moveX) ==
                Math.abs(this.getPosY() - moveY));
    }

    private boolean validPathAsBishop(int endX, int endY, ChessPiece[][] path) {
        if (this.validMoveAsBishop(endX, endY)) {

            int distance = Math.abs(this.getPosX() - endX);
            boolean valid;

            for (int pos = 1; pos < distance; pos++) {
                if (this.getPosX() < endX && this.getPosY() < endY) {
                    valid = path[this.getPosX() + pos][this.getPosY() + pos] ==
                            null;
                    if (!valid) {
                        return false;
                    }
                }
                if (this.getPosX() < endX && this.getPosY() > endY) {
                    valid = path[this.getPosX() + pos][this.getPosY() - pos] ==
                            null;
                    if (!valid) {
                        return false;
                    }
                }
                if (this.getPosX() > endX && this.getPosY() > endY) {
                    valid = path[this.getPosX() - pos][this.getPosY() - pos] ==
                            null;
                    if (!valid) {
                        return false;
                    }
                }
                if (this.getPosX() > endX && this.getPosY() < endY) {
                    valid = path[this.getPosX() - pos][this.getPosY() + pos] ==
                            null;
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

}

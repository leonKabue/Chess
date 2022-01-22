package org.cis120.chess;

import java.awt.*;

public class Bishop extends ChessPiece {
    public Bishop(int posX, int posY, String player, String pieceName) {
        super(posX, posY, player, pieceName);
    }

    @Override
    public boolean validMove(int moveX, int moveY) {
        if (moveX > 7 || moveX < 0 || moveY > 7 || moveY < 0 ||
                this.getPosX() == moveX || this.getPosY() == moveY) {
            return false;
        } else {
            return (Math.abs(this.getPosX() - moveX) ==
                    Math.abs(this.getPosY() - moveY));
        }
    }


    @Override
    public boolean validPath(int endX, int endY, ChessPiece[][] path) {
        if (this.validMove(endX, endY)) {

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

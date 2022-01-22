package org.cis120.chess;


public class King extends ChessPiece {
    public King(int posX, int posY, String player, String pieceName) {
        super(posX, posY, player, pieceName);
    }

    @Override
    public boolean validMove(int moveX, int moveY) {
        if (moveX > 7 || moveX < 0 || moveY > 7 || moveY < 0) {
            return false;
        }
        return (this.getPosX() + 1 == moveX && moveY == this.getPosY()) ||
                (this.getPosX() - 1 == moveX && moveY == this.getPosY()) ||
                (this.getPosX() + 1 == moveX && this.getPosY() + 1 == moveY) ||
                (this.getPosX() - 1 == moveX && this.getPosY() - 1 == moveY) ||
                (this.getPosX() - 1 == moveX && this.getPosY() + 1 == moveY) ||
                (this.getPosX() + 1 == moveX && this.getPosY() - 1 == moveY) ||
                (this.getPosY() + 1 == moveY && moveX == this.getPosX()) ||
                (this.getPosY() - 1 == moveY && moveX == this.getPosX());
    }

    @Override
    public void move(int moveX, int moveY) {
        this.setPosX(moveX);
        this.setPosY(moveY);
        this.moveNumber++;
        if (toEnPassant) {
            toEnPassant = false;
        }
        if (this.getPlayer().equals("WHITE")) {
            ChessPiece.WHITE_POS_X = moveX;
            ChessPiece.WHITE_POS_Y = moveY;
        } else {
            ChessPiece.BLACK_POS_X = moveX;
            ChessPiece.BLACK_POS_Y = moveY;
        }
    }


    @Override
    public boolean validPath(int endX, int endY, ChessPiece[][] path) {

        return (this.validMove(endX, endY));
    }


}

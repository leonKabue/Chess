package org.cis120.chess;


public class Pawn extends ChessPiece {

    public Pawn(int posX, int posY, String player, String pieceName) {
        super(posX, posY, player, pieceName);
    }

    @Override
    public boolean validMove(int moveX, int moveY) {
        if (moveX > 7 || moveX < 0 || moveY > 7 || moveY < 0 ||
                this.getPosX() != moveX) {
            return false;
        }
        if (this.getMoveNumber() == 0) {
            if (this.getPlayer().equals("WHITE")) {
                return (this.getPosY() + 1) == moveY ||
                        (this.getPosY() + 2) == moveY;
            } else {
                return (this.getPosY() - 1) == moveY ||
                        (this.getPosY() - 2) == moveY;
            }
        } else {
            if (this.getPlayer().equals("WHITE")) {
                return (this.getPosY() + 1) == moveY;
            } else {
                return (this.getPosY() - 1) == moveY;
            }
        }
    }

    @Override
    public boolean validCapture(ChessPiece piece) {
        if (piece == null) {
            return false;
        }
        if (this.getPlayer().equals(piece.getPlayer())) {
            return false;
        } else {
            int posX = piece.getPosX();
            int posY = piece.getPosY();
            if (this.getPlayer().equals("WHITE")) {
                return ((this.getPosX() + 1) == posX &&
                        (this.getPosY() + 1) == posY) ||
                        ((this.getPosX() - 1) == posX &&
                                ((this.getPosY() + 1) == posY));
            } else {
                return ((this.getPosX() + 1) == posX &&
                        (this.getPosY() - 1) == posY) ||
                        ((this.getPosX() - 1) == posX &&
                                ((this.getPosY() - 1) == posY));
            }
        }
    }


    @Override
    public boolean validPath(int endX, int endY, ChessPiece[][] path) {
        if (endX > 7 || endX < 0 || endY > 7 || endY < 0 ||
                this.getPosX() != endX) {
            return false;
        }
        if (this.getMoveNumber() == 0) {
            if (this.getPlayer().equals("WHITE")) {
                return ((this.getPosY() + 1) == endY &&
                        path[endX][endY] == null) ||
                        ((this.getPosY() + 2) == endY &&
                                path[endX][(this.getPosY() + 1)] == null &&
                                path[endX][endY] == null);
            } else {
                return ((this.getPosY() - 1) == endY &&
                        path[endX][endY] == null) ||
                        ((this.getPosY() - 2) == endY &&
                                path[endX][endY + 1] == null &&
                                path[endX][endY] == null);
            }
        } else {
            if (this.getPlayer().equals("WHITE")) {
                return ((this.getPosY() + 1) == endY && path[endX][endY] == null);
            } else {
                return ((this.getPosY() - 1) == endY && path[endX][endY] == null);
            }
        }
    }

    @Override
    public void setValidPositions(ChessPiece[][] board) {
        this.validPositions.clear();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (this.validPath(x, y, board)) {
                    if (board[x][y] == null) {
                        if (this.validMove(x, y)) {
                            Position p = new Position(x, y);
                            this.validPositions.add(p);
                        }
                    }
                } else {
                    if (this.validCapture(board[x][y])) {
                        Position p = new Position(x, y);
                        this.validPositions.add(p);
                    }
                }
            }
        }
    }

    @Override
    public void setValidAttacks(ChessPiece[][] board) {
        validAttacks.clear();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                if (this.getPlayer().equals("WHITE")) {
                    if ((((this.getPosX() + 1) == x) &&
                            (this.getPosY() + 1) == y) ||
                            ((this.getPosX() - 1) == x &&
                                    ((this.getPosY() + 1) == y))) {
                        validAttacks.add(new Position(x, y));
                    }
                } else if ((((this.getPosX() + 1) == x) &&
                        (this.getPosY() - 1) == y) ||
                        ((this.getPosX() - 1) == x &&
                                ((this.getPosY() - 1) == y))) {
                    validAttacks.add(new Position(x, y));
                }
            }
        }
    }
}

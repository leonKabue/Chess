package org.cis120.chess;


import java.awt.*;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Chess class stores the model of the game
 */

public class Chess {
    private ChessPiece[][] board;
    private boolean whiteTurn;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private boolean whiteWins;
    private boolean blackWins;
    private boolean draw;
    private boolean gameOver;

    private TreeSet<Position> clickedPiecePositions;
    private TreeMap<Position, TreeSet<Position>> boardValidMoves;


    /**
     * strings to store the players and piece filename
     */
    private final String black = "BLACK";
    private final String white = "WHITE";
    private final String blackKing = "files/BlackKing.png";
    private final String whiteKing = "files/WhiteKing.png";
    private final String blackQueen = "files/BlackQueen.png";
    private final String whiteQueen = "files/WhiteQueen.png";
    private final String blackRook = "files/BlackRook.png";
    private final String whiteRook = "files/WhiteRook.png";
    private final String blackBishop = "files/BlackBishop.png";
    private final String whiteBishop = "files/WhiteBishop.png";
    private final String blackKnight = "files/BlackKnight.png";
    private final String whiteKnight = "files/WhiteKnight.png";
    private final String blackPawn = "files/BlackPawn.png";
    private final String whitePawn = "files/WhitePawn.png";

    public Chess() {
        reset();
    }

    /**
     * resets the game state to the starting position
     * also places the pieces in their initial positions
     */
    public void reset() {
        board = new ChessPiece[8][8];
        gameOver = false;
        whiteInCheck = false;
        blackInCheck = false;
        whiteWins = false;
        blackWins = false;
        draw = false;
        whiteTurn = true;

        clickedPiecePositions = new TreeSet<>();
        boardValidMoves = new TreeMap<>();

        board[0][0] = new Rook(0, 0, white, whiteRook);
        board[1][0] = new Knight(1, 0, white, whiteKnight);
        board[2][0] = new Bishop(2, 0, white, whiteBishop);
        board[3][0] = new Queen(3, 0, white, whiteQueen);
        board[4][0] = new King(4, 0, white, whiteKing);
        board[5][0] = new Bishop(5, 0, white, whiteBishop);
        board[6][0] = new Knight(6, 0, white, whiteKnight);
        board[7][0] = new Rook(7, 0, white, whiteRook);

        board[0][7] = new Rook(0, 7, black, blackRook);
        board[1][7] = new Knight(1, 7, black, blackKnight);
        board[2][7] = new Bishop(2, 7, black, blackBishop);
        board[3][7] = new Queen(3, 7, black, blackQueen);
        board[4][7] = new King(4, 7, black, blackKing);
        board[5][7] = new Bishop(5, 7, black, blackBishop);
        board[6][7] = new Knight(6, 7, black, blackKnight);
        board[7][7] = new Rook(7, 7, black, blackRook);

        for (int pos = 0; pos <= 7; pos++) {
            board[pos][1] = new Pawn(pos, 1, white, whitePawn);
            board[pos][6] = new Pawn(pos, 6, black, blackPawn);
        }

        ChessPiece.WHITE_POS_X = 4;
        ChessPiece.WHITE_POS_Y = 0;
        ChessPiece.BLACK_POS_Y = 7;
        ChessPiece.BLACK_POS_X = 4;

        this.updateValidMoves();

    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isBlackTurn() {
        return !whiteTurn;
    }

    public boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public boolean isBlackInCheck() {
        return blackInCheck;
    }

    public boolean isWhiteWins() {
        return whiteWins;
    }

    public boolean isBlackWins() {
        return blackWins;
    }

    public boolean isGameOver() {
        return (gameOver || checkMate() || staleMate());
    }

    public boolean isDraw() {
        return draw;
    }

    public Position whiteKingPos() {
        return new Position(ChessPiece.WHITE_POS_X, ChessPiece.WHITE_POS_Y);
    }

    public Position blackKingPos() {
        return new Position(ChessPiece.BLACK_POS_X, ChessPiece.BLACK_POS_Y);
    }

    /**
     * playMove makes a move for a piece by taking in the coordinates where
     * the piece to be moved to, doing the necessary checks to ensure the
     * move is legal and then changing the state of the game to reflect this
     *
     * @param pieceX x-coordinate of piece to be played
     * @param pieceY y-coordinate of piece to be played
     * @param x      x-coordinated to move the piece to
     * @param y      y-coordinate to move the piece to
     * @return whether the move was successful
     */
    public boolean playMove(int pieceX, int pieceY, int x, int y) {
        ChessPiece piece = board[pieceX][pieceY];
        if (piece == null ||
                ((piece.getPlayer().equals(white) && !whiteTurn) ||
                        (piece.getPlayer().equals(black) && whiteTurn)) ||
                (board[x][y] != null &&
                        (board[x][y].getPieceName().equals(whiteKing) ||
                                board[x][y].getPieceName().equals(blackKing))) ||
                gameOver) {
            return false;
        }
        if (playCastle(pieceX, pieceY, x, y)) {
            this.updateValidMoves();
            inCheck(black);
            inCheck(white);
            return true;
        }
        if (playPromote(pieceX, pieceY, x, y)) {
            this.updateValidMoves();
            inCheck(black);
            inCheck(white);
            return true;
        }
        if (ChessPiece.toEnPassant) {
            if ((canEnPassant(pieceX, pieceY, x, (y - 1)) &&
                    piece.getPlayer().equals(white)) ||
                    (canEnPassant(pieceX, pieceY, x, (y + 1)) &&
                            piece.getPlayer().equals(black))) {
                piece.move(x, y);
                whiteTurn = !whiteTurn;
                board[x][y] = piece;

                if (piece.getPlayer().equals(black)) {
                    board[x][y + 1] = null;
                }
                if (piece.getPlayer().equals(white)) {
                    board[x][y - 1] = null;
                }
                this.updateValidMoves();

                inCheck(black);
                inCheck(white);
                return true;
            }
        }
        Position pos1 = new Position(pieceX, pieceY);
        Position pos2 = new Position(x, y);

        if (boardValidMoves.get(pos1).contains(pos2)) {

            piece.move(x, y);
            board[x][y] = piece;
            board[pieceX][pieceY] = null;

            this.updateValidMoves();
            inCheck(black);
            inCheck(white);

            ChessPiece.toEnPassant = Math.abs(pieceY - y) == 2 && (
                    (x != 7 && canEnPassant(x, y, x + 1, y)) ||
                            (x == 7 && canEnPassant(x, y, x - 1, y)) ||
                            (x != 0 && canEnPassant(x, y, x - 1, y)) ||
                            (x == 0 && canEnPassant(x, y, x + 1, y))
            );

            this.updateEnPassant(x, y);
            whiteTurn = !whiteTurn;
            return true;
        } else {
            return false;
        }
    }

    private boolean playCastle(int pieceX, int pieceY, int x, int y) {
        ChessPiece king = board[pieceX][pieceY];
        if (king != null && king.getMoveNumber() == 0 &&
                (king.getPieceName().equals(blackKing) ||
                        king.getPieceName().equals(whiteKing)) &&
                (Math.abs(pieceX - x) == 2) &&
                (pieceY == y) && !whiteInCheck && !blackInCheck) {
            if (king.getPieceName().equals(whiteKing)) {
                if (x == 6 && board[5][0] == null && board[6][0] == null &&
                        board[7][0].getMoveNumber() == 0 &&
                        !(squareAttacked(5, 0, black)) &&
                        !(squareAttacked(6, 0, black))) {
                    king.move(x, y);
                    ChessPiece rook = board[7][0];
                    rook.move(5, 0);
                    board[x][y] = king;
                    board[5][0] = rook;
                    board[7][0] = null;
                    board[pieceX][pieceY] = null;

                    this.updateValidMoves();
                    inCheck(black);
                    inCheck(white);

                    whiteTurn = !whiteTurn;
                    return true;
                }
                if (x == 2 && board[1][0] == null && board[2][0] == null &&
                        board[3][0] == null && board[0][0].getMoveNumber() == 0
                        &&
                        !(squareAttacked(1, 0, black)) &&
                        !(squareAttacked(2, 0, black)) &&
                        !(squareAttacked(3, 0, black))) {
                    king.move(x, y);
                    ChessPiece rook = board[0][0];
                    rook.move(3, 0);
                    board[x][y] = king;
                    board[3][0] = rook;
                    board[0][0] = null;
                    board[pieceX][pieceY] = null;

                    this.updateValidMoves();
                    inCheck(black);
                    inCheck(white);

                    whiteTurn = !whiteTurn;
                    return true;
                }
            } else if (x == 6 && board[5][7] == null && board[6][7] == null &&
                    board[7][7].getMoveNumber() == 0 &&
                    !(squareAttacked(5, 7, white)) &&
                    !(squareAttacked(6, 7, white))) {
                king.move(x, y);
                ChessPiece rook = board[7][7];
                rook.move(5, 7);
                board[x][y] = king;
                board[5][7] = rook;
                board[7][7] = null;
                board[pieceX][pieceY] = null;

                this.updateValidMoves();
                inCheck(black);
                inCheck(white);

                whiteTurn = !whiteTurn;
                return true;
            }
            if (x == 2 && board[1][7] == null && board[2][7] == null &&
                    board[3][7] == null && board[0][7].getMoveNumber() == 0 &&
                    !(squareAttacked(1, 7, white)) &&
                    !(squareAttacked(2, 7, white)) &&
                    !(squareAttacked(3, 7, white))) {
                king.move(x, y);
                ChessPiece rook = board[0][7];
                rook.move(3, 7);
                board[x][y] = king;
                board[3][7] = rook;
                board[0][7] = null;
                board[pieceX][pieceY] = null;

                this.updateValidMoves();
                inCheck(black);
                inCheck(white);

                whiteTurn = !whiteTurn;
                return true;
            }

        }
        return false;
    }


    private boolean squareAttacked(int posX, int posY, String Player) {
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {

                ChessPiece piece = board[x][y];

                if ((piece != null && (piece.getPlayer().equals(Player)))) {
                    if (piece.getValidAttacks(board).contains(
                            new Position(posX, posY))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean canEnPassant(int pieceX, int pieceY, int x, int y) {
        if (board[x][y] == null || board[pieceX][pieceY] == null) {
            return false;
        }
        if ((board[pieceX][pieceY].getPieceName().equals(blackPawn) ||
                board[pieceX][pieceY].getPieceName().equals(whitePawn)) &&
                (board[x][y].getPieceName().equals(whitePawn) ||
                        board[x][y].getPieceName().equals(blackPawn))) {
            ChessPiece piece1 = board[pieceX][pieceY];
            ChessPiece piece2 = board[x][y];
            return !(piece1.getPlayer().equals(piece2.getPlayer())) &&
                    pieceY == y &&
                    (pieceX - 1 == x || pieceX + 1 == x);
        } else {
            return false;
        }
    }

    private void updateEnPassant(int x, int y) {
        if (ChessPiece.toEnPassant) {
            TreeSet<Position> positions = new TreeSet<>();
            ChessPiece piece = board[x][y];
            if (board[x + 1][y] != null &&
                    board[x + 1][y].getPieceName().equals(blackPawn) &&
                    piece.getPieceName().equals(whitePawn)) {
                positions.addAll(boardValidMoves.get(new Position((x + 1), y)));
                positions.add(new Position(x, (y - 1)));
                boardValidMoves.put(new Position((x + 1), y),
                        new TreeSet<>(positions));
                positions.clear();
            }
            if (board[x - 1][y] != null &&
                    board[x - 1][y].getPieceName().equals(blackPawn) &&
                    piece.getPieceName().equals(whitePawn)) {
                positions.addAll(boardValidMoves.get(new Position((x - 1), y)));
                positions.add(new Position(x, (y - 1)));
                boardValidMoves.put(new Position((x - 1), y),
                        new TreeSet<>(positions));
                positions.clear();
            }
            if (board[x + 1][y] != null &&
                    board[x + 1][y].getPieceName().equals(whitePawn) &&
                    piece.getPieceName().equals(blackPawn)) {
                positions.addAll(boardValidMoves.get(new Position((x + 1), y)));
                positions.add(new Position(x, (y + 1)));
                boardValidMoves.put(new Position((x + 1), y),
                        new TreeSet<>(positions));
                positions.clear();
            }
            if (board[x - 1][y] != null &&
                    board[x - 1][y].getPieceName().equals(whitePawn) &&
                    piece.getPieceName().equals(blackPawn)) {
                positions.addAll(boardValidMoves.get(new Position((x - 1), y)));
                positions.add(new Position(x, (y + 1)));
                boardValidMoves.put(new Position((x - 1), y),
                        new TreeSet<>(positions));
                positions.clear();
            }
        }
    }

    private boolean checkMate() {
        if (inCheck(white)) {
            for (int x = 0; x <= 7; x++) {
                for (int y = 0; y <= 7; y++) {
                    if (board[x][y] != null &&
                            board[x][y].getPlayer().equals(white)) {
                        Position pos = new Position(x, y);
                        if (!(boardValidMoves.get(pos).isEmpty())) {
                            return false;
                        }
                    }
                }
            }
            blackWins = true;
            gameOver = true;
            return true;
        } else if (inCheck(black)) {
            for (int x = 0; x <= 7; x++) {
                for (int y = 0; y <= 7; y++) {
                    if (board[x][y] != null &&
                            board[x][y].getPlayer().equals(black)) {
                        Position pos = new Position(x, y);
                        if (!(boardValidMoves.get(pos).isEmpty())) {
                            return false;
                        }
                    }
                }
            }
            whiteWins = true;
            gameOver = true;
            return true;
        } else {
            return false;
        }
    }


    private boolean staleMate() {
        if (!whiteInCheck && whiteTurn) {
            for (int x = 0; x <= 7; x++) {
                for (int y = 0; y <= 7; y++) {
                    if (board[x][y] != null &&
                            board[x][y].getPlayer().equals(white)) {
                        Position pos = new Position(x, y);
                        if (!(boardValidMoves.get(pos).isEmpty())) {
                            return false;
                        }
                    }
                }
            }
            draw = true;
            gameOver = true;
            return true;
        } else if (!blackInCheck && !whiteTurn) {
            for (int x = 0; x <= 7; x++) {
                for (int y = 0; y <= 7; y++) {
                    if (board[x][y] != null &&
                            board[x][y].getPlayer().equals(black)) {
                        Position pos = new Position(x, y);
                        if (!(boardValidMoves.get(pos).isEmpty())) {
                            return false;
                        }
                    }
                }
            }
            draw = true;
            gameOver = true;
            return true;
        } else {
            return false;
        }
    }

    private boolean inCheck(String Player) {

        int blackX = ChessPiece.BLACK_POS_X;
        int whiteX = ChessPiece.WHITE_POS_X;
        int blackY = ChessPiece.BLACK_POS_Y;
        int whiteY = ChessPiece.WHITE_POS_Y;

        if (Player.equals(black) && squareAttacked(blackX, blackY, white)) {
            blackInCheck = true;
            return true;
        } else if (Player.equals(white) && squareAttacked(whiteX, whiteY, black)) {
            whiteInCheck = true;
            return true;
        } else if (Player.equals(white)) {
            whiteInCheck = false;
            return false;
        } else {
            blackInCheck = false;
            return false;
        }
    }

    private boolean playPromote(int pieceX, int pieceY, int x, int y) {
        ChessPiece pawn = board[pieceX][pieceY];
        if (!(pawn == null || (!pawn.getPieceName().equals(blackPawn) &&
                !pawn.getPieceName().equals(whitePawn)))) {
            if (pawn.getPieceName().equals(whitePawn) && pieceY == 6) {

                Position pos1 = new Position(pieceX, pieceY);
                Position pos2 = new Position(x, y);

                if (boardValidMoves.get(pos1).contains(pos2)) {
                    board[x][y] = new Queen(x, y, white, whiteQueen);
                    board[pieceX][pieceY] = null;
                    whiteTurn = !whiteTurn;
                    return true;
                }
            }
            if (pawn.getPieceName().equals(blackPawn) && pieceY == 1) {

                Position pos1 = new Position(pieceX, pieceY);
                Position pos2 = new Position(x, y);

                if (boardValidMoves.get(pos1).contains(pos2)) {
                    board[x][y] = new Queen(x, y, black, blackQueen);
                    board[pieceX][pieceY] = null;
                    whiteTurn = !whiteTurn;
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board[x][y];
                if (piece != null) {
                    piece.draw(g);
                }
            }
        }
        for (Position pos : clickedPiecePositions) {
            int posX = pos.getPositionX();
            int posY = pos.getPositionY();
            int x = Math.abs(posX * ChessPiece.pieceSize);
            int y = Math.abs((posY - 7) * ChessPiece.pieceSize);
            g.setColor(new Color(159, 173, 224, 150));
            g.fillRect(x, y, ChessPiece.pieceSize, ChessPiece.pieceSize);
            g.setColor(Color.black);
        }
    }

    public void highlight(int x, int y) {
        clickedPiecePositions.clear();
        clickedPiecePositions.addAll(boardValidMoves.get(new Position(x, y)));
        clickedPiecePositions.add(new Position(x, y));
    }

    public void unHighlight() {
        clickedPiecePositions.clear();
    }

    private void setValidMoves(int x, int y) {
        ChessPiece piece = board[x][y];
        TreeSet<Position> validMoves = new TreeSet<>();
        if (piece != null) {
            for (Position pos : piece.getValidPositions(board)) {

                int posX = pos.getPositionX();
                int posY = pos.getPositionY();
                String player = piece.getPlayer();
                String pieceName = piece.getPieceName();

                ChessPiece temp = board[posX][posY];

                board[posX][posY] = piece;
                board[x][y] = null;

                if (!(pieceName.equals(whiteKing)) &&
                        !(pieceName.equals(blackKing))) {
                    if (player.equals(white)) {
                        if (!(squareAttacked(ChessPiece.WHITE_POS_X,
                                ChessPiece.WHITE_POS_Y, black))) {
                            validMoves.add(new Position(posX, posY));
                        }
                    } else {
                        if (!(squareAttacked(ChessPiece.BLACK_POS_X,
                                ChessPiece.BLACK_POS_Y, white))) {
                            validMoves.add(new Position(posX, posY));
                        }
                    }
                } else {
                    if (pieceName.equals(whiteKing)) {
                        if (!(squareAttacked(posX, posY, black))) {
                            validMoves.add(new Position(posX, posY));
                        }
                    }
                    if (pieceName.equals(blackKing)) {
                        if (!(squareAttacked(posX, posY, white))) {
                            validMoves.add(new Position(posX, posY));
                        }
                    }
                }
                board[posX][posY] = temp;
                board[x][y] = piece;
            }
        }
        boardValidMoves.put(new Position(x, y), validMoves);
    }

    public void updateValidMoves() {
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                this.setValidMoves(x, y);
            }
        }
    }

    public boolean isEmptySquare(int x, int y) {
        return (x < 0 || x > 7 || y < 0 || y > 7 || board[x][y] == null);
    }

    public boolean isWhitePiece(int x, int y) {
        if (!isEmptySquare(x, y)) {
            return board[x][y].getPlayer().equals(white);
        } else return false;
    }

    public boolean isBlackPiece(int x, int y) {
        if (!isEmptySquare(x, y)) {
            return board[x][y].getPlayer().equals(black);
        } else return false;
    }

    private void printGameState() {
        StringBuilder print;
        for (int y = 7; y >= 0; y--) {
            print = new StringBuilder();
            for (int x = 0; x < 8; x++) {
                if (board[x][y] == null) {
                    print.append("files/EMPTY_SQUARE");
                } else {
                    print.append(board[x][y].getPieceName());
                }
                print.append(" ");
                print.append(x);
                print.append(", ");
                print.append(y);
                if (x != 7) {
                    print.append(" | ");
                }
            }
            System.out.println(print);
        }
    }

    public static void main(String[] args) {
        Chess ch = new Chess();


        System.out.println("\n ---------TEST CHECKMATE-------\n");
        ch.reset();

        if (!(ch.playMove(4, 1, 4, 3))) {
            System.out.println("Unsuccessful pawn push 1");
        }
        if (!(ch.playMove(5, 6, 5, 4))) {
            System.out.println("Unsuccessful pawn push 2");
        }
        if (!(ch.playMove(4, 3, 5, 4))) {
            System.out.println("Unsuccessful pawn capture 3");
        }
        if (!(ch.playMove(6, 6, 6, 4))) {
            System.out.println("Unsuccessful pawn push 4");
        }
        if (!(ch.playMove(3, 0, 7, 4))) {
            System.out.println("Unsuccessful checkmate 5");
        }
        ch.printGameState();

        if (ch.checkMate()) {
            System.out.println("Checkmate recognized 6");
        }
        if (!(ch.playMove(6, 7, 5, 5))) {
            System.out.println("Unsuccessful knight move while in mate 7");
        }

        System.out.println("\n ---------TEST CASTLING-------\n");
        ch.reset();

        if (!(ch.playMove(4, 1, 4, 3))) {
            System.out.println("Unsuccessful pawn push 8");
        }
        if (!(ch.playMove(5, 6, 5, 4))) {
            System.out.println("Unsuccessful pawn push 9");
        }
        if (!(ch.playMove(5, 0, 3, 2))) {
            System.out.println("Unsuccessful bishop move 10");
        }
        if (!(ch.playMove(5, 4, 4, 3))) {
            System.out.println("Unsuccessful pawn capture 11");
        }
        if (!(ch.playMove(6, 0, 5, 2))) {
            System.out.println("Unsuccessful knight move 12");
        }
        if (!(ch.playMove(6, 7, 5, 5))) {
            System.out.println("Unsuccessful knight move 13");
        }
        if (!(ch.playMove(4, 0, 6, 0))) {
            System.out.println("Unsuccessful castle 14");
        }
        if (ch.checkMate()) {
            System.out.println("Error: checkmate not occurred 15");
        }
        if (ch.staleMate()) {
            System.out.println("Error: stalemate not occurred 16");
        }
        ch.printGameState();

        System.out.println("\n ---------TEST HIGHLIGHT-------\n");
        ch.reset();


        if (!(ch.playMove(0, 1, 0, 2))) {
            System.out.println("Unsuccessful PUSH");
        }
        if (!(ch.playMove(6, 7, 5, 5))) {
            System.out.println("Unsuccessful KNIGHT MOVE");
        }
        ch.inCheck("WHITE");
        ch.inCheck("BLACK");
    }

}

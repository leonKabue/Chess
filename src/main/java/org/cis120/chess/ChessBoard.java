package org.cis120.chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChessBoard extends JPanel {

    private Chess chess;
    private JLabel status;

    private int startPositionX;
    private int startPositionY;
    private int endPositionX;
    private int endPositionY;
    private int gameMode;
    private final int playMode = 1;
    private final int moveMode = 2;
    private final int checkMode = 3;
    private final int endMode = 4;
    private boolean moved;
    private boolean toShowMoves;


    private Position toHighlight;
    public static final int BOARD_WIDTH = 640;
    public static final int BOARD_HEIGHT = 640;

    private static BufferedImage img;

    public ChessBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        chess = new Chess();
        status = statusInit;
        this.reset();

        try {
            if (img == null) {
                img = ImageIO.read(new File("files/ChessBoard1.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                int posX = p.x / 80;
                int posY = Math.abs((p.y / 80) - 7);

                switch (gameMode) {
                    case playMode:
                        if ((chess.isWhiteTurn() && chess.isWhitePiece(posX, posY)) ||
                                (chess.isBlackTurn() && chess.isBlackPiece(posX, posY))) {
                            startPositionX = posX;
                            startPositionY = posY;
                            if (toShowMoves) {
                                chess.highlight(posX, posY);
                            }
                            gameMode = moveMode;
                        }
                        break;
                    case moveMode:
                        if (startPositionX == posX && startPositionY == posY) {
                            gameMode = playMode;
                            chess.unHighlight();
                            break;
                        }
                        endPositionY = posY;
                        endPositionX = posX;

                        moved = chess.playMove(startPositionX, startPositionY,
                                endPositionX, endPositionY);

                        if (moved && (chess.isWhiteInCheck() || chess.isBlackInCheck())) {
                            gameMode = checkMode;
                            chess.unHighlight();
                            break;
                        } else if (moved) {
                            toHighlight = null;
                        }
                        gameMode = playMode;
                        chess.unHighlight();
                        break;
                    default:
                        break;
                }

                updateStatus();
                repaint();
            }
        });
        // Makes sure this component has mouse focus
        requestFocusInWindow();
    }

    public void reset() {
        chess.reset();

        toHighlight = null;
        startPositionX = 8;
        startPositionY = 8;
        endPositionX = 8;
        endPositionY = 8;
        gameMode = 1;
        moved = false;
        toShowMoves = true;

        status.setText("White to Play");
        repaint();

        requestFocusInWindow();
    }


    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        switch (gameMode) {
            case playMode:
                if (chess.isGameOver()) {
                    gameMode = endMode;
                }
            case moveMode:
                if (chess.isWhiteTurn()) {
                    status.setText("White to Move");
                } else {
                    status.setText("Black to Move");
                }
                break;
            case checkMode:
                if (chess.isWhiteInCheck()) {
                    this.setToHighlight(chess.whiteKingPos());
                } else {
                    this.setToHighlight(chess.blackKingPos());
                }
                if (chess.isGameOver()) {
                    gameMode = endMode;
                } else {
                    if (chess.isWhiteTurn()) {
                        status.setText("White to Move From Check");
                    } else {
                        status.setText("Black to Move From Check");
                    }
                    gameMode = playMode;
                    break;
                }
            case endMode:
                if (chess.isWhiteWins()) {
                    status.setText("WHITE WON !!! GG");
                } else if (chess.isBlackWins()) {
                    status.setText("BLACK WON !!! GG");
                } else if (chess.isDraw()) {
                    status.setText("DRAW !!! BETTER LUCK NEXT TIME");
                } else status.setText("SOMETHING IS WRONG !!!");
                break;
        }
    }

    public void showMoves() {
        this.toShowMoves = !toShowMoves;
    }

    private void setToHighlight(Position pos) {
        int posX = Math.abs(pos.getPositionX() * ChessPiece.pieceSize);
        int posY = Math.abs((pos.getPositionY() - 7) * ChessPiece.pieceSize);
        if (!(pos.getPositionX() > 7 || pos.getPositionX() < 0 ||
                pos.getPositionY() > 7 || pos.getPositionY() < 0)) {
            toHighlight = new Position(posX, posY);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, 640, 640, null);
        chess.draw(g);

        if (toHighlight != null) {
            g.setColor(new Color(201, 77, 83, 120));
            g.fillRect(toHighlight.getPositionX(), toHighlight.getPositionY(),
                    ChessPiece.pieceSize, ChessPiece.pieceSize);
            g.setColor(Color.black);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}

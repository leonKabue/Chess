package org.cis120.chess;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RunChess implements Runnable {
    public void run() {

        final JFrame frame = new JFrame("CHESS");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        //Chess board
        final ChessBoard board = new ChessBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Restart");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);
        String Message = "WELCOME TO CHESS:\n\n" +
                "Chess is a two-player board game using a chessboard and \n" +
                "sixteen pieces of six types for each player.\n" +
                "Each type of piece moves in a distinct way. \n" +
                "The object of the game is to checkmate (threaten with " +
                "inescapable capture)\n" +
                "the opponent's king. Games do not necessarily end in " +
                "checkmate; \n" +
                "A game can also end in a draw in several ways.\n\n" +
                "CLICK OK TO SEE HOW TO MOVE PIECES";

        JOptionPane.showMessageDialog(null, Message);

        Message = "HOW TO MOVE :\n\nThe king moves exactly one square" +
                "horizontally," +
                " vertically, or diagonally.\n" + "A special move with the " +
                "king known as castling is allowed only once per player, per " +
                "game " +
                "(see below).\n" +
                "A rook moves any number of vacant squares horizontally or " +
                "vertically." +
                "It also is moved when castling.\n" +
                "A bishop moves any number of vacant squares diagonally.\n" +
                "The queen moves any number of vacant squares horizontally, " +
                "vertically, " +
                "or diagonally.\n" +
                "A knight moves to one of the nearest squares not on the same " +
                "rank, " +
                "file, or diagonal.\n(This can be thought of as moving two " +
                "squares " +
                "horizontally" +
                " then one square vertically,\nor moving one square horizontally" +
                " then two " +
                "squares vertically—i.e. in an \"L\" pattern.)\nThe knight is not " +
                "blocked by other pieces; it jumps to the new location.\n" +
                "Pawns have the most complex rules of movement:\n" +
                "A pawn moves straight forward one square, if that square is " +
                "vacant.\n" +
                "If it has not yet moved, a pawn also has the option of moving " +
                "two " +
                "squares straight forward, provided both squares are vacant.\n" +
                "Pawns cannot move backwards.\n" +
                "A pawn, unlike other pieces, captures differently from how it " +
                "moves.\n" +
                "A pawn can capture an enemy piece on either of the two squares " +
                "diagonally " +
                "in front of the pawn.\nIt cannot move to those squares when vacant " +
                "except " +
                "when capturing en passant." +
                "\n\nENJOY THE GAME !!!";

        JOptionPane.showMessageDialog(null, Message);

        final JCheckBox showMoves = new JCheckBox("Highlight Moves", true);
        showMoves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.showMoves();
            }
        });
        control_panel.add(showMoves);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

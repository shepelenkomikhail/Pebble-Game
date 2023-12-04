import javax.swing.*;
public class Game {
    private final Player[] players;         //Array to hold the players
    private int currentPlayerIndex = 0;     //Index to keep track of the current player
    private final int height;               //Dimensions of the game board
    private final int width;
    public Board board;                        //The game board and the total number of turns allowed
    public int totalTurnsLeft;

    public Game(int width, int height) {           //Constructor to initialize the game with specified width and height
        this.width = width;
        this.height = height;
        totalTurnsLeft = width * 5;
        board = new Board(width);
        players = new Player[]{
                new Player("Player 1", "W"),
                new Player("Player 2", "B")
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }         //Getters and setters

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public void minusTurn() {
        totalTurnsLeft--;
    }            //Method to decrement the total number of turns left

    public Player getOpponentPlayer() {return players[(currentPlayerIndex + 1) % 2];} //Getter method to retrieve the opponent player

    public void switchPlayerTurn(   ) {currentPlayerIndex = (currentPlayerIndex + 1) % 2;}// Method to switch the turn to the next player

    public boolean determineWinner() {         //Method to determine the winner or if the game is a draw
        int whiteCount = 0;
        int blackCount = 0;
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (board.getBoard()[i][j].getText().equals("W")) {
                    whiteCount++;
                } else if (board.getBoard()[i][j].getText().equals("B")) {
                    blackCount++;
                }
            }
        }
        if (whiteCount == 0) {       //Check if either player has lost all their pebbles
            JOptionPane.showMessageDialog(null, "White lost all their pebbles, Black won!", "Game over!", JOptionPane.PLAIN_MESSAGE);
            return true;
        } else if (blackCount == 0) {
            JOptionPane.showMessageDialog(null, "Black lost all their pebbles, White won!", "Game over!", JOptionPane.PLAIN_MESSAGE);
            return true;
        }
        if (totalTurnsLeft <= 0) {           //Check if the total turns have been exhausted
            if (whiteCount > blackCount) {
                JOptionPane.showMessageDialog(null, "White won!", "Game over!", JOptionPane.PLAIN_MESSAGE);
                return true;
            } else if (whiteCount < blackCount) {
                JOptionPane.showMessageDialog(null, "Black won!", "Game over!", JOptionPane.PLAIN_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Draw!", "Game over!", JOptionPane.PLAIN_MESSAGE);
                return true;
            }
        }

        return false;    //If none of the game-ending conditions are met, return false
    }
}

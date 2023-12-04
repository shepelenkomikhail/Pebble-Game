import javax.swing.*;
import java.util.Random;

public class Board {
    public final int size;
    private final JButton[][] board;             //Initializing variables to store a board data

    public Board(int size) {
        this.size = size;

        board = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new JButton("");
            }
        }

        Random random = new Random(); //Creating a random object; There are 2 * size pebbles according to the game rules
        int totalPebbles = size * 2;
        for (int i = 0; i < totalPebbles; i++) {                          //Random arrangement of pebbles
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if (board[x][y].getText().isEmpty()) {
                if (i < totalPebbles / 2) {
                    board[x][y].setText("B");
                } else {
                    board[x][y].setText("W");
                }
            } else {
                i--;
            }
        }
    }

    public JButton[][] getBoard() {
        return board;
    }          //Board getter
}

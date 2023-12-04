import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private JPanel topPanelContainer;
    private JLabel turnLabel;                  //Initializing java swing elements
    private JLabel elementLabel;
    private Game game;                     //Instance of the game logic
    private JPanel cellPanel;

    public void start() {
        displayFirstScreen();
        frame.setVisible(true);                    //Method to start the GUI
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    private void displayFirstScreen() {                      //Method to display the initial screen
        frame = new JFrame("Pebble");                     //Create the main window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //Set close operation
        JPanel centerPanel = new JPanel();
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

        JLabel introText = new JLabel("Welcome!");                            //Create and add introductory text labels
        JLabel chooseBoardText = new JLabel("Choose the board's size!");

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(introText);
        textPanel.add(chooseBoardText);

        JPanel buttonPanel = new JPanel();

        JButton[] layoutButtons = getLayoutButtons();       //Create and add buttons for different board sizes
        for (JButton button : layoutButtons) {
            buttonPanel.add(button);
        }

        centerPanel.add(textPanel);        //Add text and button panels to the center panel
        centerPanel.add(buttonPanel);

        frame.getContentPane().add(centerPanel); //Add the center panel to the content pane
    }

    private JButton[] getLayoutButtons() {          //Method to get buttons for different board sizes
        JButton button3x3 = new JButton("3x3");
        button3x3.addActionListener(new defineTableButtonListener());

        JButton button4x4 = new JButton("4x4");
        button4x4.addActionListener(new defineTableButtonListener());

        JButton button6x6 = new JButton("6x6");
        button6x6.addActionListener(new defineTableButtonListener());

        return new JButton[]{button3x3, button4x4, button6x6};
    }

    private void initializeBoard() {         //Method to initialize the game board
        cellPanel = new JPanel(new GridLayout(game.getHeight(), game.getWidth()));  //Create panels for cells and layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(game.getWidth() * 100, game.getHeight() * 100));

        for (int i = 0; i < game.getHeight(); i++) {         //Loop through each cell in the board
            for (int j = 0; j < game.getWidth(); j++) {
                JButton currCell = game.board.getBoard()[i][j];

                currCell.setBounds(j * 100, i * 100, 100, 100); //Set position and size of the cell
                currCell.setPreferredSize(new Dimension(100, 100));
                layeredPane.add(currCell, 1);      //Add cell to the layered pane and cell panel
                cellPanel.add(currCell);

                int finalI = i;
                int finalJ = j;         //Add action listener to handle button clicks
                currCell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (firstClickedButton == null) {            //If no button is clicked, check if the current player can move the pebble
                            if (game.getCurrentPlayer().getPebbleColour().equals(currCell.getText())) {
                                //Allow the current player to move their pebbles
                                firstClickedButton = (JButton) e.getSource();
                                firstClickedButtonCoordinates = new int[]{finalI, finalJ};
                            } else if (currCell.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "You cannot move empty pebbles!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                            } else {
                                //Show a dialog indicating that it's not the player's turn to move
                                JOptionPane.showMessageDialog(null, "It's not your turn to move!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            //If a button is already clicked, perform the swap
                            JButton secondClickedButton = (JButton) e.getSource();
                            if (!secondClickedButton.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "You can swap pebbles only with empty cells!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            //Check if the swap is valid and update the board
                            int rowAbsDifference = Math.abs(firstClickedButtonCoordinates[0] - finalI);
                            int colAbsDifference = Math.abs(firstClickedButtonCoordinates[1] - finalJ);
                            if ((rowAbsDifference == 0 && colAbsDifference == 1) || (rowAbsDifference == 1 && colAbsDifference == 0)) {
                                swapButtonValues(firstClickedButton, secondClickedButton);
                                firstClickedButton = null;
                                firstClickedButtonCoordinates = null;

                                String opponentColour = game.getOpponentPlayer().getPebbleColour();
                                //Remove opponent pebbles and update turn information
                                if (finalI - 1 >= 0 && game.board.getBoard()[finalI - 1][finalJ].getText() == opponentColour) {
                                    game.board.getBoard()[finalI - 1][finalJ].setText("");
                                }
                                if (finalI + 1 < game.getHeight() && game.board.getBoard()[finalI + 1][finalJ].getText() == opponentColour) {
                                    game.board.getBoard()[finalI + 1][finalJ].setText("");
                                }
                                if (finalJ - 1 >= 0 && game.board.getBoard()[finalI][finalJ - 1].getText() == opponentColour) {
                                    game.board.getBoard()[finalI][finalJ - 1].setText("");
                                }
                                if (finalJ + 1 < game.getWidth() && game.board.getBoard()[finalI][finalJ + 1].getText() == opponentColour) {
                                    game.board.getBoard()[finalI][finalJ + 1].setText("");
                                }
                                //Reset for the next swap
                                game.minusTurn();
                                updateTopPanel();
                                if (game.determineWinner()) {      //Check for a winner and reset the game if needed
                                    resetGame();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Only neighbour pebbles can be swapped!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                });
            }
        }

        initializeTopPanel();           //Initialize and add the top panel and cell panel to the frame
        frame.setLayout(new BorderLayout());
        frame.add(cellPanel, BorderLayout.CENTER);
        frame.add(topPanelContainer, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    JButton firstClickedButton = null;            //Variables to keep track of the first clicked button and its coordinates
    int[] firstClickedButtonCoordinates;

    private void swapButtonValues(JButton firstButton, JButton secondButton) {        //Method to swap values between two buttons
        String aux = firstButton.getText();
        firstButton.setText(secondButton.getText());
        secondButton.setText(aux);
    }

    class defineTableButtonListener implements ActionListener {     //Listener class for board size buttons
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String buttonText = btn.getText();
            int width = Integer.parseInt(buttonText.split("x")[0]);
            int height = Integer.parseInt(buttonText.split("x")[1]);
            frame.getContentPane().removeAll();
            frame.setSize(width * 100, height * 100 + 100);
            game = new Game(width, height);
            initializeBoard();
            frame.setLocationRelativeTo(null);
        }
    }

    public void resetGame() {          //Method to reset the game
        game = new Game(game.getWidth(), game.getHeight()); //Create a new Game instance with the same dimensions
        frame.remove(cellPanel);
        initializeBoard();
        game.totalTurnsLeft = game.getWidth() * 5; //Reset the number of turns
        updateTopPanel();
    }

    private void updateTopPanel() {     //Method to update the top panel with turn and element information
        turnLabel.setText("Player " + game.getCurrentPlayer().getName() + "'s Turn, turns left: " + game.totalTurnsLeft);
        elementLabel.setText("Element to Place: " + game.getCurrentPlayer().getPebbleColour());

        //Update the player's name and pebble color labels
        if (game.getCurrentPlayer().getPebblesCount() > 0) {
            turnLabel.setText("Player " + game.getCurrentPlayer().getName() + "'s Turn, turns left: " + game.totalTurnsLeft);
            elementLabel.setText("Element to Place: " + game.getCurrentPlayer().getPebbleColour());
        } else {
            //If there are no pebbles left for the current player, switch to the opponent player
            game.switchPlayerTurn();
            turnLabel.setText("Player " + game.getCurrentPlayer().getName() + "'s Turn, turns left: " + game.totalTurnsLeft);
            elementLabel.setText("Element to Place: " + game.getCurrentPlayer().getPebbleColour());
        }
    }

    private void initializeTopPanel() {         //Method to initialize the top panel with turn and element labels
        topPanelContainer = new JPanel(new BorderLayout());
        JPanel topTextPanel = new JPanel(new GridLayout(2, 1));

        turnLabel = new JLabel("Player " + game.getCurrentPlayer().getName() + "'s Turn, turns left: " + game.totalTurnsLeft);
        elementLabel = new JLabel("Element to Place: " + game.getCurrentPlayer().getPebbleColour());

        topTextPanel.add(turnLabel);
        topTextPanel.add(elementLabel);
        topTextPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        topPanelContainer.add(topTextPanel, BorderLayout.NORTH);
    }
}
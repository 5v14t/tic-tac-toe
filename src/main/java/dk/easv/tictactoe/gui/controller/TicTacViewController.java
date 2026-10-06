
package dk.easv.tictactoe.gui.controller;

// Java imports
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

// Project imports
import dk.easv.tictactoe.bll.GameBoard;
import dk.easv.tictactoe.bll.IGameBoard;

/**
 *
 * @author EASV
 */
public class TicTacViewController implements Initializable
{
    @FXML
    private Label lblPlayer;

    @FXML
    private Button btnNewGame;

    @FXML
    private GridPane gridPane;
    
    private static final String TXT_PLAYER = "Player: ";
    private IGameBoard game;

    /**
     * Event handler for the grid buttons
     *
     * @param event
     */
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        try
        {
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            Integer col = GridPane.getColumnIndex((Node) event.getSource());
            int r = (row == null) ? 0 : row;
            int c = (col == null) ? 0 : col;
            int player = game.getNextPlayer();
            if (game.play(c, r))
            {
                Button btn = (Button) event.getSource();
                String xOrO = player == 0 ? "X" : "O";
                btn.setText(xOrO);

                if (game.isGameOver())
                {
                    int winner = game.getWinner();
                    displayWinner(winner);
                    highlightWinningLine(game.getWinningLine());
                } else {
                    setPlayer();
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Event handler for starting a new game
     *
     * @param event
     */
    @FXML
    private void handleNewGame(ActionEvent event)
    {
        game.newGame();
        setPlayer();
        clearBoard();
    }

    /**
     * Initializes a new controller
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param rb
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        game = new GameBoard();
        setPlayer();
    }

    /**
     * Set the next player
     */
    private void setPlayer()
    {
        lblPlayer.setText(TXT_PLAYER + game.getNextPlayer());
    }


    /**
     * Finds a winner or a draw and displays a message based
     * @param winner
     */
    private void displayWinner(int winner)
    {
        String message = "";
        switch (winner)
        {
            case -1:
                message = "It's a draw :-(";
                break;
            default:
                message = "Player " + winner + " wins!!!";
                break;
        }
        lblPlayer.setText(message);
    }

    /**
     * Clears the game board in the GUI
     */
    private void clearBoard()
    {
        for(Node n : gridPane.getChildren())
        {
            Button btn = (Button) n;
            btn.setText("");
        }
    }

    private void highlightWinningLine(ArrayList<int[]> winningLine) {
        // Get all the children of the GridPane
        ObservableList<Node> children = gridPane.getChildrenUnmodifiable();

        // For every cell in the winging line
        for (int[] cell : winningLine) {

            // For every child of the GridPane
            for (Node child : children) {

                // Get the row index for a child (button) of the GridPane
                Integer row = GridPane.getRowIndex(child);

                // Get the column index for a child (button) of the GridPane
                Integer column = GridPane.getColumnIndex(child);

                /*
                 Index can be null when it was not specifically mentioned
                 These items take the 0,0 cell.
                 So the child that has index as null, will be placed automatically in the 0,0 cell
                 But how would our code know that it is 0,0, if it returns null?
                 This is why, when we get index, we have to check if it is null
                 Cuz if it is, we have to manually store 0 in the corresponded indexes
                 As it is done lower
                */
                if (row == null) row = 0;
                if (column == null) column = 0;

                // If the winning cell coordinates match the child (button) coordinates
                if (cell[0] == row && cell[1] == column) {

                    // Change the color of that button
                    Button button = (Button) child;
                    button.setStyle("-fx-background-color: #D1FFBD");
                }
            }
        }
    }
}

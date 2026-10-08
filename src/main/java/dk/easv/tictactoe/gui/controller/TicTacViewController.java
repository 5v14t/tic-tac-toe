
package dk.easv.tictactoe.gui.controller;

// Java imports
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dk.easv.tictactoe.bll.AutomatedPlayer;
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
    private GridPane gridPane;
    
    private static final String TXT_PLAYER = "Player: ";
    private IGameBoard game;

    private AutomatedPlayer automatedPlayer = new AutomatedPlayer(new Integer[][]{});

    /**
     * Event handler for the grid buttons
     *
     * @param event
     */
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        humanPlayerMove(event);
        automatedPlayerMove();
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
        automatedPlayer = new AutomatedPlayer(new Integer[][]{});
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
     * Finds a winner or a draw and displays a message based.
     */
    private void displayWinner()
    {
        int winner = game.getWinner();

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
        highlightWinningLine(game.getWinningLine());
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
            btn.setStyle("");
        }
    }

    /**
     * Highlights the winning line with green color.
     *
     * @param winningLine an Arraylist of array of integers that contains the winning cells.
     */
    private void highlightWinningLine(ArrayList<int[]> winningLine) {
        ArrayList<Button> buttons = getButtonsAtCoordinates(winningLine);
        if (buttons == null) return;

        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #D1FFBD");
        }
    }

    /**
     * Gets the button that has been pressed by a human, and stores it in the game's grid by making a move.
     *
     * @param event ActionEvent that has been passed from the interacted item.
     */
    private void humanPlayerMove(ActionEvent event) {
        move((Button) event.getSource());
    }

    /**
     * Gets a random empty cell coordinates and makes a move.
     */
    private void automatedPlayerMove() {
        int[] cell = automatedPlayer.play();
        if (cell.length != 0) {
            Button button = getButtonAtCoordinate(cell);
            move(button);
        }
    }

    /**
     * Marks a button with the player ID in the grid and displays it in the application.
     * @param button button that has been chosen by automated or real player.
     * @param player the player that chose the button.
     */
    private void markButton(Button button, int player) {
        String xOrO = player == 0 ? "X" : "O";
        button.setText(xOrO);
    }

    /**
     * Gets the coordinates of the button, stores the current player, and makes a move with that player,
     * by marking the chosen button and updating the grid for the automated player. If after the turn
     * the game is over, we display the winner, otherwise we change to the next player.
     *
     * @param button the button that was chosen
     */
    private void move(Button button) {
        Integer row = GridPane.getRowIndex(button);
        Integer col = GridPane.getColumnIndex(button);
        int r = (row == null) ? 0 : row;
        int c = (col == null) ? 0 : col;

        int player = game.getNextPlayer();

        if (game.play(c, r))
        {
            markButton(button, player);
            automatedPlayer.updateGrid(game.getGrid());

            if (game.isGameOver()) displayWinner();
            else setPlayer();
        }
    }

    /**
     * Gets button at a specific coordinates by going through each button in the grid, and checking the coordinates
     * of the current button and the chosen button.
     *
     * @param coordinates the coordinates of the chosen button.
     *
     * @return a Button object at the specified coordinates. May return NULL if none have been found.
     */
    private Button getButtonAtCoordinate(int[] coordinates) {
        ObservableList<Node> children = gridPane.getChildrenUnmodifiable();

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

            if (coordinates[0] == row && coordinates[1] == column) {
                return (Button) child;
            }
        }

        return null;
    }

    /**
     * Gets buttons at different coordinates by going through each button in the grid and checking
     * if the current button matches one of the required coordinates. If so, it adds it to an array of buttons.
     *
     * @param coordinatesList a list of coordinates.
     *
     * @return an ArrayList of Buttons that were found at the specified coordinates. May return NULL if none is found.
     */
    private ArrayList<Button> getButtonsAtCoordinates(ArrayList<int[]> coordinatesList) {
        ArrayList<Button> buttons = new ArrayList<>();

        for (int[] coordinate : coordinatesList) {
            Button button = getButtonAtCoordinate(coordinate);
            if (button != null) buttons.add(button);
        }

        if (buttons.isEmpty()) return null;

        return buttons;
    }
}

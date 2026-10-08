
package dk.easv.tictactoe.bll;

import java.util.ArrayList;

/**
 *
 * @author EASV
 */
public class GameBoard implements IGameBoard
{
    private static final int GRID_SIZE = 3;

    private int player = 0;
    private Integer[][] grid = new Integer[GRID_SIZE][GRID_SIZE];
    private Integer winner;
    private ArrayList<int[]> winningLine = new ArrayList<>();

    /**
     * Returns 0 for player 0, 1 for player 1.
     *
     * @return int ID of the next player.
     */
    public int getNextPlayer()
    {
        return player;
    }

    /**
     * Attempts to let the current player play at the given coordinates. It the
     * attempt is successful the current player has ended his turn, and it is the
     * next players turn.
     *
     * @param col column to place a marker in.
     * @param row row to place a marker in.
     * @return true if the move is accepted, otherwise false. If gameOver == true
     * this method will always return false.
     */
    public boolean play(int col, int row)
    {
        if (isGameOver()) return false;

        Integer cell = grid[row][col];
        if (cell != null) return false;

        populateCell(row, col);
        switchPlayer();
        return true;
    }

    /**
     * Tells us if the game has ended either by draw or by meeting the winning
     * condition.
     *
     * @return true if the game is over, else it will return false.
     */
    public boolean isGameOver()
    {
        if (checkRows()) return true;
        if (checkColums()) return true;
        if (checkDescendingDiagonal()) return true;
        if (checkAscendingDiagonal()) return true;
        if (hasEmptySpaces()) return false;

        winner = -1;
        return true;
    }

    /**
     * Gets the id of the winner, -1 if it's a draw.
     *
     * @return int id of winner, or -1 if you draw.
     */
    public int getWinner()
    {
        return winner;
    }

    /**
     * Resets the game to a new game state.
     */
    public void newGame()
    {
        grid = new Integer[GRID_SIZE][GRID_SIZE];
        player = 0;
        winner = null;
        winningLine = new ArrayList<>();
    }

    /**
     * Switches players. If 0, switch to 1, and vice versa.
     */
    private void switchPlayer()
    {
        if (player == 0) player = 1;
        else player = 0;
    }

    /**
     * Populate the specified cell with current player's signature.
     *
     * @param row row to place a marker in.
     * @param col column to place a marker in.
     */
    private void populateCell(int row, int col)
    {
        grid[row][col] = player;
    }

    /**
     * Checking all the rows for a complete set, and saving the winner if it finds a complete set.
     * @return true if any of the rows have a complete set, otherwise false.
     */
    private boolean checkRows() {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (checkRow(row)) {
                winner = grid[row][0];
                saveWinningRow(row);
                return true;
            }
        }

        return false;
    }

    /**
     * Checking a specific row for a complete set.
     * @param row specified row to check.
     * @return true if the row has a complete set, otherwise false.
     */
    private boolean checkRow(int row) {
        Integer firstCell = grid[row][0];
        if (firstCell == null) return false;

        for (int col = 1; col < GRID_SIZE; col++) {
            if (!firstCell.equals(grid[row][col])) return false;
        }

        return true;
    }

    /**
     * Checking a specific column for a complete set.
     * @param col specified column to check.
     * @return true if the column has a complete set, otherwise false.
     */
    private boolean checkColumn(int col) {
        Integer firstCell = grid[0][col];
        if (firstCell == null) return false;

        for (int row = 1; row < GRID_SIZE; row++) {
            if (!firstCell.equals(grid[row][col])) return false;
        }

        return true;
    }

    /**
     * Checking all the columns for a complete set, and saving the winner if it finds a complete set.
     * @return true if any of the columns have a complete set, otherwise false.
     */
    private boolean checkColums() {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (checkColumn(col)) {
                winner = grid[0][col];
                saveWinningColumn(col);
                return true;
            }
        }

        return false;
    }

    /**
     * Checking the descending diagonal for a complete set and saving the winner if found.
     * @return true if complete set has been found, otherwise false.
     */
    private boolean checkDescendingDiagonal() {
        Integer firstCell = grid[0][0];
        if (firstCell == null) return false;

        for (int cell = 1; cell < GRID_SIZE; cell++) {
            if (!firstCell.equals(grid[cell][cell])) return false;
        }

        winner = firstCell;
        saveWinningDescendingDiagonal();
        return true;
    }

    /**
     * Checking the ascending diagonal for a complete set and saving the winner if found.
     * @return true if complete set has been found, otherwise false.
     */
    private boolean checkAscendingDiagonal() {
        Integer firstCell = grid[GRID_SIZE - 1][0];
        if (firstCell == null) return false;

        for (int cell = 1; cell < GRID_SIZE; cell++) {
            if (!firstCell.equals(grid[GRID_SIZE - 1 - cell][cell])) return false;
        }

        winner = firstCell;
        saveWinningAscendingDiagonal();
        return true;
    }

    /**
     * Checks all the cells for empty cells.
     * @return true if found empty cell, otherwise false.
     */
    private boolean hasEmptySpaces() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == null) return true;
            }
        }

        return false;
    }

    /**
     * Saves a list of the winning row cells into the memory.
     *
     * @param row a row that is being saved into the memory
     */
    private void saveWinningRow(int row) {
        for (int col = 0; col < GRID_SIZE; col++) {
            winningLine.add(new int[]{row, col});
        }
    }

    /**
     * Saves a list of the winning column cells into the memory.
     *
     * @param col a column that is being saved into the memory.
     */
    private void saveWinningColumn(int col) {
        for (int row = 0; row < GRID_SIZE; row++) {
            winningLine.add(new int[]{row, col});
        }
    }

    /**
     * Saves a list of the winning descending diagonal cells into the memory.
     */
    private void saveWinningDescendingDiagonal() {
        for (int i = 0; i < GRID_SIZE; i++) {
            winningLine.add(new int[]{i, i});
        }
    }

    /**
     * Saves a list of the winning ascending diagonal cells into the memory.
     */
    private void saveWinningAscendingDiagonal() {
        for (int i = 0; i < GRID_SIZE; i++) {
            winningLine.add(new int[]{GRID_SIZE - 1 - i, i});
        }
    }

    /**
     * Gets the winning line (list of cells) out of memory
     *
     * @return an Arraylist of array of integers with the winning cells.
     */
    public ArrayList<int[]> getWinningLine() {
        return winningLine;
    }

    /**
     * Gets the grid of the current game.
     *
     * @return a 2D array of Integers with the current game's grid.
     */
    public Integer[][] getGrid() {
        return this.grid;
    }
}

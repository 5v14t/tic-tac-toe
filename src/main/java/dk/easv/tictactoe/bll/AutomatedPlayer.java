package dk.easv.tictactoe.bll;

import java.util.ArrayList;

/**
 * This class resembles an automated player which acts and plays within the tic-tac-toe game.
 *
 * @author Sviatoslav
 */
public class AutomatedPlayer {
    private Integer[][] grid;

    /**
     * Constructs a new instance of AutomatedPlayer with given grid.
     *
     * @param grid the grid of the tic-tac-toe game.
     */
    public AutomatedPlayer(Integer[][] grid) {
        updateGrid(grid);
    }

    /**
     * Finds a list of all the empty spaces (button) and chooses a random one of those.
     *
     * @return a 1D array of x and y coordinates of the chosen space (button), or an empty int array
     * if no empty spaces were found.
     */
    public int[] play() {
        ArrayList<int[]> emptySpaces = findEmptySpaces();
        if (emptySpaces == null) return new int[]{};

        int randomIndex = getRandomIndexFrom(emptySpaces.size());
        return emptySpaces.get(randomIndex);
    }

    /**
     * Calculates a random index based on the amount possible options.
     *
     * @param size the amount of possible options to choose from.
     *
     * @return an index of the option that was chosen.
     */
    private int getRandomIndexFrom(int size) {
        return (int) (Math.random() * size);
    }

    /**
     * Goes through all the rows and columns searching for empty spaces (buttons).
     *
     * @return a ArrayList of integer arrays with the empty spaces (buttons). May return NULL
     * if no empty spaces (buttons) were found.
     */
    private ArrayList<int[]> findEmptySpaces() {
        ArrayList<int[]> result = new ArrayList<>();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col] == null) {
                    result.add(new int[]{row, col});
                }
            }
        }

        if (result.isEmpty()) return null;

        return result;
    }

    /**
     * Updates the automated player's memory of the game grid.
     *
     * @param grid a current game's grid represented by a 2D array of Integers
     */
    public void updateGrid(Integer[][] grid) {
        this.grid = grid;
    }
}

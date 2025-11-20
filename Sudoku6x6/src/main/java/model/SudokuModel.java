package model;

import java.util.Random;

/**
 * Represents the logic and data structure for a 6x6 Sudoku puzzle.
 * This class is responsible for generating a full valid Sudoku solution,
 * creating the playable puzzle by removing cells, and validating conflicts.
 */
public class SudokuModel {

    /** Visible board (with removed cells for the puzzle). */
    private int[][] board;

    /** Complete solution board (fully solved Sudoku). */
    private int[][] solutionBoard;

    /** Size of the Sudoku grid (6x6). */
    private static final int SIZE = 6;

    /** Number of columns in each block (3). */
    private static final int BLOCK_COLS = 3;

    /** Number of rows in each block (2). */
    private static final int BLOCK_ROWS = 2;

    /** Random number generator for shuffling and removing cells. */
    private Random random;

    /**
     * Constructs a new SudokuModel.
     * <p>
     * Steps performed:
     * <ol>
     *     <li>Generates a full valid solution.</li>
     *     <li>Copies the solution into the visible board.</li>
     *     <li>Removes cells to leave only two visible numbers per block.</li>
     * </ol>
     */
    public SudokuModel() {
        board = new int[SIZE][SIZE];
        solutionBoard = new int[SIZE][SIZE];
        random = new Random();

        generateFullSolution();
        copySolutionToBoard();
        removeCellsToLeaveTwoPerBlock();
    }

    /**
     * Returns the current visible board (with empty cells = 0).
     *
     * @return the 6x6 board the user interacts with
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Returns the complete solved Sudoku board.
     *
     * @return full solution board
     */
    public int[][] getSolutionBoard() {
        return solutionBoard;
    }

    /**
     * Generates a fully solved Sudoku board by calling the recursive solver.
     */
    private void generateFullSolution() {
        solveBoard(0, 0);
    }

    /**
     * Recursive backtracking algorithm to fill the Sudoku solution.
     *
     * @param row the current row index
     * @param col the current column index
     * @return true if the board is successfully solved
     */
    private boolean solveBoard(int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        if (solutionBoard[row][col] != 0)
            return solveBoard(nextRow, nextCol);

        int[] nums = generateShuffledNumbers();
        for (int num : nums) {
            if (isSafe(row, col, num, solutionBoard)) {
                solutionBoard[row][col] = num;
                if (solveBoard(nextRow, nextCol)) {
                    return true;
                }
                solutionBoard[row][col] = 0;
            }
        }
        return false;
    }

    /**
     * Generates an array containing numbers 1–6 in random order.
     *
     * @return shuffled array of numbers
     */
    private int[] generateShuffledNumbers() {
        int[] nums = {1, 2, 3, 4, 5, 6};
        for (int i = nums.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        return nums;
    }

    /**
     * Copies the completed solution board into the playable board.
     */
    private void copySolutionToBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = solutionBoard[i][j];
            }
        }
    }

    /**
     * Removes cells to ensure only 2 numbers remain visible per 2x3 block.
     */
    private void removeCellsToLeaveTwoPerBlock() {
        for (int blockRow = 0; blockRow < SIZE; blockRow += BLOCK_ROWS) {
            for (int blockCol = 0; blockCol < SIZE; blockCol += BLOCK_COLS) {
                removeAllButTwo(blockRow, blockCol);
            }
        }
    }

    /**
     * Removes all numbers in a block except two random positions.
     *
     * @param startRow starting row of the block
     * @param startCol starting column of the block
     */
    private void removeAllButTwo(int startRow, int startCol) {
        int[][] positions = new int[BLOCK_ROWS * BLOCK_COLS][2];
        int index = 0;

        for (int r = 0; r < BLOCK_ROWS; r++) {
            for (int c = 0; c < BLOCK_COLS; c++) {
                positions[index][0] = startRow + r;
                positions[index][1] = startCol + c;
                index++;
            }
        }

        // shuffle block positions
        for (int i = positions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        // remove all except 2
        for (int i = 2; i < positions.length; i++) {
            int r = positions[i][0];
            int c = positions[i][1];
            board[r][c] = 0;
        }
    }

    /**
     * Checks if placing a given number at a location is valid.
     *
     * @param row the row index
     * @param col the column index
     * @param num the number to validate
     * @param targetBoard the board to check against
     * @return true if the number does not violate Sudoku rules
     */
    private boolean isSafe(int row, int col, int num, int[][] targetBoard) {
        // row
        for (int c = 0; c < SIZE; c++) {
            if (targetBoard[row][c] == num) return false;
        }

        // column
        for (int r = 0; r < SIZE; r++) {
            if (targetBoard[r][col] == num) return false;
        }

        // block
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int r = 0; r < BLOCK_ROWS; r++) {
            for (int c = 0; c < BLOCK_COLS; c++) {
                if (targetBoard[startRow + r][startCol + c] == num) return false;
            }
        }

        return true;
    }

    /**
     * Checks if placing the given number in a specific cell causes a conflict
     * with existing numbers in the same row, column, or block.
     *
     * @param currentBoard the board currently being played
     * @param row row index
     * @param col column index
     * @param num number to check
     * @return true if a conflict exists
     */
    public boolean hasConflict(int[][] currentBoard, int row, int col, int num) {
        if (num == 0) return false;

        // row
        for (int c = 0; c < SIZE; c++) {
            if (c != col && currentBoard[row][c] == num)
                return true;
        }

        // column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && currentBoard[r][col] == num)
                return true;
        }

        // block
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int r = 0; r < BLOCK_ROWS; r++) {
            for (int c = 0; c < BLOCK_COLS; c++) {
                int rr = startRow + r;
                int cc = startCol + c;
                if (!(rr == row && cc == col) && currentBoard[rr][cc] == num)
                    return true;
            }
        }
        return false;
    }
}

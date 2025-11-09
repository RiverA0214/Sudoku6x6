package model;

import java.util.Random;

public class SudokuModel {

    private int[][] board; // tablero visible (con huecos)
    private int[][] solutionBoard; // tablero con solución completa
    private static final int SIZE = 6;
    private static final int BLOCK_COLS = 3;
    private static final int BLOCK_ROWS = 2;
    private Random random;

    public SudokuModel() {
        board = new int[SIZE][SIZE];
        solutionBoard = new int[SIZE][SIZE];
        random = new Random();

        generateFullSolution(); // genera sudoku completo en solutionBoard
        copySolutionToBoard(); // copia la solución a board
        removeCellsToLeaveTwoPerBlock(); // borra celdas (deja 2 por bloque)
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolutionBoard() {
        return solutionBoard;
    }

    //
    // GENERA UNA SOLUCIÓN COMPLETA
    //
    private void generateFullSolution() {
        solveBoard(0, 0);
    }

    private boolean solveBoard(int row, int col) {
        if (row == SIZE) return true; // terminó

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

    //
    // Copia la solucion al tablero visible
    //
    private void copySolutionToBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = solutionBoard[i][j];
            }
        }
    }

    //
    // Borra celdas hasta dejar dos por bloque
    //
    private void removeCellsToLeaveTwoPerBlock() {
        for (int blockRow = 0; blockRow < SIZE; blockRow += BLOCK_ROWS) {
            for (int blockCol = 0; blockCol < SIZE; blockCol += BLOCK_COLS) {
                removeAllButTwo(blockRow, blockCol);
            }
        }
    }

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

        // baraja posiciones
        for (int i = positions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        // deja solo 2 números
        for (int i = 2; i < positions.length; i++) {
            int r = positions[i][0];
            int c = positions[i][1];
            board[r][c] = 0;
        }
    }

    //
    // VALIDACIONES
    //
    private boolean isSafe(int row, int col, int num, int[][] targetBoard) {
        // fila
        for (int c = 0; c < SIZE; c++) {
            if (targetBoard[row][c] == num) return false;
        }

        // columna
        for (int r = 0; r < SIZE; r++) {
            if (targetBoard[r][col] == num) return false;
        }

        // bloque
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int r = 0; r < BLOCK_ROWS; r++) {
            for (int c = 0; c < BLOCK_COLS; c++) {
                if (targetBoard[startRow + r][startCol + c] == num) return false;
            }
        }

        return true;
    }

    public boolean hasConflict(int[][] currentBoard, int row, int col, int num) {
        if (num == 0) return false;

        // fila
        for (int c = 0; c < SIZE; c++) {
            if (c != col && currentBoard[row][c] == num)
                return true;
        }

        // columna
        for (int r = 0; r < SIZE; r++) {
            if (r != row && currentBoard[r][col] == num)
                return true;
        }

        // bloque
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

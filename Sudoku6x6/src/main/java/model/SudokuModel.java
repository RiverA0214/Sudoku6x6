package model;

import java.util.Random;

public class SudokuModel {

    private int[][] board;  // tablero 6x6
    private static final int SIZE = 6;
    private static final int BLOCK_COLS = 3; // bloques horizontales
    private static final int BLOCK_ROWS = 2; // bloques verticales
    private Random random;

    public SudokuModel() {
        board = new int[SIZE][SIZE];
        random = new Random();
        generateFullSolution();
        removeCellsToLeaveTwoPerBlock();
    }

    public int[][] getBoard() {
        return board;
    }

    // ===================================================
    // GENERADOR COMPLETO CON BACKTRACKING
    // ===================================================
    private boolean generateFullSolution() {
        return solveBoard(0, 0);
    }

    private boolean solveBoard(int col, int row) {
        if (col == SIZE) return true;

        int nextCol = (row == SIZE - 1) ? col + 1 : col;
        int nextRow = (row + 1) % SIZE;

        if (board[col][row] != 0)
            return solveBoard(nextCol, nextRow);

        int[] nums = generateShuffledNumbers();
        for (int num : nums) {
            if (isSafe(col, row, num)) {
                board[col][row] = num;
                if (solveBoard(nextCol, nextRow)) return true;
                board[col][row] = 0;
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

    // ===================================================
    // REMOVER CELDAS HASTA DEJAR 2 POR BLOQUE
    // ===================================================
    private void removeCellsToLeaveTwoPerBlock() {
        for (int blockCol = 0; blockCol < SIZE; blockCol += BLOCK_COLS) {
            for (int blockRow = 0; blockRow < SIZE; blockRow += BLOCK_ROWS) {
                removeAllButTwo(blockCol, blockRow);
            }
        }
    }

    private void removeAllButTwo(int startCol, int startRow) {
        int[][] positions = new int[BLOCK_COLS * BLOCK_ROWS][2];
        int index = 0;

        for (int c = 0; c < BLOCK_COLS; c++) {
            for (int r = 0; r < BLOCK_ROWS; r++) {
                positions[index][0] = startCol + c;
                positions[index][1] = startRow + r;
                index++;
            }
        }

        // barajar posiciones
        for (int i = positions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        // eliminar todas menos 2
        for (int i = 2; i < positions.length; i++) {
            int c = positions[i][0];
            int r = positions[i][1];
            board[c][r] = 0;
        }
    }

    // ===================================================
    // VALIDACIONES
    // ===================================================
    private boolean isSafe(int col, int row, int num) {
        // verifica fila
        for (int c = 0; c < SIZE; c++) {
            if (board[c][row] == num) return false;
        }

        // verifica columna
        for (int r = 0; r < SIZE; r++) {
            if (board[col][r] == num) return false;
        }

        // verifica bloque 3x2
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;

        for (int c = 0; c < BLOCK_COLS; c++) {
            for (int r = 0; r < BLOCK_ROWS; r++) {
                if (board[startCol + c][startRow + r] == num)
                    return false;
            }
        }

        return true;
    }

    public boolean hasConflict(int[][] currentBoard, int col, int row, int num) {
        if (num == 0) return false;

        // Revisa fila (misma row)
        for (int c = 0; c < SIZE; c++) {
            if (c != col && currentBoard[c][row] == num)
                return true;
        }

        // Revisa columna (misma col)
        for (int r = 0; r < SIZE; r++) {
            if (r != row && currentBoard[col][r] == num)
                return true;
        }

        // Revisa bloque
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;

        for (int c = 0; c < BLOCK_COLS; c++) {
            for (int r = 0; r < BLOCK_ROWS; r++) {
                int cc = startCol + c;
                int rr = startRow + r;
                if (!(cc == col && rr == row) && currentBoard[cc][rr] == num)
                    return true;
            }
        }

        return false;
    }
}

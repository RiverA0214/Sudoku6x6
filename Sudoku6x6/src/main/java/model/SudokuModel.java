package model;

import java.util.Random;

public class SudokuModel {

    private int[][] board;  //tablero 6x6
    private static final int SIZE = 6;
    private static final int BLOCK_COLS = 3;
    private static final int BLOCK_ROWS = 2;
    private Random random;

    public SudokuModel() {
        board = new int[SIZE][SIZE];
        random = new Random();
        generateInitialBoard();
    }

    public int[][] getBoard() {
        return board;
    }

    private void generateInitialBoard() {
        for (int blockCol = 0; blockCol < SIZE; blockCol += BLOCK_ROWS) {
            for (int blockRow = 0; blockRow < SIZE; blockRow += BLOCK_COLS) {
                fillBlock(blockCol, blockRow);
            }
        }
    }

    private void fillBlock(int startCol, int startRow) {
        int filled = 0;
        while (filled < 2) {
            int row = startCol + random.nextInt(BLOCK_ROWS);
            int col = startRow + random.nextInt(BLOCK_COLS);
            int num = 1 + random.nextInt(SIZE);

            if (board[col][row] == 0 && isSafe(col, row, num)){
                board[col][row] = num;
                filled++;
            }
        }
    }

    private boolean isSafe(int col, int row, int num) {
        //verifica fila
        for (int c = 0; c < SIZE; c++) {
            if (board[c][row] == num) return false;
        }

        //verifica columna
        for (int r = 0; r < SIZE; r++) {
            if (board[col][r] == num) return false;
        }

        //verifica bloque 2x3
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int r = 0; r < BLOCK_ROWS; r++) {
            for (int c = 0; c < BLOCK_COLS; c++) {
                if (board[startCol + c][startRow + r] == num) return false;
            }
        }

        return true;
    }

}

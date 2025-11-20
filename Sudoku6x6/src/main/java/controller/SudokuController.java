package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.SudokuModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the logic and user interactions
 * of the 6x6 Sudoku game.
 * <p>
 * This class initializes the Sudoku board, handles input validation,
 * highlights conflicts, and processes hint requests.
 */
public class SudokuController implements Initializable {

    @FXML private TextField cell00, cell01, cell02, cell03, cell04, cell05;
    @FXML private TextField cell10, cell11, cell12, cell13, cell14, cell15;
    @FXML private TextField cell20, cell21, cell22, cell23, cell24, cell25;
    @FXML private TextField cell30, cell31, cell32, cell33, cell34, cell35;
    @FXML private TextField cell40, cell41, cell42, cell43, cell44, cell45;
    @FXML private TextField cell50, cell51, cell52, cell53, cell54, cell55;

    private TextField[][] cells;
    private SudokuModel model;
    private int[][] board;

    /**
     * Initializes the Sudoku board, configures editable cells,
     * assigns listeners for input validation, and loads the initial puzzle.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new SudokuModel();
        board = model.getBoard();

        // Initialize 6x6 grid of TextFields
        cells = new TextField[][]{
                {cell00, cell01, cell02, cell03, cell04, cell05},
                {cell10, cell11, cell12, cell13, cell14, cell15},
                {cell20, cell21, cell22, cell23, cell24, cell25},
                {cell30, cell31, cell32, cell33, cell34, cell35},
                {cell40, cell41, cell42, cell43, cell44, cell45},
                {cell50, cell51, cell52, cell53, cell54, cell55}
        };

        // Configure each cell based on the initial puzzle
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {

                // Pre-filled cells: black text, non-editable
                if (board[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                    cells[row][col].setEditable(false);
                    cells[row][col].setStyle("-fx-text-fill: black; -fx-background-color: white;");
                }
                // Editable cells: gray text, empty, with validation listener
                else {
                    cells[row][col].clear();
                    cells[row][col].setEditable(true);
                    cells[row][col].setStyle("-fx-text-fill: gray; -fx-background-color: white;");

                    final int r = row;
                    final int c = col;

                    // Accept only numbers 1–6
                    cells[r][c].textProperty().addListener((obs, oldValue, newValue) -> {
                        if (!newValue.matches("[1-6]?")) {
                            cells[r][c].setText(oldValue);
                            return;
                        }

                        int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                        board[r][c] = value;

                        validateConflicts();
                    });
                }
            }
        }
    }

    /**
     * Validates the current board state and highlights conflicting cells
     * by changing their background color to pink.
     * <p>
     * Non-editable cells are always styled in black text,
     * editable cells in gray text unless part of a conflict.
     */
    private void validateConflicts() {
        // Reset styles
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (!cells[row][col].isEditable()) {
                    cells[row][col].setStyle("-fx-text-fill: black; -fx-background-color: white;");
                } else {
                    cells[row][col].setStyle("-fx-text-fill: gray; -fx-background-color: white;");
                }
            }
        }

        // Highlight conflicts
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int num = board[row][col];
                if (num != 0 && model.hasConflict(board, row, col, num)) {
                    cells[row][col].setStyle("-fx-background-color: #FFB6C1;");
                }
            }
        }
    }

    /**
     * Handles the "Hint" button action.
     * <p>
     * Selects a random empty cell and fills it with the correct value
     * from the solution board, marking it with blue text.
     */
    @FXML
    private void onHintButtonClick() {
        int[][] solution = model.getSolutionBoard();

        // Attempt up to 100 random empty cells
        for (int attempt = 0; attempt < 100; attempt++) {
            int row = (int) (Math.random() * 6);
            int col = (int) (Math.random() * 6);

            // Fill the chosen cell with the solution value
            if (board[row][col] == 0) {
                int value = solution[row][col];
                board[row][col] = value;
                cells[row][col].setText(String.valueOf(value));
                cells[row][col].setEditable(true);
                cells[row][col].setStyle("-fx-text-fill: blue; -fx-background-color: white;");
                return;
            }
        }
    }
}

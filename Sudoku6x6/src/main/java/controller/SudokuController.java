package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.SudokuModel;

import java.net.URL;
import java.util.ResourceBundle;

//fx:controller="controller.SudokuController"

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new SudokuModel();
        board = model.getBoard();

        cells = new TextField[][]{
                {cell00,cell01,cell02,cell03,cell04,cell05},
                {cell10,cell11,cell12,cell13,cell14,cell15},
                {cell20,cell21,cell22,cell23,cell24,cell25},
                {cell30,cell31,cell32,cell33,cell34,cell35},
                {cell40,cell41,cell42,cell43,cell44,cell45},
                {cell50,cell51,cell52,cell53,cell54,cell55}
        };

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {

                final int col = i;
                final int row = j;

                if (board[col][row] != 0) {
                    cells[col][row].setText(String.valueOf(board[i][j]));
                    cells[col][row].setEditable(false);
                    cells[col][row].setStyle("-fx-text-fill: black; -fx-background-color: white;");
                } else {
                    cells[col][row].clear();
                    cells[col][row].setEditable(true);
                    cells[col][row].setStyle("-fx-text-fill: gray; -fx-background-color: white;");

                    //validar solo se permite numeros del 1 al 6
                    cells[col][row].textProperty().addListener((obs, oldValue, newValue) -> {
                        if (!newValue.matches("[1-6]?")) {
                            cells[col][row].setText(oldValue);
                            return;
                        }

                        // actualiza modelo
                        int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                        board[col][row] = value;

                        //valida el tablero
                        validateConflicts();
                    });
                }
            }
        }
    }

    private void validateConflicts() {
        // limpia colores
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                // si la celda es fija, mantenerla gris
                if (!cells[i][j].isEditable()) {
                    cells[i][j].setStyle("-fx-text-fill: black; -fx-background-color: white;");
                } else {
                    cells[i][j].setStyle("-fx-text-fill: gray; -fx-background-color: white;");
                }
            }
        }

        // revisa conflictos
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int num = board[i][j];
                if (num != 0 && model.hasConflict(board, i, j, num)) {
                    // pinta rosado
                    cells[i][j].setStyle("-fx-background-color: #FFB6C1;");
                }
            }
        }
    }

}

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SudokuModel model = new SudokuModel();
        int[][] board = model.getBoard();

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
                if (board[i][j] != 0) {
                    cells[i][j].setText(String.valueOf(board[i][j]));
                    cells[i][j].setEditable(false);
                } else {
                    cells[i][j].clear();
                    cells[i][j].setEditable(true);
                }
            }
        }
    }
}

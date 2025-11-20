import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of the 6x6 Sudoku application.
 * <p>
 * This class initializes the JavaFX environment and loads the main Sudoku view
 * from the corresponding FXML file.
 */
public class Launcher extends Application {

    /**
     * Starts the JavaFX application by loading the main interface (SudokuView.fxml)
     * and displaying it on the primary stage.
     *
     * @param stage the primary stage used to display the application's UI
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SudokuView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sudoku 6x6");
        stage.setScene(scene);
        stage.show();
    }
}

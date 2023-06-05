package tw.edu.ncku.csie.javaminesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StartSceneController {
    private String[] difficultyArr = {"easy", "medium", "hard"};
    private int diffIdx = 0;
    @FXML
    private VBox root;
    @FXML
    private Label difficulty;

    @FXML
    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void initialize() {
        difficulty.setText(difficultyArr[diffIdx]);
    }

    @FXML
    protected void handleStartGameButton() throws IOException {
        BorderPane p = (BorderPane) Main.loadFxml("gamescene");
        p.setCenter(new MineGame(diffIdx));
        p.setStyle("-fx-background-color: white");
        Main.addNode(p);
    }

    @FXML
    protected void handleLeftButton() {
        diffIdx = (diffIdx + 2) % 3;
        this.difficulty.setText(difficultyArr[diffIdx]);
    }

    @FXML
    protected void handleRightButton() {
        diffIdx = (diffIdx + 1) % 3;
        this.difficulty.setText(difficultyArr[diffIdx]);
    }
}
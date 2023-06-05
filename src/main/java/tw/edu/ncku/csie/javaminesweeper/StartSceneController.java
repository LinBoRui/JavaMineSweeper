package tw.edu.ncku.csie.javaminesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StartSceneController {
    @FXML
    private VBox root;

    @FXML
    private Label difficultyLabel;

    private String[] difficultyArr = {"easy", "medium", "hard"};
    private int diffcultyIdx = 0;

    @FXML
    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void handleStartGameButton() throws IOException {
        BorderPane p = (BorderPane) Main.loadFxml("gamescene");
        p.setCenter(new MineGame(diffcultyIdx));
//        p.setPrefSize(600, 600);
        p.setStyle("-fx-background-color: white");
        Main.addNode(p);

//        rootPane.getChildren().add(p);
    }

    @FXML
    protected void onSettingClicked(ActionEvent event) throws IOException {
        Pane rootPane = (Pane) root.getParent();
        Main.addFxml("settingscene");
        rootPane.getChildren().get(rootPane.getChildren().size() - 1).setStyle("-fx-background-color: white");
    }

    @FXML
    protected void handleChangeDifficultyLeft() {
        diffcultyIdx = (diffcultyIdx + 2) % 3;
        difficultyLabel.setText(difficultyArr[diffcultyIdx]);
    }

    @FXML
    protected void handleChangeDifficultyRight() {
        diffcultyIdx = (diffcultyIdx + 1) % 3;
        difficultyLabel.setText(difficultyArr[diffcultyIdx]);
    }
}
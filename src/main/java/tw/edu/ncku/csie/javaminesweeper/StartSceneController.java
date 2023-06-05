package tw.edu.ncku.csie.javaminesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StartSceneController {
    @FXML
    private VBox root;

    @FXML
    private Label difficultyLabel;

    @FXML
    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void handleStartGameButton() throws IOException {
        BorderPane p = (BorderPane) Main.loadFxml("gamescene");
        p.setCenter(new MineGame());
//        p.setPrefSize(600, 600);
        p.setStyle("-fx-background-color: white");
        Main.addNode(p);

//        rootPane.getChildren().add(p);
    }

    @FXML
    protected void onSettingClicked(ActionEvent event) throws IOException {
        VBox pane = (VBox) Main.loadFxml("settingscene");
        pane.setStyle("-fx-background-color: white");
        Main.addNode(pane);
    }

    @FXML
    protected void handleChangeDifficultyLeft() {
        Level.changeLevel(2);
        difficultyLabel.setText(Level.getLevelString());
    }

    @FXML
    protected void handleChangeDifficultyRight() {
        Level.changeLevel(1);
        difficultyLabel.setText(Level.getLevelString());
    }
}
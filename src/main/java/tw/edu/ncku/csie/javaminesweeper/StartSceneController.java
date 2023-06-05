package tw.edu.ncku.csie.javaminesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StartSceneController {
    @FXML
    private VBox root;

    @FXML
    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void handleStartGameButton() throws IOException {

//        StackPane rootPane = (StackPane) this.root.getParent();
//        rootPane.getChildren().clear();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamescene.fxml"));
        BorderPane p = (BorderPane) Main.loadFxml("gamescene");
        p.setCenter(new MineGame());
        p.setStyle("-fx-background-color: white");
        Main.addNode(p);

//        rootPane.getChildren().add(p);
    }

    @FXML
    protected void onSettingClicked(ActionEvent event) throws IOException {
        Pane rootPane = (Pane) root.getParent();
        Main.addFxml("settingscene");
        rootPane.getChildren().get(rootPane.getChildren().size()-1).setStyle("-fx-background-color: white");
    }
}
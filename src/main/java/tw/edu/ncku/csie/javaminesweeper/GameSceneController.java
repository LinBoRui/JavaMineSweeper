package tw.edu.ncku.csie.javaminesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class GameSceneController {

    @FXML
    private BorderPane root;

    @FXML
    private Label bombCount;

    @FXML
    protected void handleBackButton() throws IOException {
        Main.removeNode(1);
    }

    @FXML
    protected void handleRestartButton() {
        MineGame prev = (MineGame) this.root.getCenter();
        if (prev.getInit()) {
            prev.stopTimer();
        }
        this.root.setCenter(new MineGame());
        setBombCount(10 + Level.getLevelInt() * 40);
    }

    @FXML
    protected void onSettingClicked() throws IOException {
        VBox pane = (VBox) Main.loadFxml("settingscene");
        pane.setStyle("-fx-background-color: white");
        Main.addNode(pane);
    }

    @FXML
    public void setBombCount(int number) {
        bombCount.setText(Integer.toString(number));
    }
}

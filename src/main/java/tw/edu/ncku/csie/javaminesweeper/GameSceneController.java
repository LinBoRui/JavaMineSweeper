package tw.edu.ncku.csie.javaminesweeper;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class GameSceneController {

    @FXML
    private BorderPane root;

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
    }
}

package tw.edu.ncku.csie.javaminesweeper;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class GameSceneController {

    NumberBinding size;

    @FXML
    private BorderPane root;

    @FXML
    private HBox topHBox, bottomHBox;

    @FXML
    private Label bombCount, timerLabel;

    @FXML
    private void initialize() {
        size = Bindings.min(root.widthProperty().multiply(0.9), root.heightProperty().add(topHBox.heightProperty().add(bottomHBox.heightProperty()).multiply(-1)));
        MineGame m = new MineGame(size);
        root.setCenter(m);
        // root.setPrefSize(600, 600);
        root.setStyle("-fx-background-color: white");
        setBombCount((int) Math.pow(Level.getLevelInt() + 1, 2) * 10);
    }

    @FXML
    protected void handleBackButton() throws IOException {
        Main.removeNode(1);
    }

    @FXML
    protected void handleRestartButton() {
        MineGame prev = (MineGame) this.root.getCenter();
        if (prev.isCreating) {
            prev.createThread.interrupt();
        }
        if (prev.getInit()) {
            prev.stopTimer();
        }
        this.root.setCenter(new MineGame(size));
        resetTimer();
        setBombCount(prev.getTotalBomb());
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

    @FXML
    public void resetTimer() {
        timerLabel.setText("00:00");
    }

    @FXML
    protected void handleScoreBoardButton() {
        Rank.showRanking();
    }
}

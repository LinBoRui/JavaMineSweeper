package tw.edu.ncku.csie.javaminesweeper;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.net.URI;

public class AuthorSceneController {
    @FXML
    private FlowPane rootPane;

    @FXML
    public void initialize() {
        rootPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    protected void handleBackButton() {
        Main.removeNode(1);
    }

    @FXML
    protected void handleAuthor1Button() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/yungen-lu"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleAuthor2Button() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Darth-Phoenix"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleAuthor3Button() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/LinBoRui"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

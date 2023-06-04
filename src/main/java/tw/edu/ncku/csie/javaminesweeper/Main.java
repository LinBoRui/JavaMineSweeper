package tw.edu.ncku.csie.javaminesweeper;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static StackPane rootPane = new StackPane();
    private Scene rootScene;

    @Override
    public void start(Stage stage) {
        try {
            VBox node = (VBox) loadFxml("startscene");
            node.setStyle("-fx-background-color: white");
            rootPane.getChildren().add(node);
            this.rootScene = new Scene(rootPane);
            MFXThemeManager.addOn(this.rootScene, Themes.DEFAULT);
            stage.setScene(this.rootScene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Parent loadFxml(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void addFxml(String fxml) throws IOException {

        addNode(loadFxml(fxml));
    }

    public static void addNode(Node node) {
        rootPane.getChildren().add(node);
    }

    public static void removeNode(int idx) {
        rootPane.getChildren().remove(rootPane.getChildren().size() - idx);
    }

    public static void main(String[] args) {
        launch();
    }
}

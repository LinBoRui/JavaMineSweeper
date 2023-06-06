package tw.edu.ncku.csie.javaminesweeper;

import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RankSceneController {

    @FXML
    FlowPane rankPane;

    @FXML
    Label difficultyLabel;

    @FXML
    VBox rankListVBox;

    @FXML
    private void initialize() {
        rankPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
        difficultyLabel.setText("Difficulty: " + Level.getLevelString());
        List<RankItem> rankList = Rank.getRankList();
        rankListVBox.getChildren().clear();
        for (int i = 0; i < rankList.size(); i++) {
            rankListVBox.getChildren().add(createAnchorPane(rankList.get(i), i + 1));
        }
    }
    
    private AnchorPane createAnchorPane(RankItem rankItem, int idx) {
        AnchorPane anchorPane = new AnchorPane();
        Label timeLabel = new Label(rankItem.getTimeString());
        Label dateLabel = new Label(idx + ". " + rankItem.getDate());
        AnchorPane.setLeftAnchor(dateLabel, 0.0);
        AnchorPane.setRightAnchor(timeLabel, 0.0);
        anchorPane.getChildren().addAll(dateLabel, timeLabel);
        return anchorPane;
    }
    
    @FXML
    private void rankButtonClick() {
        Main.removeNode(1);
    }
}

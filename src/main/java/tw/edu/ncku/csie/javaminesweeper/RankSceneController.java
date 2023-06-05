package tw.edu.ncku.csie.javaminesweeper;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class RankSceneController {

    @FXML
    Label difficultyLabel;

    @FXML
    VBox rankListVBox;

    @FXML
    public void initialize() {
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
    private void goMenu() {
        Main.removeNode(1);
        Main.removeNode(1);
    }
}

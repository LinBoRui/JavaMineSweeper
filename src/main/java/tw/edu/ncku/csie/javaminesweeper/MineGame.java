package tw.edu.ncku.csie.javaminesweeper;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineGame extends Pane {
    private static int TILE_SIZE = 40;
    private static final int W = 400;
    private static final int H = 400;
    private static int X_TILES = W / TILE_SIZE;
    private static int Y_TILES = H / TILE_SIZE;
    private static int TOTAL_BOMB = 0;
    private Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private ArrayList<Tile> bombs = new ArrayList<>();
    private ArrayList<Tile> flags = new ArrayList<>();
    private boolean init = false;
    private boolean isDone = false;
    private int flagCount = 0;
    private int openedCount = 0;

    private int seconds = 0;
    private Label timerLabel;
    private Timeline timeline;


    MineGame() {
        Task<int[]> task = new Task<int[]>() {
            @Override
            public int[] call() {
                setMaxSize(W, H);
                switch (Level.currLevel) {
                    case EASY -> {
                        TILE_SIZE = 40;
                        TOTAL_BOMB = 10;
                        X_TILES = W / TILE_SIZE;
                        Y_TILES = H / TILE_SIZE;
                        grid = new Tile[X_TILES][Y_TILES];
                    }
                    case MEDIUM -> {
                        TILE_SIZE = 25;
                        TOTAL_BOMB = 40;
                        X_TILES = W / TILE_SIZE;
                        Y_TILES = H / TILE_SIZE;
                        grid = new Tile[X_TILES][Y_TILES];
                    }
                    case HARD -> {
                        TILE_SIZE = 20;
                        TOTAL_BOMB = 90;
                        X_TILES = W / TILE_SIZE;
                        Y_TILES = H / TILE_SIZE;
                        grid = new Tile[X_TILES][Y_TILES];
                    }
                }
                for (int y = 0; y < Y_TILES; y++) {
                    for (int x = 0; x < X_TILES; x++) {
                        Tile tile = new Tile(x, y, TILE_SIZE);
                        grid[x][y] = tile;
                        updateValue(new int[]{x, y});
                    }
                }
                return null;
            }
        };
        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                for (int y = 0; y < Y_TILES; y++) {
                    for (int x = 0; x < X_TILES; x++) {
                        grid[x][y].setDisable(false);
                    }
                }
                return;
            }
            int[] oldloc = (oldValue != null)? (int[]) oldValue : new int[]{-1, 0};
            int[] newloc = (int[]) newValue;
            for (int i = oldloc[1]; i <= newloc[1]; i++) {
                for (int j = 0; j < X_TILES; j++) {
                    if (i == oldloc[1] && j <= oldloc[0])
                        continue;
                    else if (i == newloc[1] && j > newloc[0])
                        break;
                    grid[j][i].setDisable(true);
                    getChildren().add(grid[j][i]);
                }
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private void createBomb(int initX, int initY) {
        int bombPlaced = 0;
        Random random = new Random();

        while (bombPlaced < TOTAL_BOMB) {
            int x = random.nextInt(X_TILES);
            int y = random.nextInt(Y_TILES);
            if (grid[x][y].type != Tile.TileType.BOMB && !(Math.abs(x - initX) <= 1 && Math.abs(y - initY) <= 1)) {
                this.grid[x][y].addBombToButton();
                this.bombs.add(this.grid[x][y]);
                bombPlaced++;
            }
        }

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = grid[x][y];

                if (tile.type == Tile.TileType.BOMB)
                    continue;
                int bombCount = (int) getNeighbors(tile).stream().filter(tile1 -> tile1.type == Tile.TileType.BOMB).count();

                if (bombCount > 0) {
                    tile.addNumberToButton(bombCount);
                }
            }
        }
    }

    public List<Tile> getNeighbors(Tile tile) {
        List<Tile> neighbors = new ArrayList<>();

        int[] xNext = new int[]{-1, 0, 1, 1, 1, 0, -1, -1};
        int[] yNext = new int[]{1, 1, 1, 0, -1, -1, -1, 0};
        for (int i = 0; i < 8; i++) {
            int newX = tile.x + xNext[i];
            int newY = tile.y + yNext[i];
            if (newX >= 0 && newX < X_TILES && newY >= 0 && newY < Y_TILES) {
                neighbors.add(this.grid[newX][newY]);
            }
        }
        return neighbors;
    }

    private class Tile extends StackPane {
        private int x, y;
        private TileType type = TileType.NONE;
        private boolean isOpen = false;

        private MFXButton button;
        private MFXFontIcon icon;
        private int number;
        private double size;
        private BooleanProperty isFlag = new SimpleBooleanProperty(false);
        //        private boolean isFlag = false;
        private MFXFontIcon flagIcon = new MFXFontIcon("fas-flag");


        enum TileType {
            NONE, BOMB, NUM
        }

        public Tile(int x, int y, double size) {
            this.x = x;
            this.y = y;
            this.size = size;

            this.flagIcon.setSize(size / 2.0);

            this.icon = new MFXFontIcon("fas-0");
            this.icon.setVisible(false);
            this.button = new MFXButton("", this.flagIcon);
            this.button.setStyle("-fx-background-color: MediumPurple");
//            this.button.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            this.button.setPrefSize(this.size, this.size);
            this.button.setMaxSize(this.size, this.size);
            this.button.setPadding(new Insets(0, 0, 0, 0));
            this.button.setOnMouseClicked(this::open);

            this.flagIcon.visibleProperty().bind(this.isFlag);
//            this.flagIcon.setSize(this.size);

            this.setStyle("-fx-background-color: gray");
//            this.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            Color lineColor = Color.WHITE;
            double lineWidth = 1.0;
            Line topLine = new Line();
            topLine.setStartX(0);
            topLine.setStartY(0);
            topLine.setEndX(TILE_SIZE - TILE_SIZE / 4.0);
            topLine.setEndY(0);
            topLine.setStroke(lineColor);
            topLine.setStrokeWidth(lineWidth);
            topLine.setTranslateY(TILE_SIZE / 2.0);

            Line rightLine = new Line();
            rightLine.setStartX(0);
            rightLine.setStartY(0);
            rightLine.setEndX(0);
            rightLine.setEndY(TILE_SIZE - TILE_SIZE / 4.0);
            rightLine.setStroke(lineColor);
            rightLine.setStrokeWidth(lineWidth);
            rightLine.setTranslateX(TILE_SIZE / 2.0);

            Line bottomLine = new Line();
            bottomLine.setStartX(0);
            bottomLine.setStartY(TILE_SIZE);
            bottomLine.setEndX(TILE_SIZE - TILE_SIZE / 4.0);
            bottomLine.setEndY(TILE_SIZE);
            bottomLine.setStroke(lineColor);
            bottomLine.setStrokeWidth(lineWidth);
            bottomLine.setTranslateY(-(TILE_SIZE / 2.0));

            Line leftLine = new Line();
            leftLine.setStartX(TILE_SIZE);
            leftLine.setStartY(0);
            leftLine.setEndX(TILE_SIZE);
            leftLine.setEndY(TILE_SIZE - TILE_SIZE / 4.0);
            leftLine.setStroke(lineColor);
            leftLine.setStrokeWidth(lineWidth);
            leftLine.setTranslateX(-(TILE_SIZE / 2.0));
            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);
            this.getChildren().addAll(this.button, topLine, rightLine, bottomLine, leftLine);

        }

        public void addNumberToButton(int number) {
            this.type = TileType.NUM;
            this.number = number;
            this.icon = new MFXFontIcon(String.format("fas-%d", number));
            this.icon.setVisible(false);
            this.icon.setSize(size / 2.0);
            StackPane p = new StackPane(this.flagIcon, this.icon);
            this.button = new MFXButton("", p);
            this.button.setPadding(new Insets(0, 0, 0, 0));
//            this.button = new MFXButton("", this.icon);
            this.button.setOnMouseClicked(this::open);
            this.button.setStyle("-fx-background-color: MediumPurple");
//            this.button.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            this.button.setPrefSize(this.size, this.size);
            this.button.setMaxSize(this.size, this.size);
            this.getChildren().set(0, this.button);

        }

        public void addBombToButton() {
            this.type = TileType.BOMB;
            this.icon = new MFXFontIcon("fas-bomb");
            this.icon.setVisible(false);

            StackPane p = new StackPane(this.flagIcon, this.icon);
            this.button = new MFXButton("", p);
            this.button.setPadding(new Insets(0, 0, 0, 0));
            this.button.setOnMouseClicked(this::open);
            this.button.setStyle("-fx-background-color: MediumPurple");
//            this.button.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            this.button.setPrefSize(this.size, this.size);
            this.button.setMaxSize(this.size, this.size);

            this.getChildren().set(0, this.button);
        }

        public void easyDig(Tile t) {
            List<Tile> l = getNeighbors(this);
            int flagCount = (int) l.stream().filter(tile1 -> tile1.isFlag.get()).count();
            if (this.number == flagCount) {
                bfs(this.x, this.y);
                for (Tile i : l) {
                    if (!i.isFlag.get() && i.type == TileType.BOMB) {
                        stopTimer();
                        i.icon.setVisible(true);
                        i.button.setStyle("-fx-background-color: red;");
                        i.isOpen = true;
                        isDone = true;
                    }
                }
                if (isDone) {
                    showBombs();
                }
            }
        }

        public void easyFlag(Tile t) {
            List<Tile> l = getNeighbors(this);
            int blankCount = (int) l.stream().filter(tile1 -> !tile1.isOpen).count();
            if (this.number == blankCount) {
                for (Tile i : l) {
                    if (!i.isOpen && !i.isFlag.get()) {
                        i.isFlag.set(true);
                        flagCount++;
                        flags.add(i);
                        i.button.setStyle("-fx-background-color: LightGray;");
                        setBombCount(TOTAL_BOMB-flagCount);
                    }
                }
            }
        }

        public void open(MouseEvent e) {
//            System.out.println(this.type);
            if (!init) {
                if (e.getButton() == MouseButton.SECONDARY ^ Setting.getDefaultClick()) return;
                init = true;
                createBomb(this.x, this.y);
                startTimer();
            }
            if (isDone) {
                return;
            }
            if (this.isOpen) {
                if ((e.getButton() == MouseButton.PRIMARY ^ Setting.getDefaultClick()) && this.type == TileType.NUM) {
                    if (Setting.isEasyFlag()) {
                        easyFlag(this);
                    }
                    if (Setting.isEasyDig()) {
                        easyDig(this);
                    }
                }
            }
            else if ((e.getButton() == MouseButton.PRIMARY ^ Setting.getDefaultClick()) && !this.isFlag.get()) {
                switch (this.type) {
                    case NONE -> {
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                        openedCount++;
                        bfs(this.x, this.y);
                    }
                    case NUM -> {
                        this.icon.setVisible(true);
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                        openedCount++;
                    }
                    case BOMB -> {
                        stopTimer();
                        this.icon.setVisible(true);
                        showBombs();
                        this.button.setStyle("-fx-background-color: red;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                        isDone = true;
                    }
                }
            }
            else if ((e.getButton() == MouseButton.SECONDARY ^ Setting.getDefaultClick())) {
                if (!this.isOpen) {
                    this.isFlag.set(!this.isFlag.get());
                    if (this.isFlag.get()) {
                        flagCount++;
                        flags.add(this);
                        this.button.setStyle("-fx-background-color: LightGray;");
                    }
                    else {
                        flagCount--;
                        flags.remove(this);
                        this.button.setStyle("-fx-background-color: MediumPurple;");
                    }
                    setBombCount(TOTAL_BOMB-flagCount);
                }
            }
            if ((X_TILES * Y_TILES) - openedCount == TOTAL_BOMB) {
                System.out.println("done!!!");
                stopTimer();
                for (Tile i:bombs) {
                    if (!i.isFlag.get()) {
                        i.isFlag.set(true);
                    }
                }
                setBombCount(0);
                System.out.println(getTime());
                isDone = true;
                Rank.updateScore(new RankItem(getTime()));
                Rank.showRanking();
            }
        }

//        public void toggle() {
//            if (!this.isFlag.get()) {
//                openedCount++;
//                switch (this.type) {
//                    case NUM, BOMB -> {
//                        this.icon.setVisible(true);
//                        this.button.setStyle("-fx-background-color: white;");
//                        //                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//                        this.isOpen = true;
//                    }
//                    case NONE -> {
//                        this.button.setStyle("-fx-background-color: white;");
//                        //                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//                        this.isOpen = true;
//                        dfs(this.x, this.y);
//                    }
//                }
//            }
//        }

    }

    public void showBombs() {
        for (Tile i : flags) {
            i.isFlag.set(false);
        }
        for (Tile i : this.bombs) {
            i.icon.setVisible(true);
            i.isOpen = true;
        }
    }

    public boolean getInit() {
        return this.init;
    }

    public boolean getDone() {
        return this.isDone;
    }

//    public void dfs(int x, int y) {
//        List<Tile> l = getNeighbors(this.grid[x][y]);
//        for (Tile i : l) {
//            if (!i.isOpen && (i.type == Tile.TileType.NONE || i.type == Tile.TileType.NUM)) {
//                i.toggle();
//            }
//        }
//    }

    public void bfs(int x, int y) {
        Thread thread = new Thread(() -> {
            List<Tile> openList = getNeighbors(this.grid[x][y]);
            Runnable updater = () -> {
                while (openList.size() > 0) {
                    Tile t = openList.remove(0);
                    if (!t.isOpen && !t.isFlag.get()) {
                        openedCount++;
                        t.isOpen = true;
                        t.button.setStyle("-fx-background-color: white;");
                        if (t.type == Tile.TileType.NUM) {
                            t.icon.setVisible(true);
                        }
                        if (t.type == Tile.TileType.NONE) {
                            List<Tile> tmp = getNeighbors(t);
                            openList.addAll(tmp);
                        }
                    }
                }
            };

            while (openList.size() > 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(updater);
            }

        });
        thread.setDaemon(true);
        thread.start();
    }

    public void startTimer() {
        BorderPane parent = (BorderPane) getParent();
        this.timerLabel = (Label) parent.lookup("#timerLabel");
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            this.seconds++;
            updateTimerLabel();
        }));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    public void stopTimer() {
        this.timeline.stop();
    }

    public int getTime() {
        return this.seconds;
    }

    private void updateTimerLabel() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        String timeString = String.format("%02d:%02d", minutes, remainingSeconds);
        this.timerLabel.setText(timeString);
    }

    public void setBombCount(int bombCount) {
        BorderPane parent = (BorderPane) getParent();
        Label bombLabel = (Label) parent.lookup("#bombCount");
        bombLabel.setText(Integer.toString(bombCount));
    }

    public int getTotalBomb() {
        return TOTAL_BOMB;
    }
}
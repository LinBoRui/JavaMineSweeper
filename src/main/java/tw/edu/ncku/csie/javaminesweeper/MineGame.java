package tw.edu.ncku.csie.javaminesweeper;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.css.themes.Themes;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;

import javafx.beans.property.BooleanProperty;

public class MineGame extends Application {
    private static final int TILE_SIZE = 40;
    private static final int W = 400;
    private static final int H = 400;
    private static final int X_TILES = W / TILE_SIZE;
    private static final int Y_TILES = H / TILE_SIZE;

    private Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private Scene scene;
    private boolean init = false;
    private int bombCount = 0;
    private int openedCount = 0;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W, H);


        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y, TILE_SIZE);
                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }
        return root;
    }

    private void createBomb(int initX, int initY) {
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                if (x != initX && y != initY && x != (initX + 1) && x != (initX - 1) && y != (initY + 1) && y != (initY - 1) && Math.random() < 0.1) {
                    this.bombCount++;
                    this.grid[x][y].addBombToButton();
                }
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

        int[] xNext = new int[]{1, -1, 0, 0, 1, -1, -1, 1};
        int[] yNext = new int[]{0, 0, 1, -1, 1, 1, -1, -1};
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

            this.icon = new MFXFontIcon("fas-0");
            this.icon.setVisible(false);
            this.button = new MFXButton("", this.flagIcon);
            this.button.setStyle("-fx-background-color: gray");
//            this.button.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            this.button.setPrefSize(this.size, this.size);
            this.button.setMaxSize(this.size, this.size);
            this.button.setOnMouseClicked(this::open);

            this.flagIcon.visibleProperty().bind(this.isFlag);

            this.setStyle("-fx-background-color: gray");
//            this.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            Color lineColor = Color.WHITE;
            double lineWidth = 2.0;
            Line topLine = new Line();
            topLine.setStartX(0);
            topLine.setStartY(0);
            topLine.setEndX(TILE_SIZE - 10);
            topLine.setEndY(0);
            topLine.setStroke(lineColor);
            topLine.setStrokeWidth(lineWidth);
            topLine.setTranslateY(20);

            Line rightLine = new Line();
            rightLine.setStartX(0);
            rightLine.setStartY(0);
            rightLine.setEndX(0);
            rightLine.setEndY(TILE_SIZE - 10);
            rightLine.setStroke(lineColor);
            rightLine.setStrokeWidth(lineWidth);
            rightLine.setTranslateX(20);
            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);

            this.getChildren().addAll(this.button, topLine, rightLine);

        }

        public void addNumberToButton(int number) {
            this.type = TileType.NUM;
            this.icon = new MFXFontIcon(String.format("fas-%d", number));
            this.icon.setVisible(false);
            StackPane p = new StackPane(this.flagIcon, this.icon);
            this.button = new MFXButton("", p);
//            this.button = new MFXButton("", this.icon);
            this.button.setOnMouseClicked(this::open);
            this.button.setStyle("-fx-background-color: gray");
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
            this.button.setOnMouseClicked(this::open);
            this.button.setStyle("-fx-background-color: gray");
//            this.button.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            this.button.setPrefSize(this.size, this.size);
            this.button.setMaxSize(this.size, this.size);

            this.getChildren().set(0, this.button);
        }

        public void open(MouseEvent e) {
//            System.out.println(this.type);
            if (!init) {
                init = true;
                createBomb(this.x, this.y);
                openedCount++;
            }
            if (e.getButton() == MouseButton.PRIMARY && !this.isFlag.get()) {
                switch (this.type) {
                    case NONE -> {
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                        dfs(this.x, this.y);
                    }
                    case NUM -> {
                        this.icon.setVisible(true);
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                        openedCount++;
                    }
                    case BOMB -> {
                        this.icon.setVisible(true);
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                    }
                }
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                if (!this.isOpen) {
                    this.isFlag.set(!this.isFlag.get());
                }
            }
            if ((X_TILES * Y_TILES) - openedCount == bombCount) {
                System.out.println("done!!!");
            }

        }

        public void toggle() {
            if (!this.isFlag.get()) {
                openedCount++;
                switch (this.type) {
                    case NUM, BOMB -> {
                        this.icon.setVisible(true);
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                    }
                    case NONE -> {
                        this.button.setStyle("-fx-background-color: white;");
//                        this.button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                        this.isOpen = true;
                        dfs(this.x, this.y);
                    }
                }
            }
        }

    }

    public void dfs(int x, int y) {
        List<Tile> l = getNeighbors(this.grid[x][y]);
        for (Tile i : l) {
            if (!i.isOpen && (i.type == Tile.TileType.NONE || i.type == Tile.TileType.NUM)) {
                i.toggle();
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
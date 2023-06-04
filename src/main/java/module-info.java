module tw.edu.ncku.csie.javaminesweeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens tw.edu.ncku.csie.javaminesweeper to javafx.fxml;
    exports tw.edu.ncku.csie.javaminesweeper;
}
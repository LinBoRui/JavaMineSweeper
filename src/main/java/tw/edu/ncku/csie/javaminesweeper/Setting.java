package tw.edu.ncku.csie.javaminesweeper;

import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class Setting {

    private static boolean easyDig = false;
    private static boolean easyFlag = false;
    private static boolean defaultClick = false;

    public static boolean isEasyDig() {
        return easyDig;
    }

    public static boolean isEasyFlag() {
        return easyFlag;
    }

    public static boolean getDefaultClick() {
        return defaultClick;
    }

    @FXML
    private MFXToggleButton easyDig_button, easyFlag_button;
    @FXML
    private MFXRadioButton dig_button, flag_button;
    @FXML
    private Text delay_value;
    @FXML
    private MFXSlider delay_slider;

    @FXML
    public void initialize() {
        flag_button.setSelected(defaultClick);
        easyDig_button.setSelected((easyDig));
        easyFlag_button.setSelected((easyFlag));
    }

    @FXML
    protected void easyDigButtonHandler() {
        if (easyDig_button.isSelected()) {
            easyDig_button.setText("On");
            easyDig = true;
        } else {
            easyDig_button.setText("Off");
            easyDig = false;
        }
    }

    @FXML
    protected void easyFlagButtonHandler() {
        if (easyFlag_button.isSelected()) {
            easyFlag_button.setText("On");
            easyFlag = true;
        } else {
            easyFlag_button.setText("Off");
            easyFlag = false;
        }
    }

    @FXML
    protected void onDefaultToggle() {
        if (dig_button.isSelected()) {
            defaultClick = false;
        } else if (flag_button.isSelected()) {
            defaultClick = true;
        }
    }

    @FXML
    protected void onDelaySliderDrag() {
        delay_value.setText(String.valueOf(Math.round(delay_slider.getValue())) + " ms");
    }

    @FXML
    protected void onReturnMenu(ActionEvent event) throws IOException {
        Main.removeNode(1);
    }
}
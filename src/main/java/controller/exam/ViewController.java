package controller.exam;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class ViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab tbExam;

    @FXML
    private AnchorPane anExam;

    @FXML
    private LineChart<?, ?> chExam;

    @FXML
    private JFXComboBox<?> comboVar;

    @FXML
    void initialize() {
        assert tbExam != null : "fx:id=\"tbExam\" was not injected: check your FXML file 'view.fxml'.";
        assert anExam != null : "fx:id=\"anExam\" was not injected: check your FXML file 'view.fxml'.";
        assert chExam != null : "fx:id=\"chExam\" was not injected: check your FXML file 'view.fxml'.";
        assert comboVar != null : "fx:id=\"comboVar\" was not injected: check your FXML file 'view.fxml'.";

    }
}

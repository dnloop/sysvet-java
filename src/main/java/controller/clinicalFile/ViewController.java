package controller.clinicalFile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private TableView<?> tvFicha;

    @FXML
    private AnchorPane apContent;

    @FXML
    private TableView<?> tvTratamiento;

    @FXML
    private TableView<?> tvHistoria;

    @FXML
    void initialize() {
        assert dpDesde != null : "fx:id=\"dpDesde\" was not injected: check your FXML file 'view.fxml'.";
        assert dpHasta != null : "fx:id=\"dpHasta\" was not injected: check your FXML file 'view.fxml'.";
        assert tvFicha != null : "fx:id=\"tvFicha\" was not injected: check your FXML file 'view.fxml'.";
        assert apContent != null : "fx:id=\"apContent\" was not injected: check your FXML file 'view.fxml'.";
        assert tvTratamiento != null : "fx:id=\"tvTratamiento\" was not injected: check your FXML file 'view.fxml'.";
        assert tvHistoria != null : "fx:id=\"tvHistoria\" was not injected: check your FXML file 'view.fxml'.";

    }
}

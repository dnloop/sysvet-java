package controller.patient;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab tabPaciente;

    @FXML
    private AnchorPane apPaciente;

    @FXML
    private Tab tabFicha;

    @FXML
    private AnchorPane apFicha;

    @FXML
    private Tab tabExamen;

    @FXML
    private AnchorPane apExamen;

    @FXML
    private Tab tabInternacion;

    @FXML
    private AnchorPane apInternacion;

    @FXML
    private Tab tabVacunas;

    @FXML
    private AnchorPane apVacuna;

    @FXML
    private Tab tabDesparasitaciones;

    @FXML
    private AnchorPane apDesparasitaciones;

    @FXML
    void initialize() {
        assert tabPaciente != null : "fx:id=\"tabPaciente\" was not injected: check your FXML file 'main.fxml'.";
        assert apPaciente != null : "fx:id=\"apPaciente\" was not injected: check your FXML file 'main.fxml'.";
        assert tabFicha != null : "fx:id=\"tabFicha\" was not injected: check your FXML file 'main.fxml'.";
        assert apFicha != null : "fx:id=\"apFicha\" was not injected: check your FXML file 'main.fxml'.";
        assert tabExamen != null : "fx:id=\"tabExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert apExamen != null : "fx:id=\"apExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert tabInternacion != null : "fx:id=\"tabInternacion\" was not injected: check your FXML file 'main.fxml'.";
        assert apInternacion != null : "fx:id=\"apInternacion\" was not injected: check your FXML file 'main.fxml'.";
        assert tabVacunas != null : "fx:id=\"tabVacunas\" was not injected: check your FXML file 'main.fxml'.";
        assert apVacuna != null : "fx:id=\"apVacuna\" was not injected: check your FXML file 'main.fxml'.";
        assert tabDesparasitaciones != null : "fx:id=\"tabDesparasitaciones\" was not injected: check your FXML file 'main.fxml'.";
        assert apDesparasitaciones != null : "fx:id=\"apDesparasitaciones\" was not injected: check your FXML file 'main.fxml'.";

    }
}

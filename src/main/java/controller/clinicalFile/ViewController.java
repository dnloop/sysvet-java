package controller.clinicalFile;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import model.FichasClinicas;

public class ViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFecha;

    @FXML
    private JFXTextField txtMotivoConsulta;

    @FXML
    private JFXTextArea txtAnamnesis;

    @FXML
    private JFXTextField txtMed;

    @FXML
    private JFXTextField txtMedAnterior;

    @FXML
    private JFXTextField txtEstNutricion;

    @FXML
    private JFXTextField txtEstSanitario;

    @FXML
    private JFXTextField txtAspectoGeneral;

    @FXML
    private JFXTextField txtDerivaciones;

    @FXML
    private JFXTextArea txtDeterDiagComp;

    @FXML
    private JFXTextArea txtPronostico;

    @FXML
    private JFXTextArea txtDiagnostico;

    @FXML
    private JFXTextArea txtExploracion;

    @FXML
    private JFXTextArea txtEvolucion;

    private FichasClinicas ficha;

    @FXML
    void initialize() {
        assert txtFecha != null : "fx:id=\"txtFecha\" was not injected: check your FXML file 'view.fxml'.";
        assert txtMotivoConsulta != null : "fx:id=\"txtMotivoConsulta\" was not injected: check your FXML file 'view.fxml'.";
        assert txtAnamnesis != null : "fx:id=\"txtAnamnesis\" was not injected: check your FXML file 'view.fxml'.";
        assert txtMed != null : "fx:id=\"txtMed\" was not injected: check your FXML file 'view.fxml'.";
        assert txtMedAnterior != null : "fx:id=\"txtMedAnterior\" was not injected: check your FXML file 'view.fxml'.";
        assert txtEstNutricion != null : "fx:id=\"txtEstNutricion\" was not injected: check your FXML file 'view.fxml'.";
        assert txtEstSanitario != null : "fx:id=\"txtEstSanitario\" was not injected: check your FXML file 'view.fxml'.";
        assert txtAspectoGeneral != null : "fx:id=\"txtAspectoGeneral\" was not injected: check your FXML file 'view.fxml'.";
        assert txtDerivaciones != null : "fx:id=\"txtDerivaciones\" was not injected: check your FXML file 'view.fxml'.";
        assert txtDeterDiagComp != null : "fx:id=\"txtDeterDiagComp\" was not injected: check your FXML file 'view.fxml'.";
        assert txtPronostico != null : "fx:id=\"txtPronostico\" was not injected: check your FXML file 'view.fxml'.";
        assert txtDiagnostico != null : "fx:id=\"txtDiagnostico\" was not injected: check your FXML file 'view.fxml'.";
        assert txtExploracion != null : "fx:id=\"txtExploracion\" was not injected: check your FXML file 'view.fxml'.";
        assert txtEvolucion != null : "fx:id=\"txtEvolucion\" was not injected: check your FXML file 'view.fxml'.";

    }

    public void setObject(FichasClinicas ficha) {
        this.ficha = ficha;
    }

}

package controller.clinicalFile;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import model.FichasClinicas;

public class ViewController {

    @FXML
    private JFXTextField txtFecha;

    @FXML
    private JFXTextField txtMotivoConsulta;

    @FXML
    private JFXTextArea txtAnamnesis;

    @FXML
    private JFXTextField txtMed;

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

        Platform.runLater(() -> {
            txtFecha.setText(ficha.getFecha().toString());
            txtAnamnesis.setText(ficha.getAnamnesis());
            txtAspectoGeneral.setText(ficha.getAspectoGeneral());
            txtDerivaciones.setText(ficha.getDerivaciones());
            txtEstNutricion.setText(ficha.getEstadoNutricion());
            txtDiagnostico.setText(ficha.getDiagnostico());
            txtEstSanitario.setText(ficha.getEstadoSanitario());
            txtEvolucion.setText(ficha.getEvolucion());
            txtExploracion.setText(ficha.getExploracion());
            txtMed.setText(ficha.getMedicacion());
            txtMotivoConsulta.setText(ficha.getMotivoConsulta());
            txtPronostico.setText(ficha.getPronostico());
        });
    }

    public void setObject(FichasClinicas ficha) {
        this.ficha = ficha;
    }

}

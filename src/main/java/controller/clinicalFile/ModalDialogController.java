package controller.clinicalFile;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.PacientesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Pacientes;
import utils.DialogBox;

public class ModalDialogController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXComboBox<Pacientes> comboPA;

    @FXML
    private JFXTextField txtMotivoConsulta;

    @FXML
    private JFXTextArea txtAnamnesis;

    @FXML
    private JFXTextField txtMedActual;

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

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private static PacientesHome daoPA = new PacientesHome();

    private FichasClinicas fichaClinica;

    private Stage stage;

    final ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboPA != null : "fx:id=\"comboPA\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMotivoConsulta != null : "fx:id=\"txtMotivoConsulta\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAnamnesis != null : "fx:id=\"txtAnamnesis\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMedActual != null : "fx:id=\"txtMedActual\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMedAnterior != null : "fx:id=\"txtMedAnterior\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEstNutricion != null : "fx:id=\"txtEstNutricion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEstSanitario != null : "fx:id=\"txtEstSanitario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAspectoGeneral != null : "fx:id=\"txtAspectoGeneral\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDerivaciones != null : "fx:id=\"txtDerivaciones\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDeterDiagComp != null : "fx:id=\"txtDeterDiagComp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPronostico != null : "fx:id=\"txtPronostico\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDiagnostico != null : "fx:id=\"txtDiagnostico\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtExploracion != null : "fx:id=\"txtExploracion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEvolucion != null : "fx:id=\"txtEvolucion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            pacientes.setAll(daoPA.displayRecords());
            log.info("Loading fields");
            comboPA.setItems(pacientes);
            comboPA.getSelectionModel().select(fichaClinica.getPacientes().getId() - 1);
            txtMotivoConsulta.setText(fichaClinica.getMotivoConsulta());
            txtAnamnesis.setText(fichaClinica.getAnamnesis());
            txtMedActual.setText(fichaClinica.getMedicacionActual());
            txtMedAnterior.setText(fichaClinica.getMedicacionAnterior());
            txtEstNutricion.setText(fichaClinica.getEstadoNutricion());
            txtEstSanitario.setText(fichaClinica.getEstadoSanitario());
            txtAspectoGeneral.setText(fichaClinica.getAspectoGeneral());
            txtDerivaciones.setText(fichaClinica.getDerivaciones());
            txtDeterDiagComp.setText(fichaClinica.getDeterDiagComp());
            txtPronostico.setText(fichaClinica.getPronostico());
            txtDiagnostico.setText(fichaClinica.getDiagnostico());
            txtExploracion.setText(fichaClinica.getExploracion());
            txtEvolucion.setText(fichaClinica.getEvolucion());
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void updateRecord() {
        fichaClinica.setPacientes(comboPA.getSelectionModel().getSelectedItem());
        fichaClinica.setMotivoConsulta(txtMotivoConsulta.getText());
        fichaClinica.setAnamnesis(txtAnamnesis.getText());
        fichaClinica.setMedicacionActual(txtMedActual.getText());
        fichaClinica.setMedicacionAnterior(txtMedAnterior.getText());
        fichaClinica.setEstadoNutricion(txtEstNutricion.getText());
        fichaClinica.setEstadoSanitario(txtEstSanitario.getText());
        fichaClinica.setAspectoGeneral(txtAspectoGeneral.getText());
        fichaClinica.setDerivaciones(txtDerivaciones.getText());
        fichaClinica.setDeterDiagComp(txtDeterDiagComp.getText());
        fichaClinica.setPronostico(txtPronostico.getText());
        fichaClinica.setDiagnostico(txtDiagnostico.getText());
        fichaClinica.setExploracion(txtExploracion.getText());
        fichaClinica.setEvolucion(txtEvolucion.getText());
        Date fecha = new Date();
        fichaClinica.setUpdatedAt(fecha);
        daoFC.update(fichaClinica);
        log.info("record updated");
        this.stage.close();
    }

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

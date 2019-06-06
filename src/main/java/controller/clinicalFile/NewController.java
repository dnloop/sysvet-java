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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Pacientes;
import utils.DialogBox;
import utils.HibernateValidator;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnStore;

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

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private PacientesHome daoPA = new PacientesHome();

    private FichasClinicas fichaClinica = new FichasClinicas();

    private Stage stage;

    final ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert btnStore != null : "fx:id=\"btnStore\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert comboPA != null : "fx:id=\"comboPA\" was not injected: check your FXML file 'new.fxml'.";
        assert txtMotivoConsulta != null : "fx:id=\"txtMotivoConsulta\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAnamnesis != null : "fx:id=\"txtAnamnesis\" was not injected: check your FXML file 'new.fxml'.";
        assert txtMedActual != null : "fx:id=\"txtMedActual\" was not injected: check your FXML file 'new.fxml'.";
        assert txtMedAnterior != null : "fx:id=\"txtMedAnterior\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEstNutricion != null : "fx:id=\"txtEstNutricion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEstSanitario != null : "fx:id=\"txtEstSanitario\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAspectoGeneral != null : "fx:id=\"txtAspectoGeneral\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDerivaciones != null : "fx:id=\"txtDerivaciones\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDeterDiagComp != null : "fx:id=\"txtDeterDiagComp\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPronostico != null : "fx:id=\"txtPronostico\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDiagnostico != null : "fx:id=\"txtDiagnostico\" was not injected: check your FXML file 'new.fxml'.";
        assert txtExploracion != null : "fx:id=\"txtExploracion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEvolucion != null : "fx:id=\"txtEvolucion\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        // create list and fill it with dao
        pacientes.setAll(daoPA.displayRecords());

        comboPA.setItems(pacientes);

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnStore.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                storeRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void storeRecord() {
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
        fichaClinica.setCreatedAt(fecha);
        if (HibernateValidator.validate(fichaClinica)) {
            daoFC.add(fichaClinica);
            log.info("record created");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
        }
    }

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

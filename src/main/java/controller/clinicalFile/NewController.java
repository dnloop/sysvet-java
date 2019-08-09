package controller.clinicalFile;

import java.net.URL;
import java.util.Date;
import java.util.List;
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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;

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

    @FXML
    private DatePicker dpFecha;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private PacientesHome daoPA = new PacientesHome();

    private FichasClinicas fichaClinica = new FichasClinicas();

    private Stage stage;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    @FXML
    void initialize() {
        assert btnStore != null : "fx:id=\"btnStore\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert comboPA != null : "fx:id=\"comboPA\" was not injected: check your FXML file 'new.fxml'.";
        assert txtMotivoConsulta != null : "fx:id=\"txtMotivoConsulta\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAnamnesis != null : "fx:id=\"txtAnamnesis\" was not injected: check your FXML file 'new.fxml'.";
        assert txtMed != null : "fx:id=\"txtMed\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEstNutricion != null : "fx:id=\"txtEstNutricion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEstSanitario != null : "fx:id=\"txtEstSanitario\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAspectoGeneral != null : "fx:id=\"txtAspectoGeneral\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDerivaciones != null : "fx:id=\"txtDerivaciones\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDeterDiagComp != null : "fx:id=\"txtDeterDiagComp\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPronostico != null : "fx:id=\"txtPronostico\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDiagnostico != null : "fx:id=\"txtDiagnostico\" was not injected: check your FXML file 'new.fxml'.";
        assert txtExploracion != null : "fx:id=\"txtExploracion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEvolucion != null : "fx:id=\"txtEvolucion\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";

        log.info("Retrieving details");
        loadDao();

        comboPA.setItems(pacientesList);

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
        fichaClinica.setMedicacion(txtMed.getText());
        fichaClinica.setEstadoNutricion(txtEstNutricion.getText());
        fichaClinica.setEstadoSanitario(txtEstSanitario.getText());
        fichaClinica.setAspectoGeneral(txtAspectoGeneral.getText());
        fichaClinica.setDerivaciones(txtDerivaciones.getText());
        fichaClinica.setDeterDiagComp(txtDeterDiagComp.getText());
        fichaClinica.setPronostico(txtPronostico.getText());
        fichaClinica.setDiagnostico(txtDiagnostico.getText());
        fichaClinica.setExploracion(txtExploracion.getText());
        fichaClinica.setEvolucion(txtEvolucion.getText());
        if (dpFecha.getValue() != null)
            fecha = java.sql.Date.valueOf(dpFecha.getValue());

        fichaClinica.setFecha(fecha);
        fecha = new Date();
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
            HibernateValidator.resetError();
            log.error("failed to create record");
        }
    }

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Pacientes>> task = new Task<List<Pacientes>>() {
            @Override
            protected List<Pacientes> call() throws Exception {
                Thread.sleep(500);
                return daoPA.displayRecords();
            }
        };

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            log.info("Loading fields");
            comboPA.setItems(pacientesList);
            comboPA.getSelectionModel().select(fichaClinica.getPacientes().getId() - 1);
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            log.debug("Failed to Query Patient list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

package controller.clinicalFile;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.application.Platform;
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
import utils.viewswitcher.ViewSwitcher;

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

    private FichasClinicas fichaClinica;

    private Stage stage;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    @FXML
    void initialize() {
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboPA != null : "fx:id=\"comboPA\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMotivoConsulta != null : "fx:id=\"txtMotivoConsulta\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAnamnesis != null : "fx:id=\"txtAnamnesis\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMed != null : "fx:id=\"txtMed\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEstNutricion != null : "fx:id=\"txtEstNutricion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEstSanitario != null : "fx:id=\"txtEstSanitario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAspectoGeneral != null : "fx:id=\"txtAspectoGeneral\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDerivaciones != null : "fx:id=\"txtDerivaciones\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDeterDiagComp != null : "fx:id=\"txtDeterDiagComp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPronostico != null : "fx:id=\"txtPronostico\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDiagnostico != null : "fx:id=\"txtDiagnostico\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtExploracion != null : "fx:id=\"txtExploracion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEvolucion != null : "fx:id=\"txtEvolucion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> loadFields()); // Required to prevent NullPointer

        loadDao();

        btnCancel.setOnAction((event) ->

        {
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
        fecha = java.sql.Date.valueOf(dpFecha.getValue());
        fichaClinica.setFecha(fecha);
        fecha = new Date();
        fichaClinica.setUpdatedAt(fecha);
        if (HibernateValidator.validate(fichaClinica)) {
            daoFC.update(fichaClinica);
            log.info("record updated");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error("failed to update record");
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
        Task<List<Pacientes>> task = daoPA.displayRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            log.info("Loading fields");
            comboPA.setItems(pacientesList);
            for (Pacientes paciente : comboPA.getItems())
                if (fichaClinica.getPacientes().getId().equals(paciente.getId())) {
                    comboPA.getSelectionModel().select(paciente);
                    break;
                }
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        log.info("Loading fields.");
        fecha = new Date(fichaClinica.getFecha().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dpFecha.setValue(lfecha);
        txtMotivoConsulta.setText(fichaClinica.getMotivoConsulta());
        txtAnamnesis.setText(fichaClinica.getAnamnesis());
        txtMed.setText(fichaClinica.getMedicacion());
        txtEstNutricion.setText(fichaClinica.getEstadoNutricion());
        txtEstSanitario.setText(fichaClinica.getEstadoSanitario());
        txtAspectoGeneral.setText(fichaClinica.getAspectoGeneral());
        txtDerivaciones.setText(fichaClinica.getDerivaciones());
        txtDeterDiagComp.setText(fichaClinica.getDeterDiagComp());
        txtPronostico.setText(fichaClinica.getPronostico());
        txtDiagnostico.setText(fichaClinica.getDiagnostico());
        txtExploracion.setText(fichaClinica.getExploracion());
        txtEvolucion.setText(fichaClinica.getEvolucion());
    }
}

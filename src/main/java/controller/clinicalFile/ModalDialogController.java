package controller.clinicalFile;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private PacientesHome daoPA = new PacientesHome();

    private FichasClinicas fichaClinica;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    @FXML
    void initialize() {

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });
    }

    /*
     * Class Methods
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
            log.info(marker, "Record updated.");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error(marker, "Failed to update record.");
        }
    }

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    }

    public void loadDao() {
        Task<List<Pacientes>> task = daoPA.displayRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            comboPA.setItems(pacientesList);
            for (Pacientes paciente : comboPA.getItems())
                if (fichaClinica.getPacientes().getId().equals(paciente.getId())) {
                    comboPA.getSelectionModel().select(paciente);
                    break;
                }
            initFields();
            log.info(marker, "List Loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        log.info(marker, "Loading fields.");
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

    /**
     * Load the modal fields after the stage starts.
     */
    private void initFields() {
        Platform.runLater(() -> loadFields());
    }
}

package controller.internation;

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

import dao.InternacionesHome;
import dao.PacientesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Internaciones;
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
    private JFXComboBox<Pacientes> comboPaciente;

    @FXML
    private DatePicker dpFechaIngreso;

    @FXML
    private DatePicker dpFechaAlta;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private InternacionesHome dao = new InternacionesHome();

    private PacientesHome daoFC = new PacientesHome();

    private Internaciones internacion;

    private Stage stage;

    private Date fechaIngreso;

    private LocalDate lfechaIngreso;

    private Date fechaAlta;

    private LocalDate lfechaAlta;

    final ObservableList<Pacientes> fichasList = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        Platform.runLater(() -> loadFields()); // TODO Required to prevent null pointer, find alternative

        loadDao();

        btnCancel.setOnAction((event) -> {
            this.stage.close();
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
        // date conversion from LocalDate
        Date fecha = new Date();
        fechaIngreso = java.sql.Date.valueOf(dpFechaIngreso.getValue());
        internacion.setFechaIngreso(fechaIngreso);
        if (dpFechaAlta.getValue() != null) {
            fechaAlta = java.sql.Date.valueOf(dpFechaAlta.getValue());
            internacion.setFechaAlta(fechaAlta);
        }
        internacion.setPacientes(comboPaciente.getSelectionModel().getSelectedItem());
        internacion.setUpdatedAt(fecha);
        if (HibernateValidator.validate(internacion)) {
            dao.update(internacion);
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

    public void setObject(Internaciones internacion) {
        this.internacion = internacion;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Pacientes>> task = daoFC.displayRecords();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            comboPaciente.setItems(fichasList); // to string?
            for (Pacientes paciente : comboPaciente.getItems())
                if (internacion.getPacientes().getId().equals(paciente.getId())) {
                    comboPaciente.getSelectionModel().select(paciente);
                    break;
                }
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        fechaIngreso = new Date(internacion.getFechaIngreso().getTime());
        lfechaIngreso = fechaIngreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (internacion.getFechaAlta() != null) {
            fechaAlta = new Date(internacion.getFechaAlta().getTime());
            lfechaAlta = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        log.info("Loading fields");
        dpFechaIngreso.setValue(lfechaIngreso);
        dpFechaAlta.setValue(lfechaAlta);
    }
}

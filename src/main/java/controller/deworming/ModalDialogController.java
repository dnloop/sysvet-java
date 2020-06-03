package controller.deworming;

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
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
import dao.PacientesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.Desparasitaciones;
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
    private JFXComboBox<Pacientes> comboPatient;

    @FXML
    private JFXTextField txtTreatment;

    @FXML
    private JFXTextField txtType;

    @FXML
    private DatePicker dpDate;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private DatePicker dpNextDate;

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static DesparasitacionesHome daoD = new DesparasitacionesHome();

    private static PacientesHome daoPA = new PacientesHome();

    private Desparasitaciones desparasitacion;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    private Date fechaProxima;

    private LocalDate lfechaProxima;

    @FXML
    void initialize() {

        Platform.runLater(() -> loadFields());

        loadDao();

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
        // date conversion from LocalDate
        fecha = java.sql.Date.valueOf(dpDate.getValue());
        fechaProxima = java.sql.Date.valueOf(dpNextDate.getValue());
        desparasitacion.setFecha(fecha);
        desparasitacion.setTratamiento(txtTreatment.getText());
        desparasitacion.setTipo(txtType.getText());
        desparasitacion.setFechaProxima(fechaProxima);
        fecha = new Date();
        desparasitacion.setUpdatedAt(fecha);
        if (HibernateValidator.validate(desparasitacion)) {
            daoD.update(desparasitacion);
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

    public void setObject(Desparasitaciones desparasitacion) {
        this.desparasitacion = desparasitacion;
    }

    private void loadDao() {
        Task<List<Pacientes>> task = daoPA.displayRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            comboPatient.setItems(pacientesList);
            for (Pacientes paciente : comboPatient.getItems())
                if (desparasitacion.getPacientes().getId().equals(paciente.getId())) {
                    comboPatient.getSelectionModel().select(paciente);
                    break;
                }
            log.info(marker, "List Loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        fecha = new Date(desparasitacion.getFecha().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        fechaProxima = new Date(desparasitacion.getFechaProxima().getTime());
        lfechaProxima = fechaProxima.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txtTreatment.setText(desparasitacion.getTratamiento());
        txtType.setText(desparasitacion.getTipo());
        dpDate.setValue(lfecha);
        dpNextDate.setValue(lfechaProxima);
        log.info(marker, "Fields loaded.");
    }
}

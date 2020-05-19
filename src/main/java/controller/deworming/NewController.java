package controller.deworming;

import java.net.URL;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.validator.Trim;
import utils.viewswitcher.ViewSwitcher;

public class NewController {

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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private DatePicker dpNextDate;

    private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private DesparasitacionesHome daoD = new DesparasitacionesHome();

    private PacientesHome daoPA = new PacientesHome();

    private Desparasitaciones desparasitacion = new Desparasitaciones();

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private Date fechaProxima;

    @FXML
    void initialize() {

        log.info(marker, "Retrieving details");
        loadDao();

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.close();
        });

        btnSave.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                storeRecord();
        });
    }

    /*
     * Class Methods
     */

    private void storeRecord() {
        // date conversion from LocalDate
        if (dpDate.getValue() != null)
            fecha = java.sql.Date.valueOf(dpDate.getValue());

        if (dpNextDate.getValue() != null)
            fechaProxima = java.sql.Date.valueOf(dpNextDate.getValue());

        desparasitacion.setFecha(fecha);
        desparasitacion.setTratamiento(Trim.trim(txtTreatment.getText()));
        desparasitacion.setTipo(Trim.trim(txtType.getText()));
        desparasitacion.setPacientes(comboPatient.getSelectionModel().getSelectedItem());
        desparasitacion.setFechaProxima(fechaProxima);
        fecha = new Date();
        desparasitacion.setCreatedAt(fecha);
        if (HibernateValidator.validate(desparasitacion)) {
            daoD.add(desparasitacion);
            log.info(marker, "record created");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error("failed to create record");
        }
    }

    private void loadDao() {
        Task<List<Pacientes>> task = daoPA.displayRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            comboPatient.setItems(pacientesList);
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }
}

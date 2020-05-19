package controller.hospitalization;

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

import dao.FichasClinicasHome;
import dao.InternacionesHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.Internaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class NewController {
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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private InternacionesHome dao = new InternacionesHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Internaciones internacion = new Internaciones();

    final ObservableList<Pacientes> fichasList = FXCollections.observableArrayList();

    private Date fechaIngreso;

    @FXML
    void initialize() {

        log.info("Retrieving details");
        // create list and fill it with dao
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
        if (dpFechaAlta.getValue() != null) {
            Date fechaAlta = java.sql.Date.valueOf(dpFechaAlta.getValue());
            internacion.setFechaAlta(fechaAlta);
        }
        if (dpFechaIngreso.getValue() != null)
            fechaIngreso = java.sql.Date.valueOf(dpFechaIngreso.getValue());

        internacion.setFechaIngreso(fechaIngreso);
        internacion.setPacientes(comboPaciente.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        internacion.setCreatedAt(fecha);
        if (HibernateValidator.validate(internacion)) {
            dao.add(internacion);
            log.info(marker, "record created");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error(marker, "failed to create record");
        }
    }

    private void loadDao() {
        Task<List<Pacientes>> task = daoFC.displayRecordsWithPatients();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            comboPaciente.setItems(fichasList); // to string?
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }
}

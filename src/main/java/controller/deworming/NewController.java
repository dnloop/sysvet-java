package controller.deworming;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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
import javafx.stage.Stage;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.validator.Trim;

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

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private DesparasitacionesHome daoD = new DesparasitacionesHome();

    private PacientesHome daoPA = new PacientesHome();

    private Desparasitaciones desparasitacion = new Desparasitaciones();

    private Stage stage;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private Date fechaProxima;

    @FXML
    void initialize() {
        assert comboPatient != null : "fx:id=\"comboPatient\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTreatment != null : "fx:id=\"txtTreatment\" was not injected: check your FXML file 'new.fxml'.";
        assert dpDate != null : "fx:id=\"dpDate\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert dpNextDate != null : "fx:id=\"dpNextDate\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        loadDao();

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
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
            log.info("record created");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error("failed to create record");
        }
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
            comboPatient.setItems(pacientesList);
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            log.debug("Failed to Query Patient list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

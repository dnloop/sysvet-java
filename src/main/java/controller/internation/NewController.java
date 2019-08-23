package controller.internation;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
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
import javafx.stage.Stage;
import model.Internaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.validator.HibernateValidator;

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

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private InternacionesHome dao = new InternacionesHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Internaciones internacion = new Internaciones();

    private Stage stage;

    final ObservableList<Pacientes> fichasList = FXCollections.observableArrayList();

    private Date fechaIngreso;

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaIngreso != null : "fx:id=\"dpFechaIngreso\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaAlta != null : "fx:id=\"dpFechaAlta\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        // create list and fill it with dao
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

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Pacientes>> task = new Task<List<Pacientes>>() {
            @Override
            protected List<Pacientes> call() throws Exception {
                Thread.sleep(500);
                return daoFC.displayRecordsWithPatients();
            }
        };

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            comboPaciente.setItems(fichasList); // to string?
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            log.debug("Failed to Query Patient list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

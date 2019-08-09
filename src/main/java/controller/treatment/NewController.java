package controller.treatment;

import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import dao.FichasClinicasHome;
import dao.TratamientosHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Tratamientos;
import utils.DialogBox;
import utils.validator.HibernateValidator;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<FichasClinicas> comboFicha;

    @FXML
    private JFXTextField txtTratamiento;

    @FXML
    private JFXTextField txtProcAdicional;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private JFXTimePicker tpHora;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static TratamientosHome daoTR = new TratamientosHome();

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private Tratamientos tratamiento = new Tratamientos();

    private Stage stage;

    final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    private Date fecha;

    @FXML
    void initialize() {
        assert comboFicha != null : "fx:id=\"comboFicha\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTratamiento != null : "fx:id=\"txtTratamiento\" was not injected: check your FXML file 'new.fxml'.";
        assert txtProcAdicional != null : "fx:id=\"txtProcAdicional\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'new.fxml'.";
        assert tpHora != null : "fx:id=\"tpHora\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

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
        if (dpFecha.getValue() != null) {
            fecha = java.sql.Date.valueOf(dpFecha.getValue());
            tratamiento.setFecha(fecha);
        }
        tratamiento.setTratamiento(txtTratamiento.getText());
        if (tpHora.getValue() != null)
            tratamiento.setHora(Time.valueOf(tpHora.getValue()));
        tratamiento.setFichasClinicas((comboFicha.getSelectionModel().getSelectedItem()));
        fecha = new Date();
        tratamiento.setCreatedAt(fecha);
        if (HibernateValidator.validate(tratamiento)) {
            daoTR.add(tratamiento);
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

    public void setObject(Tratamientos tratamiento) {
        this.tratamiento = tratamiento;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = new Task<List<FichasClinicas>>() {
            @Override
            protected List<FichasClinicas> call() throws Exception {
                Thread.sleep(500);
                return daoFC.displayRecords();
            }
        };

        task.setOnSucceeded(event -> {
            fichasList.setAll(daoFC.displayRecords());
            comboFicha.setItems(fichasList);
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            log.debug("Failed to Query treatment list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

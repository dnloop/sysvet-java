package controller.clinicHistory;

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
import dao.HistoriaClinicaHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.HistoriaClinica;
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
    private JFXComboBox<FichasClinicas> comboFC;

    @FXML
    private JFXTextField txtResultado;

    @FXML
    private JFXTextField txtSecuelas;

    @FXML
    private JFXTextField txtConsideraciones;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaResolucion;

    @FXML
    private JFXTextArea txtDescEvento;

    @FXML
    private JFXTextArea txtComentarios;

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private HistoriaClinicaHome daoCH = new HistoriaClinicaHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private HistoriaClinica historiaClinica = new HistoriaClinica();

    private Stage stage;

    final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    private Date fecha;

    private Date fechaInicio;

    private Date fechaResolucion;

    @FXML
    void initialize() {
        assert btnStore != null : "fx:id=\"btnStore\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert comboFC != null : "fx:id=\"comboHC\" was not injected: check your FXML file 'new.fxml'.";
        assert txtResultado != null : "fx:id=\"txtResultado\" was not injected: check your FXML file 'new.fxml'.";
        assert txtSecuelas != null : "fx:id=\"txtSecuelas\" was not injected: check your FXML file 'new.fxml'.";
        assert txtConsideraciones != null : "fx:id=\"txtConsideraciones\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaResolucion != null : "fx:id=\"dpFechaResolucion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDescEvento != null : "fx:id=\"txtDescEvento\" was not injected: check your FXML file 'new.fxml'.";
        assert txtComentarios != null : "fx:id=\"txtComentarios\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        loadDao();

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
        // date conversion from LocalDate
        fecha = new Date();
        if (dpFechaInicio.getValue() != null)
            fechaInicio = java.sql.Date.valueOf(dpFechaInicio.getValue());

        if (dpFechaResolucion.getValue() != null)
            fechaResolucion = java.sql.Date.valueOf(dpFechaResolucion.getValue());

        historiaClinica.setFechaInicio(fechaInicio);
        historiaClinica.setFechaResolucion(fechaResolucion);
        historiaClinica.setResultado(txtResultado.getText());
        historiaClinica.setSecuelas(txtSecuelas.getText());
        historiaClinica.setConsideraciones(txtConsideraciones.getText());
        historiaClinica.setComentarios(txtComentarios.getText());
        historiaClinica.setDescripcionEvento(txtDescEvento.getText());
        historiaClinica.setFichasClinicas(comboFC.getSelectionModel().getSelectedItem());
        historiaClinica.setCreatedAt(fecha);
        if (HibernateValidator.validate(historiaClinica)) {
            daoCH.add(historiaClinica);
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
        Task<List<FichasClinicas>> task = new Task<List<FichasClinicas>>() {
            @Override
            protected List<FichasClinicas> call() throws Exception {
                Thread.sleep(500);
                return daoFC.displayRecords();
            }
        };

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            comboFC.setItems(fichasList);
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            log.debug("Failed to Query clinical files list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

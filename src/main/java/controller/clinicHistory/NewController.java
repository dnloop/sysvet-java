package controller.clinicHistory;

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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.FichasClinicas;
import model.HistoriaClinica;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

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

    private static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private HistoriaClinicaHome daoCH = new HistoriaClinicaHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private HistoriaClinica historiaClinica = new HistoriaClinica();

    private final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    private Date fecha;

    private Date fechaInicio;

    private Date fechaResolucion;

    @FXML
    void initialize() {
        log.info(marker, "Retrieving details");
        loadDao();

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.close();
        });

        btnStore.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                storeRecord();
        });
    }

    /*
     * Class Methods
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
        Task<List<FichasClinicas>> task = daoFC.displayRecords();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            comboFC.setItems(fichasList);
            log.info(marker, "Loaded Item.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }
}

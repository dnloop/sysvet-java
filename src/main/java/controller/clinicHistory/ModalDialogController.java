package controller.clinicHistory;

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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import javafx.application.Platform;
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
import utils.viewswitcher.ViewSwitcher;

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXComboBox<FichasClinicas> comboFC;

    @FXML
    private JFXTextArea txtDescEvento;

    @FXML
    private JFXTextField txtResultado;

    @FXML
    private JFXTextField txtSecuelas;

    @FXML
    private JFXTextField txtConsideraciones;

    @FXML
    private JFXTextArea txtComentarios;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaResolucion;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private HistoriaClinicaHome daoCH = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private Stage stage;

    final ObservableList<FichasClinicas> fichasList = FXCollections.observableArrayList();

    private Date fechaResolucion;

    private LocalDate lfechaResolucion;

    private Date fechaInicio;

    private LocalDate lfechaInicio;

    @FXML
    void initialize() {

        Platform.runLater(() -> loadingFields()); // TODO Required to prevent NullPointer, find alternative

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

    /**
     * Extract the fields to create a clinic history object used to update the
     * database.
     */
    private void updateRecord() {
        // date conversion from LocalDate
        fechaResolucion = java.sql.Date.valueOf(dpFechaResolucion.getValue());
        fechaInicio = java.sql.Date.valueOf(dpFechaInicio.getValue());
        fechaResolucion = new Date(); // recycling
        historiaClinica.setFechaResolucion(fechaResolucion);
        historiaClinica.setFechaInicio(fechaInicio);
        historiaClinica.setResultado(txtResultado.getText());
        historiaClinica.setSecuelas(txtSecuelas.getText());
        historiaClinica.setConsideraciones(txtConsideraciones.getText());
        historiaClinica.setComentarios(txtComentarios.getText());
        historiaClinica.setDescripcionEvento(txtDescEvento.getText());
        historiaClinica.setFichasClinicas(comboFC.getSelectionModel().getSelectedItem());
        historiaClinica.setUpdatedAt(fechaResolucion);
        if (HibernateValidator.validate(historiaClinica)) {
            daoCH.update(historiaClinica);
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

    public void setObject(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = daoFC.displayRecords();

        task.setOnSucceeded(event -> {
            fichasList.setAll(task.getValue());
            comboFC.setItems(fichasList);
            for (FichasClinicas ficha : comboFC.getItems())
                if (historiaClinica.getFichasClinicas().getPacientes().getId().equals(ficha.getPacientes().getId())) {
                    comboFC.getSelectionModel().select(ficha);
                    break;
                }
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadingFields() {
        log.info("Loading fields.");
        // create list and fill it with dao
        fechaResolucion = new Date(historiaClinica.getFechaResolucion().getTime());
        lfechaResolucion = fechaResolucion.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        fechaInicio = new Date(historiaClinica.getFechaInicio().getTime());
        lfechaInicio = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        log.info("Loading fields");
        dpFechaResolucion.setValue(lfechaResolucion);
        dpFechaInicio.setValue(lfechaInicio);
        txtResultado.setText(historiaClinica.getResultado());
        txtSecuelas.setText(historiaClinica.getSecuelas());
        txtConsideraciones.setText(historiaClinica.getConsideraciones());
        txtComentarios.setText(historiaClinica.getComentarios());
        txtDescEvento.setText(historiaClinica.getDescripcionEvento());
    }
}

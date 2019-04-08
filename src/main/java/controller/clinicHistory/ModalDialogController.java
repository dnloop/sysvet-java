package controller.clinicHistory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.HistoriaClinica;

public class ModalDialogController {
    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXComboBox<HistoriaClinica> comboHC;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private JFXTextField txtResultado;

    @FXML
    private JFXTextField txtSecuelas;

    @FXML
    private JFXTextField txtConsideraciones;

    @FXML
    private JFXTextField txtComentarios;

    @FXML
    private DatePicker dpFechaResolucion;

    @FXML
    private JFXTextArea txtDescEvento;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    FichasClinicasHome daoFC = new FichasClinicasHome();

    static HistoriaClinicaHome daoCH = new HistoriaClinicaHome();

    private HistoriaClinica historiaClinica;

    private Stage stage;

    void initialize() {
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboHC != null : "fx:id=\"comboFC\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaInicio != null : "fx:id=\"dpFechaInicio\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtResultado != null : "fx:id=\"txtResultado\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtSecuelas != null : "fx:id=\"txtSecuelas\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtConsideraciones != null : "fx:id=\"txtConsideraciones\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaResolucion != null : "fx:id=\"dpFechaResolucion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDescEvento != null : "fx:id=\"txtDescEvento\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtComentarios != null : "fx:id=\"txtComentarios\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            ObservableList<HistoriaClinica> fichasClinicas = FXCollections.observableArrayList();
            fichasClinicas = loadTable(fichasClinicas);
            // sort list elements asc by id
            Comparator<HistoriaClinica> comp = Comparator.comparingInt(HistoriaClinica::getId);
            FXCollections.sort(fichasClinicas, comp);
            // required conversion for datepicker
            log.info("Formatting dates");
            Date fechaInicio = new Date(historiaClinica.getFechaInicio().getTime());
            LocalDate lfechaInicio = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date fechaResolucion = new Date(historiaClinica.getFechaInicio().getTime());
            LocalDate lfechaResolucion = fechaResolucion.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            dpFechaInicio.setValue(lfechaInicio);
            dpFechaResolucion.setValue(lfechaResolucion);
            txtResultado.setText(historiaClinica.getResultado());
            txtSecuelas.setText(historiaClinica.getSecuelas());
            txtConsideraciones.setText(historiaClinica.getConsideraciones());
            txtComentarios.setText(historiaClinica.getComentarios());
            comboHC.setItems(fichasClinicas);
            comboHC.getSelectionModel().select(historiaClinica.getFichasClinicas().getId() - 1);

            log.info("Loading fields");
            txtDescEvento.setText(historiaClinica.getDescripcionEvento());
        });

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (confirmDialog())
                updateRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void updateRecord() {
        // date conversion from LocalDate
        Date fechaInicio = java.sql.Date.valueOf(dpFechaInicio.getValue());
        Date fechaResolucion = java.sql.Date.valueOf(dpFechaResolucion.getValue());
        fechaInicio = new Date(); // recycling
        historiaClinica.setFechaInicio(fechaInicio);
        historiaClinica.setFechaResolucion(fechaResolucion);
        historiaClinica.setUpdatedAt(fechaInicio);
        daoCH.update(historiaClinica);
        log.info("record updated");
        this.stage.close();
    }

    private boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea actualizar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    static ObservableList<HistoriaClinica> loadTable(ObservableList<HistoriaClinica> fichasClinicas) {
        List<HistoriaClinica> list = daoCH.displayRecords();
        for (HistoriaClinica item : list)
            fichasClinicas.add(item);
        return fichasClinicas;
    }

    public void setObject(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

package controller.treatment;

import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import dao.InternacionesHome;
import dao.TratamientosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Internaciones;
import model.Tratamientos;
import utils.DialogBox;

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<Internaciones> comboInternacion;

    @FXML
    private JFXTextField txtTratamiento;

    @FXML
    private JFXTextField txtProcAdicional;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private JFXTimePicker tpHora;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static TratamientosHome daoTR = new TratamientosHome();

    private static InternacionesHome daoIT = new InternacionesHome();

    private Tratamientos tratamiento;

    private Stage stage;

    final ObservableList<Internaciones> tratamientosList = FXCollections.observableArrayList();

    private Date fecha;

    private Time hora;

    private LocalDate lfecha;

    private LocalTime lhora;

    @FXML
    void initialize() {
        assert comboInternacion != null : "fx:id=\"comboInternacion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTratamiento != null : "fx:id=\"txtTratamiento\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtProcAdicional != null : "fx:id=\"txtProcAdicional\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        // Date conversion needed, this should be an utility
        fecha = new Date(tratamiento.getFecha().getTime());
        hora = new Time(tratamiento.getHora().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        lhora = hora.toLocalTime();

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            tratamientosList.setAll(daoIT.displayRecords());

            log.info("Loading fields");
            txtProcAdicional.setText(tratamiento.getProcAdicional());
            txtTratamiento.setText(tratamiento.getTratamiento());
            dpFecha.setValue(lfecha);
            tpHora.setValue(lhora);
            comboInternacion.setItems(tratamientosList);
            comboInternacion.getSelectionModel().select(tratamiento.getInternaciones().getId() - 1);
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
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
        fecha = java.sql.Date.valueOf(dpFecha.getValue());
        tratamiento.setFecha(fecha);
        tratamiento.setTratamiento(txtTratamiento.getText());
        tratamiento.setProcAdicional(txtProcAdicional.getText());
        tratamiento.setHora(Time.valueOf(tpHora.getValue()));
        tratamiento.setInternaciones((comboInternacion.getSelectionModel().getSelectedItem()));
        fecha = new Date();
        tratamiento.setUpdatedAt(fecha);
        daoTR.update(tratamiento);
        log.info("record updated");
        this.stage.close();
    }

    public void setObject(Tratamientos tratamiento) {
        this.tratamiento = tratamiento;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

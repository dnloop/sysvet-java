package controller.treatment;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import dao.FichasClinicasHome;
import dao.TratamientosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.FichasClinicas;
import model.Tratamientos;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class ModalDialogController {

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
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static TratamientosHome daoTR = new TratamientosHome();

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private Tratamientos tratamiento;

    private final ObservableList<FichasClinicas> tratamientosList = FXCollections.observableArrayList();

    private Date fecha;

    private Time hora;

    private LocalDate lfecha;

    private LocalTime lhora;

    @FXML
    void initialize() {

        Platform.runLater(() -> loadFields());

        loadDao();

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.hide();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });
    }

    /*
     * Class Methods
     */

    private void updateRecord() {
        // date conversion from LocalDate
        fecha = java.sql.Date.valueOf(dpFecha.getValue());
        tratamiento.setFecha(fecha);
        tratamiento.setTratamiento(txtTratamiento.getText());
        tratamiento.setHora(Time.valueOf(tpHora.getValue()));
        tratamiento.setFichasClinicas((comboFicha.getSelectionModel().getSelectedItem()));
        fecha = new Date();
        tratamiento.setUpdatedAt(fecha);
        if (HibernateValidator.validate(tratamiento)) {
            daoTR.update(tratamiento);
            log.info(marker, "Record updated.");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro.");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error(marker, "Failed to update record.");
        }
    }

    public void setObject(Tratamientos tratamiento) {
        this.tratamiento = tratamiento;
    }

    private void loadDao() {
        Task<List<FichasClinicas>> task = daoFC.displayRecords();

        task.setOnSucceeded(event -> {
            tratamientosList.setAll(task.getValue());
            comboFicha.setItems(tratamientosList);
            for (FichasClinicas ficha : comboFicha.getItems())
                if (tratamiento.getFichasClinicas().getPacientes().getId().equals(ficha.getPacientes().getId())) {
                    comboFicha.getSelectionModel().select(ficha);
                    break;
                }
            log.info(marker, "Treatment Loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        log.info("Loading fields.");
        // Date conversion needed, this should be an utility
        fecha = new Date(tratamiento.getFecha().getTime());
        hora = new Time(tratamiento.getHora().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        lhora = hora.toLocalTime();
        log.info(marker, "Loading fields");
        txtTratamiento.setText(tratamiento.getTratamiento());
        dpFecha.setValue(lfecha);
        tpHora.setValue(lhora);
    }
}

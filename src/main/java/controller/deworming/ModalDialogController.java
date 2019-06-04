package controller.deworming;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
import dao.PacientesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Desparasitaciones;
import model.Pacientes;
import utils.DialogBox;

public class ModalDialogController {
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
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private DatePicker dpNextDate;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static DesparasitacionesHome daoD = new DesparasitacionesHome();

    private static PacientesHome daoPA = new PacientesHome();

    private Desparasitaciones desparasitacion;

    private Stage stage;

    final ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();

    private Date fecha = new Date(desparasitacion.getFecha().getTime());

    private LocalDate lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    private Date fechaProxima = new Date(desparasitacion.getFechaProxima().getTime());

    private LocalDate lfechaProxima = fechaProxima.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    @FXML
    void initialize() {
        assert comboPatient != null : "fx:id=\"comboPatient\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTreatment != null : "fx:id=\"txtTreatment\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpDate != null : "fx:id=\"dpDate\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpNextDate != null : "fx:id=\"dpNextDate\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            pacientes.setAll(daoPA.displayRecords());

            log.info("Loading fields");
            txtTreatment.setText(desparasitacion.getTratamiento());
            txtType.setText(desparasitacion.getTipo());
            dpDate.setValue(lfecha);
            dpNextDate.setValue(lfechaProxima);
            comboPatient.setItems(pacientes);
            comboPatient.getSelectionModel().select(desparasitacion.getPacientes().getId() - 1);
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
        fecha = java.sql.Date.valueOf(dpDate.getValue());
        fechaProxima = java.sql.Date.valueOf(dpNextDate.getValue());
        desparasitacion.setFecha(fecha);
        desparasitacion.setTratamiento(txtTreatment.getText());
        desparasitacion.setTipo(txtType.getText());
        desparasitacion.setFechaProxima(fechaProxima);
        fecha = new Date();
        desparasitacion.setUpdatedAt(fecha);
        daoD.update(desparasitacion);
        log.info("record updated");
        this.stage.close();
    }

    public void setObject(Desparasitaciones desparasitacion) {
        this.desparasitacion = desparasitacion;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

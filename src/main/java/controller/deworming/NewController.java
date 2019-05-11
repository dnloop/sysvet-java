package controller.deworming;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Desparasitaciones;
import model.Pacientes;

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

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    DesparasitacionesHome daoD = new DesparasitacionesHome();

    static PacientesHome daoPA = new PacientesHome();

    private Desparasitaciones desparasitacion = new Desparasitaciones();

    private Stage stage;

    final ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboPatient != null : "fx:id=\"comboPatient\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTreatment != null : "fx:id=\"txtTreatment\" was not injected: check your FXML file 'new.fxml'.";
        assert dpDate != null : "fx:id=\"dpDate\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert dpNextDate != null : "fx:id=\"dpNextDate\" was not injected: check your FXML file 'new.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            pacientes.setAll(daoPA.displayRecords());
            comboPatient.setItems(pacientes);
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
            if (confirmDialog())
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
        Date fecha = java.sql.Date.valueOf(dpDate.getValue());
        Date fechaProxima = java.sql.Date.valueOf(dpNextDate.getValue());
        desparasitacion.setFecha(fecha);
        desparasitacion.setTratamiento(txtTreatment.getText());
        desparasitacion.setTipo(txtType.getText());
        desparasitacion.setPacientes(comboPatient.getSelectionModel().getSelectedItem());
        desparasitacion.setFechaProxima(fechaProxima);
        fecha = new Date();
        desparasitacion.setCreatedAt(fecha);
        daoD.add(desparasitacion);
        log.info("record created");
        this.stage.close();
    }

    private boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea guardar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

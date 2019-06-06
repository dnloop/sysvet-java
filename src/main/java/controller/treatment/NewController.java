package controller.treatment;

import java.net.URL;
import java.sql.Time;
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
import utils.HibernateValidator;

public class NewController {

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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static TratamientosHome daoTR = new TratamientosHome();

    private static InternacionesHome daoIT = new InternacionesHome();

    private Tratamientos tratamiento;

    private Stage stage;

    final ObservableList<Internaciones> internacionesList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboInternacion != null : "fx:id=\"comboInternacion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTratamiento != null : "fx:id=\"txtTratamiento\" was not injected: check your FXML file 'new.fxml'.";
        assert txtProcAdicional != null : "fx:id=\"txtProcAdicional\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'new.fxml'.";
        assert tpHora != null : "fx:id=\"tpHora\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            internacionesList.setAll(daoIT.displayRecords());
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
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
        Date fecha = java.sql.Date.valueOf(dpFecha.getValue());
        tratamiento.setFecha(fecha);
        tratamiento.setTratamiento(txtTratamiento.getText());
        tratamiento.setProcAdicional(txtProcAdicional.getText());
        tratamiento.setHora(Time.valueOf(tpHora.getValue()));
        tratamiento.setInternaciones((comboInternacion.getSelectionModel().getSelectedItem()));
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
        }
    }

    public void setObject(Tratamientos tratamiento) {
        this.tratamiento = tratamiento;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

package controller.deworming;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

public class ModalDialogController {

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    DesparasitacionesHome daoD = new DesparasitacionesHome();
    PacientesHome daoPA = new PacientesHome(); 

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<Pacientes> comboPatient;

    @FXML
    private JFXTextField txtTreatment;

    @FXML
    private DatePicker dpDate;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private DatePicker dpNextDate;

    private Desparasitaciones desparasitacion;

    private Stage stage;

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
            ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();
            List <Pacientes> list = daoPA.displayRecords();
            for ( Pacientes item : list)
                pacientes.add(item);

            // sort list elements asc by id
            Comparator<Pacientes> comp = Comparator.comparingInt(Pacientes::getId);
            FXCollections.sort(pacientes, comp);
            log.info("Formatting dates");

            // required conversion for datepicker
            Date fecha = new Date(desparasitacion.getFecha().getTime());
            LocalDate lfecha = fecha.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Date fechaProxima = new Date(desparasitacion.getFechaProxima().getTime());
            LocalDate lfechaProxima = fechaProxima.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            log.info("Loading fields");
            txtTreatment.setText(desparasitacion.getTratamiento());
            dpDate.setValue(lfecha);
            dpNextDate.setValue(lfechaProxima);
            comboPatient.setItems(pacientes);
            comboPatient.getSelectionModel().select(
                    desparasitacion.getPacientes().getId() -1
                    ); // arrays starts at 0 =)
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            // date conversion from LocalDate
            Date fecha = java.sql.Date.valueOf( dpDate.getValue());
            Date fechaProxima = java.sql.Date.valueOf( dpNextDate.getValue());
            desparasitacion.setFecha(fecha);
            desparasitacion.setTratamiento(txtTreatment.getText());
            desparasitacion.setFechaProxima(fechaProxima);
            daoD.update(desparasitacion);
            this.stage.close();
        });
    }
    /* Class Methods */
    public void setObject(Desparasitaciones desparasitacion) {
        this.desparasitacion = desparasitacion;
    }

    public void showModal(Stage stage){
        this.stage = stage;
        this.stage.showAndWait();
    }

}

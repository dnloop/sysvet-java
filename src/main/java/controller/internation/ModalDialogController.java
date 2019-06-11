package controller.internation;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import dao.FichasClinicasHome;
import dao.InternacionesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Internaciones;
import model.Pacientes;
import utils.DialogBox;
import utils.HibernateValidator;

public class ModalDialogController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<Pacientes> comboPaciente;

    @FXML
    private DatePicker dpFechaIngreso;

    @FXML
    private DatePicker dpFechaAlta;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private InternacionesHome dao = new InternacionesHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Internaciones internacion;

    private Stage stage;

    private Date fechaIngreso;

    private LocalDate lfechaIngreso;

    private Date fechaAlta;

    private LocalDate lfechaAlta;

    final ObservableList<Pacientes> fichasClinicas = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaIngreso != null : "fx:id=\"dpFechaIngreso\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaAlta != null : "fx:id=\"dpFechaAlta\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            fichasClinicas.setAll(daoFC.displayRecordsWithPatients());

            fechaIngreso = new Date(internacion.getFechaIngreso().getTime());
            lfechaIngreso = fechaIngreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (internacion.getFechaAlta() != null) {
                fechaAlta = new Date(internacion.getFechaAlta().getTime());
                lfechaAlta = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            log.info("Loading fields");
            comboPaciente.setItems(fichasClinicas); // to string?
            comboPaciente.getSelectionModel().select(internacion.getPacientes().getId() - 1);
            dpFechaIngreso.setValue(lfechaIngreso);
            dpFechaAlta.setValue(lfechaAlta);
        });

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
        Date fecha = new Date();
        fechaIngreso = java.sql.Date.valueOf(dpFechaIngreso.getValue());
        internacion.setFechaIngreso(fechaIngreso);
        if (dpFechaAlta.getValue() != null) {
            fechaAlta = java.sql.Date.valueOf(dpFechaAlta.getValue());
            internacion.setFechaAlta(fechaAlta);
        }
        internacion.setPacientes(comboPaciente.getSelectionModel().getSelectedItem());
        internacion.setUpdatedAt(fecha);
        if (HibernateValidator.validate(internacion)) {
            dao.update(internacion);
            log.info("record updated");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error("failed to update record");
        }
    }

    public void setObject(Internaciones internacion) {
        this.internacion = internacion;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

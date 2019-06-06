package controller.vaccine;

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

import dao.PacientesHome;
import dao.VacunasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Pacientes;
import model.Vacunas;
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
    private DatePicker dpFecha;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXTextField txtDesc;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static PacientesHome dao = new PacientesHome();

    private static VacunasHome daoVC = new VacunasHome();

    private Vacunas vacuna;

    private Stage stage;

    final ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDesc != null : "fx:id=\"txtDesc\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            pacientes.setAll(dao.displayRecords());
            // required conversion for datepicker
            log.info("Formatting dates");
            Date fecha = new Date(vacuna.getFecha().getTime());
            LocalDate lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            log.info("Loading fields");
            dpFecha.setValue(lfecha);
            comboPaciente.setItems(pacientes);
            comboPaciente.getSelectionModel().select(vacuna.getPacientes().getId() - 1);
            txtDesc.setText(vacuna.getDescripcion());
        });

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
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
        Date fecha = java.sql.Date.valueOf(dpFecha.getValue());
        vacuna.setFecha(fecha);
        vacuna.setDescripcion(txtDesc.getText());
        fecha = new Date(); // recycling
        vacuna.setUpdatedAt(fecha);
        if (HibernateValidator.validate(vacuna)) {
            daoVC.update(vacuna);
            log.info("record updated");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
        }
    }

    public void setObject(Vacunas vacuna) {
        this.vacuna = vacuna;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

package controller.vaccine;

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
import com.jfoenix.controls.JFXTextField;

import dao.PacientesHome;
import dao.VacunasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Pacientes;
import model.Vacunas;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

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
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXTextField txtDesc;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static PacientesHome dao = new PacientesHome();

    private static VacunasHome daoVC = new VacunasHome();

    private Vacunas vacuna;

    private Stage stage;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDesc != null : "fx:id=\"txtDesc\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> loadFields()); // Required to prevent NullPointer

        loadDao();

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
        if (dpFecha.getValue() != null) {
            fecha = java.sql.Date.valueOf(dpFecha.getValue());
            vacuna.setFecha(fecha);
        }
        vacuna.setDescripcion(txtDesc.getText());
        fecha = new Date();
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
            HibernateValidator.resetError();
            log.error("failed to update record");
        }
    }

    public void setObject(Vacunas vacuna) {
        this.vacuna = vacuna;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Pacientes>> task = dao.displayRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            comboPaciente.setItems(pacientesList);
            for (Pacientes paciente : comboPaciente.getItems())
                if (vacuna.getPacientes().getId().equals(paciente.getId())) {
                    comboPaciente.getSelectionModel().select(paciente);
                    break;
                }

            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void loadFields() {
        log.info("Loading fields.");
        // required conversion for datepicker
        log.info("Formatting dates");
        fecha = new Date(vacuna.getFecha().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        log.info("Loading fields");
        dpFecha.setValue(lfecha);
        txtDesc.setText(vacuna.getDescripcion());
    }
}

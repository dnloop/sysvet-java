package controller.vaccine;

import java.net.URL;
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

public class NewController {

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

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private static PacientesHome dao = new PacientesHome();

    private static VacunasHome daoVC = new VacunasHome();

    private Vacunas vacuna = new Vacunas();

    private Stage stage;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDesc != null : "fx:id=\"txtDesc\" was not injected: check your FXML file 'new.fxml'.";

        loadDao();

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                createRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void createRecord() {
        // date conversion from LocalDate
        if (dpFecha.getValue() != null) {
            fecha = java.sql.Date.valueOf(dpFecha.getValue());
            vacuna.setFecha(fecha);
        }
        vacuna.setPacientes(comboPaciente.getSelectionModel().getSelectedItem());
        vacuna.setDescripcion(txtDesc.getText());
        fecha = new Date();
        vacuna.setCreatedAt(fecha);
        if (HibernateValidator.validate(vacuna)) {
            daoVC.add(vacuna);
            log.info("record created");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error("failed to create record");
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
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }
}

package controller.vaccine;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Pacientes;
import model.Vacunas;

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

    @FXML
    void initialize() {
        assert comboPaciente != null : "fx:id=\"comboPaciente\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDesc != null : "fx:id=\"txtDesc\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();
            pacientes = loadTable(pacientes);
            // sort list elements asc by id
            Comparator<Pacientes> comp = Comparator.comparingInt(Pacientes::getId);
            FXCollections.sort(pacientes, comp);
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

        btnAccept.setOnAction((event) -> {
            if (confirmDialog())
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
        daoVC.update(vacuna);
        log.info("record updated");
        this.stage.close();
    }

    private boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea actualizar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    static ObservableList<Pacientes> loadTable(ObservableList<Pacientes> pacientes) {
        List<Pacientes> list = dao.displayRecords();
        for (Pacientes item : list)
            pacientes.add(item);
        return pacientes;
    }

    public void setObject(Vacunas vacuna) {
        this.vacuna = vacuna;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

package controller.internation;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Internaciones;

public class ModalDialogController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<FichasClinicas> comboFicha;

    @FXML
    private DatePicker dpFechaIngreso;

    @FXML
    private DatePicker dpFechaAlta;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static InternacionesHome dao = new InternacionesHome();

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private Internaciones internacion;

    private Stage stage;

    private Date fechaIngreso;

    private LocalDate lfechaIngreso;

    private Date fechaAlta;

    private LocalDate lfechaAlta;

    final ObservableList<FichasClinicas> fichasClinicas = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboFicha != null : "fx:id=\"comboFicha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaIngreso != null : "fx:id=\"dpFechaIngreso\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFechaAlta != null : "fx:id=\"dpFechaAlta\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            fichasClinicas.setAll(daoFC.displayRecords());

            fechaIngreso = new Date(internacion.getFechaIngreso().getTime());
            lfechaIngreso = fechaIngreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (internacion.getFechaAlta() != null) {
                fechaAlta = new Date(internacion.getFechaAlta().getTime());
                lfechaAlta = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            log.info("Loading fields");
            comboFicha.setItems(fichasClinicas); // to string?
            comboFicha.getSelectionModel().select(internacion.getFichasClinicas().getId() - 1);
            dpFechaIngreso.setValue(lfechaIngreso);
            dpFechaAlta.setValue(lfechaAlta);
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
        Date fecha = new Date();
        fechaIngreso = java.sql.Date.valueOf(dpFechaIngreso.getValue());
        internacion.setFechaIngreso(fechaIngreso);
        if (dpFechaAlta.getValue() != null) {
            fechaAlta = java.sql.Date.valueOf(dpFechaAlta.getValue());
            internacion.setFechaAlta(fechaAlta);
        }
        internacion.setFichasClinicas(comboFicha.getSelectionModel().getSelectedItem());
        internacion.setUpdatedAt(fecha);
        dao.update(internacion);
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

    public void setObject(Internaciones internacion) {
        this.internacion = internacion;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

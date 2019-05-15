package controller.internation;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import controller.exam.ModalDialogController;
import dao.FichasClinicasHome;
import dao.InternacionesHome;
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

public class NewController {
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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static InternacionesHome dao = new InternacionesHome();

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private Internaciones internacion = new Internaciones();

    private Stage stage;

    final ObservableList<FichasClinicas> fichasClinicas = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboFicha != null : "fx:id=\"comboFicha\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaIngreso != null : "fx:id=\"dpFechaIngreso\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaAlta != null : "fx:id=\"dpFechaAlta\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        // create list and fill it with dao
        fichasClinicas.setAll(daoFC.displayRecords());
        comboFicha.setItems(fichasClinicas);

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

    private void storeRecord() {
        // date conversion from LocalDate
        if (dpFechaAlta.getValue() != null) {
            Date fechaAlta = java.sql.Date.valueOf(dpFechaAlta.getValue());
            internacion.setFechaAlta(fechaAlta);
        }
        Date fechaIngreso = java.sql.Date.valueOf(dpFechaIngreso.getValue());
        internacion.setFechaIngreso(fechaIngreso);
        internacion.setFichasClinicas(comboFicha.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        internacion.setCreatedAt(fecha);
        dao.add(internacion);
        log.info("record created");
        this.stage.close();
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

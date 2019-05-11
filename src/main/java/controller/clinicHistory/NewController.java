package controller.clinicHistory;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import controller.exam.ModalDialogController;
import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.HistoriaClinica;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnStore;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXComboBox<FichasClinicas> comboFC;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private JFXTextField txtResultado;

    @FXML
    private JFXTextField txtSecuelas;

    @FXML
    private JFXTextField txtConsideraciones;

    @FXML
    private DatePicker dpFechaResolucion;

    @FXML
    private JFXTextArea txtDescEvento;

    @FXML
    private JFXTextArea txtComentarios;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static HistoriaClinicaHome daoCH = new HistoriaClinicaHome();

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private HistoriaClinica historiaClinica = new HistoriaClinica();

    private Stage stage;

    final ObservableList<FichasClinicas> propietarios = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert btnStore != null : "fx:id=\"btnStore\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert comboFC != null : "fx:id=\"comboHC\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaInicio != null : "fx:id=\"dpFechaInicio\" was not injected: check your FXML file 'new.fxml'.";
        assert txtResultado != null : "fx:id=\"txtResultado\" was not injected: check your FXML file 'new.fxml'.";
        assert txtSecuelas != null : "fx:id=\"txtSecuelas\" was not injected: check your FXML file 'new.fxml'.";
        assert txtConsideraciones != null : "fx:id=\"txtConsideraciones\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFechaResolucion != null : "fx:id=\"dpFechaResolucion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDescEvento != null : "fx:id=\"txtDescEvento\" was not injected: check your FXML file 'new.fxml'.";
        assert txtComentarios != null : "fx:id=\"txtComentarios\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        // create list and fill it with dao
        propietarios.setAll(daoFC.displayRecords());
        comboFC.setItems(propietarios);

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnStore.setOnAction((event) -> {
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
        Date fecha = new Date();
        Date fechaInicio = java.sql.Date.valueOf(dpFechaInicio.getValue());
        Date fechaResolucion = java.sql.Date.valueOf(dpFechaResolucion.getValue());
        historiaClinica.setFechaInicio(fechaInicio);
        historiaClinica.setFechaResolucion(fechaResolucion);
        historiaClinica.setResultado(txtResultado.getText());
        historiaClinica.setSecuelas(txtSecuelas.getText());
        historiaClinica.setConsideraciones(txtConsideraciones.getText());
        historiaClinica.setComentarios(txtComentarios.getText());
        historiaClinica.setDescripcionEvento(txtDescEvento.getText());
        historiaClinica.setFichasClinicas(comboFC.getSelectionModel().getSelectedItem());
        historiaClinica.setCreatedAt(fecha);
        daoCH.add(historiaClinica);
        log.info("record created");
        this.stage.close();
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

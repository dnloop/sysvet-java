package controller.currentAccount;

import java.math.BigDecimal;
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
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
import dao.PropietariosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.CuentasCorrientes;
import model.Propietarios;

public class ModalDialogController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<Propietarios> comboPropietario;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private DatePicker dpDate;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static CuentasCorrientesHome daoCC = new CuentasCorrientesHome();

    private static PropietariosHome daoPO = new PropietariosHome();

    private CuentasCorrientes cuentaCorriente;

    private Stage stage;

    final ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();

    private Date fecha = new Date(cuentaCorriente.getFecha().getTime());

    private LocalDate lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    @FXML
    void initialize() {
        assert comboPropietario != null : "fx:id=\"comboPropietario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDescription != null : "fx:id=\"txtDescripcion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAmount != null : "fx:id=\"txtMonto\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpDate != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            propietarios.setAll(daoPO.displayRecords());
            log.info("Loading fields");
            txtDescription.setText(cuentaCorriente.getDescripcion());
            txtAmount.setText(cuentaCorriente.getMonto().toString());
            dpDate.setValue(lfecha);
            comboPropietario.setItems(propietarios);
            comboPropietario.getSelectionModel().select(cuentaCorriente.getPropietarios().getId() - 1);
        }); // required to prevent NullPointer

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

    private void updateRecord() {
        // date conversion from LocalDate
        fecha = java.sql.Date.valueOf(dpDate.getValue());
        cuentaCorriente.setFecha(fecha);
        cuentaCorriente.setDescripcion(txtDescription.getText());
        cuentaCorriente.setMonto(new BigDecimal(txtAmount.getText()));
        cuentaCorriente.setPropietarios(comboPropietario.getSelectionModel().getSelectedItem());
        fecha = new Date();
        cuentaCorriente.setUpdatedAt(fecha);
        daoCC.update(cuentaCorriente);
        log.info("record updated");
        this.stage.close();
    }

    public void setObject(CuentasCorrientes cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}
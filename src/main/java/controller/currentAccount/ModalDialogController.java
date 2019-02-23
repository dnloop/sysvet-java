package controller.currentAccount;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
import dao.PropietariosHome;

import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.CuentasCorrientes;
import model.Propietarios;

public class ModalDialogController {

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    CuentasCorrientesHome daoCC = new CuentasCorrientesHome();
    PropietariosHome daoPO = new PropietariosHome(); 

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<Propietarios> comboPropietario;

    @FXML
    private JFXTextField txtDescripcion;

    @FXML
    private JFXTextField txtMonto;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private JFXButton btnAceptar;

    @FXML
    private JFXButton btnCancelar;

    private CuentasCorrientes cuentaCorriente;

    @FXML
    void initialize() {
        assert comboPropietario != null : "fx:id=\"comboPropietario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDescripcion != null : "fx:id=\"txtDescripcion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMonto != null : "fx:id=\"txtMonto\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAceptar != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancelar != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {

            ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();
            List <Propietarios> list = daoPO.displayRecords();
            for ( Propietarios item : list)
                propietarios.add(item);

            log.info("Loading fields");
            txtDescripcion.setText(cuentaCorriente.getDescripcion()
                    );
            txtMonto.setText(
                    cuentaCorriente.getMonto().toString()
                    );
            dpFecha.setValue(
                    cuentaCorriente.getFecha()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    );
            comboPropietario.setItems(propietarios);
            comboPropietario.getSelectionModel().select(
                    cuentaCorriente.getPropietarios()
                    );
        });
    }
    /* Class Methods */
    public void setObject(CuentasCorrientes cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    } // if null use Platform.runLater
}
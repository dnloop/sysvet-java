package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
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

public class NewController {

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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    CuentasCorrientesHome daoCC = new CuentasCorrientesHome();

    static PropietariosHome daoPO = new PropietariosHome();

    private CuentasCorrientes cuentaCorriente = new CuentasCorrientes();

    private Stage stage;

    @FXML
    void initialize() {
        assert comboPropietario != null : "fx:id=\"comboPropietario\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDescription != null : "fx:id=\"txtDescription\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAmount != null : "fx:id=\"txtAmount\" was not injected: check your FXML file 'new.fxml'.";
        assert dpDate != null : "fx:id=\"dpDate\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();
            propietarios = loadTable(propietarios);
            // sort list elements asc by id
            Comparator<Propietarios> comp = Comparator.comparingInt(Propietarios::getId);
            FXCollections.sort(propietarios, comp);
            comboPropietario.setItems(propietarios);

            btnCancel.setOnAction((event) -> {
                this.stage.close();
            });
            btnSave.setOnAction((event) -> {
                if (confirmDialog())
                    storeRecord();
            });
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
        Date fecha = java.sql.Date.valueOf(dpDate.getValue());
        cuentaCorriente.setFecha(fecha);
        cuentaCorriente.setDescripcion(txtDescription.getText());
        cuentaCorriente.setMonto(new BigDecimal(txtAmount.getText()));
        cuentaCorriente.setPropietarios(comboPropietario.getSelectionModel().getSelectedItem());
        fecha = new Date();
        cuentaCorriente.setCreatedAt(fecha);
        daoCC.add(cuentaCorriente);
        log.info("record created");
        this.stage.close();
    }

    static ObservableList<Propietarios> loadTable(ObservableList<Propietarios> propietarios) {
        List<Propietarios> list = daoPO.displayRecords();
        for (Propietarios item : list)
            propietarios.add(item);
        return propietarios;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

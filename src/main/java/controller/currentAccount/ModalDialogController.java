package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
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
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private DatePicker dpDate;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    private CuentasCorrientes cuentaCorriente;

    private Stage stage;

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
            ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();
            List <Propietarios> list = daoPO.displayRecords();
            for ( Propietarios item : list)
                propietarios.add(item);
            // sort list elements asc by id
            Comparator<Propietarios> comp = Comparator.comparingInt(Propietarios::getId);
            FXCollections.sort(propietarios, comp);
            log.info("Loading fields");
            // required conversion for datepicker
            Date fecha = new Date(cuentaCorriente.getFecha().getTime());
            LocalDate lfecha = fecha.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            txtDescription.setText(cuentaCorriente.getDescripcion()
                    );

            txtAmount.setText(
                    cuentaCorriente.getMonto().toString()
                    );

            dpDate.setValue(lfecha);

            comboPropietario.setItems(propietarios);
            comboPropietario.getSelectionModel().select(
                    cuentaCorriente.getPropietarios().getId() -1
                    ); // arrays starts at 0 =)
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            // date conversion from LocalDate
            Date fecha = java.sql.Date.valueOf( dpDate.getValue());
            cuentaCorriente.setFecha(fecha);
            cuentaCorriente.setDescripcion(txtDescription.getText());
            cuentaCorriente.setMonto(
                    new BigDecimal(txtAmount.getText())
                    );
            daoCC.update(cuentaCorriente);
            this.stage.close();
        });
    }
    /* Class Methods */
    public void setObject(CuentasCorrientes cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    public void showModal(Stage stage){
        this.stage = stage;
        this.stage.showAndWait();
    }
}
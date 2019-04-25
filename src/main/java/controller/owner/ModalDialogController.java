package controller.owner;

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

import dao.LocalidadesHome;
import dao.PropietariosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.Localidades;
import model.Propietarios;

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtApellido;

    @FXML
    private JFXTextField txtDomicilio;

    @FXML
    private JFXTextField txtTelCel;

    @FXML
    private JFXTextField txtTelFijo;

    @FXML
    private JFXTextField txtMail;

    @FXML
    private JFXComboBox<Localidades> comboLocalidad;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static LocalidadesHome daoLC = new LocalidadesHome();

    private static PropietariosHome daoPO = new PropietariosHome();

    private Propietarios propietario;

    private Stage stage;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtApellido != null : "fx:id=\"txtApellido\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTelCel != null : "fx:id=\"txtTelCel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTelFijo != null : "fx:id=\"txtTelFijo\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMail != null : "fx:id=\"txtMail\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboLocalidad != null : "fx:id=\"comboLocalidad\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            ObservableList<Localidades> localidades = FXCollections.observableArrayList();
            localidades = loadTable(localidades);
            // sort list elements asc by id
            Comparator<Localidades> comp = Comparator.comparingInt(Localidades::getId);
            FXCollections.sort(localidades, comp);

            log.info("Loading fields");
            txtNombre.setText(propietario.getNombre());
            txtApellido.setText(propietario.getApellido());
            txtDomicilio.setText(propietario.getDomicilio());
            txtTelCel.setText(String.valueOf(propietario.getTelCel()));
            txtTelFijo.setText(String.valueOf(propietario.getTelFijo()));
            txtMail.setText(propietario.getMail());
            comboLocalidad.setItems(localidades);
            comboLocalidad.getSelectionModel().select(propietario.getLocalidades().getId() - 1); // arrays starts
                                                                                                 // at 0 =)
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
        propietario.setNombre(txtNombre.getText());
        propietario.setApellido(txtApellido.getText());
        propietario.setDomicilio(txtDomicilio.getText());
        propietario.setLocalidades(comboLocalidad.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        propietario.setUpdatedAt(fecha);
        daoPO.update(propietario);
        log.info("record updated");
        this.stage.close();
    }

    static ObservableList<Localidades> loadTable(ObservableList<Localidades> localidades) {
        List<Localidades> list = daoLC.displayRecords();
        for (Localidades item : list)
            localidades.add(item);
        return localidades;
    }

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

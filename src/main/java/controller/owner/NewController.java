package controller.owner;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.LocalidadesHome;
import dao.PropietariosHome;
import dao.ProvinciasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Localidades;
import model.Propietarios;
import model.Provincias;
import utils.DialogBox;

public class NewController {

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
    private JFXComboBox<Provincias> comboProvincia;

    @FXML
    private JFXComboBox<Localidades> comboLocalidad;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private ProvinciasHome daoPR = new ProvinciasHome();

    private LocalidadesHome daoLC = new LocalidadesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    private Propietarios propietario = new Propietarios();

    private Stage stage;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'new.fxml'.";
        assert txtApellido != null : "fx:id=\"txtApellido\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDomicilio != null : "fx:id=\"txtDomicilio\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTelCel != null : "fx:id=\"txtTelCel\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTelFijo != null : "fx:id=\"txtTelFijo\" was not injected: check your FXML file 'new.fxml'.";
        assert txtMail != null : "fx:id=\"txtMail\" was not injected: check your FXML file 'new.fxml'.";
        assert comboProvincia != null : "fx:id=\"comboProvincia\" was not injected: check your FXML file 'new.fxml'.";
        assert comboLocalidad != null : "fx:id=\"comboLocalidad\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            provincias.setAll(daoPR.displayRecords());
            comboProvincia.setItems(provincias);

            comboProvincia.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null) {
                    localidades.setAll(daoLC.showByProvincia(newValue));
                    comboLocalidad.getItems().clear();
                    comboLocalidad.getItems().setAll(localidades);
                    comboLocalidad.setDisable(false);
                } else {
                    comboLocalidad.getItems().clear();
                    comboLocalidad.setDisable(true);
                }
            });

            btnCancel.setOnAction((event) -> {
                this.stage.close();
            });

            btnSave.setOnAction((event) -> {
                if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                    storeRecord();
            });
        });
    }

    private void storeRecord() {
        propietario.setNombre(txtNombre.getText());
        propietario.setApellido(txtApellido.getText());
        propietario.setDomicilio(txtDomicilio.getText());
        propietario.setLocalidades(comboLocalidad.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        propietario.setCreatedAt(fecha);
        daoPO.add(propietario);
        log.info("record created");
        this.stage.close();
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

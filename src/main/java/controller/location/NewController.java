package controller.location;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.LocalidadesHome;
import dao.ProvinciasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Localidades;
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
    private JFXTextField txtCod_postal;

    @FXML
    private JFXComboBox<Provincias> comboProvincia;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private LocalidadesHome daoLC = new LocalidadesHome();

    private ProvinciasHome daoPR = new ProvinciasHome();

    private Localidades localidad;

    private Stage stage;

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'new.fxml'.";
        assert txtCod_postal != null : "fx:id=\"txtCod_postal\" was not injected: check your FXML file 'new.fxml'.";
        assert comboProvincia != null : "fx:id=\"comboProvincia\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        Platform.runLater(() -> {
            // create list and fill it with dao
            provincias.setAll(daoPR.displayRecords());
            log.info("Loading fields");

            txtNombre.setText(localidad.getNombre());
            txtCod_postal.setText(String.valueOf(localidad.getCodPostal()));
            comboProvincia.setItems(provincias);
            comboProvincia.getSelectionModel().select(localidad.getProvincias().getId() - 1);
        });

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                storeRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void storeRecord() {
        localidad.setNombre(txtNombre.getText());
        localidad.setCodPostal(Integer.valueOf(txtCod_postal.getText()));
        localidad.setProvincias(comboProvincia.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        localidad.setCreatedAt(fecha);
        daoLC.add(localidad);
        log.info("record created");
        this.stage.close();
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

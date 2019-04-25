package controller.location;

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
import dao.ProvinciasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.Localidades;
import model.Provincias;

public class ModalDialogController {

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
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    LocalidadesHome daoLC = new LocalidadesHome();

    static ProvinciasHome daoPR = new ProvinciasHome();

    private Localidades localidad;

    private Stage stage;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtCod_postal != null : "fx:id=\"txtCod_postal\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboProvincia != null : "fx:id=\"comboProvincia\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> {
            // create list and fill it with dao
            ObservableList<Provincias> provincias = FXCollections.observableArrayList();
            provincias = loadTable(provincias);
            // sort list elements asc by id
            Comparator<Provincias> comp = Comparator.comparingInt(Provincias::getId);
            FXCollections.sort(provincias, comp);
            log.info("Loading fields");

            txtNombre.setText(localidad.getNombre());
            txtCod_postal.setText(String.valueOf(localidad.getCodPostal()));
            comboProvincia.setItems(provincias);
            comboProvincia.getSelectionModel().select(localidad.getProvincia().getId() - 1);
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
        localidad.setNombre(txtNombre.getText());
        localidad.setCodPostal(Integer.valueOf(txtCod_postal.getText()));
        localidad.setProvincias(comboProvincia.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        localidad.setUpdatedAt(fecha);
        daoLC.update(localidad);
        log.info("record updated");
        this.stage.close();
    }

    static ObservableList<Provincias> loadTable(ObservableList<Provincias> provincias) {
        List<Provincias> list = daoPR.displayRecords();
        for (Provincias item : list)
            provincias.add(item);
        return provincias;
    }

    public void setObject(Localidades localidad) {
        this.localidad = localidad;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

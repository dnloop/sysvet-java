package controller.location;

import java.net.URL;
import java.util.Date;
import java.util.List;
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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Localidades;
import model.Provincias;
import utils.DialogBox;
import utils.ViewSwitcher;
import utils.validator.HibernateValidator;

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

    private LocalidadesHome daoLC = new LocalidadesHome();

    private ProvinciasHome daoPR = new ProvinciasHome();

    private Localidades localidad;

    private Stage stage;

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtCod_postal != null : "fx:id=\"txtCod_postal\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboProvincia != null : "fx:id=\"comboProvincia\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

        loadDao();

        Platform.runLater(() -> {
            log.info("Loading fields");
            txtNombre.setText(localidad.getNombre());
            txtCod_postal.setText(String.valueOf(localidad.getCodPostal()));
            comboProvincia.setItems(provincias);
            comboProvincia.getSelectionModel().select(localidad.getProvincias().getId() - 1);
        });

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });
    }

    /**
     *
     * Class Methods
     *
     */

    private void updateRecord() {
        localidad.setNombre(txtNombre.getText());
        localidad.setCodPostal(Integer.valueOf(txtCod_postal.getText()));
        localidad.setProvincias(comboProvincia.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        localidad.setUpdatedAt(fecha);
        if (HibernateValidator.validate(localidad)) {
            daoLC.update(localidad);
            log.info("record updated");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error("failed to update record");
        }
    }

    public void setObject(Localidades localidad) {
        this.localidad = localidad;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Provincias>> task = daoPR.displayRecords();

        task.setOnSucceeded(event -> {
            // load items
            provincias.setAll(task.getValue());
            comboProvincia.setItems(provincias);
            comboProvincia.getSelectionModel().select(localidad.getProvincias().getId() - 1);
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}

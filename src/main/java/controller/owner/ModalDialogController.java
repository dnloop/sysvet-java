package controller.owner;

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
import dao.PropietariosHome;
import dao.ProvinciasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Localidades;
import model.Propietarios;
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
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static ProvinciasHome daoPR = new ProvinciasHome();

    private LocalidadesHome daoLC = new LocalidadesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    private Propietarios propietario;

    private Stage stage;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtApellido != null : "fx:id=\"txtApellido\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDomicilio != null : "fx:id=\"txtDomicilio\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTelCel != null : "fx:id=\"txtTelCel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTelFijo != null : "fx:id=\"txtTelFijo\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMail != null : "fx:id=\"txtMail\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboProvincia != null : "fx:id=\"comboProvincia\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboLocalidad != null : "fx:id=\"comboLocalidad\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

        log.info("Retrieving details");
        loadDao();
        Platform.runLater(() -> {
            log.info("Loading fields");
            txtNombre.setText(propietario.getNombre());
            txtApellido.setText(propietario.getApellido());
            txtDomicilio.setText(propietario.getDomicilio());
            txtTelCel.setText(String.valueOf(propietario.getTelCel()));
            txtTelFijo.setText(String.valueOf(propietario.getTelFijo()));
            txtMail.setText(propietario.getMail());
        });

        comboProvincia.valueProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue != null) {
                Task<List<Localidades>> task = daoLC.showByProvincia(newValue);

                task.setOnSucceeded(event -> {
                    localidades.setAll(task.getValue());
                    comboLocalidad.getItems().clear();
                    comboLocalidad.getItems().setAll(localidades);
                    log.info("Loaded Items.");
                });

                ViewSwitcher.getLoadingDialog().showStage();
                ViewSwitcher.getLoadingDialog().setTask(task);
                ViewSwitcher.getLoadingDialog().startTask();

            }
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
        propietario.setNombre(txtNombre.getText());
        propietario.setApellido(txtApellido.getText());
        propietario.setDomicilio(txtDomicilio.getText());
        propietario.setLocalidades(comboLocalidad.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        propietario.setUpdatedAt(fecha);
        if (HibernateValidator.validate(propietario)) {
            daoPO.update(propietario);
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

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Provincias>> task1 = daoPR.displayRecords();

        Task<List<Localidades>> task2 = daoLC.showByProvincia(propietario.getLocalidades().getProvincias());

        task1.setOnSucceeded(event -> {
            // load items
            provincias.setAll(task1.getValue());
            comboProvincia.setItems(provincias);
            // set selected items
            comboProvincia.getSelectionModel().select(propietario.getLocalidades().getProvincias().getId() - 1);
            log.info("Loaded Item.");
        });

        task2.setOnSucceeded(event -> {
            // load items
            localidades.setAll(task2.getValue());
            comboLocalidad.getItems().setAll(localidades);
            // set selected items
            comboLocalidad.getSelectionModel().select(propietario.getLocalidades().getId() - 1);
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task1);
        ViewSwitcher.getLoadingDialog().setTask(task2);
    }
}

package controller.owner;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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
import model.Localidades;
import model.Propietarios;
import model.Provincias;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

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

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static ProvinciasHome daoPR = new ProvinciasHome();

    private LocalidadesHome daoLC = new LocalidadesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    final ObservableList<Provincias> provincias = FXCollections.observableArrayList();

    private Propietarios propietario;

    @FXML
    void initialize() {

        Platform.runLater(() -> loadFields());

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.hide();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });

    }

    /*
     * Class Methods
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
            log.info(marker, "record updated");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error(marker, "failed to update record");
        }
    }

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    }

    public void loadDao() {
        List<Provincias> provinciasList = daoPR.normalAll();
        provincias.setAll(provinciasList);
        comboProvincia.setItems(provincias);
        // set selected items
        for (Provincias provincia : comboProvincia.getItems())
            if (propietario.getLocalidades().getProvincias().getId().equals(provincia.getId())) {
                comboProvincia.getSelectionModel().select(provincia);
                break;
            }
        log.info(marker, "Loaded Provinces .");
        Task<List<Localidades>> task = daoLC.showByProvincia(propietario.getLocalidades().getProvincias());

        task.setOnSucceeded(event -> {
            // load items
            localidades.setAll(task.getValue());
            comboLocalidad.getItems().setAll(localidades);
            // set selected items
            for (Localidades localidad : comboLocalidad.getItems())
                if (propietario.getLocalidades().getId().equals(localidad.getId())) {
                    comboLocalidad.getSelectionModel().select(localidad);
                    break;
                }
            log.info(marker, "Loaded Localities.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        log.info(marker, "Loading fields");
        txtNombre.setText(propietario.getNombre());
        txtApellido.setText(propietario.getApellido());
        txtDomicilio.setText(propietario.getDomicilio());
        txtTelCel.setText(String.valueOf(propietario.getTelCel()));
        txtTelFijo.setText(String.valueOf(propietario.getTelFijo()));
        txtMail.setText(propietario.getMail());

        // this fixes the bug in locality picker
        comboProvincia.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                Task<List<Localidades>> task = daoLC.showByProvincia(newValue);

                task.setOnSucceeded(event -> {
                    localidades.setAll(task.getValue());
                    comboLocalidad.getItems().clear();
                    comboLocalidad.getItems().setAll(localidades);
                    log.info(marker, "Loaded Items.");
                });

                ViewSwitcher.loadingDialog.addTask(task);
                ViewSwitcher.loadingDialog.startTask();
            }
        });

    }
}

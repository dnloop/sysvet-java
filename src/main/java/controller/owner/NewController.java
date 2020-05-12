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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Localidades;
import model.Propietarios;
import model.Provincias;
import utils.DialogBox;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

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

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private ProvinciasHome daoPR = new ProvinciasHome();

    private LocalidadesHome daoLC = new LocalidadesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    final ObservableList<Localidades> localidades = FXCollections.observableArrayList();

    final ObservableList<Provincias> provinciasList = FXCollections.observableArrayList();

    private Propietarios propietario = new Propietarios();

    private Stage stage;

    @FXML
    void initialize() {

        log.info("Retrieving details");
        loadDao();
        comboProvincia.setItems(provinciasList);

        comboProvincia.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                Task<List<Localidades>> task = daoLC.showByProvincia(newValue);

                task.setOnSucceeded(event -> {
                    localidades.setAll(task.getValue());
                    comboLocalidad.getItems().clear();
                    comboLocalidad.getItems().setAll(localidades);
                    log.info("Loaded Items.");
                });

                ViewSwitcher.loadingDialog.setTask(task);
                ViewSwitcher.loadingDialog.startTask();

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

    }

    /*
     * Class methods
     */

    private void storeRecord() {
        propietario.setNombre(txtNombre.getText());
        propietario.setApellido(txtApellido.getText());
        propietario.setDomicilio(txtDomicilio.getText());
        propietario.setLocalidades(comboLocalidad.getSelectionModel().getSelectedItem());
        Date fecha = new Date();
        propietario.setCreatedAt(fecha);
        if (HibernateValidator.validate(propietario)) {
            daoPO.add(propietario);
            log.info("record created");
            DialogBox.displaySuccess();
            this.stage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            log.error("failed to create record");
        }
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        Task<List<Provincias>> task = daoPR.displayRecords();

        task.setOnSucceeded(event -> {
            provinciasList.setAll(task.getValue());
            comboProvincia.setItems(provinciasList);
            log.info("Loaded Item.");
        });

        ViewSwitcher.loadingDialog.setTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

}

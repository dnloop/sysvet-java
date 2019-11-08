package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.ViewSwitcher;
import utils.validator.HibernateValidator;
import utils.validator.Trim;

public class ModalDialogController {
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

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static CuentasCorrientesHome daoCC = new CuentasCorrientesHome();

    private static PropietariosHome daoPO = new PropietariosHome();

    private CuentasCorrientes cuentaCorriente;

    private Stage stage;

    final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    private TextFormatter<Double> textFormatter;

    @FXML
    void initialize() {
        assert comboPropietario != null : "fx:id=\"comboPropietario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDescription != null : "fx:id=\"txtDescripcion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAmount != null : "fx:id=\"txtMonto\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpDate != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAceptar\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancelar\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> loadFields()); // Required to prevent NullPointer

        loadDao();

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

    @FXML
    void formatMask(KeyEvent event) {
        Pattern validDoubleText = Pattern.compile("-?((\\d{0,7})|(\\d+\\.\\d{0,4}))");
        textFormatter = new TextFormatter<Double>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (validDoubleText.matcher(newText).matches())
                return change;
            else
                return null;
        });

        txtAmount.setTextFormatter(textFormatter);

    }

    private void updateRecord() {
        // date conversion from LocalDate
        fecha = java.sql.Date.valueOf(dpDate.getValue());
        cuentaCorriente.setFecha(fecha);
        cuentaCorriente.setDescripcion(Trim.trim(txtDescription.getText()));
        cuentaCorriente.setMonto(new BigDecimal(txtAmount.getText()));
        cuentaCorriente.setPropietarios(comboPropietario.getSelectionModel().getSelectedItem());
        fecha = new Date();
        cuentaCorriente.setUpdatedAt(fecha);
        if (HibernateValidator.validate(cuentaCorriente)) {
            daoCC.update(cuentaCorriente);
            DialogBox.displaySuccess();
            log.info("record updated");
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error("failed to update record");
        }
    }

    public void setObject(CuentasCorrientes cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

    private void loadDao() {
        log.info("Loading fields");
        Task<List<Propietarios>> task = daoPO.displayRecords();

        task.setOnSucceeded(event -> {
            propietariosList.setAll(task.getValue());
            comboPropietario.setItems(propietariosList);
            comboPropietario.getSelectionModel().select(cuentaCorriente.getPropietarios().getId() - 1);
            log.info("Loaded Item.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void loadFields() {
        fecha = new Date(cuentaCorriente.getFecha().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        log.info("Loading fields");
        txtDescription.setText(cuentaCorriente.getDescripcion());
        txtAmount.setText(cuentaCorriente.getMonto().toString());
        dpDate.setValue(lfecha);
    }
}
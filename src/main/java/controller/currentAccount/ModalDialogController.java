package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
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

import dao.CuentasCorrientesHome;
import dao.PropietariosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;
import model.CuentasCorrientes;
import model.Propietarios;
import utils.DialogBox;
import utils.FieldFormatter;
import utils.validator.HibernateValidator;
import utils.validator.Trim;
import utils.viewswitcher.ViewSwitcher;

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

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static CuentasCorrientesHome daoCC = new CuentasCorrientesHome();

    private static PropietariosHome daoPO = new PropietariosHome();

    private CuentasCorrientes cuentaCorriente;

    final ObservableList<Propietarios> propietariosList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    private FieldFormatter fieldFormatter = new FieldFormatter();

    @FXML
    void initialize() {
        Platform.runLater(() -> loadFields());

        loadDao();

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });

        fieldFormatter.setFloatPoint();
    }

    /*
     * Class Methods
     */

    @FXML
    void formatMask(KeyEvent event) {
        txtAmount.setTextFormatter(fieldFormatter.getFloatPoint());
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
            log.info(marker, "Record updated.");
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error(marker, "Failed to update record.");
        }
    }

    public void setObject(CuentasCorrientes cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    private void loadDao() {
        Task<List<Propietarios>> task = daoPO.displayRecords();
        task.setOnSucceeded(event -> {
            propietariosList.setAll(task.getValue());
            comboPropietario.setItems(propietariosList);
            for (Propietarios propietario : comboPropietario.getItems())
                if (cuentaCorriente.getPropietarios().getId().equals(propietario.getId())) {
                    comboPropietario.getSelectionModel().select(propietario);
                    break;
                }
            log.info(marker, "List Loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        fecha = new Date(cuentaCorriente.getFecha().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txtDescription.setText(cuentaCorriente.getDescripcion());
        txtAmount.setText(cuentaCorriente.getMonto().toString());
        dpDate.setValue(lfecha);
        log.info(marker, "Fields Loaded.");
    }
}
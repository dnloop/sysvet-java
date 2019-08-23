package controller.currentAccount;

import java.math.BigDecimal;
import java.net.URL;
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
import utils.validator.HibernateValidator;
import utils.validator.Trim;

public class NewController {

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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(NewController.class);

    private static CuentasCorrientesHome daoCC = new CuentasCorrientesHome();

    private static PropietariosHome daoPO = new PropietariosHome();

    private CuentasCorrientes cuentaCorriente = new CuentasCorrientes();

    private Stage stage;

    final ObservableList<Propietarios> propietarios = FXCollections.observableArrayList();

    private TextFormatter<Double> textFormatter;

    @FXML
    void initialize() {
        assert comboPropietario != null : "fx:id=\"comboPropietario\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDescription != null : "fx:id=\"txtDescription\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAmount != null : "fx:id=\"txtAmount\" was not injected: check your FXML file 'new.fxml'.";
        assert dpDate != null : "fx:id=\"dpDate\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        loadDao();

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnSave.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea guardar el registro?"))
                storeRecord();
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

    private void storeRecord() {
        // date conversion from LocalDate
        Date fecha = dpDate.getValue() != null ? java.sql.Date.valueOf(dpDate.getValue()) : null;
        cuentaCorriente.setFecha(fecha);
        cuentaCorriente.setDescripcion(Trim.trim(txtDescription.getText()));
        if (!txtAmount.getText().isEmpty())
            cuentaCorriente.setMonto(new BigDecimal(txtAmount.getText()));
        else
            cuentaCorriente.setMonto(null);
        cuentaCorriente.setPropietarios(comboPropietario.getSelectionModel().getSelectedItem());
        fecha = new Date();
        cuentaCorriente.setCreatedAt(fecha);
        if (HibernateValidator.validate(cuentaCorriente)) {
            daoCC.add(cuentaCorriente);
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
        Task<List<Propietarios>> task = new Task<List<Propietarios>>() {
            @Override
            protected List<Propietarios> call() throws Exception {
                Thread.sleep(500);
                return daoPO.displayRecords();
            }
        };

        task.setOnSucceeded(event -> {
            propietarios.setAll(task.getValue());
            comboPropietario.setItems(propietarios);
            log.info("Loaded Item.");
        });

        task.setOnFailed(event -> {
            log.debug("Failed to Query owners list.");
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

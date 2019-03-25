package controller.exam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Pacientes;

public class ModalDialogControllerTest {

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXComboBox<FichasClinicas> comboFC;

    @FXML
    private JFXTextField txtPesoCorp;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private JFXTextField txtTempCorp;

    @FXML
    private JFXTextField txtDeshidratacion;

    @FXML
    private JFXTextField txtFrecResp;

    @FXML
    private JFXTextField txtAmplitud;

    @FXML
    private JFXTextField txtTipo;

    @FXML
    private JFXTextField txtRitmo;

    @FXML
    private JFXTextField txtFrecCardio;

    @FXML
    private JFXTextField txtPulso;

    @FXML
    private JFXTextField txtTllc;

    @FXML
    private JFXTextField txtBucal;

    @FXML
    private JFXTextField txtEscleral;

    @FXML
    private JFXTextField txtPalperal;

    @FXML
    private JFXTextField txtVulvar;

    @FXML
    private JFXTextField txtPeneana;

    @FXML
    private JFXTextField txtSubmandibular;

    @FXML
    private JFXTextField txtPreescapular;

    @FXML
    private JFXTextField txtPrecrural;

    @FXML
    private JFXTextField txtInguinal;

    @FXML
    private JFXTextField txtPopliteo;

    @FXML
    private JFXTextArea txtOtros;

    private Integer examenGeneral;

    private Pacientes paciente;

    private Stage stage;

    @FXML
    void initialize() {
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboFC != null : "fx:id=\"comboFC\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPesoCorp != null : "fx:id=\"txtPesoCorp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTempCorp != null : "fx:id=\"txtTempCorp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDeshidratacion != null : "fx:id=\"txtDeshidratacion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtFrecResp != null : "fx:id=\"txtFrecResp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAmplitud != null : "fx:id=\"txtAmplitud\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTipo != null : "fx:id=\"txtTipo\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtRitmo != null : "fx:id=\"txtRitmo\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtFrecCardio != null : "fx:id=\"txtFrecCardio\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPulso != null : "fx:id=\"txtPulso\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTllc != null : "fx:id=\"txtTllc\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtBucal != null : "fx:id=\"txtBucal\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEscleral != null : "fx:id=\"txtEscleral\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPalperal != null : "fx:id=\"txtPalperal\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtVulvar != null : "fx:id=\"txtVulvar\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPeneana != null : "fx:id=\"txtPeneana\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtSubmandibular != null : "fx:id=\"txtSubmandibular\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPreescapular != null : "fx:id=\"txtPreescapular\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPrecrural != null : "fx:id=\"txtPrecrural\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtInguinal != null : "fx:id=\"txtInguinal\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPopliteo != null : "fx:id=\"txtPopliteo\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtOtros != null : "fx:id=\"txtOtros\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("View succesfully switched. ID: " + examenGeneral);

        });

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {

        });
    }

    /* Class Methods */
    public void setObject(Integer id) {
        this.examenGeneral = id;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

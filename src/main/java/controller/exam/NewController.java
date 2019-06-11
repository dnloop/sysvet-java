package controller.exam;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
import dao.PacientesHome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.ExamenGeneral;
import model.Pacientes;
import utils.DialogBox;
import utils.HibernateValidator;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXComboBox<Pacientes> comboPA;

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

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private ExamenGeneralHome daoEX = new ExamenGeneralHome();

    private PacientesHome daoFC = new PacientesHome();

    private ExamenGeneral examenGeneral = new ExamenGeneral();

    private Pacientes paciente;

    private Stage stage;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        assert comboPA != null : "fx:id=\"comboPA\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPesoCorp != null : "fx:id=\"txtPesoCorp\" was not injected: check your FXML file 'new.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTempCorp != null : "fx:id=\"txtTempCorp\" was not injected: check your FXML file 'new.fxml'.";
        assert txtDeshidratacion != null : "fx:id=\"txtDeshidratacion\" was not injected: check your FXML file 'new.fxml'.";
        assert txtFrecResp != null : "fx:id=\"txtFrecResp\" was not injected: check your FXML file 'new.fxml'.";
        assert txtAmplitud != null : "fx:id=\"txtAmplitud\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTipo != null : "fx:id=\"txtTipo\" was not injected: check your FXML file 'new.fxml'.";
        assert txtRitmo != null : "fx:id=\"txtRitmo\" was not injected: check your FXML file 'new.fxml'.";
        assert txtFrecCardio != null : "fx:id=\"txtFrecCardio\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPulso != null : "fx:id=\"txtPulso\" was not injected: check your FXML file 'new.fxml'.";
        assert txtTllc != null : "fx:id=\"txtTllc\" was not injected: check your FXML file 'new.fxml'.";
        assert txtBucal != null : "fx:id=\"txtBucal\" was not injected: check your FXML file 'new.fxml'.";
        assert txtEscleral != null : "fx:id=\"txtEscleral\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPalperal != null : "fx:id=\"txtPalperal\" was not injected: check your FXML file 'new.fxml'.";
        assert txtVulvar != null : "fx:id=\"txtVulvar\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPeneana != null : "fx:id=\"txtPeneana\" was not injected: check your FXML file 'new.fxml'.";
        assert txtSubmandibular != null : "fx:id=\"txtSubmandibular\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPreescapular != null : "fx:id=\"txtPreescapular\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPrecrural != null : "fx:id=\"txtPrecrural\" was not injected: check your FXML file 'new.fxml'.";
        assert txtInguinal != null : "fx:id=\"txtInguinal\" was not injected: check your FXML file 'new.fxml'.";
        assert txtPopliteo != null : "fx:id=\"txtPopliteo\" was not injected: check your FXML file 'new.fxml'.";
        assert txtOtros != null : "fx:id=\"txtOtros\" was not injected: check your FXML file 'new.fxml'.";

        log.info("Retrieving details");
        // create list and fill it with dao
        pacientesList.setAll(daoFC.displayRecords());
        comboPA.setItems(pacientesList);

        comboPA.setOnAction((event) -> {
            paciente = comboPA.getSelectionModel().getSelectedItem();

            if (paciente.getSexo().equals("F"))
                txtPeneana.setDisable(true);
            else
                txtVulvar.setDisable(true);
        });

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

    private void storeRecord() {
        // date conversion from LocalDate
        Date fecha = java.sql.Date.valueOf(dpFecha.getValue());
        examenGeneral.setFecha(fecha);
        examenGeneral.setPesoCorporal(Integer.valueOf(txtPesoCorp.getText()));
        examenGeneral.setTempCorporal(Integer.valueOf(txtTempCorp.getText()));
        examenGeneral.setDeshidratacion(Integer.valueOf(txtDeshidratacion.getText()));
        examenGeneral.setFrecResp(Integer.valueOf(txtFrecResp.getText()));
        examenGeneral.setFrecCardio(Integer.valueOf(txtFrecCardio.getText()));
        examenGeneral.setAmplitud(txtAmplitud.getText());
        examenGeneral.setTipo(txtTipo.getText());
        examenGeneral.setRitmo(txtRitmo.getText());
        examenGeneral.setPulso(txtPulso.getText());
        examenGeneral.setTllc(Integer.valueOf(txtTllc.getText()));
        examenGeneral.setBucal(txtBucal.getText());
        examenGeneral.setEscleral(txtEscleral.getText());
        examenGeneral.setPalperal(txtPalperal.getText());
        if (paciente.getSexo().equals("F"))
            examenGeneral.setVulvar(txtVulvar.getText());
        else
            examenGeneral.setPeneana(txtPeneana.getText());
        examenGeneral.setSubmandibular(txtSubmandibular.getText());
        examenGeneral.setPreescapular(txtPreescapular.getText());
        examenGeneral.setPrecrural(txtPrecrural.getText());
        examenGeneral.setInguinal(txtInguinal.getText());
        examenGeneral.setPopliteo(txtPopliteo.getText());
        examenGeneral.setOtros(txtOtros.getText());
        examenGeneral.setPacientes(comboPA.getSelectionModel().getSelectedItem());
        fecha = new Date();
        examenGeneral.setCreatedAt(fecha);
        if (HibernateValidator.validate(examenGeneral)) {
            daoEX.add(examenGeneral);
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
}

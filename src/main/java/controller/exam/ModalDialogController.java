package controller.exam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
import dao.FichasClinicasHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.ExamenGeneral;
import model.FichasClinicas;
import model.Pacientes;

public class ModalDialogController {
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

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    ExamenGeneralHome daoEX = new ExamenGeneralHome();

    static FichasClinicasHome daoFC = new FichasClinicasHome();

    private ExamenGeneral examenGeneral;

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
            log.info("Retrieving details");
            // create list and fill it with dao
            ObservableList<FichasClinicas> fichasClinicas = FXCollections.observableArrayList();
            fichasClinicas = loadTable(fichasClinicas);
            // sort list elements asc by id
            Comparator<FichasClinicas> comp = Comparator.comparingInt(FichasClinicas::getId);
            FXCollections.sort(fichasClinicas, comp);
            // required conversion for datepicker
            log.info("Formatting dates");
            Date fecha = new Date(examenGeneral.getFecha().getTime());
            LocalDate lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            /*
             * This could be used to display a title in the form in order to be more
             * descriptive.
             */
            paciente = examenGeneral.getFichasClinicas().getPacientes();

            log.info("Loading fields");
            txtPesoCorp.setText(String.valueOf(examenGeneral.getPesoCorporal()));
            txtTempCorp.setText(String.valueOf(examenGeneral.getTempCorporal()));
            txtDeshidratacion.setText(String.valueOf(examenGeneral.getDeshidratacion()));
            txtFrecResp.setText(String.valueOf(examenGeneral.getFrecResp()));
            txtFrecCardio.setText(String.valueOf(examenGeneral.getFrecCardio()));
            txtAmplitud.setText(examenGeneral.getAmplitud());
            txtTipo.setText(examenGeneral.getTipo());
            txtRitmo.setText(examenGeneral.getRitmo());
            txtPulso.setText(examenGeneral.getPulso());
            txtTllc.setText(String.valueOf(examenGeneral.getTllc()));
            txtBucal.setText(examenGeneral.getBucal());
            txtEscleral.setText(examenGeneral.getEscleral());
            txtPalperal.setText(examenGeneral.getPalperal());
            // hacky =)
            if (paciente.getSexo().equals("F")) {
                txtVulvar.setText(examenGeneral.getVulvar());
                txtPeneana.setDisable(true);
            } else {
                txtPeneana.setText(examenGeneral.getPeneana());
                txtVulvar.setDisable(true);
            }
            txtSubmandibular.setText(examenGeneral.getSubmandibular());
            txtPreescapular.setText(examenGeneral.getPreescapular());
            txtPrecrural.setText(examenGeneral.getPrecrural());
            txtInguinal.setText(examenGeneral.getInguinal());
            txtPopliteo.setText(examenGeneral.getPopliteo());
            txtOtros.setText(examenGeneral.getOtros());
            dpFecha.setValue(lfecha);
            comboFC.getSelectionModel().select(examenGeneral.getFichasClinicas().getPacientes().getId() - 1);
        }); // required to prevent NullPointer

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

    private void updateRecord() {
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
        examenGeneral.setFichasClinicas(comboFC.getSelectionModel().getSelectedItem());
        fecha = new Date();
        examenGeneral.setUpdatedAt(fecha);
        daoEX.update(examenGeneral);
        log.info("record updated");
        this.stage.close();
    }

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

    static ObservableList<FichasClinicas> loadTable(ObservableList<FichasClinicas> fichasClinicas) {
        List<FichasClinicas> list = daoFC.displayRecords();
        for (FichasClinicas item : list)
            fichasClinicas.add(item);
        return fichasClinicas;
    }

    public void setObject(ExamenGeneral examenGeneral) {
        this.examenGeneral = examenGeneral;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

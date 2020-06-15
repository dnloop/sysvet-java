package controller.exam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
import dao.PacientesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import model.ExamenGeneral;
import model.Pacientes;
import utils.DialogBox;
import utils.FieldFormatter;
import utils.validator.HibernateValidator;
import utils.viewswitcher.ViewSwitcher;

public class ModalDialogController {
    @FXML
    private JFXButton btnAccept;

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
    private JFXTextField txtSexual;

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

    @FXML
    private Label lblSexual;

    private static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private static ExamenGeneralHome daoEX = new ExamenGeneralHome();

    private static PacientesHome daoPA = new PacientesHome();

    private ExamenGeneral examenGeneral;

    private Pacientes paciente;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private Date fecha;

    private LocalDate lfecha;

    private FieldFormatter fieldFormatter = new FieldFormatter();

    @FXML
    void initialize() {

        btnCancel.setOnAction((event) -> {
            ViewSwitcher.modalStage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
                updateRecord();
        });

        formatMask();
    }

    /*
     * Class Methods
     */

    private void formatMask() {
        fieldFormatter.setInteger();
        txtPesoCorp.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtTempCorp.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtDeshidratacion.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtFrecResp.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtFrecCardio.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtAmplitud.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtRitmo.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtPulso.setTextFormatter(fieldFormatter.getInteger());
        fieldFormatter = new FieldFormatter();
        fieldFormatter.setInteger();
        txtTllc.setTextFormatter(fieldFormatter.getInteger());
    } // quick and dirty

    private void updateRecord() {
        // date conversion from LocalDate
        fecha = java.sql.Date.valueOf(dpFecha.getValue());
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
        examenGeneral.setSexual(txtSexual.getText());
        examenGeneral.setSubmandibular(txtSubmandibular.getText());
        examenGeneral.setPreescapular(txtPreescapular.getText());
        examenGeneral.setPrecrural(txtPrecrural.getText());
        examenGeneral.setInguinal(txtInguinal.getText());
        examenGeneral.setPopliteo(txtPopliteo.getText());
        examenGeneral.setOtros(txtOtros.getText());
        examenGeneral.setPacientes(comboPA.getSelectionModel().getSelectedItem());
        fecha = new Date();
        examenGeneral.setUpdatedAt(fecha);
        if (HibernateValidator.validate(examenGeneral)) {
            daoEX.update(examenGeneral);
            log.info(marker, "Record updated.");
            DialogBox.displaySuccess();
            ViewSwitcher.modalStage.close();
        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error(marker, "Failed to update record.");
        }
    }

    public void setObject(ExamenGeneral examenGeneral) {
        this.examenGeneral = examenGeneral;
    }

    public void loadDao() {
        Task<List<Pacientes>> task = daoPA.displayRecords();

        task.setOnSucceeded(event -> {
            pacientesList.setAll(task.getValue());
            comboPA.setItems(pacientesList);
            for (Pacientes paciente : comboPA.getItems())
                if (examenGeneral.getPacientes().getId().equals(paciente.getId())) {
                    comboPA.getSelectionModel().select(paciente);
                    break;
                }
            initFields();
            log.info(marker, "List loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void loadFields() {
        fecha = new Date(examenGeneral.getFecha().getTime());
        lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        txtSexual.setText(examenGeneral.getSexual());
        paciente = examenGeneral.getPacientes();

        if (paciente != null)
            if (paciente.getSexo().equals("F"))
                lblSexual.setText("Vulvar");
            else
                lblSexual.setText("Peneana");

        txtSubmandibular.setText(examenGeneral.getSubmandibular());
        txtPreescapular.setText(examenGeneral.getPreescapular());
        txtPrecrural.setText(examenGeneral.getPrecrural());
        txtInguinal.setText(examenGeneral.getInguinal());
        txtPopliteo.setText(examenGeneral.getPopliteo());
        txtOtros.setText(examenGeneral.getOtros());
        dpFecha.setValue(lfecha);
        log.info("Fields loaded.");
    }

    /**
     * Load the modal fields after the stage starts.
     */
    private void initFields() {
        Platform.runLater(() -> loadFields());
    }
}

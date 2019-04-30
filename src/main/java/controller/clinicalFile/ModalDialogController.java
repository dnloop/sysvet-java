package controller.clinicalFile;

import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dao.FichasClinicasHome;
import dao.PacientesHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Pacientes;

public class ModalDialogController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXComboBox<FichasClinicas> comboPA;

    @FXML
    private JFXTextField txtMotivoConsulta;

    @FXML
    private JFXTextArea txtAnamnesis;

    @FXML
    private JFXTextField txtMedActual;

    @FXML
    private JFXTextField txtMedAnterior;

    @FXML
    private JFXTextField txtEstNutricion;

    @FXML
    private JFXTextField txtEstSanitario;

    @FXML
    private JFXTextField txtAspectoGeneral;

    @FXML
    private JFXTextField txtDerivaciones;

    @FXML
    private JFXTextArea txtDeterDiagComp;

    @FXML
    private JFXTextArea txtPronostico;

    @FXML
    private JFXTextArea txtDiagnostico;

    @FXML
    private JFXTextArea txtExploracion;

    @FXML
    private JFXTextArea txtEvolucion;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    static FichasClinicasHome daoFC = new FichasClinicasHome();

    static PacientesHome daoPA = new PacientesHome();

    private FichasClinicas fichaClinica;

    private Stage stage;

    @FXML
    void initialize() {
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert comboPA != null : "fx:id=\"comboPA\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMotivoConsulta != null : "fx:id=\"txtMotivoConsulta\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAnamnesis != null : "fx:id=\"txtAnamnesis\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMedActual != null : "fx:id=\"txtMedActual\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtMedAnterior != null : "fx:id=\"txtMedAnterior\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEstNutricion != null : "fx:id=\"txtEstNutricion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEstSanitario != null : "fx:id=\"txtEstSanitario\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtAspectoGeneral != null : "fx:id=\"txtAspectoGeneral\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDerivaciones != null : "fx:id=\"txtDerivaciones\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDeterDiagComp != null : "fx:id=\"txtDeterDiagComp\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtPronostico != null : "fx:id=\"txtPronostico\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtDiagnostico != null : "fx:id=\"txtDiagnostico\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtExploracion != null : "fx:id=\"txtExploracion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtEvolucion != null : "fx:id=\"txtEvolucion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            // create list and fill it with dao
            ObservableList<Pacientes> pacientes = FXCollections.observableArrayList();
            pacientes = loadTable(pacientes);
            // sort list elements asc by id
            Comparator<Pacientes> comp = Comparator.comparingInt(Pacientes::getId);
            FXCollections.sort(pacientes, comp);

            log.info("Loading fields");
            comboPA.getSelectionModel().select(fichaClinica.getPacientes().getId() - 1);
            txtMotivoConsulta.setText(fichaClinica.getMotivoConsulta());
            txtAnamnesis.setText(fichaClinica.getAnamnesis());
            txtMedActual.setText(fichaClinica.getMedicacionActual());
            txtMedAnterior.setText(fichaClinica.getMedicacionAnterior());
            txtEstNutricion.setText(fichaClinica.getEstadoNutricion());
            txtEstSanitario.setText(fichaClinica.getEstadoSanitario());
            txtAspectoGeneral.setText(fichaClinica.getAspectoGeneral());
            txtDerivaciones.setText(fichaClinica.getDerivaciones());
            txtDeterDiagComp.setText(fichaClinica.getDeterDiagComp());
            txtPronostico.setText(fichaClinica.getPronostico());
            txtExploracion.setText(fichaClinica.getExploracion());
            txtEvolucion.setText(fichaClinica.getEvolucion());
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
        Date fecha = new Date();
        fichaClinica.setUpdatedAt(fecha);
        daoFC.update(fichaClinica);
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

    static ObservableList<Pacientes> loadTable(ObservableList<Pacientes> paciente) {
        List<Pacientes> list = daoPA.displayRecords();
        for (Pacientes item : list)
            paciente.add(item);
        return paciente;
    }

    public void setObject(FichasClinicas fichaClinica) {
        this.fichaClinica = fichaClinica;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

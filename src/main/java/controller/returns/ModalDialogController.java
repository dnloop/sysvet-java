package controller.returns;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import dao.FichasClinicasHome;
import dao.RetornosHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Retornos;

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<FichasClinicas> comboFicha;

    @FXML
    private DatePicker dpReturn;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    RetornosHome daoRT = new RetornosHome();

    static FichasClinicasHome daoFC = new FichasClinicasHome();

    private Retornos retorno;

    private Stage stage;

    @FXML
    void initialize() {
        assert comboFicha != null : "fx:id=\"comboFicha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpReturn != null : "fx:id=\"dpReturn\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

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
            Date fecha = new Date(retorno.getFecha().getTime());
            LocalDate lfecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            log.info("Loading fields");
            dpReturn.setValue(lfecha);
            comboFicha.setItems(fichasClinicas);
            comboFicha.getSelectionModel().select(retorno.getFichasClinicas().getId() - 1);
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
        Date fecha = java.sql.Date.valueOf(dpReturn.getValue());
        retorno.setFichasClinicas(comboFicha.getSelectionModel().getSelectedItem());
        retorno.setFecha(fecha);
        fecha = new Date();
        retorno.setUpdatedAt(fecha);
        daoRT.update(retorno);
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

    public void setObject(Retornos retorno) {
        this.retorno = retorno;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }

}

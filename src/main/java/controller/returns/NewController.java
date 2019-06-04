package controller.returns;

import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.FichasClinicas;
import model.Retornos;
import utils.DialogBox;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<FichasClinicas> comboFicha;

    @FXML
    private DatePicker dpReturn;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private RetornosHome daoRT = new RetornosHome();

    private FichasClinicasHome daoFC = new FichasClinicasHome();

    private Retornos retorno;

    private Stage stage;

    final ObservableList<FichasClinicas> fichasClinicas = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert comboFicha != null : "fx:id=\"comboFicha\" was not injected: check your FXML file 'new.fxml'.";
        assert dpReturn != null : "fx:id=\"dpReturn\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";
        Platform.runLater(() -> {
            log.info("Retrieving details");
            fichasClinicas.setAll(daoFC.displayRecords());
        }); // required to prevent NullPointer

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
        Date fecha = java.sql.Date.valueOf(dpReturn.getValue());
        retorno.setFichasClinicas(comboFicha.getSelectionModel().getSelectedItem());
        retorno.setFecha(fecha);
        fecha = new Date();
        retorno.setCreatedAt(fecha);
        daoRT.add(retorno);
        log.info("record created");
        this.stage.close();
    }

    public void setObject(Retornos retorno) {
        this.retorno = retorno;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

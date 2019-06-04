package controller.province;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.ProvinciasHome;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Provincias;
import utils.DialogBox;

public class NewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    private ProvinciasHome dao = new ProvinciasHome();

    private Provincias provincia;

    private Stage stage;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'new.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'new.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'new.fxml'.";

        Platform.runLater(() -> {
            log.info("Loading fields");
            txtNombre.setText(provincia.getNombre());
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
        Date fecha = new Date();
        provincia.setNombre(txtNombre.getText());
        provincia.setCreatedAt(fecha);
        dao.add(provincia);
        log.info("record created");
        this.stage.close();
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

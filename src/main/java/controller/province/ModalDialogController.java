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

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    protected static final Logger log = (Logger) LogManager.getLogger(ModalDialogController.class);

    ProvinciasHome dao = new ProvinciasHome();

    private Provincias provincia;

    private Stage stage;

    @FXML
    void initialize() {
        assert txtNombre != null : "fx:id=\"txtNombre\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

        Platform.runLater(() -> {
            log.info("Loading fields");
            txtNombre.setText(provincia.getNombre());
        }); // required to prevent NullPointer

        btnCancel.setOnAction((event) -> {
            this.stage.close();
        });

        btnAccept.setOnAction((event) -> {
            if (DialogBox.confirmDialog("¿Desea actualizar el registro?"))
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
        provincia.setNombre(txtNombre.getText());
        provincia.setUpdatedAt(fecha);
        dao.update(provincia);
        log.info("record updated");
        this.stage.close();
    }

    public void setObject(Provincias provincia) {
        this.provincia = provincia;
    }

    public void showModal(Stage stage) {
        this.stage = stage;
        this.stage.showAndWait();
    }
}

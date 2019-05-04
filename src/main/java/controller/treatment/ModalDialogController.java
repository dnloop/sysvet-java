package controller.treatment;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class ModalDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<?> comboInternacion;

    @FXML
    private JFXTextField txtTratamiento;

    @FXML
    private JFXTextField txtProcAdicional;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private JFXButton btnAccept;

    @FXML
    private JFXButton btnCancel;

    @FXML
    void initialize() {
        assert comboInternacion != null : "fx:id=\"comboInternacion\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtTratamiento != null : "fx:id=\"txtTratamiento\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert txtProcAdicional != null : "fx:id=\"txtProcAdicional\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert dpFecha != null : "fx:id=\"dpFecha\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnAccept != null : "fx:id=\"btnAccept\" was not injected: check your FXML file 'modalDialog.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'modalDialog.fxml'.";

    }
}

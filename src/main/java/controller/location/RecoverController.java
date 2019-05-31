package controller.location;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;

public class RecoverController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnRecover;

    @FXML
    private JFXTreeTableView<?> indexLC;

    @FXML
    private JFXTreeTableColumn<?, ?> nombre;

    @FXML
    private JFXTreeTableColumn<?, ?> codigoPostal;

    @FXML
    private JFXTreeTableColumn<?, ?> provincia;

    @FXML
    private Pagination tablePagination;

    @FXML
    private JFXSlider pageSlider;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexLC != null : "fx:id=\"indexLC\" was not injected: check your FXML file 'recover.fxml'.";
        assert nombre != null : "fx:id=\"nombre\" was not injected: check your FXML file 'recover.fxml'.";
        assert codigoPostal != null : "fx:id=\"codigoPostal\" was not injected: check your FXML file 'recover.fxml'.";
        assert provincia != null : "fx:id=\"provincia\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        assert pageSlider != null : "fx:id=\"pageSlider\" was not injected: check your FXML file 'recover.fxml'.";

    }
}

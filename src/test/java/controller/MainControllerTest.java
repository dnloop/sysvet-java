package controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import utils.ViewSwitcher;

public class MainControllerTest {

    protected static final Logger log = (Logger) LogManager.getLogger(MainControllerTest.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox mainVBOX;

    @FXML
    private Label lblNavi;

    @FXML
    private Font x31;

    @FXML
    private Color x41;

    @FXML
    private JFXButton btnIndCC;

    @FXML
    private JFXButton btnIndDesp;

    @FXML
    private JFXButton btnIndExamen;

    @FXML
    private JFXButton btnIndFC;

    @FXML
    private JFXButton btnIndHC;

    @FXML
    private JFXButton btnIndInter;

    @FXML
    private JFXButton btnLoc;

    @FXML
    private JFXButton btnIndPac;

    @FXML
    private JFXButton btnIndProp;

    @FXML
    private JFXButton btnIndRet;

    @FXML
    private JFXButton btnIndTC;

    @FXML
    private JFXButton btnIndVac;

    @FXML
    private JFXButton btnDelCC;

    @FXML
    private JFXButton btnDelDesp;

    @FXML
    private JFXButton btnDelExamen;

    @FXML
    private JFXButton btnDelFC;

    @FXML
    private JFXButton btnDelHC;

    @FXML
    private JFXButton btnDelInter;

    @FXML
    private JFXButton btnDelLoc;

    @FXML
    private JFXButton btnDelPac;

    @FXML
    private JFXButton btnDelProp;

    @FXML
    private JFXButton btnDelRet;

    @FXML
    private JFXButton btnDelTC;

    @FXML
    private JFXButton btnDelVac;

    @FXML
    private JFXButton mainView;

    @FXML
    private BorderPane contentPane;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    private Label lblClock;

    String fxml;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        Platform.runLater(() -> {});

    }

    @FXML
    void indexCC(ActionEvent event) {

    }

    @FXML
    void indexDesp(ActionEvent event) {

    }

    @FXML
    void indexExamen(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/exam/index.fxml");
    }

    @FXML
    void indexFC(ActionEvent event) {

    }

    @FXML
    void indexInter(ActionEvent event) {

    }

    @FXML
    void indexLoc(ActionEvent event) {

    }

    @FXML
    void indexPac(ActionEvent event) {

    }

    @FXML
    void indexProp(ActionEvent event) {

    }

    @FXML
    void indexTC(ActionEvent event) {

    }

    @FXML
    void indexVac(ActionEvent event) {

    }

    /* Class methods */
    public void setView(Node node) {
        contentPane.setCenter(node);
    }
}

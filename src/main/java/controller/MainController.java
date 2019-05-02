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
import utils.HibernateUtil;
import utils.ViewSwitcher;

public class MainController {

    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);
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

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lblNavi != null : "fx:id=\"lblNavi\" was not injected: check your FXML file 'Untitled'.";
        assert x31 != null : "fx:id=\"x31\" was not injected: check your FXML file 'Untitled'.";
        assert x41 != null : "fx:id=\"x41\" was not injected: check your FXML file 'Untitled'.";
        assert contentPane != null : "fx:id=\"contentPane\" was not injected: check your FXML file 'Untitled'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'Untitled'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'Untitled'.";
        assert lblClock != null : "fx:id=\"lblClock\" was not injected: check your FXML file 'Untitled'.";

        try {
            HibernateUtil.setUp();
        } catch (Exception e) {
            log.debug(marker, "Unable establish the session. " + e.getMessage());
        }
    }

    /* Class methods */

    @FXML
    void indexCC(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/currentAccount/index.fxml");
    }

    @FXML
    void indexDesp(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/deworming/index.fxml");
    }

    @FXML
    void indexExamen(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/exam/index.fxml");
    }

    @FXML
    void indexFC(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/clinicalFile/index.fxml");
    }

    @FXML
    void indexInter(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/internation/index.fxml");
    }

    @FXML
    void indexLoc(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/location/index.fxml");
    }

    @FXML
    void indexPac(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/patient/index.fxml");
    }

    @FXML
    void indexProp(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/owner/index.fxml");
    }

    @FXML
    void indexTC(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/treatment/index.fxml");
    }

    @FXML
    void indexVac(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/vaccine/index.fxml");
    }

    @FXML
    void indexRet(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/return/index.fxml");
    }

    @FXML
    void indexHC(ActionEvent event) {
        ViewSwitcher.loadView("/fxml/clinicHistory/index.fxml");
    }

    public void setView(Node node) {
        contentPane.setCenter(node);
    }
}

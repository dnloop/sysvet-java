package controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import utils.Route;
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
        assert mainVBOX != null : "fx:id=\"mainVBOX\" was not injected: check your FXML file 'main.fxml'.";
        assert lblNavi != null : "fx:id=\"lblNavi\" was not injected: check your FXML file 'main.fxml'.";
        assert x31 != null : "fx:id=\"x31\" was not injected: check your FXML file 'main.fxml'.";
        assert x41 != null : "fx:id=\"x41\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndCC != null : "fx:id=\"btnIndCC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndDesp != null : "fx:id=\"btnIndDesp\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndExamen != null : "fx:id=\"btnIndExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndFC != null : "fx:id=\"btnIndFC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndHC != null : "fx:id=\"btnIndHC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndInter != null : "fx:id=\"btnIndInter\" was not injected: check your FXML file 'main.fxml'.";
        assert btnLoc != null : "fx:id=\"btnLoc\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndPac != null : "fx:id=\"btnIndPac\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndProp != null : "fx:id=\"btnIndProp\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndRet != null : "fx:id=\"btnIndRet\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndTC != null : "fx:id=\"btnIndTC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndVac != null : "fx:id=\"btnIndVac\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelCC != null : "fx:id=\"btnDelCC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelDesp != null : "fx:id=\"btnDelDesp\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelExamen != null : "fx:id=\"btnDelExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelFC != null : "fx:id=\"btnDelFC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelHC != null : "fx:id=\"btnDelHC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelInter != null : "fx:id=\"btnDelInter\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelLoc != null : "fx:id=\"btnDelLoc\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelPac != null : "fx:id=\"btnDelPac\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelProp != null : "fx:id=\"btnDelProp\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelRet != null : "fx:id=\"btnDelRet\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelTC != null : "fx:id=\"btnDelTC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnDelVac != null : "fx:id=\"btnDelVac\" was not injected: check your FXML file 'main.fxml'.";
        assert mainView != null : "fx:id=\"mainView\" was not injected: check your FXML file 'main.fxml'.";
        assert contentPane != null : "fx:id=\"contentPane\" was not injected: check your FXML file 'main.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'main.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'main.fxml'.";
        assert lblClock != null : "fx:id=\"lblClock\" was not injected: check your FXML file 'main.fxml'.";

        bindToTime();
    }

    /* Class methods */

    private void bindToTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(0), actionEvent -> {
            Calendar time = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            lblClock.setText(simpleDateFormat.format(time.getTime()));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    void mainView(ActionEvent event) {
//        ViewSwitcher.loadView("/fxml/main.fxml");
    }

    @FXML
    void indexCC(ActionEvent event) {
        ViewSwitcher.loadView(Route.CUENTACORRIENTE.indexView());
    }

    @FXML
    void indexDesp(ActionEvent event) {
        ViewSwitcher.loadView(Route.DESPARASITACION.indexView());
    }

    @FXML
    void indexExamen(ActionEvent event) {
        ViewSwitcher.loadView(Route.EXAMEN.indexView());
    }

    @FXML
    void indexFC(ActionEvent event) {
        ViewSwitcher.loadView(Route.FICHACLINICA.indexView());
    }

    @FXML
    void indexInter(ActionEvent event) {
        ViewSwitcher.loadView(Route.INTERNACION.indexView());
    }

    @FXML
    void indexLoc(ActionEvent event) {
        ViewSwitcher.loadView(Route.LOCALIDAD.indexView());
    }

    @FXML
    void indexPac(ActionEvent event) {
        ViewSwitcher.loadView(Route.PACIENTE.indexView());
    }

    @FXML
    void indexProp(ActionEvent event) {
        ViewSwitcher.loadView(Route.PROPIETARIO.indexView());
    }

    @FXML
    void indexTC(ActionEvent event) {
        ViewSwitcher.loadView(Route.TRATAMIENTO.indexView());
    }

    @FXML
    void indexVac(ActionEvent event) {
        ViewSwitcher.loadView(Route.VACUNA.indexView());
    }

    @FXML
    void indexRet(ActionEvent event) {
//        ViewSwitcher.loadView(Route.RETORNO.indexView());
    }

    @FXML
    void indexHC(ActionEvent event) {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.indexView());
    }

    @FXML
    void indexDelCC(ActionEvent event) {
        ViewSwitcher.loadView(Route.CUENTACORRIENTE.recoverView());
    }

    @FXML
    void indexDelDesp(ActionEvent event) {
        ViewSwitcher.loadView(Route.DESPARASITACION.recoverView());
    }

    @FXML
    void indexDelExamen(ActionEvent event) {
        ViewSwitcher.loadView(Route.EXAMEN.recoverView());
    }

    @FXML
    void indexDelFC(ActionEvent event) {
        ViewSwitcher.loadView(Route.FICHACLINICA.recoverView());
    }

    @FXML
    void indexDelHC(ActionEvent event) {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.recoverView());
    }

    @FXML
    void indexDelInter(ActionEvent event) {
        ViewSwitcher.loadView(Route.INTERNACION.recoverView());
    }

    @FXML
    void indexDelLoc(ActionEvent event) {
        ViewSwitcher.loadView(Route.LOCALIDAD.recoverView());
    }

    @FXML
    void indexDelPac(ActionEvent event) {
        ViewSwitcher.loadView(Route.PACIENTE.recoverView());
    }

    @FXML
    void indexDelProp(ActionEvent event) {
        ViewSwitcher.loadView(Route.PROPIETARIO.recoverView());
    }

    @FXML
    void indexDelRet(ActionEvent event) {
//        ViewSwitcher.loadView(Route.RETORNO.recoverView());
    }

    @FXML
    void indexDelTC(ActionEvent event) {
        ViewSwitcher.loadView(Route.TRATAMIENTO.recoverView());
    }

    @FXML
    void indexDelVac(ActionEvent event) {
        ViewSwitcher.loadView(Route.VACUNA.recoverView());
    }

    public void setView(Node node) {
        contentPane.setCenter(node);
    }
}

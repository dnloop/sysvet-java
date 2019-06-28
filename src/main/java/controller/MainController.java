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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import utils.ViewSwitcher;
import utils.routes.Route;
import utils.routes.RouteExtra;

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
    private MenuItem miNew;

    @FXML
    private MenuItem miCC;

    @FXML
    private MenuItem miDP;

    @FXML
    private MenuItem miEX;

    @FXML
    private MenuItem miFC;

    @FXML
    private MenuItem miHC;

    @FXML
    private MenuItem miIT;

    @FXML
    private MenuItem miLC;

    @FXML
    private MenuItem miPC;

    @FXML
    private MenuItem miPR;

    @FXML
    private MenuItem miTR;

    @FXML
    private MenuItem miVC;

    @FXML
    private MenuItem miQuit;

    @FXML
    private MenuItem miAbout;

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
        assert miNew != null : "fx:id=\"miNew\" was not injected: check your FXML file 'main.fxml'.";
        assert miCC != null : "fx:id=\"miCC\" was not injected: check your FXML file 'main.fxml'.";
        assert miDP != null : "fx:id=\"miDP\" was not injected: check your FXML file 'main.fxml'.";
        assert miEX != null : "fx:id=\"miEX\" was not injected: check your FXML file 'main.fxml'.";
        assert miFC != null : "fx:id=\"miFC\" was not injected: check your FXML file 'main.fxml'.";
        assert miHC != null : "fx:id=\"miHC\" was not injected: check your FXML file 'main.fxml'.";
        assert miIT != null : "fx:id=\"miIT\" was not injected: check your FXML file 'main.fxml'.";
        assert miLC != null : "fx:id=\"miLC\" was not injected: check your FXML file 'main.fxml'.";
        assert miPC != null : "fx:id=\"miPC\" was not injected: check your FXML file 'main.fxml'.";
        assert miPR != null : "fx:id=\"miPR\" was not injected: check your FXML file 'main.fxml'.";
        assert miTR != null : "fx:id=\"miTR\" was not injected: check your FXML file 'main.fxml'.";
        assert miVC != null : "fx:id=\"miVC\" was not injected: check your FXML file 'main.fxml'.";
        assert miQuit != null : "fx:id=\"miQuit\" was not injected: check your FXML file 'main.fxml'.";
        assert miAbout != null : "fx:id=\"miAbout\" was not injected: check your FXML file 'main.fxml'.";
        assert lblNavi != null : "fx:id=\"lblNavi\" was not injected: check your FXML file 'main.fxml'.";
        assert x31 != null : "fx:id=\"x31\" was not injected: check your FXML file 'main.fxml'.";
        assert x41 != null : "fx:id=\"x41\" was not injected: check your FXML file 'main.fxml'.";
        assert mainView != null : "fx:id=\"mainView\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndCC != null : "fx:id=\"btnIndCC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndDesp != null : "fx:id=\"btnIndDesp\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndExamen != null : "fx:id=\"btnIndExamen\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndFC != null : "fx:id=\"btnIndFC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndHC != null : "fx:id=\"btnIndHC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndInter != null : "fx:id=\"btnIndInter\" was not injected: check your FXML file 'main.fxml'.";
        assert btnLoc != null : "fx:id=\"btnLoc\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndPac != null : "fx:id=\"btnIndPac\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndProp != null : "fx:id=\"btnIndProp\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndTC != null : "fx:id=\"btnIndTC\" was not injected: check your FXML file 'main.fxml'.";
        assert btnIndVac != null : "fx:id=\"btnIndVac\" was not injected: check your FXML file 'main.fxml'.";
        assert contentPane != null : "fx:id=\"contentPane\" was not injected: check your FXML file 'main.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'main.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'main.fxml'.";
        assert lblClock != null : "fx:id=\"lblClock\" was not injected: check your FXML file 'main.fxml'.";

        bindToTime();
        Platform.runLater(() -> {
            ViewSwitcher.loadView("/fxml/charts/total.fxml");
        });

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
        ViewSwitcher.loadView(RouteExtra.CHART.getPath());
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
    void indexHC(ActionEvent event) {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.indexView());
    }

    @FXML
    void miCC(ActionEvent event) {
        ViewSwitcher.loadView(Route.CUENTACORRIENTE.recoverView());
    }

    @FXML
    void miDP(ActionEvent event) {
        ViewSwitcher.loadView(Route.DESPARASITACION.recoverView());
    }

    @FXML
    void miEX(ActionEvent event) {
        ViewSwitcher.loadView(Route.EXAMEN.recoverView());
    }

    @FXML
    void miFC(ActionEvent event) {
        ViewSwitcher.loadView(Route.FICHACLINICA.recoverView());
    }

    @FXML
    void miHC(ActionEvent event) {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.recoverView());
    }

    @FXML
    void miIT(ActionEvent event) {
        ViewSwitcher.loadView(Route.INTERNACION.recoverView());
    }

    @FXML
    void miLC(ActionEvent event) {
        ViewSwitcher.loadView(Route.LOCALIDAD.recoverView());
    }

    @FXML
    void miPC(ActionEvent event) {
        ViewSwitcher.loadView(Route.PACIENTE.recoverView());
    }

    @FXML
    void miPR(ActionEvent event) {
        ViewSwitcher.loadView(Route.PROPIETARIO.recoverView());
    }

    @FXML
    void miTR(ActionEvent event) {
        ViewSwitcher.loadView(Route.TRATAMIENTO.recoverView());
    }

    @FXML
    void miVC(ActionEvent event) {
        ViewSwitcher.loadView(Route.VACUNA.recoverView());
    }

    @FXML
    void miQuit(ActionEvent event) {
        Platform.exit();
    }

    public void setView(Node node) {
        contentPane.setCenter(node);
    }
}

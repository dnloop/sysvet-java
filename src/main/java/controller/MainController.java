package controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;
import org.controlsfx.control.BreadCrumbBar;

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
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import utils.routes.Route;
import utils.routes.RouteExtra;
import utils.viewswitcher.ViewSwitcher;

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
    private BreadCrumbBar<String> naviBar;

    @FXML
    private JFXButton mainView;

    @FXML
    private JFXButton btnIndPac;

    @FXML
    private JFXButton btnIndFC;

    @FXML
    private JFXButton btnIndHC;

    @FXML
    private JFXButton btnIndExamen;

    @FXML
    private JFXButton btnIndInter;

    @FXML
    private JFXButton btnIndDesp;

    @FXML
    private JFXButton btnIndVac;

    @FXML
    private JFXButton btnIndTC;

    @FXML
    private JFXButton btnIndProp;

    @FXML
    private JFXButton btnIndCC;

    @FXML
    private BorderPane contentPane;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    private Label lblClock;

    @FXML
    void initialize() {

        bindToTime();
        /*
         * First module to be displayed it runs in the ui thread because otherwise the
         * animation is not shown. This should be further investigated to implement a
         * better solution when the database records increase.
         */
        Platform.runLater(() -> {
            ViewSwitcher.loadView("/fxml/charts/total.fxml");
            String path[] = { "Principal" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        });

        naviBar.setAutoNavigationEnabled(false);

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
        String path[] = { "Principal" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
    }

    @FXML
    void indexCC(ActionEvent event) {
        ViewSwitcher.loadView(Route.CUENTACORRIENTE.indexView());
        String path[] = { "Cuenta Corriente", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexDesp(ActionEvent event) {
        ViewSwitcher.loadView(Route.DESPARASITACION.indexView());
        String path[] = { "Desparastación", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexExamen(ActionEvent event) {
        ViewSwitcher.loadView(Route.EXAMEN.indexView());
        String path[] = { "Exámen", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexFC(ActionEvent event) {
        ViewSwitcher.loadView(Route.FICHACLINICA.indexView());
        String path[] = { "Ficha Clínica", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexInter(ActionEvent event) {
        ViewSwitcher.loadView(Route.INTERNACION.indexView());
        String path[] = { "Internación", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexLoc(ActionEvent event) {
        ViewSwitcher.loadView(Route.LOCALIDAD.indexView());
        String path[] = { "Localidad", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexPac(ActionEvent event) {
        ViewSwitcher.loadView(Route.PACIENTE.indexView());
        String path[] = { "Paciente", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexProp(ActionEvent event) {
        ViewSwitcher.loadView(Route.PROPIETARIO.indexView());
        String path[] = { "Propietario", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexTC(ActionEvent event) {
        ViewSwitcher.loadView(Route.TRATAMIENTO.indexView());
        String path[] = { "Tratamiento", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexVac(ActionEvent event) {
        ViewSwitcher.loadView(Route.VACUNA.indexView());
        String path[] = { "Vacunación", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void indexHC(ActionEvent event) {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.indexView());
        String path[] = { "Historia Clínica", "Índice" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miCC(ActionEvent event) {
        ViewSwitcher.loadView(Route.CUENTACORRIENTE.recoverView());
        String path[] = { "Cuenta Corriente", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miDP(ActionEvent event) {
        ViewSwitcher.loadView(Route.DESPARASITACION.recoverView());
        String path[] = { "Desparacitación", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miEX(ActionEvent event) {
        ViewSwitcher.loadView(Route.EXAMEN.recoverView());
        String path[] = { "Exámen", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miFC(ActionEvent event) {
        ViewSwitcher.loadView(Route.FICHACLINICA.recoverView());
        String path[] = { "Ficha Clínica", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miHC(ActionEvent event) {
        ViewSwitcher.loadView(Route.HISTORIACLINICA.recoverView());
        String path[] = { "Historia Clínica", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miIT(ActionEvent event) {
        ViewSwitcher.loadView(Route.INTERNACION.recoverView());
        String path[] = { "Internación", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miPC(ActionEvent event) {
        ViewSwitcher.loadView(Route.PACIENTE.recoverView());
        String path[] = { "Paciente", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miPR(ActionEvent event) {
        ViewSwitcher.loadView(Route.PROPIETARIO.recoverView());
        String path[] = { "Propietario", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miTR(ActionEvent event) {
        ViewSwitcher.loadView(Route.TRATAMIENTO.recoverView());
        String path[] = { "Tratamiento", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miVC(ActionEvent event) {
        ViewSwitcher.loadView(Route.VACUNA.recoverView());
        String path[] = { "Vacunación", "Eliminados" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
        ViewSwitcher.loadingDialog.showStage();
        ViewSwitcher.loadingDialog.startTask();
    }

    @FXML
    void miNew(ActionEvent event) {
        ViewSwitcher vs = new ViewSwitcher();
        SelectController sc = vs.loadModal(RouteExtra.NEW.getPath());
        sc.setStage(vs.getStage());
        sc.showModal();
        String path[] = { "Principal", "Nuevo Registro" };
        ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
    }

    @FXML
    void miQuit(ActionEvent event) {
        Platform.exit();
    }

    /* Class methods */

    /**
     * Inserts graphic node in the main content pane.
     * 
     * @param node
     */
    public void setView(Node node) {
        contentPane.setCenter(node);
    }

    /**
     * Constructs the view hierarchy of the BreadCrumbBar.
     * 
     * @param path - The current view in the hierarchy.
     * @return The model used on the BreadCrumbBar.
     * 
     * @see BreadCrumbBar
     */
    public TreeItem<String> setPath(String[] path) {
        TreeItem<String> model = BreadCrumbBar.buildTreeModel(path);
        return model;
    }

    /**
     * Displays the current view hierarchy on a BreadCrumbBar.
     * 
     * @param model - The path of the current view.
     */
    public void setNavi(TreeItem<String> model) {
        naviBar.setSelectedCrumb(model);
    }
}

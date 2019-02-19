package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.HibernateUtil;
import utils.PaneSwitcher;
import utils.ViewHelper;

public class MainController {

    protected static final Logger log = (Logger) LogManager.getLogger(MainController.class);
    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lblNavi"
    private Label lblNavi; // Value injected by FXMLLoader

    @FXML // fx:id="x31"
    private Font x31; // Value injected by FXMLLoader

    @FXML // fx:id="x41"
    private Color x41; // Value injected by FXMLLoader

    @FXML // fx:id="indexList"
    private JFXListView<String> indexList; // Value injected by FXMLLoader

    @FXML // fx:id="loaderList"
    private JFXListView<String> loaderList; // Value injected by FXMLLoader

    @FXML // fx:id="deletedList"
    private JFXListView<String> deletedList; // Value injected by FXMLLoader

    @FXML
    private JFXButton mainView;

    @FXML
    private BorderPane contentPane;

    @FXML // fx:id="x3"
    private Font x3; // Value injected by FXMLLoader

    @FXML // fx:id="x4"
    private Color x4; // Value injected by FXMLLoader

    @FXML // fx:id="lblClock"
    private Label lblClock; // Value injected by FXMLLoader

    PaneSwitcher switcher = new PaneSwitcher();

    ViewHelper helper = new ViewHelper();

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lblNavi != null : "fx:id=\"lblNavi\" was not injected: check your FXML file 'Untitled'.";
        assert x31 != null : "fx:id=\"x31\" was not injected: check your FXML file 'Untitled'.";
        assert x41 != null : "fx:id=\"x41\" was not injected: check your FXML file 'Untitled'.";
        assert indexList != null : "fx:id=\"indexList\" was not injected: check your FXML file 'Untitled'.";
        assert loaderList != null : "fx:id=\"loaderList\" was not injected: check your FXML file 'Untitled'.";
        assert deletedList != null : "fx:id=\"deletedList\" was not injected: check your FXML file 'Untitled'.";
        assert contentPane != null : "fx:id=\"contentPane\" was not injected: check your FXML file 'Untitled'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'Untitled'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'Untitled'.";
        assert lblClock != null : "fx:id=\"lblClock\" was not injected: check your FXML file 'Untitled'.";

        try {
            HibernateUtil.setUp();
        } catch (Exception e) {
            log.debug(marker, "Unable establish the session. " + e.getMessage());
        }

        /* BEGIN SIDEBAR */
        ObservableList<String> items = FXCollections.observableArrayList (
                "Pacientes", 
                "Propietarios", 
                "Vacunas", 
                "Desparasitaciones", 
                "Fichas Clínicas", 
                "Exámenes", 
                "Historia clínica",
                "Internaciones",
                "Tratamientos Clínicas",
                "Retornos",
                "Localidades",
                "Cuentas Corrientes"
                );

        indexList.setItems(items);
        loaderList.setItems(items);
        deletedList.setItems(items);

        indexList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, 
                            String old_val, String new_val) {
                        switcher.setFxmlPath(
                                helper.route(0, indexList.getSelectionModel().getSelectedIndex()
                                        ));
                        
                        switcher.switcher(contentPane);
                    }
                });
        loaderList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, 
                            String old_val, String new_val) {
                        switcher.setFxmlPath(
                                helper.route(1, loaderList.getSelectionModel().getSelectedIndex()
                                        ));
                        switcher.switcher(contentPane);
                    }
                });
        deletedList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, 
                            String old_val, String new_val) {
                        switcher.setFxmlPath(
                                helper.route(2, deletedList.getSelectionModel().getSelectedIndex()
                                        ));
                        switcher.switcher(contentPane);
                    }
                });
        mainView.setOnAction((event) -> {
            //            switcher.setFxmlPath("/fxml/main.fxml");
            //            switcher.switcher(contentPane);
            log.info(marker, "Loading main stage" );
            try {
                String fxmlFile = "/fxml/main.fxml";
                VBox loader = FXMLLoader.load(getClass().getResource(fxmlFile));
                Stage stage = (Stage) mainView.getScene().getWindow();
                Scene scene = new Scene(loader);
                stage.setScene(scene);
            } catch (IOException e) {
                log.debug(marker, "DEAD IO" + e.getMessage());
            }
        });
        /* END SIDEBAR */
    }
}

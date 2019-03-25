package controller.exam;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import model.FichasClinicas;
import utils.ViewSwitcher;

public class IndexControllerTest {
    protected static final Logger log = (Logger) LogManager.getLogger(IndexControllerTest.class);
    // static FichasClinicasHome daoFC = new FichasClinicasHome();
    // static ExamenGeneralHome daoEG = new ExamenGeneralHome();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<FichasClinicas> indexE;

    @FXML
    private BorderPane contentPane;

    private int num;

    public void setNumero(Integer num) {
        this.num = num;
    }

    public void setContentPane(BorderPane contentPane) {
        this.contentPane = contentPane;
    }

    @FXML
    void initialize() {

//        Platform.runLater(() -> {
        System.out.println("parameters added: " + num);
        btnShow.setOnAction((event) -> {
            String fxml = "/fxml/exam/show.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            try {
                Node node = fxmlLoader.load();
                ShowControllerTest controller = fxmlLoader.getController();
                num = 24;
                controller.setNumero(num);
                setView(node);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        btnDelete.setOnAction((event) -> {
            log.info("Item deleted.");
        });
        // TODO add search filter
//        });
    }

    private void setView(Node node) {
        ViewSwitcher.loadNode(node);
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }
}

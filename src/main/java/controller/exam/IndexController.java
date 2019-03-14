package controller.exam;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.ExamenGeneralHome;
import dao.FichasClinicasHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.FichasClinicas;
import model.Pacientes;

public class IndexController {
    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);
    static FichasClinicasHome daoFC = new FichasClinicasHome();
    static ExamenGeneralHome daoEG = new ExamenGeneralHome();

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

    private Integer id;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexE != null : "fx:id=\"indexE\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<FichasClinicas, Pacientes> pacientes = new JFXTreeTableColumn<FichasClinicas, Pacientes>(
                "Pacientes - (ficha)");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<FichasClinicas, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue().getPacientes()));

        log.info("loading table items");

        ObservableList<FichasClinicas> fichasClinicas = FXCollections.observableArrayList();
        fichasClinicas = loadTable(fichasClinicas);

        TreeItem<FichasClinicas> root = new RecursiveTreeItem<FichasClinicas>(fichasClinicas,
                RecursiveTreeObject::getChildren);
        indexE.getColumns().setAll(pacientes);

        indexE.setShowRoot(false);
        indexE.setRoot(root);

        // Handle ListView selection changes.
        indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            id = newValue.getValue().getId();
            log.info("Item selected.");
        });

        btnShow.setOnAction((event) -> {
            Parent rootNode;
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deworming/modalDialog.fxml"));
            Window node = ((Node) event.getSource()).getScene().getWindow();
            try {
                rootNode = (Parent) fxmlLoader.load();
                ShowController sc = fxmlLoader.getController();
                sc.setId(id);
                stage.setScene(new Scene(rootNode));
                stage.setTitle("Examen General");
//                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(node);
                stage.setOnHidden((stageEvent) -> {
                    indexE.refresh();
                });
                sc.showStage(stage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnDelete.setOnAction((event) -> {
            daoEG.delete(id);
            indexE.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexE.refresh();
            log.info("Item deleted.");
        });
        // TODO add search filter
    }

    static ObservableList<FichasClinicas> loadTable(ObservableList<FichasClinicas> fichasClinicas) {
        List<FichasClinicas> list = daoFC.displayRecordsWithExams();
        for (FichasClinicas item : list)
            fichasClinicas.add(item);
        return fichasClinicas;
    }
}

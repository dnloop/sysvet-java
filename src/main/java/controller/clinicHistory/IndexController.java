package controller.clinicHistory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import controller.exam.ShowController;
import dao.FichasClinicasHome;
import dao.HistoriaClinicaHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.FichasClinicas;
import model.PacienteFicha;
import model.Pacientes;
import utils.ViewSwitcher;

public class IndexController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnShow;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<PacienteFicha> indexCH;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private static HistoriaClinicaHome daoHC = new HistoriaClinicaHome();

    private Integer id;

    Parent root;

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexCH != null : "fx:id=\"indexCH\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<PacienteFicha, Pacientes> pacientes = new JFXTreeTableColumn<PacienteFicha, Pacientes>(
                "Pacientes - (ficha)");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<PacienteFicha, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue().getPaciente()));

        log.info("loading table items");

        ObservableList<PacienteFicha> fichasClinicas = FXCollections.observableArrayList();
        fichasClinicas = loadTable(fichasClinicas);

        TreeItem<PacienteFicha> root = new RecursiveTreeItem<PacienteFicha>(fichasClinicas,
                RecursiveTreeObject::getChildren);

        indexCH.getColumns().setAll(pacientes);
        indexCH.setShowRoot(false);
        indexCH.setRoot(root);

        // Handle ListView selection changes.
        indexCH.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            id = newValue.getValue().getId();
            log.info("Item selected.");
        });

        btnShow.setOnAction((event) -> {
            if (id != null)
                displayShow(event);
            else
                displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null)
                confirmDialog();
            else
                displayWarning();
        });
        // TODO add search filter
    }

    /**
     *
     * Class Methods
     *
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void setView(Node node) {
        ViewSwitcher.loadNode(node);
    }

    static ObservableList<PacienteFicha> loadTable(ObservableList<PacienteFicha> fichasClinicas) {

        List<Object> list = daoFC.displayRecordsWithExams();
        for (Object object : list) {
            Object[] result = (Object[]) object;
            PacienteFicha ficha = new PacienteFicha();
            ficha.setId((Integer) result[0]);
            ficha.setPaciente((Pacientes) result[1]);
            fichasClinicas.add(ficha);
        }

        return fichasClinicas;
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/clinicHistory/show.fxml"));
        FichasClinicas fc = daoFC.showById(id);
        try {
            Node node = fxmlLoader.load();
            ShowController sc = fxmlLoader.getController();
            sc.setFC(fc);
            setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText("¿Desea eliminar el registro?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            daoHC.delete(id);
            indexCH.getSelectionModel().getSelectedItem().getParent().getChildren().remove(id - 1);
            indexCH.refresh();
            log.info("Item deleted.");
        }
    }

    private void displayWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }
}
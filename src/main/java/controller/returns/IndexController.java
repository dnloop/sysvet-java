package controller.returns;

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

import controller.exam.ShowController;
import dao.FichasClinicasHome;
import dao.RetornosHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.FichasClinicas;
import model.Pacientes;
import model.Record;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
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
    private Pagination tablePagination;

    @FXML
    private JFXTreeTableView<Record<Pacientes>> indexRT;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static FichasClinicasHome daoFC = new FichasClinicasHome();

    private static RetornosHome daoRT = new RetornosHome();

    private Integer id;

    final ObservableList<Record<Pacientes>> fichasClinicas = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnNew != null : "fx:id=\"btnNew\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexRT != null : "fx:id=\"indexRT\" was not injected: check your FXML file 'index.fxml'.";

        JFXTreeTableColumn<Record<Pacientes>, Pacientes> pacientes = new JFXTreeTableColumn<Record<Pacientes>, Pacientes>(
                "Pacientes - (ficha)");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory((
                TreeTableColumn.CellDataFeatures<Record<Pacientes>, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                        param.getValue().getValue().getRecord()));

        log.info("loading table items");

        fichasClinicas.setAll(loadTable(fichasClinicas));

        TreeItem<Record<Pacientes>> root = new RecursiveTreeItem<Record<Pacientes>>(fichasClinicas,
                RecursiveTreeObject::getChildren);

        indexRT.getColumns().setAll(pacientes);
        indexRT.setShowRoot(false);
        indexRT.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexRT, fichasClinicas, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexRT.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id = newValue.getValue().getId();
                log.info("Item selected.");
            }
        });

        btnShow.setOnAction((event) -> {
            if (id != null)
                displayShow(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (id != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    daoRT.delete(id);
                    TreeItem<Record<Pacientes>> selectedItem = indexRT.getSelectionModel().getSelectedItem();
                    indexRT.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexRT.refresh();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
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

    private static ObservableList<Record<Pacientes>> loadTable(ObservableList<Record<Pacientes>> fichasClinicas) {
        List<Object> list = daoFC.displayRecordsWithReturns();
        for (Object object : list) {
            Object[] result = (Object[]) object;
            Record<Pacientes> ficha = new Record<Pacientes>();
            ficha.setId((Integer) result[0]);
            ficha.setRecord((Pacientes) result[1]);
            fichasClinicas.add(ficha);
        }

        return fichasClinicas;
    }

    private void displayShow(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.RETORNO.showView()));
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
}
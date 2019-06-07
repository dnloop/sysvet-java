package controller.returns;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import dao.RetornosHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Pacientes;
import model.Retornos;
import utils.DialogBox;
import utils.TableUtil;
import utils.ViewSwitcher;

public class RecoverController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnRecover;

    @FXML
    private JFXTreeTableView<Retornos> indexRT;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private static RetornosHome dao = new RetornosHome();

    private Retornos retorno;

    private Pacientes paciente;

    final ObservableList<Retornos> returnsList = FXCollections.observableArrayList();

    private TreeItem<Retornos> root;

    @SuppressWarnings("unchecked")

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexRT != null : "fx:id=\"indexRT\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";
        Platform.runLater(() -> {
            JFXTreeTableColumn<Retornos, Pacientes> pacientes = new JFXTreeTableColumn<Retornos, Pacientes>(
                    "Pacientes - (ficha)");
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Retornos, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            JFXTreeTableColumn<Retornos, Date> fecha = new JFXTreeTableColumn<Retornos, Date>("Fecha");
            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Retornos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            log.info("loading table items");

            returnsList.setAll(dao.showByPaciente(paciente));
            root = new RecursiveTreeItem<Retornos>(returnsList, RecursiveTreeObject::getChildren);

            indexRT.getColumns().setAll(pacientes, fecha);
            indexRT.setShowRoot(false);
            indexRT.setRoot(root);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexRT, returnsList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexRT.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    retorno = newValue.getValue();
                    log.info("Item selected.");
                }
            });

            btnRecover.setOnAction((event) -> {
                if (retorno != null)
                    if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                        dao.recover(retorno.getId());
                        TreeItem<Retornos> selectedItem = indexRT.getSelectionModel().getSelectedItem();
                        indexRT.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        refreshTable();
                        log.info("Item recovered.");
                        retorno = null;
                    } else
                        DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexRT.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty())
                        return true;

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (item.getValue().toString().toLowerCase().contains(lowerCaseFilter))
                        return true;

                    return false;
                });
            });
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void refreshTable() {
        returnsList.clear();
        returnsList.setAll(dao.showByPaciente(paciente));
        root = new RecursiveTreeItem<Retornos>(returnsList, RecursiveTreeObject::getChildren);
        indexRT.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexRT, returnsList, tablePagination, index, 20));
    }
}

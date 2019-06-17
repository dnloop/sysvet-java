package controller.treatment;

import java.io.IOException;
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

import dao.TratamientosHome;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.FichasClinicas;
import model.Pacientes;
import model.Tratamientos;
import utils.DialogBox;
import utils.Route;
import utils.TableUtil;
import utils.ViewSwitcher;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTreeTableView<Tratamientos> indexTR;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static TratamientosHome dao = new TratamientosHome();

    private FichasClinicas ficha;

    private Tratamientos tratamiento;

    private TreeItem<Tratamientos> root;

    final ObservableList<Tratamientos> pacientesList = FXCollections.observableArrayList();

    // Table columns
    private JFXTreeTableColumn<Tratamientos, Pacientes> pacientes = new JFXTreeTableColumn<Tratamientos, Pacientes>(
            "Pacientes");

    private JFXTreeTableColumn<Tratamientos, String> tratamientoColumn = new JFXTreeTableColumn<Tratamientos, String>(
            "Tratamiento");

    private JFXTreeTableColumn<Tratamientos, Date> fecha = new JFXTreeTableColumn<Tratamientos, Date>("Fecha");

    private JFXTreeTableColumn<Tratamientos, Date> hora = new JFXTreeTableColumn<Tratamientos, Date>("Hora");

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert indexTR != null : "fx:id=\"indexTR\" was not injected: check your FXML file 'show.fxml'.";

        Platform.runLater(() -> {
            pacientes.setPrefWidth(200);
            pacientes.setCellValueFactory((
                    TreeTableColumn.CellDataFeatures<Tratamientos, Pacientes> param) -> new ReadOnlyObjectWrapper<Pacientes>(
                            param.getValue().getValue().getFichasClinicas().getPacientes()));

            tratamientoColumn.setPrefWidth(150);
            tratamientoColumn.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Tratamientos, String> param) -> new ReadOnlyStringWrapper(
                            param.getValue().getValue().getTratamiento()));

            fecha.setPrefWidth(150);
            fecha.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Tratamientos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getFecha()));

            hora.setPrefWidth(150);
            hora.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<Tratamientos, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                            param.getValue().getValue().getHora()));

            log.info("loading table items");
            pacientesList.setAll(dao.showByFicha((ficha)));

            root = new RecursiveTreeItem<Tratamientos>(pacientesList, RecursiveTreeObject::getChildren);

            indexTR.getColumns().setAll(pacientes, tratamientoColumn, fecha, hora);
            indexTR.setShowRoot(false);
            indexTR.setRoot(root);
            tablePagination.setPageFactory(
                    (index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));

            // Handle ListView selection changes.
            indexTR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    tratamiento = newValue.getValue();
                    log.info("Item selected." + ficha.getId());
                }
            });

            btnEdit.setOnAction((event) -> {
                if (tratamiento != null)
                    displayModal(event);
                else
                    DialogBox.displayWarning();
            });

            btnDelete.setOnAction((event) -> {
                if (tratamiento != null) {
                    if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                        dao.delete(tratamiento.getId());
                        TreeItem<Tratamientos> selectedItem = indexTR.getSelectionModel().getSelectedItem();
                        indexTR.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                        refreshTable();
                        tratamiento = null;
                        DialogBox.displaySuccess();
                        log.info("Item deleted.");
                    }
                } else
                    DialogBox.displayWarning();
            });
            // search filter
            txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
                indexTR.setPredicate(item -> {
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

    public void setObject(FichasClinicas ficha) {
        this.ficha = ficha;
    }

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        Parent rootNode;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Route.TRATAMIENTO.modalView()));
        Window node = ((Node) event.getSource()).getScene().getWindow();
        try {
            rootNode = (Parent) fxmlLoader.load();
            ModalDialogController sc = fxmlLoader.getController();
            sc.setObject(tratamiento);
            log.info("Loaded Item.");
            stage.setScene(new Scene(rootNode));
            stage.setTitle("Editar - Tratamiento");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(node);
            stage.setOnHidden((stageEvent) -> {
                refreshTable();
            });
            sc.showModal(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        pacientesList.clear();
        pacientesList.setAll(dao.showByFicha(ficha));
        root = new RecursiveTreeItem<Tratamientos>(pacientesList, RecursiveTreeObject::getChildren);
        indexTR.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexTR, pacientesList, tablePagination, index, 20));
    }
}

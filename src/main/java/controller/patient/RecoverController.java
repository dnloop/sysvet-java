package controller.patient;

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

import dao.PacientesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;

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
    private JFXTreeTableView<Pacientes> indexPA;

    @FXML
    private Pagination tablePagination;

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    // protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private PacientesHome dao = new PacientesHome();

    private Pacientes paciente;

    final ObservableList<Pacientes> pacientesList = FXCollections.observableArrayList();

    private TreeItem<Pacientes> root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'recover.fxml'.";
        assert btnRecover != null : "fx:id=\"btnRecover\" was not injected: check your FXML file 'recover.fxml'.";
        assert indexPA != null : "fx:id=\"indexPA\" was not injected: check your FXML file 'recover.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'recover.fxml'.";

        log.info("creating table");

        JFXTreeTableColumn<Pacientes, String> nombre = new JFXTreeTableColumn<Pacientes, String>("Nombre");
        nombre.setPrefWidth(200);
        nombre.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getNombre()));

        JFXTreeTableColumn<Pacientes, String> especie = new JFXTreeTableColumn<Pacientes, String>("Especie");
        especie.setPrefWidth(200);
        especie.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getEspecie()));

        JFXTreeTableColumn<Pacientes, String> raza = new JFXTreeTableColumn<Pacientes, String>("Raza");
        raza.setPrefWidth(200);
        raza.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getRaza()));

        JFXTreeTableColumn<Pacientes, String> sexo = new JFXTreeTableColumn<Pacientes, String>("Sexo");
        sexo.setPrefWidth(200);
        sexo.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getSexo()));

        JFXTreeTableColumn<Pacientes, String> temp = new JFXTreeTableColumn<Pacientes, String>("Temperamento");
        temp.setPrefWidth(200);
        temp.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getTemperamento()));

        JFXTreeTableColumn<Pacientes, String> pelaje = new JFXTreeTableColumn<Pacientes, String>("Pelaje");
        pelaje.setPrefWidth(200);
        pelaje.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, String> param) -> new ReadOnlyStringWrapper(
                        param.getValue().getValue().getPelaje()));

        JFXTreeTableColumn<Pacientes, Date> fecha = new JFXTreeTableColumn<Pacientes, Date>("Fecha");
        fecha.setPrefWidth(150);
        fecha.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Pacientes, Date> param) -> new ReadOnlyObjectWrapper<Date>(
                        param.getValue().getValue().getFechaNacimiento()));
        log.info("loading table items");

        pacientesList.setAll(dao.displayRecords());

        root = new RecursiveTreeItem<Pacientes>(pacientesList, RecursiveTreeObject::getChildren);
        indexPA.getColumns().setAll(nombre, especie, raza, sexo, pelaje, fecha);
        indexPA.setShowRoot(false);
        indexPA.setRoot(root);
        tablePagination
                .setPageFactory((index) -> TableUtil.createPage(indexPA, pacientesList, tablePagination, index, 20));

        // Handle ListView selection changes.
        indexPA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                paciente = newValue.getValue();
                log.info("Item selected.");
            }
        });

        btnRecover.setOnAction((event) -> {
            if (paciente != null) {
                if (DialogBox.confirmDialog("¿Desea recuperar el registro?")) {
                    dao.recover(paciente.getId());
                    TreeItem<Pacientes> selectedItem = indexPA.getSelectionModel().getSelectedItem();
                    indexPA.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                    indexPA.refresh();
                    log.info("Item recovered.");
                }
            } else
                DialogBox.displayWarning();
        });
    }
}

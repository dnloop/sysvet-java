package controller.currentAccount;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
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

import dao.CuentasCorrientesHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.CuentasCorrientes;
import model.Propietarios;

public class IndexController {

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);
//    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    static CuentasCorrientesHome dao = new CuentasCorrientesHome();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXTreeTableView<CuentasCorrientes> indexCA;
    
    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    private CuentasCorrientes cc;

    private Integer id;

    Parent root;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        assert indexCA != null : "fx:id=\"indexPatient\" was not injected: check your FXML file 'index.fxml'.";
        
        // this should be a helper class to load everything
        log.info("creating table");

        JFXTreeTableColumn<CuentasCorrientes, Propietarios> propietarios = new JFXTreeTableColumn<CuentasCorrientes, Propietarios>("Propietarios");
        propietarios.setPrefWidth(200);
        propietarios.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CuentasCorrientes, Propietarios> param) -> 
                new ReadOnlyObjectWrapper<Propietarios>(param.getValue().getValue().getPropietarios())
        );

        JFXTreeTableColumn<CuentasCorrientes, String> descripcion = new JFXTreeTableColumn<CuentasCorrientes, String>("Descripción");
        descripcion.setPrefWidth(200);
        descripcion.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CuentasCorrientes, String> param) -> 
                new ReadOnlyStringWrapper(param.getValue().getValue().getDescripcion())
        );

        JFXTreeTableColumn<CuentasCorrientes, BigDecimal> monto = new JFXTreeTableColumn<CuentasCorrientes, BigDecimal>("Monto");
        monto.setPrefWidth(150);
        monto.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CuentasCorrientes, BigDecimal> param) -> 
                new ReadOnlyObjectWrapper<BigDecimal>(param.getValue().getValue().getMonto())
        );

        JFXTreeTableColumn<CuentasCorrientes, Date> fecha = new JFXTreeTableColumn<CuentasCorrientes, Date>("Fecha");
        fecha.setPrefWidth(150);
        fecha.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CuentasCorrientes, Date> param) -> 
                new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getFecha())
        );
        log.info("loading table items");

        ObservableList<CuentasCorrientes> cuentasCorrientes = FXCollections.observableArrayList();
        cuentasCorrientes = loadTable(cuentasCorrientes);

        TreeItem<CuentasCorrientes> root = 
                new RecursiveTreeItem<CuentasCorrientes>(cuentasCorrientes, RecursiveTreeObject::getChildren);
        indexCA.getColumns().setAll(fecha, propietarios, descripcion, monto);
        indexCA.setShowRoot(false);
        indexCA.setRoot(root);

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cc = newValue.getValue();
            id = this.cc.getId();
            log.info("Item selected.");
        });

        btnEdit.setOnAction((event) -> {
          Parent rootNode;
          Stage stage = new Stage();
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/currentAccount/modalDialog.fxml"));
          Window node = ((Node)event.getSource()).getScene().getWindow();
          try {
              rootNode = (Parent) fxmlLoader.load();
              ModalDialogController mdc = fxmlLoader.getController();
              mdc.setObject(this.cc);
              stage.setScene(new Scene(rootNode));
              stage.setTitle("Cuenta Corriente");
              stage.initModality(Modality.APPLICATION_MODAL);
              stage.initOwner(node);
              stage.setOnHidden((stageEvent) -> {
                  indexCA.refresh();
              });
              mdc.showModal(stage);
              
          } catch (IOException e) {
              e.printStackTrace();
          }
      });

        btnDelete.setOnAction((event) -> {
            dao.delete(id);
            indexCA.getSelectionModel()
                .getSelectedItem()
                .getParent()
                .getChildren()
                .remove(id -1);
            indexCA.refresh();
            log.info("Item deleted.");
        });
        //TODO add search filter
    }

    static ObservableList<CuentasCorrientes> loadTable(ObservableList<CuentasCorrientes> cuentasCorrientes) {
        List <CuentasCorrientes> list = dao.displayRecords();
        for ( CuentasCorrientes item : list)
            cuentasCorrientes.add(item);
        return cuentasCorrientes;
    }
}

package controller.deworming;

import java.io.IOException;
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

import dao.DesparasitacionesHome;
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
import model.Desparasitaciones;
import model.Pacientes;

public class IndexController {
    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);
    static DesparasitacionesHome dao = new DesparasitacionesHome();

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
    private JFXTreeTableView<Desparasitaciones> indexD;

    private Desparasitaciones desparacitaciones;
    
    private Integer id;

    @FXML
    void initialize() {
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'index.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'index.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'index.fxml'.";
        assert indexD != null : "fx:id=\"indexD\" was not injected: check your FXML file 'index.fxml'.";
        
        JFXTreeTableColumn<Desparasitaciones, Pacientes> pacientes = new JFXTreeTableColumn<Desparasitaciones, Pacientes>("Pacientes");
        pacientes.setPrefWidth(200);
        pacientes.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, Pacientes> param) -> 
                new ReadOnlyObjectWrapper<Pacientes>(param.getValue().getValue().getPacientes())
        );

        JFXTreeTableColumn<Desparasitaciones, String> tratamiento = new JFXTreeTableColumn<Desparasitaciones, String>("Tratamiento");
        tratamiento.setPrefWidth(200);
        tratamiento.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, String> param) -> 
                new ReadOnlyStringWrapper(param.getValue().getValue().getTratamiento())
        );

        JFXTreeTableColumn<Desparasitaciones, Date> fecha = new JFXTreeTableColumn<Desparasitaciones, Date>("Fecha");
        fecha.setPrefWidth(150);
        fecha.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> 
                new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getFecha())
        );

        JFXTreeTableColumn<Desparasitaciones, Date> fechaProxima = new JFXTreeTableColumn<Desparasitaciones, Date>("Fecha Próxima");
        fechaProxima.setPrefWidth(150);
        fechaProxima.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Desparasitaciones, Date> param) -> 
                new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getFechaProxima())
        );

        log.info("loading table items");

        ObservableList<Desparasitaciones> desparasitaciones = FXCollections.observableArrayList();
        desparasitaciones = loadTable(desparasitaciones);

        TreeItem<Desparasitaciones> root = 
                new RecursiveTreeItem<Desparasitaciones>(desparasitaciones, RecursiveTreeObject::getChildren);
        indexD.getColumns().setAll(fecha, pacientes, tratamiento, fechaProxima);
        indexD.setShowRoot(false);
        indexD.setRoot(root);

        // Handle ListView selection changes.
        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            desparacitaciones = newValue.getValue();
            id = desparacitaciones.getId();
            log.info("Item selected.");
        });

        btnEdit.setOnAction((event) -> {
          Parent rootNode;
          Stage stage = new Stage();
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deworming/modalDialog.fxml"));
          Window node = ((Node)event.getSource()).getScene().getWindow();
          try {
              rootNode = (Parent) fxmlLoader.load();
              ModalDialogController mdc = fxmlLoader.getController();
              mdc.setObject(desparacitaciones);
              stage.setScene(new Scene(rootNode));
              stage.setTitle("Desparasitaciones");
              stage.initModality(Modality.APPLICATION_MODAL);
              stage.initOwner(node);
              stage.setOnHidden((stageEvent) -> {
                  indexD.refresh();
              });
              mdc.showModal(stage);
              
          } catch (IOException e) {
              e.printStackTrace();
          }
      });

        btnDelete.setOnAction((event) -> {
            dao.delete(id);
            indexD.getSelectionModel()
                .getSelectedItem()
                .getParent()
                .getChildren()
                .remove(id -1);
            indexD.refresh();
            log.info("Item deleted.");
        });
        //TODO add search filter
    }

    static ObservableList<Desparasitaciones> loadTable(ObservableList<Desparasitaciones> desparasitaciones) {
        List <Desparasitaciones> list = dao.displayRecords();
        for ( Desparasitaciones item : list)
            desparasitaciones.add(item);
        return desparasitaciones;
    }
}

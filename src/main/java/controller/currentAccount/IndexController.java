package controller.currentAccount;

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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.CuentasCorrientes;
import model.Propietarios;
import utils.PaneSwitcher;
import utils.ViewHelper;

public class IndexController {

    protected static final Logger log = (Logger) LogManager.getLogger(IndexController.class);
//    protected static final Marker marker = MarkerManager.getMarker("CLASS");
    CuentasCorrientesHome dao = new CuentasCorrientesHome();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXTreeTableView<CuentasCorrientes> indexCA;
    
    @FXML
    private JFXButton btnMostrar;

    private CuentasCorrientes cc;

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
        List <CuentasCorrientes> list = dao.displayRecords();

        for ( CuentasCorrientes item : list)
            cuentasCorrientes.add(item);

        final TreeItem<CuentasCorrientes> root = 
                new RecursiveTreeItem<CuentasCorrientes>(cuentasCorrientes, RecursiveTreeObject::getChildren);
        indexCA.getColumns().setAll(fecha, propietarios, descripcion, monto);
        indexCA.setShowRoot(false);
        indexCA.setRoot(root);

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.cc = newValue.getValue();
            System.out.println(cc.getMonto());
        });

        btnMostrar.setOnAction((event) -> {
            ViewHelper helper = new ViewHelper();
            PaneSwitcher switcher = new PaneSwitcher(helper.route(3, 11));
            ModalDialogController mdc = switcher.getController();
            switcher.modalView("Cuenta Corriente");
        });
        // TODO get selected
        //TODO add search filter
    }

}

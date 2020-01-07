
package controller.exam;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.ExamenGeneralHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ExamenGeneral;
import model.Pacientes;
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

public class ShowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilter;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<ExamenGeneral> indexE;

    @FXML
    private Pagination tablePagination;

    // Table columns
    @FXML
    private TableColumn<ExamenGeneral, Date> fecha;

    @FXML
    private TableColumn<ExamenGeneral, String> pesoCorporal;

    @FXML
    private TableColumn<ExamenGeneral, String> tempCorporal;

    @FXML
    private TableColumn<ExamenGeneral, String> frecResp;

    @FXML
    private TableColumn<ExamenGeneral, String> deshidratacion;

    @FXML
    private TableColumn<ExamenGeneral, String> amplitud;

    @FXML
    private TableColumn<ExamenGeneral, String> tipo;

    @FXML
    private TableColumn<ExamenGeneral, String> ritmo;

    @FXML
    private TableColumn<ExamenGeneral, String> frecCardio;

    @FXML
    private TableColumn<ExamenGeneral, String> tllc;

    @FXML
    private TableColumn<ExamenGeneral, String> escleral;

    @FXML
    private TableColumn<ExamenGeneral, String> pulso;

    @FXML
    private TableColumn<ExamenGeneral, String> palperal;

    @FXML
    private TableColumn<ExamenGeneral, String> vulvar;

    @FXML
    private TableColumn<ExamenGeneral, String> peneana;

    @FXML
    private TableColumn<ExamenGeneral, String> submandibular;

    @FXML
    private TableColumn<ExamenGeneral, String> preescapular;

    @FXML
    private TableColumn<ExamenGeneral, String> precrural;

    @FXML
    private TableColumn<ExamenGeneral, String> inguinal;

    @FXML
    private TableColumn<ExamenGeneral, String> otros;

    @FXML
    private TableColumn<ExamenGeneral, String> popliteo;

    @FXML
    private TableColumn<ExamenGeneral, String> bucal;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private ExamenGeneralHome dao = new ExamenGeneralHome();

    private ExamenGeneral examenGeneral;

    private Pacientes paciente;

    final ObservableList<ExamenGeneral> examenList = FXCollections.observableArrayList();

    private FilteredList<ExamenGeneral> filteredData;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'show.fxml'.";
        assert txtFilter != null : "fx:id=\"txtFilter\" was not injected: check your FXML file 'show.fxml'.";
        assert btnEdit != null : "fx:id=\"btnEdit\" was not injected: check your FXML file 'show.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'show.fxml'.";
        assert tablePagination != null : "fx:id=\"tablePagination\" was not injected: check your FXML file 'show.fxml'.";
        assert indexE != null : "fx:id=\"indexE\" was not injected: check your FXML file 'show.fxml'.";
        assert fecha != null : "fx:id=\"fecha\" was not injected: check your FXML file 'show.fxml'.";
        assert pesoCorporal != null : "fx:id=\"pesoCorporal\" was not injected: check your FXML file 'show.fxml'.";
        assert tempCorporal != null : "fx:id=\"tempCorporal\" was not injected: check your FXML file 'show.fxml'.";
        assert frecResp != null : "fx:id=\"frecResp\" was not injected: check your FXML file 'show.fxml'.";
        assert deshidratacion != null : "fx:id=\"deshidratacion\" was not injected: check your FXML file 'show.fxml'.";
        assert amplitud != null : "fx:id=\"amplitud\" was not injected: check your FXML file 'show.fxml'.";
        assert tipo != null : "fx:id=\"tipo\" was not injected: check your FXML file 'show.fxml'.";
        assert ritmo != null : "fx:id=\"ritmo\" was not injected: check your FXML file 'show.fxml'.";
        assert frecCardio != null : "fx:id=\"frecCardio\" was not injected: check your FXML file 'show.fxml'.";
        assert tllc != null : "fx:id=\"tllc\" was not injected: check your FXML file 'show.fxml'.";
        assert escleral != null : "fx:id=\"escleral\" was not injected: check your FXML file 'show.fxml'.";
        assert pulso != null : "fx:id=\"pulso\" was not injected: check your FXML file 'show.fxml'.";
        assert palperal != null : "fx:id=\"palperal\" was not injected: check your FXML file 'show.fxml'.";
        assert vulvar != null : "fx:id=\"vulvar\" was not injected: check your FXML file 'show.fxml'.";
        assert peneana != null : "fx:id=\"peneana\" was not injected: check your FXML file 'show.fxml'.";
        assert submandibular != null : "fx:id=\"submandibular\" was not injected: check your FXML file 'show.fxml'.";
        assert preescapular != null : "fx:id=\"preescapular\" was not injected: check your FXML file 'show.fxml'.";
        assert precrural != null : "fx:id=\"precrural\" was not injected: check your FXML file 'show.fxml'.";
        assert inguinal != null : "fx:id=\"inguinal\" was not injected: check your FXML file 'show.fxml'.";
        assert otros != null : "fx:id=\"otros\" was not injected: check your FXML file 'show.fxml'.";
        assert popliteo != null : "fx:id=\"popliteo\" was not injected: check your FXML file 'show.fxml'.";
        assert bucal != null : "fx:id=\"bucal\" was not injected: check your FXML file 'show.fxml'.";

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        pesoCorporal.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getPesoCorporal())));

        tempCorporal.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getTempCorporal())));

        frecResp.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getFrecResp())));

        deshidratacion.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDeshidratacion())));

        amplitud.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getAmplitud()));

        tipo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTipo()));

        ritmo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getRitmo()));

        frecCardio.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getFrecCardio())));

        tllc.setCellValueFactory((param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getTllc())));

        escleral.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getEscleral()));

        pulso.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPulso()));

        palperal.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPalperal()));

        vulvar.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getVulvar()));

        peneana.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPeneana()));

        submandibular.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getSubmandibular()));

        preescapular.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPreescapular()));

        precrural.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPrecrural()));

        inguinal.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getOtros()));

        otros.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getInguinal()));

        popliteo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPopliteo()));

        bucal.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getBucal()));

        // Handle ListView selection changes.
        indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                examenGeneral = newValue;
                log.info("Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.EXAMEN.indexView());
            String path[] = { "Exámen", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.getLoadingDialog().startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (examenGeneral != null)
                displayModal(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (examenGeneral != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                    dao.delete(examenGeneral.getId());
                    ExamenGeneral selectedItem = indexE.getSelectionModel().getSelectedItem();
                    examenList.remove(selectedItem);
                    indexE.setItems(examenList);
                    refreshTable();
                    examenGeneral = null;
                    DialogBox.displaySuccess();
                    log.info("Item deleted.");
                }
            } else
                DialogBox.displayWarning();
        });
        // search filter
        filteredData = new FilteredList<>(examenList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ficha -> newValue == null || newValue.isEmpty()
                    || ficha.getAmplitud().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getBucal().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getEscleral().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getInguinal().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getOtros().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPopliteo().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPrecrural().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPreescapular().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPeneana().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getVulvar().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getPulso().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getRitmo().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getSubmandibular().toLowerCase().contains(newValue.toLowerCase())
                    || ficha.getTipo().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    } // FichasClinicas

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void displayModal(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.EXAMEN.modalView(), "Examen General", event);
        vs.getStage().setOnHidden((stageEvent) -> {
            indexE.refresh();
        });
        mc.setObject(examenGeneral);
        mc.showModal(vs.getStage());
    }

    private void refreshTable() {
        examenList.clear();
        loadDao();
        ViewSwitcher.getLoadingDialog().startTask();
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, examenList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<ExamenGeneral> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexE.comparatorProperty());
        indexE.setItems(sortedData);
    }

    public void loadDao() {
        log.info("Loading table items");
        Task<List<ExamenGeneral>> task = dao.showByPaciente(paciente);

        task.setOnSucceeded(event -> {
            examenList.setAll(task.getValue());
            indexE.setItems(examenList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexE, examenList, tablePagination, index, 20));
//            ViewSwitcher.getLoadingDialog().getStage().close();
            log.info("Table loaded.");
        });

        ViewSwitcher.getLoadingDialog().setTask(task);
    }
}

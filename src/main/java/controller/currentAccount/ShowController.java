package controller.currentAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.CuentasCorrientesHome;
import dao.EntregaHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import model.CuentasCorrientes;
import model.Entrega;
import model.Propietarios;
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
import utils.validator.HibernateValidator;
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
    private JFXTextField txtPay;

    @FXML
    private JFXButton btnPay;

    @FXML
    private JFXButton btnCancelDebt;

    @FXML
    private Accordion details;

    @FXML
    private TitledPane tpCA;

    @FXML
    private JFXTextField txtSubCA;

    @FXML
    private TitledPane tpPay;

    @FXML
    private JFXTextField txtSubPay;

    @FXML
    private TableView<CuentasCorrientes> indexCA;

    @FXML
    private TableView<Entrega> indexPay;

    @FXML
    private JFXComboBox<String> comboType;

    @FXML
    private Pagination tablePagination;

    @FXML
    private TableColumn<CuentasCorrientes, Propietarios> tcPropietario;

    @FXML
    private TableColumn<CuentasCorrientes, String> tcDescripcion;

    @FXML
    private TableColumn<CuentasCorrientes, BigDecimal> tcMonto;

    @FXML
    private TableColumn<CuentasCorrientes, Date> tcFecha;

    @FXML
    private TableColumn<Entrega, BigDecimal> tcMontoPago;

    @FXML
    private TableColumn<Entrega, Date> tcFechaPago;

    @FXML
    private JFXTextField txtTotal;

    protected static final Logger log = (Logger) LogManager.getLogger(ShowController.class);

    private CuentasCorrientesHome dao = new CuentasCorrientesHome();

    private EntregaHome daoPay = new EntregaHome();

    private CuentasCorrientes cuentaCorriente;

    private Entrega entrega;

    private Propietarios propietario;

    final ObservableList<CuentasCorrientes> cuentasList = FXCollections.observableArrayList();

    final ObservableList<Entrega> entregaList = FXCollections.observableArrayList();

    private FilteredList<CuentasCorrientes> filteredData;

    private BigDecimal total = new BigDecimal(0);

    private BigDecimal subtotal = new BigDecimal(0);

    private String deuda;

    @FXML
    void initialize() {
        log.info("Loading details]");
        tcPropietario.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Propietarios>(param.getValue().getPropietarios()));

        tcDescripcion.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcion()));

        tcMonto.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<BigDecimal>(param.getValue().getMonto()));

        tcFecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        tcMontoPago.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<BigDecimal>(param.getValue().getMonto()));

        tcFechaPago.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        comboType.getItems().addAll("Efectivo", "Tarjeta Crédito", "Tarjeta Débito", "Mercado Pago", "Otro");
        comboType.setValue("Efectivo");

        // Handle ListView selection changes.
        indexCA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cuentaCorriente = newValue;
                log.info("Item selected.");
            }
        });

        btnBack.setOnAction((event) -> {
            IndexController ic = new IndexController();
            ic.setView(Route.CUENTACORRIENTE.indexView());
            String path[] = { "Cuenta Corriente", "Índice" };
            ViewSwitcher.setNavi(ViewSwitcher.setPath(path));
            ViewSwitcher.loadingDialog.startTask();
        });

        btnEdit.setOnAction((event) -> {
            if (cuentaCorriente != null)
                displayModal(event);
            else
                DialogBox.displayWarning();
        });

        btnDelete.setOnAction((event) -> {
            if (cuentaCorriente != null) {
                if (DialogBox.confirmDialog("¿Desea eliminar el registro?"))
                    deleteItem();
            } else
                DialogBox.displayWarning();
        });

        btnPay.setOnAction((event) -> {
            registerPayment();
        });

        btnCancelDebt.setOnAction((event) -> {
            cancellDebt();
        });

        // search filter
        filteredData = new FilteredList<>(cuentasList, p -> true);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cuenta -> newValue == null || newValue.isEmpty()
                    || cuenta.getPropietarios().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tablePagination.getCurrentPageIndex(), 20);
        });

    }

    /*
     * Class Methods
     */

    public void setObject(Propietarios propietario) {
        this.propietario = propietario;
    } // Propietarios

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }

    private void refreshTable() {
        cuentasList.clear();
        loadDao();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void displayModal(Event event) {
        ViewSwitcher vs = new ViewSwitcher();
        ModalDialogController mc = vs.loadModal(Route.CUENTACORRIENTE.modalView(), "Editar - Cuenta Corriente", event);
        mc.setObject(cuentaCorriente);
        vs.getStage().setOnHiding((stageEvent) -> {
            refreshTable();
        });
        mc.showModal(vs.getStage());
    }

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, cuentasList.size());
        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<CuentasCorrientes> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexCA.comparatorProperty());
        indexCA.setItems(sortedData);
    }

    private void deleteItem() {
        try {
            dao.delete(cuentaCorriente.getId());
            CuentasCorrientes selectedItem = indexCA.getSelectionModel().getSelectedItem();
            cuentasList.remove(selectedItem);
            indexCA.setItems(cuentasList);
            indexCA.refresh();
            // Abstract this logic
            total = new BigDecimal(0);
            for (CuentasCorrientes cuentasCorrientes : cuentasList)
                total = total.add(cuentasCorrientes.getMonto()).setScale(2, RoundingMode.HALF_UP);
            txtTotal.setText(total.toString());
            DialogBox.displaySuccess();
            cuentaCorriente = null;
            log.info("Item deleted.");
        } catch (RuntimeException e) {
            DialogBox.displayError();
        }
    }

    private void registerPayment() {
        BigDecimal montoPago = !txtPay.getText().isEmpty() ? new BigDecimal(txtPay.getText()) : null;
        BigDecimal montoDeuda = new BigDecimal(txtTotal.getText());
        boolean pago = false;
        if (montoPago != null)
            pago = montoPago.compareTo(BigDecimal.ZERO) > 0;
        boolean deuda = montoDeuda.compareTo(BigDecimal.ZERO) > 0;

        Date fecha = new Date();
        entrega = new Entrega();
        entrega.setFecha(fecha);
        entrega.setCreatedAt(fecha);
        entrega.setMonto(montoPago);
        entrega.setPropietarios(propietario);
        entrega.setTipo(comboType.getSelectionModel().getSelectedItem());

        if (HibernateValidator.validate(entrega)) {
            int result = montoPago.compareTo(montoDeuda);
            if (result == -1) {
                if (montoPago.compareTo(montoDeuda) > 0) {
                    entrega.setMonto(montoPago);
                    entrega.setPendiente(montoDeuda.subtract(montoPago));
                    txtTotal.setText(entrega.getPendiente().toString());
                    indexPay.getItems().add(entrega);
                    daoPay.add(entrega);
                    log.info("payment added");
                    DialogBox.displaySuccess();
                } else {
                    DialogBox.setHeader("Aviso");
                    DialogBox.setContent("La entrega debe ser mayor a 0.");
                    DialogBox.displayCustomWarning();
                }
            } else if (result == 1) {
                entrega.setPendiente(montoDeuda);
                cancellDebt();
            } else if (result == 0)
                if (pago && deuda)
                    cancellDebt();
                else {
                    DialogBox.setHeader("Aviso");
                    DialogBox.setContent("No se registra deuda pendiente.");
                    DialogBox.displayCustomWarning();
                }

        } else {
            DialogBox.setHeader("Fallo en la carga del registro");
            DialogBox.setContent(HibernateValidator.getError());
            DialogBox.displayError();
            HibernateValidator.resetError();
            log.error("failed to create record");
        }
    }

    private void cancellDebt() {
        int id = propietario.getId();
        entrega.setMonto(entrega.getPendiente());
        entrega.setPendiente(new BigDecimal(0));
        daoPay.add(entrega);
        dao.deleteAll(id);
        daoPay.deleteAll(id);
        indexCA.getItems().clear();
        indexPay.getItems().clear();
        txtSubCA.setText("0");
        txtSubPay.setText("0");
        txtTotal.setText("0");
        txtPay.setText("");
        log.info("debt cancelled");
        DialogBox.displaySuccess();
    }

    void loadDao() {
        log.info("Loading table items");
        Task<List<CuentasCorrientes>> taskCA = dao.showByOwner(propietario);
        Task<List<Entrega>> taskPay = daoPay.showByOwner(propietario);

        taskCA.setOnSucceeded(event -> {
            cuentasList.setAll(taskCA.getValue());
            indexCA.setItems(cuentasList);
            tablePagination
                    .setPageFactory((index) -> TableUtil.createPage(indexCA, cuentasList, tablePagination, index, 20));
            ViewSwitcher.loadingDialog.getStage().close();
            log.info("Loaded total debt.");

            for (CuentasCorrientes cuentasCorrientes : cuentasList)
                total = total.add(cuentasCorrientes.getMonto()).setScale(2, RoundingMode.HALF_UP);
            deuda = total.toString();
            txtSubCA.setText(deuda);
            txtTotal.setText(deuda);
        });

        taskPay.setOnSucceeded(event -> {
            entregaList.setAll(taskPay.getValue());
            indexPay.setItems(entregaList);
            ViewSwitcher.loadingDialog.getStage().close();
            log.info("Loaded payments.");

            for (Entrega entrega : entregaList)
                subtotal = subtotal.add(entrega.getMonto()).setScale(2, RoundingMode.HALF_UP);

            if (entregaList.size() > 0)
                entrega = entregaList.get(entregaList.size() - 1); // last element
            txtSubPay.setText(subtotal.toString());
            if (entrega != null) {
                String str = entrega.getPendiente().toString();
                txtTotal.setText(str);
            }
        });

        ViewSwitcher.loadingDialog.setTask(taskCA);
        ViewSwitcher.loadingDialog.setTask(taskPay);
    }

}

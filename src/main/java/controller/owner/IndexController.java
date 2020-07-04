package controller.owner;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.PropietariosHome;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Localidades;
import model.Propietarios;
import utils.DialogBox;
import utils.RecordInsertCallback;
import utils.TableUtil;
import utils.routes.Route;
import utils.routes.RouteExtra;
import utils.viewswitcher.ViewSwitcher;

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
	private JFXButton btnEdit;

	@FXML
	private JFXButton btnDelete;

	@FXML
	private TableView<Propietarios> indexO;

	@FXML
	private Pagination tablePagination;

	@FXML
	TableColumn<Propietarios, String> tcNombre;

	@FXML
	TableColumn<Propietarios, String> tcApellido;

	@FXML
	TableColumn<Propietarios, String> tcDomicilio;

	@FXML
	TableColumn<Propietarios, String> tcTelCel;

	@FXML
	TableColumn<Propietarios, String> tcTelFijo;

	@FXML
	TableColumn<Propietarios, String> tcMail;

	@FXML
	TableColumn<Propietarios, Localidades> tcLocalidad;

	@FXML
	private Tab tabAccount;

	private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private PropietariosHome dao = new PropietariosHome();

	private Propietarios propietario;

	final ObservableList<Propietarios> ownersList = FXCollections.observableArrayList();

	private FilteredList<Propietarios> filteredData;

	/**
	 * Boolean property used to confirm the database has changed and perform
	 * subsequent actions.
	 */
	private SimpleBooleanProperty updated = new SimpleBooleanProperty(false);

	/**
	 * Used as a callback to confirm the database has changed and the controls needs
	 * to be updated.
	 */
	private RecordInsertCallback created = new RecordInsertCallback() {

		@Override
		public void recordCreated(boolean record) {
			if (record)
				updated.setValue(Boolean.FALSE);
		}
	};

	public boolean isUpdated() {
		return updated.get();
	}

	public void setUpdated(boolean updated) {
		this.updated.setValue(updated);
	}

	@FXML
	void initialize() {

		log.info(marker, "Creating table");

		tcNombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

		tcApellido.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getApellido()));

		tcDomicilio.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDomicilio()));

		tcTelCel.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTelCel()));

		tcTelFijo.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTelFijo()));

		tcMail.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMail()));

		tcLocalidad.setCellValueFactory(
				(param) -> new ReadOnlyObjectWrapper<Localidades>(param.getValue().getLocalidades()));

		// Handle ListView selection changes.
		indexO.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				propietario = newValue;
				log.info(marker, "Item selected.");
			}
		});

		btnNew.setOnAction((event) -> displayNew());

		btnEdit.setOnAction((event) -> {
			if (propietario != null)
				displayEdit();
			else
				DialogBox.displayWarning();
		});

		btnDelete.setOnAction((event) -> {
			if (propietario != null) {
				if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
					dao.delete(propietario.getId());
					Propietarios selectedItem = indexO.getSelectionModel().getSelectedItem();
					ownersList.remove(selectedItem);
					indexO.setItems(ownersList);
					propietario = null;
					DialogBox.displaySuccess();
					log.info("Item deleted.");
				}
			} else
				DialogBox.displayWarning();
		});
		// search filter
		filteredData = new FilteredList<>(ownersList, p -> true);
		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(owner -> newValue == null || newValue.isEmpty()
					|| owner.getNombre().toLowerCase().contains(newValue.toLowerCase())
					|| owner.getApellido().toLowerCase().contains(newValue.toLowerCase()));
			changeTableView(tablePagination.getCurrentPageIndex(), 20);
		});
	}

	/*
	 * Class Methods
	 */

	private void displayNew() {
		ViewSwitcher.loadModal(Route.PROPIETARIO.newView(), "Nuevo elemento - Propietario", true);
		NewController nc = ViewSwitcher.getController(Route.PROPIETARIO.newView());
		nc.setCreatedCallback(created);
		nc.loadDao();
		updated.addListener((obs, oldVal, newVal) -> {
			if (!updated.getValue()) {
				refreshTable(nc.getID());
				nc.cleanFields();
			}
		});
		ViewSwitcher.modalStage.showAndWait();
		ViewSwitcher.loadingDialog.startTask();
	}

	private void displayEdit() {
		ViewSwitcher.loadModal(Route.PROPIETARIO.modalView(), "Propietario", true);
		ModalDialogController mc = ViewSwitcher.getController(Route.PROPIETARIO.modalView());
		ViewSwitcher.modalStage.setOnHidden((stageEvent) -> {
			indexO.refresh();
		});
		mc.setObject(propietario);
		mc.loadDao();
		ViewSwitcher.modalStage.showAndWait();
		ViewSwitcher.loadingDialog.startTask();
	}

	private void refreshTable(Integer id) {
		ownersList.add(dao.showById(id));
		indexO.setItems(ownersList);
		log.info(marker, "[ Owners List ] - updated.");
	}

	private void changeTableView(int index, int limit) {
		int fromIndex = index * limit;
		int toIndex = Math.min(fromIndex + limit, ownersList.size());
		int minIndex = Math.min(toIndex, filteredData.size());
		SortedList<Propietarios> sortedData = new SortedList<>(
				FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
		sortedData.comparatorProperty().bind(indexO.comparatorProperty());
		indexO.setItems(sortedData);
	}

	public void loadDao() {
		log.info(marker, "loading table items");
		Task<List<Propietarios>> task = dao.displayRecords();

		task.setOnSucceeded(event -> {
			ownersList.setAll(task.getValue());
			indexO.setItems(ownersList);
			tablePagination
					.setPageFactory((index) -> TableUtil.createPage(indexO, ownersList, tablePagination, index, 20));
			log.info("[ Owners ] - loaded");
		});

		ViewSwitcher.loadingDialog.addTask(task);
	}

	public void loadCurrentAccounts() {
		tabAccount.setContent(ViewSwitcher.getView(RouteExtra.CUENTASCORRIENTESMAIN.getPath()));
	}
}

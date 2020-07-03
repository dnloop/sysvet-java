package controller.clinicalFile;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dao.HistoriaClinicaHome;
import dao.TratamientosHome;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import model.FichasClinicas;
import model.HistoriaClinica;
import model.Tratamientos;
import utils.DialogBox;
import utils.routes.Route;
import utils.routes.RouteExtra;
import utils.viewswitcher.ViewSwitcher;

public class OverviewController {

	@FXML
	private TableView<Tratamientos> tvTratamiento;

	@FXML
	private TableView<HistoriaClinica> tvHistoria;

	@FXML
	private BorderPane bpContent;

	@FXML
	private TableColumn<Tratamientos, String> tcIdTratamiento;

	@FXML
	private TableColumn<Tratamientos, String> tcTratamiento;

	@FXML
	private TableColumn<HistoriaClinica, String> tcIdDescripcion;

	@FXML
	private TableColumn<HistoriaClinica, String> tcDescripcion;

	@FXML
	private JFXTextField txtFilterHC;

	@FXML
	private JFXButton btnNewCH;

	@FXML
	private JFXButton btnEditCH;

	@FXML
	private JFXButton btnDeleteCH;

	@FXML
	private JFXTextField txtFilterT;

	@FXML
	private JFXButton btnNewT;

	@FXML
	private JFXButton btnEditT;

	@FXML
	private JFXButton btnDeleteT;

	private static final Logger log = (Logger) LogManager.getLogger(ViewController.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private FichasClinicas clinicalFile;

	private HistoriaClinica clinicHistory;

	private Tratamientos treatment;

	private TratamientosHome daoTR = new TratamientosHome();

	private HistoriaClinicaHome daoHC = new HistoriaClinicaHome();

	private final ObservableList<Tratamientos> treatmentList = FXCollections.observableArrayList();

	private final ObservableList<HistoriaClinica> clinicHistoryList = FXCollections.observableArrayList();

	private FilteredList<HistoriaClinica> filteredDataCH;

	private FilteredList<Tratamientos> filteredDataT;

	@FXML
	void initialize() {

		tcIdTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

		tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

		tcIdDescripcion.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

		tcDescripcion
				.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcionEvento()));

		tvHistoria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				clinicHistory = newValue;
				log.info(marker, "Item selected.");
			}
		});

		tvHistoria.setOnMouseClicked((event) -> {
			if (event.getButton() == MouseButton.PRIMARY)
				if (event.getClickCount() == 2 && clinicHistory != null)
					editHistory(clinicHistory);
		});

		tvTratamiento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				treatment = newValue;
				log.info(marker, "Item selected (treatment).");
			}
		});

		tvTratamiento.setOnMouseClicked((event) -> {
			if (event.getButton() == MouseButton.PRIMARY)
				if (event.getClickCount() == 2 && treatment != null)
					editTreatment(treatment);
		});

		btnEditCH.setOnAction((event) -> {
			if (clinicHistory != null)
				editHistory(clinicHistory);
			else
				DialogBox.displayWarning();
		});

		btnEditT.setOnAction((event) -> {
			if (treatment != null)
				editTreatment(treatment);
			else
				DialogBox.displayWarning();
		});

		btnNewCH.setOnAction((event) -> newClinicHistory(event));

		btnNewT.setOnAction((event) -> newTreatment(event));

		// search filter
		filteredDataCH = new FilteredList<>(clinicHistoryList, p -> true);
		txtFilterHC.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredDataCH.setPredicate(clinicHistory -> newValue == null || newValue.isEmpty()
					|| clinicHistory.getDescripcionEvento().toLowerCase().contains(newValue.toLowerCase()));

		});

		filteredDataT = new FilteredList<>(treatmentList, p -> true);
		txtFilterT.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredDataT.setPredicate(treatment -> newValue == null || newValue.isEmpty()
					|| treatment.getTratamiento().toLowerCase().contains(newValue.toLowerCase()));

		});
	}

	/*
	 * Class Methods
	 */

	@FXML
	void deleteClinicHistory(ActionEvent event) {
		if (clinicHistory != null) {
			if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
				daoHC.delete(clinicHistory.getId());
				HistoriaClinica selectedItem = tvHistoria.getSelectionModel().getSelectedItem();
				clinicHistoryList.remove(selectedItem);
				tvHistoria.setItems(clinicHistoryList);
				clinicHistory = null;
				DialogBox.displaySuccess();
				log.info(marker, "Clinic History deleted.");
			}
		} else
			DialogBox.displayWarning();
	}

	@FXML
	void deleteTreatment(ActionEvent event) {
		if (treatment != null) {
			if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
				daoTR.delete(treatment.getId());
				Tratamientos selectedItem = tvTratamiento.getSelectionModel().getSelectedItem();
				treatmentList.remove(selectedItem);
				tvTratamiento.setItems(treatmentList);
				treatment = null;
				DialogBox.displaySuccess();
				log.info(marker, "Treatment deleted.");
			}
		} else
			DialogBox.displayWarning();
	}

	@FXML
	void newClinicHistory(ActionEvent event) {
		ViewSwitcher.loadModal(Route.HISTORIACLINICA.newView(), "Nuevo Elemento - Historia Clínica", true);
		controller.clinicHistory.NewController nc = ViewSwitcher.getController(Route.HISTORIACLINICA.newView());
		nc.setComboBox(clinicalFile);
		ViewSwitcher.modalStage.setOnHidden(stageEvent -> {
			refreshClinicHistory(nc.getID());
			nc.cleanFields();
		});

		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newTreatment(ActionEvent event) {
		ViewSwitcher.loadModal(Route.TRATAMIENTO.newView(), "Nuevo Elemento - Tratamiento", true);
		controller.treatment.NewController nc = ViewSwitcher.getController(Route.TRATAMIENTO.newView());
		nc.setComboBox(clinicalFile);
		ViewSwitcher.modalStage.setOnHidden(stageEvent -> {
			refreshTreatment(nc.getID());
			nc.cleanFields();
		});

		ViewSwitcher.modalStage.showAndWait();
	}

	/**
	 * Takes the ID of the clinical file to load the relationships:
	 * {@link Tratamientos} and {@link HistoriaClinica}.
	 * 
	 * @param clinicalFile - The clinical file that contains the relationships.
	 */
	public void loadTables(FichasClinicas clinicalFile) {
		log.info(marker, "Loading table items.");
		Task<List<Tratamientos>> task1 = loadTreatment(clinicalFile);
		Task<List<HistoriaClinica>> task2 = loadClinicHistory(clinicalFile);
		ViewSwitcher.loadingDialog.addTask(task1);
		ViewSwitcher.loadingDialog.addTask(task2);
	}

	/**
	 * Clinic History contains records of events associated to a Clinical file.
	 * 
	 * @param clinicalFile - Parent record
	 * @return The list of events
	 */
	public Task<List<HistoriaClinica>> loadClinicHistory(FichasClinicas clinicalFile) {
		Task<List<HistoriaClinica>> task = daoHC.showByPatient(clinicalFile);

		task.setOnSucceeded(event -> {
			clinicHistoryList.setAll(task.getValue());
			tvHistoria.setItems(clinicHistoryList);
			log.info("History Loaded.");
		});

		return task;
	}

	/**
	 * Treatments corresponding to a Clinical file.
	 * 
	 * @param clinicalFile - Parent record
	 * @return The list of treatments
	 */
	public Task<List<Tratamientos>> loadTreatment(FichasClinicas clinicalFile) {
		Task<List<Tratamientos>> task = daoTR.showByFicha(clinicalFile);

		task.setOnSucceeded(event -> {
			treatmentList.setAll(task.getValue());
			tvTratamiento.setItems(treatmentList);

			log.info(marker, "Treatments Loaded.");
		});

		return task;
	}

	public void setObject(FichasClinicas clinicalFile) {
		this.clinicalFile = clinicalFile;
	}

	/**
	 * Load the Border pane with the clinical file details.
	 */
	public void loadContent() {
		log.info(marker, "[ Loading panes ]");
		Node node = ViewSwitcher.getView(RouteExtra.CLINICVIEW.getPath());
		controller.clinicalFile.ViewController vc = ViewSwitcher.getController(RouteExtra.CLINICVIEW.getPath());
		vc.setObject(clinicalFile);
		vc.loadFields();
		bpContent.setCenter(node);
	}

	/**
	 * Modal dialog for treatment edition.
	 * 
	 * @see Tratamientos
	 */
	private void editTreatment(Tratamientos treatment) {
		ViewSwitcher.loadModal(Route.TRATAMIENTO.modalView(), "Tratamiento", true);
		controller.treatment.ModalDialogController mc = ViewSwitcher.getController(Route.TRATAMIENTO.modalView());
		mc.setObject(treatment);
		ViewSwitcher.modalStage.setOnHidden(stageEvent -> {
			tvTratamiento.refresh();
		});
		ViewSwitcher.modalStage.showAndWait();
	}

	/**
	 * Modal dialog to edit an event in the clinical file
	 * 
	 * @see HistoriaClinica
	 */
	private void editHistory(HistoriaClinica clinicHistory) {
		ViewSwitcher.loadModal(Route.HISTORIACLINICA.modalView(), "Historia Clínica", true);
		controller.clinicHistory.ModalDialogController mc = ViewSwitcher
				.getController(Route.HISTORIACLINICA.modalView());
		mc.setObject(clinicHistory);
		ViewSwitcher.modalStage.setOnHidden(stageEvent -> {
			tvHistoria.refresh();
		});
		ViewSwitcher.modalStage.showAndWait();
	}

	private void refreshClinicHistory(Integer id) {
		clinicHistoryList.add(daoHC.showById(id));
		tvHistoria.setItems(clinicHistoryList);
		log.info(marker, "[ Clinic History List ] - updated.");
	}

	private void refreshTreatment(Integer id) {
		treatmentList.add(daoTR.showById(id));
		tvTratamiento.setItems(treatmentList);
		log.info(marker, "[ Treatments List ] - updated.");
	}
}

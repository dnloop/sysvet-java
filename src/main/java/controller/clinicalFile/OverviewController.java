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
import javafx.application.Platform;
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

	@SuppressWarnings("unchecked")
	@FXML
	void initialize() {

		log.info(marker, "creating table");

		tcIdTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

		tcTratamiento.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

		tcIdDescripcion.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));

		tcDescripcion
				.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcionEvento()));

		log.info(marker, "Loading Clinical File details");
		Platform.runLater(() -> loadContent());

		tvHistoria.getColumns().setAll(tcIdDescripcion, tcDescripcion);
		tvTratamiento.getColumns().setAll(tcIdTratamiento, tcTratamiento);

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
		ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
			refreshClinicHistory();
		});
		ViewSwitcher.modalStage.showAndWait();
	}

	@FXML
	void newTreatment(ActionEvent event) {
		ViewSwitcher.loadModal(Route.TRATAMIENTO.newView(), "Nuevo Elemento - Tratamiento", true);
		controller.treatment.NewController nc = ViewSwitcher.getController(Route.TRATAMIENTO.newView());
		nc.setComboBox(clinicalFile);
		ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
			refreshTreatment();
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
		Task<List<Tratamientos>> task1 = daoTR.showByFicha(clinicalFile);

		Task<List<HistoriaClinica>> task2 = daoHC.showByPatient(clinicalFile);

		task1.setOnSucceeded(event -> {
			treatmentList.setAll(task1.getValue());
			tvTratamiento.setItems(treatmentList);
			ViewSwitcher.loadingDialog.getStage().close();
			log.info(marker, "Treatments Loaded.");
		});

		task2.setOnSucceeded(event -> {
			clinicHistoryList.setAll(task2.getValue());
			tvHistoria.setItems(clinicHistoryList);
			ViewSwitcher.loadingDialog.getStage().close();
			log.info("History Loaded.");
		});

		ViewSwitcher.loadingDialog.addTask(task1);
		ViewSwitcher.loadingDialog.addTask(task2);
		ViewSwitcher.loadingDialog.startTask();
	}

	public void setObject(FichasClinicas clinicalFile) {
		this.clinicalFile = clinicalFile;
	}

	/**
	 * Load the Border pane with the clinical file details.
	 */
	private void loadContent() {
		log.info(marker, "[ Loading panes ]");
		Node node = ViewSwitcher.getView(RouteExtra.CLINICVIEW.getPath());
		controller.clinicalFile.ViewController vc = ViewSwitcher.getController(RouteExtra.CLINICVIEW.getPath());
		vc.setObject(this.clinicalFile);
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
		ViewSwitcher.modalStage.setOnHiding(stageEvent -> {
			this.treatment = null;
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
		ViewSwitcher.modalStage.setOnHiding(stageEvent -> {
			this.clinicHistory = null;
		});
		ViewSwitcher.modalStage.showAndWait();
	}

	/*
	 * TODO Sub optimal refresh method.
	 */
	private void refreshClinicHistory() {
		clinicHistoryList.clear();
		loadTables(clinicalFile);
		ViewSwitcher.loadingDialog.startTask();
	}

	/*
	 * TODO Sub optimal refresh method.
	 */
	private void refreshTreatment() {
		treatmentList.clear();
		loadTables(clinicalFile);
		ViewSwitcher.loadingDialog.startTask();
	}
}

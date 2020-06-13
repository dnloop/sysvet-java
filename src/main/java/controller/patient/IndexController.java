package controller.patient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dao.DesparasitacionesHome;
import dao.ExamenGeneralHome;
import dao.FichasClinicasHome;
import dao.InternacionesHome;
import dao.PacientesHome;
import dao.VacunasHome;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import model.Desparasitaciones;
import model.ExamenGeneral;
import model.FichasClinicas;
import model.Internaciones;
import model.Pacientes;
import model.Propietarios;
import model.Vacunas;
import utils.DialogBox;
import utils.TableUtil;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

public class IndexController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txtFilterPat;

    @FXML
    private JFXButton btnShowPat;

    @FXML
    private JFXButton btnNewPat;

    @FXML
    private JFXButton btnEditPat;

    @FXML
    private JFXButton btnDeletePat;

    @FXML
    private Pagination tpPatient;

    @FXML
    private TableView<Pacientes> indexPA;

    @FXML
    private TableColumn<Pacientes, String> nombre;

    @FXML
    private TableColumn<Pacientes, String> especie;

    @FXML
    private TableColumn<Pacientes, Date> fecha;

    @FXML
    private TableColumn<Pacientes, Propietarios> propietario;

    @FXML
    private TitledPane descripcionVc;

    @FXML
    private JFXTextField txtFilterVac;

    @FXML
    private JFXButton btnNewVac;

    @FXML
    private JFXButton btnEditVac;

    @FXML
    private JFXButton btnDeleteVac;

    @FXML
    private TableView<Vacunas> indexVC;

    @FXML
    private TableColumn<Vacunas, String> descripcionV;

    @FXML
    private TableColumn<Vacunas, Date> fechaV;

    @FXML
    private JFXTextField txtFilterDew;

    @FXML
    private JFXButton btnNewDew;

    @FXML
    private JFXButton btnEditDew;

    @FXML
    private JFXButton btnDeleteDew;

    @FXML
    private TableView<Desparasitaciones> indexD;

    @FXML
    private TableColumn<Desparasitaciones, String> tcTratamientoD;

    @FXML
    private TableColumn<Desparasitaciones, String> tcTipoD;

    @FXML
    private TableColumn<Desparasitaciones, Date> tcFechaD;

    @FXML
    private TableColumn<Desparasitaciones, Date> tcFechaProximaD;

    @FXML
    private JFXTextField txtFilterHos;

    @FXML
    private JFXButton btnNewHos;

    @FXML
    private JFXButton btnEditHos;

    @FXML
    private JFXButton btnDeleteHos;

    @FXML
    private TableView<Internaciones> indexHS;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaIngresoH;

    @FXML
    private TableColumn<Internaciones, Date> tcFechaAltaH;

    @FXML
    private JFXTextField txtFilterFc;

    @FXML
    private JFXButton btnNewFc;

    @FXML
    private JFXButton btnEditFc;

    @FXML
    private JFXButton btnDeleteFc;

    @FXML
    private TableView<FichasClinicas> indexFC;

    @FXML
    private TableColumn<FichasClinicas, String> tcIdFc;

    @FXML
    private TableColumn<FichasClinicas, String> tcMotivoFc;

    @FXML
    private JFXTextField txtNombreP;

    @FXML
    private JFXTextField txtEspecieP;

    @FXML
    private JFXTextField txtRazaP;

    @FXML
    private JFXTextField txtSexoP;

    @FXML
    private JFXTextField txtTempP;

    @FXML
    private JFXTextField txtPelajeP;

    @FXML
    private JFXTextField txtPesoP;

    @FXML
    private JFXTextField txtFechaNacP;

    @FXML
    private JFXTextField txtPropietarioP;

    @FXML
    private ImageView ivFoto;

    @FXML
    private JFXTextField txtFilterExam;

    @FXML
    private JFXButton btnEditExam;

    @FXML
    private JFXButton btnDeleteExam;

    @FXML
    private Pagination tpExam;

    @FXML
    private TableView<ExamenGeneral> indexE;

    @FXML
    private TableColumn<ExamenGeneral, Date> fechaE;

    @FXML
    private TableColumn<ExamenGeneral, String> pesoCorporalE;

    @FXML
    private TableColumn<ExamenGeneral, String> tempCorporalE;

    @FXML
    private TableColumn<ExamenGeneral, String> frecRespE;

    @FXML
    private TableColumn<ExamenGeneral, String> deshidratacionE;

    @FXML
    private TableColumn<ExamenGeneral, String> amplitudE;

    @FXML
    private TableColumn<ExamenGeneral, String> tipoE;

    @FXML
    private TableColumn<ExamenGeneral, String> ritmoE;

    @FXML
    private TableColumn<ExamenGeneral, String> frecCardioE;

    @FXML
    private TableColumn<ExamenGeneral, String> tllcE;

    @FXML
    private TableColumn<ExamenGeneral, String> escleralE;

    @FXML
    private TableColumn<ExamenGeneral, String> pulsoE;

    @FXML
    private TableColumn<ExamenGeneral, String> palperalE;

    @FXML
    private TableColumn<ExamenGeneral, String> vulvarE;

    @FXML
    private TableColumn<ExamenGeneral, String> peneanaE;

    @FXML
    private TableColumn<ExamenGeneral, String> submandibularE;

    @FXML
    private TableColumn<ExamenGeneral, String> preescapularE;

    @FXML
    private TableColumn<ExamenGeneral, String> precruralE;

    @FXML
    private TableColumn<ExamenGeneral, String> inguinalE;

    @FXML
    private TableColumn<ExamenGeneral, String> otrosE;

    @FXML
    private TableColumn<ExamenGeneral, String> popliteoE;

    @FXML
    private TableColumn<ExamenGeneral, String> bucalE;

    @FXML
    private LineChart<String, Number> chExam;

    @FXML
    private JFXComboBox<String> cbVarEx;

    private static final Logger log = (Logger) LogManager.getLogger(IndexController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private Pacientes patient;

    private FichasClinicas clinicalFile;

    private Vacunas vaccine;

    private Desparasitaciones deworming;

    private Internaciones hospitalization;

    private ExamenGeneral exam;

    private PacientesHome daoPatient = new PacientesHome();

    private FichasClinicasHome daoClinicalFile = new FichasClinicasHome();

    private VacunasHome daoVaccine = new VacunasHome();

    private DesparasitacionesHome daoDeworming = new DesparasitacionesHome();

    private InternacionesHome daoHospitalizations = new InternacionesHome();

    private ExamenGeneralHome daoExams = new ExamenGeneralHome();

    private final ObservableList<Pacientes> patientsList = FXCollections.observableArrayList();

    private final ObservableList<Vacunas> vaccinesList = FXCollections.observableArrayList();

    private final ObservableList<Desparasitaciones> dewormingList = FXCollections.observableArrayList();

    private final ObservableList<Internaciones> hospitalizationsList = FXCollections.observableArrayList();

    private final ObservableList<ExamenGeneral> examsList = FXCollections.observableArrayList();

    private final ObservableList<FichasClinicas> clinicalFilesList = FXCollections.observableArrayList();

    private FilteredList<Pacientes> filteredDataP;

    // several basic choices for a series

    @SuppressWarnings("rawtypes")
    private XYChart.Series seriePeso = new XYChart.Series();

    @SuppressWarnings("rawtypes")
    private XYChart.Series serieFrecResp = new XYChart.Series();

    @SuppressWarnings("rawtypes")
    private XYChart.Series serieFrecCardio = new XYChart.Series();

    @SuppressWarnings("rawtypes")
    private XYChart.Series serieTllc = new XYChart.Series();

    @SuppressWarnings("rawtypes")
    private XYChart.Series serieDeshidratacion = new XYChart.Series();

    @FXML
    void initialize() {
        log.info(marker, "Adding Table details.");
        // Patients
        nombre.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getNombre()));

        especie.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getEspecie()));

        fecha.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaNacimiento()));

        propietario.setCellValueFactory(
                (param) -> new ReadOnlyObjectWrapper<Propietarios>(param.getValue().getPropietarios()));

        // Vaccines
        descripcionV.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getDescripcion()));

        fechaV.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        // Deworming
        tcTratamientoD.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTratamiento()));

        tcTipoD.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTipo()));

        tcFechaD.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        tcFechaProximaD.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        // Hospitalizations
        tcFechaIngresoH
                .setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaIngreso()));

        tcFechaAltaH.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFechaAlta()));

        // Exams
        fechaE.setCellValueFactory((param) -> new ReadOnlyObjectWrapper<Date>(param.getValue().getFecha()));

        pesoCorporalE.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getPesoCorporal())));

        tempCorporalE.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getTempCorporal())));

        frecRespE.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getFrecResp())));

        deshidratacionE.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getDeshidratacion())));

        amplitudE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getAmplitud()));

        tipoE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getTipo()));

        ritmoE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getRitmo()));

        frecCardioE.setCellValueFactory(
                (param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getFrecCardio())));

        tllcE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getTllc())));

        escleralE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getEscleral()));

        pulsoE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPulso()));

        palperalE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPalperal()));

        vulvarE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getVulvar()));

        peneanaE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPeneana()));

        submandibularE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getSubmandibular()));

        preescapularE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPreescapular()));

        precruralE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPrecrural()));

        inguinalE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getOtros()));

        otrosE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getInguinal()));

        popliteoE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getPopliteo()));

        bucalE.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getBucal()));

        tcIdFc.setCellValueFactory((param) -> new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId())));

        tcMotivoFc.setCellValueFactory((param) -> new ReadOnlyStringWrapper(param.getValue().getMotivoConsulta()));

        // Load Table Items
        log.info(marker, "Loading table items.");

        loadPatients();

        // Handle ListView selection changes.
        indexPA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                patient = newValue;
                log.info(marker, "Patient selected.");
            }
        });

        indexVC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                vaccine = newValue;
                log.info(marker, "Vaccine selected.");
            }
        });

        indexVC.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && vaccine != null) {
                    displayVaccine(vaccine);
                }
        });

        indexD.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                deworming = newValue;
                log.info(marker, "Deworming selected.");
            }
        });

        indexD.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && deworming != null) {
                    displayDeworming(deworming);
                }
        });

        indexHS.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                hospitalization = newValue;
                log.info(marker, "Hospitalization selected.");
            }
        });

        indexHS.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && hospitalization != null) {
                    displayHospitalizations(hospitalization);
                }
        });

        indexFC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clinicalFile = newValue;
                log.info(marker, "Clinical File selected.");
            }
        });

        indexFC.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && clinicalFile != null) {
                    displayClinicalFile(clinicalFile);
                }
        });

        indexE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                exam = newValue;
                log.info(marker, "Exam selected.");
            }
        });

        indexE.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY)
                if (event.getClickCount() == 2 && exam != null) {
                    displayExam(exam);
                }
        });

        // Button Actions

        btnEditPat.setOnAction((event) -> {
            if (patient != null)
                displayPatient(patient);
            else
                DialogBox.displayWarning();
        });

        btnEditDew.setOnAction((event) -> {
            if (deworming != null)
                displayDeworming(deworming);
            else
                DialogBox.displayWarning();
        });

        btnEditExam.setOnAction((event) -> {
            if (exam != null)
                displayExam(exam);
            else
                DialogBox.displayWarning();
        });

        btnEditFc.setOnAction((event) -> {
            if (clinicalFile != null)
                displayClinicalFile(clinicalFile);
            else
                DialogBox.displayWarning();
        });

        btnEditHos.setOnAction((event) -> {
            if (hospitalization != null)
                displayHospitalizations(hospitalization);
            else
                DialogBox.displayWarning();
        });

        btnEditVac.setOnAction((event) -> {
            if (vaccine != null)
                displayVaccine(vaccine);
            else
                DialogBox.displayWarning();
        });

        btnNewDew.setOnAction((event) -> newDeworming(event));

        btnNewFc.setOnAction((event) -> newClinicalFile(event));

        btnNewHos.setOnAction((event) -> newHospitalization(event));

        btnNewPat.setOnAction((event) -> newPatient(event));

        btnNewVac.setOnAction((event) -> newVaccination(event));

        // search filter
        filteredDataP = new FilteredList<>(patientsList, p -> true);
        txtFilterPat.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataP.setPredicate(paciente -> newValue == null || newValue.isEmpty()
                    || paciente.getNombre().toLowerCase().contains(newValue.toLowerCase())
                    || paciente.getPropietarios().toString().toLowerCase().contains(newValue.toLowerCase()));
            changeTableView(tpPatient.getCurrentPageIndex(), 20);
        });

        // Graphic
        log.info(marker, "Loading combobox details.");
        cbVarEx.getItems().setAll("Peso", // 0
                "Frecuencia Respiratoria", // 1
                "Frecuencia Cardíaca", // 2
                "T.L.L.C.", // 3
                "Deshidratación" // 4
        );

        cbVarEx.setOnAction((event) -> {
            int item = cbVarEx.getSelectionModel().getSelectedIndex();
            loadChart(item);
        });
    }

    /*
     * Class Methods
     */

    @FXML
    void deleteClinicalFile(ActionEvent event) {
        if (clinicalFile != null) {
            if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                daoClinicalFile.delete(clinicalFile.getId());
                FichasClinicas selectedItem = indexFC.getSelectionModel().getSelectedItem();
                clinicalFilesList.remove(selectedItem);
                indexFC.setItems(clinicalFilesList);
                refreshClinicalFiles();
                clinicalFile = null;
                DialogBox.displaySuccess();
                log.info(marker, "Clinical File deleted.");
            }
        } else
            DialogBox.displayWarning();
    }

    @FXML
    void deleteDeworming(ActionEvent event) {
        if (clinicalFile != null) {
            if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                daoDeworming.delete(deworming.getId());
                Desparasitaciones selectedItem = indexD.getSelectionModel().getSelectedItem();
                dewormingList.remove(selectedItem);
                indexFC.setItems(clinicalFilesList);
                refreshDewormings();
                deworming = null;
                DialogBox.displaySuccess();
                log.info(marker, "Deworming deleted.");
            }
        } else
            DialogBox.displayWarning();
    }

    @FXML
    void deleteExam(ActionEvent event) {
        if (exam != null) {
            if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                daoExams.delete(exam.getId());
                ExamenGeneral selectedItem = indexE.getSelectionModel().getSelectedItem();
                examsList.remove(selectedItem);
                indexE.setItems(examsList);
                refreshExams();
                exam = null;
                DialogBox.displaySuccess();
                log.info(marker, "Exam deleted.");
            }
        } else
            DialogBox.displayWarning();
    }

    @FXML
    void deleteHospitalization(ActionEvent event) {
        if (hospitalization != null) {
            if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                daoHospitalizations.delete(hospitalization.getId());
                Internaciones selectedItem = indexHS.getSelectionModel().getSelectedItem();
                hospitalizationsList.remove(selectedItem);
                indexFC.setItems(clinicalFilesList);
                refreshClinicalFiles();
                hospitalization = null;
                DialogBox.displaySuccess();
                log.info(marker, "Hospitalizations deleted.");
            }
        } else
            DialogBox.displayWarning();
    }

    @FXML
    void deletePatient(ActionEvent event) {
        if (patient != null) {
            if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                daoPatient.delete(patient.getId());
                Pacientes selectedItem = indexPA.getSelectionModel().getSelectedItem();
                patientsList.remove(selectedItem);
                indexPA.setItems(patientsList);
                refreshClinicalFiles();
                hospitalization = null;
                DialogBox.displaySuccess();
                log.info(marker, "Patient deleted.");
            }
        } else
            DialogBox.displayWarning();
    }

    @FXML
    void deleteVaccination(ActionEvent event) {
        if (vaccine != null) {
            if (DialogBox.confirmDialog("¿Desea eliminar el registro?")) {
                daoVaccine.delete(vaccine.getId());
                Vacunas selectedItem = indexVC.getSelectionModel().getSelectedItem();
                vaccinesList.remove(selectedItem);
                indexVC.setItems(vaccinesList);
                refreshClinicalFiles();
                hospitalization = null;
                DialogBox.displaySuccess();
                log.info(marker, "Vaccine deleted.");
            }
        } else
            DialogBox.displayWarning();
    }

    @FXML
    void loadGraphic(ActionEvent event) {

    }

    @FXML
    void newClinicalFile(ActionEvent event) {
        ViewSwitcher.loadModal(Route.FICHACLINICA.newView(), "Nuevo elemento - Ficha Clínica", true);
        controller.clinicalFile.NewController nc = ViewSwitcher.getController(Route.FICHACLINICA.newView());
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshClinicalFiles();
            nc.cleanFields();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    private void newExam() {
        ViewSwitcher.loadModal(Route.EXAMEN.newView(), "Nuevo elemento - Exámen General", true);
        controller.exam.NewController nc = ViewSwitcher.getController(Route.EXAMEN.newView());
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshExams();
            nc.cleanFields();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newDeworming(ActionEvent event) {
        ViewSwitcher.loadModal(Route.DESPARASITACION.newView(), "Nuevo elemento - Desparasitaciones", true);
        controller.deworming.NewController nc = ViewSwitcher.getController(Route.DESPARASITACION.newView());
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshDewormings();
            nc.cleanFields();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newHospitalization(ActionEvent event) {
        ViewSwitcher.loadModal(Route.INTERNACION.newView(), "Nuevo elemento - Internaciones", true);
        controller.hospitalization.NewController nc = ViewSwitcher.getController(Route.INTERNACION.newView());
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshHospitalizations();
            nc.cleanFields();

        });
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newPatient(ActionEvent event) {
        ViewSwitcher.loadModal(Route.PACIENTE.newView(), "Nuevo elemento - Paciente", true);
        controller.patient.NewController nc = ViewSwitcher.getController(Route.PACIENTE.newView());
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshPatients();
            nc.cleanFields();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void newVaccination(ActionEvent event) {
        ViewSwitcher.loadModal(Route.VACUNA.newView(), "Nuevo elemento - Vacunación", true);
        controller.vaccine.NewController nc = ViewSwitcher.getController(Route.VACUNA.newView());
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshVaccines();
            nc.cleanFields();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    @FXML
    void showPatient(ActionEvent event) {
        if (patient != null) {
            loadFields(patient);
            loadExams(patient);
            loadClinicalFiles(patient);
            loadDeworming(patient);
            loadVaccines(patient);

            ViewSwitcher.loadingDialog.startTask();
        } else
            DialogBox.displayWarning();
    }

    /**
     * Changes the page of the table view in the search filter.
     * 
     * @param <T>
     * 
     * @param index
     * @param limit
     */
    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, patientsList.size());
        int minIndex = Math.min(toIndex, filteredDataP.size());
        SortedList<Pacientes> sortedData = new SortedList<Pacientes>(
                FXCollections.observableArrayList(filteredDataP.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(indexPA.comparatorProperty());
        indexPA.setItems(sortedData);
    }

    /**
     * Creates a concurrent task that retrieves all records from the database.
     */
    private void loadPatients() {
        Task<List<Pacientes>> task = daoPatient.displayRecords();

        task.setOnSucceeded(event -> {
            patientsList.setAll(task.getValue());
            indexPA.setItems(patientsList);
            tpPatient.setPageFactory((index) -> TableUtil.createPage(indexPA, patientsList, tpPatient, index, 20));
            log.info(marker, "[ Patients ] - loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }

    private void loadHospitalizations(Pacientes patient) {
        Task<List<Internaciones>> task = daoHospitalizations.showByPatient(patient);

        task.setOnSucceeded(event -> {
            hospitalizationsList.setAll(task.getValue());
            indexHS.setItems(hospitalizationsList);
            log.info(marker, "[ Hospitalizations ] - loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);

    }

    private void loadDeworming(Pacientes patient) {
        Task<List<Desparasitaciones>> task = daoDeworming.showByPatient(patient);

        task.setOnSucceeded(event -> {
            dewormingList.setAll(task.getValue());
            indexD.setItems(dewormingList);
            log.info(marker, "[ Dewormings ] - loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);

    }

    private void loadVaccines(Pacientes patient) {
        Task<List<Vacunas>> task = daoVaccine.showByPatient(patient);

        task.setOnSucceeded(event -> {
            vaccinesList.setAll(task.getValue());
            indexVC.setItems(vaccinesList);
            log.info(marker, "[ Vaccines ] - loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);

    }

    private void loadClinicalFiles(Pacientes patient) {
        Task<List<FichasClinicas>> task = daoClinicalFile.showByPatient(patient);

        task.setOnSucceeded(event -> {
            clinicalFilesList.setAll(task.getValue());
            indexFC.setItems(clinicalFilesList);
            log.info(marker, "[ Clinical Files] - loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);

    }

    private void loadExams(Pacientes patient) {
        Task<List<ExamenGeneral>> task = daoExams.showByPatient(patient);

        task.setOnSucceeded(event -> {
            examsList.setAll(task.getValue());
            indexE.setItems(examsList);
            loadSeries();
            log.info(marker, "[ Vaccines ] - loaded.");
        });

        ViewSwitcher.loadingDialog.addTask(task);

    }

    @SuppressWarnings("unchecked")
    private void loadChart(int item) {
        switch (item) {
        case 0:
            chExam.getData().clear();
            chExam.layout();
            chExam.getData().addAll(seriePeso);
            break;

        case 1:
            chExam.getData().clear();
            chExam.layout();
            chExam.getData().addAll(serieFrecResp);
            break;

        case 2:
            chExam.getData().clear();
            chExam.layout();
            chExam.getData().addAll(serieFrecCardio);
            break;

        case 3:
            chExam.getData().clear();
            chExam.layout();
            chExam.getData().addAll(serieTllc);
            break;

        case 4:
            chExam.getData().clear();
            chExam.layout();
            chExam.getData().addAll(serieDeshidratacion);
            break;
        default:
            break;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadSeries() {
        examsList.sort(Comparator.comparing(ExamenGeneral::getFecha));

        seriePeso.setName("Peso");
        examsList.forEach((item) -> {
            seriePeso.getData().add(new XYChart.Data(item.getFecha().toString(), item.getPesoCorporal()));
        });

        serieDeshidratacion.setName("Deshidratación");
        examsList.forEach((item) -> {
            serieDeshidratacion.getData().add(new XYChart.Data(item.getFecha().toString(), item.getDeshidratacion()));
        });

        serieFrecCardio.setName("Frecuencia Cardíaca");
        examsList.forEach((item) -> {
            serieFrecCardio.getData().add(new XYChart.Data(item.getFecha().toString(), item.getFrecCardio()));
        });

        serieFrecResp.setName("Frecuencia Respiratoria");
        examsList.forEach((item) -> {
            serieFrecResp.getData().add(new XYChart.Data(item.getFecha().toString(), item.getFrecResp()));
        });

        serieTllc.setName("T.L.L.C.");
        examsList.forEach((item) -> {
            serieTllc.getData().add(new XYChart.Data(item.getFecha().toString(), item.getTllc()));
        });
    } // maybe another thread each task?

    /*
     * Modal dialogs corresponding to each table component.
     */

    private void displayPatient(Pacientes patient) {
        ViewSwitcher.loadModal(Route.PACIENTE.modalView(), "Paciente", true);
        controller.patient.ModalDialogController mc = ViewSwitcher.getController(Route.PACIENTE.modalView());
        mc.setObject(patient);
        mc.loadDao();
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshPatients();
        });
        ViewSwitcher.loadingDialog.startTask();
        ViewSwitcher.modalStage.showAndWait();
    }

    private void displayExam(ExamenGeneral exam) {
        ViewSwitcher.loadModal(Route.EXAMEN.modalView(), "Exámen General", true);
        controller.exam.ModalDialogController mc = ViewSwitcher.getController(Route.EXAMEN.modalView());
        mc.setObject(exam);
        mc.loadDao();
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshExams();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void displayClinicalFile(FichasClinicas clinicalFile) {
        ViewSwitcher.loadModal(Route.FICHACLINICA.modalView(), "Ficha Clínica", true);
        controller.clinicalFile.ModalDialogController mc = ViewSwitcher.getController(Route.FICHACLINICA.modalView());
        mc.setObject(clinicalFile);
        mc.loadDao();
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshClinicalFiles();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void displayHospitalizations(Internaciones hospitalization) {
        ViewSwitcher.loadModal(Route.INTERNACION.modalView(), "Internaciones", true);
        controller.hospitalization.ModalDialogController mc = ViewSwitcher.getController(Route.INTERNACION.modalView());
        mc.setObject(hospitalization);
        mc.loadDao();
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshHospitalizations();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void displayDeworming(Desparasitaciones deworming) {
        ViewSwitcher.loadModal(Route.DESPARASITACION.modalView(), "Desparasitaciones", true);
        controller.deworming.ModalDialogController mc = ViewSwitcher.getController(Route.DESPARASITACION.modalView());
        mc.setObject(deworming);
        mc.loadDao();
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshDewormings();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    private void displayVaccine(Vacunas vaccine) {
        ViewSwitcher.loadModal(Route.VACUNA.modalView(), "Vacunación", true);
        controller.vaccine.ModalDialogController mc = ViewSwitcher.getController(Route.VACUNA.modalView());
        mc.setObject(vaccine);
        mc.loadDao();
        ViewSwitcher.modalStage.setOnHiding((stageEvent) -> {
            refreshVaccines();
        });
        ViewSwitcher.modalStage.showAndWait();
    }

    /*
     * Table reload methods
     */

    /**
     * Reloads Patient's table list.
     */
    private void refreshPatients() {
        patientsList.clear();
        loadPatients();
        ViewSwitcher.loadingDialog.startTask();
    }

    private void refreshExams() {
        examsList.clear();
        loadExams(patient);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void refreshClinicalFiles() {
        clinicalFilesList.clear();
        loadClinicalFiles(patient);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void refreshHospitalizations() {
        hospitalizationsList.clear();
        loadHospitalizations(patient);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void refreshDewormings() {
        dewormingList.clear();
        loadDeworming(patient);
        ViewSwitcher.loadingDialog.startTask();
    }

    private void refreshVaccines() {
        vaccinesList.clear();
        loadVaccines(patient);
        ViewSwitcher.loadingDialog.startTask();
    }

    // Load patient details
    private void setFoto(String path) {

        /*
         * TODO encapsulate behavior
         */
        log.info(marker, "Loading Image");
        URL url;
        try {

            if (path != null)
                url = new URL(path);
            else
                url = getClass().getResource("/images/DogCat.jpg");

            if (ImageIO.read(url) != null) {
                Image image = new Image(url.toString());
                ivFoto.setImage(image);
                log.info(marker, "Image Loaded " + path);
            }
        } catch (IOException e) {
            DialogBox.setHeader("Ruta incorrecta");
            DialogBox.setContent(e.getMessage());
            DialogBox.displayError();
            ivFoto = new ImageView("/images/DogCat.jpg");
        }
    }

    public void loadFields(Pacientes patient) {
        log.info(marker, "Loading patient's fields");
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                txtNombreP.setText(patient.getNombre());
                txtEspecieP.setText(patient.getEspecie());
                txtFechaNacP.setText(patient.getFechaNacimiento().toString());
                txtRazaP.setText(patient.getRaza());
                txtSexoP.setText(patient.getSexo());
                txtTempP.setText(patient.getTemperamento());
                txtPelajeP.setText(patient.getPelaje());
                txtPropietarioP.setText(patient.getPropietarios().toString());
                setFoto(patient.getFoto());
                return null;
            }
        };

        ViewSwitcher.loadingDialog.addTask(task);
    }
}

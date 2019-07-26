package controller.exam;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.jfoenix.controls.JFXComboBox;

import dao.ExamenGeneralHome;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import model.ExamenGeneral;
import model.Pacientes;
import utils.ViewSwitcher;
import utils.routes.Route;

public class ViewController extends ViewSwitcher {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane apExam;

    @FXML
    private LineChart<String, Number> chExam;

    @FXML
    private JFXComboBox<String> comboVar;

    private Pacientes paciente;

    protected static final Logger log = (Logger) LogManager.getLogger(ViewController.class);

    // several basic choices for a serie

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

    private controller.exam.ShowController examController;

    final ObservableList<ExamenGeneral> examenList = FXCollections.observableArrayList();

    private ExamenGeneralHome dao = new ExamenGeneralHome();

    @FXML
    void initialize() {
        assert apExam != null : "fx:id=\"apExam\" was not injected: check your FXML file 'view.fxml'.";
        assert chExam != null : "fx:id=\"chExam\" was not injected: check your FXML file 'view.fxml'.";
        assert comboVar != null : "fx:id=\"comboVar\" was not injected: check your FXML file 'view.fxml'.";

        log.info("Loading combobox details.");
        comboVar.getItems().setAll("Peso", // 0
                "Frecuencia Respiratoria", // 1
                "Frecuencia Cardíaca", // 2
                "T.L.L.C.", // 3
                "Deshidratación" // 4
        );
        log.info("Loading line chart details.");
        Platform.runLater(() -> {
            examenList.setAll(dao.showByPaciente(paciente));
            loadContent();
            loadSeries();
        });

        comboVar.setOnAction((event) -> {
            int item = comboVar.getSelectionModel().getSelectedIndex();
            loadChart(item);
        });
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
        examenList.sort(Comparator.comparing(ExamenGeneral::getFecha));

        seriePeso.setName("Peso");
        examenList.forEach((item) -> {
            seriePeso.getData().add(new XYChart.Data(item.getFecha().toString(), item.getPesoCorporal()));
        });

        serieDeshidratacion.setName("Deshidratación");
        examenList.forEach((item) -> {
            serieDeshidratacion.getData().add(new XYChart.Data(item.getFecha().toString(), item.getDeshidratacion()));
        });

        serieFrecCardio.setName("Frecuencia Cardíaca");
        examenList.forEach((item) -> {
            serieFrecCardio.getData().add(new XYChart.Data(item.getFecha().toString(), item.getFrecCardio()));
        });

        serieFrecResp.setName("Frecuencia Respiratoria");
        examenList.forEach((item) -> {
            serieFrecResp.getData().add(new XYChart.Data(item.getFecha().toString(), item.getFrecResp()));
        });

        serieTllc.setName("T.L.L.C.");
        examenList.forEach((item) -> {
            serieTllc.getData().add(new XYChart.Data(item.getFecha().toString(), item.getTllc()));
        });
    } // maybe another thread each task?

    public void setObject(Pacientes paciente) {
        this.paciente = paciente;
    }

    private void loadContent() {
        log.info("[ Loading panes ]");
        log.debug("Attempting to load ExamenGeneral-View.");
        examController = super.loadCustomAnchor(Route.EXAMEN.showView(), apExam, examController);
        examController.setObject(paciente);
    }
}

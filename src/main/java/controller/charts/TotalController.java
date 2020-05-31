package controller.charts;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import dao.PacientesHome;
import dao.PropietariosHome;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import utils.viewswitcher.ViewSwitcher;

public class TotalController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML // fx:id="registers"
    private PieChart registers;

    private static final Logger log = (Logger) LogManager.getLogger(TotalController.class);

    private static final Marker marker = MarkerManager.getMarker("CLASS");

    private final ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

    private PacientesHome daoPA = new PacientesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    private Integer propietarios;

    private Integer pacientes;

    @FXML
    void initialize() {
        loadDao();
    }

    /*
     * Class Methods
     */

    private void loadDao() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                propietarios = daoPO.getTotalRecords();
                pacientes = daoPA.getTotalRecords();
                return null;
            }
        };

        task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Exception e = (Exception) newValue;
                log.error(marker, "Failed to load chart.", e.getMessage());
                log.debug(marker, "[ Chart - Loading Failed ]", e);
            }
        });

        task.setOnSucceeded(event -> {
            chartData.addAll(new PieChart.Data("Propietarios", propietarios),
                    new PieChart.Data("Pacientes", pacientes));
            // add values to labels
            chartData.forEach(
                    data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty())));
            registers.setData(chartData);
            log.info(marker, "Chart Loaded.");
        });

        task.setOnFailed(event -> {
            log.warn(marker, "Failed to load chart.");
        });

        ViewSwitcher.loadingDialog.addTask(task);
    }
}
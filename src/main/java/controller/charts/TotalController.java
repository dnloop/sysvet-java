package controller.charts;

import java.net.URL;
import java.util.ResourceBundle;

import dao.PacientesHome;
import dao.PropietariosHome;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import utils.ViewSwitcher;

public class TotalController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML // fx:id="registers"
    private PieChart registers;

    private final ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

    private PacientesHome daoPA = new PacientesHome();

    private PropietariosHome daoPO = new PropietariosHome();

    private Integer propietarios;

    private Integer pacientes;

    @FXML
    void initialize() {
        assert registers != null : "fx:id=\"registers\" was not injected: check your FXML file 'total.fxml'.";
        Platform.runLater(() -> {
            propietarios = daoPO.getTotalRecords();
            pacientes = daoPA.getTotalRecords();
            chartData.addAll(new PieChart.Data("Propietarios", propietarios),
                    new PieChart.Data("Pacientes", pacientes));
            // add values to labels
            chartData.forEach(
                    data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty())));
            registers.setData(chartData);
        });
    }

    /**
     *
     * Class Methods
     *
     */

    public void setView(String fxml) {
        ViewSwitcher.loadView(fxml);
    }
}
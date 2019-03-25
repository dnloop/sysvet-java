package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class BaseControllerTest {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane contentPane;

    @FXML
    void initialize() {

    }

    public void setView(Node node) {
        contentPane.getChildren().setAll(node);
    }
}

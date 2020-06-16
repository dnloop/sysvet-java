package controller.currentAccount;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import utils.routes.Route;
import utils.viewswitcher.ViewSwitcher;

/**
 * Simple Controller to load Current Accounts in the same pane.
 * 
 * @author dnloop
 *
 */
public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TitledPane tpaneOwner;

    @FXML
    private TitledPane tpaneAccount;

    @FXML
    void initialize() {
        tpaneOwner.setContent(ViewSwitcher.getView(Route.CUENTACORRIENTE.indexView()));

        tpaneAccount.setContent(ViewSwitcher.getView(Route.CUENTACORRIENTE.showView()));
    }
}

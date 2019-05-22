package utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DialogBox {

    public DialogBox() {
        // TODO Auto-generated constructor stub
    }

    public static boolean confirmDialog(String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción.");
        alert.setContentText(content);
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    public static void displayWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }

    public static void displayError(String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error.");
        alert.setHeaderText("Se produjo un error en la ejecución del programa.");
        alert.setContentText(content);
        alert.setResizable(true);

        alert.showAndWait();
    }

}

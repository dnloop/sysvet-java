package utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DialogBox {

    private static String header;

    private static String content;

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

    public static void displayCustomWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia.");
        alert.setHeaderText("Elemento vacío.");
        alert.setContentText("No se seleccionó ningún elemento de la lista. Elija un ítem e intente nuevamente.");
        alert.setResizable(true);

        alert.showAndWait();
    }

    public static void displaySuccess() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Aviso.");
        alert.setHeaderText("Éxito.");
        alert.setContentText("La operación se concretó en forma satisfactoria.");
        alert.setResizable(true);

        alert.showAndWait();
    }

    public static void displayError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error.");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setResizable(true);

        alert.showAndWait();
    }

    public static String getHeader() {
        return header;
    }

    public static void setHeader(String header) {
        DialogBox.header = header;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        DialogBox.content = content;
    }

}

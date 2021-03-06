package utils.routes;

/**
 * <p>
 * Extra constant paths of the fxml files used as views in the project.
 * </p>
 *
 * @author dnloop
 */
public enum RouteExtra {
    CHART("/fxml/charts/total.fxml"), 
    PACIENTEMAIN("/fxml/patient/main.fxml"),
    CUENTASCORRIENTESMAIN("/fxml/currentAccount/main.fxml"),
    PACIENTEVIEW("/fxml/patient/view.fxml"),
    CLINICOVERVIEW("/fxml/clinicalFile/overview.fxml"),
    CLINICVIEW("/fxml/clinicalFile/view.fxml"),
    EXAMVIEW("/fxml/exam/view.fxml"),
    LOADING("/fxml/util/loading.fxml"),
    NEW("/fxml/util/select.fxml"),
    ABOUT("/fxml/util/about.fxml"),
    MAIN("/fxml/main.fxml");

    RouteExtra(String path) {
        this.path = path;
    };

    private final String path;

    public String getPath() {
        return path;
    }
}

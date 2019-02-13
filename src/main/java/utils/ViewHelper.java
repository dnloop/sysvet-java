package utils;

public class ViewHelper {

    static String[] indexList = {
            "/fxml/patient/index.fxml",
            "/fxml/owner/index.fxml",
            "/fxml/vaccine/index.fxml",
            "/fxml/deworming/index.fxml",
            "/fxml/clinicalFile/index.fxml",
            "/fxml/exam/index.fxml",
            "/fxml/clinicHistory/index.fxml",
            "/fxml/internation/index.fxml",
            "/fxml/clinicalTreatment/index.fxml",
            "/fxml/return/index.fxml",
            "/fxml/location/index.fxml",
            "/fxml/currentAccount/index.fxml"
    };

    static String[] loaderList = {
            "/fxml/patient/index.fxml",
            "/fxml/owner/index.fxml",
            "/fxml/vaccine/index.fxml",
            "/fxml/deworming/index.fxml",
            "/fxml/clinicalFile/index.fxml",
            "/fxml/exam/index.fxml",
            "/fxml/clinicHistory/index.fxml",
            "/fxml/internation/index.fxml",
            "/fxml/clinicalTreatment/index.fxml",
            "/fxml/return/index.fxml",
            "/fxml/location/index.fxml",
            "/fxml/currentAccount/index.fxml"
    };

    static String[] deletedList = {
            "/fxml/patient/index.fxml",
            "/fxml/owner/index.fxml",
            "/fxml/vaccine/index.fxml",
            "/fxml/deworming/index.fxml",
            "/fxml/clinicalFile/index.fxml",
            "/fxml/exam/index.fxml",
            "/fxml/clinicHistory/index.fxml",
            "/fxml/internation/index.fxml",
            "/fxml/clinicalTreatment/index.fxml",
            "/fxml/return/index.fxml",
            "/fxml/location/index.fxml",
            "/fxml/currentAccount/index.fxml"
    };
    public ViewHelper() {}

    public String route(int val, int key) {
        /**
         * indexList    - 0
         * loaderList   - 1
         * deletedList  - 2
         * */
        switch (val) {
        case 0:
            return indexList[key];
        case 1:
            return loaderList[key];
        case 2:
            return deletedList[key];
        default:
            break;
        }
        return null;
    }
}

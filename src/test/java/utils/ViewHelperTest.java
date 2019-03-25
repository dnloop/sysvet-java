package utils;

public final class ViewHelperTest {

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

    static String[] modalList = {
            "/fxml/patient/modalDialog.fxml",
            "/fxml/owner/modalDialog.fxml",
            "/fxml/vaccine/modalDialog.fxml",
            "/fxml/deworming/modalDialog.fxml",
            "/fxml/clinicalFile/modalDialog.fxml",
            "/fxml/exam/modalDialog.fxml",
            "/fxml/clinicHistory/modalDialog.fxml",
            "/fxml/internation/modalDialog.fxml",
            "/fxml/clinicalTreatment/modalDialog.fxml",
            "/fxml/return/modalDialog.fxml",
            "/fxml/location/modalDialog.fxml",
            "/fxml/currentAccount/modalDialog.fxml"
    };

    public ViewHelperTest() {}

    public String route(int val, int key) {
        /**
         * indexList    - 0
         * loaderList   - 1
         * deletedList  - 2
         * modalList    - 3
         * */
        switch (val) {
        case 0:
            return indexList[key];
        case 1:
            return loaderList[key];
        case 2:
            return deletedList[key];
        case 3:
            return modalList[key];
        default:
            break;
        }
        return null;
    }
}

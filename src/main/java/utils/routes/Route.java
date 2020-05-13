package utils.routes;

/**
 * <p>
 * Displays the contants paths of the fxml files used as views in the project.
 * </p>
 *
 * @author dnloop
 */
public enum Route implements View {
    PACIENTE {
        @Override
        public String indexView() {
            return "/fxml/patient/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/patient/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/patient/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/patient/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/patient/recover.fxml";
        }
    },

    PROPIETARIO {
        @Override
        public String indexView() {
            return "/fxml/owner/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/owner/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/owner/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/owner/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/owner/recover.fxml";
        }
    },

    CUENTACORRIENTE {
        @Override
        public String indexView() {
            return "/fxml/currentAccount/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/currentAccount/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/currentAccount/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/currentAccount/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/currentAccount/recover.fxml";
        }
    },

    DESPARASITACION {
        @Override
        public String indexView() {
            return "/fxml/deworming/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/deworming/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/deworming/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/deworming/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/deworming/recover.fxml";
        }
    },

    EXAMEN {
        @Override
        public String indexView() {
            return "/fxml/exam/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/exam/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/exam/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/exam/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/exam/recover.fxml";
        }
    },

    FICHACLINICA {
        @Override
        public String indexView() {
            return "/fxml/clinicalFile/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/clinicalFile/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/clinicalFile/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/clinicalFile/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/clinicalFile/recover.fxml";
        }
    },

    HISTORIACLINICA {
        @Override
        public String indexView() {
            return "/fxml/clinicHistory/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/clinicHistory/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/clinicHistory/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/clinicHistory/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/clinicHistory/recover.fxml";
        }
    },

    INTERNACION {
        @Override
        public String indexView() {
            return "/fxml/hospitalization/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/hospitalization/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/hospitalization/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/hospitalization/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/hospitalization/recover.fxml";
        }
    },

    TRATAMIENTO {
        @Override
        public String indexView() {
            return "/fxml/treatment/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/treatment/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/treatment/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/treatment/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/treatment/recover.fxml";
        }
    },

    VACUNA {
        @Override
        public String indexView() {
            return "/fxml/vaccine/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/vaccine/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/vaccine/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/vaccine/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/vaccine/recover.fxml";
        }
    },

    PROVINCIA {
        @Override
        public String indexView() {
            return "/fxml/province/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/province/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/province/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/province/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/province/recover.fxml";
        }
    },

    LOCALIDAD {
        @Override
        public String indexView() {
            return "/fxml/location/index.fxml";
        }

        @Override
        public String modalView() {
            return "/fxml/location/modalDialog.fxml";
        }

        @Override
        public String newView() {
            return "/fxml/location/new.fxml";
        }

        @Override
        public String showView() {
            return "/fxml/location/show.fxml";
        }

        @Override
        public String recoverView() {
            return "/fxml/location/recover.fxml";
        }
    },
}

package utils.viewswitcher;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import utils.routes.Route;
import utils.routes.RouteExtra;

/**
 * Implementation class for UILoader used on application startup to initialize
 * the visual nodes.
 * 
 * @author dnloop
 */
public class InitView {

    private final List<Task<Void>> taskList = new ArrayList<>();

    public InitView() {
    }

    public void initTasks() {
        UILoader uiLoader = new UILoader();
        // Load main
        taskList.add(uiLoader.<controller.charts.TotalController>buildTask(RouteExtra.CHART.getPath()));
        taskList.add(uiLoader.<controller.MainController>buildTask(RouteExtra.MAIN.getPath()));
        taskList.add(uiLoader.<controller.SelectController>buildTask(RouteExtra.NEW.getPath()));
        // Load patients
        taskList.add(uiLoader.<controller.patient.IndexController>buildTask(Route.PACIENTE.indexView()));
        taskList.add(uiLoader.<controller.patient.ModalDialogController>buildTask(Route.PACIENTE.modalView()));
        taskList.add(uiLoader.<controller.patient.NewController>buildTask(Route.PACIENTE.newView()));
        taskList.add(uiLoader.<controller.patient.RecoverController>buildTask(Route.PACIENTE.recoverView()));
        taskList.add(uiLoader.<controller.patient.MainController>buildTask(RouteExtra.PACIENTEMAIN.getPath()));
        taskList.add(uiLoader.<controller.patient.ViewController>buildTask(RouteExtra.PACIENTEVIEW.getPath()));
        // Load clinical files
        taskList.add(uiLoader.<controller.clinicalFile.IndexController>buildTask(Route.FICHACLINICA.indexView()));
        taskList.add(uiLoader.<controller.clinicalFile.ModalDialogController>buildTask(Route.FICHACLINICA.modalView()));
        taskList.add(uiLoader.<controller.clinicalFile.NewController>buildTask(Route.FICHACLINICA.newView()));
        taskList.add(uiLoader.<controller.clinicalFile.ShowController>buildTask(Route.FICHACLINICA.showView()));
        taskList.add(uiLoader.<controller.clinicalFile.RecoverController>buildTask(Route.FICHACLINICA.recoverView()));
        taskList.add(
                uiLoader.<controller.clinicalFile.OverviewController>buildTask(RouteExtra.CLINICOVERVIEW.getPath()));
        taskList.add(uiLoader.<controller.clinicalFile.ViewController>buildTask(RouteExtra.CLINICVIEW.getPath()));
        // Load clinic history
        taskList.add(uiLoader.<controller.clinicHistory.IndexController>buildTask(Route.HISTORIACLINICA.indexView()));
        taskList.add(
                uiLoader.<controller.clinicHistory.ModalDialogController>buildTask(Route.HISTORIACLINICA.modalView()));
        taskList.add(uiLoader.<controller.clinicHistory.NewController>buildTask(Route.HISTORIACLINICA.newView()));
        taskList.add(uiLoader.<controller.clinicHistory.ShowController>buildTask(Route.HISTORIACLINICA.showView()));
        taskList.add(
                uiLoader.<controller.clinicHistory.RecoverController>buildTask(Route.HISTORIACLINICA.recoverView()));
        // Load current account
        taskList.add(uiLoader.<controller.currentAccount.IndexController>buildTask(Route.CUENTACORRIENTE.indexView()));
        taskList.add(
                uiLoader.<controller.currentAccount.ModalDialogController>buildTask(Route.CUENTACORRIENTE.modalView()));
        taskList.add(uiLoader.<controller.currentAccount.NewController>buildTask(Route.CUENTACORRIENTE.newView()));
        taskList.add(uiLoader.<controller.currentAccount.ShowController>buildTask(Route.CUENTACORRIENTE.showView()));
        taskList.add(
                uiLoader.<controller.currentAccount.RecoverController>buildTask(Route.CUENTACORRIENTE.recoverView()));
        // Load deworming
        taskList.add(uiLoader.<controller.deworming.IndexController>buildTask(Route.DESPARASITACION.indexView()));
        taskList.add(uiLoader.<controller.deworming.ModalDialogController>buildTask(Route.DESPARASITACION.modalView()));
        taskList.add(uiLoader.<controller.deworming.NewController>buildTask(Route.DESPARASITACION.newView()));
        taskList.add(uiLoader.<controller.deworming.ShowController>buildTask(Route.DESPARASITACION.showView()));
        taskList.add(uiLoader.<controller.deworming.RecoverController>buildTask(Route.DESPARASITACION.recoverView()));
        // Load exam
        taskList.add(uiLoader.<controller.exam.IndexController>buildTask(Route.EXAMEN.indexView()));
        taskList.add(uiLoader.<controller.exam.ModalDialogController>buildTask(Route.EXAMEN.modalView()));
        taskList.add(uiLoader.<controller.exam.NewController>buildTask(Route.EXAMEN.newView()));
        taskList.add(uiLoader.<controller.exam.ShowController>buildTask(Route.EXAMEN.showView()));
        taskList.add(uiLoader.<controller.exam.RecoverController>buildTask(Route.EXAMEN.recoverView()));
        taskList.add(uiLoader.<controller.exam.ViewController>buildTask(RouteExtra.EXAMVIEW.getPath()));
        // Load hospitalization
        taskList.add(uiLoader.<controller.hospitalization.IndexController>buildTask(Route.INTERNACION.indexView()));
        taskList.add(
                uiLoader.<controller.hospitalization.ModalDialogController>buildTask(Route.INTERNACION.modalView()));
        taskList.add(uiLoader.<controller.hospitalization.NewController>buildTask(Route.INTERNACION.newView()));
        taskList.add(uiLoader.<controller.hospitalization.ShowController>buildTask(Route.INTERNACION.showView()));
        taskList.add(uiLoader.<controller.hospitalization.RecoverController>buildTask(Route.INTERNACION.recoverView()));
        // Load owner
        taskList.add(uiLoader.<controller.owner.IndexController>buildTask(Route.PROPIETARIO.indexView()));
        taskList.add(uiLoader.<controller.owner.ModalDialogController>buildTask(Route.PROPIETARIO.modalView()));
        taskList.add(uiLoader.<controller.owner.NewController>buildTask(Route.PROPIETARIO.newView()));
        taskList.add(uiLoader.<controller.owner.RecoverController>buildTask(Route.PROPIETARIO.recoverView()));
        // Load vaccine
        taskList.add(uiLoader.<controller.vaccine.IndexController>buildTask(Route.VACUNA.indexView()));
        taskList.add(uiLoader.<controller.vaccine.ModalDialogController>buildTask(Route.VACUNA.modalView()));
        taskList.add(uiLoader.<controller.vaccine.NewController>buildTask(Route.VACUNA.newView()));
        taskList.add(uiLoader.<controller.vaccine.ShowController>buildTask(Route.VACUNA.showView()));
        taskList.add(uiLoader.<controller.vaccine.RecoverController>buildTask(Route.VACUNA.recoverView()));
    }
}

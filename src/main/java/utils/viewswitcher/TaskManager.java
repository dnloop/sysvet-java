package utils.viewswitcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;
import utils.DatabaseInitException;
import utils.DialogBox;
import utils.HibernateUtil;
import utils.validator.HibernateValidator;

/**
 * Script containing all available tasks to be loaded in the background.
 * 
 * @author dnloop
 */
public class TaskManager {

	private static final Logger log = (Logger) LogManager.getLogger(TaskManager.class);

	private static final Marker marker = MarkerManager.getMarker("CLASS");

	private static String message = "";

	/**
	 * Sets the initial configuration for Hibernate.
	 * 
	 * @return The Hibernate task.
	 */
	public static final Task<Void> hibernateConfiguration = new Task<Void>() {
		@Override
		protected Void call() throws Exception {
			log.info(marker, "Loading Database");
			try {
				HibernateUtil.setUp();
				HibernateValidator.buildValid();
			} catch (DatabaseInitException e) {
				log.error(marker, "Unable establish the session. " + e.getMessage());
				message = e.getMessage();
				failed();
			}
			return null;
		}

		@Override
		protected void cancelled() {
			updateMessage("Carga cancelada.");
			log.debug(marker, "Canceled Hibernate module loading.");
		}

		@Override
		protected void failed() {
			updateMessage("Carga fallida.");
			log.error(marker, "Hibernate module loading failed.");
			DialogBox.setHeader("Error al iniciar la aplicación.");
			DialogBox.setContent(message);
			DialogBox.displayError();
			Platform.exit();
		}

	};
}

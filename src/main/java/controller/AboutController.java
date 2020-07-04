package controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AboutController {

	@FXML
	private ImageView logo;

	@FXML
	private TextFlow content;

	@FXML
	private TextField txtContact;

	private Scanner reader;

	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

	private Calendar date = Calendar.getInstance();

	private Text text;

	@FXML
	void initialize() throws FileNotFoundException {

		final InputStream license1 = getClass().getResourceAsStream("/license/apache-p1");

		final InputStream license2 = getClass().getResourceAsStream("/license/apache-p2");

		setLogo();

		fileLoader(license1);

		text = new Text("   Copyright " + formatter.format(date.getTime()) + " dnloop \n\n");

		content.getChildren().add(text);

		fileLoader(license2);

	}

	public void fileLoader(InputStream license2) {
		reader = new Scanner(license2);
		while (reader.hasNextLine()) {
			text = new Text(reader.nextLine() + "\n");

			content.getChildren().add(text);
		}
		reader.close();
	}

	private void setLogo() {
		URL url;
		url = getClass().getResource("/images/SysvetLogo.png");
		Image image = new Image(url.toString());
		logo.setImage(image);
	}
}

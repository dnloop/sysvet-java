package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

	private final String license1 = getClass().getClassLoader().getResource("license/apache-p1").getFile();

	private final String license2 = getClass().getClassLoader().getResource("license/apache-p2").getFile();

	private final File file1 = new File(license1);

	private final File file2 = new File(license2);

	private Scanner reader;

	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

	private Calendar date = Calendar.getInstance();

	private Text text;

	@FXML
	void initialize() throws FileNotFoundException {

		setLogo();

		fileLoader(file1);

		text = new Text("   Copyright " + formatter.format(date.getTime()) + " dnloop \n\n");

		content.getChildren().add(text);

		fileLoader(file2);

	}

	public void fileLoader(File file) {
		try {

			reader = new Scanner(file);
			while (reader.hasNextLine()) {
				text = new Text(reader.nextLine() + "\n");

				content.getChildren().add(text);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setLogo() {
		URL url;
		url = getClass().getResource("/images/SysvetLogo.png");
		Image image = new Image(url.toString());
		logo.setImage(image);
	}
}

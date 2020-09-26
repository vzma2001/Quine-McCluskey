package base;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApp extends Application {

	private HBox logicFormatterPage;
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;

		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/LogicFormatterPage.fxml"));
			logicFormatterPage = (HBox) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(logicFormatterPage);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
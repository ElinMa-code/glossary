
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GlossaryApp extends Application {

	// Define the absolute path
	private static final String GLOSSARY_FILE_PATH = "C:/Users/elinm/eclipse-workspace/glossary/glossary.ser";
	private GlossaryController controller;

	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("glossary.fxml"));
			BorderPane root = loader.load();

			controller = loader.getController();
			loadGlossary(); // Load the glossary when the application starts

			Scene scene = new Scene(root, 700, 720);
			primaryStage.setTitle("Glossary Application");
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(event -> saveGlossary()); // Save the glossary when the application closes
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadGlossary() {
		if (controller == null) {
	        // If the controller is null, there's no point in trying to load the glossary
	        return;
	    }
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(GLOSSARY_FILE_PATH))) {
			@SuppressWarnings("unchecked")
			TreeMap<String, String> glossary = (TreeMap<String, String>) in.readObject();
			controller.setGlossary(glossary);
		} catch (Exception e) {
			// Handle exception (e.g., file not found, deserialization error) and initialize
			// empty glossary
			e.printStackTrace();
		}
	}

	private void saveGlossary() {
		if (controller != null) {
			try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(GLOSSARY_FILE_PATH))) {
				out.writeObject(controller.getGlossary());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.Serializable;
import java.util.TreeMap;

public class GlossaryController implements Serializable {

	private static final long serialVersionUID = 1L;

	private TreeMap<String, String> glossary;
	private ObservableList<String> termsList;

	@FXML
	private ListView<String> termsListView;
	@FXML
	private TextField termTextField;
	@FXML
	private TextArea definitionTextArea;
	@FXML
	private TextArea definitionDisplayTextArea;

	public GlossaryController() {
		glossary = new TreeMap<>();
		termsList = FXCollections.observableArrayList();
	}

	@FXML
	public void initialize() {
		termsListView.setItems(termsList);

		// Set the TextArea to be non-editable
		definitionDisplayTextArea.setEditable(false);

		// Add listener to ListView's selection model (with lambda)
		termsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// Update the content of the TextArea based on the selected term
			if (newValue != null) {
				String definition = glossary.get(newValue);
				definitionDisplayTextArea.setText(definition);
			} else {
				definitionDisplayTextArea.clear();
			}
		});
	}

	// Method to get the glossary map
	public TreeMap<String, String> getGlossary() {
		return glossary;
	}

	// Method to set the glossary map
	public void setGlossary(TreeMap<String, String> glossary) {
		this.glossary = glossary;
		updateTermsList();
	}

	@FXML
	private void handleAdd() {
		String term = termTextField.getText().trim();
		String definition = definitionTextArea.getText();

		if (term.isEmpty() || definition.isEmpty()) {
			showAlert(AlertType.ERROR, "Invalid Input", "Term and definition cannot be empty.");
			clearInputFields();
			return;
		}

		if (glossary.containsKey(term)) {
			showAlert(AlertType.ERROR, "Invalid Input", "Term Already exists.");
			clearInputFields();
			return;
		} else {
			glossary.put(term, definition);
			updateTermsList();
			clearInputFields();
			showAlert(AlertType.INFORMATION, "Success", "Term added successfully.");
		}

	}

	@FXML
	private void handleDelete() {
		String term = termTextField.getText();

		if (term.isEmpty()) {
			showAlert(AlertType.ERROR, "Invalid Input", "Term cannot be empty.");
			clearInputFields();
			return;
		}

		if (glossary.containsKey(term)) {
			glossary.remove(term);
			updateTermsList();
			clearInputFields();
			showAlert(AlertType.INFORMATION, "Success", "Term deleted successfully.");
		} else {
			showAlert(AlertType.ERROR, "Not Found", "Term not found in the glossary.");
			clearInputFields();
		}
	}

	@FXML
	private void handleUpdate() {
		String term = termTextField.getText();
		String definition = definitionTextArea.getText();

		if (term.isEmpty() || definition.isEmpty()) {
			showAlert(AlertType.ERROR, "Invalid Input", "Term and definition cannot be empty.");
			return;
		}

		if (glossary.containsKey(term)) {
			glossary.put(term, definition);
			updateTermsList();
			clearInputFields();
			showAlert(AlertType.INFORMATION, "Success", "Term updated successfully.");
		} else {
			showAlert(AlertType.ERROR, "Not Found", "Term not found.");
			termTextField.clear();
		}
	}

	@FXML
	private void handleLookUp() {
		String term = termTextField.getText();

		if (term.isEmpty()) {
			showAlert(AlertType.ERROR, "Invalid Input", "Term cannot be empty.");
			clearInputFields();
			return;
		}

		String definition = glossary.get(term);

		if (definition != null) {
			showAlert(AlertType.INFORMATION, "Definition", term + ": " + definition);
			clearInputFields();
		} else {
			showAlert(AlertType.ERROR, "Not Found", "Term not found.");
			clearInputFields();
		}
	}

	private void updateTermsList() {
		termsList.setAll(glossary.keySet());
	}

	private void clearInputFields() {
		termTextField.clear();
		definitionTextArea.clear();
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}

package it.unife.lp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import it.unife.lp.model.Studente;
import it.unife.lp.MainApp;
import javafx.scene.control.Alert.AlertType;

public class StudentiOverviewController {
    @FXML
    private TableView<Studente> studentiTable;
    @FXML
    private TableColumn<Studente, String> nomeColumn;
    @FXML
    private TableColumn<Studente, String> cognomeColumn;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label cognomeLabel;
    @FXML
    private Label dataNascitaLabel;
    @FXML
    private Label classeLabel;
    @FXML
    private Label corsiLabel;
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public StudentiOverviewController() {
        // Constructor logic (if any) goes here
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the student table with the two columns.
        nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        // Clear student details.
        showStudentDetails(null);
        // Listen for selection changes and show the student details when changed.
        studentiTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStudentDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        studentiTable.setItems(mainApp.getStudentiData());
    }

    private void showStudentDetails(Studente studente) {
        if (studente != null) {
            // Fill the labels with info from the student object.
            nomeLabel.setText(studente.getNome());
            cognomeLabel.setText(studente.getCognome());
            dataNascitaLabel.setText(studente.getDataNascita().toString());
            classeLabel.setText(studente.getClasse());
            corsiLabel.setText(getCorsiAsString(studente));
        } else {
            // Student is null, remove all the text.
            nomeLabel.setText("");
            cognomeLabel.setText("");
            dataNascitaLabel.setText("");
            classeLabel.setText("");
            corsiLabel.setText("");
        }
    }

    private String getCorsiAsString(Studente studente) {
        StringBuilder corsiStr = new StringBuilder();
        for (var corso : studente.getCorsi()) {
            if (corsiStr.length() > 0) {
                corsiStr.append(", ");
            }
            corsiStr.append(corso.getNome());
        }
        return corsiStr.toString();
    }

    @FXML
    private void handleDeleteStudente(){
        int selectedIndex = studentiTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            studentiTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No student selected");
            alert.setContentText("Please select a student from the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewStudente() {
        Studente tempStudente = new Studente(0, "", "", "", "", javafx.collections.FXCollections.observableArrayList());
        boolean okClicked = mainApp.showStudentEditDialog(tempStudente);
        if (okClicked) {
            mainApp.getStudentiData().add(tempStudente);
        }
    }

    @FXML
    private void handleEditStudente() {
        Studente selectedStudente = studentiTable.getSelectionModel().getSelectedItem();
        if (selectedStudente != null) {
            boolean okClicked = mainApp.showStudentEditDialog(selectedStudente);
            if (okClicked) {
                showStudentDetails(selectedStudente);
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No student selected");
            alert.setContentText("Please select a student from the table.");
            alert.showAndWait();
        }
    }

}
package it.unife.lp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import it.unife.lp.model.Insegnante;
import it.unife.lp.MainApp;
import javafx.scene.control.Alert.AlertType;

public class TeacherOverviewController {
    @FXML
    private TableView<Insegnante> insegnantiTable;
    @FXML
    private TableColumn<Insegnante, String> nomeColumn;
    @FXML
    private TableColumn<Insegnante, String> cognomeColumn;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label cognomeLabel;
    @FXML
    private Label materiaLabel;
    @FXML
    private Label corsiInsegnatiLabel;
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TeacherOverviewController() {
        // Constructor logic (if any) goes here
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the teacher table with the two columns.
        nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        // Clear teacher details.
        showTeacherDetails(null);
        // Listen for selection changes and show the teacher details when changed.
        insegnantiTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTeacherDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        insegnantiTable.setItems(mainApp.getInsegnantiData());
    }

    private void showTeacherDetails(Insegnante insegnante) {
        if (insegnante != null) {
            // Fill the labels with info from the teacher object.
            nomeLabel.setText(insegnante.getNome());
            cognomeLabel.setText(insegnante.getCognome());
            materiaLabel.setText(insegnante.getMateria());
            corsiInsegnatiLabel.setText(getCorsiAsString(insegnante));
        } else {
            // Teacher is null, remove all the text.
            nomeLabel.setText("");
            cognomeLabel.setText("");
            materiaLabel.setText("");
            corsiInsegnatiLabel.setText("");
        }
    }

    private String getCorsiAsString(Insegnante insegnante) {
        StringBuilder corsiStr = new StringBuilder();
        if (insegnante.getCorsiInsegnati() != null) {
            for (var corso : insegnante.getCorsiInsegnati()) {
                if (corsiStr.length() > 0) {
                    corsiStr.append(", ");
                }
                corsiStr.append(corso.getNome());
            }
        }
        return corsiStr.toString();
    }

    @FXML
    private void handleDeleteInsegnante(){
        int selectedIndex = insegnantiTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            insegnantiTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No teacher selected");
            alert.setContentText("Please select a teacher from the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewInsegnante() {
        Insegnante tempInsegnante = new Insegnante(0, "", "", "", javafx.collections.FXCollections.observableArrayList());
        boolean okClicked = mainApp.showTeacherEditDialog(tempInsegnante);
        if (okClicked) {
            mainApp.getInsegnantiData().add(tempInsegnante);
        }
    }

    @FXML
    private void handleEditInsegnante() {
        Insegnante selectedInsegnante = insegnantiTable.getSelectionModel().getSelectedItem();
        if (selectedInsegnante != null) {
            boolean okClicked = mainApp.showTeacherEditDialog(selectedInsegnante);
            if (okClicked) {
                showTeacherDetails(selectedInsegnante);
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No teacher selected");
            alert.setContentText("Please select a teacher from the table.");
            alert.showAndWait();
        }
    }

}
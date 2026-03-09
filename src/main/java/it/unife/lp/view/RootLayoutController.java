package it.unife.lp.view;

import java.io.File;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import it.unife.lp.MainApp;
import it.unife.lp.MainApp.DataType;
import it.unife.lp.model.Studente;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class RootLayoutController {
    // Reference to the main application
    private MainApp mainApp;
    /**
    * Is called by the main application to give a reference back to itself.
    *
    * @param mainApp
    */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates a new empty data set based on current active data type.
     */
    @FXML
    private void handleNew() {
        DataType dataType = mainApp.getCurrentDataType();
        if (dataType == DataType.STUDENTE) {
            mainApp.getStudentiData().clear();
        } else if (dataType == DataType.INSEGNANTE) {
            mainApp.getInsegnantiData().clear();
        } else if (dataType == DataType.CORSO) {
            mainApp.getCorsiData().clear();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to load.
    */

    @FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        
        // Set initial directory to user's home
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);

        // Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            loadDataFile(file);
        }
    }

    /**
     * Saves the file. If there is no open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSaveFile() {
        saveDataFile();
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        
        // Set initial directory to user's home
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);
        fileChooser.setInitialFileName("school.json");

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            System.out.println("DEBUG: Selected file: " + file.getAbsolutePath());
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }
            System.out.println("DEBUG: File to save: " + file.getAbsolutePath());
            saveDataToFile(file);
        }
    }

    /**
     * Save current data based on active page/data type
     */
    private void saveDataFile() {
        File file = mainApp.currentDataFilePath;
        if (file != null) {
            // Save to existing file
            saveDataToFile(file);
        } else {
            // No file opened yet, show save as dialog
            handleSaveAs();
        }
    }

    /**
     * Save data to file based on current active data type
     */
    private void saveDataToFile(File file) {
        DataType dataType = mainApp.getCurrentDataType();
        if (dataType == DataType.STUDENTE) {
            mainApp.saveDataToFile(file, mainApp.getStudentiData(), DataType.STUDENTE);
        } else if (dataType == DataType.INSEGNANTE) {
            mainApp.saveDataToFile(file, mainApp.getInsegnantiData(), DataType.INSEGNANTE);
        } else if (dataType == DataType.CORSO) {
            mainApp.saveDataToFile(file, mainApp.getCorsiData(), DataType.CORSO);
        }
    }

    /**
     * Load data from file based on a generic approach
     */
    private void loadDataFile(File file) {
        DataType dataType = mainApp.getCurrentDataType();
        // Load all three files at once based on any data type
        mainApp.loadDataFromFile(file, Studente.class, dataType);
    }

    @FXML
    private void handleStudenti() {
        mainApp.showStudentiOverview();
    }

    @FXML
    private void handleInsegnanti() {
        mainApp.showInsegnantiOverview();
    }

    @FXML
    private void handleCorsi() {
        mainApp.showCorsoOverview();
    }

    @FXML
    private void handleFunzionamento() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How the application works");
        alert.setHeaderText("Usage instructions");
        String instructions = "1. Default data is loaded on startup; you can edit it or open your own files.\n"
            + "2. Use the File menu to create, open, and save data.\n"
            + "3. Choose Students, Teachers, or Courses from the View menu to manage each section.\n"
            + "4. In Students, you can add, edit, delete students, and enroll them in courses.\n"
            + "5. In Teachers, you can add, edit, or delete teachers.\n"
            + "6. In Courses, you can add, edit, delete courses, and assign teachers to courses.\n"
            + "7. Use Add and Remove buttons to manage student enrollments in courses.\n"
            + "8. Save frequently to avoid data loss.";
        javafx.scene.control.TextArea content = new javafx.scene.control.TextArea(instructions);
        content.setEditable(false);
        content.setWrapText(true);
        content.setPrefWidth(520);
        content.setPrefHeight(260);
        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }

    /**
    * Closes the application.
    */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    
}
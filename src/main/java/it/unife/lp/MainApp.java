package it.unife.lp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import it.unife.lp.model.Studente;
import it.unife.lp.model.Insegnante;
import it.unife.lp.view.RootLayoutController;
import it.unife.lp.view.StudentEditDialogController;
import it.unife.lp.view.StudentiOverviewController;
import java.io.File;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.unife.lp.model.Corso;
import it.unife.lp.view.TeacherOverviewController;
import it.unife.lp.view.TeacherEditDialogController;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private DataType currentDataType = DataType.STUDENTE;

    private ObservableList<Insegnante> insegnantiData = FXCollections.observableArrayList();
    private ObservableList<Corso> corsiData = FXCollections.observableArrayList();
    private ObservableList<Studente> studentiData = FXCollections.observableArrayList();

    public enum DataType{
        STUDENTE,
        INSEGNANTE,
        CORSO
    }

    public MainApp() {
        // Dati di esempio possono essere aggiunti qui se necessario
        // Nel progetto i dati default vengono caricati da file JSON
    }

    public ObservableList<Corso> getCorsiData() {
        return corsiData;
    }
    public ObservableList<Studente> getStudentiData() {
        return studentiData;
    }
    public ObservableList<Insegnante> getInsegnantiData() {
        return insegnantiData;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public DataType getCurrentDataType() {
        return currentDataType;
    }

    public void setCurrentDataType(DataType dataType) {
        this.currentDataType = dataType;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestione Scuola");

        initRootLayout();
        showCorsoOverview();
    }

    public void showStudentiOverview() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StudentOverview.fxml"));
            AnchorPane studentiOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(studentiOverview);

            StudentiOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setCurrentDataType(DataType.STUDENTE);
    }

    public void showInsegnantiOverview() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TeacherOverview.fxml"));
            AnchorPane insegnantiOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(insegnantiOverview);

            TeacherOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setCurrentDataType(DataType.INSEGNANTE);
    }

    public void showCorsoOverview() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CourseOverview.fxml"));
            AnchorPane corsoOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(corsoOverview);

            it.unife.lp.view.CourseOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setCurrentDataType(DataType.CORSO);
    }

    public void initRootLayout() {
        try {
        // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        // Try to load the last file that was saved
        File lastFile = loadLastFilePath();
        if (lastFile != null && lastFile.exists()) {
            loadDataFromFile(lastFile, Studente.class, DataType.STUDENTE);
            System.out.println("Loaded previously saved file: " + lastFile.getName());
        } else {
            // Load default data on startup only if no previous file exists
            try {
                File defaultFile = new File(MainApp.class.getResource("default_studenti.json").toURI());
                loadDataFromFile(defaultFile, Studente.class, DataType.STUDENTE);
                System.out.println("Loaded default data");
            } catch (Exception e) {
                System.err.println("Could not load default data: " + e.getMessage());
            }
        }
    }

    public boolean showStudentEditDialog(Studente studente) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StudentEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifica Studente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Set the person into the controller.
            StudentEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setStudente(studente);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showTeacherEditDialog(Insegnante insegnante) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TeacherEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifica Insegnante");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Set the person into the controller.
            TeacherEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setInsegnante(insegnante);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showCourseEditDialog(Corso corso) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CourseEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifica Corso");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Set the person into the controller.
            it.unife.lp.view.CourseEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setCorso(corso);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generic save method that works for all data types
    public <T> void saveDataToFile(File file, ObservableList<T> data, DataType dataType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            
            // Get the base directory from the selected file
            File baseDir = file.getParentFile();
            String fileName = file.getName().replaceFirst("[.][^.]+$", ""); // Remove extension
            
            // Remove type suffixes if they exist using Stream API
            String baseName = java.util.stream.Stream.of("_studenti", "_insegnanti", "_corsi")
                .filter(fileName::endsWith)
                .findFirst()
                .map(suffix -> fileName.substring(0, fileName.length() - suffix.length()))
                .orElse(fileName);
            
            System.out.println("DEBUG: Saving to baseName: " + baseName);
            System.out.println("DEBUG: studentiData count: " + studentiData.size());
            System.out.println("DEBUG: insegnantiData count: " + insegnantiData.size());
            System.out.println("DEBUG: corsiData count: " + corsiData.size());
            
            // Save only the file for the current data type
            // Only save other files if they already exist
            if (dataType == DataType.STUDENTE) {
                mapper.writeValue(new File(baseDir, baseName + "_studenti.json"), studentiData);
                File insegnantiFile = new File(baseDir, baseName + "_insegnanti.json");
                File corsiFile = new File(baseDir, baseName + "_corsi.json");
                if (insegnantiFile.exists()) {
                    mapper.writeValue(insegnantiFile, insegnantiData);
                }
                if (corsiFile.exists()) {
                    mapper.writeValue(corsiFile, corsiData);
                }
            } else if (dataType == DataType.INSEGNANTE) {
                mapper.writeValue(new File(baseDir, baseName + "_insegnanti.json"), insegnantiData);
                File studentiFile = new File(baseDir, baseName + "_studenti.json");
                File corsiFile = new File(baseDir, baseName + "_corsi.json");
                if (studentiFile.exists()) {
                    mapper.writeValue(studentiFile, studentiData);
                }
                if (corsiFile.exists()) {
                    mapper.writeValue(corsiFile, corsiData);
                }
            } else if (dataType == DataType.CORSO) {
                mapper.writeValue(new File(baseDir, baseName + "_corsi.json"), corsiData);
                File studentiFile = new File(baseDir, baseName + "_studenti.json");
                File insegnantiFile = new File(baseDir, baseName + "_insegnanti.json");
                if (studentiFile.exists()) {
                    mapper.writeValue(studentiFile, studentiData);
                }
                if (insegnantiFile.exists()) {
                    mapper.writeValue(insegnantiFile, insegnantiData);
                }
            }
            
            System.out.println("DEBUG: Successfully saved to " + baseDir.getAbsolutePath());
            
            // Save the file path to the registry.
            setDataFilePath(file, dataType);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    // Generic load method that works for all data types
    public <T> void loadDataFromFile(File file, Class<T> elementType, DataType dataType) {
        try {
            System.out.println(">>> LOAD DATA FROM FILE CALLED with file: " + file.getName());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            // Get the base directory from the selected file
            File baseDir = file.getParentFile();
            String fileName = file.getName().replaceFirst("[.][^.]+$", ""); // Remove extension
            
            // Remove the type suffix (_studenti, _insegnanti, _corsi) to get the base name using Stream API
            String baseName = java.util.stream.Stream.of("_studenti", "_insegnanti", "_corsi")
                .filter(fileName::endsWith)
                .findFirst()
                .map(suffix -> fileName.substring(0, fileName.length() - suffix.length()))
                .orElse(fileName);
            
            System.out.println("Base directory: " + baseDir);
            System.out.println("File name: " + fileName);
            System.out.println("Base name: " + baseName);
            
            // Load all three files
            File studentiFile = new File(baseDir, baseName + "_studenti.json");
            File insegnantiFile = new File(baseDir, baseName + "_insegnanti.json");
            File corsiFile = new File(baseDir, baseName + "_corsi.json");
            
            System.out.println("Looking for studenti file: " + studentiFile.getAbsolutePath());
            System.out.println("Studenti file exists: " + studentiFile.exists());
            
            if (studentiFile.exists()) {
                // Create new mapper for each file to avoid ID conflicts
                ObjectMapper studentiMapper = new ObjectMapper();
                studentiMapper.registerModule(new JavaTimeModule());
                List<Studente> loadedStudenti = studentiMapper.readValue(studentiFile, 
                    studentiMapper.getTypeFactory().constructCollectionType(List.class, Studente.class));
                System.out.println("Loaded " + loadedStudenti.size() + " students from JSON");
                studentiData.setAll(loadedStudenti);
            } else {
                studentiData.clear();
            }
            
            System.out.println("Looking for insegnanti file: " + insegnantiFile.getAbsolutePath());
            System.out.println("Insegnanti file exists: " + insegnantiFile.exists());
            
            if (insegnantiFile.exists()) {
                // Create new mapper for each file to avoid ID conflicts
                ObjectMapper insegnantiMapper = new ObjectMapper();
                insegnantiMapper.registerModule(new JavaTimeModule());
                List<Insegnante> loadedInsegnanti = insegnantiMapper.readValue(insegnantiFile, 
                    insegnantiMapper.getTypeFactory().constructCollectionType(List.class, Insegnante.class));
                System.out.println("Loaded " + loadedInsegnanti.size() + " teachers from JSON");
                insegnantiData.setAll(loadedInsegnanti);
            } else {
                insegnantiData.clear();
            }
            
            System.out.println("Looking for corsi file: " + corsiFile.getAbsolutePath());
            System.out.println("Corsi file exists: " + corsiFile.exists());
            
            if (corsiFile.exists()) {
                // Create new mapper for each file to avoid ID conflicts
                ObjectMapper corsiMapper = new ObjectMapper();
                corsiMapper.registerModule(new JavaTimeModule());
                List<Corso> loadedCorsi = corsiMapper.readValue(corsiFile, 
                    corsiMapper.getTypeFactory().constructCollectionType(List.class, Corso.class));
                System.out.println("Loaded " + loadedCorsi.size() + " courses from JSON");
                corsiData.setAll(loadedCorsi);
            } else {
                corsiData.clear();
            }
            
            // Reconstruct bidirectional relationships
            rebuildRelationships();
            
            System.out.println("Loaded data - Studenti: " + studentiData.size() + ", Insegnanti: " + insegnantiData.size() + ", Corsi: " + corsiData.size());
            if (!studentiData.isEmpty()) {
                System.out.println("First student: " + studentiData.get(0).getNome() + " " + studentiData.get(0).getCognome());
            }
            
            setDataFilePath(file, dataType);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
            e.printStackTrace(); // Print stack trace to help debug
        }
    }
    
    /**
     * Reconstruct bidirectional relationships after loading from JSON files.
     * The JSON files store only IDs, so we need to rebuild the object references.
     */
    private void rebuildRelationships() {
        // Create maps for quick lookup using Stream API
        java.util.Map<Integer, Corso> corsoMap = corsiData.stream()
            .peek(corso -> {
                if (corso.getStudentiIscritti() == null) {
                    corso.setStudentiIscritti(FXCollections.observableArrayList());
                } else {
                    corso.getStudentiIscritti().clear();
                }
            })
            .collect(java.util.stream.Collectors.toMap(Corso::getId, corso -> corso));
        
        java.util.Map<Integer, Studente> studenteMap = studentiData.stream()
            .peek(studente -> {
                if (studente.getCorsi() == null) {
                    studente.setCorsi(FXCollections.observableArrayList());
                } else {
                    studente.getCorsi().clear();
                }
            })
            .collect(java.util.stream.Collectors.toMap(Studente::getId, studente -> studente));
        
        java.util.Map<Integer, Insegnante> insegnanteMap = insegnantiData.stream()
            .peek(insegnante -> {
                if (insegnante.getCorsiInsegnati() == null) {
                    insegnante.setCorsiInsegnati(FXCollections.observableArrayList());
                } else {
                    insegnante.getCorsiInsegnati().clear();
                }
            })
            .collect(java.util.stream.Collectors.toMap(Insegnante::getId, insegnante -> insegnante));
        
        // Rebuild course-teacher relationships using Stream API
        corsiData.stream()
            .filter(corso -> corso.getInsegnante() != null && corso.getInsegnante().getId() > 0)
            .forEach(corso -> {
                java.util.Optional.ofNullable(insegnanteMap.get(corso.getInsegnante().getId()))
                    .ifPresent(actualTeacher -> {
                        corso.setInsegnante(actualTeacher);
                        if (actualTeacher.getCorsiInsegnati() != null) {
                            actualTeacher.getCorsiInsegnati().add(corso);
                        }
                    });
            });
        
        // Rebuild student-course relationships from stored IDs using Stream API
        studentiData.stream()
            .filter(studente -> studente.getTempCorsiIds() != null)
            .forEach(studente -> 
                studente.getTempCorsiIds().stream()
                    .map(corsoMap::get)
                    .filter(java.util.Objects::nonNull)
                    .forEach(corso -> {
                        studente.getCorsi().add(corso);
                        corso.getStudentiIscritti().add(studente);
                    })
            );
        
        // Also rebuild from corso side if studentiIscritti IDs were stored
        corsiData.stream()
            .filter(corso -> corso.getTempStudentiIscrittiIds() != null)
            .forEach(corso -> 
                corso.getTempStudentiIscrittiIds().stream()
                    .map(studenteMap::get)
                    .filter(java.util.Objects::nonNull)
                    .filter(studente -> !corso.getStudentiIscritti().contains(studente))
                    .forEach(studente -> {
                        corso.getStudentiIscritti().add(studente);
                        if (!studente.getCorsi().contains(corso)) {
                            studente.getCorsi().add(corso);
                        }
                    })
            );
        
        // Also rebuild from teacher side if corsiInsegnati IDs were stored
        insegnantiData.stream()
            .filter(insegnante -> insegnante.getTempCorsiInsegnatiIds() != null)
            .forEach(insegnante -> 
                insegnante.getTempCorsiInsegnatiIds().stream()
                    .map(corsoMap::get)
                    .filter(java.util.Objects::nonNull)
                    .forEach(corso -> {
                        if (!insegnante.getCorsiInsegnati().contains(corso)) {
                            insegnante.getCorsiInsegnati().add(corso);
                        }
                        if (corso.getInsegnante() == null || corso.getInsegnante().getId() == 0) {
                            corso.setInsegnante(insegnante);
                        }
                    })
            );
        
        System.out.println("Relationships rebuilt successfully");
    }

    public File currentDataFilePath = null;
    private static final String CONFIG_FILE = "scuola_config.txt";

    public File getDataFilePath(DataType dataType) {
        return currentDataFilePath;
    }

    private void setDataFilePath(File file, DataType dataType) {
        // Don't set currentDataFilePath for default files (inside target/classes)
        if (file != null && !file.getAbsolutePath().contains("target")) {
            currentDataFilePath = file;
            if (file != null) {
                primaryStage.setTitle("Gestione Scuola - " + file.getName());
                // Save the file path to configuration so it's remembered next time
                saveLastFilePath(file);
            }
        }
    }

    /**
     * Save the last file path to a config file
     */
    private void saveLastFilePath(File file) {
        try {
            File configFile = new File(System.getProperty("user.home"), CONFIG_FILE);
            java.nio.file.Files.write(configFile.toPath(), file.getAbsolutePath().getBytes());
            System.out.println("Saved last file path to config: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Could not save config file: " + e.getMessage());
        }
    }

    /**
     * Load the last file path from config file
     */
    private File loadLastFilePath() {
        try {
            File configFile = new File(System.getProperty("user.home"), CONFIG_FILE);
            if (configFile.exists()) {
                String filePath = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
                File file = new File(filePath);
                if (file.exists()) {
                    System.out.println("Loaded last file path from config: " + filePath);
                    return file;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load config file: " + e.getMessage());
        }
        return null;
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
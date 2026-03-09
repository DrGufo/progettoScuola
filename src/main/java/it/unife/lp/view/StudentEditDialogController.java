package it.unife.lp.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import it.unife.lp.MainApp;
import it.unife.lp.model.Studente;
import it.unife.lp.model.Corso;
import it.unife.lp.util.DateUtil;

public class StudentEditDialogController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cognomeField;
    @FXML
    private DatePicker dataNascitaPicker;
    @FXML
    private TextField classeField;
    @FXML
    private ListView<Corso> corsiList;
    private Stage dialogStage;
    private Studente studente;
    private MainApp mainApp;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
        nomeField.setText(studente.getNome());
        cognomeField.setText(studente.getCognome());
        
        // Parse date using DateUtil and set to DatePicker
        if (studente.getDataNascita() != null && !studente.getDataNascita().isEmpty()) {
            dataNascitaPicker.setValue(DateUtil.parse(studente.getDataNascita()));
        }
        
        classeField.setText(studente.getClasse());
        
        // Display courses in ListView
        if (studente.getCorsi() != null) {
            corsiList.setItems(FXCollections.observableArrayList(studente.getCorsi()));
        } else {
            corsiList.setItems(FXCollections.observableArrayList());
        }
        
        // Set cell factory for corsi list
        corsiList.setCellFactory(param -> new javafx.scene.control.ListCell<Corso>() {
            @Override
            protected void updateItem(Corso item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome());
                }
            }
        });
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            // Assign next ID only if it's a new student (ID == 0)
            if (studente.getId() == 0) {
                int maxId = 0;
                for (Studente s : mainApp.getStudentiData()) {
                    if (s.getId() > maxId) {
                        maxId = s.getId();
                    }
                }
                studente.setId(maxId + 1);
            }

            studente.setNome(nomeField.getText());
            studente.setCognome(cognomeField.getText());
            
            // Convert DatePicker value to string using DateUtil
            if (dataNascitaPicker.getValue() != null) {
                studente.setDataNascita(DateUtil.format(dataNascitaPicker.getValue()));
            }
            
            studente.setClasse(classeField.getText());
            
            // Update corsi from ListView (handled by ObservableList binding)
            
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleAddCorso() {
        SelectCourseDialog dialog = new SelectCourseDialog(mainApp.getCorsiData(), studente.getCorsi());
        Corso selected = dialog.showDialog(dialogStage);
        
        if (selected != null && !studente.getCorsi().contains(selected)) {
            studente.getCorsi().add(selected);
            // Add student to course's student list if not already there
            if (!selected.getStudentiIscritti().contains(studente)) {
                selected.getStudentiIscritti().add(studente);
            }
            corsiList.setItems(FXCollections.observableArrayList(studente.getCorsi()));
        }
    }

    @FXML
    private void handleRemoveCorso() {
        Corso selected = corsiList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            studente.getCorsi().remove(selected);
            // Remove student from course
            if (selected.getStudentiIscritti().contains(studente)) {
                selected.getStudentiIscritti().remove(studente);
            }
            corsiList.setItems(FXCollections.observableArrayList(studente.getCorsi()));
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (cognomeField.getText() == null || cognomeField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (dataNascitaPicker.getValue() == null) {
            errorMessage += "No valid birthdate!\n";
        }
        if (classeField.getText() == null || classeField.getText().length() == 0) {
            errorMessage += "No valid class!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
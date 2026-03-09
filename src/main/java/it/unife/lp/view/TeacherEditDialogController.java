package it.unife.lp.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import it.unife.lp.model.Insegnante;
import it.unife.lp.model.Corso;
import it.unife.lp.MainApp;


public class TeacherEditDialogController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cognomeField;
    @FXML
    private TextField materiaField;
    @FXML
    private ListView<Corso> corsiInsegnatiList;
    private Stage dialogStage;
    private MainApp mainApp;
    private Insegnante insegnante;
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

    public void setInsegnante(Insegnante insegnante) {
        this.insegnante = insegnante;
        nomeField.setText(insegnante.getNome());
        cognomeField.setText(insegnante.getCognome());
        materiaField.setText(insegnante.getMateria());
        
        // Display courses in ListView
        if (insegnante.getCorsiInsegnati() != null) {
            corsiInsegnatiList.setItems(FXCollections.observableArrayList(insegnante.getCorsiInsegnati()));
        } else {
            corsiInsegnatiList.setItems(FXCollections.observableArrayList());
        }
        
        // Set cell factory for corsi list
        corsiInsegnatiList.setCellFactory(param -> new javafx.scene.control.ListCell<Corso>() {
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
            // Assign next ID only for new teachers (id == 0)
            if (insegnante.getId() == 0) {
                int maxId = 0;
                for (Insegnante i : mainApp.getInsegnantiData()) {
                    if (i.getId() > maxId) {
                        maxId = i.getId();
                    }
                }
                insegnante.setId(maxId + 1);
            }

            insegnante.setNome(nomeField.getText());
            insegnante.setCognome(cognomeField.getText());
            insegnante.setMateria(materiaField.getText());
            
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
        SelectCourseDialog dialog = new SelectCourseDialog(mainApp.getCorsiData(), insegnante.getCorsiInsegnati());
        Corso selected = dialog.showDialog(dialogStage);
        
        if (selected != null && !insegnante.getCorsiInsegnati().contains(selected)) {
            insegnante.getCorsiInsegnati().add(selected);
            // Update the course to point to this teacher
            selected.setInsegnante(insegnante);
            corsiInsegnatiList.setItems(FXCollections.observableArrayList(insegnante.getCorsiInsegnati()));
        }
    }

    @FXML
    private void handleRemoveCorso() {
        Corso selected = corsiInsegnatiList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            insegnante.getCorsiInsegnati().remove(selected);
            // Remove teacher reference from course
            if (selected.getInsegnante() != null && selected.getInsegnante().getId() == insegnante.getId()) {
                selected.setInsegnante(null);
            }
            corsiInsegnatiList.setItems(FXCollections.observableArrayList(insegnante.getCorsiInsegnati()));
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
        if (materiaField.getText() == null || materiaField.getText().length() == 0) {
            errorMessage += "No valid subject!\n";
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
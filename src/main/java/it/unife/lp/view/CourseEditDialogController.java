package it.unife.lp.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import it.unife.lp.MainApp;
import it.unife.lp.model.Corso;
import it.unife.lp.model.Insegnante;
import it.unife.lp.model.Studente;

public class CourseEditDialogController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextArea descrizioneField;
    @FXML
    private ComboBox<Insegnante> insegnanteCombo;
    @FXML
    private ListView<Studente> studentiList;
    @FXML
    private TextField idField;

    private Stage dialogStage;
    private Corso corso;
    private MainApp mainApp;
    private boolean okClicked = false;

    public CourseEditDialogController() {
    }

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Populate insegnante combo
        insegnanteCombo.setItems(mainApp.getInsegnantiData());
        insegnanteCombo.setCellFactory(param -> new javafx.scene.control.ListCell<Insegnante>() {
            @Override
            protected void updateItem(Insegnante item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome() + " " + item.getCognome());
                }
            }
        });
        insegnanteCombo.setButtonCell(new javafx.scene.control.ListCell<Insegnante>() {
            @Override
            protected void updateItem(Insegnante item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome() + " " + item.getCognome());
                }
            }
        });
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
        nomeField.setText(corso.getNome());
        descrizioneField.setText(corso.getDescrizione());
        insegnanteCombo.setValue(corso.getInsegnante());
        idField.setText(String.valueOf(corso.getId()));

        if (corso.getStudentiIscritti() != null) {
            studentiList.setItems(FXCollections.observableArrayList(corso.getStudentiIscritti()));
        }

        // Set cell factory for studenti list
        studentiList.setCellFactory(param -> new javafx.scene.control.ListCell<Studente>() {
            @Override
            protected void updateItem(Studente item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome() + " " + item.getCognome());
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
            corso.setNome(nomeField.getText());
            corso.setDescrizione(descrizioneField.getText());
            corso.setInsegnante(insegnanteCombo.getValue());
            
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleAddStudente() {
        // Create a dialog to select a student
        SelectStudentDialog dialog = new SelectStudentDialog(mainApp.getStudentiData(), corso.getStudentiIscritti());
        Studente selected = dialog.showDialog(dialogStage);
        
        if (selected != null && !corso.getStudentiIscritti().contains(selected)) {
            corso.getStudentiIscritti().add(selected);
            studentiList.setItems(FXCollections.observableArrayList(corso.getStudentiIscritti()));
        }
    }

    @FXML
    private void handleRemoveStudente() {
        Studente selected = studentiList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            corso.getStudentiIscritti().remove(selected);
            studentiList.setItems(FXCollections.observableArrayList(corso.getStudentiIscritti()));
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Invalid name!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}

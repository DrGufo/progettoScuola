package it.unife.lp.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import it.unife.lp.model.Studente;
import java.util.List;
import java.util.stream.Collectors;

public class SelectStudentDialog {
    private Stage dialogStage;
    private ListView<Studente> studentList;
    private Studente selectedStudent;
    private List<Studente> availableStudents;
    private List<Studente> alreadySelected;

    public SelectStudentDialog(List<Studente> availableStudents, List<Studente> alreadySelected) {
        this.availableStudents = availableStudents;
        this.alreadySelected = alreadySelected;
        this.selectedStudent = null;
    }

    public Studente showDialog(Window owner) {
        dialogStage = new Stage();
        dialogStage.setTitle("Select Student");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Create list of students not yet selected
        List<Studente> filteredStudents = availableStudents.stream()
            .filter(s -> !alreadySelected.contains(s))
            .collect(Collectors.toList());

        studentList = new ListView<>();
        studentList.setItems(FXCollections.observableArrayList(filteredStudents));
        studentList.setCellFactory(param -> new javafx.scene.control.ListCell<Studente>() {
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
        studentList.setPrefHeight(300);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> handleOk());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(okButton, cancelButton);

        root.getChildren().addAll(studentList, buttonBox);

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return selectedStudent;
    }

    private void handleOk() {
        selectedStudent = studentList.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            dialogStage.close();
        }
    }

    private void handleCancel() {
        selectedStudent = null;
        dialogStage.close();
    }
}

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
import it.unife.lp.model.Corso;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCourseDialog {
    private Stage dialogStage;
    private ListView<Corso> courseList;
    private Corso selectedCourse;
    private List<Corso> availableCourses;
    private List<Corso> alreadySelected;

    public SelectCourseDialog(List<Corso> availableCourses, List<Corso> alreadySelected) {
        this.availableCourses = availableCourses;
        this.alreadySelected = alreadySelected;
        this.selectedCourse = null;
    }

    public Corso showDialog(Window owner) {
        dialogStage = new Stage();
        dialogStage.setTitle("Select Course");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Create list of courses not yet selected
        List<Corso> filteredCourses = availableCourses.stream()
            .filter(c -> !alreadySelected.contains(c))
            .collect(Collectors.toList());

        courseList = new ListView<>();
        courseList.setItems(FXCollections.observableArrayList(filteredCourses));
        courseList.setCellFactory(param -> new javafx.scene.control.ListCell<Corso>() {
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
        courseList.setPrefHeight(300);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> handleOk());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(okButton, cancelButton);

        root.getChildren().addAll(courseList, buttonBox);

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return selectedCourse;
    }

    private void handleOk() {
        selectedCourse = courseList.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            dialogStage.close();
        }
    }

    private void handleCancel() {
        selectedCourse = null;
        dialogStage.close();
    }
}

package it.unife.lp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import it.unife.lp.MainApp;
import it.unife.lp.model.Corso;

public class CourseOverviewController {
    @FXML
    private TableView<Corso> corsiTable;
    @FXML
    private TableColumn<Corso, String> nomeColumn;
    @FXML
    private TableColumn<Corso, String> insegnanteColumn;

    @FXML
    private Label nomeLabel;
    @FXML
    private Label descrizioneLabel;
    @FXML
    private Label insegnanteLabel;
    @FXML
    private Label studentiLabel;
    @FXML
    private Label idLabel;

    private MainApp mainApp;

    public CourseOverviewController() {
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        nomeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        insegnanteColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getInsegnante() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getInsegnante().getNome() + " " + 
                    cellData.getValue().getInsegnante().getCognome()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        // Clear details when nothing is selected
        corsiTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showCorsoDetails(newValue)
        );
        
        // Clear labels on startup
        showCorsoDetails(null);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        corsiTable.setItems(mainApp.getCorsiData());
    }

    private void showCorsoDetails(Corso corso) {
        if (corso != null) {
            nomeLabel.setText(corso.getNome());
            descrizioneLabel.setText(corso.getDescrizione());
            
            if (corso.getInsegnante() != null) {
                insegnanteLabel.setText(corso.getInsegnante().getNome() + " " + corso.getInsegnante().getCognome());
            } else {
                insegnanteLabel.setText("-");
            }
            
            studentiLabel.setText(getStudentiAsString(corso));
            idLabel.setText(String.valueOf(corso.getId()));
        } else {
            nomeLabel.setText("");
            descrizioneLabel.setText("");
            insegnanteLabel.setText("");
            studentiLabel.setText("");
            idLabel.setText("");
        }
    }

    private String getStudentiAsString(Corso corso) {
        if (corso.getStudentiIscritti() == null || corso.getStudentiIscritti().isEmpty()) {
            return "-";
        }
        return corso.getStudentiIscritti().stream()
            .map(s -> s.getNome() + " " + s.getCognome())
            .reduce((s1, s2) -> s1 + ", " + s2)
            .orElse("-");
    }

    @FXML
    private void handleNewCorso() {
        Corso tempCorso = new Corso();
        boolean okClicked = mainApp.showCourseEditDialog(tempCorso);
        if (okClicked) {
            // Get new ID
            int newId = mainApp.getCorsiData().stream()
                .mapToInt(Corso::getId)
                .max()
                .orElse(0) + 1;
            tempCorso.setId(newId);
            mainApp.getCorsiData().add(tempCorso);
        }
    }

    @FXML
    private void handleEditCorso() {
        Corso selectedCorso = corsiTable.getSelectionModel().getSelectedItem();
        if (selectedCorso != null) {
            boolean okClicked = mainApp.showCourseEditDialog(selectedCorso);
            if (okClicked) {
                showCorsoDetails(selectedCorso);
            }
        } else {
            mainApp.showAlert("No Selection", "Please select a course to edit.");
        }
    }

    @FXML
    private void handleDeleteCorso() {
        Corso selectedCorso = corsiTable.getSelectionModel().getSelectedItem();
        if (selectedCorso != null) {
            mainApp.getCorsiData().remove(selectedCorso);
        } else {
            mainApp.showAlert("No Selection", "Please select a course to delete.");
        }
    }
}

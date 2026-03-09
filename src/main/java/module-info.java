module it.unife.lp {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.base;

    requires java.prefs;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens it.unife.lp to javafx.fxml;
    opens it.unife.lp.view to javafx.fxml;
    opens it.unife.lp.model to javafx.fxml, com.fasterxml.jackson.databind;

    exports it.unife.lp;
    exports it.unife.lp.view;
    exports it.unife.lp.model;
}

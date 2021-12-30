module es.ideas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens es.ideas to javafx.fxml;
    exports es.ideas;
    exports es.ideas.model;
}

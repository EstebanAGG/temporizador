module es.ideas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires sound;

    opens es.ideas to javafx.fxml;
    exports es.ideas;
    exports es.ideas.model;
}

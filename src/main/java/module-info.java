module es.ideas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens es.ideas to javafx.fxml;
    exports es.ideas;
}

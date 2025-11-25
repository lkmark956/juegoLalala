module org.example.juegolalala {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.juegolalala to javafx.fxml;
    exports org.example.juegolalala;
}
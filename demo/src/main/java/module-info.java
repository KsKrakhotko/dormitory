module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example.domain;
    requires org.example.server.api;

    opens org.example.application to javafx.fxml;
    exports org.example.application;
}
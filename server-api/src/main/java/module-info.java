module org.example.server.api {
    requires java.management;
    requires jakarta.persistence;
    exports commands;
    exports connect;
    exports dto;
    exports enums;
    exports service_interface;
}
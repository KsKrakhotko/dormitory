package org.example.application;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Application {
    private final StringProperty student;
    private final StringProperty dormitory;
    private final StringProperty room;
    private final StringProperty status;
    private final ObjectProperty<LocalDate> date;

    public Application(String student, String dormitory, String room, String status, LocalDate date) {
        this.student = new SimpleStringProperty(student);
        this.dormitory = new SimpleStringProperty(dormitory);
        this.room = new SimpleStringProperty(room);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleObjectProperty<>(date);
    }

    public String getStudent() {
        return student.get();
    }

    public StringProperty studentProperty() {
        return student;
    }

    public void setStudent(String student) {
        this.student.set(student);
    }

    public String getDormitory() {
        return dormitory.get();
    }

    public StringProperty dormitoryProperty() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory.set(dormitory);
    }

    public String getRoom() {
        return room.get();
    }

    public StringProperty roomProperty() {
        return room;
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }
} 
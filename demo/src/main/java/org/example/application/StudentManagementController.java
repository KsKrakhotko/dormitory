package org.example.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;

public class StudentManagementController {
    @FXML
    private ComboBox<String> dormitoryComboBox;

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private TableColumn<Student, String> fullNameColumn;

    @FXML
    private TableColumn<Student, String> groupColumn;

    @FXML
    private TableColumn<Student, String> facultyColumn;

    @FXML
    private TableColumn<Student, String> roomNumberColumn;

    @FXML
    private TableColumn<Student, LocalDate> checkInDateColumn;

    @FXML
    public void initialize() {
        // Инициализация колонок таблицы
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));

        // Инициализация ComboBox с общежитиями
        ObservableList<String> dormitories = FXCollections.observableArrayList(
            "Общежитие 1", "Общежитие 2", "Общежитие 3"
        );
        dormitoryComboBox.setItems(dormitories);

        // Добавление тестовых данных
        ObservableList<Student> students = FXCollections.observableArrayList(
            new Student("Иванов Иван Иванович", "ПИ-101", "ФИТ", "101", LocalDate.now()),
            new Student("Петров Петр Петрович", "ИВТ-202", "ФИТ", "102", LocalDate.now().minusMonths(1)),
            new Student("Сидорова Анна Павловна", "ПМИ-301", "ФИТ", "201", LocalDate.now().minusMonths(2))
        );
        studentsTable.setItems(students);
    }

    @FXML
    private void handleCheckIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student-registration.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Регистрация студента");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(dormitoryComboBox.getScene().getWindow());
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Для отладки
            showAlert("Ошибка", "Не удалось открыть форму регистрации: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCheckOut(ActionEvent event) {
        showAlert("Информация", "Функция выселения студента в разработке", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("commandant-dashboard.fxml"));
            Parent root = loader.load();
            stage.setTitle("Панель управления коменданта");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось вернуться на предыдущую страницу: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Вспомогательный класс для представления студента
    public static class Student {
        private String fullName;
        private String group;
        private String faculty;
        private String roomNumber;
        private LocalDate checkInDate;

        public Student(String fullName, String group, String faculty, String roomNumber, LocalDate checkInDate) {
            this.fullName = fullName;
            this.group = group;
            this.faculty = faculty;
            this.roomNumber = roomNumber;
            this.checkInDate = checkInDate;
        }

        // Геттеры
        public String getFullName() { return fullName; }
        public String getGroup() { return group; }
        public String getFaculty() { return faculty; }
        public String getRoomNumber() { return roomNumber; }
        public LocalDate getCheckInDate() { return checkInDate; }

        // Сеттеры
        public void setFullName(String fullName) { this.fullName = fullName; }
        public void setGroup(String group) { this.group = group; }
        public void setFaculty(String faculty) { this.faculty = faculty; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
        public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    }
} 
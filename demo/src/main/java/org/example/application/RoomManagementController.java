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

import java.io.IOException;

public class RoomManagementController {
    @FXML
    private ComboBox<String> dormitoryComboBox;

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, String> roomNumberColumn;

    @FXML
    private TableColumn<Room, Integer> floorColumn;

    @FXML
    private TableColumn<Room, Integer> capacityColumn;

    @FXML
    private TableColumn<Room, String> typeColumn;

    @FXML
    private TableColumn<Room, Integer> occupiedColumn;

    @FXML
    public void initialize() {
        // Инициализация колонок таблицы
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        occupiedColumn.setCellValueFactory(new PropertyValueFactory<>("occupied"));

        // Инициализация ComboBox с общежитиями
        ObservableList<String> dormitories = FXCollections.observableArrayList(
            "Общежитие 1", "Общежитие 2", "Общежитие 3"
        );
        dormitoryComboBox.setItems(dormitories);

        // Добавление тестовых данных
        ObservableList<Room> rooms = FXCollections.observableArrayList(
            new Room("101", 1, 3, "Стандарт", 2),
            new Room("102", 1, 2, "Улучшенная", 1),
            new Room("201", 2, 3, "Стандарт", 0)
        );
        roomsTable.setItems(rooms);
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        showAlert("Информация", "Функция добавления комнаты в разработке", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        showAlert("Информация", "Функция изменения комнаты в разработке", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        showAlert("Информация", "Функция удаления комнаты в разработке", Alert.AlertType.INFORMATION);
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

    // Вспомогательный класс для представления комнаты
    public static class Room {
        private String roomNumber;
        private int floor;
        private int capacity;
        private String type;
        private int occupied;

        public Room(String roomNumber, int floor, int capacity, String type, int occupied) {
            this.roomNumber = roomNumber;
            this.floor = floor;
            this.capacity = capacity;
            this.type = type;
            this.occupied = occupied;
        }

        // Геттеры
        public String getRoomNumber() { return roomNumber; }
        public int getFloor() { return floor; }
        public int getCapacity() { return capacity; }
        public String getType() { return type; }
        public int getOccupied() { return occupied; }

        // Сеттеры
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
        public void setFloor(int floor) { this.floor = floor; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
        public void setType(String type) { this.type = type; }
        public void setOccupied(int occupied) { this.occupied = occupied; }
    }
} 
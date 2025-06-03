package org.example.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

public class CommandantDashboardController {

    @FXML
    private void handleApplications(ActionEvent event) {
        loadView(event, "application-management.fxml", "Управление заявлениями");
    }

    @FXML
    private void handleRooms(ActionEvent event) {
        loadView(event, "room-management.fxml", "Управление комнатами");
    }

    @FXML
    private void handleStudents(ActionEvent event) {
        loadView(event, "student-management.fxml", "Управление студентами");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadView(event, "main-view.fxml", "Вход в систему");
    }

    private void loadView(ActionEvent event, String fxmlFile, String title) {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Для отладки
            showAlert("Ошибка", "Не удалось загрузить страницу: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
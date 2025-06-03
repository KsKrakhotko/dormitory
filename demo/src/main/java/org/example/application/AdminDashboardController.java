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

public class AdminDashboardController {

    @FXML
    private void handleUserManagement(ActionEvent event) {
        loadView(event, "admin-view.fxml", "Управление пользователями");
    }

    @FXML
    private void handleDormitoryManagement(ActionEvent event) {
        loadView(event, "dormitory-management.fxml", "Управление общежитиями");
    }

    @FXML
    private void handleApplications(ActionEvent event) {
        loadView(event, "application-management.fxml", "Управление заявлениями");
    }

    @FXML
    private void handleReports(ActionEvent event) {
        loadView(event, "reports-view.fxml", "Отчеты");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loadView(event, "main-view.fxml", "Вход в систему");
    }

    private void loadView(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
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
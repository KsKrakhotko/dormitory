package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.application.MainApp.*;

public class MainController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Введите логин и пароль!");
            return;
        }

        try {
            UserDTO userDTO = new UserDTO(login, password);
            Request request = new Request(CommandType.AUTHORIZATION, userDTO);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                UserDTO authorizedUser = (UserDTO) response.getData();
                setCurrentUserId(authorizedUser.getId());

                switch (authorizedUser.getRole()) {
                    case STUDENT:
                        loadStudentDashboard();
                        break;
                    case COMMANDANT:
                        loadCommandantDashboard();
                        break;
                    case ADMIN:
                        loadAdminDashboard();
                        break;
                }
            } else {
                messageLabel.setText("Неверный логин или пароль!");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при авторизации: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleStudentRegistration() {
        try {
            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("student-registration.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Для отладки
            showAlert("Ошибка", "Не удалось открыть форму регистрации: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadStudentDashboard() {
        loadDashboard("student-dashboard.fxml", "Личный кабинет студента");
    }

    private void loadCommandantDashboard() {
        loadDashboard("commandant-dashboard.fxml", "Панель управления коменданта");
    }

    private void loadAdminDashboard() {
        loadDashboard("admin-dashboard.fxml", "Панель администратора");
    }

    private void loadDashboard(String fxmlFile, String title) {
        try {
            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Set user ID in the controller if it's a StudentDashboardController
            Object controller = loader.getController();
            if (controller instanceof StudentDashboardController) {
                ((StudentDashboardController) controller).initData(getCurrentUserId());
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить панель управления: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
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
package org.example.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.example.application.MainApp.*;

public class MainAppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button signInButton;

    @FXML
    void initialize() {
        signInButton.setOnAction(event -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText();
            if (login.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Введите логин и пароль!");
                return;
            }
            UserDTO dto = new UserDTO(login, password);
            Request request = new Request(CommandType.AUTHORIZATION, dto);
            Response response;

            try {
                getOut().writeObject(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                response = (Response) getIn().readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (response.getState() == 1) {
                UserDTO authorizedUser = (UserDTO) response.getData();
                MainApp.setCurrentUserId(authorizedUser.getId());
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
        });
    }

    @FXML
    private void handleStudentRegistration() {
        try {
            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student-registration.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось открыть форму регистрации", Alert.AlertType.ERROR);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить панель управления", Alert.AlertType.ERROR);
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


package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class AdminAddController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> accessComboBox;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        roleComboBox.setValue("STUDENT");
        accessComboBox.setValue("true");
    }

    @FXML
    private void handleCreateUser() {
        String login = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();
        String access = accessComboBox.getValue();

        if (login == null || login.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            selectedRole == null || access == null) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля", Alert.AlertType.ERROR);
            return;
        }

        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setLogin(login);
            userDTO.setPassword(password);
            userDTO.setRole(Role.valueOf(selectedRole));
            userDTO.setAccess(access);

            Request request = new Request(CommandType.ADD_USER, userDTO);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response != null && response.getState() == 1) {
                showAlert("Успех", "Пользователь успешно добавлен", Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                String errorMessage = response != null && response.getData() != null ? 
                    response.getData().toString() : "Не удалось добавить пользователя";
                showAlert("Ошибка", errorMessage, Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка при добавлении пользователя: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Меню администратора");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню администратора", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue("STUDENT");
        accessComboBox.setValue("true");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
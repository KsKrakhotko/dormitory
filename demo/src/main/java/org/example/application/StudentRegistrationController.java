package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.StudentDTO;
import dto.UserDTO;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class StudentRegistrationController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField facultyField;
    @FXML
    private TextField groupNumberField;
    @FXML
    private ComboBox<Integer> courseComboBox;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // Заполняем комбобокс курсов
        courseComboBox.getItems().addAll(1, 2, 3, 4, 5, 6);
        courseComboBox.setValue(1);
    }

    @FXML
    private void handleRegistration() {
        try {
            if (!validateFields()) {
                return;
            }

            // Создаем пользователя
            UserDTO userDTO = new UserDTO(
                loginField.getText(),
                passwordField.getText(),
                "1",  // доступ разрешен
                Role.STUDENT
            );

            System.out.println("Registering user: " + userDTO.getLogin());
            // Регистрируем пользователя
            Request userRequest = new Request(CommandType.REGISTRATION, userDTO);
            getOut().writeObject(userRequest);
            Response userResponse = (Response) getIn().readObject();

            if (userResponse.getState() == 1) {  // Успешная регистрация пользователя
                UserDTO createdUser = (UserDTO) userResponse.getData();
                Long userId = createdUser.getId();
                System.out.println("User registered successfully with ID: " + userId);

                // Создаем студента
                StudentDTO studentDTO = new StudentDTO(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    middleNameField.getText(),
                    birthDatePicker.getValue(),
                    facultyField.getText(),
                    groupNumberField.getText(),
                    courseComboBox.getValue(),
                    userId,
                    null  // roomId пока null
                );

                System.out.println("Creating student for user ID: " + userId);
                Request studentRequest = new Request(CommandType.ADD_STUDENT, studentDTO);
                getOut().writeObject(studentRequest);
                Response studentResponse = (Response) getIn().readObject();

                if (studentResponse.getState() == 1) {
                    System.out.println("Student created successfully");
                    showAlert("Успех", "Регистрация успешно завершена", Alert.AlertType.INFORMATION);
                    loadLoginScreen();
                } else {
                    System.err.println("Failed to create student: " + studentResponse.getData());
                    showAlert("Ошибка", "Не удалось создать профиль студента: " + studentResponse.getData(), Alert.AlertType.ERROR);
                    // Удаляем созданного пользователя, так как не удалось создать студента
                    System.out.println("Deleting user due to student creation failure");
                    Request deleteRequest = new Request(CommandType.DELETE_USER, userDTO.getLogin());
                    getOut().writeObject(deleteRequest);
                    getIn().readObject(); // Читаем ответ, даже если не используем его
                }
            } else {
                System.err.println("User registration failed: " + userResponse.getData());
                showAlert("Ошибка", "Пользователь с таким логином уже существует", Alert.AlertType.ERROR);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка при регистрации. Пожалуйста, попробуйте еще раз позже.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        String errorMessage = null;
        
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty() ||
            confirmPasswordField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
            firstNameField.getText().isEmpty() || facultyField.getText().isEmpty() ||
            groupNumberField.getText().isEmpty() || birthDatePicker.getValue() == null) {
            errorMessage = "Заполните все обязательные поля!";
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorMessage = "Пароли не совпадают!";
        } else if (birthDatePicker.getValue().isAfter(LocalDate.now().minusYears(16))) {
            errorMessage = "Возраст должен быть не менее 16 лет!";
        }

        if (errorMessage != null) {
            messageLabel.setText(errorMessage);
            showAlert("Ошибка валидации", errorMessage, Alert.AlertType.ERROR);
            return false;
        }

        messageLabel.setText("");
        return true;
    }

    @FXML
    private void handleCancel() {
        loadLoginScreen();
    }

    private void loadLoginScreen() {
        try {
            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить экран входа", Alert.AlertType.ERROR);
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
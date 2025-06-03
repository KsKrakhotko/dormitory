package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class AdminEditController {

    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, String> loginColumn;

    @FXML
    private TableColumn<UserDTO, String> passwordColumn;

    @FXML
    private TableColumn<UserDTO, String> roleColumn;

    @FXML
    private TableColumn<UserDTO, String> accessColumn;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> accessComboBox;

    @FXML
    private Button updateButton;

    private final ObservableList<UserDTO> userData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupComboBoxes();
        loadUsers();
        setupSelectionListener();
    }

    private void setupTable() {
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        accessColumn.setCellValueFactory(new PropertyValueFactory<>("access"));
        userTable.setItems(userData);
    }

    private void setupComboBoxes() {
        roleComboBox.setItems(FXCollections.observableArrayList(
                Role.ADMIN.toString(),
                Role.COMMANDANT.toString(),
                Role.STUDENT.toString()
        ));
        accessComboBox.setItems(FXCollections.observableArrayList(
                "true",
                "false"
        ));
    }

    private void setupSelectionListener() {
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loginField.setText(newSelection.getLogin());
                passwordField.setText(newSelection.getPassword());
                roleComboBox.setValue(newSelection.getRole().toString());
                accessComboBox.setValue(newSelection.getAccess());
            }
        });
    }

    private void loadUsers() {
        try {
            Request request = new Request(CommandType.FULL_LIST_USERS, new UserDTO());
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response != null && response.getState() == 1) {
                Object data = response.getData();
                if (data instanceof List<?>) {
                    List<UserDTO> users = ((List<?>) data).stream()
                            .filter(obj -> obj instanceof UserDTO)
                            .map(obj -> (UserDTO) obj)
                            .collect(Collectors.toList());
                    userData.setAll(users);
                }
            } else {
                showAlert("Ошибка", "Не удалось загрузить список пользователей");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при получении данных с сервера");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Ошибка", "Выберите пользователя для обновления");
            return;
        }

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(selectedUser.getId());
        updatedUser.setLogin(loginField.getText());
        updatedUser.setPassword(passwordField.getText());
        updatedUser.setRole(Role.valueOf(roleComboBox.getValue()));
        updatedUser.setAccess(accessComboBox.getValue());

        try {
            Request request = new Request(CommandType.UPDATE_USER, updatedUser);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response != null && response.getState() == 1) {
                showAlertInfo("Успех", "Пользователь успешно обновлен");
                loadUsers(); // Перезагружаем список пользователей
            } else {
                showAlert("Ошибка", "Не удалось обновить пользователя");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при обновлении пользователя");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) userTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Меню администратора");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню администратора");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

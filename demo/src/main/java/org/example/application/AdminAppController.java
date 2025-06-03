package org.example.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminAppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonPrintAll;

    @FXML
    private Button buttonBack;

    @FXML
    void initialize() {
        assert buttonAdd != null : "fx:id=\"buttonAdd\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonDelete != null : "fx:id=\"buttonDelete\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonEdit != null : "fx:id=\"buttonEdit\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonExit != null : "fx:id=\"buttonExit\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonPrintAll != null : "fx:id=\"buttonPrintAll\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonBack != null : "fx:id=\"buttonBack\" was not injected: check your FXML file 'admin-view.fxml'.";
    }

    @FXML
    private void loadMainMenuAdmin() {
        try {
            Stage stage = (Stage) buttonExit.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Главное меню");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить главное меню");
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

    @FXML
    private void loadAdminPrint() {
        loadView("admin-print-view.fxml", "Список пользователей");
    }

    @FXML
    private void loadAdminAdd() {
        loadView("admin-add-view.fxml", "Добавление пользователя");
    }

    @FXML
    private void loadAdminDelete() {
        loadView("admin-delete-view.fxml", "Удаление пользователя");
    }

    @FXML
    private void loadAdminEdit() {
        loadView("admin-edit-view.fxml", "Редактирование пользователя");
    }

    @FXML
    private void handleBack() {
        loadView("admin-dashboard.fxml", "Панель администратора");
    }

    private void loadView(String fxmlFile, String title) {
        try {
            Stage stage = (Stage) buttonBack.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить страницу: " + e.getMessage());
        }
    }
}

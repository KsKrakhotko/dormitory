package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.DormitoryDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class DormitoryManagementController {
    @FXML
    private TableView<DormitoryDTO> dormitoriesTable;
    @FXML
    private TableColumn<DormitoryDTO, Long> idColumn;
    @FXML
    private TableColumn<DormitoryDTO, Integer> numberColumn;
    @FXML
    private TableColumn<DormitoryDTO, String> nameColumn;
    @FXML
    private TableColumn<DormitoryDTO, String> addressColumn;
    @FXML
    private TableColumn<DormitoryDTO, Integer> totalCapacityColumn;
    @FXML
    private TableColumn<DormitoryDTO, Integer> availablePlacesColumn;

    @FXML
    private TextField numberField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField capacityField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private DormitoryDTO selectedDormitory;

    @FXML
    public void initialize() {
        initializeTable();
        loadDormitories();
        setupSelectionListener();
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        totalCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("totalCapacity"));
        availablePlacesColumn.setCellValueFactory(new PropertyValueFactory<>("availablePlaces"));
    }

    private void setupSelectionListener() {
        dormitoriesTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedDormitory = newValue;
                if (newValue != null) {
                    numberField.setText(String.valueOf(newValue.getNumber()));
                    nameField.setText(newValue.getName());
                    addressField.setText(newValue.getAddress());
                    capacityField.setText(String.valueOf(newValue.getTotalCapacity()));
                    updateButton.setDisable(false);
                    deleteButton.setDisable(false);
                } else {
                    clearFields();
                    updateButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
            }
        );
    }

    private void loadDormitories() {
        try {
            Request request = new Request(CommandType.LIST_DORMITORIES, null);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                List<DormitoryDTO> dormitories = (List<DormitoryDTO>) response.getData();
                dormitoriesTable.getItems().setAll(dormitories);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось загрузить список общежитий: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) {
            return;
        }

        try {
            DormitoryDTO newDormitory = new DormitoryDTO();
            newDormitory.setNumber(Integer.parseInt(numberField.getText()));
            newDormitory.setName(nameField.getText().trim());
            newDormitory.setAddress(addressField.getText());
            newDormitory.setTotalCapacity(Integer.parseInt(capacityField.getText()));

            Request request = new Request(CommandType.ADD_DORMITORY, newDormitory);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                showAlert("Успех", "Общежитие успешно добавлено", Alert.AlertType.INFORMATION);
                loadDormitories();
                clearFields();
            } else {
                showAlert("Ошибка", "Не удалось добавить общежитие: " + response.getData(), Alert.AlertType.ERROR);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при добавлении общежития: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        if (!validateFields() || selectedDormitory == null) {
            return;
        }

        try {
            DormitoryDTO updatedDormitory = new DormitoryDTO();
            updatedDormitory.setId(selectedDormitory.getId());
            updatedDormitory.setNumber(Integer.parseInt(numberField.getText()));
            updatedDormitory.setName(nameField.getText().trim());
            updatedDormitory.setAddress(addressField.getText());
            updatedDormitory.setTotalCapacity(Integer.parseInt(capacityField.getText()));

            Request request = new Request(CommandType.UPDATE_DORMITORY, updatedDormitory);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                showAlert("Успех", "Общежитие успешно обновлено", Alert.AlertType.INFORMATION);
                loadDormitories();
                clearFields();
            } else {
                showAlert("Ошибка", "Не удалось обновить общежитие: " + response.getData(), Alert.AlertType.ERROR);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при обновлении общежития: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedDormitory == null) {
            showAlert("Внимание", "Выберите общежитие для удаления", Alert.AlertType.WARNING);
            return;
        }

        try {
            Request request = new Request(CommandType.DELETE_DORMITORY, selectedDormitory.getId());
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                showAlert("Успех", "Общежитие успешно удалено", Alert.AlertType.INFORMATION);
                loadDormitories();
                clearFields();
            } else {
                showAlert("Ошибка", "Не удалось удалить общежитие: " + response.getData(), Alert.AlertType.ERROR);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при удалении общежития: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) dormitoriesTable.getScene().getWindow();
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("admin-dashboard.fxml")).load()));
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось вернуться на главную страницу", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (numberField.getText().trim().isEmpty() || 
            nameField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            capacityField.getText().trim().isEmpty()) {
            showAlert("Внимание", "Все поля должны быть заполнены", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(numberField.getText());
            Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e) {
            showAlert("Внимание", "Номер и вместимость должны быть числами", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void clearFields() {
        numberField.clear();
        nameField.clear();
        addressField.clear();
        capacityField.clear();
        selectedDormitory = null;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
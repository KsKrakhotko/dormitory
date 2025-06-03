package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.ApplicationDTO;
import dto.DormitoryDTO;
import enums.ApplicationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class ApplicationFormController {
    @FXML
    private ComboBox<DormitoryDTO> dormitoryComboBox;
    @FXML
    private ComboBox<String> roomTypeComboBox;
    @FXML
    private TextArea commentArea;
    @FXML
    private Label messageLabel;

    private Long studentId;

    @FXML
    public void initialize() {
        loadDormitories();
        initializeRoomTypes();
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    private void loadDormitories() {
        try {
            Request request = new Request(CommandType.LIST_DORMITORIES, null);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                List<DormitoryDTO> dormitories = (List<DormitoryDTO>) response.getData();
                dormitoryComboBox.getItems().setAll(dormitories);
                
                dormitoryComboBox.setConverter(new StringConverter<DormitoryDTO>() {
                    @Override
                    public String toString(DormitoryDTO dormitory) {
                        return dormitory == null ? "" : "Общежитие №" + dormitory.getNumber();
                    }

                    @Override
                    public DormitoryDTO fromString(String string) {
                        return null; // Not needed for ComboBox
                    }
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось загрузить список общежитий", Alert.AlertType.ERROR);
        }
    }

    private void initializeRoomTypes() {
        roomTypeComboBox.getItems().addAll(
            "Двухместная",
            "Трехместная",
            "Четырехместная"
        );
    }

    @FXML
    private void handleSubmit() {
        if (!validateForm()) {
            return;
        }

        try {
            ApplicationDTO application = new ApplicationDTO();
            application.setStudentId(studentId);
            application.setDormitoryId(dormitoryComboBox.getValue().getId());
            application.setPreferredRoomType(roomTypeComboBox.getValue());
            application.setComment(commentArea.getText().trim());
            application.setApplicationDate(LocalDateTime.now());
            application.setStatus(ApplicationStatus.PENDING);

            Request request = new Request(CommandType.SUBMIT_APPLICATION, application);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                showAlert("Успех", "Заявление успешно подано", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                messageLabel.setText("Ошибка при подаче заявления: " + response.getData());
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось подать заявление: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateForm() {
        if (dormitoryComboBox.getValue() == null) {
            messageLabel.setText("Выберите общежитие");
            return false;
        }
        if (roomTypeComboBox.getValue() == null) {
            messageLabel.setText("Выберите тип комнаты");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) dormitoryComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
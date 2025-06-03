package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.ApplicationDTO;
import dto.DormitoryDTO;
import enums.ApplicationStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class ApplicationManagementController {
    @FXML
    private TableView<ApplicationDTO> applicationsTable;
    @FXML
    private TableColumn<ApplicationDTO, Long> idColumn;
    @FXML
    private TableColumn<ApplicationDTO, String> studentColumn;
    @FXML
    private TableColumn<ApplicationDTO, String> dormitoryColumn;
    @FXML
    private TableColumn<ApplicationDTO, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<ApplicationDTO, String> roomTypeColumn;
    @FXML
    private TableColumn<ApplicationDTO, ApplicationStatus> statusColumn;
    @FXML
    private TextArea commentArea;

    @FXML
    public void initialize() {
        initializeTable();
        loadApplications();
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        dormitoryColumn.setCellValueFactory(new PropertyValueFactory<>("dormitoryId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRoomType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Форматирование даты
        dateColumn.setCellFactory(column -> new TableCell<ApplicationDTO, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Форматирование статуса
        statusColumn.setCellFactory(column -> new TableCell<ApplicationDTO, ApplicationStatus>() {
            @Override
            protected void updateItem(ApplicationStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    switch (item) {
                        case PENDING:
                            setText("На рассмотрении");
                            break;
                        case APPROVED:
                            setText("Одобрено");
                            break;
                        case REJECTED:
                            setText("Отклонено");
                            break;
                        case CANCELLED:
                            setText("Отменено");
                            break;
                    }
                }
            }
        });

        // Обработчик выбора заявления
        applicationsTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    commentArea.setText(newValue.getComment());
                } else {
                    commentArea.clear();
                }
            }
        );
    }

    private void loadApplications() {
        try {
            Request request = new Request(CommandType.LIST_ALL_APPLICATIONS, null);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                List<ApplicationDTO> applications = (List<ApplicationDTO>) response.getData();
                applicationsTable.getItems().setAll(applications);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось загрузить список заявлений: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleApprove() {
        processApplication(ApplicationStatus.APPROVED);
    }

    @FXML
    private void handleReject() {
        processApplication(ApplicationStatus.REJECTED);
    }

    private void processApplication(ApplicationStatus newStatus) {
        ApplicationDTO selectedApplication = applicationsTable.getSelectionModel().getSelectedItem();
        if (selectedApplication == null) {
            showAlert("Внимание", "Выберите заявление для обработки", Alert.AlertType.WARNING);
            return;
        }

        try {
            Request request = new Request(
                CommandType.PROCESS_APPLICATION,
                new Object[]{selectedApplication.getId(), newStatus, commentArea.getText(), MainApp.getCurrentUserId()}
            );
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                showAlert("Успех", "Заявление успешно обработано", Alert.AlertType.INFORMATION);
                loadApplications();
            } else {
                showAlert("Ошибка", "Не удалось обработать заявление: " + response.getData(), Alert.AlertType.ERROR);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при обработке заявления: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) applicationsTable.getScene().getWindow();
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("commandant-dashboard.fxml")).load()));
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось вернуться на главную страницу", Alert.AlertType.ERROR);
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
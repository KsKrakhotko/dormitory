package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.ApplicationDTO;
import dto.RoomDTO;
import dto.StudentDTO;
import enums.ApplicationStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.example.application.MainApp.*;

public class StudentDashboardController {
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label facultyLabel;
    @FXML
    private Label groupLabel;
    @FXML
    private Label courseLabel;

    @FXML
    private TableView<ApplicationDTO> applicationsTable;
    @FXML
    private TableColumn<ApplicationDTO, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<ApplicationDTO, String> dormitoryColumn;
    @FXML
    private TableColumn<ApplicationDTO, String> roomTypeColumn;
    @FXML
    private TableColumn<ApplicationDTO, ApplicationStatus> statusColumn;
    @FXML
    private TableColumn<ApplicationDTO, String> commentColumn;
    @FXML
    private Button createApplicationButton;

    @FXML
    private TitledPane roomInfoPane;
    @FXML
    private Label dormitoryNumberLabel;
    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label floorLabel;
    @FXML
    private Label roomTypeLabel;

    private StudentDTO currentStudent;
    private Long userId;

    public void initData(Long userId) {
        this.userId = userId;
        if (userId != null) {
            if (loadStudentInfo()) {
                loadApplications();
                loadRoomInfo();
            }
        }
    }

    @FXML
    public void initialize() {
        initializeTable();
    }

    private boolean loadStudentInfo() {
        try {
            if (userId == null) {
                showAlert("Ошибка", "Не удалось получить ID пользователя", Alert.AlertType.ERROR);
                return false;
            }

            Request request = new Request(CommandType.GET_STUDENT_BY_USER, userId);
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                currentStudent = (StudentDTO) response.getData();
                updateStudentInfo();
                return true;
            } else {
                showAlert("Ошибка", "Не удалось загрузить информацию о студенте: " + response.getData(), Alert.AlertType.ERROR);
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось загрузить информацию о студенте: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
    }

    private void updateStudentInfo() {
        fullNameLabel.setText(String.format("%s %s %s", 
            currentStudent.getLastName(),
            currentStudent.getFirstName(),
            currentStudent.getMiddleName() != null ? currentStudent.getMiddleName() : ""));
        facultyLabel.setText(currentStudent.getFaculty());
        groupLabel.setText(currentStudent.getGroupNumber());
        courseLabel.setText(String.valueOf(currentStudent.getCourse()));
    }

    private void initializeTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        dormitoryColumn.setCellValueFactory(new PropertyValueFactory<>("dormitoryId"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("preferredRoomType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

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
    }

    private void loadApplications() {
        try {
            if (currentStudent == null) {
                return;
            }
            Request request = new Request(CommandType.LIST_STUDENT_APPLICATIONS, currentStudent.getUserId());
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();

            if (response.getState() == 1) {
                List<ApplicationDTO> applications = (List<ApplicationDTO>) response.getData();
                applicationsTable.getItems().setAll(applications);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось загрузить список заявлений: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadRoomInfo() {
            try {
            if (currentStudent == null || currentStudent.getRoomId() == null) {
                roomInfoPane.setVisible(false);
                return;
            }
                Request request = new Request(CommandType.GET_ROOM, currentStudent.getRoomId());
                getOut().writeObject(request);
                Response response = (Response) getIn().readObject();

                if (response.getState() == 1) {
                    RoomDTO room = (RoomDTO) response.getData();
                    updateRoomInfo(room);
                } else {
                    roomInfoPane.setVisible(false);
                }
            } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Не удалось загрузить информацию о комнате: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            roomInfoPane.setVisible(false);
        }
    }

    private void updateRoomInfo(RoomDTO room) {
        if (room != null) {
            dormitoryNumberLabel.setText(String.valueOf(room.getDormitoryNumber()));
        roomNumberLabel.setText(room.getRoomNumber());
            floorLabel.setText(String.valueOf(room.getFloor()));
        roomTypeLabel.setText(room.getCapacity() + "-местная");
            roomInfoPane.setVisible(true);
        } else {
            roomInfoPane.setVisible(false);
        }
    }

    @FXML
    private void handleCreateApplication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("application-form.fxml"));
            Parent root = loader.load();
            
            ApplicationFormController controller = loader.getController();
            controller.setStudentId(currentStudent.getUserId());

            Stage stage = new Stage();
            stage.setTitle("Подача заявления");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(applicationsTable.getScene().getWindow());
            
            // Обновляем список заявлений после закрытия формы
            stage.setOnHidden(event -> loadApplications());

            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось открыть форму подачи заявления: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleViewApplications() {
        loadApplications();
    }

    @FXML
    private void handleRoomInfo() {
        loadRoomInfo();
    }

    @FXML
    private void handleLogout() {
        try {
            // Reset the current user ID
            MainApp.setCurrentUserId(null);
            
            // Load the login screen
            Stage stage = (Stage) fullNameLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Вход в систему");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось выйти из системы: " + e.getMessage(), Alert.AlertType.ERROR);
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
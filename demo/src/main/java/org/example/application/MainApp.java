package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import enums.Role;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//import service_impl.*;

public class MainApp extends Application {
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    public static String username;
    private static Long currentUserId;

    @Override
    public void start(Stage stage) throws IOException {
        //

//        String name = "admin";
//        String password = "12345";
//        String access = "1";
//        Role roleInSQL=Role.STUDENT;
//        UserDTO userDTO = new UserDTO(name, password, access, roleInSQL);
//        Request request = new Request(CommandType.ADD_USER, userDTO);
//        getOut().writeObject(request);
        //

        connectToServer("localhost", 12345);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Управление общежитиями");
        stage.setScene(scene);
        stage.show();
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу: " + e.getMessage());
            showErrorAlert("Не удалось подключиться к серверу");
            System.exit(1);
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static ObjectOutputStream getOut() {
        return out;
    }

    public static ObjectInputStream getIn() {
        return in;
    }

    public static Long getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(Long userId) {
        currentUserId = userId;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showErrorAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
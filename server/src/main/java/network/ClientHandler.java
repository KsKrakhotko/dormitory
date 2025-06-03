package network;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.ApplicationDTO;
import dto.DormitoryDTO;
import dto.StudentDTO;
import dto.UserDTO;
import enums.ApplicationStatus;
import service_impl.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final AuthorizationServiceImpl authorizationService;
    private final RegistrationServiceImpl registrationService;
    private final StudentServiceImpl studentService;
    private final DormitoryServiceImpl dormitoryService;
    private final ApplicationServiceImpl applicationService;
    private volatile boolean running = true;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.authorizationService = new AuthorizationServiceImpl();
        this.registrationService = new RegistrationServiceImpl();
        this.studentService = new StudentServiceImpl();
        this.dormitoryService = new DormitoryServiceImpl();
        this.applicationService = new ApplicationServiceImpl();
    }

    @Override
    public void run() {
        try {
            while (running) {
                try {
                    Request request = (Request) in.readObject();
                    Response response;

                    switch (request.getCommandType()) {
                        case AUTHORIZATION:
                            UserDTO userDTO = (UserDTO) request.getData();
                            response = authorizationService.authorization(userDTO);
                            break;
                        case REGISTRATION:
                            userDTO = (UserDTO) request.getData();
                            response = registrationService.registration(userDTO);
                            break;
                        case GET_ID:
                            String username = (String) request.getData();
                            response = authorizationService.getId(username);
                            break;
                        case FULL_LIST_USERS:
                            response = authorizationService.getUsers();
                            break;
                        case DELETE_USER:
                            String login = (String) request.getData();
                            response = authorizationService.deleteUser(login);
                            break;
                        case UPDATE_USER:
                            userDTO = (UserDTO) request.getData();
                            response = authorizationService.updateUser(userDTO);
                            break;
                        case ADD_USER:
                            userDTO = (UserDTO) request.getData();
                            response = registrationService.registration(userDTO);
                            break;
                        case ADD_STUDENT:
                            StudentDTO studentDTO = (StudentDTO) request.getData();
                            response = studentService.addStudent(studentDTO);
                            break;
                        case GET_STUDENT_BY_USER:
                            Long userId = (Long) request.getData();
                            response = studentService.getStudentByUser(userId);
                            break;
                        // Dormitory management commands
                        case ADD_DORMITORY:
                            DormitoryDTO dormitoryDTO = (DormitoryDTO) request.getData();
                            response = dormitoryService.addDormitory(dormitoryDTO);
                            break;
                        case UPDATE_DORMITORY:
                            dormitoryDTO = (DormitoryDTO) request.getData();
                            response = dormitoryService.updateDormitory(dormitoryDTO);
                            break;
                        case DELETE_DORMITORY:
                            Long dormitoryId = (Long) request.getData();
                            response = dormitoryService.deleteDormitory(dormitoryId);
                            break;
                        case GET_DORMITORY:
                            dormitoryId = (Long) request.getData();
                            response = dormitoryService.getDormitory(dormitoryId);
                            break;
                        case LIST_DORMITORIES:
                            response = dormitoryService.getAllDormitories();
                            break;
                        // Application management commands
                        case SUBMIT_APPLICATION:
                            ApplicationDTO applicationDTO = (ApplicationDTO) request.getData();
                            response = applicationService.submitApplication(applicationDTO);
                            break;
                        case PROCESS_APPLICATION:
                            Object[] processData = (Object[]) request.getData();
                            response = applicationService.processApplication(
                                (Long) processData[0],
                                (ApplicationStatus) processData[1],
                                (String) processData[2],
                                (Long) processData[3]
                            );
                            break;
                        case CANCEL_APPLICATION:
                            Long applicationId = (Long) request.getData();
                            response = applicationService.cancelApplication(applicationId);
                            break;
                        case GET_APPLICATION:
                            applicationId = (Long) request.getData();
                            response = applicationService.getApplication(applicationId);
                            break;
                        case LIST_STUDENT_APPLICATIONS:
                            Long studentId = (Long) request.getData();
                            response = applicationService.listStudentApplications(studentId);
                            break;
                        case LIST_DORMITORY_APPLICATIONS:
                            Long dormId = (Long) request.getData();
                            response = applicationService.listDormitoryApplications(dormId);
                            break;
                        case LIST_ALL_APPLICATIONS:
                            response = applicationService.listAllApplications();
                            break;
                        default:
                            response = new Response(-1, "Неизвестная команда: " + request.getCommandType());
                    }

                    synchronized (out) {
                        out.writeObject(response);
                        out.flush();
                        out.reset(); // Clear the cache after each write
                    }
                } catch (SocketException e) {
                    System.err.println("Connection reset with client " + clientSocket.getInetAddress());
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error handling client request: " + e.getMessage());
                    break;
                }
            }
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        running = false;
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
    }
}
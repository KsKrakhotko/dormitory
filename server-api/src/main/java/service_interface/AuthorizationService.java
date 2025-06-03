package service_interface;

import connect.Response;
import dto.UserDTO;

public interface AuthorizationService {
    Response authorization(UserDTO dto);
    Response getId(String username);
    Response getUsers();
    Response deleteUser(String login);
    Response updateUser(UserDTO userDTO);
} 
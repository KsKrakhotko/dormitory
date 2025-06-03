package service_impl;

import connect.Response;
import dto.UserDTO;
import entity.User;
import service_interface.AuthorizationService;
import workWithHibernate.UserHibernate;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationServiceImpl implements AuthorizationService {
    @Override
    public Response authorization(UserDTO dto) {
        User user = UserHibernate.findByUsername(dto.getLogin());
        if (user != null && user.getPassword().equals(dto.getPassword())) {
            UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getAccess(),
                user.getRole()
            );
            return new Response(1, userDTO);
        }
        return new Response(0, "Неверный логин или пароль");
    }

    @Override
    public Response getId(String username) {
        User user = UserHibernate.findByUsername(username);
        if (user != null) {
            return new Response(1, user.getId());
        }
        return new Response(0, "Пользователь не найден");
    }

    @Override
    public Response getUsers() {
        try {
            List<User> users = UserHibernate.findAllUsers();
            List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(
                    user.getId(),
                    user.getLogin(),
                    user.getPassword(),
                    user.getAccess(),
                    user.getRole()
                ))
                .collect(Collectors.toList());
            return new Response(1, userDTOs);
        } catch (Exception e) {
            return new Response(0, "Ошибка при получении списка пользователей: " + e.getMessage());
        }
    }

    @Override
    public Response deleteUser(String login) {
        try {
            User user = UserHibernate.findByUsername(login);
            if (user == null) {
                return new Response(0, "Пользователь не найден");
            }
            
            // Check if user has associated student records
            if (UserHibernate.hasStudentRecords(user.getId())) {
                return new Response(0, "Невозможно удалить пользователя, так как с ним связаны записи студента");
            }
            
            boolean deleted = UserHibernate.deleteUserByName(login);
            return new Response(deleted ? 1 : 0, deleted ? "Пользователь успешно удален" : "Ошибка при удалении пользователя");
        } catch (Exception e) {
            return new Response(0, "Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    @Override
    public Response updateUser(UserDTO userDTO) {
        try {
            User existingUser = UserHibernate.findById(userDTO.getId());
            if (existingUser == null) {
                return new Response(0, "Пользователь не найден");
            }

            // Check if username is being changed and if it's already taken
            if (!existingUser.getLogin().equals(userDTO.getLogin()) && 
                UserHibernate.findByUsername(userDTO.getLogin()) != null) {
                return new Response(0, "Пользователь с таким логином уже существует");
            }

            existingUser.setLogin(userDTO.getLogin());
            existingUser.setPassword(userDTO.getPassword());
            existingUser.setRole(userDTO.getRole());
            existingUser.setAccess(userDTO.getAccess());

            UserHibernate.updateUser(existingUser);
            return new Response(1, "Пользователь успешно обновлен");
        } catch (Exception e) {
            return new Response(0, "Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }
}


package service_impl;

import connect.Response;
import dto.UserDTO;
import entity.User;
import service_interface.RegistrationService;
import workWithHibernate.UserHibernate;

import java.util.Random;

public class RegistrationServiceImpl implements RegistrationService {

    @Override
    public Response registration(UserDTO dto) {
        if (UserHibernate.findByUsername(dto.getLogin()) != null) {
            return new Response(0, "Пользователь с таким логином уже существует");
        }

        try {
            User newUser = new User();
            newUser.setLogin(dto.getLogin());
            newUser.setPassword(dto.getPassword());
            newUser.setAccess(dto.getAccess());
            newUser.setRole(dto.getRole());

            UserHibernate.addUser(newUser);
            
            // Создаем DTO для ответа
            UserDTO responseDto = new UserDTO(
                newUser.getId(),
                newUser.getLogin(),
                newUser.getPassword(),
                newUser.getAccess(),
                newUser.getRole()
            );
            
            return new Response(1, responseDto);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при регистрации: " + e.getMessage());
        }
    }

    public static String hashPassword(String password, String salt) {
        int hash = 7;
        String saltInput = password + salt;
        for (int i = 0; i < saltInput.length(); i++) {
            hash = hash * 31 + saltInput.charAt(i);
        }
        return String.valueOf(hash);
    }

    private String generateSalt() {
        Random random = new Random();
        return String.valueOf(100_00 + random.nextInt(9_900_000));
    }
}
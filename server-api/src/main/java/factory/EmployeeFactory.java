package factory;

import dto.UserDTO;
import dto.StudentDTO;
import dto.EmployeeDTO;
import enums.Role;
import java.time.LocalDate;

public class EmployeeFactory extends UserFactory {
    @Override
    public UserDTO createUser(String login, String password, String access, Role role) {
        return new UserDTO(login, password, access, Role.COMMANDANT);
    }

    @Override
    public StudentDTO createStudent(String firstName, String lastName, String middleName) {
        throw new UnsupportedOperationException("Employee factory cannot create students");
    }

    @Override
    public EmployeeDTO createEmployee(String firstName, String lastName, String middleName) {
        return new EmployeeDTO(firstName, lastName, middleName, LocalDate.now(), 
                             "Комендант", LocalDate.now(), null, null, null);
    }
} 
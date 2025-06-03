package factory;

import dto.UserDTO;
import dto.StudentDTO;
import dto.EmployeeDTO;
import enums.Role;
import java.time.LocalDate;

public class StudentFactory extends UserFactory {
    @Override
    public UserDTO createUser(String login, String password, String access, Role role) {
        return new UserDTO(login, password, access, Role.STUDENT);
    }

    @Override
    public StudentDTO createStudent(String firstName, String lastName, String middleName) {
        return new StudentDTO(firstName, lastName, middleName, LocalDate.now(), null, null, 1, null, null);
    }

    @Override
    public EmployeeDTO createEmployee(String firstName, String lastName, String middleName) {
        throw new UnsupportedOperationException("Student factory cannot create employees");
    }
} 
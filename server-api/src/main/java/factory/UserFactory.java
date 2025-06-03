package factory;

import dto.UserDTO;
import dto.StudentDTO;
import dto.EmployeeDTO;
import enums.Role;

public abstract class UserFactory {
    public abstract UserDTO createUser(String login, String password, String access, Role role);
    public abstract StudentDTO createStudent(String firstName, String lastName, String middleName);
    public abstract EmployeeDTO createEmployee(String firstName, String lastName, String middleName);

    public static UserFactory getFactory(Role role) {
        switch (role) {
            case STUDENT:
                return new StudentFactory();
            case COMMANDANT:
                return new EmployeeFactory();
            case ADMIN:
                return new AdminFactory();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
} 
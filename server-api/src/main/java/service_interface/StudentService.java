package service_interface;

import connect.Response;
import dto.StudentDTO;

public interface StudentService {
    Response addStudent(StudentDTO studentDTO);
    Response updateStudent(Long id, StudentDTO studentDTO);
    Response getStudent(Long id);
    Response getStudentByUser(Long userId);
    Response listStudents();
    Response deleteStudent(Long id);
} 
package service_impl;

import connect.Response;
import dto.StudentDTO;
import entity.Student;
import entity.User;
import service_interface.StudentService;
import workWithHibernate.UserHibernate;
import workWithHibernate.StudentHibernate;

public class StudentServiceImpl implements StudentService {
    @Override
    public Response addStudent(StudentDTO studentDTO) {
        try {
            // Проверяем существование пользователя
            User user = UserHibernate.findById(studentDTO.getUserId());
            if (user == null) {
                return new Response(0, "Пользователь не найден");
            }

            // Создаем студента
            Student student = new Student(
                studentDTO.getFirstName(),
                studentDTO.getLastName(),
                studentDTO.getMiddleName(),
                studentDTO.getBirthDate(),
                studentDTO.getFaculty(),
                studentDTO.getGroupNumber(),
                studentDTO.getCourse()
            );
            student.setUser(user);

            // Сохраняем студента
            StudentHibernate.addStudent(student);
            return new Response(1, student);
        } catch (Exception e) {
            return new Response(0, "Ошибка при создании студента: " + e.getMessage());
        }
    }

    @Override
    public Response updateStudent(Long id, StudentDTO studentDTO) {
        try {
            Student student = StudentHibernate.findStudentById(id);
            if (student == null) {
                return new Response(0, "Студент не найден");
            }

            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());
            student.setMiddleName(studentDTO.getMiddleName());
            student.setBirthDate(studentDTO.getBirthDate());
            student.setFaculty(studentDTO.getFaculty());
            student.setGroupNumber(studentDTO.getGroupNumber());
            student.setCourse(studentDTO.getCourse());

            StudentHibernate.updateStudent(student);
            return new Response(1, student);
        } catch (Exception e) {
            return new Response(0, "Ошибка при обновлении данных студента: " + e.getMessage());
        }
    }

    @Override
    public Response getStudent(Long id) {
        try {
            Student student = StudentHibernate.findStudentById(id);
            if (student == null) {
                return new Response(0, "Студент не найден");
            }
            return new Response(1, student);
        } catch (Exception e) {
            return new Response(0, "Ошибка при получении данных студента: " + e.getMessage());
        }
    }

    @Override
    public Response getStudentByUser(Long userId) {
        try {
            System.out.println("Looking for student with userId: " + userId);
            Student student = StudentHibernate.findStudentByUser(userId);
            if (student == null) {
                System.out.println("Student not found for userId: " + userId);
                return new Response(0, "Студент не найден");
            }
            
            System.out.println("Found student: " + student.getFirstName() + " " + student.getLastName());
            StudentDTO studentDTO = new StudentDTO(
                student.getFirstName(),
                student.getLastName(),
                student.getMiddleName(),
                student.getBirthDate(),
                student.getFaculty(),
                student.getGroupNumber(),
                student.getCourse(),
                student.getUser().getId(),
                student.getRoom() != null ? student.getRoom().getId() : null
            );
            
            return new Response(1, studentDTO);
        } catch (Exception e) {
            System.err.println("Error getting student by userId " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return new Response(0, "Ошибка при получении данных студента: " + e.getMessage());
        }
    }

    @Override
    public Response listStudents() {
        try {
            return new Response(1, StudentHibernate.findAllStudents());
        } catch (Exception e) {
            return new Response(0, "Ошибка при получении списка студентов: " + e.getMessage());
        }
    }

    @Override
    public Response deleteStudent(Long id) {
        try {
            if (StudentHibernate.deleteStudent(id)) {
                return new Response(1, true);
            }
            return new Response(0, "Студент не найден");
        } catch (Exception e) {
            return new Response(0, "Ошибка при удалении студента: " + e.getMessage());
        }
    }
} 
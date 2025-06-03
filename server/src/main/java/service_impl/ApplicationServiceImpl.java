package service_impl;

import connect.Response;
import dto.ApplicationDTO;
import entity.Application;
import entity.Dormitory;
import entity.Student;
import entity.User;
import enums.ApplicationStatus;
import service_interface.ApplicationService;
import workWithHibernate.ApplicationHibernate;
import workWithHibernate.DormitoryHibernate;
import workWithHibernate.StudentHibernate;
import workWithHibernate.UserHibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public Response submitApplication(ApplicationDTO applicationDTO) {
        try {
            if (applicationDTO == null) {
                return new Response(-1, "Данные заявления не могут быть пустыми");
            }

            // Проверяем существование студента
            Student student = StudentHibernate.findStudentByUser(applicationDTO.getStudentId());
            if (student == null) {
                return new Response(-1, "Студент не найден");
            }

            // Проверяем существование общежития
            Dormitory dormitory = DormitoryHibernate.getDormitoryById(applicationDTO.getDormitoryId());
            if (dormitory == null) {
                return new Response(-1, "Общежитие не найдено");
            }

            // Создаем новое заявление
            Application application = new Application();
            application.setStudent(student);
            application.setDormitory(dormitory);
            application.setApplicationDate(LocalDateTime.now());
            application.setStatus(ApplicationStatus.PENDING);
            application.setComment(applicationDTO.getComment());
            application.setPreferredRoomType(applicationDTO.getPreferredRoomType());

            // Сохраняем заявление
            ApplicationHibernate.addApplication(application);

            return new Response(1, "Заявление успешно подано");
        } catch (Exception e) {
            return new Response(-1, "Ошибка при подаче заявления: " + e.getMessage());
        }
    }

    @Override
    public Response processApplication(Long id, ApplicationStatus newStatus, String comment, Long processedById) {
        try {
            Application application = ApplicationHibernate.getApplicationById(id);
            if (application == null) {
                return new Response(-1, "Заявление не найдено");
            }

            User processedBy = UserHibernate.getUserById(processedById);
            if (processedBy == null) {
                return new Response(-1, "Пользователь, обрабатывающий заявление, не найден");
            }

            application.setStatus(newStatus);
            application.setComment(comment);
            application.setProcessedBy(processedBy);
            application.setProcessedDate(LocalDateTime.now());

            ApplicationHibernate.updateApplication(application);
            return new Response(1, "Заявление успешно обработано");
        } catch (Exception e) {
            return new Response(-1, "Ошибка при обработке заявления: " + e.getMessage());
        }
    }

    @Override
    public Response cancelApplication(Long id) {
        try {
            Application application = ApplicationHibernate.getApplicationById(id);
            if (application == null) {
                return new Response(-1, "Заявление не найдено");
            }

            application.setStatus(ApplicationStatus.CANCELLED);
            ApplicationHibernate.updateApplication(application);
            return new Response(1, "Заявление успешно отменено");
        } catch (Exception e) {
            return new Response(-1, "Ошибка при отмене заявления: " + e.getMessage());
        }
    }

    @Override
    public Response getApplication(Long id) {
        try {
            Application application = ApplicationHibernate.getApplicationById(id);
            if (application == null) {
                return new Response(-1, "Заявление не найдено");
            }

            return new Response(1, convertToDTO(application));
        } catch (Exception e) {
            return new Response(-1, "Ошибка при получении заявления: " + e.getMessage());
        }
    }

    @Override
    public Response listStudentApplications(Long studentId) {
        try {
            List<Application> applications = ApplicationHibernate.findApplicationsByStudent(studentId);
            List<ApplicationDTO> dtos = new ArrayList<>();
            for (Application application : applications) {
                dtos.add(convertToDTO(application));
            }
            return new Response(1, dtos);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при получении списка заявлений студента: " + e.getMessage());
        }
    }

    @Override
    public Response listDormitoryApplications(Long dormitoryId) {
        try {
            List<Application> applications = ApplicationHibernate.findApplicationsByDormitory(dormitoryId);
            List<ApplicationDTO> dtos = new ArrayList<>();
            for (Application application : applications) {
                dtos.add(convertToDTO(application));
            }
            return new Response(1, dtos);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при получении списка заявлений общежития: " + e.getMessage());
        }
    }

    @Override
    public Response listAllApplications() {
        try {
            List<Application> applications = ApplicationHibernate.getAllApplications();
            List<ApplicationDTO> dtos = new ArrayList<>();
            for (Application application : applications) {
                dtos.add(convertToDTO(application));
            }
            return new Response(1, dtos);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при получении списка всех заявлений: " + e.getMessage());
        }
    }

    private ApplicationDTO convertToDTO(Application application) {
        return new ApplicationDTO(
            application.getId(),
            application.getStudent().getUser().getId(),
            application.getDormitory().getId(),
            application.getApplicationDate(),
            application.getStatus(),
            application.getComment(),
            application.getPreferredRoomType(),
            application.getProcessedBy() != null ? application.getProcessedBy().getId() : null,
            application.getProcessedDate()
        );
    }
} 
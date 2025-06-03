package service_interface;

import connect.Response;
import dto.ApplicationDTO;
import enums.ApplicationStatus;

public interface ApplicationService {
    Response submitApplication(ApplicationDTO applicationDTO);
    Response processApplication(Long id, ApplicationStatus newStatus, String comment, Long processedById);
    Response cancelApplication(Long id);
    Response getApplication(Long id);
    Response listStudentApplications(Long studentId);
    Response listDormitoryApplications(Long dormitoryId);
    Response listAllApplications();
} 
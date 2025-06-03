package dto;

import enums.ApplicationStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

public class ApplicationDTO implements Serializable {
    private Long id;
    private Long studentId;
    private Long dormitoryId;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;
    private String comment;
    private String preferredRoomType;
    private Long processedById;
    private LocalDateTime processedDate;

    public ApplicationDTO() {
    }

    public ApplicationDTO(Long id, Long studentId, Long dormitoryId, LocalDateTime applicationDate,
                         ApplicationStatus status, String comment, String preferredRoomType,
                         Long processedById, LocalDateTime processedDate) {
        this.id = id;
        this.studentId = studentId;
        this.dormitoryId = dormitoryId;
        this.applicationDate = applicationDate;
        this.status = status;
        this.comment = comment;
        this.preferredRoomType = preferredRoomType;
        this.processedById = processedById;
        this.processedDate = processedDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(Long dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPreferredRoomType() {
        return preferredRoomType;
    }

    public void setPreferredRoomType(String preferredRoomType) {
        this.preferredRoomType = preferredRoomType;
    }

    public Long getProcessedById() {
        return processedById;
    }

    public void setProcessedById(Long processedById) {
        this.processedById = processedById;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }
} 
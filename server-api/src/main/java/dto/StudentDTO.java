package dto;

import java.io.Serializable;
import java.time.LocalDate;

public class StudentDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String faculty;
    private String groupNumber;
    private Integer course;
    private Long userId;
    private Long roomId;

    public StudentDTO() {
    }

    public StudentDTO(String firstName, String lastName, String middleName, LocalDate birthDate,
                     String faculty, String groupNumber, Integer course, Long userId, Long roomId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.faculty = faculty;
        this.groupNumber = groupNumber;
        this.course = course;
        this.userId = userId;
        this.roomId = roomId;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
} 
package dto;

import java.io.Serializable;
import java.time.LocalDate;

public class EmployeeDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String position;
    private LocalDate hireDate;
    private String phoneNumber;
    private Long userId;
    private Long dormitoryId;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String firstName, String lastName, String middleName, LocalDate birthDate,
                      String position, LocalDate hireDate, String phoneNumber, Long userId, Long dormitoryId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.position = position;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.dormitoryId = dormitoryId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(Long dormitoryId) {
        this.dormitoryId = dormitoryId;
    }
} 
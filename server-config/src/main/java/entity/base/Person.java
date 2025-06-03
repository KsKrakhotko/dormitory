package entity.base;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
public abstract class Person implements Serializable {
    @Column(name = "first_name", nullable = false, length = 100)
    protected String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    protected String lastName;

    @Column(name = "middle_name", length = 100)
    protected String middleName;

    @Column(name = "birth_date")
    protected LocalDate birthDate;

    public Person() {
    }

    public Person(String firstName, String lastName, String middleName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
    }

    public abstract String getFullName();

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

    @Override
    public String toString() {
        return String.format("%s %s %s", lastName, firstName, middleName != null ? middleName : "");
    }
} 
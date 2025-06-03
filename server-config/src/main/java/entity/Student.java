package entity;

import entity.base.Person;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculty", nullable = false, length = 100)
    private String faculty;

    @Column(name = "group_number", nullable = false, length = 20)
    private String groupNumber;

    @Column(name = "course", nullable = false)
    private Integer course;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String middleName, LocalDate birthDate,
                  String faculty, String groupNumber, Integer course) {
        super(firstName, lastName, middleName, birthDate);
        this.faculty = faculty;
        this.groupNumber = groupNumber;
        this.course = course;
    }

    @Override
    public String getFullName() {
        return String.format("%s %s %s, группа %s", 
            getLastName(), getFirstName(), 
            getMiddleName() != null ? getMiddleName() : "", 
            groupNumber);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
} 
package dto;

import java.io.Serializable;

public class DormitoryDTO implements Serializable {
    private Long id;
    private Integer number;
    private String name;
    private String address;
    private Integer totalCapacity;
    private Integer availablePlaces;

    public DormitoryDTO() {
    }

    public DormitoryDTO(Long id, Integer number, String name, String address, Integer totalCapacity, Integer availablePlaces) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.address = address;
        this.totalCapacity = totalCapacity;
        this.availablePlaces = availablePlaces;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(Integer availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    @Override
    public String toString() {
        return "Общежитие №" + number + " (" + name + ")";
    }
} 
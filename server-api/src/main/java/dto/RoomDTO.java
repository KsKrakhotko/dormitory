package dto;

import java.io.Serializable;

public class RoomDTO implements Serializable {
    private String roomNumber;
    private Integer floor;
    private Integer capacity;
    private Boolean isMale;
    private Long dormitoryId;
    private Integer dormitoryNumber;

    public RoomDTO() {
    }

    public RoomDTO(String roomNumber, Integer floor, Integer capacity, Boolean isMale,
                  Long dormitoryId, Integer dormitoryNumber) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.capacity = capacity;
        this.isMale = isMale;
        this.dormitoryId = dormitoryId;
        this.dormitoryNumber = dormitoryNumber;
    }

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale = isMale;
    }

    public Long getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(Long dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public Integer getDormitoryNumber() {
        return dormitoryNumber;
    }

    public void setDormitoryNumber(Integer dormitoryNumber) {
        this.dormitoryNumber = dormitoryNumber;
    }
} 
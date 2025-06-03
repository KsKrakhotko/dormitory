package org.example.application;

import javafx.beans.property.*;

public class Dormitory {
    private final StringProperty number;
    private final StringProperty address;
    private final IntegerProperty capacity;
    private final IntegerProperty occupied;

    public Dormitory(String number, String address, int capacity, int occupied) {
        this.number = new SimpleStringProperty(number);
        this.address = new SimpleStringProperty(address);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.occupied = new SimpleIntegerProperty(occupied);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public int getCapacity() {
        return capacity.get();
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity.set(capacity);
    }

    public int getOccupied() {
        return occupied.get();
    }

    public IntegerProperty occupiedProperty() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied.set(occupied);
    }
} 
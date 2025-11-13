package com.system.property;

public class Office  extends Property {
    private int workstations;
    private boolean hasMeetingRoom;

    public Office(String name, Address address, double area, int workstations, boolean hasMeetingRoom) {
        super(name, address, area, PropertyType.OFFICE);
        this.workstations = workstations;
        this.hasMeetingRoom = hasMeetingRoom;
    }

    public int getWorkstations() {
        return workstations;
    }

    public boolean hasMeetingRoom() {
        return hasMeetingRoom;
    }

    public boolean isCoworking() {
        return workstations >= 20 && getArea() > 100;
    }

    public boolean isExecutiveOffice() {
        return workstations <= 3 && hasMeetingRoom && getArea() > 50;
    }

}

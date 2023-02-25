package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String mobNo;
    private String password;
    private Cab cab;
    private List<TripBooking> tripBookingList;

    @OneToOne(mappedBy = "driverFk",cascade = CascadeType.ALL)
    private Cab cabFk;

    public Driver() {
        tripBookingList = new ArrayList<>();
    }

    public Driver(int id, String mobNo, String password, Cab cab, List<TripBooking> tripBookingList, Cab cabFk) {
        Id = id;
        this.mobNo = mobNo;
        this.password = password;
        this.cab = cab;
        this.tripBookingList = tripBookingList;
        this.cabFk = cabFk;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }

    public Cab getCabFk() {
        return cabFk;
    }

    public void setCabFk(Cab cabFk) {
        this.cabFk = cabFk;
    }
}

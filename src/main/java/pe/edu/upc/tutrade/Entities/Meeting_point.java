package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Entity
@Table(name="Meeting_point")
public class Meeting_point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMeeting_point;

    @Column(name = "addressMeeting_point",length=200, nullable = false)
    private String addressMeeting_point;

    @Column(name = "latitudeMeeting_point",nullable = false)
    private double latitudeMeeting_point;

    @Column(name = "longitudeMeeting_point",nullable=false)
    private double longitudeMeeting_point;

    @Column(name = "scheduled_atMeeting_point",nullable = false)
    private LocalDate scheduled_atMeeting_point;
    @ManyToOne
    @JoinColumn(name = "idTrade")
    private Trade trade;

    public Meeting_point() {
    }

    public Meeting_point(int idMeeting_point, String addressMeeting_point, double latitudeMeeting_point, double longitudeMeeting_point, LocalDate scheduled_atMeeting_point, Trade trade) {
        this.idMeeting_point = idMeeting_point;
        this.addressMeeting_point = addressMeeting_point;
        this.latitudeMeeting_point = latitudeMeeting_point;
        this.longitudeMeeting_point = longitudeMeeting_point;
        this.scheduled_atMeeting_point = scheduled_atMeeting_point;
        this.trade = trade;
    }

    public int getIdMeeting_point() {
        return idMeeting_point;
    }

    public void setIdMeeting_point(int idMeeting_point) {
        this.idMeeting_point = idMeeting_point;
    }

    public String getAddressMeeting_point() {
        return addressMeeting_point;
    }

    public void setAddressMeeting_point(String addressMeeting_point) {
        this.addressMeeting_point = addressMeeting_point;
    }

    public double getLatitudeMeeting_point() {
        return latitudeMeeting_point;
    }

    public void setLatitudeMeeting_point(double latitudeMeeting_point) {
        this.latitudeMeeting_point = latitudeMeeting_point;
    }

    public double getLongitudeMeeting_point() {
        return longitudeMeeting_point;
    }

    public void setLongitudeMeeting_point(double longitudeMeeting_point) {
        this.longitudeMeeting_point = longitudeMeeting_point;
    }

    public LocalDate getScheduled_atMeeting_point() {
        return scheduled_atMeeting_point;
    }

    public void setScheduled_atMeeting_point(LocalDate scheduled_atMeeting_point) {
        this.scheduled_atMeeting_point = scheduled_atMeeting_point;
    }
}

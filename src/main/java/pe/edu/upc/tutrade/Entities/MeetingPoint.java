package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetingpoint")
public class MeetingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMeetingPoint;

    @Column(name = "addressMeetingPoint", length = 200, nullable = false)
    private String addressMeetingPoint;

    @Column(name = "latitudeMeetingPoint", nullable = false)
    private double latitudeMeetingPoint;

    @Column(name = "longitudeMeetingPoint", nullable = false)
    private double longitudeMeetingPoint;

    @Column(name = "scheduledAtMeetingPoint", nullable = false)
    private LocalDateTime scheduledAtMeetingPoint;

    @ManyToOne
    @JoinColumn(name = "idTrade")
    private Trade trade;

    public MeetingPoint() {
    }

    public MeetingPoint(int idMeetingPoint, String addressMeetingPoint, double latitudeMeetingPoint, double longitudeMeetingPoint, LocalDateTime scheduledAtMeetingPoint, Trade trade) {
        this.idMeetingPoint = idMeetingPoint;
        this.addressMeetingPoint = addressMeetingPoint;
        this.latitudeMeetingPoint = latitudeMeetingPoint;
        this.longitudeMeetingPoint = longitudeMeetingPoint;
        this.scheduledAtMeetingPoint = scheduledAtMeetingPoint;
        this.trade = trade;
    }

    public int getIdMeetingPoint() { return idMeetingPoint; }
    public void setIdMeetingPoint(int idMeetingPoint) { this.idMeetingPoint = idMeetingPoint; }

    public String getAddressMeetingPoint() { return addressMeetingPoint; }
    public void setAddressMeetingPoint(String addressMeetingPoint) { this.addressMeetingPoint = addressMeetingPoint; }

    public double getLatitudeMeetingPoint() { return latitudeMeetingPoint; }
    public void setLatitudeMeetingPoint(double latitudeMeetingPoint) { this.latitudeMeetingPoint = latitudeMeetingPoint; }

    public double getLongitudeMeetingPoint() { return longitudeMeetingPoint; }
    public void setLongitudeMeetingPoint(double longitudeMeetingPoint) { this.longitudeMeetingPoint = longitudeMeetingPoint; }

    public LocalDateTime getScheduledAtMeetingPoint() { return scheduledAtMeetingPoint; }
    public void setScheduledAtMeetingPoint(LocalDateTime scheduledAtMeetingPoint) { this.scheduledAtMeetingPoint = scheduledAtMeetingPoint; }

    public Trade getTrade() { return trade; }
    public void setTrade(Trade trade) { this.trade = trade; }
}

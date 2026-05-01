package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDateTime;

public class MeetingPointResponseDTO {
    private int idMeetingPoint;
    private String address;
    private double latitude;
    private double longitude;
    private LocalDateTime scheduledAt;
    private int tradeId;

    public int getIdMeetingPoint() { return idMeetingPoint; }
    public void setIdMeetingPoint(int idMeetingPoint) { this.idMeetingPoint = idMeetingPoint; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public int getTradeId() { return tradeId; }
    public void setTradeId(int tradeId) { this.tradeId = tradeId; }
}

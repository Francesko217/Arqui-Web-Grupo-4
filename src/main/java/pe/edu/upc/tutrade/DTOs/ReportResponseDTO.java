package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDate;

public class ReportResponseDTO {
    private int idReport;
    private String reason;
    private String description;
    private String status;
    private LocalDate createdAt;
    private int reportedUserId;
    private int reporterUserId;

    public int getIdReport() { return idReport; }
    public void setIdReport(int idReport) { this.idReport = idReport; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public int getReportedUserId() { return reportedUserId; }
    public void setReportedUserId(int reportedUserId) { this.reportedUserId = reportedUserId; }

    public int getReporterUserId() { return reporterUserId; }
    public void setReporterUserId(int reporterUserId) { this.reporterUserId = reporterUserId; }
}

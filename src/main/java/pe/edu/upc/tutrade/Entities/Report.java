package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReport;

    @Column(name = "reasonReport", length = 100, nullable = false)
    private String reasonReport;

    @Column(name = "descriptionReport", length = 500, nullable = false)
    private String descriptionReport;

    @Column(name = "statusReport", length = 20, nullable = false)
    private String statusReport = "PENDING";

    @Column(name = "created_atReport", nullable = false)
    private LocalDate created_atReport;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private User reported;

    public Report() {}

    public int getIdReport() { return idReport; }
    public void setIdReport(int idReport) { this.idReport = idReport; }

    public String getReasonReport() { return reasonReport; }
    public void setReasonReport(String reasonReport) { this.reasonReport = reasonReport; }

    public String getDescriptionReport() { return descriptionReport; }
    public void setDescriptionReport(String descriptionReport) { this.descriptionReport = descriptionReport; }

    public String getStatusReport() { return statusReport; }
    public void setStatusReport(String statusReport) { this.statusReport = statusReport; }

    public LocalDate getCreated_atReport() { return created_atReport; }
    public void setCreated_atReport(LocalDate created_atReport) { this.created_atReport = created_atReport; }

    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }

    public User getReported() { return reported; }
    public void setReported(User reported) { this.reported = reported; }
}

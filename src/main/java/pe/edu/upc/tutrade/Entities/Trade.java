package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name="Trade")
public class Trade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idTrade;

    @Column(name = "statusTrade",length = 20, nullable = false)
    private String statusTrade;

    @Column(name="created_atTrade",nullable = false)
    private LocalDate created_atTrade;

    @Column(name = "completed_atTrade",nullable = false)
    private LocalDate completed_atTrade;

    @ManyToOne
    @JoinColumn(name="proposer_id")
    private User proposer;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private User reciever;

    public Trade() {
    }

    public Trade(int idTrade, User reciever, User proposer, LocalDate completed_atTrade, LocalDate created_atTrade, String statusTrade) {
        this.idTrade = idTrade;
        this.reciever = reciever;
        this.proposer = proposer;
        this.completed_atTrade = completed_atTrade;
        this.created_atTrade = created_atTrade;
        this.statusTrade = statusTrade;
    }

    public int getIdTrade() {
        return idTrade;
    }

    public void setIdTrade(int idTrade) {
        this.idTrade = idTrade;
    }

    public String getStatusTrade() {
        return statusTrade;
    }

    public void setStatusTrade(String statusTrade) {
        this.statusTrade = statusTrade;
    }

    public LocalDate getCreated_atTrade() {
        return created_atTrade;
    }

    public void setCreated_atTrade(LocalDate created_atTrade) {
        this.created_atTrade = created_atTrade;
    }

    public LocalDate getCompleted_atTrade() {
        return completed_atTrade;
    }

    public void setCompleted_atTrade(LocalDate completed_atTrade) {
        this.completed_atTrade = completed_atTrade;
    }

    public User getProposer() {
        return proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }
}
